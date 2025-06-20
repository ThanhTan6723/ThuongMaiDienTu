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
        /* Styles for the navigation tab */
        .nav-tabs-container {
            text-align: center;
            margin-top: 20px; /* Adjust as needed */
        }

        .nav-tabs {
            display: inline-flex;
            list-style: none;
            padding: 0;
            margin: 0;
            border-bottom: 2px solid #ddd;
            background-color: #f5f5f5;
            font-family: Arial, sans-serif;
            font-size: 14px;
        }

        .nav-tabs li {
            margin: 0;
        }

        .nav-tabs a {
            display: block;
            padding: 15px 20px;
            text-decoration: none;
            color: #000;
            border: 1px solid transparent;
            border-radius: 4px 4px 0 0;
            position: relative;
            text-align: center;
            white-space: nowrap; /* Ensures text doesn't wrap */
        }

        .nav-tabs a.active {
            color: #ff5400;
            background-color: #fff;
            border: 1px solid #ddd;
            border-bottom: none;
        }

        .nav-tabs a:hover {
            color: #ff5400;
            background-color: #fff;
            border: 1px solid #ddd;
            border-bottom: none;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-left: 40px;
        }

        td {
            padding-top: 10px;
            padding-bottom: 20px;
        }

        .checkout__input {
            margin-top: 15px;
            margin-bottom: 20px;
            margin-right: 20px;
            position: relative;
        }

        /* Style for the table rows */
        .shoping__cart__item img {
            width: 73px;
            height: auto;
            margin-right: 10px;
        }

        /* Style for the total row */
        .shoping__cart__total {
            font-weight: bold;
            color: red;
        }

        .order-info {
            font-size: 14px;
            font-weight: 700;
            font-family: "Arial", sans-serif;
            margin-bottom: 10px;
        }

        .data {
            font-weight: normal;
        }
    </style>

</head>

<body>
<jsp:include page="./header/header.jsp"></jsp:include>
<c:if test="${not empty listProductOrder}">
    <!-- Navigation Tab -->
    <div class="nav-tabs-container">
        <ul class="nav-tabs">
            <li><a href="#" class="active" data-status="all">Tất cả</a></li>
            <li><a href="#" data-status="Đơn hàng đang chờ xác nhận">Chờ xác nhận</a></li>
            <li><a href="#" data-status="Đơn hàng đã được xác nhận và chờ đóng gói">Chờ đóng gói</a></li>
            <li><a href="#" data-status="Đơn hàng đã được đóng gọi và chờ vận chuyển">Chờ vận chuyển</a></li>
            <li><a href="#" data-status="Đang giao hàng">Chờ giao hàng</a></li>
            <li><a href="#" data-status="Giao hàng thành công">Hoàn thành</a></li>
            <li><a href="#" data-status="Đơn hàng đã được hủy">Đã hủy</a></li>
        </ul>
    </div>
</c:if>
<section class="shoping-cart spad">
    <div class="container">
        <div class="order-list" id="order-list-container">

        </div>
    </div>
</section>

<jsp:include page="./footer/footer.jsp"></jsp:include>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="../../assets/js/order-filter.js"></script>
<script>
    function sendCancelRequest(orderId) {
        $.ajax({
            url: 'CancelOrderServlet',
            method: 'POST',
            data: {
                orderId: orderId
            },
            success: function(response) {
                alert('Yêu cầu hủy đơn hàng đã được gửi thành công!');
                // Reload the page to reflect the changes
                location.reload();
            },
            error: function(xhr, status, error) {
                alert('Có lỗi xảy ra khi gửi yêu cầu hủy đơn hàng: ' + xhr.responseText);
            }
        });
    }

    function verifyOrder(orderId) {
        // Kiểm tra nếu orderId hợp lệ
        if (!orderId) {
            alert("orderId không hợp lệ.");
            return;
        }

        // Tạo URL với tham số orderId
        var url = 'VerifyOrderDetailControll?orderId=' + orderId;

        // Chuyển hướng tới URL đã tạo
        window.location.href = url;
    }


</script>
</body>
</html>
