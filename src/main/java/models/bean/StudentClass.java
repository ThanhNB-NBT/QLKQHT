package models.bean;

public class StudentClass {
	private String studentClassID;
    private String classID;
    private String studentID;
    private String status;
    
	public StudentClass() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StudentClass(String studentClassID, String classID, String studentID, String status) {
		super();
		this.studentClassID = studentClassID;
		this.classID = classID;
		this.studentID = studentID;
		this.status = status;
	}

	public String getStudentClassID() {
		return studentClassID;
	}

	public String getClassID() {
		return classID;
	}

	public String getStudentID() {
		return studentID;
	}

	public String getStatus() {
		return status;
	}

	public void setStudentClassID(String studentClassID) {
		this.studentClassID = studentClassID;
	}

	public void setClassID(String classID) {
		this.classID = classID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
    
}
