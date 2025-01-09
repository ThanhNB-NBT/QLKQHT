package models.dao;

import models.bean.StudentClass;
import common.ConnectDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TeacherStudentClassDAO {
    private static final Logger logger = Logger.getLogger(TeacherStudentClassDAO.class.getName());

    // Lấy danh sách sinh viên theo TeacherID
    private static final String SQL_GET_BY_TEACHER_ID = "SELECT sc.StudentClassID, sc.ClassID, sc.StudentID, sc.Status, "
            + "c.ClassName, s.StudentCode, CONCAT(s.FirstName, ' ', s.LastName) AS StudentName "
            + "FROM StudentClasses sc "
            + "JOIN Classes c ON sc.ClassID = c.ClassID "
            + "JOIN Students s ON sc.StudentID = s.StudentID "
            + "JOIN Teachers t ON c.TeacherID = t.TeacherID "
            + "WHERE t.TeacherID = ? "
            + "ORDER BY sc.ClassID";
    public static List<StudentClass> getStudentsByTeacherId(String teacherID) {
        List<StudentClass> studentClasses = new ArrayList<>();
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement pstmt = conn.prepareStatement(SQL_GET_BY_TEACHER_ID)) {

            pstmt.setString(1, teacherID);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    studentClasses.add(mapStudentClass(rs));
                }
            }
        } catch (SQLException e) {
            logger.severe("Lỗi khi lấy danh sách sinh viên theo TeacherID: " + e.getMessage());
        }
        return studentClasses;
    }

    // Tìm kiếm danh sách sinh viên theo TeacherID và tên sinh viên
    private static final String SQL_SEARCH_BY_STUDENT_NAME = "SELECT sc.StudentClassID, sc.ClassID, sc.StudentID, sc.Status, "
            + "c.ClassName, s.StudentCode, CONCAT(s.FirstName, ' ', s.LastName) AS StudentName "
            + "FROM StudentClasses sc "
            + "JOIN Classes c ON sc.ClassID = c.ClassID "
            + "JOIN Students s ON sc.StudentID = s.StudentID "
            + "JOIN Teachers t ON c.TeacherID = t.TeacherID "
            + "WHERE t.TeacherID = ? AND CONCAT(s.FirstName, ' ', s.LastName) LIKE ? "
            + "ORDER BY sc.ClassID";
    public static List<StudentClass> searchStudentsByTeacherId(String teacherID, String studentName) {
        List<StudentClass> studentClasses = new ArrayList<>();
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement pstmt = conn.prepareStatement(SQL_SEARCH_BY_STUDENT_NAME)) {

            pstmt.setString(1, teacherID);
            pstmt.setString(2, "%" + studentName + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    studentClasses.add(mapStudentClass(rs));
                }
            }
        } catch (SQLException e) {
            logger.severe("Lỗi khi tìm kiếm danh sách sinh viên theo TeacherID: " + e.getMessage());
        }
        return studentClasses;
    }

    // Cập nhật trạng thái (Status) của StudentClass
    private static final String SQL_UPDATE_STATUS = "UPDATE StudentClasses SET Status = ? WHERE StudentClassID = ?";
    public static boolean updateStudentClassStatus(Integer studentClassID, String status) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE_STATUS)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, studentClassID);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.severe("Lỗi khi cập nhật trạng thái của StudentClass: " + e.getMessage());
            return false;
        }
    }

    // Phương thức helper để map dữ liệu từ ResultSet sang đối tượng StudentClass
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
