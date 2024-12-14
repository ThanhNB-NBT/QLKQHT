<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="add_class" class="modal" role="dialog">
    <div class="modal-dialog modal-dialog-centered justify-content-center">
        <div class="modal-content modal-lg">
            <div class="modal-header">
                <h4 class="modal-title">Thêm lớp học</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <form id="addClassForm" action="ClassServlet" method="post" class="m-b-30">
                    <div class="row justify-content-center">
                        <!-- Mã khóa học -->
                        <div class="col-sm-12">
                            <div class="form-group form-focus">
                                <select class="form-control" name="courseID" required>
                                    <option value="" disabled selected>Chọn khóa học</option>
                                    <c:forEach var="course" items="${courses}">
                                        <option value="${course.courseID}">${course.courseName}</option>
                                    </c:forEach>
                                </select>
                                <label class="focus-label">Học phần <span class="text-danger">*</span></label>
                            </div>
                        </div>

                        <!-- Mã giáo viên -->
                        <div class="col-sm-12">
                            <div class="form-group form-focus">
                                <select class="form-control" name="teacherID" required>
                                    <option value="" disabled selected>Chọn giáo viên</option>
                                    <c:forEach var="teacher" items="${teachers}">
                                        <option value="${teacher.teacherID}">${teacher.firstName} ${teacher.lastName}</option>
                                    </c:forEach>
                                </select>
                                <label class="focus-label">Giảng viên <span class="text-danger">*</span></label>
                            </div>
                        </div>

                        <!-- Thời gian học -->
                        <div class="col-sm-6">
                            <div class="form-group form-focus">
                                <input id="classTime" name="classTime" type="text" class="form-control floating" required>
                                <label class="focus-label">Thời gian học <span class="text-danger">*</span></label>
                            </div>
                        </div>

                        <!-- Phòng học -->
                        <div class="col-sm-6">
                            <div class="form-group form-focus">
                                <input name="room" type="text" class="form-control floating" required>
                                <label class="focus-label">Phòng học <span class="text-danger">*</span></label>
                            </div>
                        </div>

                        <!-- Học kỳ -->
                        <div class="col-sm-4">
                            <div class="form-group form-focus">
                                <input name="semester" type="text" class="form-control floating" required>
                                <label class="focus-label">Học kỳ <span class="text-danger">*</span></label>
                            </div>
                        </div>

                        <!-- Số lượng sinh viên tối đa -->
                        <div class="col-sm-4">
                            <div class="form-group form-focus">
                                <input name="maxStudents" type="number" class="form-control floating" required min="1">
                                <label class="focus-label">Số lượng <span class="text-danger">*</span></label>
                            </div>
                        </div>

                        <!-- Tổng số buổi học -->
                        <div class="col-sm-4">
                            <div class="form-group form-focus">
                                <input name="totalLessons" type="number" class="form-control floating" required min="1">
                                <label class="focus-label">Số tiết <span class="text-danger">*</span></label>
                            </div>
                        </div>

                        <!-- Ngày bắt đầu -->
                        <div class="col-sm-6">
                            <div class="form-group form-focus">
                                <input name="startDate" type="date" class="form-control floating" required>
                                <label class="focus-label">Ngày bắt đầu <span class="text-danger">*</span></label>
                            </div>
                        </div>

                        <!-- Ngày kết thúc -->
                        <div class="col-sm-6">
                            <div class="form-group form-focus">
                                <input name="endDate" type="date" class="form-control floating" required>
                                <label class="focus-label">Ngày kết thúc <span class="text-danger">*</span></label>
                            </div>
                        </div>

                        <!-- Loại lớp -->
                        <div class="col-sm-6">
                            <div class="form-group form-focus">
                                <select class="form-control" name="classType" required>
                                	<option value="Lý thuyết" selected> Lý thuyết </option>
                                	<option value="Thực hành"> Thực hành </option>
                                </select>
                                <label class="focus-label">Loại lớp <span class="text-danger">*</span></label>
                            </div>
                        </div>

                         <!-- Trạng thái -->
                        <div class="col-sm-6">
                            <div class="form-group form-focus">
                                <select class="form-control" name="status" required>
                                	<option value="Đang học" selected> Đang học </option>
                                	<option value="Đã xong"> Đã xong </option>
                                	<option value="Đã hủy"> Đã hủy </option>
                                </select>
                                <label class="focus-label">Trạng thái <span class="text-danger">*</span></label>
                            </div>
                        </div>

                        <!-- Lớp cha (nếu có) -->
                        <div class="col-sm-12">
                            <div class="form-group form-focus">
                                <select class="form-control" name="parentClassID">
                                    <option value="" disabled selected>Chọn lớp lý thuyết</option>
                                    <c:forEach var="cls" items="${classes}">
                                        <option value="${cls.classID}">${cls.className}</option>
                                    </c:forEach>
                                </select>
                                <label class="focus-label">Lớp lý thuyết<span class="text-danger">*</span></label>
                            </div>
                        </div>

                        <!-- Submit -->
                        <div class="col-sm-8">
                            <div class="m-t-20 text-center">
                                <button type="submit" class="btn btn-primary btn-lg" name="action" value="create">Thêm lớp học</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
