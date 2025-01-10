<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- Available Classes Modal -->
<div class="modal fade" id="availableClassesModal" tabindex="-1"
	role="dialog" aria-hidden="true">
	<div class="modal-dialog modal-xl">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title">Danh sách lớp học có thể đăng ký</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="table-responsive">
					<table class="table table-bordered">
						<thead>
							<tr>
								<th>STT</th>
								<th>Tên lớp</th>
								<th>Tên môn học</th>
								<th>Số tín chỉ</th>
								<th>Giảng viên</th>
								<th>Thời gian học</th>
								<th>Phòng học</th>
								<th>Học kỳ</th>
								<th>Sĩ số</th>
								<th>Thao tác</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${not empty availableClasses}">
									<c:forEach var="clss" items="${availableClasses}"
										varStatus="loop">
										<tr>
											<td>${loop.index + 1}</td>
											<td>${clss.className}</td>
											<td>${clss.courseName}</td>
											<td>${clss.credits}</td>
											<td>${clss.teacherName}</td>
											<td>${clss.classTime}</td>
											<td>${clss.room}</td>
											<td>${clss.semester}</td>
											<td>${clss.registeredStudents}/${clss.maxStudents}</td>
											<td>
												<form action="ClassRegisterServlet" method="post"
													style="display: inline;">
													<input type="hidden" name="action" value="register">
													<input type="hidden" name="classID"
														value="${clss.classID}">
													<button type="submit" class="btn btn-primary btn-sm">
														<i class="fas fa-plus"></i> Đăng ký
													</button>
												</form>
											</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="10" class="text-center">Không có lớp học nào
											có thể đăng ký</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
			</div>
		</div>
	</div>
</div>

<!-- Script for DataTable -->
<script>
	$(document).ready(function() {
		$('#availableClassesTable').DataTable({
			"language" : {
				"lengthMenu" : "Hiển thị _MENU_ dòng mỗi trang",
				"zeroRecords" : "Không tìm thấy dữ liệu",
				"info" : "Hiển thị trang _PAGE_ của _PAGES_",
				"infoEmpty" : "Không có dữ liệu",
				"infoFiltered" : "(lọc từ _MAX_ dòng dữ liệu)",
				"search" : "Tìm kiếm:",
				"paginate" : {
					"first" : "Đầu",
					"last" : "Cuối",
					"next" : "Sau",
					"previous" : "Trước"
				}
			},
			"pageLength" : 5,
			"ordering" : true,
			"responsive" : true,
			"searching" : true,
			"columnDefs" : [ {
				"orderable" : false,
				"targets" : 9
			} // Disable sorting for action column
			]
		});
	});

	// Hiển thị thông báo sau khi đăng ký
	<c:if test="${not empty message}">
	$(document).ready(function() {
		$.toast({
			heading : '${messageType}',
			text : '${message}',
			position : 'top-right',
			loaderBg : '#ff6849',
			icon : '${messageType}',
			hideAfter : 3500,
			stack : 6
		});
	});
	</c:if>
</script>

