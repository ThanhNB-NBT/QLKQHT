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
import models.bean.Teacher;

public class TeacherDAO {
    private static final Logger logger = Logger.getLogger(TeacherDAO.class.getName());

    public static final String SQL_SELECT_ALL_TEACHERS =
        "SELECT t.*, d.DepartmentName, a.Avatar FROM Teachers t " +
        "LEFT JOIN Departments d ON t.DepartmentID = d.DepartmentID " +
        "LEFT JOIN Accounts a ON t.AccountID = a.AccountID";

    public static final String SQL_SELECT_TEACHER_BY_ID =
        "SELECT t.*, d.DepartmentName, a.Avatar FROM Teachers t " +
        "LEFT JOIN Departments d ON t.DepartmentID = d.DepartmentID " +
        "LEFT JOIN Accounts a ON t.AccountID = a.AccountID " +
        "WHERE t.TeacherID = ?";

    public static final String SQL_SEARCH_TEACHERS_BY_NAME_EMAIL =
        "SELECT t.*, d.DepartmentName, a.Avatar FROM Teachers t " +
        "LEFT JOIN Departments d ON t.DepartmentID = d.DepartmentID " +
        "LEFT JOIN Accounts a ON t.AccountID = a.AccountID WHERE 1=1";

    public static final String SQL_INSERT_TEACHER =
        "INSERT INTO Teachers (FirstName, LastName, Email, Phone, DepartmentID, Office, HireDate, AccountID) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String SQL_UPDATE_TEACHER =
        "UPDATE Teachers SET FirstName = ?, LastName = ?, Email = ?, Phone = ?, DepartmentID = ?, " +
        "Office = ?, HireDate = ? WHERE TeacherID = ?";

    public static final String SQL_DELETE_ACCOUNT =
        "DELETE FROM Accounts WHERE AccountID = (SELECT AccountID FROM Teachers WHERE TeacherID = ?)";

    public static final String SQL_DELETE_TEACHER =
        "DELETE FROM Teachers WHERE TeacherID = ?";

    private static Teacher mapTeacher(ResultSet rs) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setTeacherID(rs.getInt("TeacherID"));
        teacher.setFirstName(rs.getString("FirstName"));
        teacher.setLastName(rs.getString("LastName"));
        teacher.setEmail(rs.getString("Email"));
        teacher.setPhone(rs.getString("Phone"));
        teacher.setOffice(rs.getString("Office"));
        teacher.setHireDate(rs.getDate("HireDate"));

        Department department = new Department();
        department.setDepartmentID(rs.getInt("DepartmentID"));
        department.setDepartmentName(rs.getString("DepartmentName"));
        teacher.setDepartment(department);

        teacher.setDepartmentID(rs.getInt("DepartmentID"));

        Account account = new Account();
        account.setAvatar(rs.getString("Avatar"));
        teacher.setAccount(account);
        teacher.setAccountID(rs.getInt("AccountID"));

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

    public static List<Teacher> searchTeachers(String name, String email) {
        List<Teacher> teachers = new ArrayList<>();
        StringBuilder query = new StringBuilder(SQL_SEARCH_TEACHERS_BY_NAME_EMAIL);

        if (name != null && !name.isEmpty()) {
            query.append(" AND CONCAT(t.FirstName, ' ', t.LastName) LIKE ?");
        }
        if (email != null && !email.isEmpty()) {
            query.append(" AND t.Email LIKE ?");
        }

        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            int paramIndex = 1;

            if (name != null && !name.isEmpty()) {
                stmt.setString(paramIndex++, "%" + name + "%");
            }
            if (email != null && !email.isEmpty()) {
                stmt.setString(paramIndex++, "%" + email + "%");
            }

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

    public static boolean createTeacher(Teacher teacher) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_TEACHER)) {

            stmt.setString(1, teacher.getFirstName());
            stmt.setString(2, teacher.getLastName());
            stmt.setString(3, teacher.getEmail());
            stmt.setString(4, teacher.getPhone());
            stmt.setInt(5, teacher.getDepartmentID());
            stmt.setString(6, teacher.getOffice());
            stmt.setDate(7, new java.sql.Date(teacher.getHireDate().getTime()));
            stmt.setInt(8, teacher.getAccountID());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            logger.severe("Error creating teacher: " + e.getMessage());
        }
        return false;
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
            stmt.setInt(8, teacher.getTeacherID());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            logger.severe("Error updating teacher: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteTeacher(int teacherID) {
        try (Connection conn = ConnectDatabase.checkConnect()) {
            // Xóa tài khoản liên kết
            try (PreparedStatement deleteAccountStmt = conn.prepareStatement(SQL_DELETE_ACCOUNT)) {
                deleteAccountStmt.setInt(1, teacherID);
                deleteAccountStmt.executeUpdate();
            }

            // Xóa giáo viên
            try (PreparedStatement deleteTeacherStmt = conn.prepareStatement(SQL_DELETE_TEACHER)) {
                deleteTeacherStmt.setInt(1, teacherID);
                int rowsDeleted = deleteTeacherStmt.executeUpdate();
                return rowsDeleted > 0;
            }
        } catch (SQLException e) {
            logger.severe("Error deleting teacher: " + e.getMessage());
            return false;
        }
    }
}
