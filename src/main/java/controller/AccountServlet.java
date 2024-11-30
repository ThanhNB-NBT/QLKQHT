package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.bean.Account;
import models.bean.Role;
import models.dao.AccountDAO;
import common.AlertManager;
import common.SessionUtils;
import common.RoleUtils;
import java.io.IOException;
import java.util.List;

@WebServlet("/AccountServlet")
public class AccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ACCOUNTID = "accountID";
	private static final String ACCOUNT_SERVLET = "AccountServlet";

	public AccountServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (!SessionUtils.isLoggedIn(session)) {
			response.sendRedirect("login.jsp");
			return;
		}
		
		boolean isAdmin = RoleUtils.isAdmin(session);
		request.setAttribute("isAdmin", isAdmin);

		String accountIDStr = request.getParameter(ACCOUNTID);
		if (accountIDStr != null) {
			try {
				int accountID = Integer.parseInt(accountIDStr);
				Account accountToEdit = AccountDAO.getAccountById(accountID);

				if (accountToEdit != null) {
					request.setAttribute("accountToEdit", accountToEdit);
					request.getRequestDispatcher("/Views/AccountView/editAccountModal.jsp").forward(request, response);
				} else {
					response.sendError(HttpServletResponse.SC_NOT_FOUND, "Tài khoản không tồn tại.");
				}
				return;
			} catch (NumberFormatException e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ.");
				return;
			}
		}

		String searchAccount = request.getParameter("search");
		 List<Account> accounts = (searchAccount != null && !searchAccount.trim().isEmpty()) 
		            ? AccountDAO.searchAccountByUsername(searchAccount)
		            : AccountDAO.getAllAccounts();
		Account loggedInUser = SessionUtils.getLoggedInAccount(session);
		request.setAttribute("username", loggedInUser.getUsername());
		request.setAttribute("accounts", accounts);
		request.getRequestDispatcher("/Views/AccountView/accountViews.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		switch (action) {
		case "create":
			createAccount(request, response);
			break;
		case "delete":
			deleteAccount(request, response);
			break;
		case "update":
			updateAccount(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
			break;
		}
	}

	private void createAccount(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String username = request.getParameter("name");
		String password = request.getParameter("password");
		String cpass = request.getParameter("cpass");
		String email = request.getParameter("email");

		if (checkAccountInput(request, username, password, cpass, email, true)) {
			response.sendRedirect(ACCOUNT_SERVLET);
			return;
		}

		int roleID = Integer.parseInt(request.getParameter("role"));
		Role role = new Role();
		role.setRoleID(roleID);
		Account account = new Account(username, password, email, role);

		boolean success = AccountDAO.createAccount(account);
		String message = success ? "Tạo tài khoản thành công!" : "Đã có lỗi xảy ra khi tạo tài khoản.";
		AlertManager.addMessage(request, message, success); // success là boolean

		response.sendRedirect(ACCOUNT_SERVLET);
	}

	private void deleteAccount(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String accountIDStr = request.getParameter(ACCOUNTID);

		if (accountIDStr == null || accountIDStr.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Account ID không hợp lệ.");
			return;
		}

		try {
			int accountID = Integer.parseInt(accountIDStr);
			boolean success = AccountDAO.deleteAccount(accountID);

			String message = success ? "Tài khoản đã được xóa thành công." : "Có lỗi xảy ra khi xóa tài khoản.";
			AlertManager.addMessage(request, message, success);
			response.sendRedirect(ACCOUNT_SERVLET);
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Account ID không hợp lệ.");
		}
	}

	private void updateAccount(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int accountID = Integer.parseInt(request.getParameter(ACCOUNTID));
		String username = request.getParameter("name");
		String password = request.getParameter("password");
		String cpass = request.getParameter("cpass");
		String email = request.getParameter("email");

		if (checkAccountInput(request, username, password, cpass, email, false)) {
			return;
		}

		int roleID = Integer.parseInt(request.getParameter("role"));
		Role role = new Role();
		role.setRoleID(roleID);

		Account account = new Account(accountID, username, email, role);

		if (password != null && !password.trim().isEmpty()) {
			if (!password.equals(cpass)) {
				AlertManager.addMessage(request, "Mật khẩu không trùng khớp.", false); // false = Lỗi
				return;
			}

			account.setPassword(password);
		} else {
			Account oldAccount = AccountDAO.getAccountById(accountID);
			account.setPassword(oldAccount.getPassword());
		}

		boolean success = AccountDAO.updateAccount(account);
		String message = success ? "Cập nhật tài khoản thành công!" : "Có lỗi xảy ra khi cập nhật tài khoản.";
		AlertManager.addMessage(request, message, success);

		response.sendRedirect(ACCOUNT_SERVLET);
	}

	private boolean checkAccountInput(HttpServletRequest request, String username, String password, String cpass,
			String email, boolean isCreateAction) {

		boolean hasErrors = false;

		// Kiểm tra tên tài khoản
		if (username == null || username.trim().isEmpty()) {
			AlertManager.addMessage(request, "Tên tài khoản không được để trống.", false);
			hasErrors = true;
		}

		// Kiểm tra email
		if (email == null || email.trim().isEmpty()) {
			AlertManager.addMessage(request, "Email không được để trống.", false);
			hasErrors = true;
		} else if (!isValidEmailFormat(email)) {
			AlertManager.addMessage(request, "Định dạng email không hợp lệ.", false);
			hasErrors = true;
		}

		// Kiểm tra mật khẩu khi tạo tài khoản
		if (isCreateAction) {
			if (password == null || password.isEmpty()) {
				AlertManager.addMessage(request, "Mật khẩu không được để trống.", false);
				hasErrors = true;
			} else if (!password.equals(cpass)) {
				AlertManager.addMessage(request, "Mật khẩu và xác nhận mật khẩu không trùng khớp.", false);
				hasErrors = true;
			}
		} else {
			// Kiểm tra mật khẩu khi cập nhật tài khoản
			if (password != null && !password.trim().isEmpty() && !password.equals(cpass)) {
				AlertManager.addMessage(request, "Mật khẩu và xác nhận mật khẩu không trùng khớp.", false);
				hasErrors = true;
			}
		}

		return hasErrors; // Trả về true nếu có lỗi, false nếu không có lỗi
	}

	private boolean isValidEmailFormat(String email) {
		String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
		return email.matches(emailRegex);
	}
}
