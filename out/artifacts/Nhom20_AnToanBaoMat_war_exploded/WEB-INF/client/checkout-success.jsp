<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="zxx">

<head>
<meta charset="UTF-8">
<meta name="description" content="Ogani Template">
<meta name="keywords" content="Ogani, unica, creative, html">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>Sản phẩm</title>

<jsp:include page="./link/link.jsp"></jsp:include>
<style type="text/css">
.message {
	font-family: 'Arial', sans-serif;
	font-size: 18px;
	color: white;
	text-align: center;
	padding: 10px;
	box-shadow: 2px 2px 5px 0px rgba(0, 0, 0, 0.5);
	border-radius: 5px;
	background-color: #FF9900;
	margin-bottom: 20px;
}
.link-detail{
	color: #9ac448;
}
.link-detail:hover{
	color: #8fb44a;
}
</style>
</head>

<body>
	<jsp:include page="./header/header.jsp"></jsp:include>
	<section class="shoping-cart spad" style="padding-top: 60px;padding-bottom: 120px">
		<div class="container">
			<div class="row">
				<div class="col-lg-12">
					<c:url var="orderplace" value="OrderPlaceControl"></c:url>
					<br>
					<br>
<%--					<img alt="" src="assets/img/thanks.jpeg"--%>
<%--						 style="vertical-align: middle;">--%>
					<h4 style="text-align: center;">
						Cảm ơn bạn đã đặt hàng, click vào link bên dưới để theo dõi đơn hàng đã đặt nhé!<br><a class="link-detail"
							href="${pageContext.request.contextPath}/${orderplace}">Theo dõi đơn hàng</a>
					</h4>
				</div>
			</div>

		</div>
	</section>

	<jsp:include page="./footer/footer.jsp"></jsp:include>
</body>

</html>