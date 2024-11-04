package models.bean;

public class Class {
	private int classID;
    private int courseID;
    private int teacherID;
    private String classTime;
    private String room;
    private String semester;
    
	public Class() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Class(int classID, int courseID, int teacherID, String classTime, String room, String semester) {
		super();
		this.classID = classID;
		this.courseID = courseID;
		this.teacherID = teacherID;
		this.classTime = classTime;
		this.room = room;
		this.semester = semester;
	}

	public int getClassID() {
		return classID;
	}
	public int getCourseID() {
		return courseID;
	}
	public int getTeacherID() {
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
	public void setClassID(int classID) {
		this.classID = classID;
	}
	public void setCourseID(int courseID) {
		this.courseID = courseID;
	}
	public void setTeacherID(int teacherID) {
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
}
