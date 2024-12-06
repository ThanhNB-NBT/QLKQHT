package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.bean.Account;
import models.bean.Course;
import models.dao.CourseDAO;
import models.dao.DepartmentDAO;
import valid.CourseValidator;
import common.AlertManager;
import common.RoleUtils;

import java.io.IOException;
import java.util.List;

import common.SessionUtils;
import input.CourseInput;


@WebServlet("/CourseServlet")
public class CourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String COURSEID = "courseID";
    private static final String COURSE_SERVLET = "CourseServlet";

    public CourseServlet() {
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

        String courseIDStr = request.getParameter(COURSEID);
        if (courseIDStr != null) {
            try {
                int courseID = Integer.parseInt(courseIDStr);
                Course courseToEdit = CourseDAO.getCourseById(courseID);

                if (courseToEdit != null) {
                   request.setAttribute("courseToEdit", courseToEdit);
                   request.getRequestDispatcher("/Views/CourseView/courseViews.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Khóa học không tồn tại.");
                }
                return;
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ.");
                return;
            }
        }

        String searchCourse = request.getParameter("search");
        List<Course> courses = (searchCourse != null && !searchCourse.trim().isEmpty())
            ? CourseDAO.searchCoursesByName(searchCourse) :  CourseDAO.getAllCourse();
        request.setAttribute("courses", courses);
        request.setAttribute("departments", DepartmentDAO.getAllDepartment());
        Account loggedInUser = SessionUtils.getLoggedInAccount(session);
		request.setAttribute("username", loggedInUser.getUsername());
        request.getRequestDispatcher("/Views/CourseView/courseViews.jsp").forward(request, response);
	}


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String action = request.getParameter("action");

        switch (action) {
            case "create":
                createCourse(request, response);
                break;
            case "delete":
                deleteCourse(request, response);
                break;
            case "update":
                updateCourse(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
                break;
        }
	}

	private void createCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    CourseInput input = CourseInput.fromRequest(request, false);

	    if (CourseValidator.validateInput(input, request, false)) {
	        response.sendRedirect(COURSE_SERVLET);
	        return;
	    }

	    // Nếu không có courseCode, tự động tạo
	    String courseCode = input.getCourseCode();
	    if (courseCode == null || courseCode.trim().isEmpty()) {
	        Course tempCourse = new Course(input.getCourseName(), input.getCredits(),
	                input.getDepartmentID(), "", input.getCourseType(), input.getStatus());
	        courseCode = tempCourse.generateCourseCode(input.getDepartmentID(), null);

	        // Kiểm tra mã tự động sinh có bị trùng không
	        if (CourseDAO.checkCourseCode(courseCode)) {
	            AlertManager.addMessage(request, "Không thể tạo mã tự động vì mã bị trùng. Vui lòng nhập mã khác.", false);
	            response.sendRedirect(COURSE_SERVLET);
	            return;
	        }
	    }

	    // Tạo đối tượng Course từ input
	    Course course = new Course(input.getCourseName(), input.getCredits(),
	            input.getDepartmentID(), courseCode, input.getCourseType(), input.getStatus());

	    boolean success = CourseDAO.createCourse(course);
	    AlertManager.addMessage(request, success ? "Thêm học phần thành công!" : "Có lỗi khi thêm học phần", success);
	    response.sendRedirect(COURSE_SERVLET);
	}



    private void deleteCourse(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String courseIdStr = request.getParameter(COURSEID);
        if (courseIdStr == null || courseIdStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "CourseID không hợp lệ.");
            return;
        }

        int courseID = Integer.parseInt(courseIdStr);
        boolean success = CourseDAO.deleteCourse(courseID);
        String message = success ? "Học phần đã được xóa thành công" :
                "Có lỗi xảy ra khi xóa học phần";
        AlertManager.addMessage(request, message, success );

        response.sendRedirect(COURSE_SERVLET);
    }

    private void updateCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CourseInput input = CourseInput.fromRequest(request, true);

        if (CourseValidator.validateInput(input, request, true)) {
            response.sendRedirect(COURSE_SERVLET);
            return;
        }

        Course oldCourse = CourseDAO.getCourseById(input.getCourseID());
        if (oldCourse == null) {
            AlertManager.addMessage(request, "Học phần không tồn tại.", false);
            response.sendRedirect(COURSE_SERVLET);
            return;
        }

        String courseCode = input.getCourseCode();
        if (!oldCourse.getCourseName().equals(input.getCourseName()) ||
                oldCourse.getDepartmentID() != input.getDepartmentID()) {
            Course tempCourse = new Course(input.getCourseName(), input.getCredits(),
                    input.getDepartmentID(), "", input.getCourseType(), input.getStatus());
            courseCode = tempCourse.generateCourseCode(input.getDepartmentID(), input.getCourseID());

            if (CourseDAO.checkCourseCode(courseCode)) {
                AlertManager.addMessage(request, "Không thể tạo mã mới vì mã bị trùng. Vui lòng nhập mã khác.", false);
                response.sendRedirect(COURSE_SERVLET);
                return;
            }
        }

        Course updatedCourse = new Course(input.getCourseID(), input.getCourseName(), input.getCredits(),
                input.getDepartmentID(), courseCode, input.getCourseType(), input.getStatus());

        boolean success = CourseDAO.updateCourse(updatedCourse);
        AlertManager.addMessage(request, success ? "Cập nhật học phần thành công!" : "Có lỗi khi cập nhật học phần", success);
        response.sendRedirect(COURSE_SERVLET);
    }
}
