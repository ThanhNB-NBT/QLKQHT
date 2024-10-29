<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession"%>
<%@ page import="models.bean.Account"%>
<%@ page import="models.bean.Role"%>
<!DOCTYPE html>
<html>
<head>
    <title>Session Info</title>
</head>
<body>
    <h2>Session Information</h2>
    <%
        HttpSession session1 = request.getSession(false);
        if (session1 != null) {
            Account loggedInAccount = (Account) session1.getAttribute("loggedInUser");
            if (loggedInAccount != null) {
    %>
                <p>Account ID: <%= loggedInAccount.getAccountID() %></p>
                <p>Username: <%= loggedInAccount.getUsername() %></p>
                <p>Email: <%= loggedInAccount.getEmail() %></p>
                <p>RoleID: <%= loggedInAccount.getRole().getRoleID() %></p>
                <p>Role: <%= loggedInAccount.getRole() != null ? loggedInAccount.getRole().getRole() : "N/A" %></p>
    <%
            } else {
    %>
                <p>No user is logged in.</p>
    <%
            }
        } else {
    %>		<a href="login.jsp">Đăng nhập</a>
            <p>No session exists.</p>
    <%
        }
    %>
</body>
</html>
