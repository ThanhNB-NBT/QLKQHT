package common;

import java.util.Map;

public class ColumnMapping {
    // Ánh xạ giữa tên cột trong file Excel và trường trong cơ sở dữ liệu cho `Student`
    public static final Map<String, String> STUDENT_EXCEL_TO_DB_MAPPING = Map.of(
        "Họ", "FirstName",                  // Cột "Họ" -> Trường "FirstName"
        "Tên", "LastName",                  // Cột "Tên" -> Trường "LastName"
        "Email", "Email",                   // Cột "Email" giữ nguyên
        "Số điện thoại", "Phone",           // Cột "Số điện thoại" -> Trường "Phone"
        "Ngày sinh", "DateOfBirth",         // Cột "Ngày sinh" -> Trường "DateOfBirth"
        "Địa chỉ", "Address",               // Cột "Địa chỉ" -> Trường "Address"
        "Ngành", "MajorName",               // Cột "Ngành" -> Trường "MajorName"
        "Ngày nhập học", "EnrollmentYear",  // Cột "Ngày nhập học" -> Trường "EnrollmentYear"
        "Tên khoa", "DepartmentName"        // Cột "Tên khoa" -> Trường "DepartmentName"
    );

    // Ánh xạ giữa tên cột trong file Excel và trường trong cơ sở dữ liệu cho `Grade`
    public static final Map<String, String> GRADE_EXCEL_TO_DB_MAPPING = Map.of(
        "Mã sinh viên", "StudentCode",       // Cột "Mã sinh viên" -> Trường "StudentCode"
        "Tên sinh viên", "StudentName",      // Cột "Tên sinh viên" -> Trường "StudentName"
        "Điểm chuyên cần", "AttendanceScore",// Cột "Điểm chuyên cần" -> Trường "AttendanceScore"
        "Điểm giữa kỳ", "MidtermScore",      // Cột "Điểm giữa kỳ" -> Trường "MidtermScore"
        "Điểm cuối kỳ", "FinalExamScore"     // Cột "Điểm cuối kỳ" -> Trường "FinalExamScore"
    );
}
