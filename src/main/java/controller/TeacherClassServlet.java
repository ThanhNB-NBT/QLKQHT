package controller;

import java.io.IOException;
import java.util.List;

import common.AlertManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.bean.Class;
import models.bean.Account;
import models.dao.TeacherClassDAO;

@WebServlet("/TeacherClassServlet")
public class TeacherClassServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account loggedInUser = (Account) session.getAttribute("loggedInUser");

        String teacherID = loggedInUser.getTeacherID();
        String className = request.getParameter("className");

        List<Class> classList = (className != null && !className.trim().isEmpty())
        			? TeacherClassDAO.searchByClassName(className,teacherID)
        			: TeacherClassDAO.getClassesByTeacher(teacherID);

        request.setAttribute("classList", classList);
        request.getRequestDispatcher("/Views/TeacherClassView/teacherClassViews.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	String action = request.getParameter("action");

        if ("update".equals(action)) {
            updateClass(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
        }
    }

    private void updateClass(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String classIDStr = request.getParameter("classID");
        String classTime = request.getParameter("classTime");
        String room = request.getParameter("room");
        String status = request.getParameter("status");
        String maxStudentsStr = request.getParameter("maxStudents");
        String totalLessionsStr = request.getParameter("totalLessions");

        // Chuyển đổi giá trị input
        Integer classID = parseInteger(classIDStr);
        Integer maxStudents = parseInteger(maxStudentsStr);
        Integer totalLessions = parseInteger(totalLessionsStr);

        // Kiểm tra tính hợp lệ của dữ liệu
        if (classID == null || maxStudents == null || totalLessions == null || classTime == null || room == null || status == null) {
            AlertManager.addMessage(request, "Dữ liệu không hợp lệ, vui lòng kiểm tra lại!", false);
            response.sendRedirect("TeacherClassServlet");
            return;
        }

        // Tạo đối tượng Class để cập nhật
        Class cls = new Class();
        cls.setClassID(classID);
        cls.setClassTime(classTime);
        cls.setRoom(room);
        cls.setStatus(status);
        cls.setMaxStudents(maxStudents);
        cls.setTotalLessions(totalLessions);

        // Gọi DAO để cập nhật dữ liệu
        boolean isUpdated = TeacherClassDAO.updateClass(cls);

        // Gửi thông báo dựa trên kết quả
        String message = isUpdated ? "Cập nhật lớp học thành công!" : "Có lỗi khi cập nhật lớp học.";
        AlertManager.addMessage(request, message, isUpdated);

        response.sendRedirect("TeacherClassServlet");
    }

    /**
     * Chuyển đổi chuỗi thành Integer
     */
    private static Integer parseInteger(String value) {
        try {
            return (value != null && !value.trim().isEmpty()) ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
