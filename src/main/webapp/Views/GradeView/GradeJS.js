// Hiển thị modal chỉnh sửa điểm
$(document).on('click', '.edit-grade', function () {
    const gradeID = $(this).data('id');
    const attendance = $(this).data('attendance');
    const midterm = $(this).data('midterm');
    const final = $(this).data('final');
    const studentCode = $(this).data('student-code');
    const studentName = $(this).data('student-name');
    const classID = $(this).data('class-id');

	console.log('Grade Data:', {
	        gradeID: $(this).data('id'),
	        attendance: $(this).data('attendance'),
	        midterm: $(this).data('midterm'),
	        final: $(this).data('final'),
	        studentCode: $(this).data('student-code'),
	        studentName: $(this).data('student-name'),
	        classID: $(this).data('class-id'),
	    });

    // Gán dữ liệu vào modal
    $('#editGradeID').val(gradeID);
    $('#editAttendanceScore').val(attendance);
    $('#editMidtermScore').val(midterm);
    $('#editFinalScore').val(final);
    $('#editStudentCode').text(studentCode);
    $('#editStudentName').text(studentName);
    $('#editClassID').val(classID);

    // Hiển thị modal
    $('#edit_grade').modal('show');
	console.log('Sự kiện submit được kích hoạt');
});

$(document).ready(function () {
    $('#classSelect').on('change', function () {
        $('#classSelectForm').submit();
    });
});

