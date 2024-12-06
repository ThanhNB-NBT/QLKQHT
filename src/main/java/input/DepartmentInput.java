package input;

import jakarta.servlet.http.HttpServletRequest;

public class DepartmentInput {
    private Integer departmentID;
    private String departmentName;
    private String email;
    private String phone;

    // Constructor dùng cho update (có departmentID)
    public DepartmentInput(Integer departmentID, String departmentName, String email, String phone) {
        this.departmentID = departmentID;
        this.departmentName = departmentName;
        this.email = email;
        this.phone = phone;
    }

    // Constructor dùng cho create (không có departmentID)
    public DepartmentInput(String departmentName, String email, String phone) {
        this(null, departmentName, email, phone);
    }

    // Các phương thức getter
    public Integer getDepartmentID() { return departmentID; }
    public String getDepartmentName() { return departmentName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    // Phương thức từ request để tạo đối tượng DepartmentInput
    public static DepartmentInput fromRequest(HttpServletRequest request) {
        // Lấy các tham số từ request
        String departmentIDParam = request.getParameter("departmentID");
        Integer departmentID = (departmentIDParam != null && !departmentIDParam.isEmpty())
                            ? Integer.parseInt(departmentIDParam)
                            : null;

        String departmentName = request.getParameter("departmentName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        return new DepartmentInput(departmentID, departmentName, email, phone);
    }
}
