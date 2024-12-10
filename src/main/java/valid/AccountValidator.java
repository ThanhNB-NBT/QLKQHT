package valid;

import common.AlertManager;
import common.ValidationUtils;
import input.AccountInput;
import jakarta.servlet.http.HttpServletRequest;

public class AccountValidator {
	public static boolean validateInput(HttpServletRequest request, AccountInput input, boolean isUpdate) {
	    boolean hasErrors = false;

	    // Kiểm tra username
	    if (input.getUsername() == null || input.getUsername().trim().isEmpty()) {
	        AlertManager.addMessage(request, "Tên tài khoản không được để trống.", false);
	        hasErrors = true;
	    }

	    // Kiểm tra email
	    if (input.getEmail() == null || input.getEmail().trim().isEmpty()) {
	        AlertManager.addMessage(request, "Email không được để trống.", false);
	        hasErrors = true;
	    } else if (!ValidationUtils.isValidEmail(input.getEmail())) {
	        AlertManager.addMessage(request, "Định dạng email không hợp lệ.", false);
	        hasErrors = true;
	    }

	    // Kiểm tra mật khẩu khi tạo tài khoản
	    if (input.getPassword() != null && !input.getPassword().equals(input.getConfirmPassword())) {
	        AlertManager.addMessage(request, "Mật khẩu và xác nhận mật khẩu không trùng khớp.", false);
	        hasErrors = true;
	    }

	    return hasErrors;
	}

}
