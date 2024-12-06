// Lắng nghe sự kiện click trên nút sửa giáo viên
document.body.addEventListener('click', function (event) {
	if (event.target.classList.contains('edit-teacher')) {
		const teacherId = event.target.getAttribute('data-id');

		// Lấy thông tin từ thuộc tính data- và điền vào các trường input của modal
		const firstName = event.target.getAttribute('data-first-name');
		const lastName = event.target.getAttribute('data-last-name');
		const email = event.target.getAttribute('data-email');
		const phone = event.target.getAttribute('data-phone');
		const departmentId = event.target.getAttribute('data-department-id');
		const office = event.target.getAttribute('data-office');
		const hireDate = event.target.getAttribute('data-hire-date');

		// Điền thông tin vào form chỉnh sửa
		document.getElementById('editTeacherID').value = teacherId;
		document.getElementById('editFirstName').value = firstName;
		document.getElementById('editLastName').value = lastName;
		document.getElementById('editEmail').value = email;
		document.getElementById('editPhone').value = phone;
		document.getElementById('editDepartmentID').value = departmentId;
		document.getElementById('editOffice').value = office;
		document.getElementById('editHireDate').value = hireDate;

		// Hiển thị modal sửa giáo viên
		$('#edit_teacher').modal('show');
	}
});

// Lắng nghe sự kiện click trên nút xóa giáo viên
$(document).on('click', '.delete-teacher', function () {
	const teacherId = $(this).data('id');

	// Điền ID giáo viên vào input hidden trong modal xóa
	$('#deleteTeacherId').val(teacherId);

	// Hiển thị modal xóa giáo viên
	$('#delete_teacher').modal('show');
});

// Kiểm tra định dạng email khi người dùng nhập
document.getElementByName('email').addEventListener('input', function () {
	const email = this.value;
	const errorElement = document.getElementById('EmailError');

	// Biểu thức regex kiểm tra định dạng email
	const emailPattern = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}$/;
	if (!emailPattern.test(email)) {
		errorElement.style.display = 'block'; // Hiển thị thông báo lỗi
		this.style.borderColor = 'red'; // Đổi màu viền
	} else {
		errorElement.style.display = 'none'; // Ẩn thông báo lỗi
		this.style.borderColor = 'green'; // Đổi màu viền
	}
});

// Kiểm tra định dạng số điện thoại khi người dùng nhập
document.getElementByName('phone').addEventListener('input', function () {
	const phone = this.value;
	const errorElement = document.getElementById('PhoneError');

	// Biểu thức regex kiểm tra số điện thoại
	const phonePattern = /^0[0-9]{9}$/; // Số bắt đầu bằng 0 và có 10 chữ số
	if (!phonePattern.test(phone)) {
		errorElement.style.display = 'block'; // Hiển thị thông báo lỗi
		this.style.borderColor = 'red'; // Đổi màu viền
	} else {
		errorElement.style.display = 'none'; // Ẩn thông báo lỗi
		this.style.borderColor = 'green'; // Đổi màu viền
	}
});
