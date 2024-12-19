package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.bean.Account;
import models.bean.StudentClass;
import models.dao.StudentClassDAO;
import models.dao.ClassDAO;
import models.dao.StudentDAO;
import valid.StudentClassValidator;
import common.AlertManager;
import common.RoleUtils;
import common.SessionUtils;
import input.StudentClassInput;

import java.io.IOException;
import java.util.List;

@WebServlet("/StudentClassServlet")
public class StudentClassServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String STUDENTCLASS_SERVLET = "StudentClassServlet";

    public StudentClassServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (!SessionUtils.isLoggedIn(session)) {
            response.sendRedirect("login.jsp");
            return;
        }

        boolean isAdmin = RoleUtils.isAdmin(session);
        request.setAttribute("isAdmin", isAdmin);

        String studentClassIDStr = request.getParameter("studentClassID");

        if (studentClassIDStr != null && !studentClassIDStr.isEmpty()) {
            int studentClassID = Integer.parseInt(studentClassIDStr);

            // Truy vấn dữ liệu từ database
            StudentClass studentClass = StudentClassDAO.getStudentClassById(studentClassID);

            if (studentClass != null) {
                // Gán dữ liệu vào request
                request.setAttribute("editStudentClass", studentClass);
            }
        }

        String searchStudentClass = request.getParameter("search");
        List<StudentClass> studentClasses = (searchStudentClass != null && !searchStudentClass.trim().isEmpty())
            ? StudentClassDAO.searchByStudentOrClass(searchStudentClass)
            : StudentClassDAO.getAllStudentClasses();

        request.setAttribute("studentClasses", studentClasses);
        request.setAttribute("classes", ClassDAO.getAllClasses());
        request.setAttribute("students", StudentDAO.getAllStudents());

        Account loggedInUser = SessionUtils.getLoggedInAccount(session);
        request.setAttribute("username", loggedInUser.getUsername());

        request.getRequestDispatcher("/Views/StudentClassView/studentClassViews.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "create":
                createStudentClass(request, response);
                break;
            case "delete":
                deleteStudentClass(request, response);
                break;
            case "update":
                updateStudentClass(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
                break;
        }
    }

    private void createStudentClass(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StudentClassInput input = StudentClassInput.inputCreate(request);

        if (StudentClassValidator.validateInput(input, request, false)) {
            response.sendRedirect(STUDENTCLASS_SERVLET);
            return;
        }

        StudentClass studentClass = new StudentClass(input.getClassID(), input.getStudentID(), input.getStatus());
        boolean success = StudentClassDAO.createStudentClass(studentClass);

        AlertManager.addMessage(request, success ? "Thêm sinh viên vào lớp thành công!" : "Có lỗi khi thêm sinh viên vào lớp", success);
        response.sendRedirect(STUDENTCLASS_SERVLET);
    }

    private void deleteStudentClass(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String studentClassIDStr = request.getParameter("studentClassID");

        if (studentClassIDStr == null || studentClassIDStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thông tin không hợp lệ.");
            return;
        }

        Integer studentClassID = Integer.parseInt(studentClassIDStr);

        Integer classID = StudentClassDAO.getClassIDByStudentClassID(studentClassID);

        if (classID == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy lớp tương ứng.");
            return;
        }

        boolean success = StudentClassDAO.deleteStudentClass(studentClassID, classID);

        AlertManager.addMessage(request, success ? "Xóa sinh viên khỏi lớp thành công!" : "Có lỗi khi xóa sinh viên khỏi lớp", success);
        response.sendRedirect(STUDENTCLASS_SERVLET);
    }


    private void updateStudentClass(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	StudentClassInput input = StudentClassInput.inputUpdate(request);

        if (StudentClassValidator.validateInput(input, request, true)) {
            response.sendRedirect(STUDENTCLASS_SERVLET);
            return;
        }

        StudentClass updatedStudentClass = new StudentClass(input.getStudentClassID(), input.getStatus());

        boolean success = StudentClassDAO.updateStudentClass(updatedStudentClass);
        AlertManager.addMessage(request, success ? "Cập nhật thông tin sinh viên trong lớp thành công!" : "Có lỗi khi cập nhật thông tin", success);
        response.sendRedirect(STUDENTCLASS_SERVLET);
    }
}
