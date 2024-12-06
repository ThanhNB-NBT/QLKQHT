package valid;

import common.AlertManager;
import common.ValidationUtils;
import input.DepartmentInput;
import jakarta.servlet.http.HttpServletRequest;

public class DepartmentValidator {

    public static boolean validateInput(DepartmentInput input, HttpServletRequest request) {
        boolean hasErrors = false;

        if (ValidationUtils.isNullOrEmpty(input.getDepartmentName())) {
            AlertManager.addMessage(request, "Tên khoa không được để trống.", false);
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

        return hasErrors;
    }
}
