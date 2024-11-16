<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@page import="models.bean.Department"%>
<%@page import="models.bean.Account"%>
<%@page import="java.util.List"%>
<%@page import="jakarta.servlet.http.HttpSession"%>
<%@page import="common.SessionUtils"%>

<%
// Kiểm tra người dùng đã đăng nhập hay chưa
if (!SessionUtils.isLoggedIn(session)) {
	// Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
	response.sendRedirect("../login.jsp");
	return; // Dừng việc xử lý tiếp
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quản lý khoa/viện</title>
<jsp:include page="../includes/resources.jsp"></jsp:include>
</head>
<body>
	<div class="main-wrapper">
		<jsp:include page="../includes/header.jsp"></jsp:include>
		<div class="page-wrapper">
			<div class="content container-fluid">
				<div class="page-header">
					<div class="row">
						<div class="col-md-6">
							<h3 class="page-title mb-0">Quản lý khoa/viện</h3>
						</div>
						<div class="col-md-6">
							<ul class="breadcrumb mb-0 p-0 float-right">
								<li class="breadcrumb-item"><a href="index.jsp"><i
										class="fas fa-home"></i> Trang chủ</a></li>
								<li class="breadcrumb-item"><span>Khoa/viện</span></li>
							</ul>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-4 col-4"></div>
					<div class="col-sm-8 col-8 text-right add-btn-col">
						<a href="javascript:void(0);" class="btn btn-primary btn-rounded"
							data-toggle="modal" data-target="#add_department"> <i
							class="fas fa-plus"></i> Thêm khoa/viện
						</a>
					</div>
				</div>
				<div class="content-page">
					<form action="DepartmentServlet" method="GET">
						<div class="row filter-row">
							<div class="col-sm-8 col-md-3">
								<div class="form-group form-focus">
									<input type="text" name="search" class="form-control floating">
									<label class="focus-label">Tên khoan/viện</label>
								</div>
							</div>
							<div class="col-sm-6 col-md-3">
								<div class="form-group form-focus"></div>
							</div>
							<div class="col-sm-6 col-md-3">
								<div class="form-group form-focus"></div>
							</div>
							<div class="col-sm-6 col-md-3">
								<button type="submit"
									class="btn btn-search rounded btn-block mb-3">Tìm kiếm
								</button>
							</div>
						</div>
					</form>
					<div class="row">
						<div class="col-md-12 mb-3">
							<div class="table-responsive">
								<table class="table custom-table datatable">
									<thead class="thead-light">
										<tr>
											<th>STT</th>
											<th style="width: 30%;">Tên</th>
											<th>Email</th>
											<th>SĐT</th>
											<th class="text-right">Chức năng</th>
										</tr>
									</thead>
									<tbody>
										<%
										int i = 1;
										List<Department> departments = (List<Department>) request.getAttribute("departments");
										if (departments != null && !departments.isEmpty()) {
											for (Department department : departments) {
										%>
										<tr>
											<td><%=i++%></td>
											<td><a class="badge" style="font-size: 13px;"> <%=department.getDepartmentName()%>
											</a></td>
											<td><a href="/cdn-cgi/l/email-protection"
												class="__cf_email__"
												data-cfemail="dfbbbeb1b6bab3afb0adabbaad9fbaa7beb2afb3baf1bcb0b2">
													<%=department.getEmail()%>
											</a></td>
											<td><%=department.getPhone()%></td>

											<%
											Account loggedInUser = (Account) session.getAttribute("loggedInUser");
											if (loggedInUser != null) {
												Integer userRole = loggedInUser.getRole().getRoleID(); // Lấy roleID từ đối tượng Account
												if (userRole != null && userRole == 1) {
											%>
											<td class="text-right">
												<div class="dropdown dropdown-action">
													<a href="#" class="action-icon dropdown-toggle"
														data-toggle="dropdown" aria-expanded="false"><i
														class="fas fa-ellipsis-v"></i></a>
													<div class="dropdown-menu dropdown-menu-right">
														<a class="dropdown-item edit-department"
															data-id="<%=department.getDepartmentID()%>"> <i
															class="fas fa-pencil-alt m-r-5"></i> Sửa
														</a> <a class="dropdown-item delete-department"
															data-id="<%=department.getDepartmentID()%>"> <i
															class="fas fa-trash-alt m-r-5"></i> Xóa
														</a>
													</div>
												</div>
											</td>
											<%
											}
											}
											%>
										</tr>
										<%
										}
										} else {
										%>
										<tr>
											<td colspan='4'>Không có dữ liệu</td>
										</tr>
										<%
										}
										%>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div id="add_department" class="modal" role="dialog">
					<div
						class="modal-dialog modal-dialog-centered justify-content-center">
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
												<input name="email" type="email"
													class="form-control floating" required
													title="Email không hợp lệ!"> <label
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
				<div id="delete_department" class="modal" role="dialog">
					<div class="modal-dialog modal-dialog-centered">
						<div class="modal-content modal-md">
							<div class="modal-header">
								<h4 class="modal-title">Xóa khoa</h4>
								<button type="button" class="close" data-dismiss="modal">&times;</button>
							</div>
							<form id="deleteForm" method="post" action="DepartmentServlet">
								<div class="modal-body">
									<p>Bạn có chắc chắn muốn xóa không?</p>
									<input type="hidden" id="deleteDepartmentId"
										name="departmentID"> <input type="hidden"
										name="action" value="delete">
									<!-- Truyền action delete -->
									<a href="#" class="btn btn-white" data-dismiss="modal">Đóng</a>
									<button type="submit" class="btn btn-danger">Xóa</button>
								</div>
							</form>
						</div>
					</div>
				</div>

				<div class="modal" id="edit_department" role="dialog">
					<div class="modal-dialog modal-dialog-centered" role="document">
						<div class="modal-content modal-lg">
							<div class="modal-header">
								<h4 class="modal-title" id="editDepartmentLabel">Sửa tài
									khoản</h4>
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
												id="editPhone" type="tel" class="form-control floating"
												required pattern="0[0-9]{9}"
												title="Vui lòng nhập đúng định dạng 10 số">
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

			</div>
		</div>
	</div>
	<jsp:include page="../includes/footer.jsp"></jsp:include>
	<script>
	  // Xử lý sự kiện khi nhấn nút 'edit'
	  document.body.addEventListener('click', function(event) {
	    if (event.target.classList.contains('edit-department')) {
	      const departmentId = event.target.getAttribute('data-id');
	      document.getElementById('editDepartmentID').value = departmentId;
	      $.ajax({
	        url: 'DepartmentServlet',
	        type: 'GET',
	        data: {
	          departmentID: departmentId
	        },
	        dataType: 'json',
	        success: function(department) {
	          if (department) {
	            document.getElementById('editDepartmentName').value = department.departmentName;
	            document.getElementById('editEmail').value = department.email;
	            document.getElementById('editPhone').value = department.phone;
	            $('#edit_department').modal('show'); // Hiển thị modal chỉnh sửa
	          } else {
	            alert("Khoa không tồn tại.");
	          }
	        },
	        error: function(xhr, status, error) {
	          console.error("Lỗi khi tải thông tin khoa: " + error);
	        }
	      });
	    }
	  });
	
	  // Xử lý sự kiện khi nhấn nút 'delete'
	  document.body.addEventListener('click', function(event) {
	    if (event.target.classList.contains('delete-department')) {
	      const departmentId = event.target.getAttribute('data-id');
	      document.getElementById('deleteDepartmentId').value = departmentId;
	      $('#delete_department').modal('show'); // Hiển thị modal xóa
	    }
	  });
	
	  // Xử lý submit form xóa qua AJAX
	  document.getElementById('deleteForm').addEventListener('submit', function(event) {
	    event.preventDefault();
	    const departmentId = document.getElementById('deleteDepartmentId').value;
	    if (!departmentId) {
	        console.error("Department ID is missing");
	        return;
	      }
	    $.ajax({
	      url: 'DepartmentServlet',
	      type: 'POST',
	      data: {
	        departmentID: departmentId,
	        action: 'delete'
	      },
	      success: function(response) {

 	    	  location.reload(); // Reload lại trang hoặc
	        // $('tr[data-department-id="' + departmentId + '"]').remove(); // Cách thay thế: xóa dòng
	        $('#delete_department').modal('hide'); // Đóng modal
	      },
	      error: function(xhr, status, error) {
	        console.error("Lỗi khi xóa khoa: " + error);
	      }
	    });
	  });
</script>

</body>
</html>