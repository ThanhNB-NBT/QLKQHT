<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div id="delete_student" class="modal" role="dialog">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content modal-md">
            <div class="modal-header">
                <h4 class="modal-title">Xóa sinh viên</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <form id="deleteStudentForm" method="post" action="StudentServlet">
                <div class="modal-body">
                    <p>Bạn có chắc chắn muốn xóa sinh viên này không?</p>
                    <!-- Truyền ID sinh viên vào input hidden -->
                    <input type="hidden" id="deleteStudentId" name="studentID">
                    <!-- Action để xác định là hành động xóa -->
                    <input type="hidden" name="action" value="delete">
                    <a href="#" class="btn btn-white" data-dismiss="modal">Đóng</a>
                    <button type="submit" class="btn btn-danger">Xóa</button>
                </div>
            </form>
        </div>
    </div>
</div>
