package models.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import common.ConnectDatabase;
import models.bean.Account;
import models.bean.Department;
import models.bean.Student;

public class StudentDAO {
    private static final Logger logger = Logger.getLogger(StudentDAO.class.getName());

    public static final String SQL_SELECT_ALL_STUDENTS =
	    "SELECT s.*, d.DepartmentName, a.Avatar FROM Students s " +
	    "LEFT JOIN Departments d ON s.DepartmentID = d.DepartmentID " +
	    "LEFT JOIN Accounts a ON s.AccountID = a.AccountID";

	public static final String SQL_SELECT_STUDENT_BY_ID =
	    "SELECT s.*, d.DepartmentName, a.Avatar FROM Students s " +
	    "LEFT JOIN Departments d ON s.DepartmentID = d.DepartmentID " +
	    "LEFT JOIN Accounts a ON s.AccountID = a.AccountID " +
	    "WHERE s.StudentID = ?";

	public static final String SQL_SEARCH_STUDENTS_BY_NAME_MAJOR_ADDRESS =
	    "SELECT s.*, d.DepartmentName, a.Avatar FROM Students s " +
	    "LEFT JOIN Departments d ON s.DepartmentID = d.DepartmentID " +
	    "LEFT JOIN Accounts a ON s.AccountID = a.AccountID WHERE 1=1";

    public static final String SQL_INSERT_STUDENT =
        "INSERT INTO Students (StudentCode, FirstName, LastName, DateOfBirth, Email, Phone, Address, EnrollmentYear, MajorName, DepartmentID, AccountID) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String SQL_UPDATE_STUDENT =
        "UPDATE Students SET FirstName = ?, LastName = ?, DateOfBirth = ?, Email = ?, Phone = ?, Address = ?, " +
        "EnrollmentYear = ?, MajorName = ?, DepartmentID = ? WHERE StudentID = ?";

    public static final String SQL_DELETE_ACCOUNT =
	    "DELETE FROM Accounts WHERE AccountID = (SELECT AccountID FROM Students WHERE StudentID = ?)";

	public static final String SQL_DELETE_STUDENT =
	    "DELETE FROM Students WHERE StudentID = ?";

    public static final String SQL_CHECK_STUDENTCODE =
        "SELECT COUNT(*) FROM Students WHERE StudentCode = ?";

    public static final String SQL_CHECK_STUDENTCODE_UPDATE =
        "SELECT COUNT(*) FROM Students WHERE StudentCode = ? AND StudentID = ?";

    private static Student mapStudent(ResultSet rs) throws SQLException {

        Student student = new Student();
        student.setStudentID(rs.getInt("StudentID"));
        student.setFirstName(rs.getString("FirstName"));
        student.setLastName(rs.getString("LastName"));
        student.setDateOfBirth(rs.getDate("DateOfBirth"));
        student.setEmail(rs.getString("Email"));
        student.setPhone(rs.getString("Phone"));
        student.setAddress(rs.getString("Address"));
        student.setEnrollmentYear(rs.getDate("EnrollmentYear"));
        student.setMajorName(rs.getString("MajorName"));
        student.setStudentCode(rs.getString("StudentCode"));

        // Thiết lập Department
        Department department = new Department();
        department.setDepartmentID(rs.getInt("DepartmentID"));
        department.setDepartmentName(rs.getString("DepartmentName"));
        student.setDepartment(department);
        student.setDepartmentID(rs.getInt("DepartmentID"));

        Account account = new Account();
        account.setAvatar(rs.getString("Avatar"));
        student.setAccount(account);
        student.setAccountID(rs.getInt("AccountID"));

        return student;
    }

    public static List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL_STUDENTS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                students.add(mapStudent(rs));
            }
        } catch (SQLException e) {
            logger.severe("Lỗi khi lấy tất cả sinh viên: " + e.getMessage());
        }
        return students;
    }

    public static List<Student> searchStudents(String name, String major, String address) {
        List<Student> students = new ArrayList<>();
        StringBuilder query = new StringBuilder(SQL_SEARCH_STUDENTS_BY_NAME_MAJOR_ADDRESS);

        if (name != null && !name.isEmpty()) {
            query.append(" AND CONCAT(s.FirstName, ' ', s.LastName) LIKE ?");
        }
        if (major != null && !major.isEmpty()) {
            query.append(" AND s.MajorName LIKE ?");
        }
        if (address != null && !address.isEmpty()) {
            query.append(" AND s.Address LIKE ?");
        }

        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            int paramIndex = 1;

            if (name != null && !name.isEmpty()) {
                stmt.setString(paramIndex++, "%" + name + "%");
            }
            if (major != null && !major.isEmpty()) {
                stmt.setString(paramIndex++, "%" + major + "%");
            }
            if (address != null && !address.isEmpty()) {
                stmt.setString(paramIndex++, "%" + address + "%");
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(mapStudent(rs));
                }
            }
        } catch (SQLException e) {
            logger.severe("Lỗi khi thực hiện tìm kiếm: " + e.getMessage());
        }
        return students;
    }

    public static Student getStudentById(int studentID) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_STUDENT_BY_ID)) {
            stmt.setInt(1, studentID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapStudent(rs);
                }
            }
        } catch (SQLException e) {
            logger.severe("Lỗi khi lấy sinh viên theo ID: " + e.getMessage());
        }
        return null;
    }

    public static boolean checkStudentCode(String studentCode) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_CHECK_STUDENTCODE)) {
            stmt.setString(1, studentCode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Trả về true nếu mã sinh viên tồn tại
                }
            }
        } catch (SQLException e) {
            logger.severe("Lỗi khi kiểm tra StudentCode: " + e.getMessage());
        }
        return false;
    }

    public static boolean checkStudentCode(String studentCode, int excludeStudentID) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_CHECK_STUDENTCODE_UPDATE)) {
            stmt.setString(1, studentCode);
            stmt.setInt(2, excludeStudentID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0; // Trả về true nếu không có bản ghi nào
                }
            }
        } catch (SQLException e) {
            logger.severe("Lỗi khi kiểm tra StudentCode(Update): " + e.getMessage());
        }
        return false;
    }

    public static boolean createStudent(Student student) {
        Connection conn = null;
        PreparedStatement insertStudentStmt = null;

        try {
            // Kết nối cơ sở dữ liệu
            conn = ConnectDatabase.checkConnect();

            // 1. Thêm sinh viên vào bảng Students
            insertStudentStmt = conn.prepareStatement(SQL_INSERT_STUDENT);
            insertStudentStmt.setString(1, student.getStudentCode());
            insertStudentStmt.setString(2, student.getFirstName());
            insertStudentStmt.setString(3, student.getLastName());
            insertStudentStmt.setDate(4, new java.sql.Date(student.getDateOfBirth().getTime()));
            insertStudentStmt.setString(5, student.getEmail());
            insertStudentStmt.setString(6, student.getPhone());
            insertStudentStmt.setString(7, student.getAddress());
            insertStudentStmt.setDate(8, new java.sql.Date(student.getEnrollmentYear().getTime()));
            insertStudentStmt.setString(9, student.getMajorName());
            insertStudentStmt.setInt(10, student.getDepartmentID());
            insertStudentStmt.setInt(11, student.getAccountID()); // Thêm AccountID (đã được tạo ở servlet)

            int rowsInserted = insertStudentStmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            logger.severe("Lỗi khi thêm sinh viên: " + e.getMessage());
        } finally {
            // Đóng tài nguyên
            try {
                if (insertStudentStmt != null) insertStudentStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException closeEx) {
                logger.severe("Lỗi khi đóng tài nguyên: " + closeEx.getMessage());
            }
        }
        return false;
    }

    public static boolean deleteStudent(int studentID) {
        try (Connection conn = ConnectDatabase.checkConnect()) {
            // Xóa tài khoản liên kết
            try (PreparedStatement deleteAccountStmt = conn.prepareStatement(SQL_DELETE_ACCOUNT)) {
                deleteAccountStmt.setInt(1, studentID);
                deleteAccountStmt.executeUpdate();
            }

            // Xóa sinh viên
            try (PreparedStatement deleteStudentStmt = conn.prepareStatement(SQL_DELETE_STUDENT)) {
                deleteStudentStmt.setInt(1, studentID);
                int rowsDeleted = deleteStudentStmt.executeUpdate();
                return rowsDeleted > 0;
            }
        } catch (SQLException e) {
            logger.severe("Lỗi khi xóa sinh viên: " + e.getMessage());
            return false;
        }
    }


    public static boolean updateStudent(Student student) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_STUDENT)) {

            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setDate(3, new java.sql.Date(student.getDateOfBirth().getTime()));
            stmt.setString(4, student.getEmail());
            stmt.setString(5, student.getPhone());
            stmt.setString(6, student.getAddress());
            stmt.setDate(7, new java.sql.Date(student.getEnrollmentYear().getTime()));
            stmt.setString(8, student.getMajorName());
            stmt.setInt(9, student.getDepartmentID());
            stmt.setInt(10, student.getStudentID());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            logger.severe("Lỗi khi cập nhật sinh viên: " + e.getMessage());
            return false;
        }
    }
}