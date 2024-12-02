package models.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import common.ConnectDatabase;
import models.bean.Account;
import models.bean.Department;
import models.bean.Teacher;

public class TeacherDAO {
    private static final Logger logger = Logger.getLogger(TeacherDAO.class.getName());
    private static final Random RANDOM = new Random();

    private static final String SQL_SELECT_ALL_TEACHERS =
        "SELECT t.*, d.DepartmentID, d.DepartmentName FROM Teachers t JOIN Departments d ON d.DepartmentID = t.DepartmentID";
    private static final String SQL_SEARCH_TEACHERS_BY_NAME =
	    "SELECT t.*, d.DepartmentID, d.DepartmentName FROM Teachers t JOIN Departments d ON d.DepartmentID = t.DepartmentID WHERE FirstName LIKE ? OR LastName LIKE ? OR CONCAT(FirstName, ' ', LastName) LIKE ?";
    private static final String SQL_INSERT_TEACHER =
        "INSERT INTO Teachers (FirstName, LastName, Email, Phone, DepartmentID, Office, HireDate, Avatar) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_INSERT_ACCOUNT =
        "INSERT INTO Accounts (Username, Password, Email, RoleID) VALUES (?, ?, ?, ?)";
    private static final String SQL_DELETE_TEACHER =
        "DELETE FROM Teachers WHERE TeacherID = ?";
    private static final String SQL_DELETE_ACCOUNT =
		"DELETE FROM Accounts WHERE AccountID = ?";
    private static final String SQL_UPDATE_TEACHER =
        "UPDATE Teachers SET FirstName = ?, LastName = ?, Email = ?, Phone = ?, DepartmentID = ?, Office = ?, HireDate = ?, Avatar = ? " +
        "WHERE TeacherID = ?";
    private static final String SQL_SELECT_TEACHER_BY_ID =
        "SELECT t.*, d.DepartmentID, d.DepartmentName FROM Teachers t JOIN Departments d ON d.DepartmentID = t.DepartmentID WHERE TeacherID = ?";
    private static final String SQL_CHECK_EMAIL_EXISTS =
        "SELECT COUNT(*) FROM Accounts WHERE Email = ?";
    private static final String SQL_CHECK_USERNAME_EXISTS =
        "SELECT COUNT(*) FROM Accounts WHERE Username = ?";

    private static String generateUsername(String firstName, String lastName) {
        String fullName = removeAccents(firstName + lastName).toLowerCase().replaceAll("\\s+", "");
        int randomNumber = 10 + RANDOM.nextInt(90);
        return fullName + randomNumber;
    }

    private static String removeAccents(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{M}");
        return pattern.matcher(normalized).replaceAll("");
    }

    private static Teacher mapTeacher(ResultSet rs) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setTeacherID(rs.getInt("TeacherID"));
        teacher.setFirstName(rs.getString("FirstName"));
        teacher.setLastName(rs.getString("LastName"));
        teacher.setEmail(rs.getString("Email"));
        teacher.setPhone(rs.getString("Phone"));
        teacher.setDepartmentID(rs.getInt("DepartmentID"));
        teacher.setOffice(rs.getString("Office"));
        teacher.setHireDate(rs.getDate("HireDate"));
        teacher.setAccountID(rs.getInt("AccountID"));
        teacher.setAvatar(rs.getString("Avatar"));

        Department department = new Department();
        department.setDepartmentID(rs.getInt("DepartmentID"));
        department.setDepartmentName(rs.getString("DepartmentName"));

        teacher.setDepartment(department);
        return teacher;
    }

    public static List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL_TEACHERS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                teachers.add(mapTeacher(rs));
            }
        } catch (SQLException e) {
            logger.severe("Error getting all teachers: " + e.getMessage());
        }
        return teachers;
    }

    public static boolean createTeacherWithAccount(Teacher teacher) {

        if (!isEmailUnique(teacher.getEmail())) {
            logger.severe("Email đã tồn tại");
            return false;
        }

        Connection conn = null;
        PreparedStatement stmtTeacher = null;
        PreparedStatement stmtAccount = null;

        try {
            conn = ConnectDatabase.checkConnect();
            conn.setAutoCommit(false);

            // Insert giảng viên
            stmtTeacher = conn.prepareStatement(SQL_INSERT_TEACHER, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtTeacher.setString(1, teacher.getFirstName());
            stmtTeacher.setString(2, teacher.getLastName());
            stmtTeacher.setString(3, teacher.getEmail());
            stmtTeacher.setString(4, teacher.getPhone());
            stmtTeacher.setInt(5, teacher.getDepartmentID());
            stmtTeacher.setString(6, teacher.getOffice());
            stmtTeacher.setDate(7, new java.sql.Date(teacher.getHireDate().getTime()));
            stmtTeacher.setString(8, teacher.getAvatar());

            int teacherInserted = stmtTeacher.executeUpdate();

            int teacherID = -1;
            try (ResultSet generatedKeys = stmtTeacher.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    teacherID = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating teacher failed, no ID obtained.");
                }
            }

            // Tạo tài khoản cho giảng viên
            if (teacherInserted > 0) {
                String username = generateUsername(teacher.getFirstName(), teacher.getLastName());

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String password = sdf.format(teacher.getHireDate());

                // Kiểm tra username duy nhất
                int attempt = 0;
                while (!isUsernameUnique(username) && attempt < 10) {
                	username += RANDOM.nextInt(100);
                    attempt++;
                }


                Account account = new Account(username, password, teacher.getEmail(), null);

                stmtAccount = conn.prepareStatement(SQL_INSERT_ACCOUNT, PreparedStatement.RETURN_GENERATED_KEYS);
                stmtAccount.setString(1, account.getUsername());
                stmtAccount.setString(2, account.getPassword());
                stmtAccount.setString(3, account.getEmail());
                stmtAccount.setInt(4, 2);

                int accountInserted = stmtAccount.executeUpdate();

                int accountID = -1;
                try (ResultSet generatedAccountKeys = stmtAccount.getGeneratedKeys()) {
                    if (generatedAccountKeys.next()) {
                        accountID = generatedAccountKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating account failed, no ID obtained.");
                    }
                }

                // Cập nhật AccountID cho Teacher
                if (accountInserted > 0) {
                    try (PreparedStatement updateTeacherStmt = conn.prepareStatement(
                        "UPDATE Teachers SET AccountID = ? WHERE TeacherID = ?")) {
                        updateTeacherStmt.setInt(1, accountID);
                        updateTeacherStmt.setInt(2, teacherID);
                        updateTeacherStmt.executeUpdate();
                    }
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            logger.severe("Error creating teacher with account: " + e.getMessage());
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
                if (stmtTeacher != null) stmtTeacher.close();
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

    public static boolean deleteTeacher(int teacherID) {
        Connection conn = null;
        PreparedStatement stmtTeacher = null;
        PreparedStatement stmtAccount = null;
        ResultSet rs = null;

        try {
            conn = ConnectDatabase.checkConnect();
            conn.setAutoCommit(false); // Bắt đầu giao dịch

            // 1. Lấy AccountID từ Teacher
            String sqlGetAccountID = "SELECT AccountID FROM Teachers WHERE TeacherID = ?";
            try (PreparedStatement stmtGetAccountID = conn.prepareStatement(sqlGetAccountID)) {
                stmtGetAccountID.setInt(1, teacherID);
                rs = stmtGetAccountID.executeQuery();
                int accountID = -1;
                if (rs.next()) {
                    accountID = rs.getInt("AccountID");
                }

                // 2. Nếu có AccountID, thực hiện xóa Account
                if (accountID > 0) {
                    stmtAccount = conn.prepareStatement(SQL_DELETE_ACCOUNT);
                    stmtAccount.setInt(1, accountID);
                    stmtAccount.executeUpdate();
                }
            }

            // 3. Xóa Teacher
            stmtTeacher = conn.prepareStatement(SQL_DELETE_TEACHER);
            stmtTeacher.setInt(1, teacherID);
            int rowsDeleted = stmtTeacher.executeUpdate();

            conn.commit(); // Xác nhận giao dịch
            return rowsDeleted > 0;

        } catch (SQLException e) {
            logger.severe("Error deleting teacher and account: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); // Hoàn tác nếu có lỗi
                } catch (SQLException rollbackEx) {
                    logger.severe("Rollback error: " + rollbackEx.getMessage());
                }
            }
            return false;

        } finally {
            try {
                if (rs != null) rs.close();
                if (stmtTeacher != null) stmtTeacher.close();
                if (stmtAccount != null) stmtAccount.close();
                if (conn != null) {
                    conn.setAutoCommit(true); // Khôi phục chế độ mặc định
                    conn.close();
                }
            } catch (SQLException ex) {
                logger.severe("Error closing resources: " + ex.getMessage());
            }
        }
    }


    public static boolean updateTeacher(Teacher teacher) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_TEACHER)) {
            stmt.setString(1, teacher.getFirstName());
            stmt.setString(2, teacher.getLastName());
            stmt.setString(3, teacher.getEmail());
            stmt.setString(4, teacher.getPhone());
            stmt.setInt(5, teacher.getDepartmentID());
            stmt.setString(6, teacher.getOffice());
            stmt.setDate(7, new java.sql.Date(teacher.getHireDate().getTime()));
            stmt.setString(8, teacher.getAvatar());
            stmt.setInt(9, teacher.getTeacherID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error updating teacher: " + e.getMessage());
        }
        return false;
    }

    public static Teacher getTeacherById(int teacherID) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_TEACHER_BY_ID)) {
            stmt.setInt(1, teacherID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapTeacher(rs);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting teacher by ID: " + e.getMessage());
        }
        return null;
    }

    public static List<Teacher> searchTeachersByName(String searchTerm) {
        List<Teacher> teachers = new ArrayList<>();

        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_SEARCH_TEACHERS_BY_NAME)) {

            String searchPattern = "%" + searchTerm + "%";

            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    teachers.add(mapTeacher(rs));
                }
            }
        } catch (SQLException e) {
            logger.severe("Error searching teachers: " + e.getMessage());
        }

        return teachers;
    }

    private static boolean isEmailUnique(String email) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_CHECK_EMAIL_EXISTS)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        } catch (SQLException e) {
            logger.severe("Email uniqueness check failed: " + e.getMessage());
        }
        return false;
    }

    private static boolean isUsernameUnique(String username) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_CHECK_USERNAME_EXISTS)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        } catch (SQLException e) {
            logger.severe("Username uniqueness check failed: " + e.getMessage());
        }
        return false;
    }
}
