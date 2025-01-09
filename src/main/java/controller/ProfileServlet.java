package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.bean.Account;
import models.bean.Student;
import models.bean.Teacher;
import models.dao.StudentDAO;
import models.dao.TeacherDAO;

import java.io.IOException;

import common.RoleUtils;
import common.SessionUtils;


@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ProfileServlet() {
        super();
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Account loggedInUser = (Account) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        request.setAttribute("user", loggedInUser);

        if (RoleUtils.isTeacher(session)) {
        	String teacherIDStr = SessionUtils.getLoggedInAccount(session).getTeacherID();
        	int teacherID = Integer.parseInt(teacherIDStr);
            Teacher teacher = TeacherDAO.getTeacherById(teacherID);
            request.setAttribute("teacher", teacher);
            request.getRequestDispatcher("/Views/Teacher/TeacherProfile/TeacherProfile.jsp").forward(request, response);
        } else if (RoleUtils.isStudent(session)) {
            String studentIDStr = SessionUtils.getLoggedInAccount(session).getStudentID();
            int studentID = Integer.parseInt(studentIDStr);
            Student student = StudentDAO.getStudentById(studentID);
            request.setAttribute("student", student);
            request.getRequestDispatcher("/Views/Student/StudentProfile/StudentProfile.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/errorPage/error404.jsp");
        }
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
