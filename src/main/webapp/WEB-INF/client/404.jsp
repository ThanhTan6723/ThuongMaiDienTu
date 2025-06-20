<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
<title>Thông tin tài khoản</title>


<title>Page Not Found</title>
<style>

body{
	font-family:monospace;
	background-color: #f7f7f7;
	margin: 0;
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh;
	font-size: 25px;
}


</style>
</head>

<body>
	<div class="error-container">
		<h4>Uh oh! Chúng tôi không thể tìm thấy trang này.</h4>
		<c:url var="index" value="IndexControl"></c:url>


		<p>
			Bạn có thể trở về <a
				href="${pageContext.request.contextPath}/${index}">TRANG CHỦ</a>.
		</p>
		<img class="error-image" src="assets/img/404.png" alt="">
	</div>
</body>

</html>