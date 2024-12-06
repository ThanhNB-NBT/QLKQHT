package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import models.bean.Teacher;
import models.dao.DepartmentDAO;
import models.dao.TeacherDAO;

import common.AlertManager;
import common.ImageUtils;
import common.SessionUtils;
import common.RoleUtils;
import input.TeacherInput;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import valid.TeacherValidator;

@WebServlet("/TeacherServlet")
@MultipartConfig
public class TeacherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String TEACHERID = "teacherID";
    private static final String TEACHER_SERVLET = "TeacherServlet";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (!SessionUtils.isLoggedIn(session)) {
            response.sendRedirect("login.jsp");
            return;
        }

        boolean isAdmin = RoleUtils.isAdmin(session);
        request.setAttribute("isAdmin", isAdmin);

        // Xử lý chỉnh sửa giảng viên
        String teacherIDStr = request.getParameter(TEACHERID);
        if (teacherIDStr != null) {
            try {
                int teacherID = Integer.parseInt(teacherIDStr);
                Teacher teacherToEdit = TeacherDAO.getTeacherById(teacherID);

                if (teacherToEdit != null) {
                    request.setAttribute("teacherToEdit", teacherToEdit);
                    request.getRequestDispatcher("/Views/TeacherView/editTeacherModal.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Giảng viên không tồn tại.");
                }
                return;
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ.");
                return;
            }
        }

        String searchTeacher = request.getParameter("search");
        List<Teacher> teachers = (searchTeacher != null && !searchTeacher.trim().isEmpty())
            ? TeacherDAO.searchTeachersByName(searchTeacher)
            : TeacherDAO.getAllTeachers();

        request.setAttribute("departments", DepartmentDAO.getAllDepartment());
        request.setAttribute("teachers", teachers);
        request.getRequestDispatcher("/Views/TeacherView/teacherViews.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tham số hành động (action) không được gửi.");
            return;
        }

        switch (action) {
            case "create":
                createTeacher(request, response);
                break;
            case "delete":
                deleteTeacher(request, response);
                break;
            case "update":
                updateTeacher(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
                break;
        }
    }

    private void createTeacher(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            TeacherInput input = TeacherInput.fromRequest(request);

            boolean hasErrors = TeacherValidator.validateInput(input, request);
            if (hasErrors) {
                response.sendRedirect(TEACHER_SERVLET);
                return;
            }

            // Xử lý ảnh đại diện
            String avatar = processAvatar(request);


            // Tạo đối tượng Teacher
            Teacher teacher = new Teacher(input.getFirstName(), input.getLastName(), input.getEmail(),
                                           input.getPhone(), input.getDepartmentID(), input.getOffice(),
                                           input.getHireDate(), avatar);

            // Lưu thông tin Teacher vào DB
            boolean success = TeacherDAO.createTeacherWithAccount(teacher);
            String message = success ? "Thêm giảng viên thành công!" : "Có lỗi xảy ra khi thêm giảng viên.";
            AlertManager.addMessage(request, message, success);

            response.sendRedirect(TEACHER_SERVLET);
        } catch (ParseException e) {
        	 // Hiển thị lỗi định dạng ngày chi tiết
            String errorMessage = "Thêm không thành công! Lỗi: " + e.getMessage();
            AlertManager.addMessage(request, errorMessage, false);
            response.sendRedirect(TEACHER_SERVLET);
        }
    }

    private void deleteTeacher(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String teacherIDStr = request.getParameter(TEACHERID);

        if (teacherIDStr == null || teacherIDStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Teacher ID không hợp lệ.");
            return;
        }

        try {
            int teacherID = Integer.parseInt(teacherIDStr);
            boolean success = TeacherDAO.deleteTeacher(teacherID);

            String message = success ? "Giảng viên đã được xóa thành công." : "Có lỗi xảy ra khi xóa giảng viên.";
            AlertManager.addMessage(request, message, success);
            response.sendRedirect(TEACHER_SERVLET);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Teacher ID không hợp lệ.");
        }
    }

    private void updateTeacher(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            TeacherInput input = TeacherInput.fromRequest(request);

            boolean hasErrors = TeacherValidator.validateInput(input, request);
            if (hasErrors) {
                response.sendRedirect(TEACHER_SERVLET);
                return;
            }

            String avatar = processAvatar(request, request.getParameter("currentAvatar"));

            // Cập nhật thông tin giảng viên
            Teacher teacher = new Teacher();
            teacher.setTeacherID(input.getTeacherID());
            teacher.setFirstName(input.getFirstName());
            teacher.setLastName(input.getLastName());
            teacher.setEmail(input.getEmail());
            teacher.setPhone(input.getPhone());
            teacher.setDepartmentID(input.getDepartmentID());
            teacher.setOffice(input.getOffice());
            teacher.setHireDate(input.getHireDate());
            teacher.setAvatar(avatar);

            // Cập nhật trong DB
            boolean success = TeacherDAO.updateTeacher(teacher);
            String message = success ? "Cập nhật giảng viên thành công!" : "Có lỗi xảy ra khi cập nhật giảng viên.";
            AlertManager.addMessage(request, message, success);

            response.sendRedirect(TEACHER_SERVLET);
        } catch (ParseException e) {
            AlertManager.addMessage(request, "Định dạng ngày không hợp lệ.", false);
            response.sendRedirect(TEACHER_SERVLET);
        }
    }

    private String processAvatar(HttpServletRequest request) throws IOException, ServletException {
        return processAvatar(request, "assets/img/user.jpg");
    }

    private String processAvatar(HttpServletRequest request, String defaultAvatar) throws IOException, ServletException {
        try {
        	String uploadDir = "D:/eclipse-workspace/QLKQHT/src/main/webapp/assets/img/profile";
            Part avatarPart = request.getPart("avatar");

            if (avatarPart != null && avatarPart.getSize() > 0) {
                return ImageUtils.processAvatar(avatarPart, uploadDir, true, 150, 150);
            }
        } catch (Exception e) {
            // Log lỗi nếu cần
        }
        return defaultAvatar;
    }
}