
document.body.addEventListener('click', function(event) {
	if (event.target.classList.contains('edit-department')) {
		const departmentId = event.target.getAttribute('data-id');
		// Đưa departmentId vào form để có thể sửa
		document.getElementById('editDepartmentID').value = departmentId;
		// Đưa thông tin khoa vào các trường của modal chỉnh sửa
		const departmentName = event.target.getAttribute('data-name');
		const email = event.target.getAttribute('data-email');
		const phone = event.target.getAttribute('data-phone');

		document.getElementById('editDepartmentName').value = departmentName;
		document.getElementById('editEmail').value = email;
		document.getElementById('editPhone').value = phone;

		// Hiển thị modal chỉnh sửa
		$('#edit_department').modal('show');
	}
});

// Xử lý sự kiện khi nhấn nút 'delete'
document.body.addEventListener('click', function(event) {
	if (event.target.classList.contains('delete-department')) {
		const departmentId = event.target.getAttribute('data-id');
		document.getElementById('deleteDepartmentId').value = departmentId;
		$('#delete_department').modal('show'); // Hiển thị modal xóa
	}
});

// Xử lý submit form xóa qua AJAX
document.getElementById('deleteForm').addEventListener('submit', function(event) {
	event.preventDefault();
	const departmentId = document.getElementById('deleteDepartmentId').value;
	if (!departmentId) {
		console.error("Department ID is missing");
		return;
	}
	$.ajax({
		url: 'DepartmentServlet',
		type: 'POST',
		data: {
			departmentID: departmentId,
			action: 'delete'
		},
		success: function(response) {

			$('tr[data-department-id="' + departmentId + '"]').remove(); // Cách thay thế: xóa dòng
			$('#delete_department').modal('hide'); // Đóng modal
		},
		error: function(xhr, status, error) {
			console.error("Lỗi khi xóa khoa: " + error);
		}
	});
});