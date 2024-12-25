<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="edit_student_class" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="TeacherStudentClassServlet" method="POST">
                <div class="modal-header">
                    <h5 class="modal-title">Chỉnh sửa Sinh viên - Lớp học</h5>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                <div class="modal-body">
                    <!-- ID ẩn để gửi đến servlet -->
                    <input type="hidden" name="studentClassID" id="editStudentClassID">

                    <div class="form-group">
                        <label>Tên sinh viên:</label>
                        <p id="editStudentName" class="form-control-plaintext"></p>
                    </div>
                    <div class="form-group">
                        <label>Mã sinh viên:</label>
                        <p id="editStudentCode" class="form-control-plaintext"></p>
                    </div>
                    <div class="form-group">
                        <label>Tên lớp học:</label>
                        <p id="editClassName" class="form-control-plaintext"></p>
                    </div>
                    <div class="form-group">
                        <label>Trạng thái:</label>
                        <select name="status" id="editStatus" class="form-control">
                            <option value="Đang học">Đang học</option>
                            <option value="Bảo lưu">Bảo lưu</option>
                            <option value="Đã nghỉ học">Đã nghỉ học</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary" name="action" value="update">Lưu thay đổi</button>
                </div>
            </form>
        </div>
    </div>
</div>
