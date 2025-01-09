package models.dao;

import models.bean.Department;
import common.ConnectDatabase;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

public class DepartmentDAO {
	private static final Logger logger = Logger.getLogger(DepartmentDAO.class.getName());

	private	static final String SQL_INSERT_DEPARTMENT_NAME = "INSERT INTO Departments (DepartmentName) VALUES (?)";

	private static Department mapDepartment(ResultSet rs) throws SQLException{
		Department department = new Department();
		department.setDepartmentID(rs.getInt("DepartmentID"));
		department.setDepartmentName(rs.getString("DepartmentName"));
		department.setEmail(rs.getString("Email"));
		department.setPhone(rs.getString("Phone"));
		return department;
	}

	//Lấy danh sách khoa/viện
	private static final String SQL_SELECT_ALL_DEPARTMENT = "SELECT DepartmentID, DepartmentName, Email, Phone FROM Departments";
	public static List<Department> getAllDepartment(){
		List<Department> departments = new ArrayList<>();
		try(Connection conn = ConnectDatabase.checkConnect();
			PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL_DEPARTMENT);
			ResultSet rs = stmt.executeQuery()){

			while(rs.next()) {
				departments.add(mapDepartment(rs));
			}
		} catch (SQLException e) {
			logger.severe("Lỗi lấy danh sách khoa:" + e.getMessage());
		}
		return departments;
	}

	//Thêm mới
	private static final String SQL_INSERT_DEPARTMENT = "INSERT INTO Departments(DepartmentName, Email, Phone) VALUES (?, ?, ?)";
	public static boolean createDepartment(Department department) {
		try(Connection conn = ConnectDatabase.checkConnect();
			PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_DEPARTMENT)){
				stmt.setString(1, department.getDepartmentName());
				stmt.setString(2, department.getEmail());
				stmt.setString(3, department.getPhone());

				return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			logger.severe("lỗi tạo khoa:" + e.getMessage());
		}
		return false;
	}

	//Xoá
	private static final String SQL_DELETE_DEPARTMENT = "DELETE FROM Departments WHERE DepartmentID = ?";
	public static boolean deleteDepartment(int departmentId) {
		try(Connection conn = ConnectDatabase.checkConnect();
			PreparedStatement stmt = conn.prepareStatement(SQL_DELETE_DEPARTMENT)){
			stmt.setInt(1, departmentId);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			logger.severe("Lỗi xóa khoa:" + e.getMessage());
		}
		return false;
	}

	//Cập nhật
	private static final String SQL_UPDATE_DEPARTMENT = "UPDATE Departments SET DepartmentName = ?, Email = ?, Phone = ? WHERE DepartmentID = ?";
	public static boolean updateDepartment(Department department) {
		try(Connection conn = ConnectDatabase.checkConnect();
			PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_DEPARTMENT)){
				stmt.setString(1, department.getDepartmentName());
				stmt.setString(2, department.getEmail());
				stmt.setString(3, department.getPhone());
				stmt.setInt(4, department.getDepartmentID());

				return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			logger.severe("lỗi chỉnh sửa khoa: " + e.getMessage());
		}
		return false;
	}

	//Lấy thông tin theo ID
	private static final String SQL_SELECT_DEPARTMENT_BYID = "SELECT DepartmentID, DepartmentName, Email, Phone FROM Departments WHERE DepartmentID = ?";
	public static Department getDepartmentById(int departmentId) {
		try(Connection conn = ConnectDatabase.checkConnect();
			PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_DEPARTMENT_BYID)){
				stmt.setInt(1, departmentId);
				try(ResultSet rs = stmt.executeQuery()){
					if(rs.next()) {
						return mapDepartment(rs);
					}
				}
		} catch(SQLException e) {
			logger.severe("Lỗi lấy khoa theo ID: " + e.getMessage());
		}
		return null;
	}


	//Tìm kiếm
	private static final String SQL_SEARCH_DEPARTMENT = "SELECT DepartmentID, DepartmentName, Email, Phone FROM Departments WHERE DepartmentName LIKE ?";
	public static List<Department> searchByDepartmentName(String departmentName){
		List<Department> departments = new ArrayList<>();

		try(Connection conn = ConnectDatabase.checkConnect();
			PreparedStatement stmt = conn.prepareStatement(SQL_SEARCH_DEPARTMENT)) {
				stmt.setString(1, "%" + departmentName + "%");
				try(ResultSet rs = stmt.executeQuery()){
					while(rs.next()) {
						departments.add(mapDepartment(rs));
					}
				}
		} catch(SQLException e) {
			logger.severe("lỗi tìm kiếm: " + e.getMessage());
		}
		return departments;
	}

	//Kiểm tra trùng tên khoa
	private static final String SQL_CHECHK_DEPARTMENTNAME = "SELECT COUNT(*) FROM Departments WHERE DepartmentName = ?";
	public static boolean checkDepartmentName(String departmentName) {
		try(Connection conn = ConnectDatabase.checkConnect();
			PreparedStatement stmt = conn.prepareStatement(SQL_CHECHK_DEPARTMENTNAME)){
				stmt.setString(1, departmentName);
				try(ResultSet rs = stmt.executeQuery()){
					if(rs.next()) {
						return rs.getInt(1) > 0;
					}
				}
		} catch(SQLException e) {
			logger.severe("Lỗi kiểm tra tên khoa: " + e.getMessage());
		}
		return false;
	}

	//Lấy ID theo tên khoa (cho chức năng nhập file excel ở quản lý sinh viên
	private static final String SQL_DEPARTMENTID_BYNAME = "SELECT DepartmentID FROM Departments WHERE DepartmentName = ?";
	public static Integer getDepartmentIDByName(String departmentName) {
	    Integer departmentID = null;

	    try (Connection conn = ConnectDatabase.checkConnect();
	         PreparedStatement ps = conn.prepareStatement(SQL_DEPARTMENTID_BYNAME)) {

	        ps.setString(1, departmentName);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            departmentID = rs.getInt("DepartmentID");
	        }
	    } catch (Exception e) {
	        logger.severe("Lỗi lấy tên khoa theo ID: " + e.getMessage());;
	    }
	    return departmentID;
	}


	public static Integer createDepartment(String departmentName) {
	    Connection conn = ConnectDatabase.checkConnect();

	    try (PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_DEPARTMENT_NAME, Statement.RETURN_GENERATED_KEYS)) {
	        stmt.setString(1, departmentName);
	        stmt.executeUpdate();

	        ResultSet rs = stmt.getGeneratedKeys();
	        if (rs.next()) {
	            return rs.getInt(1); // Trả về ID của khoa vừa thêm
	        }
	    } catch (SQLException e) {
	        logger.severe("Lỗi tạo khoa theo tên Khoa: " + e.getMessage());;
	    }
	    return null;
	}

}
