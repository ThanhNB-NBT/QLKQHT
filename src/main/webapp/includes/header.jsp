<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@ page import="common.SessionUtils" %>
<%@ page import="models.bean.Account" %>

<%
    // Kiểm tra người dùng đã đăng nhập hay chưa
    HttpSession session1 = request.getSession(false);  // Lấy session hiện tại
    if (!SessionUtils.isLoggedIn(session1)) {
        // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
        response.sendRedirect("../login.jsp");
        return;  // Dừng việc xử lý tiếp
    } else {
        // Nếu đã đăng nhập, lấy tài khoản và hiển thị thông tin người dùng
        Account loggedInUser = SessionUtils.getLoggedInAccount(session1);
%>


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
			<li class="nav-item dropdown d-none d-sm-block"><a href="#"
				class="dropdown-toggle nav-link" data-toggle="dropdown"> <img
					src="assets/img/sidebar/icon-22.png" alt="">
			</a>
				<div class="dropdown-menu notifications">
					<div class="topnav-dropdown-header">
						<span>Notifications</span>
					</div>
					<div class="drop-scroll">
						<ul class="notification-list">
							<li class="notification-message"><a href="activities.html">
									<div class="media">
										<span class="avatar"> <img alt="John Doe"
											src="assets/img/user-06.jpg" class="img-fluid rounded-circle">
										</span>
										<div class="media-body">
											<p class="noti-details">
												<span class="noti-title">John Doe</span> is now following
												you
											</p>
											<p class="noti-time">
												<span class="notification-time">4 mins ago</span>
											</p>
										</div>
									</div>
							</a></li>

						</ul>
					</div>
					<div class="topnav-dropdown-footer">
						<a href="activities.html">View all Notifications</a>
					</div>
				</div></li>
			<li class="nav-item dropdown d-none d-sm-block"><a
				href="javascript:void(0);" id="open_msg_box"
				class="hasnotifications nav-link"> <img
					src="assets/img/sidebar/icon-23.png" alt="">
			</a></li>
			<li class="nav-item dropdown has-arrow"><a href="#"
				class=" nav-link user-link" data-toggle="dropdown"> <span
					class="user-img"><img class="rounded-circle"
						src="assets/img/user-06.jpg" width="30" > <span
						class="status online"></span> </span> <span><%= loggedInUser.getUsername() %></span>
			</a>
				<div class="dropdown-menu">
					<a class="dropdown-item" href="profile.html">My Profile</a> <a
						class="dropdown-item" href="edit-profile.html">Edit Profile</a> <a
						class="dropdown-item" href="settings.html">Settings</a> <a
						class="dropdown-item" href="LogoutServlet">Logout</a>
				</div></li>
		</ul>
		<div class="dropdown mobile-user-menu float-right">
			<a href="#" class="nav-link dropdown-toggle" data-toggle="dropdown"
				aria-expanded="false"> <i class="fas fa-ellipsis-v"></i>
			</a>
			<div class="dropdown-menu dropdown-menu-right">
				<a class="dropdown-item" href="profile.html">My Profile</a> <a
					class="dropdown-item" href="edit-profile.html">Edit Profile</a> <a
					class="dropdown-item" href="settings.html">Settings</a> <a
					class="dropdown-item" href="LogoutServlet">Logout</a>
			</div>
		</div>
	</div>
</div>
<% } %>
<jsp:include page="sidebar.jsp"></jsp:include>