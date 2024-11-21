package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

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

	    if (loggedInAccount.isPresent()) {
            // Tạo session hoặc lấy session hiện tại nếu đã tồn tại
            HttpSession session = request.getSession(false); 

            session.setAttribute("loggedInUser", loggedInAccount.get()); 

            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } else {
            // Đăng nhập thất bại, hiển thị thông báo lỗi
            request.setAttribute("error", "Tài khoản hoặc mật khẩu không chính xác");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
	}

}
