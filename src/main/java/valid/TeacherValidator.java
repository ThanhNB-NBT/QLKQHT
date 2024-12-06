package valid;

import common.AlertManager;
import common.ValidationUtils;
import input.TeacherInput;
import jakarta.servlet.http.HttpServletRequest;

public class TeacherValidator {

    public static boolean validateInput(TeacherInput input, HttpServletRequest request) {
        boolean hasErrors = false;

        if (ValidationUtils.isNullOrEmpty(input.getFirstName())) {
            AlertManager.addMessage(request, "Họ không được để trống.", false);
            hasErrors = true;
        }

        if (ValidationUtils.isNullOrEmpty(input.getLastName())) {
            AlertManager.addMessage(request, "Tên không được để trống.", false);
            hasErrors = true;
        }

        if (!ValidationUtils.isValidEmail(input.getEmail())) {
            AlertManager.addMessage(request, "Email không hợp lệ.", false);
            hasErrors = true;
        }

        if (!ValidationUtils.isValidPhoneNumber(input.getPhone())) {
            AlertManager.addMessage(request, "Số điện thoại không hợp lệ (định dạng 0XXXXXXXXX).", false);
            hasErrors = true;
        }

        if (ValidationUtils.isNullOrEmpty(input.getOffice())) {
            AlertManager.addMessage(request, "Phòng làm việc không được để trống.", false);
            hasErrors = true;
        }

        if (input.getHireDate() == null) {
            AlertManager.addMessage(request, "Ngày làm việc không được để trống.", false);
            hasErrors = true;
        }

        if (input.getDepartmentID() == null || input.getDepartmentID() <= 0) {
            AlertManager.addMessage(request, "Khoa không hợp lệ.", false);
            hasErrors = true;
        }

        return hasErrors;
    }
}
