package models.bean;

public class StudentClass {
	private Integer studentClassID;
    private Integer classID;
    private Integer studentID;
    private String status;

	public StudentClass() {
		super();
	}

	public StudentClass(Integer studentClassID, Integer classID, Integer studentID, String status) {
		super();
		this.studentClassID = studentClassID;
		this.classID = classID;
		this.studentID = studentID;
		this.status = status;
	}

	public Integer getStudentClassID() {
		return studentClassID;
	}

	public Integer getClassID() {
		return classID;
	}

	public Integer getStudentID() {
		return studentID;
	}

	public String getStatus() {
		return status;
	}

	public void setStudentClassID(Integer studentClassID) {
		this.studentClassID = studentClassID;
	}

	public void setClassID(Integer classID) {
		this.classID = classID;
	}

	public void setStudentID(Integer studentID) {
		this.studentID = studentID;
	}

	public void setStatus(String status) {
		this.status = status;
	}


}
