package models.bean;

import java.sql.Date;

public class Grade {
	private int gradeID;
    private int studentID;
    private int classID;
    private float midtermScore;
    private float attendanceScore;
    private float finalExamScore;
    private float componentScore;
    private String gradeLetter;
    private String createdBy;
    private Date createdDate;
    
	public Grade() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Grade(int gradeID, int studentID, int classID, float midtermScore, float attendanceScore,
			float finalExamScore, float componentScore, String gradeLetter, String createdBy, Date createdDate) {
		super();
		this.gradeID = gradeID;
		this.studentID = studentID;
		this.classID = classID;
		this.midtermScore = midtermScore;
		this.attendanceScore = attendanceScore;
		this.finalExamScore = finalExamScore;
		this.componentScore = componentScore;
		this.gradeLetter = gradeLetter;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
	}
	
	public int getGradeID() {
		return gradeID;
	}
	public int getStudentID() {
		return studentID;
	}
	public int getClassID() {
		return classID;
	}
	public float getMidtermScore() {
		return midtermScore;
	}
	public float getAttendanceScore() {
		return attendanceScore;
	}
	public float getFinalExamScore() {
		return finalExamScore;
	}
	public float getComponentScore() {
		return componentScore;
	}
	public String getGradeLetter() {
		return gradeLetter;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setGradeID(int gradeID) {
		this.gradeID = gradeID;
	}
	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}
	public void setClassID(int classID) {
		this.classID = classID;
	}
	public void setMidtermScore(float midtermScore) {
		this.midtermScore = midtermScore;
	}
	public void setAttendanceScore(float attendanceScore) {
		this.attendanceScore = attendanceScore;
	}
	public void setFinalExamScore(float finalExamScore) {
		this.finalExamScore = finalExamScore;
	}
	public void setComponentScore(float componentScore) {
		this.componentScore = componentScore;
	}
	public void setGradeLetter(String gradeLetter) {
		this.gradeLetter = gradeLetter;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
