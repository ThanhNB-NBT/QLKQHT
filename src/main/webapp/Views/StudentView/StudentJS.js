
$(document).on('click', '.edit-student', function () {
    const studentId = $(this).data('id');
    const firstName = $(this).data('first-name');
    const lastName = $(this).data('last-name');
    const dateOfBirth = $(this).data('dob');
    const email = $(this).data('email');
    const phone = $(this).data('phone');
    const address = $(this).data('address');
    const majorName = $(this).data('major-name');
    const departmentID = $(this).data('department-id');
	const studentCode = $(this).data('student-code');
	const avatar = $(this).data('avatar');
	const enrollmentYear = $(this).data('enrollment-year');

    // Điền thông tin vào các trường trong modal
    $('#editStudentId').val(studentId);
    $('#editFirstName').val(firstName);
    $('#editLastName').val(lastName);
    $('#editDateOfBirth').val(dateOfBirth);
    $('#editEmail').val(email);
    $('#editPhone').val(phone);
    $('#editAddress').val(address);
    $('#editMajorName').val(majorName);
    $('#editDepartmentID').val(departmentID);
    $('#editStudentCode').text(`${studentCode}`);
	$('#editAvatar').attr('src', avatar);
	$('#editEnrollmentYear').val(enrollmentYear);

    $('#edit_student').modal('show');
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


