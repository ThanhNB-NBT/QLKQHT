<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="models.bean.Account"%>
<%@page import="models.dao.AccountDAO"%>
<%@page import="java.util.List"%>
<%@page import="jakarta.servlet.http.HttpSession"%>
<%@page import="common.SessionUtils"%>

<%
// Kiểm tra người dùng đã đăng nhập hay chưa
HttpSession session1 = request.getSession(false); // Lấy session hiện tại
if (!SessionUtils.isLoggedIn(session1)) {
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
Integer userRole = (loggedInUser != null && loggedInUser.getRole() != null) ? loggedInUser.getRole().getRoleID() : null;
boolean isAdmin = (userRole != null && userRole == 1);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quản lý tài khoản</title>
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
						<a href="javascript:void(0);" class="btn btn-primary btn-rounded"
							data-toggle="modal" data-target="#add_account"> <i
							class="fas fa-plus"></i> Thêm tài khoản
						</a>
					</div>
				</div>
				<div class="content-page">
					<form action="AccountServlet" method="GET">
						<div class="row filter-row">
							<div class="col-sm-8 col-md-3">
								<div class="form-group form-focus">
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
											<th>Quyền</th>
											<th class="text-right">Chức năng</th>
										</tr>
									</thead>
									<tbody>
										<%
										int i = 1;
										@SuppressWarnings("unchecked")
										List<Account> accounts = (List<Account>) request.getAttribute("accounts");
										if (accounts != null && !accounts.isEmpty()) {
											for (Account account : accounts) {
										%>
										<tr>
											<td><%=i++%></td>
											<td><a href="profile.html" class="avatar"><%=account.getUsername().substring(0, 1).toUpperCase()%></a>
												<h2>
													<a class="badge" href="profile.html"> <%=account.getUsername()%>
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
											if (isAdmin) {
											%>
											<td class="text-right">
												<div class="dropdown dropdown-action">
													<a href="#" class="action-icon dropdown-toggle"
														data-toggle="dropdown" aria-expanded="false"><i
														class="fas fa-ellipsis-v"></i></a>
													<div class="dropdown-menu dropdown-menu-right">
														<a class="dropdown-item edit-account"
															data-id="<%=account.getAccountID()%>"
															data-username="<%=account.getUsername()%>"
															data-email="<%=account.getEmail()%>"
															data-role="<%=account.getRole().getRole()%>"
															data-role-id="<%=account.getRole().getRoleID()%>">
															<i class="fas fa-pencil-alt m-r-5"></i> Sửa
														</a> <a class="dropdown-item delete-account"
															data-id="<%=account.getAccountID()%>"> <i
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

				<jsp:include page="addAccountModal.jsp"></jsp:include>

				<jsp:include page="deleteAccountModal.jsp"></jsp:include>

				<jsp:include page="editAccountModal.jsp"></jsp:include>

			</div>
		</div>
	</div>
	<jsp:include page="../../includes/footer.jsp"></jsp:include>
	<script
		src="${pageContext.request.contextPath}/Views/AccountView/AccountJS.js"></script>
</body>
</html>