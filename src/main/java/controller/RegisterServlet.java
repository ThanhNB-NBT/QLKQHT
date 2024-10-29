package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.bean.Account;
import models.bean.Role;
import models.dao.AccountDAO;

import java.io.IOException;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        int roleID = Integer.parseInt(request.getParameter("roleID"));
        
        Role role = new Role(roleID, ""); // Thay đổi theo cách lấy thông tin vai trò nếu cần
        Account newAccount = new Account(username, password, email, role);
        
        AccountDAO accountDAO = new AccountDAO();
        boolean isCreated = accountDAO.createAccount(newAccount);

        if (isCreated) {
            response.sendRedirect("login.jsp?success=true");
        } else {
            request.setAttribute("error", "Đã xảy ra lỗi trong quá trình đăng ký. Vui lòng thử lại.");
            request.getRequestDispatcher("register.jsp").include(request, response);
        }
	}

}
