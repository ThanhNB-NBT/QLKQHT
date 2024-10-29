package models.bean;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Account {
    private int accountID;
    private String username;
    private String password;
    private String email;
    private Role role;

    // Constructors
    public Account() {
        super();
    }

    public Account(int accountID, String username, String password, String email, Role role) {
        this.accountID = accountID;
        this.username = username;
        setPassword(password); // Băm mật khẩu trước khi lưu
        this.email = email;
        this.role = role;
    }
    
    public Account(int accountID, String username, String email, Role role) {
        this.accountID = accountID;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public Account(String username, String password, String email, Role role) {
        this.username = username;
        setPassword(password); // Băm mật khẩu trước khi lưu
        this.email = email;
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
        this.password = hashPassword(password); // Băm mật khẩu tự động
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
