<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="edit_class" class="modal" role="dialog">
    <div class="modal-dialog modal-dialog-centered justify-content-center">
        <div class="modal-content modal-lg">
            <div class="modal-header">
                <h4 class="modal-title">Sửa thông tin lớp học</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <form id="editClassForm" action="ClassServlet" method="post" class="m-b-30">
                    <div class="row justify-content-center">
                        <!-- ID Lớp học (ẩn) -->
                        <input type="hidden" id="editClassID" name="classID"/>
						<input type="hidden" id="editSemester" name="semester" />
						<input type="hidden" id="editCourseID" name="courseID" />
						<input type="hidden" id="editStartDate" name="startDate" />
						<input type="hidden" id="editEndDate" name="endDate" />
						<input type="hidden" id="editClassName" name="className" />

                        <div class="col-sm-12">
                            <div class="form-group">
                                <label>Tên lớp học:</label> <span id="editClassNameText" class="font-weight-bold"></span>
                            </div>
                        </div>

                        <div class="col-sm-6">
                            <div class="form-group">
                                <label>Ngày bắt đầu:</label> <span id="editStartDateText" class="font-weight-bold"></span>
                            </div>
                        </div>

                        <div class="col-sm-6">
                            <div class="form-group">
                                <label>Ngày kết thúc:</label> <span id="editEndDateText" class="font-weight-bold"></span>
                            </div>
                        </div>

                        <div class="col-sm-6">
                            <div class="form-group">
                                <label>Loại lớp:</label> <span id="editClassType" class="font-weight-bold"></span>
                            </div>
                        </div>

                        <!-- Học kỳ (hiển thị và input ẩn) -->

                        <div class="col-sm-5">
                            <div class="form-group">
                                <label>Học kỳ:</label> <span id="editSemesterText" class="font-weight-bold"></span>
                            </div>
                        </div>

                        <!-- Giáo viên (Có thể sửa) -->
                        <div class="col-sm-12">
                            <div class="form-group form-focus">
                                <select id="editTeacherID" class="form-control" name="teacherID">
                                    <option value="" disabled>Chọn giáo viên</option>
                                    <c:forEach var="teacher" items="${teachers}">
                                        <option value="${teacher.teacherID}">
                                            ${teacher.firstName} ${teacher.lastName}
                                        </option>
                                    </c:forEach>
                                </select>
                                <label class="focus-label">Giảng viên <span class="text-danger">*</span></label>
                            </div>
                        </div>

                        <div class="col-sm-6">
                            <div class="form-group form-focus">
                                <input id="editClassTime" name="classTime" type="text" class="form-control floating">
                                <label class="focus-label">Thời gian học </label>
                            </div>
                        </div>

                        <div class="col-sm-6">
                            <div class="form-group form-focus">
                                <input id="editRoom" name="room" type="text" class="form-control floating">
                                <label class="focus-label">Phòng học </label>
                            </div>
                        </div>

						<div class="col-sm-12">
                            <div class="form-group form-focus">
                                <select class="form-control" id="editStatus" name="status" required>
                                    <option value="Hoạt động">Hoạt động</option>
                                    <option value="Tạm ngừng">Tạm ngừng</option>
                                </select>
                                <label class="focus-label">Trạng thái</label>
                            </div>
                        </div>

                        <div class="col-sm-6">
                            <div class="form-group form-focus">
                                <input id="editMaxStudents" name="maxStudents" type="number" class="form-control floating" min="1">
                                <label class="focus-label">Số lượng</label>
                            </div>
                        </div>

                        <div class="col-sm-6">
                            <div class="form-group form-focus">
                                <input id="editTotalLessions" name="totalLessions" type="number" class="form-control floating"  min="1">
                                <label class="focus-label">Số tiết</label>
                            </div>
                        </div>

                        <div class="col-sm-8 offset-sm-2">
                            <div class="m-t-20 text-center">
                                <button type="submit" class="btn btn-primary btn-lg" name="action" value="update">Lưu thay đổi</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
