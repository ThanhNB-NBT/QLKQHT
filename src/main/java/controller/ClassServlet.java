package controller;

import common.AlertManager;
import common.RoleUtils;
import common.SessionUtils;
import input.ClassInput;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.bean.Account;
import models.bean.Class;
import models.bean.Course;
import models.dao.ClassDAO;
import models.dao.CourseDAO;
import models.dao.TeacherDAO;
import valid.ClassValidator;

import java.io.IOException;
import java.util.List;

@WebServlet("/ClassServlet")
public class ClassServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String CLASSID = "classID";
	private static final String CLASS_SERVLET = "ClassServlet";

	public ClassServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!SessionUtils.isLoggedIn(request.getSession(false))) {
			response.sendRedirect("login.jsp");
			return;
		}

		HttpSession session = request.getSession();
		request.setAttribute("isAdmin", RoleUtils.isAdmin(session));

		try {

			String courseName = request.getParameter("courseName");
			String teacherName = request.getParameter("teacherName");

			boolean noCriteria = (courseName == null || courseName.trim().isEmpty())
					&& (teacherName == null || teacherName.trim().isEmpty());

			List<Class> classes = noCriteria ? ClassDAO.getAllClasses()
					: ClassDAO.searchByClassName(courseName, teacherName);

			if (!noCriteria && classes.isEmpty()) {
				AlertManager.addMessage(request, "Không tìm thấy lớp học nào phù hợp với tiêu chí tìm kiếm.", false);
			}

			request.setAttribute("classes", classes);
			request.setAttribute("courses", CourseDAO.getAllCourse());
			request.setAttribute("teachers", TeacherDAO.getAllTeachers());

			// Gán thêm thông tin người dùng đang đăng nhập
			Account loggedInUser = SessionUtils.getLoggedInAccount(session);
			request.setAttribute("username", loggedInUser.getUsername());

			// Chuyển hướng đến trang lớp học
			request.getRequestDispatcher("/Views/ClassView/classViews.jsp").forward(request, response);

		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ.");
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi khi xử lý yêu cầu.");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action == null || action.trim().isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
			return;
		}

		switch (action) {
		case "create":
			createClass(request, response);
			break;
		case "delete":
			deleteClass(request, response);
			break;
		case "update":
			updateClass(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
			break;
		}
	}

	private void createClass(HttpServletRequest request, HttpServletResponse response) throws IOException {

		ClassInput input = ClassInput.fromRequest(request, false);

		if (ClassValidator.validateInput(input, request, false)) {
			response.sendRedirect(CLASS_SERVLET);
			return;
		}

		String courseName = null;
		String parentClassName = null;

		if (input.getParentClassID() != null && input.getParentClassID() > 0) {
			Class parentClass = ClassDAO.getClassById(input.getParentClassID());
			if (parentClass == null) {
				AlertManager.addMessage(request, "Không tìm thấy lớp lý thuyết để làm lớp gốc!", false);
				response.sendRedirect(CLASS_SERVLET);
				return;
			}
			parentClassName = parentClass.getClassName();
		} else if (input.getCourseID() > 0) {
			Course course = CourseDAO.getCourseById(input.getCourseID());
			if (course == null) {
				AlertManager.addMessage(request, "Không tìm thấy học phần để tạo lớp!", false);
				response.sendRedirect(CLASS_SERVLET);
				return;
			}
			courseName = course.getCourseName();
		} else {
			AlertManager.addMessage(request, "Cần cung cấp CourseID hoặc ParentClassID để tạo lớp học!", false);
			response.sendRedirect(CLASS_SERVLET);
			return;
		}

		// Đếm số lớp hiện có để tạo tên lớp
		int existingCount = ClassDAO.countClassesByCourseID(input.getCourseID(), input.getClassType());
		String className = input.generateClassName(existingCount, courseName, parentClassName);

		Class newClass = new Class(input.getCourseID(), input.getTeacherID(), input.getClassTime(), input.getRoom(),
				input.getSemester(), className, input.getStatus(), input.getMaxStudents(), input.getTotalLessions(),
				input.getStartDate(), input.getEndDate(), input.getClassType(), input.getParentClassID());

		boolean success = ClassDAO.createClass(newClass);

		AlertManager.addMessage(request, success ? "Thêm lớp học thành công!" : "Có lỗi khi thêm lớp học", success);
		response.sendRedirect(CLASS_SERVLET);
	}

	private void deleteClass(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String classIDStr = request.getParameter(CLASSID);
		if (classIDStr == null || classIDStr.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ClassID không hợp lệ.");
			return;
		}

		try {
			int classID = Integer.parseInt(classIDStr);
			boolean success = ClassDAO.deleteClass(classID);
			AlertManager.addMessage(request,
					success ? "Lớp học đã được xóa thành công" : "Có lỗi xảy ra khi xóa lớp học", success);
		} catch (NumberFormatException e) {
			AlertManager.addMessage(request, "ClassID không hợp lệ.", false);
		}

		response.sendRedirect(CLASS_SERVLET);
	}

	private void updateClass(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ClassInput input = ClassInput.forUpdate(request);

		if (ClassValidator.validateInput(input, request, true)) {
			response.sendRedirect(CLASS_SERVLET);
			return;
		}

		Class oldClass = ClassDAO.getClassById(input.getClassID());
		if (oldClass == null) {
			AlertManager.addMessage(request, "Lớp học không tồn tại.", false);
			response.sendRedirect(CLASS_SERVLET);
			return;
		}

		Class updatedClass = new Class(input.getClassID(), input.getTeacherID(), input.getClassTime(), input.getRoom(),
				input.getStatus(), input.getMaxStudents(), input.getTotalLessions());

		boolean success = ClassDAO.updateClass(updatedClass);
		AlertManager.addMessage(request, success ? "Cập nhật lớp học thành công!" : "Có lỗi khi cập nhật lớp học",
				success);
		response.sendRedirect(CLASS_SERVLET);
	}
}
