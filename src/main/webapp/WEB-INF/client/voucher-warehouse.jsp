<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="zxx">
<head>
    <meta charset="UTF-8">
    <meta name="description" content="Voucher Template">
    <meta name="keywords" content="Voucher, discount, codes">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Kho Voucher</title>
    <jsp:include page="link/link.jsp"></jsp:include>
    <style>
        .voucher-container {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            justify-content: center;
            margin-top: 20px;
        }

        .voucher {
            border: 1px dashed #ddd;
            border-radius: 8px;
            padding: 15px;
            width: 220px;
            background-color: #fff;
            text-align: center;
            position: relative;
        }

        .voucher-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 10px;
        }

        .voucher-details {
            margin-bottom: 10px;
        }

        .voucher-action {
            display: flex;
            flex-direction: column;
            gap: 5px;
        }

        .voucher-action .save-button, .voucher-action .conditions {
            display: inline-block;
            padding: 0px 10px;
            /*border-radius: 5px;*/
            background-color: #7fad39;
            color: #fff;
            text-decoration: none;
            cursor: pointer;
        }

        .voucher-action .conditions {
            background-color: transparent;
            color: #ff5722;
        }

        .voucher-expired {
            color: #ff0000;
            font-weight: bold;
            position: absolute;
            top: 10px;
            right: 10px;
        }
        .voucher-action .saved-button,
        .voucher-action .soldout-button {
            background-color: #ddd;
            color: #999;
            cursor: not-allowed;
        }


    </style>
</head>
<body>
<jsp:include page="header/header.jsp"></jsp:include>

<section class="checkout spad">
    <div class="container">
        <div class="voucher-container">
            <c:if test="${empty listSaved}">
                <div style="padding: 80px"><h4 style="color: #8f8f8f">Bạn chưa lưu voucher nào</h4></div>
            </c:if>
            <c:forEach var="voucher" items="${listSaved}">
                <div class="voucher" data-voucher-id="${voucher.id}">
                    <c:if test="${!voucher.active}">
                        <div class="voucher-expired">Hết lượt sử dụng</div>
                    </c:if>
                    <div class="voucher-header">
                        <div>
                            <div>Giảm <c:out value="${voucher.discountPercentage}"/>%</div>
                        </div>
                        <div>
                            <c:if test="${not empty voucher.product}">
                                <div><c:out value="${voucher.product.name}"/></div>
                            </c:if>
                            <c:if test="${not empty voucher.category}">
                                <div><c:out value="${voucher.category.name}"/></div>
                            </c:if>
                        </div>
                    </div>
                    <div class="voucher-details">
                        <div><b><c:out value="${voucher.code}"/></b></div>
                        <div>Giảm tối đa: <fmt:formatNumber value="${voucher.maximumDiscount}" pattern="#,###.### ₫"/></div>
                        <div>Đơn tối thiểu: <fmt:formatNumber value="${voucher.minimumOrderValue}" pattern="#,###.### ₫"/></div>
                        <div>Có hiệu lực từ <fmt:formatDate value="${voucher.startDate}" pattern="dd/MM/yyyy"/></div>
                    </div>
                    <div class="voucher-action">
<%--                        <c:choose>--%>
<%--                            <c:when test="${voucher.quantity <= 0 and empty sessionScope.account}">--%>
<%--                                <span class="soldout-button">Đã hết mã</span>--%>
<%--                            </c:when>--%>
<%--                            <c:when test="${voucher.quantity <= 0 and not empty sessionScope.account and not savedVoucherIds.contains(voucher.id)}">--%>
<%--                                <span class="soldout-button">Đã hết mã</span>--%>
<%--                            </c:when>--%>
<%--                            <c:when test="${not empty savedVoucherIds and savedVoucherIds.contains(voucher.id)}">--%>
<%--                                <span class="saved-button">Đã lưu</span>--%>
<%--                            </c:when>--%>
<%--                            <c:otherwise>--%>
<%--                                <a href="#" class="save-button" onclick="saveVoucher(${voucher.id}, this)">Lưu</a>--%>
<%--                            </c:otherwise>--%>
<%--                        </c:choose>--%>
                        <a href="#" class="conditions">Điều Kiện</a>
                    </div>
                </div>
            </c:forEach>

        </div>
    </div>
</section>

<jsp:include page="footer/footer.jsp"></jsp:include>

<script src="js/jquery-3.3.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery.nice-select.min.js"></script>
<script src="js/jquery-ui.min.js"></script>
<script src="js/jquery.slicknav.js"></script>
<script src="js/mixitup.min.js"></script>
<script src="js/owl.carousel.min.js"></script>
<script src="js/main.js"></script>

<script>

</script>
</body>
</html>