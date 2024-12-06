package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

import common.AlertManager;
import models.bean.Account;
import models.dao.AccountDAO;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


    public LoginServlet() {
        super();
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String identifier = request.getParameter("identifier");
	    String password = request.getParameter("password");
	    Account account = new Account(identifier, password);
	    AccountDAO accountDAO = new AccountDAO();
	    Optional<Account> loggedInAccount = accountDAO.checkAccount(account);

	 // Lấy hoặc tạo mới session
	    HttpSession session = request.getSession();

	    if (loggedInAccount.isPresent()) {
	        // Đăng nhập thành công
	        session.setAttribute("loggedInUser", loggedInAccount.get());

	        // Thêm thông báo thành công
	        AlertManager.addMessage(request, "Đăng nhập thành công!", true);

	        // Redirect tới trang index
	        response.sendRedirect(request.getContextPath() + "/index.jsp");
	    } else {
	        // Đăng nhập thất bại
	        AlertManager.addMessage(request, "Tài khoản hoặc mật khẩu không chính xác!", false);

	        // Forward lại trang login
	        request.getRequestDispatcher("login.jsp").forward(request, response);
	    }
	}

}
