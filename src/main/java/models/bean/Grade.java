package models.bean;

import java.sql.Date;

public class Grade {
    private String gradeID;
    private String studentClassID;
    private double attendanceScore;
    private double midtermScore;
    private double finalExamScore;
    private double componentScore;
    private String gradeLetter;
    private String createdBy;
    private Date createdDate;
    private Date updatedDate;

    public Grade() {

    }

    // Constructor cho việc tạo mới điểm
    public Grade(String gradeID, String studentClassID, double attendanceScore, double midtermScore,
                 double finalExamScore, String createdBy, Date createdDate) {
        this.gradeID = gradeID;
        this.studentClassID = studentClassID;
        this.attendanceScore = attendanceScore;
        this.midtermScore = midtermScore;
        this.finalExamScore = finalExamScore;
        this.componentScore = calculateComponentScore(attendanceScore, midtermScore, finalExamScore);
        this.gradeLetter = calculateGradeLetter(this.componentScore);
        this.createdBy = createdBy;
        this.createdDate = createdDate;
    }

    // Constructor cho việc cập nhật điểm
    public Grade(String gradeID, double attendanceScore, double midtermScore, double finalExamScore, Date updatedDate) {
        this.gradeID = gradeID;
        this.attendanceScore = attendanceScore;
        this.midtermScore = midtermScore;
        this.finalExamScore = finalExamScore;
        this.componentScore = calculateComponentScore(attendanceScore, midtermScore, finalExamScore);
        this.gradeLetter = calculateGradeLetter(this.componentScore);
        this.updatedDate = updatedDate;
    }

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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    // Phương thức tính ComponentScore
    private double calculateComponentScore(double attendanceScore, double midtermScore, double finalExamScore) {
        return roundToOneDecimal(attendanceScore * 0.2 + midtermScore * 0.3 + finalExamScore * 0.5);
    }

    // Phương thức tính GradeLetter
    private String calculateGradeLetter(double componentScore) {
        if (componentScore >= 8.5) return "A";
        if (componentScore >= 7.0) return "B";
        if (componentScore >= 5.5) return "C";
        if (componentScore >= 4.0) return "D";
        return "F";
    }

    // Cập nhật ComponentScore và GradeLetter khi điểm thay đổi
    private void updateComponentScoreAndGradeLetter() {
        this.componentScore = calculateComponentScore(this.attendanceScore, this.midtermScore, this.finalExamScore);
        this.gradeLetter = calculateGradeLetter(this.componentScore);
    }

    // Làm tròn số thập phân đến một chữ số
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
                ", createdBy='" + createdBy + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
