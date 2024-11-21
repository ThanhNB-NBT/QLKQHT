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

		String accountIDStr = request.getParameter(ACCOUNTID);
		if (accountIDStr != null) {
	        try {
	            int accountID = Integer.parseInt(accountIDStr);
	            Account accountToEdit = AccountDAO.getAccountById(accountID);

	            if (accountToEdit != null) {
//	                response.setContentType("application/json");
//	                response.setCharacterEncoding("UTF-8");
//	                response.getWriter().write(convertAccountToJson(accountToEdit));
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
		List<Account> accounts;
		if (searchAccount != null && !searchAccount.trim().isEmpty()) {
			accounts = AccountDAO.searchAccountByUsername(searchAccount);
		} else {
			accounts = AccountDAO.getAllAccounts();
		}
		Account loggedInUser = SessionUtils.getLoggedInAccount(session);
		request.setAttribute("username", loggedInUser.getUsername());
		request.setAttribute("accounts", accounts);
		request.getRequestDispatcher("Views/AccountView/accountViews.jsp").forward(request, response);
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

	private void createAccount(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("name");
		String password = request.getParameter("password");
		String cpass = request.getParameter("cpass");
		String email = request.getParameter("email");

		if (checkAccountInput(request, response, username, password, cpass, email, true)) {
			response.sendRedirect(ACCOUNT_SERVLET);
			return;
		}
		
		int roleID = Integer.parseInt(request.getParameter("role"));
		Role role = new Role();
		role.setRoleID(roleID);
		Account account = new Account(username, password, email, role);

		boolean success = AccountDAO.createAccount(account);
		setSessionMessage(request, success, "Tạo tài khoản thành công!", "Đã có lỗi xảy ra khi tạo tài khoản.");

		response.sendRedirect(ACCOUNT_SERVLET);
		
		
	}

	private void deleteAccount(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String accountIDStr = request.getParameter(ACCOUNTID);

		if (accountIDStr == null || accountIDStr.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Account ID không hợp lệ.");
			return;
		}

		try {
			int accountID = Integer.parseInt(accountIDStr);
			boolean success = AccountDAO.deleteAccount(accountID);
			setSessionMessage(request, success, "Tài khoản đã được xóa thành công.",
					"Có lỗi xảy ra khi xóa tài khoản.");

			response.sendRedirect(ACCOUNT_SERVLET);
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Account ID không hợp lệ.");
		}
	}

	private void updateAccount(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int accountID = Integer.parseInt(request.getParameter(ACCOUNTID));
		String username = request.getParameter("name");
		String password = request.getParameter("password");
		String cpass = request.getParameter("cpass");
		String email = request.getParameter("email");

		if (checkAccountInput(request, response, username, password, cpass, email, false)) {
			return;
		}
		int roleID = Integer.parseInt(request.getParameter("role"));
		Role role = new Role();
		role.setRoleID(roleID);
		Account account = new Account(accountID, username, email, role);
		 // Nếu người dùng nhập mật khẩu mới, cập nhật mật khẩu vào đối tượng Account
		if (password != null && !password.trim().isEmpty()) {
	        if (!password.equals(cpass)) {
	            setSessionError(request.getSession(), "Mật khẩu và xác nhận mật khẩu không trùng khớp.");
	            return;
	        }
	        account.setPassword(password); // Cập nhật mật khẩu mới
	    } else {
	        // Nếu không thay đổi mật khẩu, giữ mật khẩu cũ
	        Account oldAccount = AccountDAO.getAccountById(accountID); // Giả sử bạn có phương thức này
	        account.setPassword(oldAccount.getPassword()); // Giữ mật khẩu cũ
	    }
		boolean success = AccountDAO.updateAccount(account);
		setSessionMessage(request, success, "Cập nhật tài khoản thành công!", "Có lỗi xảy ra khi cập nhật tài khoản.");

		response.sendRedirect(ACCOUNT_SERVLET);
	}

	private boolean checkAccountInput(HttpServletRequest request, HttpServletResponse response, String username,
			String password, String cpass, String email, boolean isCreateAction) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (username == null || username.trim().isEmpty()) {
			setSessionError(session, "Tên tài khoản không được để trống.");
			return true;
		}

		if (email == null || email.trim().isEmpty()) {
			setSessionError(session, "Email không được để trống.");
			return true;
		}
		
		if (!isValidEmailFormat(email)) {
	        setSessionError(session, "Định dạng email không hợp lệ.");
	        return true;
	    }
		
		if (isCreateAction) { 
	        if (password == null || password.isEmpty()) {
	            setSessionError(session, "Mật khẩu không được để trống.");
	            return true;
	        }

	        if (!password.equals(cpass)) {
	            setSessionError(session, "Mật khẩu và xác nhận mật khẩu không trùng khớp.");
	            return true;
	        }
	    }

		// Kiểm tra nếu username hoặc email có thay đổi thì kiểm tra sự tồn tại trong DB
		Account accountToEdit = (Account) session.getAttribute("accountToEdit"); // Tải đối tượng account đang chỉnh sửa (nếu có)
		
		// Kiểm tra username
		if (accountToEdit != null && !accountToEdit.getUsername().equals(username) && AccountDAO.checkUsername(username)) {
			setSessionError(session, "Tên tài khoản đã tồn tại.");
			return true;
		}

		// Kiểm tra email
		if (accountToEdit != null && !accountToEdit.getEmail().equals(email) && AccountDAO.checkEmail(email)) {
			setSessionError(session, "Email đã tồn tại.");
			return true;	
		}
		return false;
	}
	private void setSessionError(HttpSession session, String errorMessage) {
	    session.setAttribute("errorModal", errorMessage);
	}

	private void setSessionMessage(HttpServletRequest request, boolean success, String successMessage,
			String errorMessage) {
		HttpSession session = request.getSession();
        if (success) {
            session.setAttribute("message", successMessage);
        } else {
            session.setAttribute("error", errorMessage);
        }
	}
	// Hàm chuyển đổi Account thành JSON
	private String convertAccountToJson(Account account) {
	    return "{ \"accountID\": " + account.getAccountID() +
	           ", \"username\": \"" + account.getUsername() + "\"" +
	           ", \"email\": \"" + account.getEmail() + "\"" +
	           ", \"role\": { \"roleID\": " + account.getRole().getRoleID() + 
	           ", \"role\": \"" + account.getRole().getRole() + "\" } }";
	}
	
	private boolean isValidEmailFormat(String email) {
	    String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
	    return email.matches(emailRegex);
	}

}
