package common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class AlertManager {
    private static final String SUCCESS_MESSAGES = "successMessages";
    private static final String ERROR_MESSAGES = "errorMessages";

    // Thêm thông báo vào session
    public static void addMessage(HttpServletRequest request, String message, boolean isSuccess) {
        String messageType = isSuccess ? SUCCESS_MESSAGES : ERROR_MESSAGES;
        HttpSession session = request.getSession();

        // Lấy danh sách thông báo hiện tại
        List<String> messages = (List<String>) session.getAttribute(messageType);
        if (messages == null) {
            messages = new ArrayList<>();
        }

        // Thêm thông báo mới vào danh sách
        messages.add(message);
        session.setAttribute(messageType, messages);
    }

    // Lấy danh sách thông báo và xóa chúng khỏi session (sau khi hiển thị)
    public static List<String> getMessages(HttpServletRequest request, boolean isSuccess) {
        String messageType = isSuccess ? SUCCESS_MESSAGES : ERROR_MESSAGES;
        HttpSession session = request.getSession();

        List<String> messages = (List<String>) session.getAttribute(messageType);
        if (messages == null) {
            messages = new ArrayList<>();
        } else {
            session.removeAttribute(messageType); // Xóa sau khi lấy
        }

        return messages;
    }
}
