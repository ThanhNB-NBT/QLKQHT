package excel;

import models.bean.Grade;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class GradeExportExcel {

    public static void generateExcelTemplate(String className, String classID, List<Grade> grades, HttpServletResponse response) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Class_" + classID);

            // Thêm tên lớp vào dòng đầu tiên
            Row classNameRow = sheet.createRow(0);
            classNameRow.createCell(0).setCellValue("Tên lớp: " + className);

            // Tạo header ở dòng thứ hai
            Row header = sheet.createRow(1);
            header.createCell(0).setCellValue("Mã sinh viên");
            header.createCell(1).setCellValue("Tên sinh viên");
            header.createCell(2).setCellValue("Điểm chuyên cần");
            header.createCell(3).setCellValue("Điểm giữa kỳ");
            header.createCell(4).setCellValue("Điểm cuối kỳ");

            // Điền dữ liệu bắt đầu từ dòng thứ ba
            int rowIndex = 2;
            for (Grade grade : grades) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(grade.getStudentCode());
                row.createCell(1).setCellValue(grade.getStudentName());
                row.createCell(2).setCellValue(grade.getAttendanceScore() != null ? grade.getAttendanceScore() : 0.0);
                row.createCell(3).setCellValue(grade.getMidtermScore() != null ? grade.getMidtermScore() : 0.0);
                row.createCell(4).setCellValue(grade.getFinalExamScore() != null ? grade.getFinalExamScore() : 0.0);
            }

            // Thiết lập header để tải file về
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=Class_" + classID + "_Grades.xlsx");

            // Ghi file ra output stream
            try (ServletOutputStream out = response.getOutputStream()) {
                workbook.write(out);
            }
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi tạo file Excel: " + e.getMessage(), e);
        }
    }
}
