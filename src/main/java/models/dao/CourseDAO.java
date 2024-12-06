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

	private static final String SQL_SELECT_ALL_COURSES =
	    "SELECT c.CourseID, c.CourseName, c.Credits, c.CourseCode, c.CourseType, c.Status, " +
	    "d.DepartmentID, d.DepartmentName " +
	    "FROM Courses c INNER JOIN Departments d ON c.DepartmentID = d.DepartmentID";

	private static final String SQL_SELECT_COURSE =
	    "SELECT c.CourseID, c.CourseName, c.Credits, c.CourseCode, c.CourseType, c.Status, " +
	    "d.DepartmentID, d.DepartmentName " +
	    "FROM Courses c INNER JOIN Departments d ON c.DepartmentID = d.DepartmentID " +
	    "WHERE c.CourseID = ?";

	private static final String SQL_SEARCH_COURSES =
	    "SELECT c.CourseID, c.CourseName, c.Credits, c.CourseCode, c.CourseType, c.Status, " +
	    "d.DepartmentID, d.DepartmentName " +
	    "FROM Courses c INNER JOIN Departments d ON c.DepartmentID = d.DepartmentID " +
	    "WHERE c.CourseName LIKE ?";

	private static final String SQL_DELETE_COURSE =
	    "DELETE FROM Courses WHERE CourseID = ?";

	private static final String SQL_UPDATE_COURSE =
	    "UPDATE Courses SET CourseName = ?, Credits = ?, DepartmentID = ?, CourseCode = ?, CourseType = ?, Status = ? WHERE CourseID = ?";

	private static final String SQL_INSERT_COURSE =
	    "INSERT INTO Courses (CourseName, Credits, DepartmentID, CourseCode, CourseType, Status) VALUES (?, ?, ?, ?, ?, ?)";

	private static final String SQL_CHECK_COURSECODE_UPDATE =
		"SELECT COUNT(*) FROM Courses WHERE courseCode = ? AND courseID != ?";

	private static final String SQL_CHECK_COURSECODE =
			"SELECT COUNT(*) FROM Courses WHERE courseCode = ?";

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

	public static List<Course> getAllCourse(){
		List<Course> courses = new ArrayList<>();
		try(Connection conn = ConnectDatabase.checkConnect();
			PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL_COURSES);
			ResultSet rs = stmt.executeQuery()){

			while(rs.next()) {
				courses.add(mapCourse(rs));
			}
		} catch (SQLException e) {
			logger.severe("error getting all courses:" + e.getMessage());
		}
		return courses;
	}

	public static boolean createCourse(Course course) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_COURSE)) {

            stmt.setString(1, course.getCourseName());
            stmt.setInt(2, course.getCredits());
            stmt.setInt(3, course.getDepartmentID());
            stmt.setString(4, course.getCourseCode());
            stmt.setString(5, course.getCourseType());
            stmt.setString(6, course.getStatus());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error while creating course: " + e.getMessage());
        }
        return false;
    }

	public static boolean deleteCourse(int courseID) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE_COURSE)) {
            stmt.setInt(1, courseID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error while deleting course: " + e.getMessage());
        }
        return false;
    }

	public static boolean updateCourse(Course course) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_COURSE)) {

            stmt.setString(1, course.getCourseName());
            stmt.setInt(2, course.getCredits());
            stmt.setInt(3, course.getDepartmentID());
            stmt.setString(4, course.getCourseCode());
            stmt.setString(5, course.getCourseType());
            stmt.setString(6, course.getStatus());
            stmt.setInt(7, course.getCourseID());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error while updating course: " + e.getMessage());
        }
        return false;
    }

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
            logger.severe("Error while getting course by ID: " + e.getMessage());
        }
        return null;
    }

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
            logger.severe("Error while searching courses by name: " + e.getMessage());
        }
        return courses;
    }

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
            logger.severe("Error while checking course code(Update): " + e.getMessage());
        }
        return false;
    }

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
            logger.severe("Error while checking course code(Create): " + e.getMessage());
        }
        return false;
    }
}
