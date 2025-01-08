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
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Quản lý điểm sinh viên</title>
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
							<h3 class="page-title mb-0">Quản lý Điểm Sinh viên</h3>
						</div>
						<div class="col-md-6">
							<ul class="breadcrumb mb-0 p-0 float-right">
								<li class="breadcrumb-item"><a href="index.jsp"><i
										class="fas fa-home"></i> Trang chủ</a></li>
								<li class="breadcrumb-item active"><span>Điểm sinh
										viên</span></li>
							</ul>
						</div>
					</div>
				</div>

				<div class="row">
					<div class="col-sm-4"></div>
					<div class="col-sm-4 text-right">
						<form id="uploadExcelForm" action="GradeServlet" method="POST"
							enctype="multipart/form-data">
							<input type="hidden" name="action" value="uploadExcel">
							<!-- Thêm trường hidden để gửi classID -->
							<input type="hidden" name="classID" id="hiddenClassID" value="${selectedClassID}">
							<input type="file" name="excelFile" accept=".xlsx, .xls"
								id="gradeFileInput" style="display: none;" required>
							<button type="submit" class="btn btn-primary"
								onclick="uploadGradeFile()">Nhập điểm từ Excel</button>
						</form>
					</div>

					<div class="col-sm-4 text-right">
						<button class="btn btn-success" id="downloadExcel"
							onclick="downloadExcelTemplate()">Tải danh sách SV</button>
					</div>
				</div>

				<!-- Form chọn lớp học -->
				<div class="row">
					<div class="col-md-6">
						<form id="classSelectForm" action="GradeServlet" method="GET">
							<div class="form-group">
								<label for="classSelect">Chọn lớp học</label> <select
									class="form-control" name="classID" id="classSelect">
									<option value="">-- Chọn lớp học --</option>
									<c:forEach var="cls" items="${classes}">
										<option value="${cls.classID}"
											<c:if test="${cls.classID == selectedClassID}">selected</c:if>>${cls.className}</option>
									</c:forEach>
								</select>
							</div>
						</form>
					</div>
				</div>

				<!-- Bảng điểm sinh viên -->
				<div class="row">
					<div class="col-md-12 mb-3">
						<div class="table-responsive">
							<table class="table custom-table datatable">
								<thead class="thead-light">
									<tr>
										<th>STT</th>
										<th>Mã sinh viên</th>
										<th>Tên sinh viên</th>
										<th>Điểm chuyên cần</th>
										<th>Điểm giữa kỳ</th>
										<th>Điểm cuối kỳ</th>
										<th>Điểm thành phần</th>
										<th>Điểm chữ</th>
										<c:if test="${isTeacher}">
											<th class="text-right">Chức năng</th>
										</c:if>
									</tr>
								</thead>
								<tbody>
									<c:choose>
										<c:when test="${not empty grades}">
											<c:forEach var="grade" items="${grades}" varStatus="number">
												<tr>
													<td>${number.index + 1}</td>
													<td>${grade.studentCode}</td>
													<td>${grade.studentName}</td>
													<td class="text-center">${grade.attendanceScore}</td>
													<td class="text-center">${grade.midtermScore}</td>
													<td class="text-center">${grade.finalExamScore}</td>
													<td class="text-center">${grade.componentScore}</td>
													<td class="text-center">${grade.gradeLetter}</td>
													<c:if test="${isTeacher}">
														<td class="align-items-center"><a
															class="btn btn-primary edit-grade"
															data-id="${grade.gradeID}"
															data-student-code="${grade.studentCode}"
															data-student-name="${grade.studentName}"
															data-attendance="${grade.attendanceScore}"
															data-midterm="${grade.midtermScore}"
															data-final="${grade.finalExamScore}"
															data-class-id="${selectedClassID}"> <i
																class="far fa-edit"></i>
														</a></td>
													</c:if>
												</tr>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<tr>
												<td colspan="9" class="text-center">
													<div class="alert alert-warning" role="alert">
														${empty selectedClassID ? 'Vui lòng chọn lớp học' : 'Không có dữ liệu điểm cho lớp này'}
													</div>
												</td>
											</tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
						</div>
					</div>
				</div>

				<jsp:include page="editGradeModal.jsp"></jsp:include>

			</div>
		</div>
	</div>

	<jsp:include page="../../includes/footer.jsp"></jsp:include>

	<script
		src="${pageContext.request.contextPath}/Views/GradeView/GradeJS.js?v=<%= System.currentTimeMillis() %>">

	</script>
</body>
</html>
