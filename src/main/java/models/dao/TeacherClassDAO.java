package models.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import common.ConnectDatabase;
import models.bean.Class;

public class TeacherClassDAO {
    private static final Logger logger = Logger.getLogger(TeacherClassDAO.class.getName());

    // Lấy danh sách lớp mà giảng viên phụ trách
    public static List<Class> getClassesByTeacher(String teacherID) {
        List<Class> classList = new ArrayList<>();
        String sql = "SELECT ClassID, ClassTime, Room, Semester, ClassName, Status, RegisteredStudents, "
    				+ "MaxStudents, TotalLessions, StartDate, EndDate, ClassType, ParentClassID "
                    + "FROM Classes WHERE TeacherID = ?";

        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, teacherID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Class cls = new Class();
                cls.setClassID(rs.getInt("ClassID"));
        		cls.setClassName(rs.getString("ClassName"));
        		cls.setClassTime(rs.getString("ClassTime"));
        		cls.setRoom(rs.getString("Room"));
        		cls.setSemester(rs.getString("Semester"));
        		cls.setMaxStudents(rs.getInt("MaxStudents"));
        		cls.setStartDate(rs.getDate("StartDate"));
        		cls.setEndDate(rs.getDate("EndDate"));
        		cls.setStatus(rs.getString("Status"));
        		cls.setClassType(rs.getString("ClassType"));
        		cls.setTotalLessions(rs.getInt("TotalLessions"));
        		cls.setParentClassID(rs.getInt("ParentClassID"));
        		cls.setRegisteredStudents(rs.getInt("RegisteredStudents"));
                classList.add(cls);
            }
        } catch (SQLException e) {
            logger.severe("Lỗi khi lấy danh sách lớp của giảng viên: " + e.getMessage());
        }
        return classList;
    }

    // Cập nhật thông tin lớp học
    public static boolean updateClass(Class cls) {
        String sql = "UPDATE Classes SET ClassTime = ?, Room = ?, Status = ?, "
                   + "MaxStudents = ?, TotalLessions = ? WHERE ClassID = ?";
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cls.getClassTime());
            ps.setString(2, cls.getRoom());
            ps.setString(3, cls.getStatus());
            ps.setInt(4, cls.getMaxStudents());
            ps.setInt(5, cls.getTotalLessions());
            ps.setInt(6, cls.getClassID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Lỗi khi cập nhật thông tin lớp học: " + e.getMessage());
        }
        return false;
    }

    public static List<Class> searchByClassName(String className, String teacherID) {
    	String sql = "SELECT c.ClassID, c.ClassTime, c.Room, c.Semester, c.ClassName, c.Status,"
    			+ "c.MaxStudents, c.TotalLessons, c.StartDate, c.EndDate, c.ClassType, c.ParentClassID"
    			+ "FROM Classes c"
    			+ "WHERE c.ClassName LIKE ? AND c.TeacherID = ?";
		List<Class> classes = new ArrayList<>();
		try (Connection conn = ConnectDatabase.checkConnect();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, "%" + className + "%");
			pstmt.setString(2, teacherID);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Class cls = new Class();
	                cls.setClassID(rs.getInt("ClassID"));
	        		cls.setClassName(rs.getString("ClassName"));
	        		cls.setClassTime(rs.getString("ClassTime"));
	        		cls.setRoom(rs.getString("Room"));
	        		cls.setSemester(rs.getString("Semester"));
	        		cls.setMaxStudents(rs.getInt("MaxStudents"));
	        		cls.setStartDate(rs.getDate("StartDate"));
	        		cls.setEndDate(rs.getDate("EndDate"));
	        		cls.setStatus(rs.getString("Status"));
	        		cls.setClassType(rs.getString("ClassType"));
	        		cls.setTotalLessions(rs.getInt("TotalLessions"));
	        		cls.setParentClassID(rs.getInt("ParentClassID"));
	        		cls.setRegisteredStudents(rs.getInt("RegisteredStudents"));
	                classes.add(cls);
				}
			}
		} catch (SQLException e) {
			logger.severe("Lỗi khi tìm kiếm: " + e.getMessage());
		}
		return classes;
	}
}
