<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="add_student_class" class="modal fade" role="dialog">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Thêm mới Học viên vào Lớp</h5>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <form action="StudentClassServlet" method="POST">
                <div class="modal-body">

                    <!-- Sinh viên -->
                    <div class="form-group">
                        <label for="studentID">Sinh viên</label>
                        <select class="form-control" id="studentID" name="studentID" required>
                            <c:forEach var="student" items="${students}">
                                <option value="${student.studentID}">
                                    ${student.firstName} ${student.lastName} (${student.studentCode})
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Lớp -->
                    <div class="form-group">
                        <label for="classID">Sinh viên</label>
                        <select class="form-control" id="classID" name="classID" required>
                            <c:forEach var="cls" items="${classes}">
                                <option value="${cls.classID}">
                                    ${cls.className}
                                </option>
                            </c:forEach>
                        </select>
                    </div>


                    <!-- Trạng thái -->
                    <div class="form-group">
                        <label for="status">Trạng thái</label>
                        <select class="form-control" id="status" name="status" onchange="toggleCustomStatus(this)">
                            <option value="Đang học">Đang học</option>
                            <option value="Bảo lưu">Bảo lưu</option>
                            <option value="Đã nghỉ học">Đã nghỉ học</option>
                            <option value="custom">Nhập trạng thái khác</option>
                        </select>
                        <input type="text" class="form-control mt-2 d-none" id="customStatus" name="status" placeholder="Nhập trạng thái khác">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary btn-lg" name="action" value="create">Thêm</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                </div>
            </form>
        </div>
    </div>
</div>
