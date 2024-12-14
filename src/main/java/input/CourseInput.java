package input;

import jakarta.servlet.http.HttpServletRequest;

public class CourseInput {
    private Integer courseID;
    private String courseName;
    private String courseCode;
    private int credits;
    private int departmentID;
    private String courseType;
    private String status;

    // Constructor cho Create
    public CourseInput(String courseName, String courseCode, int credits, int departmentID, String courseType, String status) {
        this(null, courseName, courseCode, credits, departmentID, courseType, status);
    }

    // Constructor cho Update
    public CourseInput(Integer courseID, String courseName, String courseCode, int credits, int departmentID, String courseType, String status) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.credits = credits;
        this.departmentID = departmentID;
        this.courseType = courseType;
        this.status = status;
    }

    // Tạo đối tượng từ request
    public static CourseInput fromRequest(HttpServletRequest request, boolean isUpdate) {
        String courseIDStr = request.getParameter("courseID");
        Integer courseID = isUpdate && courseIDStr != null ? Integer.parseInt(courseIDStr) : null;

        String courseName = request.getParameter("courseName");
        String courseCode = request.getParameter("courseCode");
        int credits = Integer.parseInt(request.getParameter("credits"));
        int departmentID = Integer.parseInt(request.getParameter("departmentID"));
        String courseType = request.getParameter("courseType");
        String status = request.getParameter("status");

        return new CourseInput(courseID, courseName, courseCode, credits, departmentID, courseType, status);
    }

    public CourseInput(Integer courseID, int credits, String courseType, String status) {
        this.courseID = courseID;
        this.credits = credits;
        this.courseType = courseType;
        this.status = status;
    }

    public static CourseInput updateForm(HttpServletRequest request) {
        String courseIDStr = request.getParameter("courseID");
        Integer courseID = courseIDStr != null ? Integer.parseInt(courseIDStr) : null;

        int credits = Integer.parseInt(request.getParameter("credits"));
        String courseType = request.getParameter("courseType");
        String status = request.getParameter("status");

        return new CourseInput(courseID, credits, courseType, status);
    }

    public Integer getCourseID() {
        return courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public int getCredits() {
        return credits;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public String getCourseType() {
        return courseType;
    }

    public String getStatus() {
        return status;
    }
}
