package models.bean;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Account {
    private int accountID;
    private String username;
    private String password;
    private String email;
    private String avatar;
    private Role role;
    private int roleID;
    private String teacherID;
    private String studentID;
    private String studentName;
    private String teacherName;

    // Constructors
    public Account() {
        super();
    }

    public Account(int accountID, String username, String password, String email, String avatar, Role role) {
        this.accountID = accountID;
        this.username = username;
        setPassword(password);
        this.email = email;
        this.avatar = avatar;
        this.role = role;
    }

    public Account(int accountID, String username, String email, String avatar, Role role) {
        this.accountID = accountID;
        this.username = username;
        this.email = email;
        this.avatar = avatar;
        this.role = role;
    }

    public Account(Integer accountID, String username, String email, Role role) {
        this.accountID = accountID;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public Account(String username, String password, String email, String avatar, Role role) {
        this.username = username;
        setPassword(password); // Băm mật khẩu trước khi lưu
        this.email = email;
        this.avatar = avatar;
        this.role = role;
    }

    public Account(String username, String password) {
        this.username = username;
        setPassword(password); // Băm mật khẩu trước khi lưu
    }

    // Getters and Setters
    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
    	if (password == null || password.trim().isEmpty()) {
            this.password = null; // Hoặc giữ mật khẩu cũ nếu không thay đổi
        } else {
            this.password = hashPassword(password); // Băm mật khẩu tự động nếu có mật khẩu mới
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getTeacherID() {
		return teacherID;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setTeacherID(String teacherID) {
		this.teacherID = teacherID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public int getRoleID() {
		return roleID;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}

	public String getStudentName() {
		return studentName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi băm mật khẩu: ", e);
        }
    }
}
