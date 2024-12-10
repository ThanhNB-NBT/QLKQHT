package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.bean.Account;
import models.bean.Role;
import models.dao.AccountDAO;
import valid.AccountValidator;

import java.io.IOException;

import common.ImageUtils;
import input.AccountInput;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public RegisterServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AccountInput input = AccountInput.fromRequest(request, false);

	    if (AccountValidator.validateInput(request, input, false)) {
	        response.sendRedirect("/register.jsp");
	        return;
	    }

	    String avatar = ImageUtils.processAvatar(request);
	    Role role = new Role();
	    role.setRoleID(input.getRoleID());

        Account newAccount = new Account(input.getUsername(), input.getPassword(), input.getEmail(), avatar, role);

        boolean isCreated = AccountDAO.createAccount(newAccount);

        if (isCreated) {
            response.sendRedirect("login.jsp?success=true");
        } else {
            request.setAttribute("error", "Đã xảy ra lỗi trong quá trình đăng ký. Vui lòng thử lại.");
            request.getRequestDispatcher("register.jsp").include(request, response);
        }
	}

}
