package models.bean;

public class Grade {
    private String gradeID;
    private String studentClassID;
    private Double attendanceScore;
    private Double midtermScore;
    private Double finalExamScore;
    private Double componentScore;
    private String gradeLetter;
    private String studentCode;
    private String studentName;

    public Grade() {
    }

    // Constructor cho việc cập nhật điểm
    public Grade(String gradeID, Double attendanceScore, Double midtermScore, Double finalExamScore) {
        this.gradeID = gradeID;
        this.attendanceScore = attendanceScore;
        this.midtermScore = midtermScore;
        this.finalExamScore = finalExamScore;
        this.componentScore = calculateComponentScore();
        this.gradeLetter = calculateGradeLetter();
    }

    // Getters và Setters
    public String getGradeID() {
        return gradeID;
    }

    public void setGradeID(String gradeID) {
        this.gradeID = gradeID;
    }

    public String getStudentClassID() {
        return studentClassID;
    }

    public void setStudentClassID(String studentClassID) {
        this.studentClassID = studentClassID;
    }

    public Double getAttendanceScore() {
        return attendanceScore;
    }

    public void setAttendanceScore(Double attendanceScore) {
        this.attendanceScore = attendanceScore != null ? attendanceScore : 0.0;
        updateComponentScoreAndGradeLetter();
    }

    public Double getMidtermScore() {
        return midtermScore;
    }

    public void setMidtermScore(Double midtermScore) {
        this.midtermScore = midtermScore != null ? midtermScore : 0.0;
        updateComponentScoreAndGradeLetter();
    }

    public Double getFinalExamScore() {
        return finalExamScore;
    }

    public void setFinalExamScore(Double finalExamScore) {
        this.finalExamScore = finalExamScore != null ? finalExamScore : 0.0;
        updateComponentScoreAndGradeLetter();
    }

    public Double getComponentScore() {
        return componentScore;
    }

    public String getGradeLetter() {
        return gradeLetter;
    }

    public String getStudentCode() {
		return studentCode;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	// Phương thức tính toán điểm thành phần
	private Double calculateComponentScore() {
	    double attendance = this.attendanceScore != null ? this.attendanceScore : 0.0;
	    double midterm = this.midtermScore != null ? this.midtermScore : 0.0;
	    double finalExam = this.finalExamScore != null ? this.finalExamScore : 0.0;
	    return roundToOneDecimal(attendance * 0.2 + midterm * 0.3 + finalExam * 0.5);
	}


    // Phương thức xác định hạng điểm
    private String calculateGradeLetter() {
        if (componentScore >= 8.5) return "A";
        if (componentScore >= 7.0) return "B";
        if (componentScore >= 5.5) return "C";
        if (componentScore >= 4.0) return "D";
        return "F";
    }

    // Cập nhật điểm thành phần và hạng điểm khi có thay đổi
    private void updateComponentScoreAndGradeLetter() {
        this.componentScore = calculateComponentScore();
        this.gradeLetter = calculateGradeLetter();
    }

    // Làm tròn giá trị đến 1 chữ số thập phân
    private double roundToOneDecimal(Double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "gradeID='" + gradeID + '\'' +
                ", studentClassID='" + studentClassID + '\'' +
                ", attendanceScore=" + attendanceScore +
                ", midtermScore=" + midtermScore +
                ", finalExamScore=" + finalExamScore +
                ", componentScore=" + componentScore +
                ", gradeLetter='" + gradeLetter + '\'' +
                '}';
    }
}
