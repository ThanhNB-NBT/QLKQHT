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
import java.util.ArrayList;


@WebServlet("/AccountServlet")
public class AccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AccountServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
        if (!SessionUtils.isLoggedIn(session)) {
            // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
            response.sendRedirect("LoginServlet");
            return;
        }

        // Nếu đã đăng nhập, lấy thông tin tài khoản từ session
        Account loggedInUser = SessionUtils.getLoggedInAccount(session);
        request.setAttribute("username", loggedInUser.getUsername());
//Hiển thị
		ArrayList<Account> accounts = (ArrayList<Account>) AccountDAO.getAllAccounts();
        request.setAttribute("accounts", accounts);
        request.getRequestDispatcher("Views/accountList.jsp").forward(request, response);
//Thêm tài khoản
        String username = request.getParameter("name");
        String password = request.getParameter("pass");
        String email = request.getParameter("email");
        String cpass = request.getParameter("cpass");
        int roleID = Integer.parseInt(request.getParameter("role"));
        
        // Tạo đối tượng Role và Account
        Role role = new Role();
        role.setRoleID(roleID);
        Account account = new Account(username, email, password, role);

        // Lưu tài khoản vào database
        boolean success = AccountDAO.createAccount(account);

        if (success) {
            request.setAttribute("message", "Tạo tài khoản thành công!");
        } else {
            request.setAttribute("error", "Đã có lỗi xảy ra khi tạo tài khoản.");
        }

        // Điều hướng lại về trang account.jsp
        request.getRequestDispatcher("accounList.jsp").forward(request, response);     
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
