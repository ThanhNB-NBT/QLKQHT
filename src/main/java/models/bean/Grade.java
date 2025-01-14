package models.bean;

import java.util.Map;

import common.XMLConfigManager;

public class Grade {
    private String gradeID;
    private String studentClassID;
    private Double attendanceScore;
    private Double midtermScore;
    private Double finalExamScore;
    private Double componentScore;
    private String gradeLetter;
    private GradeStatus gradeStatus = GradeStatus.PENDING;
    private String gradeComment;
    private String studentCode;
    private String studentName;
    private String courseName;
    private String courseCode;
    private String semester;
    private String credits;
    private static String classID;
    private static String xmlContent;

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

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getSemester() {
        return semester;
    }

    public String getCredits() {
        return credits;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public static void setClassID(String ID) {
        classID = ID;
    }

    public String getGradeStatus() {
        return gradeStatus.getCode();
    }

    public String getGradeStatusLabel() {
        return gradeStatus.getLabel();
    }

    public void setGradeStatus(String statusCode) {
        this.gradeStatus = GradeStatus.fromCode(statusCode);
    }

	public String getGradeComment() {
		return gradeComment;
	}

	public void setGradeComment(String gradeComment) {
		this.gradeComment = gradeComment;
	}

	// Thêm method để set nội dung XML
    public static void setXMLContent(String content) {
        xmlContent = content;
        System.out.println("XML Content đã được set trong Grade: " + (content != null ? "Có nội dung" : "Null"));
    }

    // Sửa lại phương thức calculateComponentScore
    private Double calculateComponentScore() {
    	System.out.println("Bắt đầu tính điểm thành phần...");
        System.out.println("ClassID: " + classID);
        System.out.println("XML Content null?: " + (xmlContent == null));
        if (classID == null || classID.isEmpty() || xmlContent == null) {
            return 0.0;
        }
        double[] weights = XMLConfigManager.getWeights(xmlContent, classID);
        double attendance = this.attendanceScore != null ? this.attendanceScore : 0.0;
        double midterm = this.midtermScore != null ? this.midtermScore : 0.0;
        double finalExam = this.finalExamScore != null ? this.finalExamScore : 0.0;
        return roundToOneDecimal(attendance * weights[0] + midterm * weights[1] + finalExam * weights[2]);
    }

    // Sửa lại phương thức calculateGradeLetter
    private String calculateGradeLetter() {
        if (classID == null || classID.isEmpty() || xmlContent == null) {
            return "F";
        }
        Map<String, double[]> gradeLetters = XMLConfigManager.getGradeLetters(xmlContent, classID);
        for (Map.Entry<String, double[]> entry : gradeLetters.entrySet()) {
            double min = entry.getValue()[0];
            double max = entry.getValue()[1];
            if (componentScore >= min && componentScore <= max) {
                return entry.getKey();
            }
        }
        return "F";
    }

    // Cập nhật điểm thành phần và điểm chữ khi có thay đổi
    private void updateComponentScoreAndGradeLetter() {
        this.componentScore = calculateComponentScore();
        this.gradeLetter = calculateGradeLetter();
    }

    // Làm tròn giá trị đến 1 chữ số thập phân
    private double roundToOneDecimal(Double value) {
        return Math.round(value * 10.0) / 10.0;
    }

 // Định nghĩa Enum cho trạng thái duyệt điểm
    public enum GradeStatus {
        PENDING("0", "Chờ duyệt"),
        APPROVED("1", "Đã duyệt"),
        REJECTED("2", "Từ chối"),
        REVISED("3", "Đã sửa");

        private final String code;
        private final String label;

        GradeStatus(String code, String label) {
            this.code = code;
            this.label = label;
        }

        public String getCode() {
            return code;
        }

        public String getLabel() {
            return label;
        }

        public static GradeStatus fromCode(String code) {
            for (GradeStatus status : values()) {
                if (status.getCode().equals(code)) {
                    return status;
                }
            }
            return PENDING; // Mặc định là chờ duyệt
        }
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
