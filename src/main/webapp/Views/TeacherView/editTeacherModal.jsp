<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="edit_teacher" class="modal" role="dialog">
	<div class="modal-dialog modal-dialog-centered justify-content-center">
		<div class="modal-content modal-lg">
			<div class="modal-header">
				<h4 class="modal-title">Sửa thông tin giảng viên</h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<div class="modal-body">
				<form id="editTeacherForm" action="TeacherServlet" method="post"
					enctype="multipart/form-data" class="m-b-30">
					<div class="row justify-content-center">
						<!-- Hidden ID giảng viên -->
						<input type="hidden" id="editTeacherID" name="teacherID"
							value="${teacher.teacherID}">
						<input type="hidden" id="editAccountID" name="accountID"
							value="${teacher.accountID}">

						<!-- Họ và Tên -->
						<div class="col-sm-6">
							<div class="form-group form-focus">
								<input id="editFirstName" name="firstName" type="text"
									class="form-control floating" value="${teacher.firstName}"
									required> <label class="focus-label">Họ <span
									class="text-danger">*</span></label>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="form-group form-focus">
								<input id="editLastName" name="lastName" type="text"
									class="form-control floating" value="${teacher.lastName}"
									required> <label class="focus-label">Tên <span
									class="text-danger">*</span></label>
							</div>
						</div>

						<!-- Email -->
						<div class="col-sm-12">
							<div class="form-group form-focus">
								<input id="editEmail" name="email" type="email"
									class="form-control floating" value="${teacher.email}" required>
								<label class="focus-label">Email <span
									class="text-danger">*</span><span id="editEmailError"
									class="text-danger" style="display: none;">Email không
										hợp lệ!</span></label>

							</div>
						</div>

						<!-- Số điện thoại -->
						<div class="col-sm-12">
							<div class="form-group form-focus">
								<input id="editPhone" name="phone" type="text"
									class="form-control floating" pattern="0[0-9]{9}"
									title="Số điện thoại phải từ 10-11 chữ số"
									value="${teacher.phone}" required> <label
									class="focus-label">Số điện thoại <span
									class="text-danger">*</span> <span id="PhoneError"
									class="text-danger" style="display: none;">Số điện thoại
										không hợp lệ!</span></label>
							</div>
						</div>

						<!-- Khoa -->
						<div class="col-sm-12">
							<div class="form-group form-focus">
								<select id="editDepartmentID" class="form-control"
									name="departmentID" required>
									<option value="" disabled>Chọn khoa</option>
									<c:forEach var="department" items="${departments}">
										<option value="${department.departmentID}"
											<c:if test="${department.departmentID == teacher.departmentID}">
												selected
											</c:if>>
											${department.departmentName}
										</option>
									</c:forEach>
								</select> <label class="focus-label">Khoa<span
									class="text-danger">*</span></label>
							</div>
						</div>

						<!-- Văn phòng -->
						<div class="col-sm-12">
							<div class="form-group form-focus">
								<input id="editOffice" name="office" type="text"
									class="form-control floating" value="${teacher.office}">
								<label class="focus-label">Văn phòng</label>
							</div>
						</div>

						<!-- Ngày tuyển dụng -->
						<div class="col-sm-12">
							<div class="form-group form-focus">
								<input id="editHireDate" name="hireDate" type="date"
									class="form-control floating" value="${teacher.hireDate}"
									required> <label class="focus-label">Ngày tuyển
									dụng <span class="text-danger">*</span>
								</label>
							</div>
						</div>

						<!-- Avatar -->
						<div class="col-sm-12">
							<input type="hidden" name="currentAvatar"
							value="${teacher.account.avatar}">
						<div class="col-sm-12">
							<div class="form-group">
								<label for="avatarInputEdit">Ảnh đại diện</label> <input
									type="file" id="avatarInputEdit"
									class="form-control avatar-input" data-target-img="editAvatar"
									accept="image/*" name="avatar"> <img id="editAvatar"
									src="${teacher.account.avatar}" alt="Ảnh đại diện"
									class="img-thumbnail mt-2" style="max-width: 150px;">
							</div>

						</div>
						</div>

						<!-- Submit -->
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

<script>
    // Hiển thị tên file khi chọn ảnh đại diện (edit)
    function displayEditFileName(input) {
        const fileName = input.files[0]?.name || "";
        document.getElementById('editAvatarFileName').textContent = fileName
            ? `Đã chọn: ${fileName}`
            : "Chưa chọn file.";
    }
</script>
