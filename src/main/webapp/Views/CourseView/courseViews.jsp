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
<title>Quản lý học phần</title>
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
							<h3 class="page-title mb-0">Quản lý học phần</h3>
						</div>
						<div class="col-md-6">
							<ul class="breadcrumb mb-0 p-0 float-right">
								<li class="breadcrumb-item"><a href="index.jsp"><i class="fas fa-home"></i> Trang chủ</a></li>
								<li class="breadcrumb-item active"><span>Học phần</span></li>
							</ul>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-4 col-4"></div>
					<div class="col-sm-8 col-8 text-right add-btn-col">
						<a href="javascript:void(0);" class="btn btn-primary btn-rounded" data-toggle="modal" data-target="#add_course">
							<i class="fas fa-plus"></i> Thêm học phần
						</a>
					</div>
				</div>
				<div class="content-page">
					<form action="CourseServlet" method="GET">
						<div class="row filter-row">
							<div class="col-sm-8 col-md-3">
								<div class="form-group form-focus">
									<input type="text" name="search" class="form-control floating">
									<label class="focus-label">Tên học phần</label>
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
											<th style="width: 30%;">Mã học phần</th>
											<th>Tên</th>
											<th>Số tín chỉ</th>
											<th>Thể loại</th>
											<th>Khoa</th>
											<th>Trạng thái</th>
											<th class="text-right">Chức năng</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${not empty courses}">
												<c:forEach var="course" items="${courses}" varStatus="number">
													<tr data-course-id="${course.courseID }">
														<td>${number.index + 1}</td>
														<td><span class="badge" style="font-size: 13px;">${course.courseCode}</span></td>
														<td>${course.courseName}</td>
														<td>${course.credits}</td>
														<td>${course.courseType}</td>
														<td>${course.department != null ? course.department.departmentName : 'Không xác định'}</td>
														<td>${course.status}</td>
														<c:if test="${isAdmin}">
															<td class="text-right">
																<div class="dropdown dropdown-action">
																	<a href="#" class="action-icon dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
																		<i class="fas fa-ellipsis-v"></i>
																	</a>
																	<div class="dropdown-menu dropdown-menu-right">
																		<a class="dropdown-item edit-course"
																			data-id="${course.courseID}"
																			data-name="${course.courseName}"
																			data-credits="${course.credits}"
																			data-type="${course.courseType}"
																			data-department="${course.department.departmentName}"
																			data-departmentid="${course.departmentID}"
																			data-status="${course.status}">
																			<i class="fas fa-pencil-alt m-r-5"></i> Sửa
																		</a>
																		<a class="dropdown-item delete-course" data-id="${course.courseID}">
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
													<td colspan="5">Hãy thêm mới học phần</td>
												</tr>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>

				<jsp:include page="addCourseModal.jsp"></jsp:include>
				<jsp:include page="deleteCourseModal.jsp"></jsp:include>
				<jsp:include page="editCourseModal.jsp"></jsp:include>
			</div>
		</div>
	</div>

	<jsp:include page="../../includes/footer.jsp"></jsp:include>
	<script src="${pageContext.request.contextPath}/Views/CourseView/courseJS.js?v=<%= System.currentTimeMillis() %>"></script>
</body>
</html>
