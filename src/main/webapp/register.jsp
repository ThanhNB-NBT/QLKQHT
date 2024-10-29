<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng ký</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0">
    <link rel="shortcut icon" type="image/x-icon" href="assets/img/favicon.png">
    <link rel="stylesheet" href="assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/plugins/fontawesome/css/all.min.css">
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>
    <div class="main-wrapper">
        <div class="account-page">
            <div class="container">
                <h3 class="account-title text-white">Đăng ký tài khoản mới</h3>
                <div class="account-box">
                    <div class="account-wrapper">
                        <div class="account-logo">
                            <a href="index.html"><img src="assets/img/logo.png" alt="SchoolAdmin"></a>
                        </div>
                        <form action="RegisterServlet" method="post">
                            <div class="form-group">
                                <label>Tên người dùng</label>
                                <input type="text" class="form-control" name="username" required>
                            </div>
                            <div class="form-group">
                                <label>Email</label>
                                <input type="email" class="form-control" name="email" required>
                            </div>
                            <div class="form-group">
                                <label>Mật khẩu</label>
                                <input type="password" class="form-control" name="password" required>
                            </div>
                            <div class="form-group">
                                <label>Chọn vai trò</label>
                                <select class="form-control" name="roleID" required>
                                    <option value="1">Admin</option>
                                    <option value="2">Teacher</option>
                                    <option value="3">Student</option>
                                    
                                </select>
                            </div>
                            <div class="form-group text-center custom-mt-form-group">
                                <button class="btn btn-primary btn-block account-btn" type="submit">Đăng ký</button>
                            </div>
                            <div class="text-center">
                                <a href="login.jsp">Đã có tài khoản? Đăng nhập ngay!</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="assets/js/jquery-3.6.0.min.js"></script>
    <script src="assets/js/bootstrap.bundle.min.js"></script>
    <script src="assets/js/app.js"></script>
</body>
</html>
