package models.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import common.ConnectDatabase;
import models.bean.Department;
import models.bean.Student;
import models.bean.Account;


public class StudentDAO {
	 private static final Logger logger = Logger.getLogger(StudentDAO.class.getName());

    // Các câu SQL sử dụng trong DAO
    public static final String SQL_SELECT_ALL_STUDENTS =
        "SELECT s.*, d.* FROM Students s " +
        "JOIN Departments d ON s.DepartmentID = d.DepartmentID";

    public static final String SQL_SELECT_STUDENT_BY_ID =
        "SELECT s.*, d.* " +
		"FROM Students s JOIN Departments d ON s.DepartmentID = d.DepartmentID" +
		"WHERE s.StudentID = ?";

    public static final String SQL_SEARCH_STUDENTS_BY_NAME_MAJOR_ADDRESS =
		"SELECT s.*, d.* FROM Students s " +
        "JOIN Departments d ON s.DepartmentID = d.DepartmentID WHERE 1=1";

    public static final String SQL_INSERT_STUDENT =
	    "INSERT INTO Students (StudentCode, FirstName, LastName, DateOfBirth, Email, Phone, Address, EnrollmentYear, MajorName, Avatar, AccountID, DepartmentID) " +
	    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String SQL_INSERT_ACCOUNT =
        "INSERT INTO Accounts (Username, Password, Email, RoleID) VALUES (?, ?, ?, ?)";

    public static final String SQL_UPDATE_STUDENT =
	    "UPDATE Students SET FirstName = ?, LastName = ?, DateOfBirth = ?, Email = ?, Phone = ?, Address = ?, " +
	    "EnrollmentYear = ?, MajorName = ?, Avatar = ?, DepartmentID = ? WHERE StudentID = ?";

    public static final String SQL_DELETE_STUDENT =
        "DELETE FROM Students WHERE StudentID = ?";

    public static final String SQL_DELETE_ACCOUNT =
		"DELETE FROM Accounts WHERE AccountID = ?";

    public static final String SQL_CHECK_EMAIL_EXISTS =
        "SELECT COUNT(*) FROM Accounts WHERE Email = ?";

    public static final String SQL_CHECK_STUDENTCODE =
        "SELECT COUNT(*) FROM Students WHERE StudentCode = ?";

    public static final String SQL_CHECK_STUDENTCODE_UPDATE =
        "SELECT COUNT(*) FROM Students WHERE StudentCode = ? AND StudentID = ?";

    public static final String SQL_SELECT_ACCOUNT_ID_FROM_STUDENT =
        "SELECT AccountID FROM Students WHERE StudentID = ?";

    public static final String SQL_UPDATE_STUDENT_ACCOUNT_ID =
        "UPDATE Students SET AccountID = ? WHERE StudentID = ?";

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
        student.setAccountID(rs.getInt("AccountID"));
        student.setAvatar(rs.getString("Avatar"));
        student.setStudentCode(rs.getString("StudentCode"));

        // Thiết lập Department
        Department department = new Department();
        department.setDepartmentID(rs.getInt("DepartmentID"));
        department.setDepartmentName(rs.getString("DepartmentName"));
        student.setDepartment(department);
        student.setDepartmentID(rs.getInt("DepartmentID"));

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
            logger.severe("Error getting all students: " + e.getMessage());
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
            logger.severe("Error searching students: " + e.getMessage());
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
            logger.severe("Error getting teacher by ID: " + e.getMessage());
        }
        return null;
    }

    // Kiểm tra email có duy nhất không
    private static boolean isEmailUnique(String email) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_CHECK_EMAIL_EXISTS)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            logger.severe("Email uniqueness check failed: " + e.getMessage());
        }
        return false;
    }

    public static boolean checkStudentCode(String studentCode) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_CHECK_STUDENTCODE)) {
            stmt.setString(1, studentCode);
            System.out.println("Executing query for studentCode: " + studentCode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("StudentCode count: " + count);
                    return count > 0; // Trả về true nếu mã sinh viên tồn tại
                } else {
                    System.out.println("No rows returned for studentCode: " + studentCode);
                    return false; // Trả về false nếu không có dòng nào
                }
            }
        } catch (SQLException e) {
            logger.severe("StudentCode uniqueness check failed: " + e.getMessage());
        }
        return false; // Trả về false nếu có lỗi
    }


    public static boolean checkStudentCode(String studentCode, int excludeStudentID) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_CHECK_STUDENTCODE_UPDATE)) {
            stmt.setString(1, studentCode);
            stmt.setInt(2, excludeStudentID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("StudentCode count for update: " + count);
                    return count == 0; // Trả về true nếu không có bản ghi nào
                }
            }
        } catch (SQLException e) {
            logger.severe("StudentCode uniqueness check failed: " + e.getMessage());
        }
        return false;
    }


    public static boolean createStudentWithAccount(Student student) {
        if (!isEmailUnique(student.getEmail())) {
            logger.severe("Email đã tồn tại");
            return false;
        }

        Connection conn = null;
        PreparedStatement stmtStudent = null;
        PreparedStatement stmtAccount = null;

        try {
            conn = ConnectDatabase.checkConnect();
            conn.setAutoCommit(false); // Bắt đầu transaction

            String username = student.getStudentCode();
            SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
            String password = spd.format(student.getDateOfBirth());

            Account account = new Account(username, password, student.getEmail(), null);
            // Tạo tài khoản trước
            stmtAccount = conn.prepareStatement(SQL_INSERT_ACCOUNT, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtAccount.setString(1, account.getUsername());
            stmtAccount.setString(2, account.getPassword());
            stmtAccount.setString(3, account.getEmail());
            stmtAccount.setInt(4, 3); // Role ID

            int accountInserted = stmtAccount.executeUpdate();
            if (accountInserted <= 0) {
                throw new SQLException("Không thể thêm tài khoản.");
            }

            // Lấy AccountID mới tạo
            int accountID;
            try (ResultSet generatedKeys = stmtAccount.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    accountID = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Không thể lấy AccountID.");
                }
            }

            // Thêm sinh viên với AccountID
            stmtStudent = conn.prepareStatement(SQL_INSERT_STUDENT, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtStudent.setString(1, student.getStudentCode());
            stmtStudent.setString(2, student.getFirstName());
            stmtStudent.setString(3, student.getLastName());
            stmtStudent.setDate(4, new java.sql.Date(student.getDateOfBirth().getTime()));
            stmtStudent.setString(5, student.getEmail());
            stmtStudent.setString(6, student.getPhone());
            stmtStudent.setString(7, student.getAddress());
            stmtStudent.setDate(8, new java.sql.Date(student.getEnrollmentYear().getTime()));
            stmtStudent.setString(9, student.getMajorName());
            stmtStudent.setString(10, student.getAvatar());
            stmtStudent.setInt(11, accountID); // Gán AccountID cho sinh viên
            stmtStudent.setInt(12, student.getDepartmentID());

            int studentInserted = stmtStudent.executeUpdate();
            if (studentInserted <= 0) {
                throw new SQLException("Không thể thêm sinh viên.");
            }

            conn.commit(); // Commit transaction
            return true;

        } catch (SQLException e) {
            logger.severe("Error creating student with account: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback nếu xảy ra lỗi
                } catch (SQLException rollbackEx) {
                    logger.severe("Rollback error: " + rollbackEx.getMessage());
                }
            }
            return false;

        } finally {
            // Đóng tài nguyên
            try {
                if (stmtStudent != null) stmtStudent.close();
                if (stmtAccount != null) stmtAccount.close();
                if (conn != null) {
                    conn.setAutoCommit(true); // Đặt lại AutoCommit
                    conn.close();
                }
            } catch (SQLException ex) {
                logger.severe("Error closing resources: " + ex.getMessage());
            }
        }
    }



    // Xóa sinh viên và tài khoản liên kết
    public static boolean deleteStudent(int studentID) {
        Connection conn = null;
        PreparedStatement stmtStudent = null;
        PreparedStatement stmtAccount = null;

        try {
            conn = ConnectDatabase.checkConnect();
            conn.setAutoCommit(false);

            // Lấy AccountID từ Student
            int accountID = -1;
            try (PreparedStatement stmtGetAccountID = conn.prepareStatement(SQL_SELECT_ACCOUNT_ID_FROM_STUDENT)) {
                stmtGetAccountID.setInt(1, studentID);
                try (ResultSet rs = stmtGetAccountID.executeQuery()) {
                    if (rs.next()) {
                        accountID = rs.getInt("AccountID");
                    }
                }
            }

            // Xóa tài khoản
            if (accountID > 0) {
                stmtAccount = conn.prepareStatement(SQL_DELETE_ACCOUNT);
                stmtAccount.setInt(1, accountID);
                stmtAccount.executeUpdate();
            }

            // Xóa sinh viên
            stmtStudent = conn.prepareStatement(SQL_DELETE_STUDENT);
            stmtStudent.setInt(1, studentID);
            int rowsDeleted = stmtStudent.executeUpdate();

            conn.commit();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            logger.severe("Error deleting student: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    logger.severe("Rollback error: " + rollbackEx.getMessage());
                }
            }
            return false;

        } finally {
            try {
                if (stmtStudent != null) stmtStudent.close();
                if (stmtAccount != null) stmtAccount.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException ex) {
                logger.severe("Error closing resources: " + ex.getMessage());
            }
        }
    }

    public static boolean updateStudent(Student student) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmtStudent = conn.prepareStatement(SQL_UPDATE_STUDENT)) {

            stmtStudent.setString(1, student.getFirstName());
            stmtStudent.setString(2, student.getLastName());
            stmtStudent.setDate(3, new java.sql.Date(student.getDateOfBirth().getTime()));
            stmtStudent.setString(4, student.getEmail());
            stmtStudent.setString(5, student.getPhone());
            stmtStudent.setString(6, student.getAddress());
            stmtStudent.setDate(7, new java.sql.Date(student.getEnrollmentYear().getTime()));
            stmtStudent.setString(8, student.getMajorName());
            stmtStudent.setString(9, student.getAvatar());
            stmtStudent.setInt(10, student.getDepartmentID());
            stmtStudent.setInt(11, student.getStudentID());

            int studentUpdated = stmtStudent.executeUpdate();
            return studentUpdated > 0;

        } catch (SQLException e) {
            logger.severe("Error updating student: " + e.getMessage());
            return false;
        }
    }
}
