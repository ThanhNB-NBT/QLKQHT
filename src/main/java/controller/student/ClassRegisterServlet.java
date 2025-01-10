package controller.student;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.bean.StudentClass;
import models.dao.ClassDAO;
import models.dao.StudentClassDAO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import common.AlertManager;
import common.SessionUtils;

@WebServlet("/ClassRegisterServlet")
public class ClassRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String REGISTRATION_PAGE = "/Views/Student/ClassRegisterView/classRegisteredViews.jsp";
	private static final String SERVLET = "ClassRegisterServlet";

    public ClassRegisterServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (!SessionUtils.isLoggedIn(session)) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Lấy ID của sinh viên đang đăng nhập
        String studentIDStr = SessionUtils.getLoggedInAccount(session).getStudentID();
        int studentID = Integer.parseInt(studentIDStr);

        // Lấy danh sách lớp có thể đăng ký và đã đăng ký
        List<Map<String, Object>> availableClasses = StudentClassDAO.getAvailableClasses(studentID);
        List<Map<String, Object>> registeredClasses = StudentClassDAO.getRegisteredClasses(studentID);

        request.setAttribute("availableClasses", availableClasses);
        request.setAttribute("registeredClasses", registeredClasses);

        request.getRequestDispatcher(REGISTRATION_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "register":
                registerClass(request, response);
                break;
            case "withdraw":
                withdrawClass(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
                break;
        }
    }

    private void registerClass(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        String studentIDStr = SessionUtils.getLoggedInAccount(session).getStudentID();
        int studentID = Integer.parseInt(studentIDStr);

        try {
            int classID = Integer.parseInt(request.getParameter("classID"));

            // Kiểm tra lớp đã đầy chưa
            if (!validateClassCapacity(classID)) {
                AlertManager.addMessage(request, "Lớp học đã đầy!", false);
                response.sendRedirect(SERVLET);
                return;
            }

            // Kiểm tra trùng lịch
            Integer courseID = ClassDAO.getCourseIDByClassID(classID);
            if (courseID != null && StudentClassDAO.isDuplicate(classID, studentID, courseID)) {
                AlertManager.addMessage(request, "Bạn đã đăng ký lớp học phần này hoặc có lịch học trùng!", false);
                response.sendRedirect(SERVLET);
                return;
            }

            // Tạo đối tượng StudentClass mới
            StudentClass studentClass = new StudentClass();
            studentClass.setStudentID(studentID);
            studentClass.setClassID(classID);
            studentClass.setStatus("Đang học");

            boolean success = StudentClassDAO.createStudentClass(studentClass);
            AlertManager.addMessage(request,
                success ? "Đăng ký lớp học phần thành công!" : "Có lỗi khi đăng ký lớp học phần",
                success);

        } catch (NumberFormatException e) {
            AlertManager.addMessage(request, "Thông tin không hợp lệ!", false);
        }

        response.sendRedirect(SERVLET);
    }

    private void withdrawClass(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        String studentIDStr = SessionUtils.getLoggedInAccount(session).getStudentID();
        int studentID = Integer.parseInt(studentIDStr);

        try {
            int studentClassID = Integer.parseInt(request.getParameter("studentClassID"));

            // Kiểm tra quyền sở hữu
            if (!checkRegistered(studentClassID, studentID)) {
                AlertManager.addMessage(request, "Bạn không có quyền hủy đăng ký lớp học này!", false);
                response.sendRedirect(SERVLET);
                return;
            }

            // Lấy ClassID để cập nhật số lượng sinh viên
            Integer classID = StudentClassDAO.getClassIDByStudentClassID(studentClassID);
            if (classID == null) {
                AlertManager.addMessage(request, "Không tìm thấy thông tin lớp học!", false);
                response.sendRedirect(SERVLET);
                return;
            }

            boolean success = StudentClassDAO.deleteStudentClass(studentClassID, classID);
            AlertManager.addMessage(request,
                success ? "Hủy đăng ký lớp học phần thành công!" : "Có lỗi khi hủy đăng ký lớp học phần",
                success);

        } catch (NumberFormatException e) {
            AlertManager.addMessage(request, "Thông tin không hợp lệ!", false);
        }

        response.sendRedirect(SERVLET);
    }

    private boolean validateClassCapacity(int classID) {
        try {
            return ClassDAO.checkClassCapacity(classID);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkRegistered(int studentClassID, int studentID) {
        try {
            StudentClass sc = StudentClassDAO.getStudentClassById(studentClassID);
            return sc != null && sc.getStudentID() == studentID;
        } catch (Exception e) {
            return false;
        }
    }

}
