package common;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import input.StudentInput;
import models.dao.DepartmentDAO;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelImportUtils {

	public static List<StudentInput> importStudentsFromExcel(InputStream fileContent) throws Exception {
	    List<StudentInput> inputs = new ArrayList<>();
	    try (Workbook workbook = new XSSFWorkbook(fileContent)) {
			Sheet sheet = workbook.getSheetAt(0);

			// Lấy mapping từ dòng tiêu đề
			Row headerRow = sheet.getRow(0);
			Map<String, Integer> columnMapping = getColumnIndexMapping(headerRow);

			for (Row row : sheet) {
			    if (row.getRowNum() == 0) {
			        // Bỏ qua dòng tiêu đề
			        continue;
			    }

			    String departmentName = getCellValueAsString(row.getCell(columnMapping.get("DepartmentName")));
			    if (departmentName == null || departmentName.isEmpty()) {
			        throw new IllegalArgumentException("Tên khoa không được để trống");
			    }
			    Integer departmentID = DepartmentDAO.getDepartmentIDByName(departmentName);

			    if (departmentID == null) {
			        System.out.println("Khoa chưa tồn tại, thêm mới: " + departmentName);
			        departmentID = DepartmentDAO.createDepartment(departmentName);
			    }


			    // Sử dụng constructor có 10 tham số
			    StudentInput input = new StudentInput(
			        getCellValueAsString(row.getCell(columnMapping.get("FirstName"))),
			        getCellValueAsString(row.getCell(columnMapping.get("LastName"))),
			        parseDate(getCellValueAsString(row.getCell(columnMapping.get("DateOfBirth")))),
			        getCellValueAsString(row.getCell(columnMapping.get("Email"))),
			        getCellValueAsString(row.getCell(columnMapping.get("Phone"))),
			        getCellValueAsString(row.getCell(columnMapping.get("Address"))),
			        parseDate(getCellValueAsString(row.getCell(columnMapping.get("EnrollmentYear")))),
			        getCellValueAsString(row.getCell(columnMapping.get("MajorName"))),
			        "", // StudentCode sẽ được sinh sau
			        departmentID
			    );

			    inputs.add(input);
			}

			workbook.close();
		}
	    return inputs;
	}

	private static Map<String, Integer> getColumnIndexMapping(Row headerRow) {
	    Map<String, Integer> columnMapping = new HashMap<>();

	    for (Cell cell : headerRow) {
	        String columnNameInExcel = cell.getStringCellValue().trim();
	        String columnNameInDB = ColumnMapping.EXCEL_TO_DB_MAPPING.get(columnNameInExcel);
	        if (columnNameInDB != null) {
	            columnMapping.put(columnNameInDB, cell.getColumnIndex());
	        }
	    }

	    return columnMapping;
	}

	private static String getCellValueAsString(Cell cell) {
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			return String.valueOf((int) cell.getNumericCellValue());
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		default:
			return "";
		}
	}

	private static java.sql.Date parseDate(String dateValue) throws ParseException {
		if (dateValue == null || dateValue.isEmpty())
			return null;

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date parsedDate = dateFormat.parse(dateValue);
		return new java.sql.Date(parsedDate.getTime());
	}

}
