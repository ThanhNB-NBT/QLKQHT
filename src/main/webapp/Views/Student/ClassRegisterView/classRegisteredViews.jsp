<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sách lớp học đã đăng ký</title>
    <jsp:include page="../../../includes/resources.jsp"></jsp:include>
    <jsp:include page="classRegisterCSS.jsp"></jsp:include>
</head>
<body>
    <div class="main-wrapper">
        <jsp:include page="../../../includes/header.jsp"></jsp:include>
        <div class="page-wrapper">
            <div class="content container-fluid">

                <!-- Button to open modal -->
                <div class="row mb-4">
                    <div class="col-12 text-right">
                        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#availableClassesModal">
                            <i class="fas fa-plus"></i> Đăng ký học
                        </button>
                    </div>
                </div>

                <!-- Registered Classes Section -->
                <div class="row">
                    <div class="col-md-12">
                        <div class="card">
                            <div class="card-header">
                                <h4 class="card-title">Các lớp học đã đăng ký</h4>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table table-bordered">
                                        <thead>
                                            <tr>
                                                <th>STT</th>
                                                <th>Tên lớp</th>
                                                <th>Tên môn học</th>
                                                <th>Số tín chỉ</th>
                                                <th>Giảng viên</th>
                                                <th>Thời gian học</th>
                                                <th>Phòng học</th>
                                                <th>Học kỳ</th>
                                                <th>Trạng thái</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${not empty registeredClasses}">
                                                    <c:forEach var="cls" items="${registeredClasses}" varStatus="loop">
                                                        <tr>
                                                            <td>${loop.index + 1}</td>
                                                            <td>${cls.className}</td>
                                                            <td>${cls.courseName}</td>
                                                            <td>${cls.credits}</td>
                                                            <td>${cls.teacherName}</td>
                                                            <td>${cls.classTime}</td>
                                                            <td>${cls.room}</td>
                                                            <td>${cls.semester}</td>
                                                            <td>
                                                                <span class="badge badge-success">${cls.status}</span>
                                                            </td>
                                                            <td>
                                                                <form action="ClassRegisterServlet" method="post" style="display: inline;">
                                                                    <input type="hidden" name="action" value="withdraw">
                                                                    <input type="hidden" name="studentClassID" value="${cls.studentClassID}">
                                                                    <button type="submit" class="btn btn-danger btn-sm"
                                                                            onclick="return confirm('Bạn có chắc muốn hủy đăng ký lớp học này?')">
                                                                        <i class="fas fa-trash"></i> Hủy đăng ký
                                                                    </button>
                                                                </form>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    <tr>
                                                        <td colspan="10" class="text-center">Chưa đăng ký lớp học nào</td>
                                                    </tr>
                                                </c:otherwise>
                                            </c:choose>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="classRegisterModal.jsp"></jsp:include>

    <jsp:include page="../../../includes/footer.jsp"></jsp:include>
</body>
</html>