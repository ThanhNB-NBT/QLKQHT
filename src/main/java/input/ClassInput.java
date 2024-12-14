package input;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.Date;

public class ClassInput {
	private Integer classID;
	private int courseID;
	private int teacherID;
	private String classTime;
	private String room;
	private String semester;
	private String className;
	private String status;
	private int maxStudents;
	private int totalLessions;
	private Date startDate;
	private Date endDate;
	private String classType;
	private Integer parentClassID;

	// Constructor cho Create
	public ClassInput(int courseID, int teacherID, String classTime, String room, String semester, String className,
			String status, int maxStudents, int totalLessions, Date startDate, Date endDate, String classType,
			Integer parentClassID) {
		this(null, courseID, teacherID, classTime, room, semester, className, status, maxStudents, totalLessions,
				startDate, endDate, classType, parentClassID);
	}

	// Constructor cho Update
	public ClassInput(Integer classID, int courseID, int teacherID, String classTime, String room, String semester,
			String className, String status, int maxStudents, int totalLessions, Date startDate, Date endDate,
			String classType, Integer parentClassID) {
		this.classID = classID;
		this.courseID = courseID;
		this.teacherID = teacherID;
		this.classTime = classTime;
		this.room = room;
		this.semester = semester;
		this.className = className;
		this.status = status;
		this.maxStudents = maxStudents;
		this.totalLessions = totalLessions;
		this.startDate = startDate;
		this.endDate = endDate;
		this.classType = classType;
		this.parentClassID = parentClassID;
	}

	// Tạo đối tượng từ request
	public static ClassInput fromRequest(HttpServletRequest request, boolean isUpdate) {
		String classIDStr = request.getParameter("classID");
		Integer classID = isUpdate && classIDStr != null && !classIDStr.isEmpty() ? Integer.parseInt(classIDStr) : null;

		int courseID = Integer.parseInt(request.getParameter("courseID"));
		int teacherID = Integer.parseInt(request.getParameter("teacherID"));
		String classTime = request.getParameter("classTime");
		String room = request.getParameter("room");
		String semester = request.getParameter("semester");
		String className = request.getParameter("className");
		String status = request.getParameter("status");
		int maxStudents = Integer.parseInt(request.getParameter("maxStudents"));
		int totalLessions = Integer.parseInt(request.getParameter("totalLessions"));
		Date startDate = Date.valueOf(request.getParameter("startDate"));
		Date endDate = Date.valueOf(request.getParameter("endDate"));
		String classType = request.getParameter("classType");
		String parentClassIDStr = request.getParameter("parentClassID");
		Integer parentClassID = null;
		if (parentClassIDStr != null && !parentClassIDStr.trim().isEmpty()) {
			parentClassID = Integer.parseInt(parentClassIDStr);
		}

		return new ClassInput(classID, courseID, teacherID, classTime, room, semester, className, status, maxStudents,
				totalLessions, startDate, endDate, classType, parentClassID);
	}

	public ClassInput(Integer classID, int teacherID, String classTime, String room, String status, int maxStudents,
			int totalLessions) {
		this.classID = classID;

		this.teacherID = teacherID;
		this.classTime = classTime;
		this.room = room;
		this.status = status;
		this.maxStudents = maxStudents;
		this.totalLessions = totalLessions;
	}

	public static ClassInput forUpdate(HttpServletRequest request) {
		Integer classID = Integer.parseInt(request.getParameter("classID"));
		int teacherID = Integer.parseInt(request.getParameter("teacherID"));
		String classTime = request.getParameter("classTime");
		String room = request.getParameter("room");
		String status = request.getParameter("status");
		int maxStudents = Integer.parseInt(request.getParameter("maxStudents"));
		int totalLessions = Integer.parseInt(request.getParameter("totalLessions"));

		return new ClassInput(classID, teacherID, classTime, room, status, maxStudents, totalLessions);

	}

	public String generateClassName(int existingCount, String courseName, String parentClassName) {
		String suffix;

		// Xác định hậu tố dựa trên loại lớp học
		switch (this.classType) {
		case "Lý thuyết":
			suffix = "_LT";
			break;
		case "Thực hành":
			suffix = "_TH";
			break;
		default:
			throw new IllegalArgumentException("Loại lớp không hợp lệ: " + this.classType);
		}

		if (this.parentClassID != null && this.parentClassID > 0) {
			// Lớp thực hành phụ thuộc lớp lý thuyết
			if (parentClassName != null && !parentClassName.isEmpty()) {
				return String.format("%s%s%02d", parentClassName, suffix, existingCount + 1);
			} else {
				throw new IllegalArgumentException("Không tìm thấy ClassName của ParentClass để tạo lớp phụ thuộc.");
			}
		} else if (courseName != null && !courseName.isEmpty()) {
			// Lớp chính (lý thuyết hoặc thực hành độc lập)
			return String.format("%s%s%02d", courseName, suffix, existingCount + 1);
		} else {
			throw new IllegalArgumentException(
					"Không đủ thông tin để tạo tên lớp: cần có courseName hoặc parentClassName.");
		}
	}

	// Getter methods
	public Integer getClassID() {
		return classID;
	}

	public int getCourseID() {
		return courseID;
	}

	public int getTeacherID() {
		return teacherID;
	}

	public String getClassTime() {
		return classTime;
	}

	public String getRoom() {
		return room;
	}

	public String getSemester() {
		return semester;
	}

	public String getClassName() {
		return className;
	}

	public String getStatus() {
		return status;
	}

	public int getMaxStudents() {
		return maxStudents;
	}

	public int getTotalLessions() {
		return totalLessions;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getClassType() {
		return classType;
	}

	public Integer getParentClassID() {
		return parentClassID;
	}
}
