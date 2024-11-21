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

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public RegisterServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        int roleID = Integer.parseInt(request.getParameter("roleID"));
        
        Role role = new Role(roleID, ""); 
        Account newAccount = new Account(username, password, email, role);
        
        boolean isCreated = AccountDAO.createAccount(newAccount);

        if (isCreated) {
            response.sendRedirect("login.jsp?success=true");
        } else {
            request.setAttribute("error", "Đã xảy ra lỗi trong quá trình đăng ký. Vui lòng thử lại.");
            request.getRequestDispatcher("register.jsp").include(request, response);
        }
	}

}
