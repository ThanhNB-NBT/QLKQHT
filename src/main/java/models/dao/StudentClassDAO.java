package models.dao;

import models.bean.StudentClass;
import common.ConnectDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class StudentClassDAO {
	private static final Logger logger = Logger.getLogger(StudentClassDAO.class.getName());

	private static final String SQL_GET_ALL_STUDENTCLASS = "SELECT sc.StudentClassID, sc.ClassID, sc.StudentID, sc.Status, "
			+ "c.ClassName, s.StudentCode, CONCAT(s.FirstName, ' ', s.LastName) AS StudentName "
			+ "FROM StudentClasses sc " + "JOIN Classes c ON sc.ClassID = c.ClassID "
			+ "JOIN Students s ON sc.StudentID = s.StudentID";

	private static final String SQL_GET_BY_ID = "SELECT sc.StudentClassID, sc.ClassID, sc.StudentID, sc.Status, "
			+ "c.ClassName, s.StudentCode, CONCAT(s.FirstName, ' ', s.LastName) AS StudentName "
			+ "FROM StudentClasses sc " + "JOIN Classes c ON sc.ClassID = c.ClassID "
			+ "JOIN Students s ON sc.StudentID = s.StudentID " + "WHERE sc.StudentClassID = ?";

	private static final String SQL_SEARCH = "SELECT sc.StudentClassID, sc.ClassID, sc.StudentID, sc.Status, "
			+ "c.ClassName, s.StudentCode, CONCAT(s.FirstName, ' ', s.LastName) AS StudentName "
			+ "FROM StudentClasses sc " + "JOIN Classes c ON sc.ClassID = c.ClassID "
			+ "JOIN Students s ON sc.StudentID = s.StudentID "
			+ "WHERE c.ClassName LIKE ? OR CONCAT(s.FirstName, ' ', s.LastName) LIKE ?";

	private static final String SQL_CREATE_STUDENTCLASS = "INSERT INTO StudentClasses (ClassID, StudentID, Status) VALUES (?, ?, ?)";

	private static final String SQL_UPDATE_STUDENTCLASS = "UPDATE StudentClasses SET Status = ? WHERE StudentClassID = ?";

	private static final String SQL_DELETE_STUDENTCLASS = "DELETE FROM StudentClasses WHERE StudentClassID = ?";

	private static final String SQL_CHECK_STUDENT = "SELECT COUNT(*) AS count FROM StudentClasses WHERE ClassID = ? AND StudentID = ?";

	private static final String SQL_GET_CLASSID_BY_STUDENTCLASSID = "SELECT ClassID FROM StudentClasses WHERE StudentClassID = ?";

	// Lấy danh sách tất cả StudentClass
	public static List<StudentClass> getAllStudentClasses() {
		List<StudentClass> studentClasses = new ArrayList<>();
		try (Connection conn = ConnectDatabase.checkConnect();
				PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL_STUDENTCLASS);
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				studentClasses.add(mapStudentClass(rs));
			}
		} catch (SQLException e) {
			logger.severe("Lỗi khi lấy danh sách StudentClasses: " + e.getMessage());
		}
		return studentClasses;
	}

	// Lấy thông tin StudentClass theo ID
	public static StudentClass getStudentClassById(int studentClassID) {
		try (Connection conn = ConnectDatabase.checkConnect();
				PreparedStatement pstmt = conn.prepareStatement(SQL_GET_BY_ID)) {
			pstmt.setInt(1, studentClassID);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return mapStudentClass(rs);
				}
			}
		} catch (SQLException e) {
			logger.severe("Lỗi khi lấy StudentClass theo ID: " + e.getMessage());
		}
		return null;
	}

	// Thêm mới StudentClass
	public static boolean createStudentClass(StudentClass studentClass) {
		Connection conn = null;
		PreparedStatement pstmtInsert = null;
		PreparedStatement pstmtUpdate = null;

		String sqlUpdateRegistered = "UPDATE Classes SET RegisteredStudents = RegisteredStudents + 1 WHERE ClassID = ?";

		try {
			conn = ConnectDatabase.checkConnect();
			conn.setAutoCommit(false);

			// Thêm mới StudentClass
			pstmtInsert = conn.prepareStatement(SQL_CREATE_STUDENTCLASS);
			pstmtInsert.setInt(1, studentClass.getClassID());
			pstmtInsert.setInt(2, studentClass.getStudentID());
			pstmtInsert.setString(3, studentClass.getStatus());

			int rowsInserted = pstmtInsert.executeUpdate();

			if (rowsInserted > 0) {
				// Cập nhật RegisteredStudents trong Classes
				pstmtUpdate = conn.prepareStatement(sqlUpdateRegistered);
				pstmtUpdate.setInt(1, studentClass.getClassID());
				int rowsUpdated = pstmtUpdate.executeUpdate();

				if (rowsUpdated > 0) {
					conn.commit(); // Commit nếu cả hai thành công
					return true;
				} else {
					conn.rollback(); // Rollback nếu cập nhật thất bại
				}
			} else {
				conn.rollback(); // Rollback nếu thêm thất bại
			}
		} catch (SQLException e) {
			logger.severe("Lỗi khi thêm StudentClass và cập nhật RegisteredStudents: " + e.getMessage());
			try {
				if (conn != null)
					conn.rollback(); // Rollback nếu có lỗi
			} catch (SQLException ex) {
				logger.severe("Lỗi rollback: " + ex.getMessage());
			}
		} finally {
			try {
				if (pstmtInsert != null)
					pstmtInsert.close();
				if (pstmtUpdate != null)
					pstmtUpdate.close();
				if (conn != null)
					conn.setAutoCommit(true); // Trả lại trạng thái AutoCommit
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.severe("Lỗi khi đóng tài nguyên: " + e.getMessage());
			}
		}
		return false;
	}

	// Cập nhật StudentClass
	public static boolean updateStudentClass(StudentClass studentClass) {
		try (Connection conn = ConnectDatabase.checkConnect();
				PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE_STUDENTCLASS)) {
			pstmt.setString(1, studentClass.getStatus());
			pstmt.setInt(2, studentClass.getStudentClassID());

			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			logger.severe("Lỗi khi cập nhật StudentClass: " + e.getMessage());
		}
		return false;
	}

	// Xóa StudentClass
	public static boolean deleteStudentClass(int studentClassID, int classID) {
		Connection conn = null;
		PreparedStatement pstmtDelete = null;
		PreparedStatement pstmtUpdate = null;

		String sqlUpdateRegistered = "UPDATE Classes SET RegisteredStudents = RegisteredStudents - 1 WHERE ClassID = ?";

		try {
			conn = ConnectDatabase.checkConnect();
			conn.setAutoCommit(false); // Bắt đầu transaction

			// Xóa StudentClass
			pstmtDelete = conn.prepareStatement(SQL_DELETE_STUDENTCLASS);
			pstmtDelete.setInt(1, studentClassID);

			int rowsDeleted = pstmtDelete.executeUpdate();

			if (rowsDeleted > 0) {
				// Giảm RegisteredStudents trong Classes
				pstmtUpdate = conn.prepareStatement(sqlUpdateRegistered);
				pstmtUpdate.setInt(1, classID);

				int rowsUpdated = pstmtUpdate.executeUpdate();

				if (rowsUpdated > 0) {
					conn.commit(); // Commit nếu cả hai thành công
					return true;
				} else {
					conn.rollback(); // Rollback nếu cập nhật thất bại
				}
			} else {
				conn.rollback(); // Rollback nếu xóa thất bại
			}
		} catch (SQLException e) {
			logger.severe("Lỗi khi xóa StudentClass và cập nhật RegisteredStudents: " + e.getMessage());
			try {
				if (conn != null)
					conn.rollback(); // Rollback nếu có lỗi
			} catch (SQLException ex) {
				logger.severe("Lỗi rollback: " + ex.getMessage());
			}
		} finally {
			try {
				if (pstmtDelete != null)
					pstmtDelete.close();
				if (pstmtUpdate != null)
					pstmtUpdate.close();
				if (conn != null)
					conn.setAutoCommit(true); // Trả lại trạng thái AutoCommit
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.severe("Lỗi khi đóng tài nguyên: " + e.getMessage());
			}
		}
		return false;
	}

	//Lấy ClassID theo StudentClassID để xóa
	public static Integer getClassIDByStudentClassID(int studentClassID) {
		try (Connection conn = ConnectDatabase.checkConnect();
				PreparedStatement ps = conn.prepareStatement(SQL_GET_CLASSID_BY_STUDENTCLASSID)) {
			ps.setInt(1, studentClassID);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("ClassID");
				}
			}
		} catch (SQLException e) {
			logger.severe("Lỗi khi lấy ClassID từ StudentClassID: " + e.getMessage());
		}
		return null;
	}

	//Kiểm tra trùng lớp
	public static boolean isDuplicate(Integer classID, Integer studentID) {

		try (Connection connection = ConnectDatabase.checkConnect();
				PreparedStatement preparedStatement = connection.prepareStatement(SQL_CHECK_STUDENT)) {

			preparedStatement.setInt(1, classID);
			preparedStatement.setInt(2, studentID);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					int count = resultSet.getInt("count");
					return count > 0;
				}
			}
		} catch (Exception e) {
			logger.severe("Lỗi kiểm tra sinh viên tồn tại: " + e.getMessage());
		}
		return false;
	}

	//Tìm kiếm
	public static List<StudentClass> searchByStudentOrClass(String searchValue) {
		List<StudentClass> studentClasses = new ArrayList<>();

		try (Connection conn = ConnectDatabase.checkConnect(); PreparedStatement ps = conn.prepareStatement(SQL_SEARCH)) {

			ps.setString(1, "%" + searchValue + "%");
			ps.setString(2, "%" + searchValue + "%");

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					studentClasses.add(mapStudentClass(rs));
				}
			}
		} catch (SQLException e) {
			logger.severe("Lỗi khi tìm kiếm StudentClasses theo tên lớp hoặc tên sinh viên: " + e.getMessage());
		}
		return studentClasses;
	}

	private static StudentClass mapStudentClass(ResultSet rs) throws SQLException {
		StudentClass sc = new StudentClass();
		sc.setStudentClassID(rs.getInt("StudentClassID"));
		sc.setClassID(rs.getInt("ClassID"));
		sc.setStudentID(rs.getInt("StudentID"));
		sc.setStatus(rs.getString("Status"));
		sc.setClassName(rs.getString("ClassName"));
		sc.setStudentCode(rs.getString("StudentCode"));
		sc.setStudentName(rs.getString("StudentName"));
		return sc;
	}
}
