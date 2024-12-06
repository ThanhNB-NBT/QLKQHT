package common;

public class ValidationUtils {

    // Kiểm tra chuỗi null hoặc rỗng
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    // Kiểm tra số điện thoại hợp lệ
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return !isNullOrEmpty(phoneNumber) && phoneNumber.matches("^0\\d{9}$"); // Định dạng 0XXXXXXXXX
    }

    // Kiểm tra email hợp lệ
    public static boolean isValidEmail(String email) {
        return !isNullOrEmpty(email) && email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    }
}
