<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div class="modal" id="edit_account" role="dialog">
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
				class="m-b-30" enctype="multipart/form-data">
				<div class="modal-body">
					<input type="hidden" id="editAccountID" name="accountID"
						value="${account.accountID}">

					<!-- Tên đăng nhập -->
					<div class="form-group">
						<label for="editName">Tên đăng nhập</label> <input type="text"
							id="editName" name="name" class="form-control"
							value="${account.username}" required pattern=".{3,}"
							title="Tài khoản phải có ít nhất 3 ký tự">

					</div>

					<!-- Email -->
					<div class="form-group">
						<label for="editEmail">Email</label> <input type="email"
							id="editEmail" name="email" class="form-control"
							value="${account.email}" required title="Email không hợp lệ!">

					</div>

					<!-- Vai trò -->
					<div class="form-group">
						<label for="editRole">Vai trò</label> <select id="editRole"
							name="role" class="form-control" required>
							<option value="1">Quản trị viên</option>
							<option value="2">Giảng viên</option>
							<option value="3">Sinh viên</option>
						</select>

					</div>
					<input type="hidden" name="currentAvatar"
							value="${pageContext.request.contextPath}/${account.avatar}">
					<div class="form-group">
						<label for="avatarInputEdit">Ảnh đại diện</label> <input
							type="file" id="avatarInputEdit"
							class="form-control avatar-input" data-target-img="editAvatar"
							accept="image/*" name="avatar"> <img id="editAvatar"
							src="${pageContext.request.contextPath}/${account.avatar}" alt="Ảnh đại diện"
							class="img-thumbnail mt-2" style="max-width: 150px;">
					</div>

					<!-- Mật khẩu -->
					<div class="form-group">
						<label for="editPassword">Đổi mật khẩu</label> <input
							type="password" id="editPassword" name="password"
							class="form-control" placeholder="Nhập mật khẩu mới"
							pattern=".{6,}" title="Mật khẩu phải có ít nhất 6 ký tự">
					</div>

					<!-- Xác nhận mật khẩu -->
					<div class="form-group">
						<label for="editConfirmPassword">Xác nhận mật khẩu mới</label> <input
							type="password" id="editConfirmPassword" name="cpass"
							class="form-control" placeholder="Nhập lại mật khẩu mới">
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
