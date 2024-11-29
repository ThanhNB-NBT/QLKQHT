<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@page import="models.bean.Department"%>
<%@page import="models.dao.DepartmentDAO"%>
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
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
%>
<%
Account loggedInUser = (Account) session.getAttribute("loggedInUser");
Integer userRole = (loggedInUser != null && loggedInUser.getRole() != null) 
    ? loggedInUser.getRole().getRoleID() 
    : null;
boolean isAdmin = (userRole != null && userRole == 1);
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quản lý khoa/viện</title>
<jsp:include page="../../includes/resources.jsp"></jsp:include>
</head>
<body>
	<div class="main-wrapper">
		<jsp:include page="../../includes/header.jsp"></jsp:include>
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
									<label class="focus-label">Tên khoa/viện</label>
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
										@SuppressWarnings("unchecked")
										List<Department> departments = (List<Department>) request.getAttribute("departments");
										if (departments != null && !departments.isEmpty()) {
											for (Department department : departments) {
										%>
										<tr data-department-id="<%= department.getDepartmentID() %>">
											<td><%= i++ %></td>
											<td><a class="badge" style="font-size: 13px;"> <%= department.getDepartmentName() %>
											</a></td>
											<td><a href="/cdn-cgi/l/email-protection"
												class="__cf_email__"
												data-cfemail="dfbbbeb1b6bab3afb0adabbaad9fbaa7beb2afb3baf1bcb0b2">
													<%= department.getEmail() %>
											</a></td>
											<td><%= department.getPhone() %></td>

											<% if (isAdmin) { %>
											<td class="text-right">
												<div class="dropdown dropdown-action">
													<a href="#" class="action-icon dropdown-toggle"
														data-toggle="dropdown" aria-expanded="false"><i
														class="fas fa-ellipsis-v"></i></a>
													<div class="dropdown-menu dropdown-menu-right">
														<a class="dropdown-item edit-department"
															data-id="<%=department.getDepartmentID()%>"
															data-name="<%= department.getDepartmentName() %>"
														    data-email="<%= department.getEmail() %>"
														    data-phone="<%= department.getPhone() %>"> <i
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
										else {
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
				
				
				<jsp:include page="addDepartmentModal.jsp"></jsp:include>
				
				<jsp:include page="deleteDepartmentModal.jsp"></jsp:include>

				<jsp:include page="editDepartmentModal.jsp"></jsp:include>

			</div>
		</div>
	</div>
	
	<jsp:include page="../../includes/footer.jsp"></jsp:include>
	<script src="${pageContext.request.contextPath}/Views/DepartmentView/DepartmentJS.js"></script>

</body>
</html>