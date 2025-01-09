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
<title>Bảng điểm chi tiết</title>
<jsp:include page="../../includes/resources.jsp" />
<style>
/* Grade display styling */
.grade-box {
	display: inline-block;
	background-color: #007bff;
	color: white;
	border-radius: 4px;
	padding: 2px 8px;
	min-width: 40px;
	text-align: center;
	font-weight: normal;
}

/* Center alignment for grade columns */
.grade-column {
	text-align: center;
}

/* Add some spacing between rows */
.table td {
	vertical-align: middle;
}

/* Ensure consistent width for grade boxes */
.grade-box-container {
	min-width: 60px;
	display: flex;
	justify-content: center;
}
</style>
</head>
<body>
	<div class="main-wrapper">
		<jsp:include page="../../includes/header.jsp" />
		<div class="page-wrapper">
			<div class="content container-fluid">
				<div class="page-header">
					<div class="row">
						<div class="col-md-6">
							<h3 class="page-title mb-0">Bảng điểm chi tiết</h3>
						</div>
					</div>
				</div>
				<div class="content-page">
					<div class="row">
						<div class="col-md-12 mb-3">
							<div class="table-responsive">
								<table class="table custom-table datatable">
									<thead class="thead-light">
										<tr>
											<th>STT</th>
											<th>Mã học phần</th>
											<th>Tên học phần</th>
											<th class="grade-column">Số tín chỉ</th>
											<th>Học kỳ</th>
											<th class="grade-column">Điểm hệ 10</th>
											<th class="grade-column">Điểm chữ</th>
										</tr>
									</thead>
									<tbody>
										<c:choose>
											<c:when test="${not empty gradeList}">
												<c:forEach var="grade" items="${gradeList}"
													varStatus="status">
													<tr>
														<td>${status.index + 1}</td>
														<td>${grade.courseCode}</td>
														<td>${grade.courseName}</td>
														<td class="grade-column">${grade.credits}</td>
														<td>${grade.semester}</td>
														<td class="grade-column">
															<div class="grade-box-container">
																<span class="grade-box">${grade.componentScore}</span>
															</div>
														</td>
														<td class="grade-column">
															<div class="grade-box-container">
																<span class="grade-box">${grade.gradeLetter}</span>
															</div>
														</td>
													</tr>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<tr>
													<td colspan="8" class="text-center">Chưa có dữ liệu</td>
												</tr>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../../includes/footer.jsp" />
</body>
</html>
