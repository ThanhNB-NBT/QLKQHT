<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="models.bean.Account"%>
<%
Account accountToEdit = (Account) request.getAttribute("accountToEdit");
%>
<div class="modal" id="edit_account" role="dialog">
	<div class="modal-dialog modal-dialog-centered" role="document">
		<div class="modal-content modal-lg">
			<div class="modal-header">
				<h4 class="modal-title" id="editAccountLabel">
					Sửa tài khoản
					</h4>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
			</div>
			<form id="editAccountForm" action="AccountServlet" method="post">
				<div class="modal-body">
					<input type="hidden" id="editAccountID" name="accountID"
						value="<%=accountToEdit != null ? accountToEdit.getAccountID() : ""%>">

					<div class="form-group">
						<label for="editName">Tên đăng nhập</label> <input type="text"
							name="name" class="form-control"
							value="<%=%>"
							required>
					</div>
					<div class="form-group">
						<label for="editEmail">Email</label> <input type="email"
							name="email" class="form-control"
							value="<%=accountToEdit != null ? accountToEdit.getEmail() : ""%>"
							required>
					</div>
					<div class="form-group">
						<label for="editRole">Vai trò</label> <select name="role"
							class="form-control" required>
							<option value="1"
								<%=accountToEdit != null && accountToEdit.getRole().getRoleID() == 1 ? "selected" : ""%>>Quản
								trị viên</option>
							<option value="2"
								<%=accountToEdit != null && accountToEdit.getRole().getRoleID() == 2 ? "selected" : ""%>>Giảng
								viên</option>
							<option value="3"
								<%=accountToEdit != null && accountToEdit.getRole().getRoleID() == 3 ? "selected" : ""%>>Sinh
								viên</option>
						</select>
					</div>
					<div class="form-group">
						<label for="editPassword">Đổi mật khẩu</label> <input
							type="password" name="newPassword" class="form-control"
							placeholder="Nhập mật khẩu mới">
					</div>
					<div class="form-group">
						<label for="editConfirmPassword">Xác nhận mật khẩu mới</label> <input
							type="password" name="confirmPassword" class="form-control"
							placeholder="Nhập lại mật khẩu mới">
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