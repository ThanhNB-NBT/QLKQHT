
document.body.addEventListener('click', function (event) {
    if (event.target.classList.contains('edit-student')) {
        const studentId = event.target.getAttribute('data-id');
        const firstName = event.target.getAttribute('data-first-name');
        const lastName = event.target.getAttribute('data-last-name');
        const dateOfBirth = event.target.getAttribute('data-dob');
        const email = event.target.getAttribute('data-email');
        const phone = event.target.getAttribute('data-phone');
        const address = event.target.getAttribute('data-address');
        const majorName = event.target.getAttribute('data-major-name');
        const departmentID = event.target.getAttribute('data-department-id');
        const studentCode = event.target.getAttribute('data-student-code');
        const avatar = event.target.getAttribute('data-avatar');
        const enrollmentYear = event.target.getAttribute('data-enrollment-year');
        const accountID = event.target.getAttribute('data-account-id');

        // Điền thông tin vào form chỉnh sửa
        document.getElementById('editStudentId').value = studentId;
        document.getElementById('editFirstName').value = firstName;
        document.getElementById('editLastName').value = lastName;
        document.getElementById('editDateOfBirth').value = dateOfBirth;
        document.getElementById('editEmail').value = email;
        document.getElementById('editPhone').value = phone;
        document.getElementById('editAddress').value = address;
        document.getElementById('editMajorName').value = majorName;
        document.getElementById('editDepartmentID').value = departmentID;
        document.getElementById('editStudentCode').textContent = studentCode;
        document.getElementById('editEnrollmentYearShow').textContent = enrollmentYear;
		document.getElementById('editEnrollmentYear').value = enrollmentYear;
        document.getElementById('editAccountID').value = accountID;

        // Gán lại avatar
        $('#editAvatar').attr('src', avatar);
        document.querySelector('[name="currentAvatar"]').value = avatar; // Lưu lại ảnh hiện tại trong input hidden

        // Hiển thị modal sửa student
        $('#edit_student').modal('show');
    }
});



$(document).on('click', '.delete-student', function () {
    const studentId = $(this).data('id');

    $('#deleteStudentId').val(studentId);

    $('#delete_student').modal('show');
});

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


document.getElementById('dateOfBirth').addEventListener('input', function () {
    const dob = new Date(this.value);
    const today = new Date();

    // Tính khoảng cách tuổi
    const age = today.getFullYear() - dob.getFullYear();
    const monthDiff = today.getMonth() - dob.getMonth();
    const dayDiff = today.getDate() - dob.getDate();

    // Kiểm tra nếu tuổi nhỏ hơn 17 hoặc chưa đủ năm
    const isUnderage = age < 17 || (age === 17 && (monthDiff < 0 || (monthDiff === 0 && dayDiff < 0)));

    const errorElement = document.getElementById('dobError');
    if (isUnderage) {
        errorElement.style.display = 'block'; // Hiển thị thông báo lỗi
        this.style.borderColor = 'red'; // Đổi màu viền
    } else {
        errorElement.style.display = 'none'; // Ẩn thông báo lỗi
        this.style.borderColor = 'green'; // Đổi màu viền
    }
});

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


