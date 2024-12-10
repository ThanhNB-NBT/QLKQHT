package models.bean;

public class Role {
	private Integer roleID;
	private String role;

	public Role(){
		super();
	}

	public Role(Integer roleID, String role) {
        this.roleID = roleID;
        this.role = role;
    }
	public Role(Integer roleID) {
        this.roleID = roleID;
    }

	public Integer getRoleID() {
		return roleID;
	}

	public String getRole() {
		return role;
	}

	public void setRoleID(Integer roleID) {
		this.roleID = roleID;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
