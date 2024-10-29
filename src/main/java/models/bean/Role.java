package models.bean;

public class Role {
	private int roleID;
	private String role;
	
	public Role(){
		super();
	}
	
	public Role(int roleID, String role) {
        this.roleID = roleID;
        this.role = role;
    }

	public int getRoleID() {
		return roleID;
	}

	public String getRole() {
		return role;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
