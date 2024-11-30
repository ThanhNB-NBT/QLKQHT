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
import common.AlertManager;
import common.RoleUtils;

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
	
	private void createCourse(HttpServletRequest request, HttpServletResponse response)
	        throws IOException {
	    String courseName = request.getParameter("courseName");
	    String creditsStr = request.getParameter("credits");
	    String departmentIDStr = request.getParameter("departmentID");
	    String courseCode = request.getParameter("courseCode");
	    String courseType = request.getParameter("courseType");
	    String status = request.getParameter("status");

	    if (checkCourseInput(request, courseName, courseCode, creditsStr, departmentIDStr, courseType, status)) {
	        return;
	    }


	    int credits = Integer.parseInt(creditsStr);
	    int departmentID = Integer.parseInt(departmentIDStr);

	    if (courseCode == null || courseCode.trim().isEmpty()) {
	        Course tempCourse = new Course(courseName, credits, departmentID, "", courseType, status);

	        courseCode = tempCourse.generateCourseCode(departmentID,null); 

	        if (CourseDAO.checkCourseCode(courseCode)) {
	            AlertManager.addMessage(request, "Không thể tạo mã tự động vì mã bị trùng. Vui lòng nhập mã khác.", false);
	            response.sendRedirect(COURSE_SERVLET);
	            return;
	        }
	    }

	    Course course = new Course(courseName, credits, departmentID, courseCode, courseType, status);

	    boolean success = CourseDAO.createCourse(course);
	    String message = success ? "Thêm học phần thành công!" : "Đã có lỗi xảy ra khi thêm học phần";
	    AlertManager.addMessage(request, message, success);

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

    private void updateCourse(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int courseID = Integer.parseInt(request.getParameter(COURSEID));
        String courseName = request.getParameter("courseName");
        String courseCode = request.getParameter("courseCode");
        String creditsStr = request.getParameter("credits");
        String departmentIDStr = request.getParameter("departmentID");
        String courseType = request.getParameter("courseType");
        String status = request.getParameter("status");

        if (checkCourseInput(request, courseName, courseCode, creditsStr, departmentIDStr, courseType, status)) {
            return;
        }

        int credits = Integer.parseInt(creditsStr);
        int departmentID = Integer.parseInt(departmentIDStr);

        Course oldCourse = CourseDAO.getCourseById(courseID);
        if (oldCourse == null) {
            AlertManager.addMessage(request, "Không tìm thấy học phần", false);
            response.sendRedirect(COURSE_SERVLET);
            return;
        }

        if (!oldCourse.getCourseName().equals(courseName) || oldCourse.getDepartmentID() != departmentID) {
            Course tempCourse = new Course();
            tempCourse.setCourseName(courseName);
            tempCourse.setDepartmentID(departmentID);
            courseCode = tempCourse.generateCourseCode(departmentID,courseID); 
        }

        Course course = new Course(courseID, courseName, credits, departmentID, courseCode, courseType, status);

        boolean success = CourseDAO.updateCourse(course);
        String message = success ? "Cập nhật học phần thành công!" : "Có lỗi xảy ra khi cập nhật học phần";
        AlertManager.addMessage(request, message, success);
        response.sendRedirect(COURSE_SERVLET);
    }


    private boolean checkCourseInput(HttpServletRequest request, String courseName,
                                     String courseCode, String creditsStr, String departmentIDStr,
                                     String courseType, String status) {
    	boolean hasError = false;

        if (courseName == null || courseName.trim().isEmpty()) {
            AlertManager.addMessage(request, "Tên học phần không được để trống", false);
            hasError = true;
        }

        try {
            Integer.parseInt(creditsStr);
            Integer.parseInt(departmentIDStr);
        } catch (NumberFormatException e) {
        	AlertManager.addMessage(request, "Số tín chỉ hoặc khoa không hợp lệ", false);
            hasError = true;
        }

        if (courseType == null || courseType.trim().isEmpty()) {
        	AlertManager.addMessage(request, "Loại học phần không được để trống", false);
            hasError = true;
        }

        if (status == null || status.trim().isEmpty()) {
        	AlertManager.addMessage(request, "Trạng thái không được để trống", false);
            hasError = true;
        }

        if (CourseDAO.checkCourseCode(courseCode)) {
            AlertManager.addMessage(request, "Mã học phần đã tồn tại", false);
            hasError = true;
        }

        return hasError;
    }
}
