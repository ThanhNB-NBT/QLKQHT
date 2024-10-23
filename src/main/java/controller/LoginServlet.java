package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import models.bean.Account;
import models.dao.AccountDAO;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String identifier = request.getParameter("identifier");
	    String password = request.getParameter("password");
	    Account account = new Account(identifier, password);
	    
	    Account loggedInAccount = AccountDAO.checkAccount(account);

	    if (loggedInAccount != null) {
	        // Lưu thông tin vào session
	        HttpSession session = request.getSession();
	        session.setAttribute("loggedInUser", loggedInAccount); // Lưu toàn bộ đối tượng Account vào session

	        request.getRequestDispatcher("index.jsp").forward(request, response);
	    } else {
	        request.setAttribute("error", "Invalid username/email or password.");
	        request.getRequestDispatcher("login.jsp").include(request, response);
	    }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
