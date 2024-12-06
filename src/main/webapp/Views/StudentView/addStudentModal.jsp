<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="add_student" class="modal" role="dialog">
	<div class="modal-dialog modal-dialog-centered justify-content-center">
		<div class="modal-content modal-lg">
			<div class="modal-header">
				<h4 class="modal-title">Thêm sinh viên</h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<div class="modal-body">
				<form id="addStudentForm" action="StudentServlet" method="post"
					enctype="multipart/form-data" class="m-b-30">
					<div class="row justify-content-center">

						<!-- Họ và Tên -->
						<div class="col-sm-6">
							<div class="form-group form-focus">
								<input id="firstName" name="firstName" type="text"
									class="form-control floating" required> <label
									class="focus-label">Họ <span class="text-danger">*</span></label>
								<div class="text-danger small" id="firstNameError"
									style="display: none;">Họ không được để trống</div>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group form-focus">
								<input id="lastName" name="lastName" type="text"
									class="form-control floating" required> <label
									class="focus-label">Tên <span class="text-danger">*</span></label>
								<div class="text-danger small" id="lastNameError"
									style="display: none;">Tên không được để trống</div>
							</div>
						</div>

						<!-- Ngày sinh -->
						<div class="col-sm-12">
							<div class="form-group form-focus">
								<input id="dateOfBirth" name="dateOfBirth" type="date"
									class="form-control floating" required> <label
									class="focus-label">Ngày sinh <span class="text-danger">*</span></label>
								<div class="text-danger small" id="dobError"
									style="display: none;">Bạn phải ít nhất 17 tuổi</div>
							</div>
						</div>

						<!-- Email -->
						<div class="col-sm-12">
							<div class="form-group form-focus">
								<input id="email" name="email" type="email"
									class="form-control floating" required> <label
									class="focus-label">Email <span class="text-danger">*</span></label>
								<div class="text-danger small" id="emailError"
									style="display: none;">Email không hợp lệ</div>
							</div>
						</div>

						<!-- Số điện thoại -->
						<div class="col-sm-12">
							<div class="form-group form-focus">
								<input id="phone" name="phone" type="text"
									class="form-control floating" pattern="0[0-9]{9}"
									title="Số điện thoại phải từ 10 chữ số" required> <label
									class="focus-label">Số điện thoại <span
									class="text-danger">*</span></label>
								<div class="text-danger small" id="phoneError"
									style="display: none;">Số điện thoại không hợp lệ</div>
							</div>
						</div>

						<!-- Địa chỉ -->
						<div class="col-sm-12">
							<div class="form-group form-focus">
								<input id="address" name="address" type="text"
									class="form-control floating"> <label
									class="focus-label">Địa chỉ</label>
							</div>
						</div>

						<!-- Ngành học -->
						<div class="col-sm-12">
							<div class="form-group form-focus">
								<input id="majorName" name="majorName" type="text"
									class="form-control floating"> <label
									class="focus-label">Ngành học</label>
							</div>
						</div>

						<!-- Năm nhập học -->
						<div class="col-sm-12">
							<div class="form-group form-focus">
								<input id="enrollmentYear" name="enrollmentYear" type="date"
									class="form-control floating" required> <label
									class="focus-label">Năm nhập học <span
									class="text-danger">*</span></label>
								<div class="text-danger small" id="enrollmentYearError"
									style="display: none;">Năm nhập học không hợp lệ</div>
							</div>
						</div>

						<!-- Khoa -->
						<div class="col-sm-12">
							<div class="form-group form-focus">
								<select class="form-control" name="departmentID" required>
									<option value="" disabled selected>Chọn khoa</option>
									<c:forEach var="department" items="${departments}">
										<option value="${department.departmentID}">${department.departmentName}</option>
									</c:forEach>
								</select> <label class="focus-label">Khoa <span
									class="text-danger">*</span></label>
								<div class="text-danger small" id="departmentError"
									style="display: none;">Vui lòng chọn khoa</div>
							</div>
						</div>

						<!-- Avatar -->
						<div class="col-sm-12">
							<div class="form-group">
								<label for="avatarInputAdd">Ảnh đại diện</label> <input
									type="file" id="avatarInputAdd"
									class="form-control avatar-input" data-target-img="addAvatar"
									accept="image/*" name="avatar"> <img id="addAvatar"
									src="assets/img/default-avatar.png" alt="Ảnh đại diện"
									class="img-thumbnail mt-2" name="avatar" style="max-width: 150px;">
							</div>

						</div>

						<!-- Submit -->
						<div class="col-sm-12">
							<div class="m-t-20 text-center">
								<button type="submit" class="btn btn-primary btn-lg"
									name="action" value="create">Thêm sinh viên</button>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
