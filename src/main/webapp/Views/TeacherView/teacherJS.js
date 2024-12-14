// Lắng nghe sự kiện click trên nút sửa giáo viên
document.body.addEventListener('click', function (event) {
    if (event.target.classList.contains('edit-teacher')) {
        const teacherId = event.target.getAttribute('data-id');
        const firstName = event.target.getAttribute('data-first-name');
        const lastName = event.target.getAttribute('data-last-name');
        const email = event.target.getAttribute('data-email');
        const phone = event.target.getAttribute('data-phone');
        const departmentId = event.target.getAttribute('data-department-id');
        const office = event.target.getAttribute('data-office');
        const hireDate = event.target.getAttribute('data-hire-date');
        const avatar = event.target.getAttribute('data-avatar');
		const accountID = event.target.getAttribute('data-account-id');

        // Điền thông tin vào form chỉnh sửa
        document.getElementById('editTeacherID').value = teacherId;
        document.getElementById('editFirstName').value = firstName;
        document.getElementById('editLastName').value = lastName;
        document.getElementById('editEmail').value = email;
        document.getElementById('editPhone').value = phone;
        document.getElementById('editDepartmentID').value = departmentId;
        document.getElementById('editOffice').value = office;
        document.getElementById('editHireDate').value = hireDate;
		document.getElementById('editAccountID').value = accountID;

        // Gán lại avatar
        $('#editAvatar').attr('src', avatar);
        document.querySelector('[name="currentAvatar"]').value = avatar; // Lưu lại ảnh hiện tại trong input hidden

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
const emailField = document.getElementsByName('email')[0];
if (emailField) {
    emailField.addEventListener('input', function () {
        const email = this.value;
        const errorElement = document.getElementById('EmailError');
        const emailPattern = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}$/;

        if (!emailPattern.test(email)) {
            errorElement.style.display = 'block';
            this.style.borderColor = 'red';
        } else {
            errorElement.style.display = 'none';
            this.style.borderColor = 'green';
        }
    });
}


const phoneField = document.getElementsByName('phone')[0];
if (phoneField) {
    phoneField.addEventListener('input', function () {
        const phone = this.value;
        const errorElement = document.getElementById('PhoneError');
        const phonePattern = /^0[0-9]{9}$/;

        if (!phonePattern.test(phone)) {
            errorElement.style.display = 'block';
            this.style.borderColor = 'red';
        } else {
            errorElement.style.display = 'none';
            this.style.borderColor = 'green';
        }
    });
}
$(document).on('change', '.avatar-input', function (e) {
    const file = e.target.files[0]; // Lấy file người dùng chọn
    const reader = new FileReader();

    reader.onload = function (event) {
        // Lấy data URL của file và cập nhật ảnh
        const targetImgId = $(e.target).data('target-img'); // Lấy ID của thẻ img từ data attribute
        $('#' + targetImgId).attr('src', event.target.result); // Cập nhật ảnh
    };

    if (file) {
        reader.readAsDataURL(file); // Đọc file dưới dạng base64
    }
});
