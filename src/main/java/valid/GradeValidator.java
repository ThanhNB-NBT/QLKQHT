package valid;

import common.AlertManager;
import jakarta.servlet.http.HttpServletRequest;

public class GradeValidator {

    public static boolean validateUpdateInput(Integer gradeID, Double attendanceScore, Double midtermScore, Double finalExamScore, HttpServletRequest request) {
        boolean hasErrors = false;

        // Kiểm tra ID hợp lệ
        if (gradeID == null || gradeID <= 0) {
            AlertManager.addMessage(request, "ID không hợp lệ.", false);
            hasErrors = true;
        }

        // Kiểm tra điểm chuyên cần (nếu được nhập)
        if (attendanceScore != null && (attendanceScore < 0 || attendanceScore > 10)) {
            AlertManager.addMessage(request, "Điểm chuyên cần phải nằm trong khoảng từ 0 đến 10.", false);
            hasErrors = true;
        }

        // Kiểm tra điểm giữa kỳ (nếu được nhập)
        if (midtermScore != null && (midtermScore < 0 || midtermScore > 10)) {
            AlertManager.addMessage(request, "Điểm giữa kỳ phải nằm trong khoảng từ 0 đến 10.", false);
            hasErrors = true;
        }

        // Kiểm tra điểm cuối kỳ (nếu được nhập)
        if (finalExamScore != null && (finalExamScore < 0 || finalExamScore > 10)) {
            AlertManager.addMessage(request, "Điểm cuối kỳ phải nằm trong khoảng từ 0 đến 10.", false);
            hasErrors = true;
        }

        return hasErrors;
    }
}
