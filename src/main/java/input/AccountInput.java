package input;

import jakarta.servlet.http.HttpServletRequest;

public class AccountInput {
    private Integer accountID;
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private Integer roleID;
    private String avatar;

    // Constructor cho Create
    public AccountInput(String username, String password, String confirmPassword, String email, String avatar, Integer roleID) {
        this(null, username, password, confirmPassword, email, avatar, roleID);
    }

    // Constructor cho Update
    public AccountInput(Integer accountID, String username, String password, String confirmPassword, String email, String avatar, Integer roleID) {
        this.accountID = accountID;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
        this.avatar = avatar;
        this.roleID = roleID;
    }

    // Tạo đối tượng từ request
    public static AccountInput fromRequest(HttpServletRequest request, boolean isUpdate) {
        String accountIDStr = request.getParameter("accountID");
        Integer accountID = isUpdate && accountIDStr != null ? Integer.parseInt(accountIDStr) : null;

        String username = request.getParameter("name");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("cpass");
        String email = request.getParameter("email");
        String avatar = request.getParameter("avatar");
        String roleIDStr = request.getParameter("role");
        Integer roleID = roleIDStr != null ? Integer.parseInt(roleIDStr) : null;

        return new AccountInput(accountID, username, password, confirmPassword, email, avatar, roleID);
    }

    // Getter methods
    public Integer getAccountID() {
        return accountID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public Integer getRoleID() {
        return roleID;
    }

    public String getAvatar() {
        return avatar;
    }
}
