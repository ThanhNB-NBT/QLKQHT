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
import common.RoleUtils;
import models.bean.Account;
import models.dao.AccountDAO;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


    public LoginServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String identifier = request.getParameter("identifier"); // Username hoặc email
        String password = request.getParameter("password");

        if (identifier == null || password == null || identifier.trim().isEmpty() || password.trim().isEmpty()) {
            AlertManager.addMessage(request, "Vui lòng nhập đầy đủ thông tin đăng nhập!", false);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        Account account = new Account(identifier, password);
        AccountDAO accountDAO = new AccountDAO();
        Optional<Account> loggedInAccount = accountDAO.checkAccount(account);

        HttpSession session = request.getSession();

        if (loggedInAccount.isPresent()) {
            session.setAttribute("loggedInUser", loggedInAccount.get());

            String roleMessage;
            if (RoleUtils.isAdmin(session)) {
                roleMessage = "Đăng nhập thành công với vai trò Quản trị viên.";
            } else if (RoleUtils.isTeacher(session)) {
                roleMessage = "Đăng nhập thành công với vai trò Giảng viên.";
            } else if (RoleUtils.isStudent(session)) {
                roleMessage = "Đăng nhập thành công với vai trò Sinh viên.";
            } else {
                session.invalidate();
                AlertManager.addMessage(request, "Vai trò không hợp lệ. Vui lòng liên hệ quản trị viên!", false);
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            session.setAttribute("isAdmin", RoleUtils.isAdmin(session));
            session.setAttribute("isTeacher", RoleUtils.isTeacher(session));
            session.setAttribute("isStudent", RoleUtils.isStudent(session));

            AlertManager.addMessage(request, roleMessage, true);
            response.sendRedirect(request.getContextPath() + "/GradeDashboardServlet");
        } else {
            // Đăng nhập thất bại
            AlertManager.addMessage(request, "Tài khoản hoặc mật khẩu không chính xác!", false);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Chuyển hướng GET sang trang login
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

}
