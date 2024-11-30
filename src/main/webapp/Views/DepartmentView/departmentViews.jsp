<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="models.bean.Account" %>
<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
%>
<%

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
								<li class="breadcrumb-item"><a href="index.jsp"><i class="fas fa-home"></i> Trang chủ</a></li>
								<li class="breadcrumb-item"><span>Khoa/viện</span></li>
							</ul>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-4 col-4"></div>
					<div class="col-sm-8 col-8 text-right add-btn-col">
						<a href="javascript:void(0);" class="btn btn-primary btn-rounded" data-toggle="modal" data-target="#add_department">
							<i class="fas fa-plus"></i> Thêm khoa/viện
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
								<button type="submit" class="btn btn-search rounded btn-block mb-3">Tìm kiếm</button>
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
										<c:choose>
											<c:when test="${not empty departments}">
												<c:forEach var="department" items="${departments}" varStatus="status">
													<tr data-department-id="${department.departmentID}">
														<td>${status.index + 1}</td>
														<td><span class="badge" style="font-size: 13px;">${department.departmentName}</span></td>
														<td>${department.email}</td>
														<td>${department.phone}</td>
														<c:if test="${isAdmin}">
															<td class="text-right">
																<div class="dropdown dropdown-action">
																	<a href="#" class="action-icon dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
																		<i class="fas fa-ellipsis-v"></i>
																	</a>
																	<div class="dropdown-menu dropdown-menu-right">
																		<a class="dropdown-item edit-department" 
																			data-id="${department.departmentID}" 
																			data-name="${department.departmentName}" 
																			data-email="${department.email}" 
																			data-phone="${department.phone}">
																			<i class="fas fa-pencil-alt m-r-5"></i> Sửa
																		</a>
																		<a class="dropdown-item delete-department" data-id="${department.departmentID}">
																			<i class="fas fa-trash-alt m-r-5"></i> Xóa
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
													<td colspan="5">Không có dữ liệu</td>
												</tr>
											</c:otherwise>
										</c:choose>
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
	<script src="${pageContext.request.contextPath}/Views/DepartmentView/DepartmentJS.js?v=<%= System.currentTimeMillis() %>"></script>
</body>
</html>
