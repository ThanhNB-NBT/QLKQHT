package models.bean;

import java.sql.Date;

public class Class {

	private Integer classID;
	private int courseID;
	private int teacherID;
	private String classTime;
	private String room;
	private String semester;
	private String className;
	private String status;
	private int maxStudents;
	private int registeredStudents;
	private int totalLessions;
	private Date startDate;
	private Date endDate;
	private String classType;
	private Integer parentClassID;
	private Teacher teacher;
	private Course course;

	// Constructors
	public Class() {

	}

	// Lấy tất cả
	public Class(Integer classID, int courseID, int teacherID, String classTime, String room, String semester,
			String className, String status, int maxStudents, int registeredStudents, int totalLessions, Date startDate,
			Date endDate, String classType, Integer parentClassID) {
		this.classID = classID;
		this.courseID = courseID;
		this.teacherID = teacherID;
		this.classTime = classTime;
		this.room = room;
		this.semester = semester;
		this.className = className;
		this.status = status;
		this.maxStudents = maxStudents;
		this.registeredStudents = registeredStudents;
		this.totalLessions = totalLessions;
		this.startDate = startDate;
		this.endDate = endDate;
		this.classType = classType;
		this.parentClassID = parentClassID;
	}

	// update
	public Class(Integer classID, int teacherID, String classTime, String room, String status, int maxStudents,
			int totalLessions) {
		this.classID = classID;
		this.teacherID = teacherID;
		this.classTime = classTime;
		this.room = room;
		this.status = status;
		this.maxStudents = maxStudents;
		this.totalLessions = totalLessions;
	}

	// Create
	public Class(int courseID, int teacherID, String classTime, String room, String semester, String className,
			String status, int maxStudents, int totalLessions, Date startDate, Date endDate, String classType,
			Integer parentClassID) {
		this.courseID = courseID;
		this.teacherID = teacherID;
		this.classTime = classTime;
		this.room = room;
		this.semester = semester;
		this.className = className;
		this.status = status;
		this.maxStudents = maxStudents;
		this.totalLessions = totalLessions;
		this.startDate = startDate;
		this.endDate = endDate;
		this.classType = classType;
		this.parentClassID = parentClassID;
	}

	// Getters
	public Integer getClassID() {
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

	public String getClassName() {
		return className;
	}

	public String getStatus() {
		return status;
	}

	public int getMaxStudents() {
		return maxStudents;
	}

	public int getRegisteredStudents() {
		return registeredStudents;
	}

	public int getTotalLessions() {
		return totalLessions;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getClassType() {
		return classType;
	}

	public Integer getParentClassID() {
		return parentClassID;
	}

	// Setters
	public void setClassID(Integer classID) {
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

	public void setClassName(String classCode) {
		this.className = classCode;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setMaxStudents(int maxStudents) {
		this.maxStudents = maxStudents;
	}

	public void setRegisteredStudents(int registeredStudents) {
		this.registeredStudents = registeredStudents;
	}

	public void setTotalLessions(int totalLessions) {
		this.totalLessions = totalLessions;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public void setParentClassID(Integer parentClassID) {
		this.parentClassID = parentClassID;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public Course getCourse() {
		return course;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	@Override
	public String toString() {
		return "Class{"
				+ "classID=" + classID + '\''
				+ ", courseID=" + courseID + '\''
				+ ", teacherID=" + teacherID + '\''
				+ ", classTime='"+ classTime + '\''
				+ ", room='" + room + '\''
				+ ", semester='" + semester + '\''
				+ ", className='" + className + '\''
				+ ", status='" + status + '\''
				+ ", maxStudents=" + maxStudents + '\''
				+ ", registeredStudents=" + registeredStudents + ", totalLessions=" + totalLessions + ", startDate="
				+ startDate + ", endDate=" + endDate + ", classType='" + classType + '\'' + ", parentClassID="
				+ parentClassID + ", teacher=" + (teacher != null ? teacher.toString() : "null") + ", course="
				+ (course != null ? course.toString() : "null") + '}';
	}
}
