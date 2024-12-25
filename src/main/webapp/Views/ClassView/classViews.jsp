<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quản lý lớp học</title>
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
							<h3 class="page-title mb-0">Quản lý lớp học</h3>
						</div>
						<div class="col-md-6">
							<ul class="breadcrumb mb-0 p-0 float-right">
								<li class="breadcrumb-item"><a href="index.jsp"><i
										class="fas fa-home"></i> Trang chủ</a></li>
								<li class="breadcrumb-item active"><span>Lớp học</span></li>
							</ul>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-4 col-4"></div>
					<div class="col-sm-8 col-8 text-right add-btn-col">
						<a href="javascript:void(0);" class="btn btn-primary btn-rounded"
							data-toggle="modal" data-target="#add_class"> <i
							class="fas fa-plus"></i> Thêm lớp học
						</a>
					</div>
				</div>
				<div class="content-page">
					<form action="ClassServlet" method="GET">
						<div class="row filter-row">
							<div class="col-sm-6 col-md-3">
								<div class="form-group form-focus">
									<input type="text" name="className"
										class="form-control floating"> <label
										class="focus-label">Tên lớp</label>
								</div>
							</div>
							<div class="col-sm-6 col-md-3">
								<div class="form-group form-focus">
									<input type="text" name="teacherName"
										class="form-control floating"> <label
										class="focus-label">Tên giáo viên</label>
								</div>
							</div>
							<div class="col-sm-6 col-md-3"></div>
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
											<th>Mã lớp</th>
											<th>Giáo viên</th>
											<th>Sinh viên ĐK</th>
											<th>Thời gian</th>
											<th>Phòng</th>
											<th>Trạng thái</th>
											<th class="text-right">Chức năng</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${not empty classes}">
												<c:forEach var="classObj" items="${classes}"
													varStatus="number">
													<tr data-id="${classObj.classID }">
														<td>${number.index + 1}</td>
														<td>${classObj.className}</td>
														<td>${classObj.teacher.firstName}
															${classObj.teacher.lastName}</td>
														<td class="text-center">${classObj.registeredStudents != null ? classObj.registeredStudents : '0'}</td>
														<td>${classObj.classTime}</td>
														<td>${classObj.room}</td>
														<td>${classObj.status}</td>
														<td class="text-right">
															<div class="dropdown dropdown-action">
																<a class="action-icon dropdown-toggle"
																	data-toggle="dropdown" aria-expanded="false"> <i
																	class="fas fa-ellipsis-v"></i>
																</a>
																<div class="dropdown-menu dropdown-menu-right">
																	<a class="dropdown-item edit-class"
																		data-id="${classObj.classID}"
																		data-name="${classObj.className}"
																		data-start-date="${classObj.startDate}"
																		data-end-date="${classObj.endDate}"
																		data-type="${classObj.classType}"
																		data-teacher-id="${classObj.teacherID}"
																		data-course-id="${classObj.courseID}"
																		data-status="${classObj.status}"
																		data-room="${classObj.room}"
																		data-time="${classObj.classTime}"
																		data-semester="${classObj.semester}"
																		data-max-students="${classObj.maxStudents}"
																		data-total-lessions="${classObj.totalLessions}"><i
																		class="fas fa-pencil-alt m-r-5"></i> Sửa
																	</a> <a class="dropdown-item delete-class"
																		data-id="${classObj.classID}"> <i
																		class="fas fa-trash-alt m-r-5"></i> Xóa
																	</a>
																</div>
															</div>
														</td>
													</tr>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<tr>
													<td colspan="8">Hãy thêm mới lớp học</td>
												</tr>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>

				<jsp:include page="addClassModal.jsp"></jsp:include>
				<jsp:include page="deleteClassModal.jsp"></jsp:include>
				<jsp:include page="editClassModal.jsp"></jsp:include>
			</div>
		</div>
	</div>

	<jsp:include page="../../includes/footer.jsp"></jsp:include>
	<script
		src="${pageContext.request.contextPath}/Views/ClassView/ClassJS.js?v=<%= System.currentTimeMillis() %>"></script>
</body>
</html>
