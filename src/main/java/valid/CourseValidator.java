package valid;

import common.AlertManager;
import input.CourseInput;
import jakarta.servlet.http.HttpServletRequest;
import models.dao.CourseDAO;

public class CourseValidator {

    public static boolean validateInput(CourseInput input, HttpServletRequest request, boolean isUpdate) {
        boolean hasErrors = false;

        if (isUpdate && (input.getCourseID() == null || input.getCourseID() <= 0)) {
            AlertManager.addMessage(request, "ID học phần không hợp lệ.", false);
            hasErrors = true;
        }

        if (!isUpdate) {
            if (input.getCourseName() == null || input.getCourseName().trim().isEmpty()) {
                AlertManager.addMessage(request, "Tên học phần không được để trống.", false);
                hasErrors = true;
            }

            if (input.getDepartmentID() <= 0) {
                AlertManager.addMessage(request, "Khoa không hợp lệ.", false);
                hasErrors = true;
            }

            if (CourseDAO.checkCourseCode(input.getCourseCode())) {
                AlertManager.addMessage(request, "Mã học phần đã tồn tại.", false);
                hasErrors = true;
            }
        }

        if (input.getCredits() <= 0) {
            AlertManager.addMessage(request, "Số tín chỉ phải lớn hơn 0.", false);
            hasErrors = true;
        }

        if (input.getCourseType() == null || input.getCourseType().trim().isEmpty()) {
            AlertManager.addMessage(request, "Loại học phần không được để trống.", false);
            hasErrors = true;
        }

        if (input.getStatus() == null || input.getStatus().trim().isEmpty()) {
            AlertManager.addMessage(request, "Trạng thái không được để trống.", false);
            hasErrors = true;
        }

        return hasErrors;
    }
}
