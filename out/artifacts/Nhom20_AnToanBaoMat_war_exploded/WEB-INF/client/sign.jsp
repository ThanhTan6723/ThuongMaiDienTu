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
    <title>Verify Digital Signature</title>
    <!-- Css Styles -->
    <jsp:include page="./link/link.jsp"></jsp:include>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            padding-bottom: 5vh;
            margin-left: 20px;
        }

        td {
            padding-top: 10px;
            padding-bottom: 20px;
        }

        .checkout__input {
            margin-top: 15px;
            margin-bottom: 20px;
            margin-right: 20px;
            /*display: flex;*/
            /*flex-direction: row;*/
            position: relative;
        }

        .xBNaac {
            background-image: repeating-linear-gradient(45deg, #6fa6d6, #6fa6d6 33px, transparent 0, transparent 41px, #f18d9b 0, #f18d9b 74px, transparent 0, transparent 82px);
            background-position-x: -30px;
            background-size: 116px 3px;
            height: 3px;
            width: 100%;
            margin-bottom: 25px;
        }


    </style>
</head>
<body>
<jsp:include page="./header/header.jsp"></jsp:include>
<c:url var="sign" value="/SignOrderControll"></c:url>

<!-- Checkout Section Begin -->
<section class="shoping-cart spad">
    <div class="container">
        <div class="checkout__form">
            <h4>Xác thực chữ ký</h4>
            <div class="xBNaac"></div>
            <form action="${sign}" id="form" method="post">

            </form>
        </div>
    </div>

</section>
<!-- Checkout Section End -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/axios/0.21.1/axios.min.js"></script>
<!-- Js Plugins -->
<jsp:include page="./footer/footer.jsp"></jsp:include>
<script>

</script>
</body>
</html>
