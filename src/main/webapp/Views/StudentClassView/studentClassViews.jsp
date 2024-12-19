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
<title>Quản lý sinh viên - Lớp học</title>
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
							<h3 class="page-title mb-0">Quản lý Sinh viên - Lớp học</h3>
						</div>
						<div class="col-md-6">
							<ul class="breadcrumb mb-0 p-0 float-right">
								<li class="breadcrumb-item"><a href="index.jsp"><i
										class="fas fa-home"></i> Trang chủ</a></li>
								<li class="breadcrumb-item"><span>Sinh viên -
										Lớp học</span></li>
							</ul>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-4 col-4"></div>
					<div class="col-sm-8 col-8 text-right add-btn-col">
						<a href="javascript:void(0);" class="btn btn-primary btn-rounded"
							data-toggle="modal" data-target="#add_student_class"> <i
							class="fas fa-plus"></i> Thêm Sinh viên - Lớp học
						</a>
					</div>
				</div>
				<div class="content-page">
					<form action="StudentClassServlet" method="GET">
						<div class="row filter-row">
							<div class="col-sm-8 col-md-3">
								<div class="form-group form-focus">
									<input type="text" name="search" class="form-control floating">
									<label class="focus-label">Tên lớp hoặc tên sinh viên</label>
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
											<th>Tên sinh viên</th>
											<th>Mã sinh viên</th>
											<th>Trạng thái</th>
											<th class="text-right">Chức năng</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${not empty studentClasses}">
												<c:forEach var="studentClass" items="${studentClasses}"
													varStatus="number">
													<tr>
														<td>${number.index + 1}</td>
														<td>${studentClass.className}</td>
														<td>${studentClass.studentName}</td>
														<td>${studentClass.studentCode}</td>
														<td>${studentClass.status}</td>
														<td class="text-right">
															<div class="dropdown dropdown-action">
																<a href="#" class="action-icon dropdown-toggle"
																	data-toggle="dropdown" aria-expanded="false"> <i
																	class="fas fa-ellipsis-v"></i>
																</a>
																<div class="dropdown-menu dropdown-menu-right">
																	<a class="dropdown-item edit-student-class"
																		data-id="${studentClass.studentClassID}"
																		data-class="${studentClass.className}"
																		data-student="${studentClass.studentName}"
																		data-code="${studentClass.studentCode}"
																		data-status="${studentClass.status}"> <i
																		class="fas fa-pencil-alt m-r-5"></i> Sửa
																	</a> <a class="dropdown-item delete-student-class"
																		data-id="${studentClass.studentClassID}"> <i
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
													<td colspan="6">Không có dữ liệu, hãy thêm mới</td>
												</tr>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>

				<jsp:include page="addStudentClassModal.jsp"></jsp:include>
				<jsp:include page="deleteStudentClassModal.jsp"></jsp:include>
				<jsp:include page="editStudentClassModal.jsp"></jsp:include>
			</div>
		</div>
	</div>

	<jsp:include page="../../includes/footer.jsp"></jsp:include>
	<script
		src="${pageContext.request.contextPath}/Views/StudentClassView/StudentClassJS.js?v=<%= System.currentTimeMillis() %>"></script>
</body>
</html>
