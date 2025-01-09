package models.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import common.ConnectDatabase;
import models.bean.Course;
import models.bean.Department;

public class CourseDAO {
	private static final Logger logger = Logger.getLogger(CourseDAO.class.getName());

















	private static Course mapCourse(ResultSet rs) throws SQLException{
		Course course = new Course();
        course.setCourseID(rs.getInt("CourseID"));
        course.setCourseName(rs.getString("CourseName"));
        course.setCredits(rs.getInt("Credits"));
        course.setDepartmentID(rs.getInt("DepartmentID"));
        course.setCourseCode(rs.getString("CourseCode"));
        course.setCourseType(rs.getString("CourseType"));
        course.setStatus(rs.getString("Status"));

        Department department = new Department();
        department.setDepartmentID(rs.getInt("DepartmentID"));
        department.setDepartmentName(rs.getString("DepartmentName"));

        course.setDepartment(department);
        return course;
	}

	//Lấy danh sách học phần
	private static final String SQL_SELECT_ALL_COURSES =
		    "SELECT c.CourseID, c.CourseName, c.Credits, c.CourseCode, c.CourseType, c.Status, " +
		    "d.DepartmentID, d.DepartmentName " +
		    "FROM Courses c INNER JOIN Departments d ON c.DepartmentID = d.DepartmentID";
	public static List<Course> getAllCourse(){
		List<Course> courses = new ArrayList<>();
		try(Connection conn = ConnectDatabase.checkConnect();
			PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL_COURSES);
			ResultSet rs = stmt.executeQuery()){

			while(rs.next()) {
				courses.add(mapCourse(rs));
			}
		} catch (SQLException e) {
			logger.severe("Lỗi khi lấy tất cả học phần:" + e.getMessage());
		}
		return courses;
	}

	//Thêm học phần
	private static final String SQL_INSERT_COURSE =
		    "INSERT INTO Courses (CourseName, Credits, DepartmentID, CourseCode, CourseType, Status) VALUES (?, ?, ?, ?, ?, ?)";
	public static boolean createCourse(Course course) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_COURSE)) {

        	setCoursePreparedStatement(stmt, course, false);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Lỗi khi thêm mới học phần: " + e.getMessage());
        }
        return false;
    }

	//Xoá học phần
	private static final String SQL_DELETE_COURSE =
		    "DELETE FROM Courses WHERE CourseID = ?";
	public static boolean deleteCourse(int courseID) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE_COURSE)) {
            stmt.setInt(1, courseID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Lỗi khi xoá học phần: " + e.getMessage());
        }
        return false;
    }

	//Cập nhật học phần
	private static final String SQL_UPDATE_COURSE =
		    "UPDATE Courses SET Credits = ?, CourseType = ?, Status = ? WHERE CourseID = ?";
	public static boolean updateCourse(Course course) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_COURSE)) {

        	stmt.setInt(1, course.getCredits());
            stmt.setString(2, course.getCourseType());
            stmt.setString(3, course.getStatus());
            stmt.setInt(4, course.getCourseID());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Lỗi khi cập nhật học phần: " + e.getMessage());
        }
        return false;
    }

	private static void setCoursePreparedStatement(PreparedStatement stmt, Course course, boolean includeCourseID) throws SQLException {
		stmt.setString(1, course.getCourseName());
        stmt.setInt(2, course.getCredits());
        stmt.setInt(3, course.getDepartmentID());
        stmt.setString(4, course.getCourseCode());
        stmt.setString(5, course.getCourseType());
        stmt.setString(6, course.getStatus());
        if(includeCourseID) {
        	stmt.setInt(7, course.getCourseID());
        }

    }

	//Lấy thông tin học phần theo CourseID
	private static final String SQL_SELECT_COURSE =
		    "SELECT c.CourseID, c.CourseName, c.Credits, c.CourseCode, c.CourseType, c.Status, " +
		    "d.DepartmentID, d.DepartmentName " +
		    "FROM Courses c INNER JOIN Departments d ON c.DepartmentID = d.DepartmentID " +
		    "WHERE c.CourseID = ?";
	public static Course getCourseById(int courseID) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_COURSE)) {

            stmt.setInt(1, courseID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapCourse(rs);
                }
            }
        } catch (SQLException e) {
            logger.severe("Lỗi khi lấy học phần theo ID: " + e.getMessage());
        }
        return null;
    }

	//Tìm kiếm học phần
	private static final String SQL_SEARCH_COURSES =
		    "SELECT c.CourseID, c.CourseName, c.Credits, c.CourseCode, c.CourseType, c.Status, " +
		    "d.DepartmentID, d.DepartmentName " +
		    "FROM Courses c INNER JOIN Departments d ON c.DepartmentID = d.DepartmentID " +
		    "WHERE c.CourseName LIKE ?";
	public static List<Course> searchCoursesByName(String courseName) {
        List<Course> courses = new ArrayList<>();
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_SEARCH_COURSES)) {

            stmt.setString(1, "%" + courseName + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    courses.add(mapCourse(rs));
                }
            }
        } catch (SQLException e) {
            logger.severe("Lỗi khi tìm kiếm học phần: " + e.getMessage());
        }
        return courses;
    }

	//Kiểm tra tồn tại của mã học phần (CourseCode)
	private static final String SQL_CHECK_COURSECODE_UPDATE =
			"SELECT COUNT(*) FROM Courses WHERE courseCode = ? AND courseID != ?";
	public static boolean checkCourseCode(String courseCode, int excludeCourseID) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_CHECK_COURSECODE_UPDATE)) {

            stmt.setString(1, courseCode);
            stmt.setInt(2, excludeCourseID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.severe("Lỗi khi kiểm tra mã học phần (cập nhật): " + e.getMessage());
        }
        return false;
    }

	//Kiểm tra tồn tại mã học phần để thêm mới
	private static final String SQL_CHECK_COURSECODE =
			"SELECT COUNT(*) FROM Courses WHERE courseCode = ?";
	public static boolean checkCourseCode(String courseCode) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_CHECK_COURSECODE)) {

            stmt.setString(1, courseCode);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.severe("Lỗi khi kiểm tra mã học phần (thêm mới): " + e.getMessage());
        }
        return false;
    }
}
