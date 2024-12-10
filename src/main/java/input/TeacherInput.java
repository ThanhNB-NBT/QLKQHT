package input;

import java.sql.Date;
import java.text.Normalizer;
import java.text.ParseException;
import java.util.Random;
import jakarta.servlet.http.HttpServletRequest;

public class TeacherInput {
    private Integer teacherID;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Integer departmentID;
    private String office;
    private Date hireDate;
    private int accountID;

    public TeacherInput(Integer teacherID, String firstName, String lastName, String email, String phone, Integer departmentID, String office, Date hireDate, int accountID) {
        this.teacherID = teacherID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.departmentID = departmentID;
        this.office = office;
        this.hireDate = hireDate;
        this.accountID = accountID;
    }

    public TeacherInput(Integer teacherID, String firstName, String lastName, String email, String phone, Integer departmentID, String office, Date hireDate) {
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
                        Integer departmentID, String office, Date hireDate) {
        this(null, firstName, lastName, email, phone, departmentID, office, hireDate);
    }

    // Thêm các getter để lấy giá trị
    public Integer getTeacherID() { return teacherID; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public Integer getDepartmentID() { return departmentID; }
    public String getOffice() { return office; }
    public Date getHireDate() { return hireDate; }
    public int getAccountID() { return accountID; }

    public static TeacherInput fromRequest(HttpServletRequest request) throws ParseException {
        String teacherIDParam = request.getParameter("teacherID");
        Integer teacherID = (teacherIDParam != null && !teacherIDParam.isEmpty())
                            ? Integer.parseInt(teacherIDParam)
                            : null;

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        Integer departmentID = Integer.parseInt(request.getParameter("departmentID"));
        String office = request.getParameter("office");
        String accountIDParam = request.getParameter("accountID");
        int accountID = (accountIDParam != null && !accountIDParam.isEmpty())
        				? Integer.parseInt(accountIDParam)
        				: 0;
        String hireDateStr = request.getParameter("hireDate");
        Date hireDate = null;
        if (hireDateStr != null && !hireDateStr.isEmpty()) {
            hireDate = Date.valueOf(hireDateStr);
        }

        return new TeacherInput(teacherID, firstName, lastName, email, phone, departmentID, office, hireDate, accountID);
    }

    public static String generateUsernameForTeacher(String firstName, String lastName) {

        String normalizedFirstName = removeDiacritics(firstName);
        String normalizedLastName = removeDiacritics(lastName);

        String baseUsername = (normalizedFirstName + normalizedLastName).toLowerCase().replaceAll("\\s+", "");

        Random random = new Random();
        int randomNumber = random.nextInt(900) + 100;

        String username = baseUsername + randomNumber;

        return username;
    }

    private static String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    }
}
