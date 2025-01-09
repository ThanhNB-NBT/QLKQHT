<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Thông tin cá nhân</title>
<jsp:include page="../../../includes/resources.jsp"></jsp:include>
</head>
<body>
	<div class="main-wrapper">
		<jsp:include page="../../../includes/header.jsp"></jsp:include>
		<div class="page-wrapper">
			<div class="content container-fluid">

				<div class="page-header">
					<div class="row">
						<div class="col-lg-6 col-md-12 col-sm-12 col-12">
							<h5 class="text-uppercase mb-0 mt-0 page-title">Thông tin cá
								nhân</h5>
						</div>
					</div>
				</div>

				<div class="content-page">
					<div class="row">
						<div class="col-lg-12 col-md-12 col-sm-12 col-12">
							<div class="aboutprofile-sidebar">
								<div class="row">
									<div class="col-lg-4 col-md-12 col-sm-12 col-12">
										<div class="aboutprofile">
											<div class="card">
												<div class="card-body">
													<div class="row">
														<div class="col-lg-12 col-md-12 col-sm-12 col-12">
															<div class="aboutprofile-pic">
																<img class="card-img-top" src="${sessionScope.loggedInUser.avatar}"
																	alt="Card image">
															</div>
															<div class="aboutprofile-name">
																<h5 class="text-center mt-2">${teacher.firstName}
																	${teacher.lastName}</h5>
																<p class="text-center">${teacher.department.departmentName}</p>
															</div>
															<ul class="list-group list-group-flush">
																<li class="list-group-item"><b>Followers</b><a
																	href="#" class="float-right">1000</a></li>
																<li class="list-group-item"><b>Following</b><a
																	href="#" class="float-right">700</a></li>
																<li class="list-group-item"><b>Friends</b><a
																	href="#" class="float-right">5000</a></li>
															</ul>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
									<div class="col-lg-8 col-md-12 col-sm-12 col-12">
										<div class="profile-content">
											<div class="row">
												<div class="col-lg-12">
													<div class="card">
														<div class="card-body">
															<div id="biography" class="biography">
																<div class="row">
																	<div class="col-md-4 col-6">
																		<strong>Họ và tên</strong>
																		<p class="text-muted">${teacher.firstName} ${teacher.lastName}</p>
																	</div>
																	<div class="col-md-4 col-6">
																		<strong>Số điện thoại</strong>
																		<p class="text-muted">${teacher.phone}</p>
																	</div>
																	<div class="col-md-4 col-6">
																		<strong>Email</strong>
																		<p class="text-muted">
																			<a href="/cdn-cgi/l/email-protection"
																				class="__cf_email__"
																				data-cfemail="82efebe1eae3e7eef4e0f7f6f6e3f0f1c2e7fae3eff2eee7ace1edef">${teacher.email}</a>
																		</p>
																	</div>
																</div>
																<hr>
																<ul class="list-group list-group-flush">
																	<li class="list-group-item"><b>Khoa/Viện</b><a href="#"
																		class="float-right">${teacher.department.departmentName}</a></li>
																	<li class="list-group-item"><b>Văn phòng</b><a
																		href="#" class="float-right">${teacher.office}</a></li>
																	<li class="list-group-item"><b>Ngày vào làm</b><a
																		href="#" class="float-right">${teacher.hireDate}</a></li>
																</ul>
															</div>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../../../includes/footer.jsp"></jsp:include>
</body>
</html>