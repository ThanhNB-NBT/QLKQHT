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

<script src="assets/js/app.js"></script>
<script>
	$(document).ready(function() {
		// Kiểm tra xem có thông báo nào không
		var successMessage = $('#successMessage');
		var errorMessage = $('#errorMessage');

		if (successMessage.length) {
			// Hiển thị thông báo
			successMessage.show();

			// Ẩn thông báo sau 3 giây
			setTimeout(function() {
				successMessage.fadeOut();
			}, 3000);
		}

		if (errorMessage.length) {
			// Hiển thị thông báo
			errorMessage.show();

			// Ẩn thông báo sau 3 giây
			setTimeout(function() {
				errorMessage.fadeOut();
			}, 3000);
		}
	});
</script>