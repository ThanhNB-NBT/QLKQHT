<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="header-outer">
    <div class="header">

        <a id="mobile_btn" class="mobile_btn float-left" href="#sidebar">
            <i class="fas fa-bars" aria-hidden="true"></i>
        </a>

        <ul class="nav user-menu float-right">
            <li class="nav-item dropdown has-arrow">
                <a href="#" data-toggle="dropdown">
                    <span class="user-img">
                        <!-- Kiểm tra avatar nếu null thì thay thế bằng ảnh mặc định -->
                        <c:choose>
                            <c:when test="${empty sessionScope.loggedInUser.avatar}">
                                <img class="rounded-circle" src="${pageContext.request.contextPath}/assets/img/user.jpg" width="30">
                            </c:when>
                            <c:otherwise>
                                <img class="rounded-circle" src="${sessionScope.loggedInUser.avatar}" width="30">
                            </c:otherwise>
                        </c:choose>
                        <span class="status online"></span>
                    </span>
                    <!-- Hiển thị tên theo vai trò -->
                    <span>
                        <c:choose>
                            <c:when test="${sessionScope.loggedInUser.role.roleID == 1}">
                                ${sessionScope.loggedInUser.username}
                            </c:when>
                            <c:when test="${sessionScope.loggedInUser.role.roleID == 2}">
                                ${sessionScope.loggedInUser.teacherName}
                            </c:when>
                            <c:when test="${sessionScope.loggedInUser.role.roleID == 3}">
                                ${sessionScope.loggedInUser.studentName}
                            </c:when>
                        </c:choose>
                    </span>
                </a>
                <div class="dropdown-menu">
                    <a class="dropdown-item" href="profile.html">Thông tin cá nhân</a>
                    <a class="dropdown-item" href="settings.html">Cài đặt</a>
                    <a class="dropdown-item" href="LogoutServlet">Đăng xuất</a>
                </div>
            </li>
        </ul>
        <div class="dropdown mobile-user-menu float-right">
            <a class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                <i class="fas fa-ellipsis-v"></i>
            </a>
            <div class="dropdown-menu dropdown-menu-right">
                <a class="dropdown-item" href="profile.html">Thông tin cá nhân</a>
                <a class="dropdown-item" href="settings.html">Cài đặt</a>
                <a class="dropdown-item" href="LogoutServlet">Đăng xuất</a>
            </div>
        </div>
    </div>
</div>
<jsp:include page="sidebar.jsp"></jsp:include>
