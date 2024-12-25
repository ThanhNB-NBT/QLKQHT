<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="edit_grade" class="modal fade" role="dialog">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Chỉnh sửa điểm sinh viên</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <form id="editGradeForm" action="GradeServlet" method="POST">
                    <input type="hidden" id="editGradeID" name="gradeID" />
                    <input type="hidden" id="editClassID" name="classID" />

                    <div class="form-group">
                        <label>Mã sinh viên:</label>
                        <span id="editStudentCode" class="font-weight-bold"></span>
                    </div>

                    <div class="form-group">
                        <label>Tên sinh viên:</label>
                        <span id="editStudentName" class="font-weight-bold"></span>
                    </div>

                    <div class="form-group">
                        <label for="editAttendanceScore">Điểm chuyên cần:</label>
                        <input type="number" class="form-control" id="editAttendanceScore" name="attendanceScore"
                               step="0.01" min="0" max="10" required />
                    </div>

                    <div class="form-group">
                        <label for="editMidtermScore">Điểm giữa kỳ:</label>
                        <input type="number" class="form-control" id="editMidtermScore" name="midtermScore"
                               step="0.01" min="0" max="10" required />
                    </div>

                    <div class="form-group">
                        <label for="editFinalScore">Điểm cuối kỳ:</label>
                        <input type="number" class="form-control" id="editFinalScore" name="finalExamScore"
                               step="0.01" min="0" max="10" required />
                    </div>

                    <div class="text-center">
                        <button type="submit" class="btn btn-primary" name="action" value="update">Cập nhật</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
