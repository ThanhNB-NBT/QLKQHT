package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.bean.Class;
import models.bean.Grade;
import models.dao.GradeDAO;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import common.RoleUtils;
import common.SessionUtils;


@WebServlet("/GradeDashboardServlet")
public class GradeDashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public GradeDashboardServlet() {
        super();

    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		String teacherID = RoleUtils.isAdmin(session) ? null : SessionUtils.getLoggedInAccount(session).getTeacherID();

        // Lấy danh sách lớp học
		List<Class> classes = GradeDAO.getClassesByTeacher(teacherID);
	    request.setAttribute("classes", classes);

        // Kiểm tra nếu có classID được chọn
        String selectedClassID = request.getParameter("classID");
        if (selectedClassID != null && !selectedClassID.isEmpty()) {
            List<Grade> gradeList = GradeDAO.getGradesByClassID(selectedClassID);

            // Tính toán thống kê
            if (!gradeList.isEmpty()) {
                // Thống kê theo điểm chữ
                Map<String, Long> gradeDistribution = gradeList.stream()
                    .collect(Collectors.groupingBy(Grade::getGradeLetter, Collectors.counting()));

                // Tính điểm trung bình của lớp
                double classAverage = gradeList.stream()
                    .mapToDouble(Grade::getComponentScore)
                    .average()
                    .orElse(0.0);

                // Tìm điểm cao nhất và thấp nhất
                double highestScore = gradeList.stream()
                    .mapToDouble(Grade::getComponentScore)
                    .max()
                    .orElse(0.0);

                double lowestScore = gradeList.stream()
                    .mapToDouble(Grade::getComponentScore)
                    .min()
                    .orElse(0.0);

                // Tính tỷ lệ đạt (điểm D trở lên)
                long passingCount = gradeList.stream()
                    .filter(g -> !g.getGradeLetter().equals("F"))
                    .count();
                double passRate = (double) passingCount / gradeList.size() * 100;

                // Đặt các thuộc tính để hiển thị
                request.setAttribute("gradeList", gradeList);
                request.setAttribute("gradeDistribution", gradeDistribution);
                request.setAttribute("classAverage", String.format("%.2f", classAverage));
                request.setAttribute("highestScore", String.format("%.1f", highestScore));
                request.setAttribute("lowestScore", String.format("%.1f", lowestScore));
                request.setAttribute("passRate", String.format("%.1f", passRate));
                request.setAttribute("selectedClassID", selectedClassID);
            }
        }

        // Forward đến trang JSP
        request.getRequestDispatcher("/index.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
