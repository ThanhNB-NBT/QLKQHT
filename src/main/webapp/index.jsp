<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="jakarta.servlet.http.HttpSession"%>
<%@ page import="common.AlertManager"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Trang chủ</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=0">

<link rel="shortcut icon" type="image/x-icon"
	href="assets/img/favicon.png">
<link rel="stylesheet" href="assets/css/bootstrap.min.css">
<link rel="stylesheet" href="assets/plugins/fontawesome/css/all.min.css">
<link rel="stylesheet"
	href="assets/plugins/fontawesome/css/fontawesome.min.css">
<link rel="stylesheet" href="assets/css/fullcalendar.min.css">
<link rel="stylesheet" href="assets/plugins/morris/morris.css">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/alertify.min.css" />
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/themes/default.min.css" />
<link rel="stylesheet" href="assets/css/style.css">

<script src="assets/js/jquery-3.6.0.min.js"></script>
<script src="assets/js/bootstrap.bundle.min.js"></script>
<script src="assets/js/jquery.slimscroll.js"></script>
<script src="assets/js/jquery.dataTables.min.js"></script>
<script src="assets/js/dataTables.bootstrap4.min.js"></script>
<script src="assets/js/select2.min.js"></script>
<script src="assets/js/moment.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script
	src="assets/plugins/datetimepicker/js/tempusdominus-bootstrap-4.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/alertify.min.js"></script>
<script src="assets/js/app.js"></script>
</head>
<body>
	<div class="main-wrapper">
		<jsp:include page="includes/header.jsp"></jsp:include>

		<div class="page-wrapper">
			<c:choose>
				<c:when test="${isStudent}">
					<div class="content container-fluid">
						<h1>Chào mừng bạn đến với hệ thống quản lí điểm</h1>
					</div>
				</c:when>
				<c:when test="${isAdmin || isTeacher}">
					<div class="content container-fluid">
						<h2 class="page-title">Thống kê điểm theo lớp</h2>

						<!-- Form chọn lớp -->
						<div class="row">
							<div class="col-md-6">
								<form id="classSelectForm" action="GradeDashboardServlet"
									method="GET">
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

						<!-- Hiển thị thống kê -->
						<c:if test="${not empty gradeList}">
							<div class="statistics-grid">
								<!-- Thống kê tổng quan -->
								<div class="statistics-box overview">
									<h3>Thống kê chung</h3>
									<div class="stat-item">
										<span class="stat-label">Điểm trung bình lớp:</span> <span
											class="stat-value">${classAverage}</span>
									</div>
									<div class="stat-item">
										<span class="stat-label">Điểm cao nhất:</span> <span
											class="stat-value">${highestScore}</span>
									</div>
									<div class="stat-item">
										<span class="stat-label">Điểm thấp nhất:</span> <span
											class="stat-value">${lowestScore}</span>
									</div>
									<div class="stat-item">
										<span class="stat-label">Tỷ lệ đạt:</span> <span
											class="stat-value">${passRate}%</span>
									</div>
								</div>

								<!-- Biểu đồ phân bố điểm -->
								<div class="statistics-box chart">
									<h3>Phân bố điểm</h3>
									<div class="chart-container">
										<canvas id="gradeDistributionChart"></canvas>
									</div>
								</div>
							</div>

							<!-- Bảng chi tiết điểm -->
							<div class="grade-table">
								<h3>Chi tiết điểm sinh viên</h3>
								<table>
									<thead>
										<tr>
											<th>Mã SV</th>
											<th>Tên SV</th>
											<th>Điểm chuyên cần</th>
											<th>Điểm giữa kỳ</th>
											<th>Điểm cuối kỳ</th>
											<th>Điểm tổng kết</th>
											<th>Điểm chữ</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="grade" items="${gradeList}">
											<tr>
												<td>${grade.studentCode}</td>
												<td>${grade.studentName}</td>
												<td>${grade.attendanceScore}</td>
												<td>${grade.midtermScore}</td>
												<td>${grade.finalExamScore}</td>
												<td>${grade.componentScore}</td>
												<td>${grade.gradeLetter}</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>

							<!-- Script vẽ biểu đồ - đặt trong cùng điều kiện có dữ liệu -->
							<script>
						        $(document).ready(function() {
						            // Đảm bảo DOM đã load xong
						            var canvas = document.getElementById('gradeDistributionChart');
						            if (canvas) {
						                var ctx = canvas.getContext('2d');

						                // Khởi tạo dữ liệu biểu đồ
						                var gradeDistributionData = {
						                    A: ${gradeDistribution['A'] != null ? gradeDistribution['A'] : 0},
						                    B: ${gradeDistribution['B'] != null ? gradeDistribution['B'] : 0},
						                    C: ${gradeDistribution['C'] != null ? gradeDistribution['C'] : 0},
						                    D: ${gradeDistribution['D'] != null ? gradeDistribution['D'] : 0},
						                    F: ${gradeDistribution['F'] != null ? gradeDistribution['F'] : 0}
						                };

						                // Tạo biểu đồ
						                new Chart(ctx, {
						                    type: 'pie',
						                    data: {
						                        labels: ['A', 'B', 'C', 'D', 'F'],
						                        datasets: [{
						                            data: Object.values(gradeDistributionData),
						                            backgroundColor: [
						                                '#4CAF50', // A - Xanh lá
						                                '#2196F3', // B - Xanh dương
						                                '#FFC107', // C - Vàng
						                                '#FF9800', // D - Cam
						                                '#F44336'  // F - Đỏ
						                            ]
						                        }]
						                    },
						                    options: {
						                        responsive: true,
						                        maintainAspectRatio: false,
						                        plugins: {
						                            legend: {
						                                position: 'right'
						                            },
						                            title: {
						                                display: true,
						                                text: 'Phân bố điểm theo loại'
						                            }
						                        }
						                    }
						                });
						            }
						        });
					    	</script>

						</c:if>
					</div>
				</c:when>
				<c:otherwise>
					<p>Bạn không có quyền truy cập chức năng này.</p>
				</c:otherwise>
			</c:choose>

		</div>
	</div>

	<!-- CSS styles -->
	<jsp:include page="dashboardCSS.jsp"></jsp:include>
	<script>
		//Xử lý sự kiện chọn lớp
		$(document).ready(function() {
		    $('#classSelect').on('change', function() {
		        $('#classSelectForm').submit();
		    });
		});
		$(document).ready(function() {
			alertify.set('notifier', 'position', 'top-right');

			// Lấy danh sách successMessages
			<%List<String> successMessages = (List<String>) request.getSession().getAttribute("successMessages");
request.getSession().removeAttribute("successMessages");%>
			<%if (successMessages != null && !successMessages.isEmpty()) {%>
				<%for (String message : successMessages) {%>
				alertify.success('<%=message%>');
				<%}%>
			<%}%>

			// Lấy danh sách errorMessages
			<%List<String> errorMessages = (List<String>) request.getSession().getAttribute("errorMessages");
request.getSession().removeAttribute("errorMessages");%>
			<%if (errorMessages != null && !errorMessages.isEmpty()) {%>
				<%for (String message : errorMessages) {%>
					alertify.error('<%=message%>');
			<%}%>

			<%}%>
		});
	</script>
</body>
</html>