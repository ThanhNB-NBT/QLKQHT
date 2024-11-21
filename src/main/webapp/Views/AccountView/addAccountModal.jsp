<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="add_account" class="modal" role="dialog">
	<div class="modal-dialog modal-dialog-centered justify-content-center">
		<div class="modal-content modal-lg">
			<div class="modal-header">
				<h4 class="modal-title">Tạo tài khoản</h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<div class="modal-body">
				<form id="addAccountForm" action="AccountServlet" method="post"
					class="m-b-30">
					<div class="row justify-content-center">
						<div class="col-sm-8">
							<div class="form-group form-focus">
								<input name="name" type="text" class="form-control floating"
									required pattern=".{3,}"
									title="Tài khoản phải có ít nhất 3 ký tự"> <label
									class="focus-label">Tên <span class="text-danger">*</span></label>
							</div>
						</div>
						<div class="col-sm-8">
							<div class="form-group form-focus">
								<input name="email" type="email" class="form-control floating"
									required title="Email không hợp lệ!"> <label
									class="focus-label">Email <span class="text-danger">*</span></label>
							</div>
						</div>
						<div class="col-sm-8">
							<div class="form-group form-focus">
								<input id="password" name="password" type="password"
									class="form-control floating" required pattern=".{6,}"
									title="Mật khẩu phải có ít nhất 6 ký tự"> <label
									class="focus-label">Mật khẩu <span class="text-danger">*</span></label>
							</div>
						</div>
						<div class="col-sm-8">
							<div class="form-group form-focus">
								<input id="confirmPassword" name="cpass" type="password"
									class="form-control floating" required
									title="Xác nhận mật khẩu không được để trống"> <label
									class="focus-label">Nhập lại mật khẩu <span
									class="text-danger">*</span></label> <small
									id="passwordMismatchWarning" class="text-danger"
									style="display: none;">Mật khẩu không khớp</small>
							</div>
						</div>
						<div class="col-sm-8">
							<div class="form-group form-focus">
								<select class="form-control" name="role" required>
									<option value="1">Quản trị</option>
									<option value="2">Giảng viên</option>
									<option value="3">Sinh viên</option>
								</select> <label class="focus-label">Quyền</label>
							</div>
							<div class="m-t-20 text-center">
								<button type="submit" class="btn btn-primary btn-lg"
									name="action" value="create">Tạo tài khoản</button>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>