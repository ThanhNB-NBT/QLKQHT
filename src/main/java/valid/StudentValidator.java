package valid;

import common.AlertManager;
import common.ValidationUtils;
import input.StudentInput;
import jakarta.servlet.http.HttpServletRequest;

public class StudentValidator {

    public static boolean validateInput(StudentInput input, HttpServletRequest request) {
        boolean hasErrors = false;

        if (ValidationUtils.isNullOrEmpty(input.getFirstName())) {
            AlertManager.addMessage(request, "Tên không được để trống.", false);
            hasErrors = true;
        }

        if (ValidationUtils.isNullOrEmpty(input.getLastName())) {
            AlertManager.addMessage(request, "Họ không được để trống.", false);
            hasErrors = true;
        }

        if (input.getDateOfBirth() == null) {
            AlertManager.addMessage(request, "Ngày sinh không được để trống.", false);
            hasErrors = true;
        }

        if (!ValidationUtils.isValidPhoneNumber(input.getPhone())) {
            AlertManager.addMessage(request, "Số điện thoại không hợp lệ (định dạng 0XXXXXXXXX).", false);
            hasErrors = true;
        }

        if (!ValidationUtils.isValidEmail(input.getEmail())) {
            AlertManager.addMessage(request, "Email không hợp lệ.", false);
            hasErrors = true;
        }

        if (ValidationUtils.isNullOrEmpty(input.getAddress())) {
            AlertManager.addMessage(request, "Địa chỉ không được để trống.", false);
            hasErrors = true;
        }

        if (input.getEnrollmentYear() == null) {
            AlertManager.addMessage(request, "Năm nhập học không được để trống.", false);
            hasErrors = true;
        }

        if (ValidationUtils.isNullOrEmpty(input.getMajorName())) {
            AlertManager.addMessage(request, "Tên chuyên ngành không được để trống.", false);
            hasErrors = true;
        }

        if (input.getDepartmentID() == null || input.getDepartmentID() <= 0) {
            AlertManager.addMessage(request, "Khoa không hợp lệ.", false);
            hasErrors = true;
        }

        return hasErrors;
    }
}
