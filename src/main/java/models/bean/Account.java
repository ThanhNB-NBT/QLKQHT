package models.bean;

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
		this.password = password;
		this.email = email;
		this.role = role;
	}
	
	public Account(String username, String email, Role role) {
		this.username = username;
		this.email = email;
		this.role = role;
	}
	
	public Account(String username, String password) {
		this.username = username;
		this.password = password;
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
		this.password = password;
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
}
