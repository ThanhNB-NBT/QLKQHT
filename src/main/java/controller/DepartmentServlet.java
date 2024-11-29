package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import common.SessionUtils;
import models.bean.Department;
import models.dao.DepartmentDAO;

@WebServlet("/DepartmentServlet")
public class DepartmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String DEPARTMENTID = "departmentID";
	private static final String DEPARTMENT_SERVLET = "DepartmentServlet";
       
    public DepartmentServlet() {
        super();

    }


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (!SessionUtils.isLoggedIn(session)) {
			response.sendRedirect("login.jsp");
			return;
		}

		String departmentIDStr = request.getParameter(DEPARTMENTID);
		if (departmentIDStr != null) {
	        try {
	            int departmentID = Integer.parseInt(departmentIDStr);
	            Department departmentToEdit = DepartmentDAO.getDepartmentById(departmentID);

	            if (departmentToEdit != null) {

	                request.setAttribute("departmentToEdit", departmentToEdit);

	                request.getRequestDispatcher("/Views/DepartmentView/editDepartmentModal.jsp").forward(request, response);
	            } else {
	                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Tài khoản không tồn tại.");
	            }
	            return;
	        } catch (NumberFormatException e) {
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ.");
	            return;
	        }
		}
		
		String searchDepartment = request.getParameter("search");
		List<Department> departments;
		if (searchDepartment != null && !searchDepartment.trim().isEmpty()) {
			departments = DepartmentDAO.searchByDepartmentName(searchDepartment);
		} else {
			departments = DepartmentDAO.getAllDepartment();
		}
		request.setAttribute("departments", departments );
		request.getRequestDispatcher("Views/DepartmentView/departmentViews.jsp").forward(request, response);
	}
@Override
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			throws ServletException, IOException {
		String departmentName = request.getParameter("departmentName");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");

		if (checkDepartmentInput(request, response, departmentName, email, phone)) {
			return;
		}

		Department department = new Department(departmentName, email, phone);

		boolean success = DepartmentDAO.createDepartment(department);
		setSessionMessage(request, success, "Thêm khoa/viện thành công!", "Đã có lỗi xảy ra khi thêm khoa/viện.");

		response.sendRedirect(DEPARTMENT_SERVLET);
	}

	private void deleteDepartment(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String departmentIdStr = request.getParameter(DEPARTMENTID);
		if (departmentIdStr == null || departmentIdStr.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "DepartmentID không hợp lệ.");
			return;
		}
		
		int departmentID = Integer.parseInt(departmentIdStr);
		boolean success = DepartmentDAO.deleteDepartment(departmentID);
		setSessionMessage(request, success, "Khoa/viện đã được xóa thành công.",
				"Có lỗi xảy ra khi xóa khoa/viện.");
		
		response.sendRedirect(DEPARTMENT_SERVLET);
	}

	private void updateDepartment(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		int departmentID = Integer.parseInt(request.getParameter(DEPARTMENTID));
		String departmentName = request.getParameter("departmentName");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");

		if (checkDepartmentInput(request, response, departmentName, email, phone)) {
			return;
		}

		Department department = new Department(departmentID, departmentName, email, phone);

		boolean success = DepartmentDAO.updateDepartment(department);
		setSessionMessage(request, success, "Cập nhật khoa/viện thành công!", "Có lỗi xảy ra khi cập nhật khoa/viện.");
		response.sendRedirect(DEPARTMENT_SERVLET);
	}

	private boolean checkDepartmentInput(HttpServletRequest request, HttpServletResponse response, String departmentName,
			String email, String phone) {
		HttpSession session = request.getSession();
		
		Department departmentToEdit = (Department) session.getAttribute("departmentToEdit");
		
		if (departmentName == null || departmentName.trim().isEmpty()) {
			setSessionError(session, "Tên khoa/viện không được để trống.");
			return true;
		}

		if (email == null || email.trim().isEmpty()) {
			setSessionError(session, "Email không được để trống.");
			return true;
		}
		
		if (phone == null || phone.trim().isEmpty()) {
			setSessionError(session, "Số điện thoại không được để trống.");
			return true;
		}

		if (departmentToEdit != null && !departmentToEdit.getDepartmentName().equals(departmentName) &&  DepartmentDAO.checkDepartmentName(departmentName)) {
			setSessionError(session, "Tên khoa/viện đã tồn tại.");
			return true;
		}
		if (departmentToEdit != null && !departmentToEdit.getEmail().equals(email) &&  DepartmentDAO.checkDepartmentName(departmentName)) {
			setSessionError(session, "Tên khoa/viện đã tồn tại.");
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
}
