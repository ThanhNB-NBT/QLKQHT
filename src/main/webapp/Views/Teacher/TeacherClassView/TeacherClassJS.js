$(document).on('click', '.edit-class', function () {
    const classID = $(this).data('id');
    const className = $(this).data('name');
    const startDate = $(this).data('start-date');
    const endDate = $(this).data('end-date');
    const classType = $(this).data('type');
    const status = $(this).data('status');
    const room = $(this).data('room');
    const classTime = $(this).data('time');
    const semester = $(this).data('semester');
    const maxStudents = $(this).data('max-students');
    const totalLessions = $(this).data('total-lessions');

    // Điền thông tin vào form chỉnh sửa
    $('#editClassID').val(classID);
    $('#editClassName').val(className);
    $('#editStartDate').val(startDate);
    $('#editEndDate').val(endDate);
    $('#editClassType').val(classType);
    $('#editStatus').val(status);
    $('#editRoom').val(room);
    $('#editClassTime').val(classTime);
    $('#editSemester').val(semester);
    $('#editMaxStudents').val(maxStudents);
    $('#editTotalLessions').val(totalLessions);

    // Hiển thị thông tin lên textContent
    $('#editClassNameText').text(className);
    $('#editStartDateText').text(startDate);
    $('#editEndDateText').text(endDate);
    $('#editSemesterText').text(semester);
    $('#editClassTypeText').text(classType);

    // Hiển thị modal chỉnh sửa lớp học
    $('#edit_class').modal('show');
    console.log('Sự kiện chỉnh sửa lớp học được kích hoạt');
});
