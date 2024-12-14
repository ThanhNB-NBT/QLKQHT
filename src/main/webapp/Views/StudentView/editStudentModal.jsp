<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="edit_student" class="modal" role="dialog">
	<div class="modal-dialog modal-dialog-centered justify-content-center">
		<div class="modal-content modal-lg">
			<div class="modal-header">
				<h4 class="modal-title">Chỉnh sửa thông tin sinh viên</h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<div class="modal-body">
				<form id="editStudentForm" action="StudentServlet" method="post"
					enctype="multipart/form-data" class="m-b-30">
					<div class="row justify-content-center">
						<!-- ID Sinh viên (ẩn) -->
						<input type="hidden" id="editStudentId" name="studentID" />
						<input type="hidden" id="editAccountID" name="accountID" />
						<input type="hidden" id="editEnrollmentYear" name="enrollmentYear" />
						<div class="col-sm-12">
							<div class="form-group">
								<label>MSSV:</label> <span id="editStudentCode"
									class="font-weight-bold"></span>
							</div>
						</div>

						<div class="col-sm-12">
							<div class="form-group">
								<label>Năm nhập học:</label> <span id="editEnrollmentYearShow"
									class="font-weight-bold"></span>
							</div>
						</div>

						<div class="col-sm-6">
							<div class="form-group form-focus">
								<input id="editFirstName" name="firstName" type="text"
									class="form-control floating" required> <label
									class="focus-label">Họ <span class="text-danger">*</span></label>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group form-focus">
								<input id="editLastName" name="lastName" type="text"
									class="form-control floating" required> <label
									class="focus-label">Tên <span class="text-danger">*</span></label>
							</div>
						</div>

						<div class="col-sm-12">
							<div class="form-group form-focus">
								<input id="editDateOfBirth" name="dateOfBirth" type="date"
									class="form-control floating" required> <label
									class="focus-label">Ngày sinh <span class="text-danger">*</span></label>
								<span id="EditDobError" class="text-danger"
									style="display: none;">Tuổi phải ít nhất 17</span>
							</div>
						</div>

						<div class="col-sm-12">
							<div class="form-group form-focus">
								<input id="editEmail" name="email" type="email"
									class="form-control floating" required> <label
									class="focus-label">Email <span class="text-danger">*</span></label>
								<span id="EditEmailError" class="text-danger"
									style="display: none;">Email không hợp lệ</span>
							</div>
						</div>

						<div class="col-sm-12">
							<div class="form-group form-focus">
								<input id="editPhone" name="phone" type="text"
									class="form-control floating" pattern="0[0-9]{9}" required>
								<label class="focus-label">Số điện thoại <span
									class="text-danger">*</span></label> <span id="EditPhoneError"
									class="text-danger" style="display: none;">Số điện thoại
									không hợp lệ</span>
							</div>
						</div>

						<div class="col-sm-12">
							<div class="form-group form-focus">
								<input id="editAddress" name="address" type="text"
									class="form-control floating"> <label
									class="focus-label">Địa chỉ</label>
							</div>
						</div>

						<div class="col-sm-12">
							<div class="form-group form-focus">
								<input id="editMajorName" name="majorName" type="text"
									class="form-control floating"> <label
									class="focus-label">Ngành học</label>
							</div>
						</div>

						<div class="col-sm-12">
							<div class="form-group form-focus">
								<select id="editDepartmentID" class="form-control"
									name="departmentID" required>
									<option value="" disabled>Chọn khoa</option>
									<c:forEach var="department" items="${departments}">
										<option value="${department.departmentID}">
											${department.departmentName}
										</option>
									</c:forEach>
								</select> <label class="focus-label">Khoa<span
									class="text-danger">*</span></label>
							</div>
						</div>


						<input type="hidden" name="currentAvatar"
							value="${pageContext.request.contextPath}/${student.account.avatar}">
						<div class="col-sm-12">
							<div class="form-group">
								<label for="avatarInputEdit">Ảnh đại diện</label> <input
									type="file" id="avatarInputEdit"
									class="form-control avatar-input" data-target-img="editAvatar"
									accept="image/*" name="avatar"> <img id="editAvatar"
									src="${pageContext.request.contextPath}/${student.account.avatar}"
									alt="Ảnh đại diện" class="img-thumbnail mt-2"
									style="max-width: 150px;">
							</div>

						</div>

						<div class="col-sm-12">
							<div class="m-t-20 text-center">
								<button type="submit" class="btn btn-primary btn-lg"
									name="action" value="update">Cập nhật</button>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
