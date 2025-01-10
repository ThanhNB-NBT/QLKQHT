package controller.student;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.bean.Grade;
import models.dao.GradeDAO;

import java.io.IOException;
import java.util.List;

import common.SessionUtils;

@WebServlet("/StudentGradeServlet")
public class StudentGradeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public StudentGradeServlet() {
        super();
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Lấy studentID từ session
        HttpSession session = request.getSession();
        String studentID = SessionUtils.getLoggedInAccount(session).getStudentID();

        if (studentID == null) {
            // Nếu không có studentID trong session, chuyển hướng về trang đăng nhập
            response.sendRedirect("login.jsp");
            return;
        }

        // Gọi DAO để lấy danh sách điểm của sinh viên
        List<Grade> gradeList = GradeDAO.getGradesByStudentID(studentID);

        // Đưa danh sách điểm vào request để hiển thị trên JSP
        request.setAttribute("gradeList", gradeList);

        // Chuyển tiếp đến trang hiển thị danh sách điểm
        request.getRequestDispatcher("/Views/Student/StudentGrade.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
