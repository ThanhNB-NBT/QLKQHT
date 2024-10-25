<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="models.bean.Account"%>
<%@page import="java.util.ArrayList"%>
<%@page import="jakarta.servlet.http.HttpSession"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quản lý tài khoản</title>
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
							<h3 class="page-title mb-0">Quản lý tài khoản</h3>
						</div>
						<div class="col-md-6">
							<ul class="breadcrumb mb-0 p-0 float-right">
								<li class="breadcrumb-item"><a href="index.jsp"><i
										class="fas fa-home"></i> Trang chủ</a></li>
								<li class="breadcrumb-item"><span>Tài khoản</span></li>
							</ul>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-sm-4 col-4"></div>
					<div class="col-sm-8 col-8 text-right add-btn-col">

						<a href="#" class="btn btn-primary btn-rounded"
							data-toggle="modal" data-target="#add_account"> <i
							class="fas fa-plus"></i> Thêm tài khoản
						</a>

					</div>
				</div>

				<div class="content-page">
					<div class="row filter-row">
						<div class="col-sm-8 col-md-3">
							<div class="form-group form-focus">
								<form action="#" method="GET">
									<input type="text" name="search" class="form-control floating">
									<label class="focus-label">Tên tài khoản</label>
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
							</form>
						</div>
					</div>

					<div class="row">
						<div class="col-md-12 mb-3">
							<div class="table-responsive">
								<table class="table custom-table datatable">
									<thead class="thead-light">
										<tr>
											<th>STT</th>
											<th style="width: 30%;">Tên</th>
											<th>Email</th>
											<th>Quyền</th>
											<th class="text-right">Chức năng</th>
										</tr>
									</thead>
									<tbody>
										<%
										int i = 1;
										ArrayList<Account> accounts = (ArrayList<Account>) request.getAttribute("accounts");
										if (accounts != null && !accounts.isEmpty()) {
											for (Account account : accounts) {
										%>
										<tr>
											<td><%=i++%></td>
											<td><a href="profile.html" class="avatar">D</a>
												<h2>
													<a href="profile.html"> <%=account.getUsername()%>
													</a>
												</h2></td>
											<td><a href="/cdn-cgi/l/email-protection"
												class="__cf_email__"
												data-cfemail="dfbbbeb1b6bab3afb0adabbaad9fbaa7beb2afb3baf1bcb0b2">
													<%=account.getEmail()%>
											</a></td>
											<td>
												<%
												if (account.getRole() != null) {
													int roleID = account.getRole().getRoleID();
													String badgeClass = "";
													if (roleID == 1) {
														badgeClass = "badge badge-danger-border";
													} else if (roleID == 2) {
														badgeClass = "badge badge-success-border";
													} else if (roleID == 3) {
														badgeClass = "badge badge-info-border";
													}
													{
												%> <span class="<%=badgeClass%>"><%=account.getRole().getRole()%></span>
												<%
												}
												%>
											</td>

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
														<a class="dropdown-item edit-account"
															data-id="<%=account.getAccountID()%>" data-toggle="modal"
															data-target="#edit_account"> <i
															class="fas fa-pencil-alt m-r-5"></i> Sửa
														</a> <a class="dropdown-item delete-account"
															data-id="<%=account.getAccountID()%>" data-toggle="modal"
															data-target="#delete_account"> <i
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
				<div id="add_account" class="modal" role="dialog">
					<div
						class="modal-dialog modal-dialog-centered justify-content-center">
						<div class="modal-content modal-lg">
							<div class="modal-header">
								<h4 class="modal-title">Tạo tài khoản</h4>
								<button type="button" class="close" data-dismiss="modal">&times;</button>
							</div>
							<div class="modal-body">
								<!-- Form gửi tới AccountServlet -->
								<form action="AccountServlet" method="post" class="m-b-30">
									<div class="row justify-content-center">
										<div class="col-sm-8">
											<div class="form-group form-focus">
												<input name="name" type="text" class="form-control floating"
													required> <label class="focus-label">Tên <span
													class="text-danger">*</span></label>
											</div>
										</div>
										<div class="col-sm-8">
											<div class="form-group form-focus">
												<input name="email" type="email"
													class="form-control floating" required> <label
													class="focus-label">Email <span class="text-danger">*</span></label>
											</div>
										</div>
										<div class="col-sm-8">
											<div class="form-group form-focus">
												<input name="pass" type="password"
													class="form-control floating" required> <label
													class="focus-label">Mật khẩu <span
													class="text-danger">*</span></label>
											</div>
										</div>
										<div class="col-sm-8">
											<div class="form-group form-focus">
												<input name="cpass" type="password"
													class="form-control floating" required> <label
													class="focus-label">Nhập lại mật khẩu <span
													class="text-danger">*</span></label>
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
												<button class="btn btn-primary btn-lg" name="submit">Tạo
													tài khoản</button>
											</div>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="../includes/footer.jsp"></jsp:include>
</body>
</html>