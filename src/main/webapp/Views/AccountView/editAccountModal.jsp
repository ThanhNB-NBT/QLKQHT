<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<div class="modal" id="edit_account"  role="dialog">
	<div class="modal-dialog modal-dialog-centered" role="document">
		<div class="modal-content modal-lg">
			<div class="modal-header">
				<h4 class="modal-title" id="editAccountLabel">Sửa tài khoản</h4>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<form id="editAccountForm" action="AccountServlet" method="post"
				class="m-b-30">
				<div class="modal-body">
					<input type="hidden" id="editAccountID" name="accountID">
					<div class="form-group">
						<label for="editName">Tên đăng nhập</label> <input type="text"
							id="editName" name="name" class="form-control" required
							pattern=".{3,}" title="Tài khoản phải có ít nhất 3 ký tự">
					</div>
					<div class="form-group">
						<label for="editEmail">Email</label> <input type="email"
							id="editEmail" name="email" class="form-control" required
							title="Email không hợp lệ!">
					</div>
					<div class="form-group">
						<label for="editRole">Vai trò</label> <select id="editRole"
							name="role" class="form-control" required>
							<option value="1">Quản trị viên</option>
							<option value="2">Giảng viên</option>
							<option value="3">Sinh viên</option>
						</select>
					</div>
					<div class="form-group">
						<label for="editPassword">Đổi mật khẩu</label> <input
							type="password" id="editPassword" name="password"
							class="form-control" placeholder="Nhập mật khẩu mới"
							pattern=".{6,}" title="Mật khẩu phải có ít nhất 6 ký tự">
					</div>
					<div class="form-group">
						<label for="editConfirmPassword">Xác nhận mật khẩu mới</label> <input
							type="password" id="editConfirmPassword" name="cpass"
							class="form-control" placeholder="Nhập lại mật khẩu mới">
						<small id="editPasswordMismatchWarning" class="text-danger"
							style="display: none;">Mật khẩu không khớp</small>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Đóng</button>
					<button type="submit" class="btn btn-primary" name="action"
						value="update">Cập nhật</button>
				</div>
			</form>
		</div>
	</div>
</div>