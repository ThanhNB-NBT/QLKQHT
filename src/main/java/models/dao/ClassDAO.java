package models.dao;

import models.bean.Class;
import models.bean.Course;
import models.bean.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import common.ConnectDatabase;

public class ClassDAO {
	private static final Logger logger = Logger.getLogger(ClassDAO.class.getName());

	// Thêm lớp học mới
	private static final String SQL_CREATE_CLASS = "INSERT INTO Classes (CourseID, TeacherID, ClassTime, Room, Semester, ClassName, Status, "
			+ "MaxStudents, TotalLessions, StartDate, EndDate, ClassType, ParentClassID, RegisteredStudents) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
	public static boolean createClass(Class cls) {
		try (Connection conn = ConnectDatabase.checkConnect();
				PreparedStatement pstmt = conn.prepareStatement(SQL_CREATE_CLASS)) {
			setClassPreparedStatement(pstmt, cls, false);
			int result = pstmt.executeUpdate();
			if (result > 0) {
				logger.info("CourseID: " + cls.getCourseID());
				logger.info("ClassName: " + cls.getClassName());
				logger.info("Semester: " + cls.getSemester());

				logger.info("Lớp học đã được thêm thành công.");
				return true;
			} else {
				logger.warning("Không thể thêm lớp học.");
			}
		} catch (SQLException e) {
			logger.severe("Lỗi tạo lớp: " + e.getMessage());
		}
		return false;
	}

	// Cập nhật lớp học
	private static final String SQL_UPDATE_CLASS = "UPDATE Classes SET TeacherID = ?, ClassTime = ?, Room = ?,  "
			+ "Status = ?, MaxStudents = ?, TotalLessions = ? WHERE ClassID = ?";
	public static boolean updateClass(Class cls) {
		try (Connection conn = ConnectDatabase.checkConnect();
				PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE_CLASS)) {

			pstmt.setInt(1, cls.getTeacherID());
			pstmt.setString(2, cls.getClassTime());
			pstmt.setString(3, cls.getRoom());
			pstmt.setString(4, cls.getStatus());
			pstmt.setInt(5, cls.getMaxStudents());
			pstmt.setInt(6, cls.getTotalLessions());
			pstmt.setInt(7, cls.getClassID());

			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			logger.severe("Lỗi sửa lớp: " + e.getMessage());
		}
		return false;
	}

	// Kiểm tra trùng lớp học
	private static final String SQL_CHECK_DUPLICATE = "SELECT COUNT(*) FROM Classes " + "WHERE ClassName = ? AND Semester = ?";
	public static boolean isDuplicateClassCode(String className, String semester) {

		try (Connection conn = ConnectDatabase.checkConnect(); PreparedStatement pstmt = conn.prepareStatement(SQL_CHECK_DUPLICATE)) {
			pstmt.setString(1, className);
			pstmt.setString(2, semester);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					int count = rs.getInt(1);
					return count > 0;
				}
			}
		} catch (SQLException e) {
			logger.severe("Lỗi khi kiểm tra trùng ClassName: " + e.getMessage());
		}
		return false;
	}

	// Xóa lớp học
	private static final String SQL_DELETE_CLASS = "DELETE FROM Classes WHERE ClassID = ?";
	public static boolean deleteClass(int classID) {

		try (Connection conn = ConnectDatabase.checkConnect();
				PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE_CLASS)) {
			pstmt.setInt(1, classID);
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			logger.severe("Lỗi khi xóa lớp: " + e.getMessage());
		}
		return false;
	}

	// Lấy lớp học theo ID
	private static final String SQL_GETCLASS_BYCLASSID = "SELECT c.*, cr.CourseName, cr.CourseID, t.TeacherID, t.FirstName, t.LastName "
			+ "FROM Classes c " + "LEFT JOIN Courses cr ON c.CourseID = cr.CourseID "
			+ "LEFT JOIN Teachers t ON c.TeacherID = t.TeacherID WHERE ClassID = ?";
	public static Class getClassById(int classID) {

		try (Connection conn = ConnectDatabase.checkConnect();
				PreparedStatement pstmt = conn.prepareStatement(SQL_GETCLASS_BYCLASSID)) {
			pstmt.setInt(1, classID);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return mapClass(rs);
				}
			}
		} catch (SQLException e) {
			logger.severe("Lỗi khi lấy lớp theo ID: " + e.getMessage());
		}
		return null;
	}

	// Lấy tất cả lớp học (kèm thông tin khóa học và giáo viên)
	private static final String SQL_GET_ALL_CLASS = "SELECT c.*, cr.CourseName, cr.CourseID, t.TeacherID, t.FirstName, t.LastName "
			+ "FROM Classes c " + "LEFT JOIN Courses cr ON c.CourseID = cr.CourseID "
			+ "LEFT JOIN Teachers t ON c.TeacherID = t.TeacherID";
	public static List<Class> getAllClasses() {
		List<Class> classes = new ArrayList<>();
		try (Connection conn = ConnectDatabase.checkConnect();
				PreparedStatement pstmt = conn.prepareStatement(SQL_GET_ALL_CLASS);
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				classes.add(mapClass(rs));
			}
		} catch (SQLException e) {
			logger.severe("Lỗi khi lấy danh sách lớp: " + e.getMessage());
		}
		return classes;
	}

	private static final String SQL_SEARCH = "SELECT c.ClassID, c.CourseID, co.CourseName, c.TeacherID, t.FirstName, t.LastName"
			+ "c.ClassTime, c.Room, c.Semester, c.ClassName, c.Status,"
			+ "c.MaxStudents, c.TotalLessions, c.StartDate, c.EndDate, c.ClassType, c.ParentClassID"
			+ "FROM Classes c" + "JOIN Courses co ON c.CourseID = co.CourseID"
			+ "JOIN Teachers t ON c.TeacherID = t.TeacherID"
			+ "WHERE c.ClassName LIKE ? OR CONCAT(t.FirstName, ' ', t.LastName) LIKE ?";
	public static List<Class> searchByClassName(String className, String teacherName) {
		List<Class> classes = new ArrayList<>();
		try (Connection conn = ConnectDatabase.checkConnect();
				PreparedStatement pstmt = conn.prepareStatement(SQL_SEARCH)) {
			pstmt.setString(1, "%" + className + "%");
			pstmt.setString(2, "%" + teacherName + "%");
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					classes.add(mapClass(rs));
				}
			}
		} catch (SQLException e) {
			logger.severe("Lỗi khi tìm kiếm: " + e.getMessage());
		}
		return classes;
	}

	//Đếm số lượng lớp theo CourseID
	private static final String SQL_COUNT_CLASS = "SELECT COUNT(*) FROM Classes WHERE CourseID = ? AND ClassType = ?";
	public static int countClassesByCourseID(int courseID, String classType) {

		try (Connection conn = ConnectDatabase.checkConnect();
				PreparedStatement ps = conn.prepareStatement(SQL_COUNT_CLASS)) {
			ps.setInt(1, courseID);
			ps.setString(2, classType);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			logger.severe("Lỗi khi lấy số lượng lớp học phần: " + e.getMessage());
		}
		return 0;
	}

	//Lấy CourseID theo ClassID
	private static final String SQL_GET_COURSE_ID = "SELECT CourseID FROM Classes WHERE ClassID = ?";
    public static Integer getCourseIDByClassID(Integer classID) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_COURSE_ID)) {

            ps.setInt(1, classID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("CourseID");
                }
            }
        } catch (SQLException e) {
            logger.severe("Lỗi lấy CourseID: " + e.getMessage());
        }
        return null;
    }

	private static Class mapClass(ResultSet rs) throws SQLException {

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
		cls.setParentClassID(rs.getInt("ParentClassID"));
		cls.setRegisteredStudents(rs.getInt("RegisteredStudents"));

		Teacher teacher = new Teacher();
		teacher.setTeacherID(rs.getInt("TeacherID"));
		teacher.setFirstName(rs.getString("FirstName"));
		teacher.setLastName(rs.getString("LastName"));
		cls.setTeacher(teacher);
		cls.setTeacherID(rs.getInt("TeacherID"));

		Course course = new Course();
		course.setCourseID(rs.getInt("CourseID"));
		course.setCourseName(rs.getString("CourseName"));
		cls.setCourse(course);
		cls.setCourseID(rs.getInt("CourseID"));

		return cls;
	}

	// Phương thức trợ giúp: Set các tham số cho PreparedStatement
	private static void setClassPreparedStatement(PreparedStatement pstmt, Class cls, boolean includeClassID)
			throws SQLException {
		pstmt.setInt(1, cls.getCourseID());
		pstmt.setInt(2, cls.getTeacherID());
		pstmt.setString(3, cls.getClassTime());
		pstmt.setString(4, cls.getRoom());
		pstmt.setString(5, cls.getSemester());
		pstmt.setString(6, cls.getClassName());
		pstmt.setString(7, cls.getStatus());
		pstmt.setInt(8, cls.getMaxStudents());
		pstmt.setInt(9, cls.getTotalLessions());
		pstmt.setDate(10, cls.getStartDate());
		pstmt.setDate(11, cls.getEndDate());
		pstmt.setString(12, cls.getClassType());
		if (cls.getParentClassID() == null) {
			pstmt.setNull(13, Types.INTEGER);
		} else {
			pstmt.setInt(13, cls.getParentClassID());
		}
		if (includeClassID) {
			pstmt.setInt(14, cls.getClassID());
		}
	}

}
