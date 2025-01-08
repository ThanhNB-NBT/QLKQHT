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
<title>Quản lý lớp học của giáo viên</title>
<jsp:include page="../../../includes/resources.jsp"></jsp:include>
</head>
<body>
	<div class="main-wrapper">
		<jsp:include page="../../../includes/header.jsp"></jsp:include>
		<div class="page-wrapper">
			<div class="content container-fluid">
				<div class="page-header">
					<div class="row">
						<div class="col-md-6">
							<h3 class="page-title mb-0">Danh sách lớp học</h3>
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
							<div class="col-sm-6 col-md-3"></div>
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
											<th>Tên lớp</th>
											<th>Thời gian</th>
											<th>Phòng</th>
											<th>Số lượng SV</th>
											<th>Trạng thái</th>
											<th class="text-right">Chức năng</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${not empty classList}">
												<c:forEach var="classObj" items="${classList}"
													varStatus="number">
													<tr data-id="${classObj.classID}">
														<td>${number.index + 1}</td>
														<td>${classObj.className}</td>
														<td>${classObj.classTime}</td>
														<td>${classObj.room}</td>
														<td>${classObj.registeredStudents}</td>
														<td>${classObj.status}</td>
														<td class="align-items-center"><a
															class="btn btn-primary edit-class"
															data-id="${classObj.classID}"
															data-name="${classObj.className}"
															data-start-date="${classObj.startDate}"
															data-end-date="${classObj.endDate}"
															data-type="${classObj.classType}"
															data-status="${classObj.status}"
															data-room="${classObj.room}"
															data-time="${classObj.classTime}"
															data-semester="${classObj.semester}"
															data-max-students="${classObj.maxStudents}"
															data-total-lessions="${classObj.totalLessions}"> <i
																class="far fa-edit"></i>
														</a></td>
													</tr>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<tr>
													<td colspan="8">Không có lớp học nào được phân công.</td>
												</tr>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<jsp:include page="editTeacherClassModal.jsp"></jsp:include>
			</div>
		</div>
	</div>
	<jsp:include page="../../../includes/footer.jsp"></jsp:include>
	<script
		src="${pageContext.request.contextPath}/Views/TeacherClassView/TeacherClassJS.js?v=<%= System.currentTimeMillis() %>"></script>
</body>
</html>
