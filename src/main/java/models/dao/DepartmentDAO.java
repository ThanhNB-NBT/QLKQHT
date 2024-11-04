package models.dao;

import models.bean.Department;
import common.ConnectDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class DepartmentDAO {
	private static final Logger logger = Logger.getLogger(DepartmentDAO.class.getName());//Ghi lại thông tin hoạt động
	private static final String SQL_SELECT_ALL_DEPARTMENT = "SELECT * FROM Departments";
	private static final String SQL_SELECT_DEPARTMENT = "SELECT * FROM Departments WHERE DepartmentID = ?";
	private static final String SQL_SEARCH_DEPARTMENT = "SELECT * FROM Departments WHERE DepartmentName LIKE ?";
	private static final String SQL_DELETE_DEPARTMENT = "DELETE FROM Deparments WHERE DepartmentID = ?";
	private static final String SQL_UPDATE_DEPARTMENT = "UPDATE Departments SET DepartmentName = ?, Email = ?, Phone = ? WHERE DepartmentID = ?";
	private static final String SQL_INSERT_DEPARTMENT = "INSERT INTO Departments(DepartmentName, Email, Phone) VALUES (?, ?, ?)";
	
	private static Department mapDepartment(ResultSet rs) throws SQLException{
		return new Department(rs.getInt("DepartmentID"),
				rs.getString("DepartmentName"),
				rs.getString("Email"),
				rs.getString("Phone")
				);
	}
	
	public static ArrayList<Department> getAllDepartment(){
		ArrayList<Department> departments = new ArrayList<>();
		try(Connection conn = ConnectDatabase.checkConnect();
			PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL_DEPARTMENT);
			ResultSet rs = stmt.executeQuery()){
			
			while(rs.next()) {
				departments.add(mapDepartment(rs));
			}
		} catch (SQLException e) {
			logger.severe("error:" + e.getMessage());
		}
		return departments;
	}
	
	public static boolean createDepartment(Department department) {
		try(Connection conn = ConnectDatabase.checkConnect();
			PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_DEPARTMENT)){
				stmt.setString(1, department.getDepartmentName());
				stmt.setString(2, department.getEmail());
				stmt.setString(3, department.getPhone());
				
				return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			logger.severe("error:" + e.getMessage());//ghi lại thông báo lỗi nghiêm trọng
		}
		return false;
	}
	
	public static boolean deleteDepartment(int departmentId) {
		try(Connection conn = ConnectDatabase.checkConnect();
			PreparedStatement stmt = conn.prepareStatement(SQL_DELETE_DEPARTMENT)){
			stmt.setInt(1, departmentId);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			logger.severe("error:" + e.getMessage());
		}
		return false;
	}
	
	public static boolean updateDepartment(Department department) {
		try(Connection conn = ConnectDatabase.checkConnect();
			PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_DEPARTMENT)){
				stmt.setString(1, department.getDepartmentName());
				stmt.setString(2, department.getEmail());
				stmt.setString(3, department.getPhone());
				stmt.setInt(4, department.getDepartmentID());
				
				return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			logger.severe("error: " + e.getMessage());
		}
		return false;
	}
}
