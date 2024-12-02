package models.bean;

import java.sql.Date;

public class Teacher {
	private int teacherID;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private int departmentID;
    private String office;
    private Date hireDate;
    private int accountID;
    private String avatar;
    private Department department;
    
	public Teacher() {
		super();
	}
	
	public Teacher(int teacherID, String firstName, String lastName, String email, String phone, int departmentID,
			String office, Date hireDate, int accountID, String avatar, Department department) {
		super();
		this.teacherID = teacherID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.departmentID = departmentID;
		this.office = office;
		this.hireDate = hireDate;
		this.accountID = accountID;
		this.avatar = avatar;
		this.department = department;
	}
	public Teacher( String firstName, String lastName, String email, String phone, int departmentID,
			String office, Date hireDate, int accountID, String avatar, Department department) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.departmentID = departmentID;
		this.office = office;
		this.hireDate = hireDate;
		this.accountID = accountID;
		this.avatar = avatar;
		this.department = department;
	}
	public Teacher( String firstName, String lastName, String email, String phone, int departmentID,
			String office, Date hireDate, String avatar) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.departmentID = departmentID;
		this.office = office;
		this.hireDate = hireDate;
		this.avatar = avatar;
	}
	
	public int getTeacherID() {
		return teacherID;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getEmail() {
		return email;
	}
	public String getPhone() {
		return phone;
	}
	public int getDepartmentID() {
		return departmentID;
	}
	public String getOffice() {
		return office;
	}
	public Date getHireDate() {
		return hireDate;
	}
	public int getAccountID() {
		return accountID;
	}
	public String getAvatar() {
		return avatar;
	}
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
	public void setTeacherID(int teacherID) {
		this.teacherID = teacherID;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setDepartmentID(int departmentID) {
		this.departmentID = departmentID;
	}
	public void setOffice(String office) {
		this.office = office;
	}
	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}
	public void setAccountID(int accountID) {
		this.accountID = accountID;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	} 
}
