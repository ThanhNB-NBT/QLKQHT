package common;

import jakarta.servlet.http.HttpSession;
import models.bean.Account;

public class SessionUtils {
	// Phương thức kiểm tra xem người dùng đã đăng nhập hay chưa
    public static boolean isLoggedIn(HttpSession session) {
    	if (session != null && session.getAttribute("loggedInUser") != null) {
            return true; // Đã đăng nhập
        }
        return false; // Chưa đăng nhập
    }

    // Phương thức lấy đối tượng Account của người dùng đã đăng nhập từ session
    public static Account getLoggedInAccount(HttpSession session) {
        if (isLoggedIn(session)) {
            return (Account) session.getAttribute("loggedInUser");
        }
        return null;
    }

    // Phương thức đăng xuất người dùng
    public static void logout(HttpSession session) {
        if (session != null) {
            session.invalidate();  // Hủy session
        }
    }
}
