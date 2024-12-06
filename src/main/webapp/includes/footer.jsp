<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="common.AlertManager" %>
<%@ page import="java.util.List" %>

<script src="assets/js/jquery-3.6.0.min.js"></script>
<script src="assets/js/bootstrap.bundle.min.js"></script>
<script src="assets/js/jquery.slimscroll.js"></script>
<script src="assets/js/jquery.dataTables.min.js"></script>
<script src="assets/js/dataTables.bootstrap4.min.js"></script>
<script src="assets/js/select2.min.js"></script>
<script src="assets/js/moment.min.js"></script>
<script src="assets/plugins/datetimepicker/js/tempusdominus-bootstrap-4.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/alertify.min.js"></script>
<script src="assets/js/app.js"></script>

<script>
$(document).ready(function() {
    alertify.set('notifier', 'position', 'top-right');

    // Lấy danh sách successMessages
    <%
        List<String> successMessages = (List<String>) request.getSession().getAttribute("successMessages");
        request.getSession().removeAttribute("successMessages");
    %>
    <% if (successMessages != null && !successMessages.isEmpty()) { %>
        <% for (String message : successMessages) { %>
            alertify.success('<%= message %>');
        <% } %>
    <% } %>

    // Lấy danh sách errorMessages
    <%
        List<String> errorMessages = (List<String>) request.getSession().getAttribute("errorMessages");
        request.getSession().removeAttribute("errorMessages");
    %>
    <% if (errorMessages != null && !errorMessages.isEmpty()) { %>
        <% for (String message : errorMessages) { %>
            alertify.error('<%= message %>');
        <% } %>
    <% } %>
});
</script>

