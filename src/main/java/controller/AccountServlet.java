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

	public AccountServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (!SessionUtils.isLoggedIn(session)) {
			// Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
			response.sendRedirect("login.jsp");
			return;
		}
		
		String accountIDStr = request.getParameter("accountID");
	    if (accountIDStr != null) {
	        int accountID = Integer.parseInt(accountIDStr);
	        Account accountToEdit = AccountDAO.getAccountById(accountID);
	        if (accountToEdit != null) {
	            request.setAttribute("accountToEdit", accountToEdit);
	        } else {
	            // Xử lý khi không tìm thấy tài khoản
	            request.setAttribute("errorMessage", "Tài khoản không tồn tại.");
	        }
	    }
	    
		// Nếu đã đăng nhập, lấy thông tin tài khoản từ session
		Account loggedInUser = SessionUtils.getLoggedInAccount(session);
		request.setAttribute("username", loggedInUser.getUsername());

		// Hiển thị danh sách tài khoản
		List<Account> accounts = AccountDAO.getAllAccounts();
		request.setAttribute("accounts", accounts);
		request.getRequestDispatcher("Views/accountList.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (!SessionUtils.isLoggedIn(session)) {
			response.sendRedirect("login.jsp");
			return;
		}

		String action = request.getParameter("action");

		if ("create".equals(action)) {
			createAccount(request, response);
		} else if ("delete".equals(action)) {
			deleteAccount(request, response);
		} else if ("update".equals(action)) {
			updateAccount(request, response);
		}
	}

	private void createAccount(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("name");
		String password = request.getParameter("pass");
		String cpass = request.getParameter("cpass");
		String email = request.getParameter("email");

		// Kiểm tra đầu vào
	    if (username == null || username.trim().isEmpty()) {
	        request.setAttribute("error", "Tên tài khoản không được để trống.");
	        forwardToAccountList(request, response);
	        return;
	    }
	    
	    if (email == null || email.trim().isEmpty()) {
	        request.setAttribute("error", "Email không được để trống.");
	        forwardToAccountList(request, response);
	        return;
	    }

	    if (!password.equals(cpass)) {
	        request.setAttribute("error", "Mật khẩu và xác nhận mật khẩu không trùng khớp.");
	        forwardToAccountList(request, response);
	        return;
	    }
	 
	    if (!AccountDAO.checkUsername(username)) {
	        request.setAttribute("error", "Tên tài khoản đã tồn tại.");
	        forwardToAccountList(request, response);
	        return;
	    }

	    if (!AccountDAO.checkEmail(email)) {
	        request.setAttribute("error", "Email đã tồn tại.");
	        forwardToAccountList(request, response);
	        return;
	    }

		// Lấy role từ form
		int roleID = Integer.parseInt(request.getParameter("role"));
		Role role = new Role();
		role.setRoleID(roleID);
		Account account = new Account(username, password, email, role);

		// Lưu tài khoản vào database
		boolean success = AccountDAO.createAccount(account);

		// Chuyển tiếp thông điệp thành công hoặc lỗi
		if (success) {
			request.getSession().setAttribute("message", "Tạo tài khoản thành công!");
		} else {
			request.getSession().setAttribute("error", "Đã có lỗi xảy ra khi tạo tài khoản.");
		}

		// Redirect đến trang accountList.jsp
		response.sendRedirect("AccountServlet");
	}

	private void deleteAccount(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String accountIDStr = request.getParameter("accountID");
		if (accountIDStr == null || accountIDStr.isEmpty()) {
			// Xử lý trường hợp accountID rỗng hoặc không tồn tại
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Account ID không hợp lệ.");
			return; // Dừng việc xử lý tiếp
		}

		try {
			int accountID = Integer.parseInt(accountIDStr);
			boolean success = AccountDAO.deleteAccount(accountID);
			request.setAttribute(success ? "message" : "error",
					success ? "Tài khoản đã được xóa thành công." : "Có lỗi xảy ra khi xóa tài khoản.");

			// Hiển thị lại danh sách tài khoản
			response.sendRedirect("AccountServlet");
		} catch (NumberFormatException e) {
			// Xử lý lỗi chuyển đổi số
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Account ID không hợp lệ.");
		}
	}

	private void updateAccount(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int accountID = Integer.parseInt(request.getParameter("accountID"));
		String username = request.getParameter("name");
		String password = request.getParameter("newPassword");
		String cpass = request.getParameter("confirmPassword");
		String email = request.getParameter("email");
		int roleID = Integer.parseInt(request.getParameter("role"));

		// Kiểm tra đầu vào
	    if (username == null || username.trim().isEmpty()) {
	        request.setAttribute("error", "Tên tài khoản không được để trống.");
	        forwardToAccountList(request, response);
	        return;
	    }
	    
	    if (!password.equals(cpass)) {
	        request.setAttribute("error", "Mật khẩu và xác nhận mật khẩu không trùng khớp.");
	        forwardToAccountList(request, response);
	        return;
	    }
	    
	    if (email == null || email.trim().isEmpty()) {
	        request.setAttribute("error", "Email không được để trống.");
	        forwardToAccountList(request, response);
	        return;
	    }
	    
	    if (!AccountDAO.checkUsername(username)) {
	        request.setAttribute("error", "Tên tài khoản đã tồn tại.");
	        forwardToAccountList(request, response);
	        return;
	    }

	    if (!AccountDAO.checkEmail(email)) {
	        request.setAttribute("error", "Email đã tồn tại.");
	        forwardToAccountList(request, response);
	        return;
	    }
		// Tạo đối tượng Role và Account
		Role role = new Role();
		role.setRoleID(roleID);
		Account account = new Account(accountID, username, email, role); 

		// Cập nhật tài khoản vào database
		boolean success = AccountDAO.updateAccount(account);
		request.getSession().setAttribute(success ? "message" : "error",
				success ? "Cập nhật tài khoản thành công!" : "Có lỗi xảy ra khi cập nhật tài khoản.");

		response.sendRedirect("AccountServlet");
	}

	private void forwardToAccountList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Account> accounts = AccountDAO.getAllAccounts();
		request.setAttribute("accounts", accounts);
		request.getRequestDispatcher("Views/accountList.jsp");
	}

}
