<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="sidebar" id="sidebar">
  <div class="sidebar-inner slimscroll">
    <div id="sidebar-menu" class="sidebar-menu">
      <div class="header-left">
        <a href="index.html" class="logo">
          <img src="assets/img/logo1.png" width="40" height="40" alt="Logo trường">
          <span class="text-uppercase">Đại học Vinh</span>
        </a>
      </div>
      <ul class="sidebar-ul">
        <li class="menu-title">Menu</li>

        <li>
          <a href="GradeServlet">
            <img src="assets/img/sidebar/icon-1.png" alt="icon"><span>Điểm</span>
          </a>
        </li>

        <li>
          <a href="TeacherServlet">
            <img src="assets/img/sidebar/icon-2.png" alt="icon"> <span> Giảng viên</span>
          </a>
        </li>

        <li>
          <a href="StudentServlet">
            <img src="assets/img/sidebar/icon-3.png" alt="icon"> <span> Sinh viên</span>
          </a>
        </li>

		<li class="submenu">
            <a>
              <img src="assets/img/sidebar/icon-4.png" alt="icon"> <span> Lớp </span> <span class="menu-arrow"></span>
            </a>
            <ul class="list-unstyled" style="display: none;">
              <li><a href="ClassServlet" ><span>Lớp học phần</span></a></li>
              <li><a href="StudentClassServlet" ><span>Danh sách đăng kí</span></a></li>

            </ul>
          </li>

        <li >
          <a href="CourseServlet">
            <img src="assets/img/sidebar/icon-4.png" alt="icon"> <span> Học phần </span>
          </a>
        </li>

        <li >
          <a href="DepartmentServlet">
            <img src="assets/img/sidebar/icon-4.png" alt="icon"> <span> Khoa/Viện</span>
          </a>

        </li>

        <li>
          <a href="AccountServlet">
            <img src="assets/img/sidebar/icon-4.png" alt="icon"> <span> Tài khoản</span>
          </a>
        </li>

      </ul>
    </div>
  </div>
</div>