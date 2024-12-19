function toggleCustomStatus(selectElement) {
        const customStatusInput = document.getElementById('customStatus');
        if (selectElement.value === 'custom') {
            customStatusInput.classList.remove('d-none');
            customStatusInput.required = true;
        } else {
            customStatusInput.classList.add('d-none');
            customStatusInput.value = ''; // Clear input when not custom
            customStatusInput.required = false;
        }
    };

$(document).on('click', '.delete-student-class', function() {
    const studentClassId = $(this).data('id');

    $('#deleteStudentClassId').val(studentClassId);

    $('#delete_student_class').modal('show');
});

$(document).on('click', '.edit-student-class', function () {

    const studentClassID = $(this).data('id');
    const studentName = $(this).data('student');
    const studentCode = $(this).data('code');
    const className = $(this).data('class');
    const status = $(this).data('status');

    // Gán dữ liệu vào modal
    $('#editStudentClassID').val(studentClassID);
    $('#editStudentName').text(studentName);
    $('#editStudentCode').text(studentCode);
    $('#editClassName').text(className);
    $('#editStatus').val(status);

    // Hiển thị modal
    $('#edit_student_class').modal('show');
});


