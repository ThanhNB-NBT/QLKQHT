package models.bean;

public class Grade {
    private String gradeID;
    private String studentClassID;
    private double attendanceScore;
    private double midtermScore;
    private double finalExamScore;
    private double componentScore;
    private String gradeLetter;
    private String studentCode;
    private String studentName;

    public Grade() {
    }

    // Constructor cho việc cập nhật điểm
    public Grade(String gradeID, double attendanceScore, double midtermScore, double finalExamScore) {
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

    public double getAttendanceScore() {
        return attendanceScore;
    }

    public void setAttendanceScore(double attendanceScore) {
        this.attendanceScore = attendanceScore;
        updateComponentScoreAndGradeLetter();
    }

    public double getMidtermScore() {
        return midtermScore;
    }

    public void setMidtermScore(double midtermScore) {
        this.midtermScore = midtermScore;
        updateComponentScoreAndGradeLetter();
    }

    public double getFinalExamScore() {
        return finalExamScore;
    }

    public void setFinalExamScore(double finalExamScore) {
        this.finalExamScore = finalExamScore;
        updateComponentScoreAndGradeLetter();
    }

    public double getComponentScore() {
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
    private double calculateComponentScore() {
        return roundToOneDecimal(this.attendanceScore * 0.2 + this.midtermScore * 0.3 + this.finalExamScore * 0.5);
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
    private double roundToOneDecimal(double value) {
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
