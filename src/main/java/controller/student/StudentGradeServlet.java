package controller.student;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.bean.Grade;
import models.dao.GradeDAO;
import models.dao.StudentDAO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import common.SessionUtils;

@WebServlet("/StudentGradeServlet")
public class StudentGradeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public StudentGradeServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            System.out.println("Đang khởi tạo GradeServlet...");

            // Lấy đường dẫn file
            String xmlPath = getServletContext().getRealPath("/Views/ClassView/grade_config.xml");
            System.out.println("Đường dẫn file XML: " + xmlPath);

            // Kiểm tra file có tồn tại không
            File xmlFile = new File(xmlPath);
            if (!xmlFile.exists()) {
                throw new ServletException("File cấu hình điểm không tồn tại tại đường dẫn: " + xmlPath);
            }

            // Đọc nội dung file
            String xmlContent = Files.readString(xmlFile.toPath());
            if (xmlContent == null || xmlContent.trim().isEmpty()) {
                throw new ServletException("Nội dung file cấu hình XML trống hoặc không hợp lệ.");
            }

            Grade.setXMLContent(xmlContent);
            System.out.println("Đã set XML content thành công");

        } catch (IOException e) {
            throw new ServletException("Không thể đọc file cấu hình điểm", e);
        }
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Lấy studentID từ session
        HttpSession session = request.getSession();
        String studentID = SessionUtils.getLoggedInAccount(session).getStudentID();

        if (studentID == null) {
            // Nếu không có studentID trong session, chuyển hướng về trang đăng nhập
            response.sendRedirect("login.jsp");
            return;
        }

        List<String> classIDs = StudentDAO.getClassIDByStudentID(studentID);
        List<Grade> gradeList = GradeDAO.getGradesByStudentID(studentID);
        String classIDsString = String.join(",", classIDs);
        System.out.println("classID:" + classIDsString);

        // Gắn chuỗi ClassID vào từng đối tượng Grade
        for (Grade grade : gradeList) {
            grade.setClassID(classIDsString); // Gắn chuỗi chứa tất cả ClassID
        }
        // Đưa danh sách điểm vào request để hiển thị trên JSP
        request.setAttribute("gradeList", gradeList);

        // Chuyển tiếp đến trang hiển thị danh sách điểm
        request.getRequestDispatcher("/Views/Student/StudentGrade.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
