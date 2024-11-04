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
import common.SessionUtils;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@WebServlet("/AccountServlet")
public class AccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AccountServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (!SessionUtils.isLoggedIn(session)) {
			response.sendRedirect("login.jsp");
			return;
		}

		String accountIDStr = request.getParameter("accountID");
		if (accountIDStr != null) {
	        try {
	            int accountID = Integer.parseInt(accountIDStr);
	            Account accountToEdit = AccountDAO.getAccountById(accountID);

	            if (accountToEdit != null) {
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write(convertAccountToJson(accountToEdit));
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
		ArrayList<Account> accounts;
		if (searchAccount != null && !searchAccount.trim().isEmpty()) {
			accounts = AccountDAO.searchAccountByUsername(searchAccount);
		} else {
			accounts = AccountDAO.getAllAccounts();
		}
		Account loggedInUser = SessionUtils.getLoggedInAccount(session);
		request.setAttribute("username", loggedInUser.getUsername());
		request.setAttribute("accounts", accounts);
		request.getRequestDispatcher("Views/accountList.jsp").forward(request, response);
	}

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

	private void createAccount(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("name");
		String password = request.getParameter("password");
		String cpass = request.getParameter("cpass");
		String email = request.getParameter("email");

		if (checkAccountInput(request, response, username, password, cpass, email)) {
			return;
		}

		int roleID = Integer.parseInt(request.getParameter("role"));
		Role role = new Role();
		role.setRoleID(roleID);
		Account account = new Account(username, password, email, role);

		boolean success = AccountDAO.createAccount(account);
		setSessionMessage(request, success, "Tạo tài khoản thành công!", "Đã có lỗi xảy ra khi tạo tài khoản.");

		response.sendRedirect("AccountServlet");
	}

	private void deleteAccount(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String accountIDStr = request.getParameter("accountID");

		if (accountIDStr == null || accountIDStr.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Account ID không hợp lệ.");
			return;
		}

		try {
			int accountID = Integer.parseInt(accountIDStr);
			boolean success = AccountDAO.deleteAccount(accountID);
			setSessionMessage(request, success, "Tài khoản đã được xóa thành công.",
					"Có lỗi xảy ra khi xóa tài khoản.");

			response.sendRedirect("AccountServlet");
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Account ID không hợp lệ.");
		}
	}

	private void updateAccount(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int accountID = Integer.parseInt(request.getParameter("accountID"));
		String username = request.getParameter("name");
		String password = request.getParameter("password");
		String cpass = request.getParameter("cpass");
		String email = request.getParameter("email");

		if (checkAccountInput(request, response, username, password, cpass, email)) {
			return;
		}
		int roleID = Integer.parseInt(request.getParameter("role"));
		Role role = new Role();
		role.setRoleID(roleID);
		Account account = new Account(accountID, username, password, email, role);

		boolean success = AccountDAO.updateAccount(account);
		setSessionMessage(request, success, "Cập nhật tài khoản thành công!", "Có lỗi xảy ra khi cập nhật tài khoản.");

		response.sendRedirect("AccountServlet");
	}

	private boolean checkAccountInput(HttpServletRequest request, HttpServletResponse response, String username,
			String password, String cpass, String email) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (username == null || username.trim().isEmpty()) {
			setSessionError(session, "Tên tài khoản không được để trống.");

			return true;
		}

		if (email == null || email.trim().isEmpty()) {
			setSessionError(session, "Email không được để trống.");

			return true;
		}

		if (!password.equals(cpass)) {
			setSessionError(session, "Mật khẩu và xác nhận mật khẩu không trùng khớp.");

			return true;
		}

		if (!AccountDAO.checkUsername(username)) {
			setSessionError(session, "Tên tài khoản đã tồn tại.");

			return true;
		}

		if (!AccountDAO.checkEmail(email)) {
			setSessionError(session, "Email đã tồn tại.");
//			forwardToAccountList(request, response);
			return true;
		}
		return false;
	}
	private void setSessionError(HttpSession session, String errorMessage) {
	    session.setAttribute("errorModal", errorMessage);
	}

//	private void forwardToAccountList(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		List<Account> accounts = AccountDAO.getAllAccounts();
//		request.setAttribute("accounts", accounts);
//		request.getRequestDispatcher("Views/accountList.jsp").forward(request, response);
//	}

	private void setSessionMessage(HttpServletRequest request, boolean success, String successMessage,
			String errorMessage) {
		request.getSession().setAttribute(success ? "message" : "error", success ? successMessage : errorMessage);
	}
	// Hàm chuyển đổi Account thành JSON
	private String convertAccountToJson(Account account) {
	    return "{ \"accountID\": " + account.getAccountID() +
	           ", \"username\": \"" + account.getUsername() + "\"" +
	           ", \"email\": \"" + account.getEmail() + "\"" +
	           ", \"role\": { \"roleID\": " + account.getRole().getRoleID() + 
	           ", \"role\": \"" + account.getRole().getRole() + "\" } }";
	}

}
