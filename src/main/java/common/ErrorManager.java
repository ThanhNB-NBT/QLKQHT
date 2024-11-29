package common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class ErrorManager {
    private static final String SUCCESS_MESSAGES = "successMessages";
    private static final String ERROR_MESSAGES = "errorMessages";

    // Thêm thông báo vào session
    public static void addMessage(HttpServletRequest request, String message, boolean isSuccess) {
        String messageType = isSuccess ? SUCCESS_MESSAGES : ERROR_MESSAGES;
        HttpSession session = request.getSession();

        // Lấy thông báo hiện tại nếu có, nối thêm thông báo mới
        String existingMessages = (String) session.getAttribute(messageType);
        session.setAttribute(messageType, 
            (existingMessages == null ? "" : existingMessages + "\n") + message);
    }
}
