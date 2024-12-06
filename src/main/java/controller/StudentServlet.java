package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import models.bean.Student;
import models.dao.DepartmentDAO;
import models.dao.StudentDAO;

import common.AlertManager;
import common.ImageUtils;
import common.SessionUtils;
import common.RoleUtils;
import input.StudentInput;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import valid.StudentValidator;

@WebServlet("/StudentServlet")
@MultipartConfig
public class StudentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String STUDENTID = "studentID";
    private static final String STUDENT_SERVLET = "StudentServlet";

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

        try {

        	String studentIDStr = request.getParameter(STUDENTID);
            if (studentIDStr != null) {
                handleEditRequest(request, response, studentIDStr);
                return;
            }

            String searchName = request.getParameter("search");
            String majorName = request.getParameter("major");
            String address = request.getParameter("address");

            boolean noCriteria = (searchName == null || searchName.trim().isEmpty()) &&
                                 (majorName == null || majorName.trim().isEmpty()) &&
                                 (address == null || address.trim().isEmpty());

            List<Student> students;

            if (noCriteria) {
                students = StudentDAO.getAllStudents();
            } else {
                students = StudentDAO.searchStudents(searchName, majorName, address);
                if (students.isEmpty()) {
                    request.setAttribute("searchMessage", "Không tìm thấy sinh viên nào phù hợp với tiêu chí tìm kiếm.");
                }
            }

            request.setAttribute("students", students);
            request.setAttribute("departments", DepartmentDAO.getAllDepartment());
            request.getRequestDispatcher("/Views/StudentView/studentViews.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Có lỗi xảy ra: " + e.getMessage());
        }
    }


    private void handleEditRequest(HttpServletRequest request, HttpServletResponse response, String studentIDStr)
            throws ServletException, IOException {
        try {
            int studentID = Integer.parseInt(studentIDStr);
            Student studentToEdit = StudentDAO.getStudentById(studentID);

            if (studentToEdit != null) {
                request.setAttribute("studentToEdit", studentToEdit);
                request.getRequestDispatcher("/Views/StudentView/editStudentModal.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Sinh viên không tồn tại.");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ.");
        }
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
                createStudent(request, response);
                break;
            case "delete":
                deleteStudent(request, response);
                break;
            case "update":
                updateStudent(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
                break;
        }
    }

    private void createStudent(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            // Lấy dữ liệu từ form
            StudentInput input = StudentInput.fromRequest(request);

            // Kiểm tra dữ liệu đầu vào
            boolean hasErrors = StudentValidator.validateInput(input, request);
            if (hasErrors) {
                // Thêm thông báo lỗi nếu có và chuyển hướng về trang quản lý sinh viên
                AlertManager.addMessage(request, "Dữ liệu nhập vào không hợp lệ, vui lòng kiểm tra lại.", false);
                response.sendRedirect(STUDENT_SERVLET);
                return;
            }

            // Xử lý ảnh đại diện
            String avatar = processAvatar(request);

            Date birthDay = input.getDateOfBirth();
            Date enrollment = input.getEnrollmentYear();
            String majorName = input.getMajorName();
            String studentCode = input.getStudentCode();

            if(studentCode == null || studentCode.trim().isEmpty()) {
            	Student tempStudent = new Student(input.getFirstName(), input.getLastName(), input.getDateOfBirth(),
                        input.getEmail(), input.getPhone(), input.getAddress(), input.getEnrollmentYear(), input.getMajorName(), avatar,"", input.getDepartmentID());

            	studentCode = tempStudent.generateStudentCode(majorName, birthDay, enrollment, null);
            	if (StudentDAO.checkStudentCode(studentCode)) {
    	            AlertManager.addMessage(request, "Không thể tạo mã tự động vì mã bị trùng. Vui lòng nhập mã khác.", false);
    	            response.sendRedirect(STUDENT_SERVLET);
    	            return;
    	        }
            }

            Student student = new Student(input.getFirstName(), input.getLastName(), input.getDateOfBirth(),
                    input.getEmail(), input.getPhone(), input.getAddress(), input.getEnrollmentYear(), input.getMajorName(), avatar, studentCode, input.getDepartmentID());

            // Thêm sinh viên vào cơ sở dữ liệu
            boolean success = StudentDAO.createStudentWithAccount(student);

            // Thêm thông báo thành công hoặc thất bại
            String message = success ? "Thêm sinh viên thành công!" : "Có lỗi xảy ra khi thêm sinh viên.";
            AlertManager.addMessage(request, message, success);

            // Chuyển hướng lại trang danh sách sinh viên
            response.sendRedirect(STUDENT_SERVLET);
        } catch (Exception e) {
            // Xử lý các lỗi không mong muốn
            AlertManager.addMessage(request, "Có lỗi xảy ra: " + e.getMessage(), false);
            response.sendRedirect(STUDENT_SERVLET);
        }
    }

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String studentIDStr = request.getParameter(STUDENTID);

        if (studentIDStr == null || studentIDStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Student ID không hợp lệ.");
            return;
        }

        try {
            int studentID = Integer.parseInt(studentIDStr);
            boolean success = StudentDAO.deleteStudent(studentID);

            String message = success ? "Sinh viên đã được xóa thành công." : "Có lỗi xảy ra khi xóa sinh viên.";
            AlertManager.addMessage(request, message, success);
            response.sendRedirect(STUDENT_SERVLET);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Student ID không hợp lệ.");
        }
    }

    private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            StudentInput input = StudentInput.fromRequest(request);

            boolean hasErrors = StudentValidator.validateInput(input, request);
            if (hasErrors) {
                response.sendRedirect(STUDENT_SERVLET);
                return;
            }

            String avatar = processAvatar(request, request.getParameter("currentAvatar"));
            if (avatar == null || avatar.isEmpty()) {
                avatar = request.getParameter("currentAvatar");
            }

            Student student = new Student();

            student.setFirstName(input.getFirstName());
            student.setLastName(input.getLastName());
            student.setDateOfBirth(input.getDateOfBirth());
            student.setEmail(input.getEmail());
            student.setPhone(input.getPhone());
            student.setAddress(input.getAddress());
            student.setEnrollmentYear(input.getEnrollmentYear());
            student.setMajorName(input.getMajorName());
            student.setAvatar(avatar);
            student.setDepartmentID(input.getDepartmentID());
            student.setStudentID(input.getStudentID());

            boolean success = StudentDAO.updateStudent(student);
            String message = success ? "Cập nhật sinh viên thành công!" : "Có lỗi xảy ra khi cập nhật sinh viên.";
            AlertManager.addMessage(request, message, success);

            response.sendRedirect(STUDENT_SERVLET);
        } catch (Exception e) {
            AlertManager.addMessage(request, "Định dạng ngày không hợp lệ.", false);
            response.sendRedirect(STUDENT_SERVLET);
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
            System.out.print("Lỗi: " + e);
        }
        return defaultAvatar;
    }
}
