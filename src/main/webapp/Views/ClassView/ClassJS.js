$(document).on('click', '.delete-class', function() {
    const classId = $(this).data('id');

    $('#deleteClassId').val(classId);

    $('#delete_class').modal('show');
});

// Lắng nghe sự kiện click trên toàn bộ document
// Đảm bảo xử lý được kể cả khi các phần tử được thêm vào DOM sau khi trang đã load
document.body.addEventListener('click', function (event) {
    if (event.target.classList.contains('edit-class')) {
        // Lấy thông tin từ thuộc tính data-* của phần tử
        const classID = event.target.getAttribute('data-id');
        const className = event.target.getAttribute('data-name');
        const startDate = event.target.getAttribute('data-start-date');
        const endDate = event.target.getAttribute('data-end-date');
        const classType = event.target.getAttribute('data-type');
        const teacherID = event.target.getAttribute('data-teacher-id');
        const courseID = event.target.getAttribute('data-course-id');
        const status = event.target.getAttribute('data-status');
        const room = event.target.getAttribute('data-room');
        const classTime = event.target.getAttribute('data-time');
        const semester = event.target.getAttribute('data-semester');
        const maxStudents = event.target.getAttribute('data-max-students');
        const totalLessions = event.target.getAttribute('data-total-lessions');

        // Điền thông tin vào form chỉnh sửa
        document.getElementById('editClassID').value = classID;
        document.getElementById('editClassName').value = className;
        document.getElementById('editStartDate').value = startDate;
        document.getElementById('editEndDate').value = endDate;
        document.getElementById('editClassType').value = classType;
        document.getElementById('editTeacherID').value = teacherID;
        document.getElementById('editCourseID').value = courseID;
        document.getElementById('editStatus').value = status;
        document.getElementById('editRoom').value = room;
        document.getElementById('editClassTime').value = classTime;
        document.getElementById('editSemester').value = semester;
        document.getElementById('editMaxStudents').value = maxStudents;
        document.getElementById('editTotalLessions').value = totalLessions;

        // Hiển thị thông tin lên textContent
        document.getElementById('editClassNameText').textContent = className;
        document.getElementById('editStartDateText').textContent = startDate;
        document.getElementById('editEndDateText').textContent = endDate;
        document.getElementById('editSemesterText').textContent = semester;

        // Hiển thị modal chỉnh sửa lớp học
        $('#edit_class').modal('show');
    }
});



