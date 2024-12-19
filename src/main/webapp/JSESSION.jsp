<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="models.bean.Account" %>
<%@ page import="models.bean.Role" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Session Info</title>
</head>
<body>
    <h2>Session Information</h2>

    <c:choose>
        <c:when test="${not empty sessionScope.loggedInUser}">
            <p><strong>Account Details:</strong></p>
            <p>Account ID: ${sessionScope.loggedInUser.accountID}</p>
            <p>Username: ${sessionScope.loggedInUser.username}</p>
            <p>Email: ${sessionScope.loggedInUser.email}</p>
            <p>Role ID: ${sessionScope.loggedInUser.role.roleID}</p>
            <p>Role: ${sessionScope.loggedInUser.role.role}</p>
            <p>Teacher ID: ${sessionScope.loggedInUser.teacherID != null ? sessionScope.loggedInUser.teacherID : 'N/A'}</p>
            <p>Student ID: ${sessionScope.loggedInUser.studentID != null ? sessionScope.loggedInUser.studentID : 'N/A'}</p>
            <a href="LogoutServlet">Logout</a>
        </c:when>
        <c:otherwise>
            <p>No user is logged in.</p>
            <a href="login.jsp">Login</a>
        </c:otherwise>
    </c:choose>

</body>
</html>
