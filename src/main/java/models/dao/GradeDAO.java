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
        String sql = "SELECT g.GradeID, g.StudentClassID, g.AttendanceScore, " +
                    "g.MidtermScore, g.FinalExamScore, g.GradeStatus, g.GradeComment, " +
                    "s.StudentCode, CONCAT(s.FirstName, ' ', s.LastName) AS StudentName " +
                    "FROM Grades g " +
                    "JOIN StudentClasses sc ON g.StudentClassID = sc.StudentClassID " +
                    "JOIN Students s ON sc.StudentID = s.StudentID " +
                    "WHERE sc.ClassID = ?";

        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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
                grade.setGradeStatus(rs.getString("GradeStatus"));
                grade.setGradeComment(rs.getString("GradeComment"));
                gradeList.add(grade);
            }
        } catch (SQLException e) {
            logger.severe("Lỗi lấy danh sách điểm: " + e.getMessage());
        }
        return gradeList;
    }

    // Cập nhật điểm
    public static boolean updateGrade(Grade grade) {
        // Kiểm tra trạng thái duyệt trước khi cho phép cập nhật
        String checkStatus = "SELECT GradeStatus FROM Grades WHERE GradeID = ?";
        String updateSql = "UPDATE Grades SET AttendanceScore = ?, MidtermScore = ?, " +
                          "FinalExamScore = ?, GradeStatus = ? WHERE GradeID = ?";

        try (Connection conn = ConnectDatabase.checkConnect()) {
            // Kiểm tra trạng thái
            try (PreparedStatement checkPs = conn.prepareStatement(checkStatus)) {
                checkPs.setString(1, grade.getGradeID());
                ResultSet rs = checkPs.executeQuery();
                if (rs.next() && rs.getString("GradeStatus").equals(Grade.GradeStatus.APPROVED.getCode())) {
                    logger.warning("Không thể cập nhật điểm đã được duyệt!");
                    return false;
                }
            }

            // Thực hiện cập nhật
            try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                updatePs.setDouble(1, grade.getAttendanceScore());
                updatePs.setDouble(2, grade.getMidtermScore());
                updatePs.setDouble(3, grade.getFinalExamScore());
                updatePs.setString(4, Grade.GradeStatus.PENDING.getCode());
                updatePs.setString(5, grade.getGradeID());
                return updatePs.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            logger.severe("Lỗi cập nhật điểm: " + e.getMessage());
            return false;
        }
    }

    //Dành cho importExcel
    public static boolean updateGradeByStudentClassID(Grade grade) {
        String sql = "UPDATE Grades SET AttendanceScore = ?, MidtermScore = ?, FinalExamScore = ? " +
                     "WHERE StudentClassID = ?";

        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (grade.getAttendanceScore() != null) {
                ps.setDouble(1, grade.getAttendanceScore());
            } else {
                ps.setNull(1, Types.DOUBLE);
            }

            if (grade.getMidtermScore() != null) {
                ps.setDouble(2, grade.getMidtermScore());
            } else {
                ps.setNull(2, Types.DOUBLE);
            }

            if (grade.getFinalExamScore() != null) {
                ps.setDouble(3, grade.getFinalExamScore());
            } else {
                ps.setNull(3, Types.DOUBLE);
            }

            ps.setString(4, grade.getStudentClassID());

            logger.info("Cập nhật điểm cho StudentClassID: " + grade.getStudentClassID());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.severe("Lỗi cập nhật điểm theo StudentClassID: " + e.getMessage());
        }
        return false;
    }

    // Lấy danh sách điểm theo StudentID
    public static List<Grade> getGradesByStudentID(String studentID) {
        List<Grade> gradeList = new ArrayList<>();
        String sql = "SELECT g.GradeID, g.StudentClassID, g.AttendanceScore, g.MidtermScore, g.FinalExamScore, " +
                     "c.Semester, co.CourseName, co.CourseCode, co.Credits " +
                     "FROM Grades g " +
                     "JOIN StudentClasses sc ON g.StudentClassID = sc.StudentClassID " +
                     "JOIN Classes c ON sc.ClassID = c.ClassID " +
                     "JOIN Courses co ON c.CourseID = co.CourseID " +
                     "WHERE sc.StudentID = ? AND g.GradeStatus = 1";

        try (Connection conn = ConnectDatabase.checkConnect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Grade grade = new Grade();
                grade.setGradeID(rs.getString("GradeID"));
                grade.setStudentClassID(rs.getString("StudentClassID"));
                grade.setAttendanceScore(rs.getDouble("AttendanceScore"));
                grade.setMidtermScore(rs.getDouble("MidtermScore"));
                grade.setFinalExamScore(rs.getDouble("FinalExamScore"));
                grade.setSemester(rs.getString("Semester"));
                grade.setCourseName(rs.getString("CourseName"));
                grade.setCourseCode(rs.getString("CourseCode"));
                grade.setCredits(rs.getString("Credits"));
                gradeList.add(grade);
            }
        } catch (SQLException e) {
            logger.severe("Lỗi lấy danh sách điểm theo StudentID: " + e.getMessage());
        }
        return gradeList;
    }

    //Kiểm tra trạng thái duyệt điểm
    public static String getGradeStatus(String gradeID) {
        String sql = "SELECT GradeStatus FROM Grades WHERE GradeID = ?";
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, gradeID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("GradeStatus");
            }
        } catch (SQLException e) {
            logger.severe("Lỗi kiểm tra trạng thái điểm: " + e.getMessage());
        }
        return null;
    }


 // Thêm phương thức duyệt điểm (dành cho admin)
    public static boolean reviewGrade(String gradeID, String status, String comment) {
        String sql = "UPDATE Grades SET GradeStatus = ?, GradeComment = ? WHERE GradeID = ?";
        logger.info("Chuẩn bị thực thi truy vấn cập nhật điểm.");
        logger.info("SQL: " + sql);
        logger.info("Thông tin đầu vào - GradeID: " + gradeID + ", Status: " + status + ", Comment: " + comment);

        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, comment);
            ps.setString(3, gradeID);

            int rowsAffected = ps.executeUpdate();
            logger.info("Số dòng bị ảnh hưởng: " + rowsAffected);

            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.severe("Lỗi duyệt điểm: " + e.getMessage());
            return false;
        }
    }

}
