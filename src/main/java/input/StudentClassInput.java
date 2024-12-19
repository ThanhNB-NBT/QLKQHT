package input;

import jakarta.servlet.http.HttpServletRequest;

public class StudentClassInput {
    private Integer studentClassID;
    private Integer classID;
    private Integer studentID;
    private String status;

    // Constructor cho Create
    public StudentClassInput( Integer classID, Integer studentID, String status) {
        this.classID = classID;
        this.studentID = studentID;
        this.status = status;
    }

    // Tạo đối tượng từ request Create
    public static StudentClassInput inputCreate(HttpServletRequest request) {

        String classIDStr = request.getParameter("classID");
        Integer classID = (classIDStr != null && !classIDStr.trim().isEmpty())
                ? Integer.parseInt(classIDStr)
                : null;

        String studentIDStr = request.getParameter("studentID");
        Integer studentID = (studentIDStr != null && !studentIDStr.trim().isEmpty())
                ? Integer.parseInt(studentIDStr)
                : null;

        String status = request.getParameter("status");

        return new StudentClassInput( classID, studentID, status);
    }

    // Constructor cho Update
    public StudentClassInput(Integer studentClassID, String status) {
        this.studentClassID = studentClassID;
        this.status = status;
    }

 // Tạo đối tượng từ request
    public static StudentClassInput inputUpdate(HttpServletRequest request) {
        Integer studentClassID = null;
        String studentClassIDStr = request.getParameter("studentClassID");

        if (studentClassIDStr != null && !studentClassIDStr.trim().isEmpty()) {
            studentClassID = Integer.parseInt(studentClassIDStr);
        }

        String status = request.getParameter("status");

        return new StudentClassInput(studentClassID, status);
    }

    // Getter và Setter
    public Integer getStudentClassID() {
        return studentClassID;
    }

    public void setStudentClassID(Integer studentClassID) {
        this.studentClassID = studentClassID;
    }

    public Integer getClassID() {
        return classID;
    }

    public void setClassID(Integer classID) {
        this.classID = classID;
    }

    public Integer getStudentID() {
        return studentID;
    }

    public void setStudentID(Integer studentID) {
        this.studentID = studentID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
