package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import models.bean.Account;
import models.bean.Role;
import models.bean.Student;
import models.dao.AccountDAO;
import models.dao.DepartmentDAO;
import models.dao.StudentDAO;

import common.AlertManager;
import common.ExcelImportUtils;
import common.ImageUtils;
import common.SessionUtils;
import input.StudentInput;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
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

        try {

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
            case "import":
                importStudents(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
                break;
        }
    }

    private void createStudent(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            StudentInput input = StudentInput.fromRequest(request);

            boolean hasErrors = StudentValidator.validateInput(input, request);
            if (hasErrors) {
                AlertManager.addMessage(request, "Dữ liệu nhập vào không hợp lệ, vui lòng kiểm tra lại.", false);
                response.sendRedirect(STUDENT_SERVLET);
                return;
            }

            boolean success = handleCreateStudent(input, request);

            String message = success ? "Thêm sinh viên thành công!" : "Có lỗi xảy ra khi thêm sinh viên.";
            AlertManager.addMessage(request, message, success);
            response.sendRedirect(STUDENT_SERVLET);

        } catch (Exception e) {
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

            String avatar = ImageUtils.processAvatar(request, request.getParameter("currentAvatar"));
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
            student.setDepartmentID(input.getDepartmentID());
            student.setStudentID(input.getStudentID());
            student.setAccountID(input.getAccountID());

            boolean success = StudentDAO.updateStudent(student);
            String message = success ? "Cập nhật sinh viên thành công!" : "Có lỗi xảy ra khi cập nhật sinh viên.";
            AlertManager.addMessage(request, message, success);

            boolean avatarUpdated = AccountDAO.updateAvatar(student.getAccountID(), avatar);
            if (avatarUpdated) {
                AlertManager.addMessage(request, "Cập nhật ảnh đại diện thành công!", true);
            } else {
                AlertManager.addMessage(request, "Có lỗi xảy ra khi cập nhật ảnh đại diện.", false);
            }

            response.sendRedirect(STUDENT_SERVLET);
        } catch (Exception e) {
            AlertManager.addMessage(request, "Định dạng ngày không hợp lệ.", false);
            response.sendRedirect(STUDENT_SERVLET);
        }
    }

    private boolean handleCreateStudent(StudentInput input, HttpServletRequest request) throws Exception {
        // 1. Tạo mã sinh viên
        String studentCode = input.getStudentCode();
        if (studentCode == null || studentCode.trim().isEmpty()) {
            studentCode = generateStudentCode(input, request);
            if (studentCode == null) {
                return false; // Thông báo lỗi đã được xử lý trong generateStudentCode
            }
        }

        // 2. Xử lý ảnh đại diện
        String avatar = ImageUtils.processAvatar(request);
        if (avatar == null || avatar.isEmpty()) {
            AlertManager.addMessage(request, "Ảnh đại diện không hợp lệ, vui lòng thử lại.", false);
            return false;
        }

        // 3. Tạo tài khoản
        String password = new SimpleDateFormat("dd/MM/yyyy").format(input.getDateOfBirth());
        Role role = new Role(3);
        Account account = new Account(studentCode, password, input.getEmail(), avatar, role);
        int accountID = AccountDAO.createAccountAndReturnID(account);

        if (accountID <= 0) {
            AlertManager.addMessage(request, "Có lỗi xảy ra khi tạo tài khoản.", false);
            return false;
        }

        // 4. Tạo đối tượng Student
        Student student = new Student(
                input.getFirstName(),
                input.getLastName(),
                input.getDateOfBirth(),
                input.getEmail(),
                input.getPhone(),
                input.getAddress(),
                input.getEnrollmentYear(),
                input.getMajorName(),
                studentCode,
                input.getDepartmentID()
        );
        student.setAccountID(accountID);

        // 5. Lưu vào DB
        return StudentDAO.createStudent(student);
    }

    private String generateStudentCode(StudentInput input, HttpServletRequest request) {
        try {
            Student tempStudent = new Student(
                    input.getFirstName(),
                    input.getLastName(),
                    input.getDateOfBirth(),
                    input.getEmail(),
                    input.getPhone(),
                    input.getAddress(),
                    input.getEnrollmentYear(),
                    input.getMajorName(),
                    null,
                    input.getDepartmentID()
            );

            String studentCode = tempStudent.generateStudentCode(input.getMajorName(), input.getEnrollmentYear(),
                    input.getDateOfBirth(), null);

            if (StudentDAO.checkStudentCode(studentCode)) {
                AlertManager.addMessage(request, "Không thể tạo mã sinh viên tự động vì mã bị trùng. Vui lòng nhập mã khác.", false);
                return null;
            }

            return studentCode;
        } catch (RuntimeException e) {
            AlertManager.addMessage(request, "Không thể tạo mã sinh viên duy nhất. Vui lòng thử lại.", false);
            return null;
        }
    }

    private void importStudents(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Part filePart = request.getPart("importFile");
            InputStream fileContent = filePart.getInputStream();

            List<StudentInput> inputs = ExcelImportUtils.importStudentsFromExcel(fileContent);

            int successCount = 0;
            for (StudentInput input : inputs) {

                if (handleCreateStudent(input, request)) {
                    successCount++;
                }
            }

            // Thông báo kết quả
            AlertManager.addMessage(request, "Nhập dữ liệu sinh viên thành công: " + successCount + " sinh viên.", true);
            response.sendRedirect(STUDENT_SERVLET);
        } catch (Exception e) {
            AlertManager.addMessage(request, "Có lỗi khi nhập dữ liệu: " + e.getMessage(), false);
            response.sendRedirect(STUDENT_SERVLET);
        }
    }
}
