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
    
    private ScoreCalculator scoreCalculator;
    
    private GradeLetterCalculator gradeLetterCalculator;

    public Grade() {
        super();
        scoreCalculator = new ScoreCalculator();
        gradeLetterCalculator = new GradeLetterCalculator();
    }

    public Grade(String gradeID, String studentClassID, double attendanceScore, double midtermScore,
                 double finalExamScore, String createdBy, Date createdDate, Date updatedDate) {
        this();
        this.gradeID = gradeID;
        this.studentClassID = studentClassID;
        setAttendanceScore(attendanceScore);
        setMidtermScore(midtermScore);
        setFinalExamScore(finalExamScore);
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.gradeLetter = calculateGradeLetter();
    }

    public String getGradeID() {
        return gradeID;
    }

    public String getStudentClassID() {
        return studentClassID;
    }

    public double getAttendanceScore() {
        return attendanceScore;
    }

    public double getMidtermScore() {
        return midtermScore;
    }

    public double getFinalExamScore() {
        return finalExamScore;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setGradeID(String gradeID) {
        this.gradeID = gradeID;
    }

    public void setStudentClassID(String studentClassID) {
        this.studentClassID = studentClassID;
    }

    public void setAttendanceScore(double attendanceScore) {
        if (isValidScore(attendanceScore)) {
            this.attendanceScore = roundToOneDecimal(attendanceScore);
            this.componentScore = scoreCalculator.calculateComponentScore(this.attendanceScore, this.midtermScore, this.finalExamScore);
        } else {
            throw new IllegalArgumentException("Invalid attendance score");
        }
    }

    public void setMidtermScore(double midtermScore) {
        if (isValidScore(midtermScore)) {
            this.midtermScore = roundToOneDecimal(midtermScore);
            this.componentScore = scoreCalculator.calculateComponentScore(this.attendanceScore, this.midtermScore, this.finalExamScore);
        } else {
            throw new IllegalArgumentException("Invalid midterm score");
        }
    }

    public void setFinalExamScore(double finalExamScore) {
        if (isValidScore(finalExamScore)) {
            this.finalExamScore = roundToOneDecimal(finalExamScore);
            this.componentScore = scoreCalculator.calculateComponentScore(this.attendanceScore, this.midtermScore, this.finalExamScore);
        } else {
            throw new IllegalArgumentException("Invalid final exam score");
        }
    }


    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    private boolean isValidScore(double score) {
        return score >= 0 && score <= 10;
    }

    public String calculateGradeLetter() {
        return gradeLetterCalculator.calculateGradeLetter(this.componentScore);
    }

    public void updateGradeLetter() {
        this.gradeLetter = calculateGradeLetter();
    }

    private static class ScoreCalculator {

        public double calculateComponentScore(double attendanceScore, double midtermScore, double finalExamScore) {
            return attendanceScore * 0.2 + midtermScore * 0.3 + finalExamScore * 0.5;
        }
    }

    private double roundToOneDecimal(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    private static class GradeLetterCalculator {

        public String calculateGradeLetter(double componentScore) {
            if (componentScore >= 8.5) return "A";
            if (componentScore >= 7.0) return "B";
            if (componentScore >= 5.5) return "C";
            if (componentScore >= 4.0) return "D";
            return "F";
        }
    }
}
