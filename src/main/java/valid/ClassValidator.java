package valid;

import common.AlertManager;
import input.ClassInput;
import jakarta.servlet.http.HttpServletRequest;
import models.dao.ClassDAO;

public class ClassValidator {

    public static boolean validateInput(ClassInput input, HttpServletRequest request, boolean isUpdate) {
        boolean hasErrors = false;

        // Kiểm tra thông tin cơ bản
        hasErrors |= validateBasicInfo(input, request, isUpdate);

        // Kiểm tra ngày tháng
        hasErrors |= validateDates(input, request, isUpdate);

        // Kiểm tra thông tin bổ sung
        hasErrors |= validateAdditionalInfo(input, request, isUpdate);

        return hasErrors;
    }

    private static boolean validateBasicInfo(ClassInput input, HttpServletRequest request, boolean isUpdate) {
        boolean hasErrors = false;

        if (isUpdate && (input.getClassID() == null || input.getClassID() <= 0)) {
            AlertManager.addMessage(request, "ID lớp học không hợp lệ", false);
            hasErrors = true;
        }

        if (!isUpdate && input.getCourseID() < 0) {
            AlertManager.addMessage(request, "Học phần không được để trống và là số hợp lệ", false);
            hasErrors = true;
        }

        if (input.getTeacherID() < 0) {
            AlertManager.addMessage(request, "Giảng viên không được để trống và là số hợp lệ", false);
            hasErrors = true;
        }

        if (input.getClassTime() == null || input.getClassTime().trim().isEmpty()) {
            AlertManager.addMessage(request, "Thời gian học không được để trống", false);
            hasErrors = true;
        }

        if (input.getRoom() == null || input.getRoom().trim().isEmpty()) {
            AlertManager.addMessage(request, "Phòng học không được để trống", false);
            hasErrors = true;
        }

        if(!isUpdate) {
        	if ( input.getSemester() == null || input.getSemester().trim().isEmpty()) {
                AlertManager.addMessage(request, "Học kỳ không được để trống", false);
                hasErrors = true;
            }
        }
        return hasErrors;
    }

    private static boolean validateDates(ClassInput input, HttpServletRequest request, boolean isUpdate) {
        boolean hasErrors = false;
        if(!isUpdate) {
        	if (input.getStartDate() == null) {
                AlertManager.addMessage(request, "Ngày bắt đầu không được để trống", false);
                hasErrors = true;
            }

            // Kiểm tra ngày kết thúc
            if (input.getEndDate() == null) {
                AlertManager.addMessage(request, "Ngày kết thúc không được để trống", false);
                hasErrors = true;
            }

            // Kiểm tra ngày kết thúc phải sau ngày bắt đầu
            if (input.getStartDate() != null && input.getEndDate() != null && input.getEndDate().before(input.getStartDate())) {
                AlertManager.addMessage(request, "Ngày kết thúc phải sau ngày bắt đầu", false);
                hasErrors = true;
            }

            // Kiểm tra nếu ngày bắt đầu và ngày kết thúc không hợp lệ (trong trường hợp so sánh ngày có thể có vấn đề về định dạng hoặc loại dữ liệu)
            if (input.getStartDate() != null && input.getEndDate() != null && input.getStartDate().after(input.getEndDate())) {
                AlertManager.addMessage(request, "Ngày bắt đầu phải trước ngày kết thúc", false);
                hasErrors = true;
            }
        }
        return hasErrors;
    }

    private static boolean validateAdditionalInfo(ClassInput input, HttpServletRequest request, boolean isUpdate) {
        boolean hasErrors = false;

        if (input.getStatus() == null || input.getStatus().trim().isEmpty()) {
            AlertManager.addMessage(request, "Trạng thái không được để trống", false);
            hasErrors = true;
        }

        if (input.getMaxStudents() <= 0) {
            AlertManager.addMessage(request, "Số sinh viên tối đa phải lớn hơn 0", false);
            hasErrors = true;
        }

        if (input.getTotalLessions() <= 0) {
            AlertManager.addMessage(request, "Tổng số buổi học phải lớn hơn 0", false);
            hasErrors = true;
        }

        if(!isUpdate) {
        	if (input.getClassType() == null || input.getClassType().trim().isEmpty()) {
                AlertManager.addMessage(request, "Loại lớp học không được để trống", false);
                hasErrors = true;
            }

            if (ClassDAO.isDuplicateClassCode(input.getClassName(), input.getSemester())) {
                AlertManager.addMessage(request, "Lớp học đã tồn tại", false);
                hasErrors = true;
            }
        }

        return hasErrors;
    }
}
