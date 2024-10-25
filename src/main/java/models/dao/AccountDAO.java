package models.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import common.ConnectDatabase;
import models.bean.Account;
import models.bean.Role;

public class AccountDAO {
	public static Account checkAccount(Account account) {
	    if (account.getUsername() == null || account.getPassword() == null) {
	        return null; // Trả về null nếu không có username hoặc password
	    }

	    String sqlAccount = "SELECT a.accountID, a.username, a.password, a.email, r.roleID, r.role " +
	                          "FROM Accounts a JOIN Roles r ON a.roleID = r.roleID " +
	                          "WHERE (a.username = ? OR a.email = ?) AND a.password = ?";

	    Account loggedInAccount = null; // Đối tượng Account để lưu thông tin tài khoản

	    try {
	        PreparedStatement stmt = ConnectDatabase.checkConnect().prepareStatement(sqlAccount);
	        String identifier = account.getUsername(); 
	        stmt.setString(1, identifier);
	        stmt.setString(2, identifier);
	        stmt.setString(3, account.getPassword());

	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            // Tạo một đối tượng Role từ dữ liệu truy vấn
	            Role role = new Role();
	            role.setRoleID(rs.getInt("roleID"));
	            role.setRole(rs.getString("role"));

	            // Tạo một đối tượng Account từ dữ liệu truy vấn
	            loggedInAccount = new Account(
	                rs.getInt("accountID"), 
	                rs.getString("username"), 
	                rs.getString("password"), 
	                rs.getString("email"), 
	                role // Gán đối tượng Role vào Account
	            );
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return loggedInAccount; // Trả về Account hoặc null
	}

	
	public static List<Account> getAllAccounts() {
	    String sqlAllAccount = "SELECT a.accountID, a.username, a.password, a.email, r.roleID, r.role " +
	                              "FROM Accounts a " +
	                              "JOIN Roles r ON a.roleID = r.roleID";
	    List<Account> accounts = new ArrayList<>();

	    try {
	        PreparedStatement stmt = ConnectDatabase.checkConnect().prepareStatement(sqlAllAccount);
	        ResultSet rs = stmt.executeQuery();
	        
	        while (rs.next()) {
	            // Tạo một đối tượng Role từ dữ liệu truy vấn
	            Role role = new Role();
	            role.setRoleID(rs.getInt("roleID")); // Lấy ID vai trò
	            role.setRole(rs.getString("role"));   // Lấy tên vai trò

	            // Tạo một đối tượng Account từ dữ liệu truy vấn
	            Account account = new Account(
	                rs.getInt("accountID"), 
	                rs.getString("username"), 
	                rs.getString("password"), 
	                rs.getString("email"), 
	                role // Gán đối tượng Role vào Account
	            );

	            // Thêm Account vào danh sách
	            accounts.add(account); 
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return accounts; 
	}
	
	public static boolean createAccount(Account account) {
        String sqlInsert = "INSERT INTO Accounts (Username, Password, Email, RoleID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = ConnectDatabase.checkConnect().prepareStatement(sqlInsert)) {

            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());
            stmt.setString(3, account.getEmail());
            stmt.setInt(4, account.getRole().getRoleID());

            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;
        } catch(SQLException e) {
        	e.printStackTrace();
        	return false;
        }
	}
	
	public void deleteAccount(int accountID) {
		String sqlDelete = "DELETE FROM Accounts WHERE AccountID = ?";
		try (PreparedStatement stmt = ConnectDatabase.checkConnect().prepareStatement(sqlDelete)){
			stmt.setInt(1, accountID);
			stmt.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public void updateAccount(int accountID) {
		String sqlUpdate = "UPDATE Accounts SET Username = ?, Password = ?, Email = ?, RoleID = ? WHERE AccountID = ?";
		
	}
}
