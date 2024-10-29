package models.dao;

import models.bean.Account;
import models.bean.Role;
import common.ConnectDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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

    // Kiểm tra tài khoản khi đăng nhập
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

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Role role = new Role(rs.getInt("roleID"), rs.getString("role"));
                Account loggedInAccount = new Account(
                        rs.getInt("accountID"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        role
                );
                return Optional.of(loggedInAccount);
            }
        } catch (SQLException e) {
            logger.severe("Error checking account: " + e.getMessage());
        }
        return Optional.empty();
    }

    // Lấy tất cả tài khoản
    public static List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL_ACCOUNTS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Role role = new Role(rs.getInt("roleID"), rs.getString("role"));
                Account account = new Account(
                        rs.getInt("accountID"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        role
                );
                accounts.add(account);
            }
        } catch (SQLException e) {
            logger.severe("Error retrieving all accounts: " + e.getMessage());
        }
        return accounts;
    }

    // Tạo tài khoản mới
    public static boolean createAccount(Account account) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_ACCOUNT)) {
            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());
            stmt.setString(3, account.getEmail()); // Mật khẩu đã được băm
            stmt.setInt(4, account.getRole().getRoleID());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error creating account: " + e.getMessage());
        }
        return false;
    }

    // Cập nhật tài khoản
    public static boolean updateAccount(Account account) {
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_ACCOUNT)) {
            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword()); // Mật khẩu đã được băm
            stmt.setString(3, account.getEmail());
            stmt.setInt(4, account.getRole().getRoleID());
            stmt.setInt(5, account.getAccountID());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.severe("Error updating account: " + e.getMessage());
        }
        return false;
    }

    // Xóa tài khoản
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
    
 // Lấy tài khoản theo ID
    public static Account getAccountById(int accountID) {
        Account account = null;
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ACCOUNT)) {
            stmt.setInt(1, accountID);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Role role = new Role(rs.getInt("roleID"), rs.getString("role"));
                    account = new Account(
                            rs.getInt("accountID"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email"),
                            role
                    );
                }
            }
        } catch (SQLException e) {
            logger.severe("Error retrieving account by ID: " + e.getMessage());
        }
        return account;
    }
    
    public static boolean checkUsername(String username) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE username = ?";
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // Nếu số lượng là 0 thì username là duy nhất
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Mặc định là không duy nhất nếu có lỗi
    }
    public static boolean checkEmail(String email) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE email = ?";
        try (Connection conn = ConnectDatabase.checkConnect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // Nếu số lượng là 0 thì email là duy nhất
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Mặc định là không duy nhất nếu có lỗi
    }

}
