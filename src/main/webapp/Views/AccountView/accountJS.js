$(document).on('click', '.edit-account', function () {
		const accountID = $(this).data('id');
		const username = $(this).data('username');
		const email = $(this).data('email');
		const role = $(this).data('role');
		const roleID = $(this).data('role-id');
		const avatar = $(this).data('avatar');

		$('#editAccountID').val(accountID);
		$('#editName').val(username);
		$('#editEmail').val(email);
		$('#editRole').val(role);
		$('#editRoleID').val(roleID);
		$('#editAvatar').val(avatar);

		$('#edit_account').modal('show');
});

$(document).on('click', '.delete-account', function() {
	var accountId = $(this).data('id');

	$('#deleteAccountId').val(accountId);

	$('#delete_account').modal('show');
});

function checkPasswordMatch(passwordFieldId, confirmPasswordFieldId, warningTextId, event = null) {
	const password = document.getElementById(passwordFieldId).value;
	const confirmPassword = document.getElementById(confirmPasswordFieldId).value;
	const warningText = document.getElementById(warningTextId);

	// Chỉ kiểm tra khi có ít nhất một trường được điền
	if (password.length > 0 || confirmPassword.length > 0) {
		if (password !== confirmPassword) {
			warningText.style.display = "block";
			document.getElementById(confirmPasswordFieldId).value = ""; // Xóa nội dung ô nhập xác nhận mật khẩu
			if (event) event.preventDefault();
		} else {
			warningText.style.display = "none";
		}
	} else {
		// Nếu cả hai trường đều trống thì ẩn cảnh báo và cho phép submit
		warningText.style.display = "none";
	}
}

// Tạo tài khoản - Gắn kiểm tra mật khẩu khi submit
document.getElementById("addAccountForm").addEventListener("submit", function(event) {
	checkPasswordMatch("password", "confirmPassword", "passwordMismatchWarning", event);
});

// Cập nhật tài khoản - Gắn kiểm tra mật khẩu khi submit
document.getElementById("editAccountForm").addEventListener("submit", function(event) {
	checkPasswordMatch("editPassword", "editConfirmPassword", "editPasswordMismatchWarning", event);
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
