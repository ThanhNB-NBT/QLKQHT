<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div id="edit_grade_review" class="modal fade" role="dialog">
	<div class="modal-dialog modal-dialog-centered modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">Chỉnh sửa điểm sinh viên</h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<div class="modal-body">
				<form id="editGradeForm" action="GradeServlet" method="POST">
					<input type="hidden" id="editGradeID" name="gradeID" /> <input
						type="hidden" id="editClassID" name="classID" />

					<div class="form-group">
						<label>Mã sinh viên:</label> <span id="editStudentCode"
							class="font-weight-bold"></span>
					</div>

					<div class="form-group">
						<label>Tên sinh viên:</label> <span id="editStudentName"
							class="font-weight-bold"></span>
					</div>

					<div class="form-group">
						<label for="editAttendanceScore">Điểm chuyên cần:</label> <span
							id="editAttendanceScore" class="font-weight-bold"></span>
					</div>

					<div class="form-group">
						<label for="editMidtermScore">Điểm giữa kỳ:</label> <span
							id="editMidtermScore" class="font-weight-bold"></span>
					</div>

					<div class="form-group">
						<label for="editFinalScore">Điểm cuối kỳ:</label> <span
							id="editFinalScore" class="font-weight-bold"></span>
					</div>

					<div class="col-sm-12">
						<div class="form-group form-focus">
							<select id="editGradeStatus" class="form-control" name="status"
								required>
								<option value="" disabled selected>Duyệt điểm</option>
								<option value="1">Đã duyệt</option>
								<option value="2">Từ chối</option>
								<option value="3">Đã sửa</option>
							</select> <label class="focus-label">Trạng thái<span
								class="text-danger">*</span></label>
						</div>
					</div>

					<div class="col-sm-12">
                            <div class="form-group form-focus">
                                <input id="editGradeComment" name="comment" type="text" class="form-control floating">
                                <label class="focus-label">Ghi chú </label>
                            </div>
                        </div>

					<div class="text-center">
						<button type="submit" class="btn btn-primary" name="action"
							value="review">Duyệt</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
