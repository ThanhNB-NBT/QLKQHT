package models.bean;

import java.util.Random;

import models.dao.CourseDAO;

public class Course {
	private int courseID;
    private String courseName;
    private int credits;
    private int departmentID;
    private String courseCode;
    private String courseType;
    private String status;
    private Department department;

	public Course() {
		super();
	}

	public Course(int courseID, String courseName, int credits, int departmentID,
			 String courseCode, String courseType, String status, Department department) {
		super();
		this.courseID = courseID;
		this.courseName = courseName;
		this.credits = credits;
		this.departmentID = departmentID;
		this.courseCode = courseCode;
		this.courseType = courseType;
		this.status = status;
		this.department = department;
	}

	public Course( String courseName, int credits, int departmentID,
			 String courseCode, String courseType,String status) {
		super();
		this.courseName = courseName;
		this.credits = credits;
		this.departmentID = departmentID;
		this.courseCode = courseCode;
		this.courseType = courseType;
		this.status = status;
	}

	public Course(int courseID, int credits, String courseType, String status) {
		super();
		this.courseID = courseID;
		this.credits = credits;
		this.courseType = courseType;
		this.status = status;
	}

	public int getCourseID() {
		return courseID;
	}

	public String getCourseName() {
		return courseName;
	}

	public int getCredits() {
		return credits;
	}

	public int getDepartmentID() {
		return departmentID;
	}

	public String getStatus() {
		return status;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public String getCourseType() {
		return courseType;
	}
	public Department getDepartment() {
	    return department;
	}

	public void setCourseID(int courseID) {
		this.courseID = courseID;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public void setDepartmentID(int departmentID) {
		this.departmentID = departmentID;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}

	public void setDepartment(Department department) {
	    this.department = department;
	}

	public String generateCourseCode(int departmentID, Integer excludeCourseID) {
	    // Kiểm tra courseName trước
	    if (this.courseName == null || this.courseName.trim().isEmpty()) {
	        throw new IllegalArgumentException("Tên học phần không được để trống.");
	    }

	    // Tạo 3 ký tự đầu tiên từ tên khóa học
	    String[] words = this.courseName.split(" ");
	    StringBuilder initials = new StringBuilder();

	    for (String word : words) {
	        if (!word.isEmpty()) {
	            initials.append(Character.toUpperCase(word.charAt(0)));
	        }
	    }

	    // Giới hạn tối đa 3 ký tự
	    if (initials.length() > 4) {
	        initials.setLength(4);
	    }

	    // Thêm departmentID và số ngẫu nhiên để đủ 8 ký tự
	    Random random = new Random();
	    String courseCode;
	    int maxAttempts = 5; // Giới hạn số lần thử
	    int attempts = 0;

	    do {
	        if (attempts >= maxAttempts) {
	            throw new RuntimeException("Không thể tạo mã khóa học duy nhất sau nhiều lần thử.");
	        }

	        StringBuilder codeBuilder = new StringBuilder(initials);

	        codeBuilder.append(String.format("%02d", departmentID % 100));

	        // Thêm số ngẫu nhiên để đạt 10 ký tự
	        while (codeBuilder.length() < 10) {
	            codeBuilder.append(random.nextInt(10));
	        }

	        courseCode = codeBuilder.toString();
	        attempts++;
	    } while (excludeCourseID == null
	             ? CourseDAO.checkCourseCode(courseCode)
	             : CourseDAO.checkCourseCode(courseCode, excludeCourseID));

	    return courseCode;
	}
}
