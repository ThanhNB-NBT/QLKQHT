package Valid;

import common.AlertManager;
import input.TeacherInput;
import jakarta.servlet.http.HttpServletRequest;

public class TeacherValidator {
    public static boolean validateInput(TeacherInput input, HttpServletRequest request) {
        boolean hasErrors = false;

        if (input.getFirstName() == null || input.getFirstName().trim().isEmpty()) {
            AlertManager.addMessage(request, "Tên không được để trống.", false);
            hasErrors = true;
        }
        if (input.getLastName() == null || input.getLastName().trim().isEmpty()) {
            AlertManager.addMessage(request, "Họ không được để trống.", false);
            hasErrors = true;
        }
        if (input.getEmail() == null || !isValidEmail(input.getEmail())) {
            AlertManager.addMessage(request, "Email không hợp lệ.", false);
            hasErrors = true;
        }
        if (input.getPhone() == null || input.getPhone().trim().isEmpty()) {
            AlertManager.addMessage(request, "Số điện thoại không được để trống.", false);
            hasErrors = true;
        }
        if (input.getDepartmentID() <= 0) {
            AlertManager.addMessage(request, "Phòng ban không hợp lệ.", false);
            hasErrors = true;
        }
        if (input.getOffice() == null || input.getOffice().trim().isEmpty()) {
            AlertManager.addMessage(request, "Văn phòng không được để trống.", false);
            hasErrors = true;
        }
        if (input.getHireDate() == null) {
            AlertManager.addMessage(request, "Ngày không được để trống.", false);
            hasErrors = true;
        }

        return hasErrors;
    }

    private static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return email.matches(regex);
    }
}

