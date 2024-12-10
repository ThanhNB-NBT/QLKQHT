<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="add_student" class="modal" role="dialog">
	<div class="modal-dialog modal-dialog-centered modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">Thêm sinh viên</h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<div class="modal-body">
				<form id="addStudentForm" action="StudentServlet" method="post"
					enctype="multipart/form-data" class="m-b-30">
					<div class="row">
						<!-- Left Column - Form Fields -->
						<div class="col-md-8">
							<div class="row">
								<!-- Họ và Tên - Same Row -->
								<div class="col-sm-6">
									<div class="form-group form-focus">
										<input id="firstName" name="firstName" type="text"
											class="form-control floating"> <label
											class="focus-label">Họ</label>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group form-focus">
										<input id="lastName" name="lastName" type="text"
											class="form-control floating"> <label
											class="focus-label">Tên</label>
									</div>
								</div>

								<!-- Ngày sinh và Năm nhập học - Same Row -->
								<div class="col-sm-6">
									<div class="form-group form-focus">
										<input id="dateOfBirth" name="dateOfBirth" type="date"
											class="form-control floating"> <label
											class="focus-label">Ngày sinh</label>
									</div>
								</div>
								<div class="col-sm-6">
									<div class="form-group form-focus">
										<input id="enrollmentYear" name="enrollmentYear" type="date"
											class="form-control floating"> <label
											class="focus-label">Năm nhập học</label>
									</div>
								</div>

								<!-- Email -->
								<div class="col-sm-12">
									<div class="form-group form-focus">
										<input id="email" name="email" type="email"
											class="form-control floating"> <label
											class="focus-label">Email</label>
									</div>
								</div>

								<!-- Số điện thoại -->
								<div class="col-sm-12">
									<div class="form-group form-focus">
										<input id="phone" name="phone" type="text"
											class="form-control floating" pattern="0[0-9]{9}"
											title="Số điện thoại phải từ 10 chữ số"> <label
											class="focus-label">Số điện thoại</label>
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

								<!-- Khoa -->
								<div class="col-sm-12">
									<div class="form-group form-focus">
										<select class="form-control" name="departmentID">
											<option value="" disabled selected>Chọn khoa</option>
											<c:forEach var="department" items="${departments}">
												<option value="${department.departmentID}">${department.departmentName}</option>
											</c:forEach>
										</select> <label class="focus-label">Khoa</label>
									</div>
								</div>

								<!-- Import File -->
								<div class="col-sm-12">
									<div class="form-group">
										<label for="importFile">Nhập file Excel</label> <input
											type="file" class="form-control" id="importFile"
											name="importFile" accept=".xlsx, .xls">
									</div>
								</div>
							</div>
						</div>

						<!-- Right Column - Avatar -->
						<div class="col-md-4">
							<div class="form-group text-center">
								<label for="avatarInputAdd">Ảnh đại diện</label> <input
									type="file" id="avatarInputAdd"
									class="form-control avatar-input" data-target-img="addAvatar"
									accept="image/*" name="avatar"> <img id="addAvatar"
									src="assets/img/user.jpg" alt="Ảnh đại diện"
									class="img-thumbnail mt-3" style="max-width: 250px;">
							</div>
						</div>

						<!-- Submit Button - Full Width -->
						<div class="text-center mt-3">
							<button type="submit" class="btn btn-primary" name="action"
								value="create">Thêm sinh viên</button>
							<button type="submit" class="btn btn-success" name="action"
								value="import">Nhập file Excel</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
