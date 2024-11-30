<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="edit_course" class="modal" role="dialog">
	<div class="modal-dialog modal-dialog-centered justify-content-center">
		<div class="modal-content modal-lg">
			<div class="modal-header">
				<h4 class="modal-title">Sửa học phần</h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<form id="editCourseForm" action="CourseServlet" method="post"
				class="m-b-30">
				<div class="modal-body">
					<div class="row justify-content-center">
						<!-- ID học phần (hidden) -->
						<input type="hidden" id="editCourseID" name="courseID"
							value="${course.courseID}">

						<!-- Tên khóa học -->
						<div class="col-sm-8">
							<div class="form-group form-focus">
								<input id="editCourseName" name="courseName" type="text"
									class="form-control floating" value="${course.courseName}"
									required pattern=".{3,}"
									title="Tên học phần phải có ít nhất 3 ký tự"> <label
									class="focus-label">Tên học phần <span
									class="text-danger">*</span></label>
							</div>
						</div>

						<!-- Số tín chỉ -->
						<div class="col-sm-8">
							<div class="form-group form-focus">
								<input id="editCredits" name="credits" type="number"
									class="form-control floating" list="credit-options"
									value="${course.credits}" required min="1" max="15">
								<datalist id="credit-options">
									<option value="1" />
									<option value="2" />
									<option value="3" />
									<option value="4" />
									<option value="5" />
								</datalist>
								<label class="focus-label">Số tín chỉ <span
									class="text-danger">*</span></label>
							</div>
						</div>

						<!-- Khoa -->
						<div class="col-sm-8">
							<div class="form-group form-focus">
								<select id="editDepartmentID" class="form-control"
									name="departmentID" required>
									<option value="" disabled>Chọn khoa</option>
									<c:forEach var="department" items="${departments}">
										<option value="${department.departmentID}"
											<c:if test="${department.departmentID == course.departmentID}">selected</c:if>>
											${department.departmentName}</option>
									</c:forEach>
								</select> <label class="focus-label">Khoa <span
									class="text-danger">*</span></label>
							</div>
						</div>
						
						<!-- Loại học phần -->
						<div class="col-sm-8">
							<div class="form-group form-focus">
								<input id="editCourseType" name="courseType" type="text"
									class="form-control floating" list="type-options"
									value="${course.courseType}" required>
								<datalist id="type-options">
									<option value="Bắt buộc" />
									<option value="Tự chọn" />
								</datalist>
								<label class="focus-label">Loại học phần <span
									class="text-danger">*</span></label>
							</div>
						</div>

						<!-- Trạng thái -->
						<div class="col-sm-8">
							<div class="form-group form-focus">
								<input id="editStatus" name="status" type="text"
									class="form-control floating" list="status-options"
									value="${course.status}" required>
								<datalist id="status-options">
									<option value="Hoạt động" />
									<option value="Tạm ngừng" />
								</datalist>
								<label class="focus-label">Trạng thái <span
									class="text-danger">*</span></label>
							</div>
						</div>

						<!-- Submit -->
						<div class="col-sm-8">
							<div class="m-t-20 text-center">
								<button type="submit" class="btn btn-primary btn-lg"
									name="action" value="update">Cập nhật</button>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
