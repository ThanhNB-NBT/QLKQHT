<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script src="assets/js/jquery-3.6.0.min.js"></script>
<script src="assets/js/bootstrap.bundle.min.js"></script>
<script src="assets/js/jquery.slimscroll.js"></script>
<script src="assets/js/jquery.dataTables.min.js"></script>
<script src="assets/js/dataTables.bootstrap4.min.js"></script>
<script src="assets/js/select2.min.js"></script>
<script src="assets/js/moment.min.js"></script>
<script
	src="assets/plugins/datetimepicker/js/tempusdominus-bootstrap-4.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/alertify.min.js"></script>
<script src="assets/js/app.js"></script>
<script>
	$(document).ready(function() {
		alertify.set('notifier', 'position', 'top-right');
		
		<%
			String message = (String) session.getAttribute("message");
			if (message != null) {
		%>
			alertify.success('<%= message %>');
			<%
				session.removeAttribute("message");
			}
		%>

		<%
		String error = (String) session.getAttribute("error");
		if (error != null) {
		%>
		alertify.error('<%= error %>');
		<%
		session.removeAttribute("error");
		}
		%>

		<%
		String errorModal = (String) session.getAttribute("errorModal");
		if (errorModal != null) {
		%>
		alertify.error('<%=errorModal%>');
		<%
		session.removeAttribute("errorModal");
		}
		%>
	});
</script>
