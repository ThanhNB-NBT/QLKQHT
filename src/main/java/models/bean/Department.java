package models.bean;

public class Department {
	private int departmentID;
    private String departmentName;
    private String email;
    private String phone;
    
	public Department() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Department(int departmentID, String departmentName, String email, String phone) {
		super();
		this.departmentID = departmentID;
		this.departmentName = departmentName;
		this.email = email;
		this.phone = phone;
	}
	
	public int getDepartmentID() {
		return departmentID;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public String getEmail() {
		return email;
	}
	public String getPhone() {
		return phone;
	}
	public void setDepartmentID(int departmentID) {
		this.departmentID = departmentID;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
