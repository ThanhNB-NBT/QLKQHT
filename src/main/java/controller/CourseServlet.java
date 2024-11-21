package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.bean.Course;
import models.dao.CourseDAO;
import models.dao.DepartmentDAO;

import java.io.IOException;
import java.util.List;

import common.SessionUtils;


@WebServlet("/CourseServlet")
public class CourseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String COURSEID = "courseID";
    private static final String COURSE_SERVLET = "CourseServlet";

    public CourseServlet() {
        super();

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
        if (!SessionUtils.isLoggedIn(session)) {
            response.sendRedirect("login.jsp");
            return;
        }

        String courseIDStr = request.getParameter(COURSEID);
        if (courseIDStr != null) {
            try {
                int courseID = Integer.parseInt(courseIDStr);
                Course courseToEdit = CourseDAO.getCourseById(courseID);

                if (courseToEdit != null) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(convertCourseToJson(courseToEdit));
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
        List<Course> courses;
        if (searchCourse != null && !searchCourse.trim().isEmpty()) {
            courses = CourseDAO.searchCoursesByName(searchCourse);
        } else {
            courses = CourseDAO.getAllCourse();
        }
        request.setAttribute("courses", courses);
        request.setAttribute("departments", DepartmentDAO.getAllDepartment());
        request.getRequestDispatcher("Views/courseViews.jsp").forward(request, response);
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
	
	private void createCourse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String courseName = request.getParameter("courseName");
        String courseCode = request.getParameter("courseCode");
        String creditsStr = request.getParameter("credits");
        String departmentIDStr = request.getParameter("departmentID");
        String courseType = request.getParameter("courseType");
        String status = request.getParameter("status");

        if (checkCourseInput(request, response, courseName, courseCode, creditsStr, departmentIDStr, courseType, status)) {
            return;
        }

        int credits = Integer.parseInt(creditsStr);
        int departmentID = Integer.parseInt(departmentIDStr);

        Course course = new Course(courseName, credits, departmentID, courseCode, courseType, status);

        boolean success = CourseDAO.createCourse(course);
        setSessionMessage(request, success, "Thêm khóa học thành công!", "Đã có lỗi xảy ra khi thêm khóa học.");

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
        setSessionMessage(request, success, "Khóa học đã được xóa thành công.",
                "Có lỗi xảy ra khi xóa khóa học.");

        response.sendRedirect(COURSE_SERVLET);
    }

    private void updateCourse(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int courseID = Integer.parseInt(request.getParameter(COURSEID));
        String courseName = request.getParameter("courseName");
        String courseCode = request.getParameter("courseCode");
        String creditsStr = request.getParameter("credits");
        String departmentIDStr = request.getParameter("departmentID");
        String courseType = request.getParameter("courseType");
        String status = request.getParameter("status");

        if (checkCourseInput(request, response, courseName, courseCode, creditsStr, departmentIDStr, courseType, status)) {
            return;
        }

        int credits = Integer.parseInt(creditsStr);
        int departmentID = Integer.parseInt(departmentIDStr);

        Course course = new Course(courseID, courseName, credits, departmentID, courseCode, courseType, status);

        boolean success = CourseDAO.updateCourse(course);
        setSessionMessage(request, success, "Cập nhật khóa học thành công!", "Có lỗi xảy ra khi cập nhật khóa học.");
        response.sendRedirect(COURSE_SERVLET);
    }

    private boolean checkCourseInput(HttpServletRequest request, HttpServletResponse response, String courseName,
                                     String courseCode, String creditsStr, String departmentIDStr,
                                     String courseType, String status) {
        HttpSession session = request.getSession();

        Course courseToEdit = (Course) session.getAttribute("courseToEdit");

        if (courseName == null || courseName.trim().isEmpty()) {
            setSessionError(session, "Tên khóa học không được để trống.");
            return true;
        }

        if (courseCode == null || courseCode.trim().isEmpty()) {
            setSessionError(session, "Mã khóa học không được để trống.");
            return true;
        }

        try {
            Integer.parseInt(creditsStr);
            Integer.parseInt(departmentIDStr);
        } catch (NumberFormatException e) {
            setSessionError(session, "Số tín chỉ hoặc phòng ban không hợp lệ.");
            return true;
        }

        if (courseType == null || courseType.trim().isEmpty()) {
            setSessionError(session, "Loại khóa học không được để trống.");
            return true;
        }

        if (status == null || status.trim().isEmpty()) {
            setSessionError(session, "Trạng thái không được để trống.");
            return true;
        }

        if (courseToEdit != null && !courseToEdit.getCourseCode().equals(courseCode) && CourseDAO.checkCourseCode(courseCode)) {
            setSessionError(session, "Mã khóa học đã tồn tại.");
            return true;
        }

        return false;
    }

    private void setSessionError(HttpSession session, String errorMessage) {
        session.setAttribute("errorModal", errorMessage);
    }

    private void setSessionMessage(HttpServletRequest request, boolean success, String successMessage,
                                   String errorMessage) {
        HttpSession session = request.getSession();
        if (success) {
            session.setAttribute("message", successMessage);
        } else {
            session.setAttribute("error", errorMessage);
        }
    }

    private String convertCourseToJson(Course course) {
        return "{ \"courseID\": " + course.getCourseID() +
                ", \"courseName\": \"" + course.getCourseName() + "\"" +
                ", \"credits\": " + course.getCredits() +
                ", \"departmentID\": " + course.getDepartmentID() +
                ", \"courseCode\": \"" + course.getCourseCode() + "\"" +
                ", \"courseType\": \"" + course.getCourseType() + "\"" +
                ", \"status\": \"" + course.getStatus() + "\" }";
    }

}
