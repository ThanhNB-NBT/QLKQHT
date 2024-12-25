package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.bean.Grade;
import models.bean.Class;
import models.dao.GradeDAO;
import valid.GradeValidator;
import common.AlertManager;
import common.RoleUtils;
import common.SessionUtils;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@WebServlet("/GradeServlet")
public class GradeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(GradeServlet.class.getName());


    public GradeServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Kiểm tra trạng thái đăng nhập
        if (!SessionUtils.isLoggedIn(session)) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Xác định quyền của người dùng
        boolean isAdmin = RoleUtils.isAdmin(session);
        request.setAttribute("isAdmin", isAdmin);

        String classID = request.getParameter("classID");
        String teacherID = isAdmin ? null : SessionUtils.getLoggedInAccount(session).getTeacherID();

        // Lấy danh sách lớp học cho giáo viên
        List<Class> classes = GradeDAO.getClassesByTeacher(teacherID);
        request.setAttribute("classes", classes);

        // Lấy danh sách điểm của lớp nếu có classID
        if (classID != null && !classID.isEmpty()) {
            List<Grade> grades = GradeDAO.getGradesByClassID(classID);
            request.setAttribute("grades", grades);
            request.setAttribute("selectedClassID", classID);
        }

        // Forward đến trang JSP
        request.getRequestDispatcher("/Views/GradeView/gradeViews.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("update".equals(action)) {
            updateGrade(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
        }
    }

    private void updateGrade(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String gradeIDStr = request.getParameter("gradeID");
        String attendanceScoreStr = request.getParameter("attendanceScore");
        String midtermScoreStr = request.getParameter("midtermScore");
        String finalExamScoreStr = request.getParameter("finalExamScore");
        String classID = request.getParameter("classID");

        // Chuyển đổi và kiểm tra giá trị input
        Integer gradeID = parseInteger(gradeIDStr);
        Double attendanceScore = parseDouble(attendanceScoreStr);
        Double midtermScore = parseDouble(midtermScoreStr);
        Double finalExamScore = parseDouble(finalExamScoreStr);

        if (GradeValidator.validateUpdateInput(gradeID, attendanceScore, midtermScore, finalExamScore, request)) {
            response.sendRedirect("GradeServlet?classID=" + classID);
            return;
        }

        // Tạo đối tượng Grade để cập nhật
        Grade grade = new Grade(String.valueOf(gradeID), attendanceScore, midtermScore, finalExamScore);

        // Cập nhật dữ liệu
        boolean success = GradeDAO.updateGrade(grade);
        AlertManager.addMessage(request,
            success ? "Cập nhật điểm thành công!" : "Có lỗi khi cập nhật điểm",
            success
        );
        System.out.println("classID: " + classID);
        // Điều hướng trở lại trang danh sách điểm
        response.sendRedirect("GradeServlet?classID=" + classID);
    }

    private static Double parseDouble(String value) {
        try {
            return (value != null && !value.trim().isEmpty()) ? Double.parseDouble(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Integer parseInteger(String value) {
        try {
            return (value != null && !value.trim().isEmpty()) ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
