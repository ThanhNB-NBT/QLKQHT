package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.bean.Account;
import models.bean.Role;
import models.dao.AccountDAO;
import common.AlertManager;
import common.ImageUtils;
import common.SessionUtils;
import input.AccountInput;
import common.RoleUtils;
import java.io.IOException;
import java.util.List;
import valid.AccountValidator;

@WebServlet("/AccountServlet")
@MultipartConfig
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

	private void createAccount(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		AccountInput input = AccountInput.fromRequest(request, false);

	    if (AccountValidator.validateInput(request, input, false)) {
	        response.sendRedirect(ACCOUNT_SERVLET);
	        return;
	    }

	    String avatar = ImageUtils.processAvatar(request);
	    Role role = new Role();
	    role.setRoleID(input.getRoleID());

	    Account account = new Account(input.getUsername(), input.getPassword(), input.getEmail(), avatar, role);

		boolean success = AccountDAO.createAccount(account);
		String message = success ? "Tạo tài khoản thành công!" : "Đã có lỗi xảy ra khi tạo tài khoản.";
		AlertManager.addMessage(request, message, success);

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

	private void updateAccount(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		AccountInput input = AccountInput.fromRequest(request, true);

	    if (AccountValidator.validateInput(request, input, true)) {
	        response.sendRedirect(ACCOUNT_SERVLET);
	        return;
	    }
	    String avatar = ImageUtils.processAvatar(request, request.getParameter("currentAvatar"));

		Role role = new Role();
		role.setRoleID(input.getRoleID());

	    Account account = new Account(input.getAccountID(), input.getUsername(), input.getEmail(), role);

	    account.setAvatar(avatar);

	    if (input.getPassword() != null && !input.getPassword().trim().isEmpty()) {
	        if (!input.getPassword().equals(input.getConfirmPassword())) {
	            AlertManager.addMessage(request, "Mật khẩu không trùng khớp.", false);
	            response.sendRedirect(ACCOUNT_SERVLET);
	            return;
	        }
	        account.setPassword(input.getPassword());
	    } else {
	        Account oldAccount = AccountDAO.getAccountById(input.getAccountID());
	        account.setPassword(oldAccount.getPassword());
	    }

		boolean success = AccountDAO.updateAccount(account);
		String message = success ? "Cập nhật tài khoản thành công!" : "Có lỗi xảy ra khi cập nhật tài khoản.";
		AlertManager.addMessage(request, message, success);

		response.sendRedirect(ACCOUNT_SERVLET);
	}
}
