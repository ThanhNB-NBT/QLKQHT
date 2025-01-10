package models.dao;

import models.bean.Account;
import models.bean.Role;
import common.ConnectDatabase;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class AccountDAO {
	private static final Logger logger = Logger.getLogger(AccountDAO.class.getName());

	private static Account mapAccount(ResultSet rs) throws SQLException {
		Account account = new Account();
		account.setAccountID(rs.getInt("accountID"));
		account.setUsername(rs.getString("username"));
		account.setPassword(rs.getString("password"));
		account.setEmail(rs.getString("email"));
		account.setAvatar(rs.getString("avatar"));

		// Map role
		Role role = new Role();
		role.setRoleID(rs.getInt("roleID"));
		role.setRole(rs.getString("role"));
		account.setRole(role);

		// Map teacherID and studentID
		String teacherID = rs.getString("teacherID");
		String studentID = rs.getString("studentID");
		account.setTeacherID(teacherID != null ? teacherID : null);
		account.setStudentID(studentID != null ? studentID : null);
		account.setStudentName(rs.getString("studentName"));
		account.setTeacherName(rs.getString("teacherName"));

		return account;
	}

	// kiểm tra tài khoản đăng nhập
	private static final String SQL_CHECK_ACCOUNT =
			"SELECT a.accountID, a.username, a.password, a.email, a.avatar, r.roleID, r.role, t.teacherID, s.studentID,"
			+ "CONCAT(t.FirstName, ' ' , t.LastName) AS TeacherName, CONCAT(s.FirstName, ' ' , s.LastName) AS StudentName "
			+ "FROM Accounts a " + "JOIN Roles r ON a.roleID = r.roleID "
			+ "LEFT JOIN Teachers t ON a.accountID = t.accountID "
			+ "LEFT JOIN Students s ON a.accountID = s.accountID "
			+ "WHERE (a.username = ? OR a.email = ?) AND a.password = ? ";

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
			logger.severe("Lỗi khi kiểm tra tài khoản: " + e.getMessage());
		}
		return Optional.empty();
	}

	// Lấy tất cả đển hiển thị
	private static final String SQL_SELECT_ALL_ACCOUNTS =
		"SELECT a.accountID, a.username, a.password, a.email, a.avatar, r.roleID, r.role, t.teacherID, s.studentID,"
		+ "CONCAT(t.FirstName, ' ' , t.LastName) AS TeacherName, CONCAT(s.FirstName, ' ' , s.LastName) AS StudentName "
		+ "FROM Accounts a " + "JOIN Roles r ON a.roleID = r.roleID "
		+ "LEFT JOIN Teachers t ON a.accountID = t.accountID "
		+ "LEFT JOIN Students s ON a.accountID = s.accountID "
		+ "ORDER BY a.RoleID";

	public static List<Account> getAllAccounts() {
		List<Account> accounts = new ArrayList<>();
		try (Connection conn = ConnectDatabase.checkConnect();
				PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL_ACCOUNTS);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				accounts.add(mapAccount(rs));
			}
		} catch (SQLException e) {
			logger.severe("Lỗi khi lấy tất cả Account: " + e.getMessage());
		}
		return accounts;
	}

	// Tìm kiếm
	private static final String SQL_SEARCH_ACCOUNT =
		"SELECT a.accountID, a.username, a.password, a.email, a.avatar, r.roleID, r.role, t.teacherID, s.studentID,"
		+ "CONCAT(t.FirstName, ' ' , t.LastName) AS TeacherName, CONCAT(s.FirstName, ' ' , s.LastName) AS StudentName "
		+ "FROM Accounts a " + "JOIN Roles r ON a.roleID = r.roleID "
		+ "LEFT JOIN Teachers t ON a.accountID = t.accountID "
		+ "LEFT JOIN Students s ON a.accountID = s.accountID "
		+ "WHERE a.Username LIKE ?";

	public static List<Account> searchAccountByUsername(String username) {
		List<Account> accounts = new ArrayList<>();

		try (Connection conn = ConnectDatabase.checkConnect();
				PreparedStatement stmt = conn.prepareStatement(SQL_SEARCH_ACCOUNT)) {
			stmt.setString(1, "%" + username + "%");
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					accounts.add(mapAccount(rs));
				}
			}
		} catch (SQLException e) {
			logger.severe("Lỗi khi tìm kiếm Account: " + e.getMessage());
		}
		return accounts;
	}

	//Thêm tài khoản
	private static final String SQL_INSERT_ACCOUNT = "INSERT INTO Accounts (Username, Password, Email, Avatar, RoleID) VALUES (?, ?, ?, ?, ?)";
	public static boolean createAccount(Account account) {
		try (Connection conn = ConnectDatabase.checkConnect();
				PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_ACCOUNT)) {
			stmt.setString(1, account.getUsername());
			stmt.setString(2, account.getPassword());
			stmt.setString(3, account.getEmail());
			stmt.setString(4, account.getAvatar());
			stmt.setInt(5, account.getRole().getRoleID());

			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			logger.severe("Lỗi khi tạo tài khoản: " + e.getMessage());
		}
		return false;
	}

	//Lấy accountID vừa tạo (Dùng ở StudentServlet và TeacherServlet)
	public static int createAccountAndReturnID(Account account) {
		try (Connection conn = ConnectDatabase.checkConnect();
				PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_ACCOUNT, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setString(1, account.getUsername());
			stmt.setString(2, account.getPassword());
			stmt.setString(3, account.getEmail());
			stmt.setString(4, account.getAvatar());
			stmt.setInt(5, account.getRole().getRoleID());

			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						return generatedKeys.getInt(1);
					}
				}
			}
		} catch (SQLException e) {
			logger.severe("Lỗi khi tạo tài khoản và trả về AccountID: " + e.getMessage());
		}
		return -1;
	}

	//Cập nhật tài khoản
	private static final String SQL_UPDATE_ACCOUNT = "UPDATE Accounts SET Username = ?, Password = ?, Email = ?, Avatar = ?, RoleID = ? WHERE AccountID = ?";
	public static boolean updateAccount(Account account) {
		try (Connection conn = ConnectDatabase.checkConnect();
				PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_ACCOUNT)) {
			stmt.setString(1, account.getUsername());
			stmt.setString(2, account.getPassword());
			stmt.setString(3, account.getEmail());
			stmt.setString(4, account.getAvatar());
			stmt.setInt(5, account.getRole().getRoleID());
			stmt.setInt(6, account.getAccountID());

			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			logger.severe("Error updating account: " + e.getMessage());
		}
		return false;
	}

	//Xoá tài khoản
	public static boolean deleteAccount(int accountID) {
	    final String UPLOAD_DIRECTORY = "D:/eclipse-workspace/QLKQHT/src/main/webapp/";
	    String avatarPath = getAvatarPath(accountID);

	    if (avatarPath == null) {
	        logger.warning("Không tìm thấy đường dẫn avatar cho AccountID: " + accountID);
	        return false;
	    }

	    if (!deleteAccountFromDatabase(accountID)) {
	        logger.severe("Không thể xóa tài khoản khỏi cơ sở dữ liệu cho AccountID: " + accountID);
	        return false;
	    }

	    if (!isDefaultAvatar(avatarPath)) {
	        deleteAvatarFile(UPLOAD_DIRECTORY, avatarPath);
	    }

	    return true;
	}
	private static final String SQL_GET_AVATAR = "SELECT Avatar FROM Accounts WHERE AccountID = ?";
	private static String getAvatarPath(int accountID) {
	    try (Connection conn = ConnectDatabase.checkConnect();
	         PreparedStatement stmt = conn.prepareStatement(SQL_GET_AVATAR)) {
	        stmt.setInt(1, accountID);
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getString("Avatar");
	            }
	        }
	    } catch (SQLException e) {
	        logger.severe("Lỗi khi lấy đường dẫn avatar: " + e.getMessage());
	    }
	    return null;
	}

	private static final String SQL_DELETE_ACCOUNT = "DELETE FROM Accounts WHERE AccountID = ?";
	private static boolean deleteAccountFromDatabase(int accountID) {
	    try (Connection conn = ConnectDatabase.checkConnect();
	         PreparedStatement stmt = conn.prepareStatement(SQL_DELETE_ACCOUNT)) {
	        stmt.setInt(1, accountID);
	        return stmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        logger.severe("Lỗi khi xóa tài khoản: " + e.getMessage());
	        return false;
	    }
	}

	private static boolean isDefaultAvatar(String avatarPath) {
	    return "assets/img/user.jpg".equals(avatarPath);
	}

	private static void deleteAvatarFile(String uploadDirectory, String avatarPath) {
	    File fileToDelete = new File(uploadDirectory + avatarPath);
	    if (fileToDelete.exists() && fileToDelete.isFile()) {
	        if (fileToDelete.delete()) {
	            logger.info("Xóa ảnh thành công: " + avatarPath);
	        } else {
	            logger.warning("Không thể xóa ảnh: " + avatarPath);
	        }
	    }
	}

	//Lấy thông tin tài khoản theo AccountID
	private static final String SQL_SELECT_ACCOUNT =
		"SELECT a.accountID, a.username, a.password, a.email, a.avatar, r.roleID, r.role, t.teacherID, s.studentID,"
		+ "CONCAT(t.FirstName, ' ' , t.LastName) AS TeacherName, CONCAT(s.FirstName, ' ' , s.LastName) AS StudentName "
		+ "FROM Accounts a " + "JOIN Roles r ON a.roleID = r.roleID "
		+ "LEFT JOIN Teachers t ON a.accountID = t.accountID "
		+ "LEFT JOIN Students s ON a.accountID = s.accountID "
		+ "WHERE a.AccountID = ?";
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

	//Kiểm tra trùng tên tài khoản
	private static final String SQL_CHECK_USERNAME = "SELECT COUNT(*) FROM Accounts WHERE Username = ?";
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

	//Kiểm tra trùng Email
	private static final String SQL_CHECK_EMAIL = "SELECT COUNT(*) FROM Accounts WHERE Email = ?";
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

	//Cập nhật Avatar
	private static final String SQL_UPDATE_AVATAR = "UPDATE Accounts SET Avatar = ? WHERE AccountID = ?";
	public static boolean updateAvatar(int accountID, String newAvatar) {
		try (Connection conn = ConnectDatabase.checkConnect();
				PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_AVATAR)) {

			// Set các tham số
			stmt.setString(1, newAvatar); // Tên file ảnh mới
			stmt.setInt(2, accountID); // ID của tài khoản cần cập nhật

			int rowsUpdated = stmt.executeUpdate();
			return rowsUpdated > 0; // Trả về true nếu cập nhật thành công
		} catch (SQLException e) {
			e.printStackTrace();
			return false; // Trả về false nếu có lỗi xảy ra
		}
	}

	//Đổi mật khẩu
	private static final String SQL_UPDATE_PASSWORD = "UPDATE Accounts SET Password = ? WHERE AccountID = ?";
	public static boolean changePassword(int accountID, String hashedPassword) {
	    try (Connection conn = ConnectDatabase.checkConnect();
	         PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_PASSWORD)) {

	        // Set các tham số
	        stmt.setString(1, hashedPassword); // Mật khẩu mới
	        stmt.setInt(2, accountID);      // ID của tài khoản cần đổi mật khẩu

	        int rowsUpdated = stmt.executeUpdate();
	        return rowsUpdated > 0; // Trả về true nếu cập nhật thành công
	    } catch (SQLException e) {
	        logger.severe("Lỗi khi đổi mật khẩu: " + e.getMessage());
	    }
	    return false; // Trả về false nếu có lỗi xảy ra
	}


}
