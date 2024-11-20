package models.bean;

public class Course {
	private String courseID;
    private String courseName;
    private int credits;
    private int departmentID;
    private String status;        
    private String courseCode;    
    private String courseType;  
    
	public Course() {
		super();
	}

	public Course(String courseID, String courseName, int credits, int departmentID,
			String status, String courseCode, String courseType) {
		super();
		this.courseID = courseID;
		this.courseName = courseName;
		this.credits = credits;
		this.departmentID = departmentID;
		this.status = status;
		this.courseCode = courseCode;
		this.courseType = courseType;
	}

	public String getCourseID() {
		return courseID;
	}

	public String getCourseName() {
		return courseName;
	}

	public int getCredits() {
		return credits;
	}

	public int getDepartmentID() {
		return departmentID;
	}

	public String getStatus() {
		return status;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public String getCourseType() {
		return courseType;
	}

	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public void setDepartmentID(int departmentID) {
		this.departmentID = departmentID;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	
	
}