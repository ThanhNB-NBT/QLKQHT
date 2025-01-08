package models.dao;

import models.bean.StudentClass;
import common.ConnectDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class StudentClassDAO {
    private static final Logger logger = Logger.getLogger(StudentClassDAO.class.getName());

    // Lấy danh sách tất cả StudentClass
    private static final String SQL_GET_ALL_STUDENTCLASS = "SELECT sc.StudentClassID, sc.ClassID, sc.StudentID, sc.Status, "
            + "c.ClassName, s.StudentCode, CONCAT(s.FirstName, ' ', s.LastName) AS StudentName "
            + "FROM StudentClasses sc "
            + "JOIN Classes c ON sc.ClassID = c.ClassID "
            + "JOIN Students s ON sc.StudentID = s.StudentID "
            + "GROUP BY sc.StudentClassID, sc.ClassID, sc.StudentID, sc.Status, c.ClassName, s.StudentCode, s.FirstName, s.LastName "
            + "ORDER BY sc.ClassID";
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
    private static final String SQL_GET_BY_ID = "SELECT sc.StudentClassID, sc.ClassID, sc.StudentID, sc.Status, "
            + "c.ClassName, s.StudentCode, CONCAT(s.FirstName, ' ', s.LastName) AS StudentName "
            + "FROM StudentClasses sc "
            + "JOIN Classes c ON sc.ClassID = c.ClassID "
            + "JOIN Students s ON sc.StudentID = s.StudentID "
            + "WHERE sc.StudentClassID = ? "
            + "ORDER BY sc.ClassID";
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
        try {
            conn = ConnectDatabase.checkConnect();
            conn.setAutoCommit(false);

            int newStudentClassID = insertStudentClass(conn, studentClass);
            if (newStudentClassID > 0) {
                if (createGradeRecord(conn, newStudentClassID) &&
                    updateRegisteredStudents(conn, studentClass.getClassID(), 1)) {
                    conn.commit();
                    return true;
                }
            }
            conn.rollback();
            return false;
        } catch (SQLException e) {
            handleTransactionError(conn, e, "Lỗi khi thêm StudentClass và các bản ghi liên quan");
            return false;
        } finally {
            closeConnection(conn);
        }
    }

    // Cập nhật StudentClass
    private static final String SQL_UPDATE_STUDENTCLASS = "UPDATE StudentClasses SET Status = ? WHERE StudentClassID = ?";
    public static boolean updateStudentClass(StudentClass studentClass) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE_STUDENTCLASS)) {

            pstmt.setString(1, studentClass.getStatus());
            pstmt.setInt(2, studentClass.getStudentClassID());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.severe("Lỗi khi cập nhật StudentClass: " + e.getMessage());
            return false;
        }
    }

    // Xóa StudentClass
    public static boolean deleteStudentClass(int studentClassID, int classID) {
        Connection conn = null;
        try {
            conn = ConnectDatabase.checkConnect();
            conn.setAutoCommit(false);

            if (deleteGradeRecord(conn, studentClassID) &&
                deleteStudentClassRecord(conn, studentClassID) &&
                updateRegisteredStudents(conn, classID, -1)) {

                conn.commit();
                return true;
            }
            conn.rollback();
            return false;
        } catch (SQLException e) {
            handleTransactionError(conn, e, "Lỗi khi xóa StudentClass và các bản ghi liên quan");
            return false;
        } finally {
            closeConnection(conn);
        }
    }

    // Tìm kiếm StudentClass
    private static final String SQL_SEARCH = "SELECT sc.StudentClassID, sc.ClassID, sc.StudentID, sc.Status, "
            + "c.ClassName, s.StudentCode, CONCAT(s.FirstName, ' ', s.LastName) AS StudentName "
            + "FROM StudentClasses sc "
            + "JOIN Classes c ON sc.ClassID = c.ClassID "
            + "JOIN Students s ON sc.StudentID = s.StudentID "
            + "WHERE c.ClassName LIKE ? OR CONCAT(s.FirstName, ' ', s.LastName) LIKE ? "
            + "ORDER BY sc.ClassID";
    public static List<StudentClass> searchByStudentOrClass(String searchValue) {
        List<StudentClass> studentClasses = new ArrayList<>();
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement ps = conn.prepareStatement(SQL_SEARCH)) {

            ps.setString(1, "%" + searchValue + "%");
            ps.setString(2, "%" + searchValue + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    studentClasses.add(mapStudentClass(rs));
                }
            }
        } catch (SQLException e) {
            logger.severe("Lỗi khi tìm kiếm StudentClasses: " + e.getMessage());
        }
        return studentClasses;
    }

    // Kiểm tra trùng lớp và khóa học
    private static final String SQL_CHECK_DUPLICATE =
            "SELECT COUNT(*) AS count " +
            "FROM StudentClasses sc " +
            "JOIN Classes c ON sc.ClassID = c.ClassID " +
            "WHERE sc.StudentID = ? AND (c.ClassID = ? OR c.CourseID = ?)";

    public static boolean isDuplicate(Integer classID, Integer studentID, Integer courseID) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement ps = conn.prepareStatement(SQL_CHECK_DUPLICATE)) {

            ps.setInt(1, studentID);
            ps.setInt(2, classID);
            ps.setInt(3, courseID);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            logger.severe("Lỗi kiểm tra trùng lặp: " + e.getMessage());
            return false;
        }
    }

    // Lấy ClassID theo StudentClassID
    private static final String SQL_GET_CLASSID_BY_STUDENTCLASSID = "SELECT ClassID FROM StudentClasses WHERE StudentClassID = ?";
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
            logger.severe("Lỗi khi lấy ClassID: " + e.getMessage());
        }
        return null;
    }

    // Các phương thức helper private

    //Thêm mới
    private static final String SQL_CREATE_STUDENTCLASS = "INSERT INTO StudentClasses (ClassID, StudentID, Status) VALUES (?, ?, ?)";
    private static int insertStudentClass(Connection conn, StudentClass studentClass) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_CREATE_STUDENTCLASS, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, studentClass.getClassID());
            pstmt.setInt(2, studentClass.getStudentID());
            pstmt.setString(3, studentClass.getStatus());

            if (pstmt.executeUpdate() > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        }
        return -1;
    }

    //Thêm vào bảng Grade
    private static final String SQL_CREATE_GRADE = "INSERT INTO Grades (StudentClassID, MidtermScore, AttendanceScore, FinalExamScore) VALUES (?, 0, 0, 0)";
    private static boolean createGradeRecord(Connection conn, int studentClassID) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_CREATE_GRADE)) {
            pstmt.setInt(1, studentClassID);
            return pstmt.executeUpdate() > 0;
        }
    }

    //Xoá ở bảng Grade
    private static final String SQL_DELETE_GRADE = "DELETE FROM Grades WHERE StudentClassID = ?";
    private static boolean deleteGradeRecord(Connection conn, int studentClassID) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE_GRADE)) {
            pstmt.setInt(1, studentClassID);
            return pstmt.executeUpdate() >= 0; // Trả về true ngay cả khi không có bản ghi nào bị xóa
        }
    }

    //Xoá
    private static final String SQL_DELETE_STUDENTCLASS = "DELETE FROM StudentClasses WHERE StudentClassID = ?";
    private static boolean deleteStudentClassRecord(Connection conn, int studentClassID) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE_STUDENTCLASS)) {
            pstmt.setInt(1, studentClassID);
            return pstmt.executeUpdate() > 0;
        }
    }

    //Thêm số lượng sinh viên đăng kí trong lớp ở bảng Class
    private static final String SQL_UPDATE_REGISTERED_STUDENTS = "UPDATE Classes SET RegisteredStudents = RegisteredStudents + ? WHERE ClassID = ?";
    private static boolean updateRegisteredStudents(Connection conn, int classID, int change) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE_REGISTERED_STUDENTS)) {
            pstmt.setInt(1, change); // +1 cho thêm mới, -1 cho xóa
            pstmt.setInt(2, classID);
            return pstmt.executeUpdate() > 0;
        }
    }

    //kiểm tra lỗi và quay lại sự kiện
    private static void handleTransactionError(Connection conn, SQLException e, String errorMessage) {
        logger.severe(errorMessage + ": " + e.getMessage());
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException ex) {
            logger.severe("Lỗi rollback: " + ex.getMessage());
        }
    }

    //đóng kết nối phi
    private static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            logger.severe("Lỗi khi đóng kết nối: " + e.getMessage());
        }
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

    public static String getStudentClassID(int studentID, int classID) {
        String sql = "SELECT StudentClassID FROM StudentClasses WHERE StudentID = ? AND ClassID = ?";
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentID);
            stmt.setInt(2, classID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("StudentClassID");
            }
        } catch (SQLException e) {
            logger.severe("Lỗi khi lấy StudentClassID: " + e.getMessage());
        }
        return null;
    }

}