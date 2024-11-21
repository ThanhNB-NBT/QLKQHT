<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<div class="modal" id="edit_department" role="dialog">
	<div class="modal-dialog modal-dialog-centered" role="document">
		<div class="modal-content modal-lg">
			<div class="modal-header">
				<h4 class="modal-title" id="editDepartmentLabel">Sửa khoa/viện</h4>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<form id="editDepartmentForm" action="DepartmentServlet"
				method="post" class="m-b-30">
				<div class="modal-body">
					<input type="hidden" id="editDepartmentID" name="departmentID">
					<div class="form-group">
						<label for="editDepartmentName">Tên khoa</label> <input
							type="text" id="editDepartmentName" name="departmentName"
							class="form-control" required pattern=".{3,}"
							title="Khoa phải có ít nhất 3 ký tự">
					</div>
					<div class="form-group">
						<label for="editEmail">Email</label> <input type="email"
							id="editEmail" name="email" class="form-control" required
							title="Email không hợp lệ!">
					</div>
					<div class="form-group">
						<div class="form-group form-focus">
							<label for="editPhone">SĐT</label> <input name="phone"
								id="editPhone" type="tel" class="form-control floating" required
								pattern="0[0-9]{9}" title="Vui lòng nhập đúng định dạng 10 số">
						</div>
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