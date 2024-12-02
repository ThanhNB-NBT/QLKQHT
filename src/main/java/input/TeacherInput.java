package input;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import jakarta.servlet.http.HttpServletRequest;

public class TeacherInput {
    private Integer teacherID;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private int departmentID;
    private String office;
    private Date hireDate;

    public TeacherInput(Integer teacherID, String firstName, String lastName, String email, String phone, int departmentID, String office, Date hireDate) {
        this.teacherID = teacherID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.departmentID = departmentID;
        this.office = office;
        this.hireDate = hireDate;
    }
    
 // Constructor không có ID (dùng cho create)
    public TeacherInput(String firstName, String lastName, String email, String phone, 
                        int departmentID, String office, Date hireDate) {
        this(null, firstName, lastName, email, phone, departmentID, office, hireDate);
    }

    // Thêm các getter để lấy giá trị
    public Integer getTeacherID() { return teacherID; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public int getDepartmentID() { return departmentID; }
    public String getOffice() { return office; }
    public Date getHireDate() { return hireDate; }

    public static TeacherInput fromRequest(HttpServletRequest request) throws ParseException {
        String teacherIDParam = request.getParameter("teacherID");
        Integer teacherID = (teacherIDParam != null && !teacherIDParam.isEmpty()) 
                            ? Integer.parseInt(teacherIDParam) 
                            : null;

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        int departmentID = Integer.parseInt(request.getParameter("departmentID"));
        String office = request.getParameter("office");

        String hireDateStr = request.getParameter("hireDate");
        Date hireDate = null;
        if (hireDateStr != null && !hireDateStr.isEmpty()) {
            hireDate = Date.valueOf(hireDateStr); // Chuyển trực tiếp thành java.sql.Date nếu đầu vào hợp lệ
        }

        return new TeacherInput(teacherID, firstName, lastName, email, phone, departmentID, office, hireDate);
    }

}
