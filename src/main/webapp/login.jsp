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
								<label>Tài khoản / Email</label>
								<input type="text" class="form-control" name="identifier" required pattern=".{3,}"
													title="Tài khoản phải có ít nhất 3 ký tự">
							</div>
							<div class="form-group">
								<label>Mật khẩu</label>
								<input type="password" class="form-control" name="password" required pattern=".{3,}"
													title="Mật khẩu phải có ít nhất 6 ký tự">
							</div>
							<div class="form-group text-center custom-mt-form-group">
								<button class="btn btn-primary btn-block account-btn" type="submit" value="login">Đăng nhập</button>
							</div>
							
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="includes/footer.jsp"></jsp:include>
</body>
</html>