package input;

import java.sql.Date;
import jakarta.servlet.http.HttpServletRequest;

public class StudentInput {
    private Integer studentID;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String email;
    private String phone;
    private String address;
    private Date enrollmentYear;
    private String majorName;
    private String studentCode;
    private Integer departmentID;

    // Constructor có ID (dùng cho update)
    public StudentInput(Integer studentID, String firstName, String lastName, Date dateOfBirth, String email, String phone,
                        String address, Date enrollmentYear, String majorName, String studentCode, Integer departmentID) {
        this.studentID = studentID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.enrollmentYear = enrollmentYear;
        this.majorName = majorName;
        this.studentCode = studentCode;
        this.departmentID = departmentID;
    }

    // Constructor không có ID (dùng cho create)
    public StudentInput(String firstName, String lastName, Date dateOfBirth, String email, String phone,
                        String address, Date enrollmentYear, String majorName, String studentCode, Integer departmentID) {
        this(null, firstName, lastName, dateOfBirth, email, phone, address, enrollmentYear, majorName, studentCode, departmentID);
    }

    // Getters
    public Integer getStudentID() { return studentID; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public Date getDateOfBirth() { return dateOfBirth; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public Date getEnrollmentYear() { return enrollmentYear; }
    public String getMajorName() { return majorName; }
    public String getStudentCode() { return studentCode; }
    public Integer getDepartmentID() {return departmentID; }

    // Phương thức để chuyển đổi từ HttpServletRequest
    public static StudentInput fromRequest(HttpServletRequest request) {
        String studentIDParam = request.getParameter("studentID");
        Integer studentID = (studentIDParam != null && !studentIDParam.isEmpty())
                            ? Integer.parseInt(studentIDParam)
                            : null;

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        String dateOfBirthStr = request.getParameter("dateOfBirth");
        Date dateOfBirth = null;
        if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
            dateOfBirth = Date.valueOf(dateOfBirthStr);
        }

        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        String enrollmentYearStr = request.getParameter("enrollmentYear");
        Date enrollmentYear = null;
        if (enrollmentYearStr != null && !enrollmentYearStr.isEmpty()) {
            enrollmentYear = Date.valueOf(enrollmentYearStr);
        }

        String majorName = request.getParameter("majorName");
        String studentCode = request.getParameter("studentCode");
        String departmentIDParam = request.getParameter("departmentID");
        Integer departmentID = (departmentIDParam != null && !departmentIDParam.isEmpty())
                ? Integer.parseInt(departmentIDParam)
                : null;
        return new StudentInput(studentID, firstName, lastName, dateOfBirth, email, phone, address, enrollmentYear, majorName, studentCode, departmentID);
    }
}
