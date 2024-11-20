package models.dao;

import models.bean.Account;
import models.bean.Role;
import common.ConnectDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Logger;

public class AccountDAO {
	private static final Logger logger = Logger.getLogger(AccountDAO.class.getName());

    private static final String SQL_CHECK_ACCOUNT = "SELECT a.accountID, a.username, a.password, a.email, r.roleID, r.role FROM Accounts a JOIN Roles r ON a.roleID = r.roleID WHERE (a.username = ? OR a.email = ?) AND a.password = ?";
    private static final String SQL_SELECT_ALL_ACCOUNTS = "SELECT a.accountID, a.username, a.password, a.email, r.roleID, r.role FROM Accounts a JOIN Roles r ON a.roleID = r.roleID";
    private static final String SQL_INSERT_ACCOUNT = "INSERT INTO Accounts (Username, Password, Email, RoleID) VALUES (?, ?, ?, ?)";
    private static final String SQL_DELETE_ACCOUNT = "DELETE FROM Accounts WHERE AccountID = ?";
    private static final String SQL_UPDATE_ACCOUNT = "UPDATE Accounts SET Username = ?, Password = ?, Email = ?, RoleID = ? WHERE AccountID = ?";
    private static final String SQL_SELECT_ACCOUNT = "SELECT a.accountID, a.username, a.password, a.email, a.roleID, r.role FROM Accounts a JOIN Roles r ON r.roleID = a.roleID WHERE AccountID = ?";
    private static final String SQL_SEARCH_ACCOUNT = "SELECT a.accountID, a.username, a.password, a.email, a.roleID, r.role FROM Accounts a JOIN Roles r ON r.roleID = a.roleID WHERE a.Username LIKE ? ";
    private static final String SQL_CHECK_USERNAME = "SELECT COUNT(*) FROM accounts WHERE username = ?";
    private static final String SQL_CHECK_EMAIL = "SELECT COUNT(*) FROM accounts WHERE email = ?";
    private static Account mapAccount(ResultSet rs) throws SQLException {
        Role role = new Role(rs.getInt("roleID"), rs.getString("role"));
        return new Account(
                rs.getInt("accountID"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email"),
                role
        );
    }

    public Optional<Account> checkAccount(Account account) {
        if (account.getUsername() == null || account.getPassword() == null) {
            return Optional.empty();
        }

        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_CHECK_ACCOUNT)) {
            String identifier = account.getUsername();
            stmt.setString(1, identifier);
            stmt.setString(2, identifier);
            stmt.setString(3, account.getPassword());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapAccount(rs));
                }
            }
        } catch (SQLException e) {
            logger.severe("Error checking account: " + e.getMessage());
        }
        return Optional.empty();
    }

    public static ArrayList<Account> getAllAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL_ACCOUNTS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                accounts.add(mapAccount(rs));
            }
        } catch (SQLException e) {
            logger.severe("Error retrieving all accounts: " + e.getMessage());
        }
        return accounts;
    }
    
    public static ArrayList<Account> searchAccountByUsername(String username) {
    	ArrayList<Account> accounts = new ArrayList<>();
    	
    	try(Connection conn = ConnectDatabase.checkConnect();
    		PreparedStatement stmt = conn.prepareStatement(SQL_SEARCH_ACCOUNT)) {
    			stmt.setString(1,"%" + username + "%");
    			try(ResultSet rs = stmt.executeQuery()){
    				while (rs.next()) {
    					accounts.add(mapAccount(rs));
    				}
    			} 
			}catch (SQLException e) {
				logger.severe("Error: " + e.getMessage());
		}
    	return accounts;
    }

    public static boolean createAccount(Account account) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_ACCOUNT)) {
            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());
            stmt.setString(3, account.getEmail());
            stmt.setInt(4, account.getRole().getRoleID());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error creating account: " + e.getMessage());
        }
        return false;
    }

    public static boolean updateAccount(Account account) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_ACCOUNT)) {
            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());
            stmt.setString(3, account.getEmail());
            stmt.setInt(4, account.getRole().getRoleID());
            stmt.setInt(5, account.getAccountID());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error updating account: " + e.getMessage());
        }
        return false;
    }

    public static boolean deleteAccount(int accountID) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE_ACCOUNT)) {
            stmt.setInt(1, accountID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error deleting account: " + e.getMessage());
        }
        return false;
    }
    
    public static Account getAccountById(int accountID) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ACCOUNT)) {
            stmt.setInt(1, accountID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapAccount(rs);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error retrieving account by ID: " + e.getMessage());
        }
        return null;
    }

    public static boolean checkUsername(String username) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement pstmt = conn.prepareStatement(SQL_CHECK_USERNAME)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.severe("Error checking username: " + e.getMessage());
        }
        return false;
    }

    public static boolean checkEmail(String email) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement pstmt = conn.prepareStatement(SQL_CHECK_EMAIL)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.severe("Error checking email: " + e.getMessage());
        }
        return false;
    }

}
