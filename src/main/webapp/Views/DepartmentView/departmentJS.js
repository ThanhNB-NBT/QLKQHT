
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

$(document).on('click', '.delete-department', function() {
	var departmentId = $(this).data('id');

	$('#deleteDepartmentId').val(departmentId);

	// Hiển thị modal
	$('#delete_department').modal('show');
});

document.getElementById("email").addEventListener("input", function() {
	var email = this.value;
	var errorElement = document.getElementById("email-error");

	// Kiểm tra định dạng email
	var emailPattern = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}$/;
	if (!emailPattern.test(email)) {
		errorElement.style.display = "block"; // Hiển thị thông báo lỗi
	} else {
		errorElement.style.display = "none"; // Ẩn thông báo lỗi nếu email hợp lệ
	}
});

