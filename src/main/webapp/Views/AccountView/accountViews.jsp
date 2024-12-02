<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quản lý tài khoản</title>
<jsp:include page="../../includes/resources.jsp" />
</head>
<body>
	<div class="main-wrapper">
		<jsp:include page="../../includes/header.jsp" />
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
								<li class="breadcrumb-item active">Tài khoản</li>
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
									<input type="text" name="search" class="form-control floating" />
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
									class="btn btn-search rounded btn-block mb-3">Tìm kiếm</button>
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
											<th>Tên</th>
											<th>Email</th>
											<th>Quyền</th>
											<th class="text-right">Chức năng</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${not empty accounts}">
												<c:forEach var="account" items="${accounts}"
													varStatus="status">
													<tr>
														<td>${status.index + 1}</td>
														<td><a href="profile.html" class="avatar">${fn:substring(account.username, 0, 1).toUpperCase()}</a>
															<h2>
																<a class="badge" href="profile.html">${account.username}</a>
															</h2></td>
														<td>${account.email}</td>
														<td><c:choose>
																<c:when test="${account.role.roleID == 1}">
																	<span class="badge badge-danger-border">${account.role.role}</span>
																</c:when>
																<c:when test="${account.role.roleID == 2}">
																	<span class="badge badge-success-border">${account.role.role}</span>
																</c:when>
																<c:when test="${account.role.roleID == 3}">
																	<span class="badge badge-info-border">${account.role.role}</span>
																</c:when>
																<c:otherwise>
																	<span>${account.role.role}</span>
																</c:otherwise>
															</c:choose></td>
														<c:if test="${isAdmin}">
															<td class="text-right">
																<div class="dropdown dropdown-action">
																	<a href="#" class="action-icon dropdown-toggle"
																		data-toggle="dropdown"> <i
																		class="fas fa-ellipsis-v"></i>
																	</a>
																	<div class="dropdown-menu dropdown-menu-right">
																		<a class="dropdown-item edit-account"
																			data-id="${account.accountID}"
																			data-username="${account.username}"
																			data-email="${account.email}"
																			data-role="${account.role.role}"
																			data-role-id="${account.role.roleID}"> <i
																			class="fas fa-pencil-alt m-r-5"></i> Sửa
																		</a> <a class="dropdown-item delete-account"
																			data-id="${account.accountID}"> <i
																			class="fas fa-trash-alt m-r-5"></i> Xóa
																		</a>
																	</div>
																</div>
															</td>
														</c:if>
													</tr>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<tr>
													<td colspan="5" class="text-center">Hãy thêm tài khoản
														mới</td>
												</tr>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>

				<jsp:include page="addAccountModal.jsp" />
				<jsp:include page="deleteAccountModal.jsp" />
				<jsp:include page="editAccountModal.jsp" />
			</div>
		</div>
	</div>
	<jsp:include page="../../includes/footer.jsp" />
	<script
		src="${pageContext.request.contextPath}/Views/AccountView/accountJS.js?v=<%= System.currentTimeMillis() %>"></script>


</body>
</html>
