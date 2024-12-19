package valid;

import common.AlertManager;
import input.StudentClassInput;
import jakarta.servlet.http.HttpServletRequest;
import models.dao.StudentClassDAO;

public class StudentClassValidator {

    public static boolean validateInput(StudentClassInput input, HttpServletRequest request, boolean isUpdate) {
        boolean hasErrors = false;

        if (isUpdate && (input.getStudentClassID() == null || input.getStudentClassID() <= 0)) {
            AlertManager.addMessage(request, "ID không hợp lệ.", false);
            hasErrors = true;
        }

        if(!isUpdate) {

            if (input.getClassID() == null || input.getClassID() <= 0) {
                AlertManager.addMessage(request, "Lớp học không hợp lệ.", false);
                hasErrors = true;
            }

            if (input.getStudentID() == null || input.getStudentID() <= 0) {
                AlertManager.addMessage(request, "Mã học sinh không hợp lệ.", false);
                hasErrors = true;
            }

            if (StudentClassDAO.isDuplicate(input.getClassID(), input.getStudentID())) {
                AlertManager.addMessage(request, "Học sinh này đã được thêm vào lớp học trước đó.", false);
                hasErrors = true;
            }
        }

        if (input.getStatus() == null || input.getStatus().trim().isEmpty()) {
            AlertManager.addMessage(request, "Trạng thái không được để trống.", false);
            hasErrors = true;
        }

        return hasErrors;
    }
}