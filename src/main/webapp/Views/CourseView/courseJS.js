document.body.addEventListener('click', function(event) {

    if (event.target.classList.contains('edit-course')) {
        const courseId = event.target.getAttribute('data-id');
        const courseName = event.target.getAttribute('data-name');
        const credits = event.target.getAttribute('data-credits');
        const courseType = event.target.getAttribute('data-type');
        const departmentId = event.target.getAttribute('data-departmentid');
        const status = event.target.getAttribute('data-status');

        document.getElementById('editCourseID').value = courseId;
        document.getElementById('editCourseName').value = courseName;
        document.getElementById('editCredits').value = credits;
        document.getElementById('editCourseType').value = courseType;
        document.getElementById('editDepartmentID').value = departmentId;
        document.getElementById('editStatus').value = status;

        $('#edit_course').modal('show');
    }
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

