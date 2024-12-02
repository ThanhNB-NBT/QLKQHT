<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="add_teacher" class="modal" role="dialog">
    <div class="modal-dialog modal-dialog-centered justify-content-center">
        <div class="modal-content modal-lg">
            <div class="modal-header">
                <h4 class="modal-title">Thêm giảng viên</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <form id="addTeacherForm" action="TeacherServlet" method="post" enctype="multipart/form-data" class="m-b-30">
                    <div class="row justify-content-center">
                        <!-- Họ và Tên -->
                        <div class="col-sm-6">
                            <div class="form-group form-focus">
                                <input id="firstName" name="firstName" type="text" class="form-control floating" required>
                                <label class="focus-label">Họ <span class="text-danger">*</span></label>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-group form-focus">
                                <input id="lastName" name="lastName" type="text" class="form-control floating" required>
                                <label class="focus-label">Tên <span class="text-danger">*</span></label>
                            </div>
                        </div>

                        <!-- Email -->
                        <div class="col-sm-12">
                            <div class="form-group form-focus">
                                <input id="email" name="email" type="email" class="form-control floating" required>
                                <label class="focus-label">Email <span class="text-danger">*</span><span id="EmailError"
									class="text-danger" style="display: none;">Email không
										hợp lệ!</span></label>

                            </div>
                        </div>

                        <!-- Số điện thoại -->
                        <div class="col-sm-12">
                            <div class="form-group form-focus">
                                <input id="phone" name="phone" type="text" class="form-control floating" pattern="0[0-9]{9}" title="Số điện thoại phải từ 10-11 chữ số" required>
                                <label class="focus-label">Số điện thoại <span class="text-danger">*</span><span id="PhoneError"
									class="text-danger" style="display: none;">Số điện thoại
										không hợp lệ!</span></label>
                            </div>
                        </div>

                        <!-- Khoa -->
                        <div class="col-sm-12">
                            <div class="form-group form-focus">
                                <select class="form-control" name="departmentID" required>
                                    <option value="" disabled selected>Chọn khoa</option>
                                    <c:forEach var="department" items="${departments}">
                                        <option value="${department.departmentID}">${department.departmentName}</option>
                                    </c:forEach>
                                </select>
                                <label class="focus-label">Khoa <span class="text-danger">*</span></label>
                            </div>
                        </div>

                        <!-- Văn phòng -->
                        <div class="col-sm-12">
                            <div class="form-group form-focus">
                                <input id="office" name="office" type="text" class="form-control floating">
                                <label class="focus-label">Văn phòng</label>
                            </div>
                        </div>

                        <!-- Ngày tuyển dụng -->
                        <div class="col-sm-12">
                            <div class="form-group form-focus">
                                <input id="hireDate" name="hireDate" type="date" class="form-control floating" required>
                                <label class="focus-label">Ngày tuyển dụng <span class="text-danger">*</span></label>
                            </div>
                        </div>

                        <!-- Avatar -->
                        <div class="col-sm-12">
                            <div class="form-group form-focus">
                                <input type="file" name="avatar" class="form-control-file">
                                <label class="focus-label">Ảnh đại diện</label>
                            </div>
                        </div>

                        <!-- Submit -->
                        <div class="col-sm-12">
                            <div class="m-t-20 text-center">
                                <button type="submit" class="btn btn-primary btn-lg" name="action" value="create">Thêm giảng viên</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
