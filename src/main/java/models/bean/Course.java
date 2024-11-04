package models.bean;

public class Course {
	private int courseID;
    private String courseName;
    private int credits;
    private String semester;
    private String description;
    private int departmentID;
    
	public Course() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Course(int courseID, String courseName, int credits, String semester, String description, int departmentID) {
		super();
		this.courseID = courseID;
		this.courseName = courseName;
		this.credits = credits;
		this.semester = semester;
		this.description = description;
		this.departmentID = departmentID;
	}
	
	public int getCourseID() {
		return courseID;
	}
	public String getCourseName() {
		return courseName;
	}
	public int getCredits() {
		return credits;
	}
	public String getSemester() {
		return semester;
	}
	public String getDescription() {
		return description;
	}
	public int getDepartmentID() {
		return departmentID;
	}
	public void setCourseID(int courseID) {
		this.courseID = courseID;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public void setCredits(int credits) {
		this.credits = credits;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setDepartmentID(int departmentID) {
		this.departmentID = departmentID;
	}
}
