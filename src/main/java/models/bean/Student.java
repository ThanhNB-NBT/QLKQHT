package models.bean;

import java.sql.Date;

public class Student {
	private int studentID;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String email;
    private String phone;
    private String address;
    private Date enrollmentYear;
    private String majorName;  
	private int accountID;
    private String avatar;
    
    public Student() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Student(int studentID, String firstName, String lastName, Date dateOfBirth, String email, String phone,
			String address, Date enrollmentYear, String majorName, int accountID, String avatar) {
		super();
		this.studentID = studentID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.enrollmentYear = enrollmentYear;
		this.majorName = majorName;
		this.accountID = accountID;
		this.avatar = avatar;
	}

	public int getStudentID() {
		return studentID;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public String getAddress() {
		return address;
	}

	public Date getEnrollmentYear() {
		return enrollmentYear;
	}

	public String getMajorName() {
		return majorName;
	}

	public int getAccountID() {
		return accountID;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setEnrollmentYear(Date enrollmentYear) {
		this.enrollmentYear = enrollmentYear;
	}

	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}

	public void setAccountID(int accountID) {
		this.accountID = accountID;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
    
}
