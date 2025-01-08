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
	console.log('Sự kiện submit được kích hoạt');
});

$(document).ready(function () {
    $('#classSelect').on('change', function () {
        $('#classSelectForm').submit();
    });
});

function downloadExcelTemplate() {
    const classID = document.getElementById("classSelect").value;
    if (!classID) {
        alert("Vui lòng chọn lớp học trước khi tải file Excel.");
        return;
    }
    window.location.href = `GradeServlet?action=downloadTemplate&classID=${classID}`;
}

function uploadGradeFile() {
    const fileInput = document.getElementById("gradeFileInput");
    const classSelect = document.getElementById("classSelect"); // Dropdown để lấy classID
    const hiddenClassID = document.getElementById("hiddenClassID");

    // Lấy classID từ dropdown
    const classID = classSelect ? classSelect.value : null;

    if (!classID) {
        alert("Vui lòng chọn lớp học trước khi nhập điểm từ Excel.");
        return;
    }

    // Gán giá trị classID vào input hidden
    hiddenClassID.value = classID;

    // Kiểm tra giá trị đã được cập nhật
    console.log("ClassID được gửi:", hiddenClassID.value);

    // Mở cửa sổ chọn file
    fileInput.click();
}

// Sự kiện khi file được chọn
document.getElementById("gradeFileInput").addEventListener("change", function () {
    const uploadForm = document.getElementById("uploadExcelForm");
    const hiddenClassID = document.getElementById("hiddenClassID");

    // Kiểm tra hiddenClassID trước khi submit
    if (!hiddenClassID.value) {
        alert("Vui lòng chọn lớp học trước khi nhập điểm từ Excel.");
        return;
    }

    console.log("Form đang được submit với classID:", hiddenClassID.value);
    uploadForm.submit(); // Gửi form
});




