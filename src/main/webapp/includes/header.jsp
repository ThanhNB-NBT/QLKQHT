<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="header-outer">
	<div class="header">
		<a id="mobile_btn" class="mobile_btn float-left" href="#sidebar"><i
			class="fas fa-bars" aria-hidden="true"></i></a> <a id="toggle_btn"
			class="float-left" href="javascript:void(0);"> <img
			src="assets/img/sidebar/icon-21.png" alt="">
		</a>

		<ul class="nav float-left">
			<li>
				<div class="top-nav-search">
					<a href="javascript:void(0);" class="responsive-search"> <i
						class="fa fa-search"></i>
					</a>
					<form action="search.html">
						<input class="form-control" type="text" placeholder="Search here">
						<button class="btn" type="submit">
							<i class="fa fa-search"></i>
						</button>
					</form>
				</div>
			</li>
			<li><a href="index.html"
				class="mobile-logo d-md-block d-lg-none d-block"><img
					src="assets/img/logo1.png" alt="" width="30" height="30"></a></li>
		</ul>

		<ul class="nav user-menu float-right">
			<li class="nav-item dropdown has-arrow"><a href="#"
				class=" nav-link user-link" data-toggle="dropdown"> <span
					class="user-img"><img class="rounded-circle"
						src="assets/img/user.jpg" width="30"> <span
						class="status online"></span> </span> <span>${username}</span>
			</a>
				<div class="dropdown-menu">
					<a class="dropdown-item" href="profile.html">Thông tin cá nhân</a><a
						class="dropdown-item" href="settings.html">Cài đặt</a> <a
						class="dropdown-item" href="LogoutServlet">Đăng xuất</a>
				</div></li>
		</ul>
		<div class="dropdown mobile-user-menu float-right">
			<a href="#" class="nav-link dropdown-toggle" data-toggle="dropdown"
				aria-expanded="false"> <i class="fas fa-ellipsis-v"></i>
			</a>
			<div class="dropdown-menu dropdown-menu-right">
				<a class="dropdown-item" href="profile.html">Thông tin cá nhân</a> <a
					class="dropdown-item" href="settings.html">Cài đặt</a> <a
					class="dropdown-item" href="LogoutServlet">Đăng xuất</a>
			</div>
		</div>
	</div>
</div>
<jsp:include page="sidebar.jsp"></jsp:include>