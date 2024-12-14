package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.bean.Account;
import models.bean.Role;
import models.bean.Teacher;
import models.dao.DepartmentDAO;
import models.dao.TeacherDAO;
import models.dao.AccountDAO;

import common.AlertManager;
import common.ImageUtils;
import common.SessionUtils;
import common.RoleUtils;
import input.TeacherInput;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Logger;

import valid.TeacherValidator;

@WebServlet("/TeacherServlet")
@MultipartConfig
public class TeacherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String TEACHERID = "teacherID";
    private static final String TEACHER_SERVLET = "TeacherServlet";
    private static final Logger logger = Logger.getLogger(TeacherServlet.class.getName());

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

        String searchName = request.getParameter("searchName");
        String searchEmail = request.getParameter("searchEmail");

        boolean noCriteria = (searchName == null || searchName.trim().isEmpty()) &&
                             (searchEmail == null || searchEmail.trim().isEmpty());

        List<Teacher> teachers;
        if (noCriteria) {
            teachers = TeacherDAO.getAllTeachers();
        } else {
            teachers = TeacherDAO.searchTeachers(searchName, searchEmail);
            if (teachers.isEmpty()) {
                AlertManager.addMessage(request, "Không tìm thấy dữ liệu", false);
            }
        }

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

            String avatar = ImageUtils.processAvatar(request);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String password = dateFormat.format(input.getHireDate());
            String username = TeacherInput.generateUsernameForTeacher(input.getFirstName(), input.getLastName());
            Role role = new Role(2);
            Account account = new Account( username, password, input.getEmail(), avatar, role);
            int accountID = AccountDAO.createAccountAndReturnID(account);

            if (accountID <= 0) {
                AlertManager.addMessage(request, "Có lỗi xảy ra khi tạo tài khoản.", false);
                response.sendRedirect(TEACHER_SERVLET);
                return;
            }
            // Tạo đối tượng Teacher
            Teacher teacher = new Teacher(input.getFirstName(), input.getLastName(), input.getEmail(),
                                           input.getPhone(), input.getDepartmentID(), input.getOffice(),
                                           input.getHireDate());
            teacher.setAccountID(accountID);

            boolean success = TeacherDAO.createTeacher(teacher);
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

            String avatar = ImageUtils.processAvatar(request, request.getParameter("currentAvatar"));
            logger.info("Processing avatar. Current avatar path: " + request.getParameter("currentAvatar") + ", new avatar: " + avatar);
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
            teacher.setAccountID(input.getAccountID());

            boolean success = TeacherDAO.updateTeacher(teacher);

            int accountID = teacher.getAccountID();

            if (avatar != null && !avatar.isEmpty()) {
                boolean avatarUpdated = AccountDAO.updateAvatar(accountID, avatar);
                if (!avatarUpdated) {

                    AlertManager.addMessage(request, "Cập nhật avatar không thành công.", false);
                }
            }

            String message = success ? "Cập nhật giảng viên thành công!" : "Có lỗi xảy ra khi cập nhật giảng viên.";
            AlertManager.addMessage(request, message, success);

            boolean avatarUpdated = AccountDAO.updateAvatar( teacher.getAccountID(), avatar);
            if (avatarUpdated) {
                AlertManager.addMessage(request, "Cập nhật ảnh đại diện thành công!", true);
            } else {

                AlertManager.addMessage(request, "Có lỗi xảy ra khi cập nhật ảnh đại diện.", false);
            }

            response.sendRedirect(TEACHER_SERVLET);
        } catch (ParseException e) {
            AlertManager.addMessage(request, "Định dạng ngày không hợp lệ.", false);
            response.sendRedirect(TEACHER_SERVLET);
        }
    }
}
