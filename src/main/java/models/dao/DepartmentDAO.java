package models.dao;

import models.bean.Department;
import common.ConnectDatabase;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

public class DepartmentDAO {
	private static final Logger logger = Logger.getLogger(DepartmentDAO.class.getName());//Ghi lại thông tin hoạt động
	private static final String SQL_SELECT_ALL_DEPARTMENT = "SELECT DepartmentID, DepartmentName, Email, Phone FROM Departments";
	private static final String SQL_SELECT_DEPARTMENT = "SELECT DepartmentID, DepartmentName, Email, Phone FROM Departments WHERE DepartmentID = ?";
	private static final String SQL_SEARCH_DEPARTMENT = "SELECT DepartmentID, DepartmentName, Email, Phone FROM Departments WHERE DepartmentName LIKE ?";
	private static final String SQL_DELETE_DEPARTMENT = "DELETE FROM Departments WHERE DepartmentID = ?";
	private static final String SQL_UPDATE_DEPARTMENT = "UPDATE Departments SET DepartmentName = ?, Email = ?, Phone = ? WHERE DepartmentID = ?";
	private static final String SQL_INSERT_DEPARTMENT = "INSERT INTO Departments(DepartmentName, Email, Phone) VALUES (?, ?, ?)";
	private static final String SQL_CHECHK_DEPARTMENTNAME = "SELECT COUNT(*) FROM Departments WHERE DepartmentName = ?";
	
	private static Department mapDepartment(ResultSet rs) throws SQLException{
		return new Department(rs.getInt("DepartmentID"),
				rs.getString("DepartmentName"),
				rs.getString("Email"),
				rs.getString("Phone")
				);
	}
	
	public static List<Department> getAllDepartment(){
		List<Department> departments = new ArrayList<>();
		try(Connection conn = ConnectDatabase.checkConnect();
			PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL_DEPARTMENT);
			ResultSet rs = stmt.executeQuery()){
			
			while(rs.next()) {
				departments.add(mapDepartment(rs));
			}
		} catch (SQLException e) {
			logger.severe("error get:" + e.getMessage());
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
			logger.severe("error createDepartment:" + e.getMessage());//ghi lại thông báo lỗi nghiêm trọng
		}
		return false;
	}
	
	public static boolean deleteDepartment(int departmentId) {
		try(Connection conn = ConnectDatabase.checkConnect();
			PreparedStatement stmt = conn.prepareStatement(SQL_DELETE_DEPARTMENT)){
			stmt.setInt(1, departmentId);
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			logger.severe("error deleteDepartment:" + e.getMessage());
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
			logger.severe("error updateDepartment: " + e.getMessage());
		}
		return false;
	}
	
	public static Department getDepartmentById(int departmentId) {
		try(Connection conn = ConnectDatabase.checkConnect();
			PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_DEPARTMENT)){
				stmt.setInt(1, departmentId);
				try(ResultSet rs = stmt.executeQuery()){
					if(rs.next()) {
						return mapDepartment(rs);
					}
				}
		} catch(SQLException e) {
			logger.severe("error getDepartmentId: " + e.getMessage());
		}
		return null;
	}
	
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
			logger.severe("error searchByDepartmentName: " + e.getMessage());
		}
		return departments;
	}
	
	public static boolean checkDepartmentName(String departmentName) {
		try(Connection conn = ConnectDatabase.checkConnect();
			PreparedStatement stmt = conn.prepareStatement(SQL_CHECHK_DEPARTMENTNAME)){
				stmt.setString(1, departmentName);
				try(ResultSet rs = stmt.executeQuery()){
					if(rs.next()) {
						return rs.getInt(1) == 0;
					}
				}
		} catch(SQLException e) {
			logger.severe("error checking departmentName: " + e.getMessage());
		}
		return false;
		
	}
	
}
