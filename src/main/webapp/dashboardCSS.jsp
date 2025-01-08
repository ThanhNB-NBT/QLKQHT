<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
.page-title {
	margin-bottom: 20px;
}

.class-selection {
	margin-bottom: 30px;
}

.statistics-grid {
	display: grid;
	grid-template-columns: 1fr 1fr;
	gap: 20px;
	margin-bottom: 30px;
}

.statistics-box {
	background: #fff;
	padding: 20px;
	border-radius: 8px;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.stat-item {
	margin: 10px 0;
	display: flex;
	justify-content: space-between;
}

.chart-container {
	height: 300px;
	width: 100%;
	position: relative;
}

.grade-table {
	background: #fff;
	padding: 20px;
	border-radius: 8px;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	margin-top: 20px;
}

table {
	width: 100%;
	border-collapse: collapse;
}

th, td {
	padding: 12px;
	text-align: left;
	border-bottom: 1px solid #ddd;
}

th {
	background-color: #f5f5f5;
	font-weight: 600;
}

tr:hover {
	background-color: #f9f9f9;
}
</style>