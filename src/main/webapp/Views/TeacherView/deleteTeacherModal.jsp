<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div id="delete_teacher" class="modal" role="dialog">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content modal-md">
			<div class="modal-header">
				<h4 class="modal-title">Xóa giảng viên</h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<form id="deleteTeacherForm" method="post" action="TeacherServlet">
				<div class="modal-body">
					<p>Bạn có chắc chắn muốn xóa giảng viên này không?</p>
					<!-- Truyền ID giảng viên vào input hidden -->
					<input type="hidden" id="deleteTeacherId" name="teacherID">
					<!-- Action để xác định là hành động xóa -->
					<input type="hidden" name="action" value="delete">
					<a href="#" class="btn btn-white" data-dismiss="modal">Đóng</a>
					<button type="submit" class="btn btn-danger">Xóa</button>
				</div>
			</form>
		</div>
	</div>
</div>
