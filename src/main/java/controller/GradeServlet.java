package controller;

import common.AlertManager;
import common.RoleUtils;
import common.SessionUtils;
import excel.GradeImportExcel;
import excel.GradeExportExcel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import models.bean.Class;
import models.bean.Grade;
import models.dao.ClassDAO;
import models.dao.GradeDAO;
import valid.GradeValidator;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@WebServlet("/GradeServlet")
@MultipartConfig
public class GradeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String URL = "GradeServlet?classID=";
	private static final String CLASSID = "classID";

	public GradeServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		// Kiểm tra trạng thái đăng nhập
		if (!SessionUtils.isLoggedIn(session)) {
			response.sendRedirect("login.jsp");
			return;
		}

		String action = request.getParameter("action");
		if (action != null) {
			if ("downloadTemplate".equals(action)) {
				downloadExcelTemplate(request, response);
				return;
			} else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
				return;
			}
		}

		handleViewGrades(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		if (action != null) {
			switch (action) {
			case "uploadExcel":
				uploadExcel(request, response);
				return;
			case "update":
				updateGrade(request, response);
				return;
			default:
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
				return;
			}
		}

		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ.");
	}

	private void handleViewGrades(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String classID = request.getParameter(CLASSID);
		HttpSession session = request.getSession();
		String teacherID = RoleUtils.isAdmin(session) ? null
						   : SessionUtils.getLoggedInAccount(session).getTeacherID();

		// Lấy danh sách lớp học
		List<Class> classes = GradeDAO.getClassesByTeacher(teacherID);
		request.setAttribute("classes", classes);

		// Lấy danh sách điểm
		if (classID != null && !classID.isEmpty()) {
			List<Grade> grades = GradeDAO.getGradesByClassID(classID);
			request.setAttribute("grades", grades);
			request.setAttribute("selectedClassID", classID);
		}

		request.getRequestDispatcher("/Views/GradeView/gradeViews.jsp").forward(request, response);
	}

	private void downloadExcelTemplate(HttpServletRequest request, HttpServletResponse response) {
		String classIDParam = request.getParameter(CLASSID);
		if (classIDParam == null || classIDParam.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		int classID;
		try {
			classID = Integer.parseInt(classIDParam);
		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		// Lấy thông tin lớp học
		Class cls = ClassDAO.getClassById(classID);
		if (cls == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// Lấy className và danh sách điểm
		String className = cls.getClassName();
		List<Grade> grades = GradeDAO.getGradesByClassID(String.valueOf(classID));
		if (grades == null || grades.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			return;
		}

		// Tạo file Excel mẫu
		GradeExportExcel.generateExcelTemplate(className, String.valueOf(classID), grades, response);
	}

	private void uploadExcel(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String classID = request.getParameter(CLASSID);
		System.out.println("ClassID for excel: " + classID);
		if (classID == null || classID.isEmpty()) {
			AlertManager.addMessage(request, "ID lớp không hợp lệ.", false);
			response.sendRedirect("GradeServlet");
			return;
		}

		Part filePart = request.getPart("excelFile");
		if (filePart == null || filePart.getSize() == 0) {
			AlertManager.addMessage(request, "Vui lòng chọn file Excel để tải lên.", false);
			response.sendRedirect(URL + classID);
			return;
		}

		try (InputStream inputStream = filePart.getInputStream()) {
			processExcelFile(inputStream, Integer.parseInt(classID), request);
		} catch (Exception e) {
			AlertManager.addMessage(request, "Lỗi khi xử lý file Excel: " + e.getMessage(), false);
		}

		response.sendRedirect(URL + classID);
	}

	private void processExcelFile(InputStream inputStream, int classID, HttpServletRequest request) {
	    try {
	        System.out.println("Bắt đầu xử lý file Excel cho classID: " + classID);
	        List<Grade> grades = GradeImportExcel.importGradesFromExcel(inputStream, classID);

	        if (grades.isEmpty()) {
	            AlertManager.addMessage(request, "Không có dữ liệu điểm nào được tìm thấy trong file.", false);
	            return;
	        }

	        int successCount = 0;
	        StringBuilder errors = new StringBuilder();

	        // Xử lý từng grade
	        for (Grade grade : grades) {
	            try {
	                System.out.println("Đang cập nhật điểm cho StudentClassID: " + grade.getStudentClassID());
	                boolean updateResult = GradeDAO.updateGradeByStudentClassID(grade);

	                if (updateResult) {
	                    successCount++;
	                    System.out.println("Cập nhật thành công điểm cho StudentClassID: " + grade.getStudentClassID());
	                } else {
	                    String error = String.format("Lỗi cập nhật điểm cho sinh viên ID: %s%n",
	                                               grade.getStudentClassID());
	                    errors.append(error);
	                    System.err.println(error);
	                }
	            } catch (Exception e) {
	                String error = String.format("Lỗi xử lý dữ liệu cho sinh viên ID %s: %s%n",
	                                           grade.getStudentClassID(), e.getMessage());
	                errors.append(error);
	                System.err.println(error);
	            }
	        }

	        // Tạo thông báo kết quả
	        StringBuilder resultMessage = new StringBuilder();
	        if (successCount > 0) {
	            resultMessage.append(String.format("Đã cập nhật điểm thành công cho %d/%d sinh viên. ",
	                                             successCount, grades.size()));
	        }

	        if (errors.length() > 0) {
	            resultMessage.append("\nCác lỗi phát sinh:\n").append(errors.toString());
	        }

	        // Gửi thông báo
	        boolean isSuccess = successCount > 0;
	        System.out.println("Kết quả xử lý: " + resultMessage.toString());
	        AlertManager.addMessage(request, resultMessage.toString(), isSuccess);

	    } catch (Exception e) {
	        String errorMsg = "Lỗi khi xử lý file Excel: " + e.getMessage();
	        System.err.println(errorMsg);
	        AlertManager.addMessage(request, errorMsg, false);
	    }
	}

	private void updateGrade(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String gradeIDStr = request.getParameter("gradeID");
		String attendanceScoreStr = request.getParameter("attendanceScore");
		String midtermScoreStr = request.getParameter("midtermScore");
		String finalExamScoreStr = request.getParameter("finalExamScore");
		String classID = request.getParameter(CLASSID);

		// Chuyển đổi và kiểm tra giá trị input
		Integer gradeID = parseInteger(gradeIDStr);
		Double attendanceScore = parseDouble(attendanceScoreStr);
		Double midtermScore = parseDouble(midtermScoreStr);
		Double finalExamScore = parseDouble(finalExamScoreStr);

		if (GradeValidator.validateUpdateInput(gradeID, attendanceScore, midtermScore, finalExamScore, request)) {
			response.sendRedirect(URL + classID);
			return;
		}

		Grade grade = new Grade(String.valueOf(gradeID), attendanceScore, midtermScore, finalExamScore);
		boolean success = GradeDAO.updateGrade(grade);
		AlertManager.addMessage(request, success ? "Cập nhật điểm thành công!" : "Có lỗi khi cập nhật điểm.", success);

		response.sendRedirect(URL + classID);
	}

	private static Double parseDouble(String value) {
		try {
			return (value != null && !value.trim().isEmpty()) ? Double.parseDouble(value) : null;
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private static Integer parseInteger(String value) {
		try {
			return (value != null && !value.trim().isEmpty()) ? Integer.parseInt(value) : null;
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
