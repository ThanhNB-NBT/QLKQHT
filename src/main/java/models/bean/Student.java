package models.bean;

import java.util.Date;
import java.util.Random;
import java.util.Calendar;

import models.dao.StudentDAO;

public class Student {
    private int studentID;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String email;
    private String phone;
    private String address;
    private Date enrollmentYear;
    private String majorName;
    private int accountID;
    private String studentCode;
    private Integer departmentID;
    private Department department;
    private Account account;

    public Student() {
        super();
    }

    public Student(int studentID, String firstName, String lastName, Date dateOfBirth, String email, String phone,
			String address, Date enrollmentYear, String majorName, int accountID, String studentCode,
			Integer departmentID, Department department, Account account) {
		super();
		this.studentID = studentID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.enrollmentYear = enrollmentYear;
		this.majorName = majorName;
		this.accountID = accountID;
		this.studentCode = studentCode;
		this.departmentID = departmentID;
		this.department = department;
		this.account = account;
	}

    public Student(String firstName, String lastName, Date dateOfBirth, String email, String phone,
                   String address, Date enrollmentYear, String majorName, String studentCode, Integer departmentID) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.enrollmentYear = enrollmentYear;
        this.majorName = majorName;
        this.studentCode = studentCode;
        this.departmentID = departmentID;
    }

    // Constructor without studentCode
    public Student(int studentID, String firstName, String lastName, Date dateOfBirth, String email, String phone,
                   String address, Date enrollmentYear, String majorName, Integer departmentID) {
        super();
		this.studentID = studentID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.enrollmentYear = enrollmentYear;
        this.majorName = majorName;
        this.departmentID = departmentID;
    }

    public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Integer getDepartmentID() {
		return departmentID;
	}

	public void setDepartmentID(Integer departmentID) {
		this.departmentID = departmentID;
	}
	// Getters and setters
    public int getStudentID() {
        return studentID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public Date getEnrollmentYear() {
        return enrollmentYear;
    }

    public String getMajorName() {
        return majorName;
    }

    public int getAccountID() {
        return accountID;
    }

    public String getStudentCode() {
        return studentCode;
    }

    // Setters
    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEnrollmentYear(Date enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    // Phương thức sinh mã sinh viên
    private String generateCode(String majorName, int enrollmentYear, int birthYear) {
        StringBuilder initials = new StringBuilder();
        String[] words = majorName.split(" ");
        for (String word : words) {
            if (!word.isEmpty()) {
                initials.append(Character.toUpperCase(word.charAt(0)));
            }
        }
        if (initials.length() > 4) initials.setLength(4);

        String enrollmentYearShort = String.format("%02d", enrollmentYear % 100);
        String birthYearShort = String.format("%02d", birthYear % 100);
        StringBuilder code = new StringBuilder(initials);
        code.append(enrollmentYearShort).append(birthYearShort);

        Random random = new Random();
        while (code.length() < 12) {
            code.append(random.nextInt(10));
        }

        return code.toString();
    }

    // Phương thức tạo mã sinh viên (sửa lại để nhận Date)
    public String generateStudentCode(String majorName, Date enrollmentYear, Date birthDate, Integer excludeStudentID) {
        int attempts = 0;
        final int maxAttempts = 5;
        String code;

        Calendar calBirth = Calendar.getInstance();
        calBirth.setTime(birthDate);
        int birthYear = calBirth.get(Calendar.YEAR);

        Calendar calEnrollment = Calendar.getInstance();
        calEnrollment.setTime(enrollmentYear);
        int enrollmentYearInt = calEnrollment.get(Calendar.YEAR);

        do {
            if (attempts >= maxAttempts) {
                throw new RuntimeException("Không thể tạo mã sinh viên duy nhất sau nhiều lần thử.");
            }
            // Gọi generateCode với các giá trị năm
            code = generateCode(majorName, enrollmentYearInt, birthYear);
            attempts++;

        } while (!isUniqueStudentCode(code, excludeStudentID));

        return code;
    }

    private boolean isUniqueStudentCode(String code, Integer excludeStudentID) {
        boolean exists = excludeStudentID == null
                ? StudentDAO.checkStudentCode(code)
                : StudentDAO.checkStudentCode(code, excludeStudentID);

        return !exists; // Đảo ngược: true nếu mã không tồn tại
    }
}