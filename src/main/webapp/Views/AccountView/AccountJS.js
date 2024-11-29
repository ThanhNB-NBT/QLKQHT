document.body.addEventListener('click', function(event) {
	if (event.target.classList.contains('edit-account')) {
		// Lấy các dữ liệu từ thuộc tính data- của link sửa
		const accountId = event.target.getAttribute('data-id');
		const username = event.target.getAttribute('data-username');
		const email = event.target.getAttribute('data-email');
		const role = event.target.getAttribute('data-role');
		//		const roleId = event.target.getAttribute('data-role-id');

		// Đưa các giá trị vào modal chỉnh sửa
		document.getElementById('editAccountID').value = accountId;
		document.getElementById('editName').value = username;
		document.getElementById('editEmail').value = email;
		document.getElementById('editRole').value = role;
		//		document.getElementById('editRoleID').value = roleId;

		// Hiển thị modal chỉnh sửa
		$('#edit_account').modal('show');
	}
});

$(document).on('click', '.delete-account', function() {
	// Lấy ID tài khoản từ thuộc tính data-id của nút Xóa
	var accountId = $(this).data('id');
	console.log("Account ID đang lấy:", accountId);

	// Gán giá trị accountId vào input hidden trong form của modal
	$('#deleteAccountId').val(accountId);

	// Hiển thị modal
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
