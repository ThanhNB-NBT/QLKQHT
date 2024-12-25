package models.dao;

import models.bean.Class;
import models.bean.Grade;
import common.ConnectDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class GradeDAO {
	private static final Logger logger = Logger.getLogger(GradeDAO.class.getName());

    // Lấy danh sách lớp học (bao gồm ClassID và ClassName) theo giảng viên hoặc tất cả lớp
    public static List<Class> getClassesByTeacher(String teacherID) {
        List<Class> classList = new ArrayList<>();
        String sql = (teacherID != null) ?
                     "SELECT DISTINCT c.ClassID, c.ClassName FROM Classes c " +
                     "JOIN Teachers t ON c.TeacherID = t.TeacherID " +
                     "WHERE t.TeacherID = ?" :
                     "SELECT DISTINCT ClassID, ClassName FROM Classes"; // Quản trị viên xem tất cả lớp

        try (Connection conn = ConnectDatabase.checkConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            if (teacherID != null) {
                ps.setString(1, teacherID); // Lọc theo TeacherID nếu là giảng viên
            }
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Class classInfo = new Class();
                classInfo.setClassID(rs.getInt("ClassID"));
                classInfo.setClassName(rs.getString("ClassName"));
                classList.add(classInfo);
            }
        } catch (SQLException e) {
           logger.severe("Lỗi lấy danh sách lớp theo giảng viên: " + e.getMessage());
        }
        return classList;
    }

    // Lấy danh sách sinh viên và điểm theo ClassID
    public static List<Grade> getGradesByClassID(String classID) {
        List<Grade> gradeList = new ArrayList<>();
        String sql = "SELECT g.GradeID, g.StudentClassID, g.AttendanceScore, g.MidtermScore, g.FinalExamScore, " +
                     "s.StudentCode, CONCAT(s.FirstName, ' ', s.LastName) AS StudentName " +
                     "FROM Grades g " +
                     "JOIN StudentClasses sc ON g.StudentClassID = sc.StudentClassID " +
                     "JOIN Students s ON sc.StudentID = s.StudentID " +
                     "WHERE sc.ClassID = ?";

        try (Connection conn = ConnectDatabase.checkConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, classID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Grade grade = new Grade();
                grade.setGradeID(rs.getString("GradeID"));
                grade.setStudentClassID(rs.getString("StudentClassID"));
                grade.setAttendanceScore(rs.getDouble("AttendanceScore"));
                grade.setMidtermScore(rs.getDouble("MidtermScore"));
                grade.setFinalExamScore(rs.getDouble("FinalExamScore"));
                grade.setStudentCode(rs.getString("StudentCode"));
                grade.setStudentName(rs.getString("StudentName"));
                gradeList.add(grade);
            }
        } catch (SQLException e) {
        	logger.severe("Lỗi Lấy danh sách sinh viên và điểm theo ClassID: " + e.getMessage());
        }
        return gradeList;
    }


    // Cập nhật điểm
    public static boolean updateGrade(Grade grade) {
        String sql = "UPDATE Grades SET AttendanceScore = ?, MidtermScore = ?, FinalExamScore = ?" +
                     "WHERE GradeID = ?";
        try (Connection conn = ConnectDatabase.checkConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, grade.getAttendanceScore());
            ps.setDouble(2, grade.getMidtermScore());
            ps.setDouble(3, grade.getFinalExamScore());
            ps.setString(4, grade.getGradeID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
        	logger.severe("Lỗi cập nhật điểm: " + e.getMessage());
        }
        return false;
    }
}
