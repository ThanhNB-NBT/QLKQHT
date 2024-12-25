package controller;

import java.io.IOException;
import java.util.List;

import common.AlertManager;
import input.StudentClassInput;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.bean.StudentClass;
import models.bean.Account;
import models.dao.StudentClassDAO;
import models.dao.StudentDAO;
import models.dao.TeacherClassDAO;
import models.dao.TeacherStudentClassDAO;
import valid.StudentClassValidator;

@WebServlet("/TeacherStudentClassServlet")
public class TeacherStudentClassServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String TEACHER_STUDENTCLASS_SERVLET = "TeacherStudentClassServlet";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account loggedInUser = (Account) session.getAttribute("loggedInUser");

        // Lấy teacherID từ thông tin người dùng đăng nhập
        String teacherID = loggedInUser.getTeacherID();
        String studentName = request.getParameter("studentName");

        // Lấy danh sách sinh viên theo teacherID hoặc tìm kiếm theo tên sinh viên
        List<StudentClass> studentClasses = (studentName != null && !studentName.trim().isEmpty())
                ? TeacherStudentClassDAO.searchStudentsByTeacherId(studentName, teacherID)
                : TeacherStudentClassDAO.getStudentsByTeacherId(teacherID);

        request.setAttribute("classList", TeacherClassDAO.getClassesByTeacher(teacherID));
        request.setAttribute("students", StudentDAO.getAllStudents());
        request.setAttribute("studentClasses", studentClasses);
        request.getRequestDispatcher("/Views/TeacherStudentClassView/teacherStudentClassViews.jsp").forward(request, response);
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
            response.sendRedirect(TEACHER_STUDENTCLASS_SERVLET);
            return;
        }

        StudentClass studentClass = new StudentClass(input.getClassID(), input.getStudentID(), input.getStatus());
        boolean success = StudentClassDAO.createStudentClass(studentClass);

        AlertManager.addMessage(request, success ? "Thêm sinh viên vào lớp thành công!" : "Có lỗi khi thêm sinh viên vào lớp", success);
        response.sendRedirect(TEACHER_STUDENTCLASS_SERVLET);
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
        response.sendRedirect(TEACHER_STUDENTCLASS_SERVLET);
    }


    private void updateStudentClass(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	StudentClassInput input = StudentClassInput.inputUpdate(request);

        if (StudentClassValidator.validateInput(input, request, true)) {
            response.sendRedirect(TEACHER_STUDENTCLASS_SERVLET);
            return;
        }

        StudentClass updatedStudentClass = new StudentClass(input.getStudentClassID(), input.getStatus());

        boolean success = StudentClassDAO.updateStudentClass(updatedStudentClass);
        AlertManager.addMessage(request, success ? "Cập nhật thông tin sinh viên trong lớp thành công!" : "Có lỗi khi cập nhật thông tin", success);
        response.sendRedirect(TEACHER_STUDENTCLASS_SERVLET);
    }
}
