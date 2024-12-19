package common;

import jakarta.servlet.http.HttpSession;
import models.bean.Account;
import models.bean.Role;

public class RoleUtils {

    public static final int ADMIN_ROLE = 1;
    public static final int TEACHER_ROLE = 2;
    public static final int STUDENT_ROLE = 3;

    public static boolean isAdmin(HttpSession session) {
        return checkRole(session, ADMIN_ROLE);
    }

    public static boolean isTeacher(HttpSession session) {
        return checkRole(session, TEACHER_ROLE);
    }

    public static boolean isStudent(HttpSession session) {
        return checkRole(session, STUDENT_ROLE);
    }

    private static boolean checkRole(HttpSession session, int roleID) {
        Account loggedInUser = (Account) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            Role role = loggedInUser.getRole();
            return role != null && role.getRoleID() == roleID;
        }
        return false;
    }
}
