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
    <title>Payment</title>
    <!-- Css Styles -->
    <jsp:include page="./link/link.jsp"></jsp:include>
    <style>
        /* Existing styles remain unchanged */
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

        .radio-item {
            margin-left: 10px;
            margin-top: 10px;
        }

        .radio-group {
            display: flex;
            flex-direction: column;
            gap: 10px;
            margin-top: 10px;
        }

        .radio-item {
            align-items: center;
            position: relative;
            padding-left: 5px;
            cursor: pointer;
            user-select: none;
        }

        .radio-item label {
            margin-left: 10px;
            font-size: 16px;
            color: #333;
        }

        .radio-item input:checked ~ label {
            color: #7fad39;
            font-weight: bold;
        }

        #qrModal {
            display: none;
            position: fixed;
            z-index: 1;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0, 0, 0, 0.4);
            justify-content: center;
            align-items: center;
        }

        #qrModalContent {
            background-color: #394764;
            color: white;
            border-radius: 10px;
            padding: 20px;
            text-align: center;
            width: 450px;
        }

        #progressBar {
            width: 100%;
            background-color: #f3f3f3;
            border-radius: 10px;
            overflow: hidden;
            margin-top: 5px;
            position: relative;
            height: 5px;
        }

        #progress {
            width: 100%;
            height: 100%;
            background-color: #f39c12;
            position: absolute;
            top: 0;
            left: 0;
            animation: progressAnimation 2s linear infinite;
        }

        @keyframes progressAnimation {
            0% {
                left: -100%;
            }
            100% {
                left: 100%;
            }
        }

        .qrModal-footer {
            margin: 15px 30px 10px 30px;
            font-size: 14px;
            display: flex;
            flex-direction: column;
            align-items: center;
            color: white;
        }

        #countdown {
            margin-left: 130px;
        }

        span {
            margin-bottom: 15px;
        }

        .error-message {
            color: red;
            font-size: 14px;
            margin-top: 10px;
            display: none; /* Hidden by default */
        }

        .error-message.show {
            display: block; /* Show when error occurs */
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

        .modal-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            display: none;
            overflow: hidden;
            justify-content: center;
            align-items: flex-start;
            z-index: 1000;
            margin: 0;
            padding: 0;
        }

        .modalV-content {
            background: white;
            border-radius: 18px;
            width: 600px;
            margin-top: 220px;
            padding: 0;
            text-align: center;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
        }

        .modalV-header {
            background: #6fa6d6;
            color: white;
            padding: 15px;
            border-top-left-radius: 16px;
            border-top-right-radius: 16px;
            font-size: 18px;
            font-weight: bold;
        }

        .modalV-body {
            margin: 20px 30px;
            font-size: 16px;
            line-height: 1.5;
        }

        .modalV-footer {
            display: flex;
            justify-content: flex-end;
            gap: 10px;
            margin: 20px 30px;
        }

        .modalV-footer button {
            padding: 6px 18px;
            font-size: 16px;
            border: none;
            border-radius: 40px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .modalV-footer button.cancel {
            background: #f0f0f0;
            color: #555;
        }

        .modalV-footer button.cancel:hover {
            background: #e0e0e0;
        }

        .modalV-footer button.confirm {
            background: #6fa6d6;
            color: white;
        }

        .modalV-footer button.confirm:hover {
            background: #5f96c6;
        }

        .custom-notification {
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background-color: rgba(60, 60, 60, 0.8);
            color: #fff;
            max-width: 500px;
            padding: 40px 60px;
            border-radius: 20px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
            font-size: 18px;
            text-align: center;
            z-index: 9999;
            opacity: 1;
            transition: opacity 0.5s ease, transform 0.5s ease;
        }

        .custom-notification.hide {
            opacity: 0;
            transform: translate(-50%, -60%);
        }

        .notification-content {
            display: flex;
            flex-direction: column;
            align-items: center;
            text-align: center;
        }

        .notification-content span {
            font-weight: bold;
        }

        .notification-image {
            max-width: 100px;
            max-height: 100px;
            margin-bottom: 10px;
            border-radius: 50%;
        }
    </style>
</head>
<body>
<jsp:include page="./header/header.jsp"></jsp:include>
<c:url var="pay" value="/PaymentInsertControll"></c:url>

<!-- Checkout Section Begin -->
<section class="shoping-cart spad">
    <div class="container">
        <div class="checkout__form">
            <h4>Xác nhận thanh toán</h4>
            <div class="xBNaac"></div>
            <form action="${pay}" id="form" method="get">
                <div class="row" style="background-color: white; padding: 10px;">
                    <div class="col-lg-8 col-md-6" style="max-width: 63.666667%;">
                        <div class="row" style="border-bottom: 1px dashed #e7e7e7; background-color: #fffcf5; height: 130px; border-top-left-radius: 12px;">
                            <div class="col-lg-6">
                                <div class="checkout__input">
                                    <c:set var="order" value="${sessionScope.bill}"></c:set>
                                    <div class="order-info">Ngày đặt hàng: <span class="data" name="bookingDate">${order.bookingDate}</span></div>
                                    <div class="order-info">Địa chỉ giao hàng: <span class="data" name="address">${order.address}</span></div>
                                    <c:if test="${not empty order.orderNotes}">
                                        <div class="order-info">Ghi chú: <span class="data" name="notes">${order.orderNotes}</span></div>
                                    </c:if>
                                </div>
                            </div>
                            <div class="col-lg-6">
                                <div class="checkout__input">
                                    <div class="order-info">Họ và tên người nhận: <span id="nameReceive" name="rname" class="data">${order.consigneeName}</span></div>
                                    <div class="order-info">Số điện thoại: <span class="data" name="rphone">${order.consigneePhone}</span></div>
                                </div>
                            </div>
                        </div>
                        <div class="row" style="background-color: #fbfbfb; border-radius: 0px 0px 12px 12px;">
                            <div class="col-lg-12">
                                <div class="checkoutOrder" style="font-family: 'Arial', sans-serif; font-size: 14px;">
                                    <h5 style="text-align: left; margin-top: 15px; margin-bottom: 15px;"><b>Danh sách sản phẩm đã đặt</b></h5>
                                    <table>
                                        <thead>
                                        <tr>
                                            <th>Tên sản phẩm</th>
                                            <th>Giá</th>
                                            <th>Số lượng</th>
                                            <th>Thành tiền</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${sessionScope.billDetail}" var="orderDetail">
                                            <tr>
                                                <td class="shoping__cart__item">${orderDetail.product.name}</td>
                                                <td class="shoping__cart__price">
                                                    <fmt:formatNumber value="${orderDetail.product.price}" pattern="#,###.### ₫"/>
                                                </td>
                                                <td class="shoping__cart__quantity">${orderDetail.quantity}</td>
                                                <td class="shoping__cart__total">
                                                    <fmt:formatNumber value="${orderDetail.price}" pattern="#,###.### ₫"/>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-6">
                        <div class="checkout__order" style="width: 400px;">
                            <h5 style="text-align: center; margin-bottom: 15px;"><b>Thông tin thanh toán</b></h5>
                            <div class="checkout__order__total">Tổng tiền hàng<span><fmt:formatNumber value="${order.totalMoney - order.ship}" pattern="#,###.### ₫"/></span></div>
                            <div class="checkout__order__total">Phí vận chuyển<span id="shipping-fee"><fmt:formatNumber value="${order.ship}" pattern="#,###.### ₫"/></span></div>
                            <c:if test="${not empty discount}">
                                <div class="checkout__order__total">Voucher từ shop<span><fmt:formatNumber value="${discount}" pattern="#,###.### ₫"/></span></div>
                            </c:if>
                            <div class="checkout__order__total">Thành tiền<span id="total"><fmt:formatNumber value="${not empty discount ? order.totalMoney - discount : order.totalMoney}" pattern="#,###.### ₫"/></span></div>
                            <input type="hidden" id="total-weight" name="total-weight" value="${totalWeight}">
                            <div class="payment" id="payment-method" style="font-size: 16px;">
                                <b>Chọn hình thức thanh toán</b>
                                <div class="radio-group">
                                    <div class="radio-item">
                                        <input type="radio" id="qr" name="paymentMethod" value="qr">
                                        <label for="qr">Quét mã QR</label>
                                    </div>
                                    <div class="radio-item">
                                        <input type="radio" id="credit" name="paymentMethod" value="vnpay">
                                        <label for="credit">Vnpay</label>
                                    </div>
                                    <div class="radio-item">
                                        <input type="radio" id="momo" name="paymentMethod" value="momo">
                                        <label for="momo">Momo</label>
                                    </div>
                                    <div class="radio-item">
                                        <input type="radio" id="cod" name="paymentMethod" value="cod">
                                        <label for="cod">Thanh toán khi nhận hàng</label>
                                    </div>
                                </div>
                                <div class="error-message" id="payment-error">Vui lòng chọn một hình thức thanh toán</div>
                            </div>
                            <input style="padding: 10px 110px; border: none; margin-top: 10px;" type="button" class="primary-btn" id="payment-button" value="Thanh toán">
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</section>

<!-- Modal xác thực thanh toán -->
<div class="modal-overlay" id="confirmationModal">
    <div class="modalV-content">
        <div class="modalV-header">Thông báo</div>
        <div class="modalV-body">
            Bạn cần xác thực đơn hàng bằng chữ ký điện tử để chứng minh là người mua chính chủ. Đơn hàng chỉ được phê duyệt khi ở trạng thái đã xác thực!
        </div>
        <div class="modalV-footer">
            <form action="PaymentInsertControll" method="GET" style="display: inline;">
                <button type="submit" class="cancel" id="cancel-payment">Để sau</button>
            </form>
            <form action="VerifyOrderControll" method="GET" style="display: inline;">
                <button type="submit" class="confirm">Bắt đầu</button>
            </form>
        </div>
    </div>
</div>

<!-- Modal for QR Code Payment -->
<div id="qrModal">
    <div id="qrModalContent">
        <img id="QRCODE-Img" src="" alt="QR Code" style="width: 80%; border-radius: 20px;">
        <h4 style="color: white; text-align: center; margin-top: 15px;"><b>Mã QR thanh toán tự động</b></h4>
        <p style="color: white; text-align: center; margin-top: 5px;">(Xác nhận tự động - Thường không quá 3')</p>
        <div class="qrModal-footer">
            <span>Đang chờ thanh toán <span id="countdown"></span></span>
            <div id="progressBar">
                <div id="progress"></div>
            </div>
        </div>
    </div>
</div>

<!-- Js Plugins -->
<script src="../../assets/js/payment.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/axios/0.21.1/axios.min.js"></script>
<jsp:include page="./footer/footer.jsp"></jsp:include>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const paymentButton = document.getElementById('payment-button');
        const form = document.getElementById('form');
        const paymentError = document.getElementById('payment-error');

        paymentButton.addEventListener('click', function () {
            if (!validatePaymentMethod()) {
                paymentError.classList.add('show');
                return;
            }
            paymentError.classList.remove('show');

            const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked').value;
            switch (paymentMethod) {
                case 'vnpay':
                    form.action = '/VNPayPaymentServlet';
                    form.submit();
                    break;
                case 'qr':
                    document.getElementById('confirmationModal').style.display = 'flex';
                    break;
                case 'momo':
                    form.action = '/MoMoPaymentServlet';
                    form.submit();
                    break;
                case 'cod':
                    form.action = '/PaymentInsertControll';
                    form.submit();
                    break;
                default:
                    paymentError.classList.add('show');
            }
        });

        // Clear error message when a payment method is selected
        document.querySelectorAll('input[name="paymentMethod"]').forEach(radio => {
            radio.addEventListener('change', () => {
                paymentError.classList.remove('show');
            });
        });
    });

    function validatePaymentMethod() {
        const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked');
        return !!paymentMethod; // Returns true if a payment method is selected, false otherwise
    }
</script>
</body>
</html>