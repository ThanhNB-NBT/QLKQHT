package common;

import java.util.HashMap;
import java.util.Map;

public class ColumnMapping {
    public static final Map<String, String> EXCEL_TO_DB_MAPPING = new HashMap<>();

    static {
        EXCEL_TO_DB_MAPPING.put("Họ", "FirstName");           // Cột "Họ" trong Excel -> Trường "FirstName" trong DB
        EXCEL_TO_DB_MAPPING.put("Tên", "LastName");
        EXCEL_TO_DB_MAPPING.put("Email", "Email");
        EXCEL_TO_DB_MAPPING.put("Số điện thoại", "Phone");
        EXCEL_TO_DB_MAPPING.put("Ngày sinh", "DateOfBirth");
        EXCEL_TO_DB_MAPPING.put("Địa chỉ", "Address");
        EXCEL_TO_DB_MAPPING.put("Ngành", "MajorName");
        EXCEL_TO_DB_MAPPING.put("Ngày nhập học", "EnrollmentYear");
        EXCEL_TO_DB_MAPPING.put("Tên khoa", "DepartmentName");
    }
}
