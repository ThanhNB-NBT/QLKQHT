package models.bean;

public class Class {
	private String classID;
    private String courseID;
    private String teacherID;
    private String classTime;
    private String room;
    private String semester;
    private String classCode;   
    private String status;       
    private int maxStudents; 
    
	public Class() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Class(String classID, String courseID, String teacherID, String classTime, String room, String semester,
			String classCode, String status, int maxStudents) {
		super();
		this.classID = classID;
		this.courseID = courseID;
		this.teacherID = teacherID;
		this.classTime = classTime;
		this.room = room;
		this.semester = semester;
		this.classCode = classCode;
		this.status = status;
		this.maxStudents = maxStudents;
	}

	public String getClassID() {
		return classID;
	}

	public String getCourseID() {
		return courseID;
	}

	public String getTeacherID() {
		return teacherID;
	}

	public String getClassTime() {
		return classTime;
	}

	public String getRoom() {
		return room;
	}

	public String getSemester() {
		return semester;
	}

	public String getClassCode() {
		return classCode;
	}

	public String getStatus() {
		return status;
	}

	public int getMaxStudents() {
		return maxStudents;
	}

	public void setClassID(String classID) {
		this.classID = classID;
	}

	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}

	public void setTeacherID(String teacherID) {
		this.teacherID = teacherID;
	}

	public void setClassTime(String classTime) {
		this.classTime = classTime;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setMaxStudents(int maxStudents) {
		this.maxStudents = maxStudents;
	}
	
	
}
