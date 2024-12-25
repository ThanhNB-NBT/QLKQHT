package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import common.AlertManager;
import common.SessionUtils;
import input.DepartmentInput;
import models.bean.Account;
import models.bean.Department;
import models.dao.DepartmentDAO;
import valid.DepartmentValidator;

@WebServlet("/DepartmentServlet")
public class DepartmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String DEPARTMENTID = "departmentID";
	private static final String DEPARTMENT_SERVLET = "DepartmentServlet";

	public DepartmentServlet() {
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

		String searchDepartment = request.getParameter("search");
		List<Department> departments = (searchDepartment != null && !searchDepartment.trim().isEmpty())
				? DepartmentDAO.searchByDepartmentName(searchDepartment)
				: DepartmentDAO.getAllDepartment();
		request.setAttribute("departments", departments);
		Account loggedInUser = SessionUtils.getLoggedInAccount(session);
		request.setAttribute("username", loggedInUser.getUsername());
		request.getRequestDispatcher("Views/DepartmentView/departmentViews.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		switch (action) {
		case "create":
			createDepartment(request, response);
			break;
		case "delete":
			deleteDepartment(request, response);
			break;
		case "update":
			updateDepartment(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
			break;
		}
	}

	private void createDepartment(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		DepartmentInput input = DepartmentInput.fromRequest(request);


		 boolean hasErrors = DepartmentValidator.validateInput(input, request);
         if (hasErrors) {
             // Thêm thông báo lỗi nếu có và chuyển hướng về trang quản lý sinh viên
             AlertManager.addMessage(request, "Dữ liệu nhập vào không hợp lệ, vui lòng kiểm tra lại.", false);
             response.sendRedirect(DEPARTMENT_SERVLET);
             return;
         }
		Department department = new Department(input.getDepartmentName(), input.getEmail(), input.getPhone());

		boolean success = DepartmentDAO.createDepartment(department);
		String message = success ? "Thêm khoa/viện thành công!" : "Đã có lỗi xảy ra khi thêm khoa/viện.";
		AlertManager.addMessage(request, message, success);

		response.sendRedirect(DEPARTMENT_SERVLET);
	}

	private void deleteDepartment(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String departmentIdStr = request.getParameter(DEPARTMENTID);
		if (departmentIdStr == null || departmentIdStr.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "DepartmentID không hợp lệ.");
			return;
		}

		int departmentID = Integer.parseInt(departmentIdStr);
		boolean success = DepartmentDAO.deleteDepartment(departmentID);
		String message = success ? "Khoa/viện đã được xóa thành công." : "Có lỗi xảy ra khi xóa khoa/viện.";
		AlertManager.addMessage(request, message, success);

		response.sendRedirect(DEPARTMENT_SERVLET);
	}

	private void updateDepartment(HttpServletRequest request, HttpServletResponse response) throws IOException {
		DepartmentInput input = DepartmentInput.fromRequest(request);

		boolean hasErrors = DepartmentValidator.validateInput(input, request);
        if (hasErrors) {
            // Thêm thông báo lỗi nếu có và chuyển hướng về trang quản lý sinh viên
            AlertManager.addMessage(request, "Dữ liệu nhập vào không hợp lệ, vui lòng kiểm tra lại.", false);
            response.sendRedirect(DEPARTMENT_SERVLET);
            return;
        }
		Department department = new Department(input.getDepartmentID(), input.getDepartmentName(), input.getEmail(), input.getPhone());

		boolean success = DepartmentDAO.updateDepartment(department);
		String message = success ? "Cập nhật khoa/viện thành công!" : "Có lỗi xảy ra khi cập nhật khoa/viện.";
		AlertManager.addMessage(request, message, success);
		response.sendRedirect(DEPARTMENT_SERVLET);
	}

}
