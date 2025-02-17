<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div id="delete_class" class="modal" role="dialog">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content modal-md">
			<div class="modal-header">
				<h4 class="modal-title">Xóa học phần</h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<form id="deleteForm" method="post" action="ClassServlet">
				<div class="modal-body">
					<p>Bạn có chắc chắn muốn xóa không?</p>
					<input type="hidden" id="deleteClassId" name="classID">
					<input type="hidden" name="action" value="delete">
					<a href="#" class="btn btn-white" data-dismiss="modal">Đóng</a>
					<button type="submit" class="btn btn-danger">Xóa</button>
				</div>
			</form>
		</div>
	</div>
</div>