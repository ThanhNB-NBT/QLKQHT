<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="add_department" class="modal" role="dialog">
	<div class="modal-dialog modal-dialog-centered justify-content-center">
		<div class="modal-content modal-lg">
			<div class="modal-header">
				<h4 class="modal-title">Thêm Khoa/viện</h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<div class="modal-body">
				<form id="addDepartmentForm" action="DepartmentServlet"
					method="post" class="m-b-30">
					<div class="row justify-content-center">
						<div class="col-sm-8">
							<div class="form-group form-focus">
								<input name="departmentName" type="text"
									class="form-control floating" required pattern=".{2,}"
									title="Tên khoa phải có ít nhất 2 ký tự"> <label
									class="focus-label">Tên khoa/viện <span
									class="text-danger">*</span></label>
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
								<input name="phone" type="tel" class="form-control floating"
									required pattern="0[0-9]{9}"
									title="Vui lòng nhập đúng định dạng 10 số"> <label
									class="focus-label">SĐT <span class="text-danger">*</span></label>
							</div>
							<div class="m-t-20 text-center">
								<button type="submit" class="btn btn-primary btn-lg"
									name="action" value="create">Thêm khoa</button>
							</div>
						</div>

					</div>
				</form>
			</div>
		</div>
	</div>
</div>