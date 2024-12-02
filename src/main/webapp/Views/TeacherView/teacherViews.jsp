<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quản lý giáo viên</title>
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
							<h3 class="page-title mb-0">Quản lý giáo viên</h3>
						</div>
						<div class="col-md-6">
							<ul class="breadcrumb mb-0 p-0 float-right">
								<li class="breadcrumb-item"><a href="index.jsp"><i class="fas fa-home"></i> Trang chủ</a></li>
								<li class="breadcrumb-item"><span>Giáo viên</span></li>
							</ul>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-4 col-4"></div>
					<div class="col-sm-8 col-8 text-right add-btn-col">
						<a href="javascript:void(0);" class="btn btn-primary btn-rounded" data-toggle="modal" data-target="#add_teacher">
							<i class="fas fa-plus"></i> Thêm giáo viên
						</a>
					</div>
				</div>
				<div class="content-page">
					<form action="TeacherServlet" method="GET">
						<div class="row filter-row">
							<div class="col-sm-8 col-md-3">
								<div class="form-group form-focus">
									<input type="text" name="search" class="form-control floating">
									<label class="focus-label">Tên giáo viên</label>
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
											<th>Avatar</th>
											<th>Họ và tên</th>
											<th>Email</th>
											<th>Khoa/Viện</th>
											<th>Ngày làm việc</th>
											<th class="text-right">Chức năng</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${not empty teachers}">
												<c:forEach var="teacher" items="${teachers}" varStatus="status">
													<tr data-teacher-id="${teacher.teacherID}">
														<td>${status.index + 1}</td>
														<td>
															<img src="${pageContext.request.contextPath}/${teacher.avatar}" alt="Avatar" class="img-thumbnail" width="40" height="40">
														</td>
														<td>${teacher.firstName} ${teacher.lastName}</td>
														<td>${teacher.email}</td>
														<td>${teacher.department != null ? teacher.department.departmentName : "Chưa có khoa/viện"}</td>
														<td>
															<fmt:formatDate value="${teacher.hireDate}" pattern="dd/MM/yyyy"/>
														</td>
														<td class="text-right">
															<div class="dropdown dropdown-action">
																<a href="#" class="action-icon dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
																	<i class="fas fa-ellipsis-v"></i>
																</a>
																<div class="dropdown-menu dropdown-menu-right">
																	<a class="dropdown-item edit-teacher"
																		data-id="${teacher.teacherID}"
																		data-first-name="${teacher.firstName}"
																		data-last-name="${teacher.lastName}"
																		data-email="${teacher.email}"
																		data-phone="${teacher.phone}"
																		data-department-id="${teacher.departmentID}"
																		data-office="${teacher.office}"
																		data-hire-date="${teacher.hireDate}"
																		data-avatar="${teacher.avatar}">
																		<i class="fas fa-pencil-alt m-r-5"></i> Sửa
																	</a>
																	<a class="dropdown-item delete-teacher" data-id="${teacher.teacherID}">
																		<i class="fas fa-trash-alt m-r-5"></i> Xóa
																	</a>
																</div>
															</div>
														</td>
													</tr>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<tr>
													<td colspan="10">Không có dữ liệu</td>
												</tr>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>

				<jsp:include page="addTeacherModal.jsp"></jsp:include>
				<jsp:include page="deleteTeacherModal.jsp"></jsp:include>
				<jsp:include page="editTeacherModal.jsp"></jsp:include>
			</div>
		</div>
	</div>

	<jsp:include page="../../includes/footer.jsp"></jsp:include>
	<script src="${pageContext.request.contextPath}/Views/TeacherView/teacherJS.js?v=<%= System.currentTimeMillis() %>"></script>
</body>
</html>
