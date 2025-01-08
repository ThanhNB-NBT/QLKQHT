package excel;

import models.bean.Grade;
import models.dao.StudentDAO;
import models.dao.StudentClassDAO;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GradeImportExcel {
    public static List<Grade> importGradesFromExcel(InputStream fileContent, int classID) throws Exception {
        List<Grade> grades = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(fileContent)) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new IllegalArgumentException("File Excel không có dữ liệu");
            }

            boolean isHeaderSkipped = false;
            for (Row row : sheet) {
                // Kiểm tra và bỏ qua các dòng không cần thiết
                if (!isHeaderSkipped || row.getRowNum() == 0 || row.getRowNum() == 1 || isEmptyRow(row)) {
                    isHeaderSkipped = true;
                    continue;
                }

                try {
                    // Xử lý dữ liệu
                    String studentCode = getCellValueAsString(row.getCell(0));
                    if (studentCode.isEmpty()) {
                        throw new IllegalArgumentException("Mã sinh viên không được để trống");
                    }

                    Double attendanceScore = validateScore(getCellValueAsDouble(row.getCell(2)));
                    Double midtermScore = validateScore(getCellValueAsDouble(row.getCell(3)));
                    Double finalExamScore = validateScore(getCellValueAsDouble(row.getCell(4)));

                    Integer studentID = StudentDAO.getStudentIDByCode(studentCode);
                    if (studentID == null) {
                        throw new IllegalArgumentException("Mã sinh viên không tồn tại: " + studentCode);
                    }

                    String studentClassID = StudentClassDAO.getStudentClassID(studentID, classID);
                    if (studentClassID == null) {
                        throw new IllegalArgumentException("Sinh viên không thuộc lớp này: " + studentCode);
                    }

                    Grade grade = new Grade();
                    grade.setStudentClassID(studentClassID);
                    grade.setAttendanceScore(attendanceScore);
                    grade.setMidtermScore(midtermScore);
                    grade.setFinalExamScore(finalExamScore);
                    grades.add(grade);

                } catch (Exception e) {
                    System.err.println("Lỗi ở dòng " + (row.getRowNum() + 1) + ": " + e.getMessage());
                }
            }


        }
        return grades;
    }

    private static boolean isEmptyRow(Row row) {
        if (row == null) return true;
        for (int i = 0; i < 5; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && !getCellValueAsString(cell).trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private static Double validateScore(Double score) {
        if (score == null) return null;
        if (score < 0 || score > 10) {
            throw new IllegalArgumentException("Điểm phải nằm trong khoảng 0-10");
        }
        return score;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        try {
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue().trim();
                case NUMERIC:
                    return String.valueOf((long) cell.getNumericCellValue());
                default:
                    return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    private static Double getCellValueAsDouble(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                String value = cell.getStringCellValue().trim();
                return value.isEmpty() ? null : Double.parseDouble(value);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Giá trị điểm không hợp lệ");
        }
        return null;
    }
}
