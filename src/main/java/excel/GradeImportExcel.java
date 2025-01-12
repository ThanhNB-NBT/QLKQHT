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

            System.out.println("Bắt đầu đọc file Excel...");

            // Bỏ qua 2 dòng đầu (tiêu đề và tên cột)
            int startRow = 2;
            int lastRow = sheet.getLastRowNum();
            System.out.println("Tổng số dòng cần xử lý: " + (lastRow - startRow + 1));

            for (int rowNum = startRow; rowNum <= lastRow; rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (isValidRow(row)) {
                    try {
                        Grade grade = processRow(row, classID, rowNum);
                        if (grade != null) {
                            grades.add(grade);
                            System.out.println("Đã xử lý thành công dòng " + (rowNum + 1));
                        }
                    } catch (Exception e) {
                        System.err.println("Lỗi xử lý dòng " + (rowNum + 1) + ": " + e.getMessage());
                        throw new Exception("Lỗi ở dòng " + (rowNum + 1) + ": " + e.getMessage());
                    }
                } else {
                    System.out.println("Dòng " + (rowNum + 1) + " không hợp lệ, bỏ qua.");
                }
            }

            System.out.println("Đã xử lý xong file Excel. Tổng số điểm đọc được: " + grades.size());

        } catch (Exception e) {
            System.err.println("Lỗi xử lý file Excel: " + e.getMessage());
            throw e;
        }
        return grades;
    }

    private static boolean isValidRow(Row row) {
        if (row == null || isEmptyRow(row)) {
            return false;
        }
        String studentCode = getCellValueAsString(row.getCell(0));
        return studentCode != null && !studentCode.trim().isEmpty();
    }

    private static Grade processRow(Row row, int classID, int rowNum) throws Exception {
        String studentCode = getCellValueAsString(row.getCell(0));
        System.out.println("Đang xử lý sinh viên: " + studentCode);

        // Lấy điểm từ các cột
        Double attendanceScore = validateScore(getCellValueAsDouble(row.getCell(2)));
        Double midtermScore = validateScore(getCellValueAsDouble(row.getCell(3)));
        Double finalExamScore = validateScore(getCellValueAsDouble(row.getCell(4)));

        // Lấy studentID từ mã sinh viên
        Integer studentID = StudentDAO.getStudentIDByCode(studentCode);
        if (studentID == null) {
            throw new IllegalArgumentException("Không tìm thấy sinh viên với mã: " + studentCode);
        }

        // Lấy studentClassID
        String studentClassID = StudentClassDAO.getStudentClassID(studentID, classID);
        if (studentClassID == null) {
            throw new IllegalArgumentException("Sinh viên " + studentCode + " không thuộc lớp này");
        }

        // Tạo đối tượng Grade và trả về
        Grade grade = new Grade();
        grade.setStudentClassID(studentClassID);
        grade.setAttendanceScore(attendanceScore);
        grade.setMidtermScore(midtermScore);
        grade.setFinalExamScore(finalExamScore);

        return grade;
    }

    private static boolean isEmptyRow(Row row) {
        if (row == null) return true;
        for (int cellNum = 0; cellNum < 5; cellNum++) {
            Cell cell = row.getCell(cellNum);
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
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getLocalDateTimeCellValue().toString();
                    }
                    return String.valueOf((long) cell.getNumericCellValue());
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    try {
                        return String.valueOf(cell.getNumericCellValue());
                    } catch (IllegalStateException e) {
                        return cell.getStringCellValue();
                    }
                default:
                    return "";
            }
        } catch (Exception e) {
            System.err.println("Lỗi đọc giá trị ô: " + e.getMessage());
            return "";
        }
    }

    private static Double getCellValueAsDouble(Cell cell) {
        if (cell == null) return null;
        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return cell.getNumericCellValue();
                case STRING:
                    String value = cell.getStringCellValue().trim();
                    return value.isEmpty() ? null : Double.parseDouble(value);
                case FORMULA:
                    try {
                        return cell.getNumericCellValue();
                    } catch (Exception e) {
                        String formulaValue = cell.getStringCellValue().trim();
                        return formulaValue.isEmpty() ? null : Double.parseDouble(formulaValue);
                    }
                default:
                    return null;
            }
        } catch (Exception e) {
            System.err.println("Lỗi chuyển đổi điểm: " + e.getMessage());
            throw new IllegalArgumentException("Giá trị điểm không hợp lệ");
        }
    }
}