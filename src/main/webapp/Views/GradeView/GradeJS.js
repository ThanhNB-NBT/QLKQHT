// Hiển thị modal chỉnh sửa điểm
$(document).on('click', '.edit-grade', function () {
    const gradeID = $(this).data('id');
    const attendance = $(this).data('attendance');
    const midterm = $(this).data('midterm');
    const final = $(this).data('final');
    const studentCode = $(this).data('student-code');
    const studentName = $(this).data('student-name');
    const classID = $(this).data('class-id');

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
    console.log('Modal edit grade được mở');
});

// Hiển thị modal chỉnh sửa điểm
$(document).on('click', '.edit-gradereview', function () {
    const gradeID = $(this).data('id');
    const attendance = $(this).data('attendance');
    const midterm = $(this).data('midterm');
    const final = $(this).data('final');
    const studentCode = $(this).data('student-code');
    const studentName = $(this).data('student-name');
    const classID = $(this).data('class-id');
	const status = $(this).data('status');
	const comment = $(this).data('comment');

    // Gán dữ liệu vào modal
    $('#editGradeID').val(gradeID);
    $('#editAttendanceScore').text(attendance);
    $('#editMidtermScore').text(midterm);
    $('#editFinalScore').text(final);
    $('#editStudentCode').text(studentCode);
    $('#editStudentName').text(studentName);
    $('#editClassID').val(classID);
	$('#editGradeStatus').val(status);
	$('#editGradeComment').val(comment);

    // Hiển thị modal
    $('#edit_grade_review').modal('show');
    console.log('Modal edit gradereview được mở');
});

$(document).ready(function () {
    // Xử lý sự kiện thay đổi lớp học
    $('#classSelect').on('change', function () {
        $('#classSelectForm').submit();
    });

    // Khởi tạo form upload
    initializeUploadForm();
});

function downloadExcelTemplate() {
    const classID = document.getElementById("classSelect").value;
    if (!classID) {
        alert("Vui lòng chọn lớp học trước khi tải file Excel.");
        return;
    }
    window.location.href = `GradeServlet?action=downloadTemplate&classID=${classID}`;
}

function initializeUploadForm() {
    const fileInput = document.getElementById("gradeFileInput");
    const uploadForm = document.getElementById("uploadExcelForm");
    const hiddenClassID = document.getElementById("hiddenClassID");

    // Xử lý sự kiện khi click nút upload
    document.querySelector('button[onclick="uploadGradeFile()"]').addEventListener('click', function(e) {
        e.preventDefault();
        const classID = document.getElementById("classSelect").value;

        hiddenClassID.value = classID;
        console.log("Mở dialog chọn file với classID:", classID);
        fileInput.click();
    });

    // Xử lý sự kiện khi file được chọn
    fileInput.addEventListener("change", function() {
        if (this.files.length > 0) {
            console.log("File được chọn:", this.files[0].name);
            console.log("ClassID khi submit:", hiddenClassID.value);

            // Tạo FormData để log
            const formData = new FormData(uploadForm);
            for (let pair of formData.entries()) {
                console.log(pair[0] + ': ' + pair[1]);
            }

            uploadForm.submit();
        }
    });
}

function uploadGradeFile() {
    // Function này chỉ được gọi thông qua onClick của button
    // Việc xử lý đã được chuyển vào trong initializeUploadForm
    console.log("Upload function called");
}