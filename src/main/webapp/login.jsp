<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Đăng nhập</title>
<jsp:include page="includes/resources.jsp"></jsp:include>
</head>
<body>
	<div class="main-wrapper">
		<div class="account-page">
			<div class="container">
				<h3 class="account-title text-white">Chào mừng</h3>
				<div class="account-box">
					<div class="account-wrapper">
						<div class="account-logo">
							<a href="index.html"><img src="assets/img/logo.png" alt="SchoolAdmin"></a>
						</div>
						<form action="LoginServlet" method="post">
							<div class="form-group">
								<label>Tên tài khoản</label>
								<input type="text" class="form-control" name="username" require>
							</div>
							<div class="form-group">
								<label>Mật khẩu</label>
								<input type="password" class="form-control" name="password" require>
							</div>
							<div class="form-group text-center custom-mt-form-group">
								<button class="btn btn-primary btn-block account-btn" type="submit" value="login">Đăng nhập</button>
							</div>
							<div class="text-center">
								<a href="forgot-password.html">Forgot your password?</a>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script src="assets/js/jquery-3.6.0.min.js"></script>

	<script src="assets/js/bootstrap.bundle.min.js"></script>

	<script src="assets/js/jquery.slimscroll.js"></script>

	<script src="assets/js/app.js"></script>
</body>
</html>