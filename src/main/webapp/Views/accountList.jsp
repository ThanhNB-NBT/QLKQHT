<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="models.bean.Account"%>
<%@page import="java.util.ArrayList"%>
<%@page import="jakarta.servlet.http.HttpSession" %>
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

		<div class="row">
			<div class="col-sm-4 col-4"></div>
			<div class="col-sm-8 col-8 text-right add-btn-col">

				<a href="#" class="btn btn-primary btn-rounded" data-toggle="modal"
					data-target="#add_account"> <i class="fas fa-plus"></i> Thêm
					tài khoản
				</a>

			</div>
		</div>

		<div class="content-page">
			<div class="row filter-row">
				<div class="col-sm-6 col-md-3">
					<div class="form-group form-focus select-focus">
						<select class="form-control">
							<option></option>
							<option>Quản trị</option>
							<option>Giảng viên</option>
							<option>Sinh viên</option>
						</select> <label class="focus-label">Tài khoản</label>
					</div>
				</div>
				<div class="col-sm-6 col-md-3">
					<div class="form-group form-focus"></div>
				</div>
				<div class="col-sm-6 col-md-3">
					<div class="form-group form-focus"></div>
				</div>
				<div class="col-sm-6 col-md-3">
					<a href="#" class="btn btn-search rounded btn-block mb-3"> Tìm
						kiếm </a>
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
									int i = 0;
									ArrayList<Account> accounts = (ArrayList<Account>) request.getAttribute("accounts");
									if (accounts != null && !accounts.isEmpty()){
										for (Account account : accounts) {
								%>
								<tr>
									<td><% i++; %></td>
									<td><a href="profile.html" class="avatar">D</a>
										<h2>
											<a href="profile.html"> <%= account.getUsername() %>
											</a>
										</h2></td>
									<td><a href="/cdn-cgi/l/email-protection"
										class="__cf_email__"
										data-cfemail="dfbbbeb1b6bab3afb0adabbaad9fbaa7beb2afb3baf1bcb0b2">
											<%= account.getEmail() %>
									</a></td>
									<td>
										<% if (account.getRole() != null) { %> <span
										class="badge badge-info-border"><%= account.getRole().getRole() %></span>
										<% } %>
									</td>

									<% Integer userRole = (Integer) session.getAttribute("role"); // Lấy vai trò từ session
								    if (userRole != null && userRole == 1) { %>
									<td class="text-right">
										<div class="dropdown dropdown-action">
											<a href="#" class="action-icon dropdown-toggle"
												data-toggle="dropdown" aria-expanded="false"><i
												class="fas fa-ellipsis-v"></i></a>
											<div class="dropdown-menu dropdown-menu-right">
												<a class="dropdown-item edit-account"
													data-id="<%= account.getAccountID() %>" data-toggle="modal"
													data-target="#edit_account"> <i
													class="fas fa-pencil-alt m-r-5"></i> Sửa
												</a> <a class="dropdown-item delete-account"
													data-id="<%= account.getAccountID() %>" data-toggle="modal"
													data-target="#delete_account"> <i
													class="fas fa-trash-alt m-r-5"></i> Xóa
												</a>
											</div>
										</div>
									</td>
									<% } %>
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
			</div>
			</div>
		</div>
	</div>
	<jsp:include page="../includes/footer.jsp"></jsp:include>
</body>
</html>