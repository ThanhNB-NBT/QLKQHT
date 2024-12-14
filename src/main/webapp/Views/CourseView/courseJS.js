$(document).on('click', '.edit-course', function () {

    const courseId = $(this).data('id');
    const courseName = $(this).data('name');
    const credits = $(this).data('credits');
    const courseType = $(this).data('type');
	const departmentName = $(this).data('department');
    const departmentId = $(this).data('departmentid');
    const status = $(this).data('status');

    $('#editCourseID').val(courseId);
    $('#editCourseName').text(courseName);
    $('#editCredits').val(credits);
    $('#editCourseType').val(courseType);
    $('#editDepartmentID').val(departmentId);
    $('#editStatus').val(status);
	$('#editDepartment').text(departmentName);

    $('#edit_course').modal('show');
});


$(document).on('click', '.delete-course', function() {
    const courseId = $(this).data('id');

    $('#deleteCourseId').val(courseId);

    $('#delete_course').modal('show');
});

document.getElementById("courseName").addEventListener("invalid", function(event) {
    if (event.target.value === "") {
        event.target.setCustomValidity("Vui lòng nhập tên học phần.");  // Thay đổi thông báo lỗi
    } else {
        event.target.setCustomValidity("");  // Hủy thông báo lỗi nếu có giá trị hợp lệ
    }
});

