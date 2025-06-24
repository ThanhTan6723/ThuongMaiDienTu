<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
    <title>Cart</title>
    <jsp:include page="./link/link.jsp"></jsp:include>
    <style>
        .shoping__cart__table {
            background-color: #ffffff;
            /*border-radius: 9px;*/
            padding: 25px;
            margin-top: 25px;
        }

        .modal {
            display: none;
            position: fixed;
            z-index: 1;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgb(0, 0, 0);
            background-color: rgba(0, 0, 0, 0.4);
        }

        .modal-content {
            background-color: #fefefe;
            margin: 100px auto; /* Cách phía trên 100px và tự động căn giữa ngang */
            padding: 20px;
            border: 1px solid #888;
            width: 40%; /* Độ dài 50% */
            /*box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);*/
        }

        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }

        .close:hover,
        .close:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
        }

        .voucher-item {
            margin-bottom: 10px;
        }

        body.modal-open {
            overflow: hidden;
        }

        .voucher-search {
            display: flex;
            align-items: center;
            background-color: #f9f9f9;
            padding: 10px;
            border: 1px solid #e0e0e0;
            /*border-radius: 5px;*/
            margin-bottom: 20px;
        }

        .voucher-search label {
            margin-right: 10px;
            font-weight: bold;
        }

        .voucher-search input {
            flex-grow: 1;
            padding: 10px;
            border: 1px solid #e0e0e0;
            /*border-radius: 5px;*/
            margin-right: 10px;
        }

        .voucher-search button {
            padding: 10px 20px;
            background-color: #e0e0e0;
            border: none;
            /*borderradius-: 5px;*/
            cursor: not-allowed;
            color: #a0a0a0;
        }

        .voucher-search button.active {
            background-color: #86b23e;
            color: white;
            cursor: pointer;
        }

        .voucher-item {
            margin-bottom: 10px;
        }

        .voucher-input {
            display: flex;
            align-items: center;
            background-color: #f9f9f9;
            padding: 10px;
            border: 1px solid #e0e0e0;
            /*border-radius: 5px;*/
            margin-bottom: 20px;
            width: fit-content;
        }

        .voucher-input label {
            margin-right: 10px;
            font-weight: bold;
        }

        .voucher-input input {
            flex-grow: 1;
            padding: 10px;
            border: 1px solid #e0e0e0;
            /*border-radius: 5px;*/
            margin-right: 10px;
        }

        .voucher-input button {
            padding: 10px 20px;
            background-color: #e0e0e0;
            border: none;
            /*border-radius: 5px;*/
            cursor: not-allowed;
            color: #a0a0a0;
        }

        .voucher-input button.apply {
            background-color: #7fad39;
            color: white;
            cursor: pointer;
        }

        /* Add this CSS to your stylesheet */

        .voucher-modal-footer {
            display: flex;
            justify-content: flex-end;
            margin-top: 20px;
        }

        .voucher-modal-footer {
            display: flex;
            justify-content: flex-end;
            margin-top: 20px;
        }

        .voucher-modal-footer button {
            padding: 10px 20px;
            margin-left: 10px;
            width: 120px;
            border: none;
            background-color: #7fad39;
            color: white;
            cursor: pointer;
            /*border-radius: 4px;*/
            font-size: 14px;
            /*font-weight: bold;*/
            /*box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);*/
            transition: background-color 0.3s, box-shadow 0.3s;
        }

        .voucher-modal-footer button:hover {
            background-color: #7ba639;
            /*box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);*/
        }

        #backButton {
            background-color: #ffffff; /* Red color */
            color: #777777;
            border: 0.5px solid #c0c0c0;
        }

        #okButton {
            background-color: #7fad39; /* Red color */
        }

        /* Voucher List Styles */
        .voucher-list-container {
            max-height: 300px;
            overflow-y: auto;
            border: 1px solid #ddd;
            /*border-radius: 5px;*/
            padding: 10px;
            background: #f9f9f9;
        }

        .voucher-list {
            display: flex;
            flex-direction: column;
        }

        .voucher-item {
            display: flex;
            padding: 10px;
            margin-bottom: 10px;
            background: #fff;
            border: 1px solid #ddd;
            border-radius: 8px;
            position: relative;
        }

        .voucher-left {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            padding: 10px;
            background: #bde4e2;
            border-radius: 12px;
            width: 120px;
            text-align: center;
            border-right: 1px solid #ddd;
        }

        .voucher-left img {
            width: 50px;
            height: 50px;
            margin-bottom: 5px;
        }

        .voucher-left span {
            font-weight: bold;
            color: #3b3b3b;
        }

        .voucher-right {
            display: flex;
            flex-direction: column;
            flex: 1;
            padding: 10px;
            justify-content: space-between;
            position: relative;
        }

        .voucher-right-top {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .voucher-desc {
            /*font-weight: bold;*/
            margin-bottom: 5px;
        }

        .voucher-expiry {
            font-size: 12px;
            color: #888;
        }

        .voucher-expiry a {
            color: #007bff;
            text-decoration: none;
        }

        .voucher-expiry a:hover {
            text-decoration: underline;
        }

        .voucher-quantity {
            background: #f8d7da;
            color: #721c24;
            border-radius: 50%;
            padding: 5px 10px;
            font-size: 12px;
            margin-left: 10px;
        }

        .voucher-select {
            position: absolute;
            right: 10px;
            bottom: 10px;
        }

        .voucher-select input {
            /*position: absolute;*/
            /*opacity: 5px;*/
            /*cursor: pointer;*/
            /*height: 0;*/
            /*width: 0;*/
        }

        .voucher-select input:checked ~ .checkmark {
            background-color: #007bff;
        }
        .voucher-remove {
            cursor: pointer;
            color: red;
            margin-right: 5px;
        }

        .cancel-voucher {
            color: red;
        }
        .voucher-select input:checked ~ .checkmark:after {
            display: block;
        }

        /* Thêm lớp CSS cho voucher-item khi được chọn */
        .voucher-item.selected {
            background-color: #e2faf4;
            border: 1px solid #35a2ef;
        }

        /* Thêm lớp CSS cho thông báo lỗi */
        .error-message {
            color: red;
            font-weight: bold;
            display: none;
        }
        .shoping__discountt{
            margin-top: 20px;
            margin-right: 350px;
        }
        .button-voucher {
            display: flex;
            align-items: center;
            padding: 10px 20px;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        .voucher-selected {
            position: relative;
        }

        .voucher-remove {
            position: absolute;
            top: -6px;
            right: 10px;
            background-color: white;
            border-radius: 50%;
            width: 20px;
            height: 20px;
            display: flex;
            justify-content: center;
            align-items: center;
            cursor: pointer;
            color: red;
            font-weight: bold;
        }

        .voucher-remove:hover {
            background-color: #f8d7da;
        }

    </style>
</head>
<body>
<jsp:include page="header/header.jsp"></jsp:include>
<!-- Shoping Cart Section Begin -->
<section class="shoping-cart spad">
    <div class="container">
        <div class="row">
            <div class="col-lg-12">
                <div class="empty-cart-message" style="display: none;">
                    <div class="img" style="text-align: center; line-height: 50vh;">
                        <img alt="" src="assets/img/empty-cart.svg" style="vertical-align: middle;">
                        <h3><b>Giỏ hàng rỗng</b></h3>
                        <p>Hiện tại bạn chưa có sản phẩm nào trong giỏ hàng. Hãy dạo quanh cửa hàng để chọn được sản
                            phẩm ưng ý nhé!</p>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="shoping__cart__btn"
                                 style=" display: block; margin: 0 auto; text-align: center;">
                                <c:url var="showProduct" value="ShowProductControl"></c:url>
                                <a href="${pageContext.request.contextPath}/${showProduct}"
                                   class="primary-btn cart-btn">Tiếp tục mua sắm</a>
                            </div>
                        </div>
                    </div>
                </div>
                <c:if test="${not empty cart}">
                <div class="shoping__cart__table">
                    <table>
                        <thead>
                        <tr>
                            <th class="shoping__product">Sản phẩm</th>
                            <th>Đơn giá</th>
                            <th>Số lượng</th>
                            <th>Thành tiền</th>
                            <th>Xóa</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${cart}" var="entry">
                            <c:set var="orderDetail" value="${entry.value}"/>
                            <tr id="row_${entry.key}">
                                <td class="shoping__cart__item">
                                    <a href="DetailControl?pid=${orderDetail.product.id}">
                                        <img style="width: 73px; height: auto;"
                                             src="${orderDetail.product.image}" width="150px" height="60px">
                                    </a>
                                    <a href="DetailControl?pid=${orderDetail.product.id}">
                                        <h5>${orderDetail.product.name}</h5>
                                    </a>
                                </td>
                                <td class="shoping__cart__price">${orderDetail.product.price}₫</td>
                                <td class="shoping__cart__quantity">
                                    <div class="quantity">
                                        <div class="pro-qty">
                                            <span class="dec qtybtn decrease-btn"
                                                  data-key="${orderDetail.product.id}">-</span>
                                            <input type="text" value="${orderDetail.quantity}" min="1"
                                                   id="updates_${entry.key}" readonly>
                                            <span class="inc qtybtn increase-btn"
                                                  data-key="${orderDetail.product.id}">+</span>
                                        </div>
                                    </div>
                                </td>
                                <td class="shoping__cart__total total-price_${entry.key}">
                                        ${orderDetail.price}₫
                                </td>
                                <td class="shoping__cart__item__close">
                                    <span class="icon_close remove-btn" data-key="${entry.key}"></span>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                        <%--                    </c:if>--%>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-12">
                    <div class="shoping__cart__btns">
                        <c:url var="showProduct" value="ShowProductControl"></c:url>
                        <a href="${pageContext.request.contextPath}/${showProduct}"
                           class="primary-btn cart-btn">Tiếp tục mua sắm</a>
<%--                        <a href="#" class="primary-btn cart-btn cart-btn-right"><span class="icon_loading"></span>--%>
<%--                            Upadate Cart</a>--%>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="shoping__continue" style="margin-top: 30px">
                        <div class="row">
                            <div class="col-lg-6"><h5 style="margin-right: 50px;"><b>Voucher</b></h5></div>
                            <div class="col-lg-6">
                <span>
                    <a href="#" class="apply">
                        <span style="color: #6fa6d6;" id="voucher-input">Chọn hoặc nhập mã
                            <i class="fa fa-plus"></i>
                        </span>
                    </a>
                </span>
                            </div>
                            <div class="voucher-selected" style="margin-top: 30px">
                                <!-- Nơi hiển thị voucher đã chọn -->
                            </div>
                        </div>
                    </div>
                </div>


                <div class="col-lg-6" style="margin-left: 580px">
                    <div class="shoping__checkout" style="background-color: #f5f5f5">
                        <h5><b>Thanh Toán</b></h5>
                        <ul>
                            <li>Tạm tính<span id="originalTotalAmount" class="total-amount"><fmt:formatNumber
                                    value="${total}"
                                    pattern="#,###.### ₫"/></span></li>
                            <li>Giảm giá<span id="discountAmount" name="discountAmount">0</span></li>
                            <li>Tổng tiền<span id="totalAmount" class="total-amount2"><fmt:formatNumber
                                    value="${total}"
                                    pattern="#,###.### ₫"/></span></li>
                        </ul>
                        <ul>
<%--                            <li>Vouncher--%>
                                    <%--                                <span id="voucher-applied" style="display: none;" class="voucher-value">Đã áp mã</span></li>--%>
                        </ul>
                        <a href="./CheckOutControll" class="primary-btn">Thanh toán</a>
                    </div>
                </div>
                </c:if>
            </div>
        </div>
    </div>
</section>

<!-- Shoping Cart Section End -->
<!-- Voucher Modal Begin -->
<div id="voucherModal" class="modal">
    <div class="modal-content">
        <h5>Chọn Voucher</h5><br>
        <div class="voucher-search">
            <label for="voucherCode">Mã Voucher</label>
            <input type="text" id="voucherCode" name="voucher-search" placeholder="Nhập mã voucher">
            <button id="applySearchVoucher" disabled>ÁP DỤNG</button>
        </div>
<%--        <span id="error" style="text-align: center;color: red"></span>--%>
        <div class="voucher-list-container">
            <div class="voucher-list">
                <c:if test="${empty savedVouchers}">
                    <span style="text-align: center; color: #707070">Bạn chưa lưu voucher nào</span>
                </c:if>
                <c:forEach var="voucher" items="${savedVouchers}">
                    <c:set var="voucherApplies" value="false"/>
                    <c:choose>
                        <c:when test="${voucher.discountType.type == 'All'}">
                            <c:set var="voucherApplies" value="true"/>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${cart}" var="cart">
                                <c:if test="${voucher.product != null && voucher.product.id == cart.value.product.id}">
                                    <c:set var="voucherApplies" value="true"/>
                                </c:if>
                                <c:if test="${voucher.category != null && voucher.category.id  == cart.value.product.category.id}">
                                    <c:set var="voucherApplies" value="true"/>
                                </c:if>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    <div class="voucher-item" data-voucher="${voucher.id}" data-expiry="${voucher.endDate}" data-minimum-value="${voucher.minimumOrderValue}"
                         <c:if test="${!voucherApplies}">style="opacity: 0.5; pointer-events: none;"</c:if>>
                        <div class="voucher-left">
                            <img src="/images/voucher.png" alt="Voucher Image">
                            <span>${voucher.code}</span>
                        </div>
                        <div class="voucher-right">
                            <div class="voucher-right-top">
                                <p class="voucher-desc">Giảm tới ${voucher.discountPercentage}%<br>${voucher.discountType.type}<br></p>
                                <p class="voucher-desc">Đơn tối thiểu ${voucher.minimumOrderValue}<br></p>
                                <p class="voucher-desc">Giảm tối đa ${voucher.maximumDiscount}</p>
                                <span class="voucher-quantity">x1</span>
                            </div>
                            <p class="voucher-expiry">Sắp hết hạn: ${voucher.endDate} <a href="#">Điều Kiện</a></p>
                            <label class="voucher-select">
                                <input type="radio" name="voucher" value="${voucher.id}">
                            </label>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
        <div class="voucher-modal-footer">
            <button id="backButton">TRỞ LẠI</button>
            <button id="okButton" <c:if test="${empty savedVouchers}">disabled
                    style="background-color: #b6b6b6" </c:if>>OK
            </button>
        </div>
    </div>
</div>
<!-- Voucher Modal End -->
<!-- Footer Section Begin -->
<jsp:include page="./footer/footer.jsp"></jsp:include>
<!-- Add jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function () {
        // Kiểm tra giỏ hàng trống khi trang vừa tải
        checkEmptyCart();
        checkVoucherExpiry();

        // Mở modal khi click vào "Chọn hoặc nhập mã"
        $('.apply').on('click', function (event) {
            event.preventDefault();
            $('#voucherModal').css('display', 'block');
            $('body').addClass('modal-open');
        });

        // Xử lý khi click vào nút "TRỞ LẠI" trên modal
        $("#backButton").click(function () {
            // Đóng modal
            $('#voucherModal').css('display', 'none');
            // Xóa lớp modal-open khỏi thẻ body
            $('body').removeClass('modal-open');
        });

        // Đóng modal khi click vào nút đóng
        $('.close').on('click', function () {
            $('#voucherModal').css('display', 'none');
            $('body').removeClass('modal-open');
        });

        // Đóng modal khi click ra ngoài modal
        $(window).on('click', function (event) {
            if ($(event.target).is('#voucherModal')) {
                $('#voucherModal').css('display', 'none');
                $('body').removeClass('modal-open');
            }
        });

        $("#okButton").click(function () {
            var selectedVoucherId = $("input[name='voucher']:checked").val();
            var minimumValue = parseFloat($("input[name='voucher']:checked").closest('.voucher-item').data('minimum-value'));
            if (selectedVoucherId) {
                var totalAmount = parseFloat($("#originalTotalAmount").text().replace(/[^\d.-]/g, ''));
                console.log(totalAmount);
                console.log(minimumValue);
                if (totalAmount >= minimumValue) {
                    applyVoucher(selectedVoucherId, totalAmount);
                } else {
                    alert("Tổng số tiền không đủ để áp dụng voucher này.");
                }
            } else {
                alert("Vui lòng chọn một voucher.");
            }
        });

        // Handle voucher item click
        $(".voucher-item").on("click", function () {
            var radioButton = $(this).find("input[type='radio']");

            // Kiểm tra trạng thái của radio button
            if (radioButton.prop("checked")) {
                // Nếu đã được chọn, bỏ chọn lại
                radioButton.prop("checked", false);
                $(this).removeClass("selected");
            } else {
                // Bỏ chọn tất cả các voucher khác trước khi chọn mới
                $(".voucher-item").removeClass("selected");
                $("input[type='radio'][name='voucher']").prop("checked", false);

                // Chọn voucher và đánh dấu màu viền
                radioButton.prop("checked", true);
                $(this).addClass("selected");
            }
        });

        // Handle radio button click
        $("input[type='radio'][name='voucher']").on("change", function () {
            if ($(this).prop("checked")) {
                // Bỏ chọn tất cả các voucher khác trước khi chọn mới
                $(".voucher-item").removeClass("selected");
                $("input[type='radio'][name='voucher']").prop("checked", false);

                // Đánh dấu màu viền cho voucher được chọn
                $(this).closest(".voucher-item").addClass("selected");
            } else {
                // Nếu không được chọn, loại bỏ màu viền
                $(this).closest(".voucher-item").removeClass("selected");
            }
        });

        // Kích hoạt nút "ÁP DỤNG" khi nhập mã voucher
        $('#voucherCode').on('input', function () {
            var code = $(this).val().trim();
            if (code) {
                $('#applySearchVoucher').prop('disabled', false).addClass('active');
            } else {
                $('#applySearchVoucher').prop('disabled', true).removeClass('active');
            }
        });

        function applyVoucher(voucherId, totalAmount) {
            console.log("Applying voucher with ID:", voucherId, "and total amount:", totalAmount);

            $.ajax({
                type: "POST",
                url: "ApplyVoucherControll",
                data: {
                    voucherId: voucherId,
                    totalAmount: totalAmount
                },
                success: function (response) {
                    console.log(response);
                    var discountValue = parseFloat(response.discountValue);
                    var finalAmount = parseFloat(response.finalAmount);

                    // Cập nhật giảm giá và tổng tiền cuối cùng trong UI
                    $("#discountAmount").text(discountValue.toLocaleString('vi-VN', {
                        style: 'currency',
                        currency: 'VND'
                    }));
                    $("#totalAmount").text(finalAmount.toLocaleString('vi-VN', {
                        style: 'currency',
                        currency: 'VND'
                    }));

                    alert('Áp voucher thành công');
                    document.getElementById('voucherModal').style.display = 'none';
                    document.body.classList.remove('modal-open');
                    // Thêm sự kiện click cho nút xóa
                    $(".voucher-remove").on("click", function () {
                        $(".voucher-selected").empty(); // Xóa voucher đã chọn
                        document.querySelector("#discountAmount").textContent = "0 VND";
                        document.querySelector("#totalAmount").textContent = totalAmount.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
                    });

                    // Thay đổi nút áp dụng voucher thành nút hủy bỏ voucher
                    $("#voucher-input").html('Bỏ chọn voucher <i class="fa fa-times"></i>');
                    $("#voucher-input").addClass("cancel-voucher");

                    // Thêm sự kiện click cho nút hủy bỏ voucher
                    $(".cancel-voucher").on("click", function () {
                        $(".voucher-selected").empty(); // Xóa voucher đã chọn
                        document.querySelector("#discountAmount").textContent = "0";
                        document.querySelector("#totalAmount").textContent = totalAmount.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });

                        // Thay đổi lại nút hủy bỏ voucher thành nút áp dụng voucher
                        $("#voucher-input").html('Chọn hoặc nhập mã <i class="fa fa-plus"></i>');
                        $("#voucher-input").removeClass("cancel-voucher");
                    });

                },
                error: function () {
                    alert("Đã xảy ra lỗi khi áp dụng voucher. Vui lòng thử lại sau."); // Xử lý lỗi khi request không thành công
                }
            });
        }


        function applySearchVoucher() {
            var voucherCode = document.getElementById('voucherCode').value;
            console.log("Applying voucher with ID:", voucherCode, "and total amount:", totalAmount);
            var totalAmount = parseFloat($("#originalTotalAmount").text().replace(/[^\d.-]/g, '')); // Giá trị totalAmount cần lấy từ input hoặc từ nơi khác

            fetch('ApplySearchVoucher', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: 'voucherCode=' + encodeURIComponent(voucherCode) + '&totalAmount=' + encodeURIComponent(totalAmount)
            })
                .then(function (response) {
                    if (!response.ok) {
                        throw new Error('Yêu cầu không thành công - ' + response.status);
                    }
                    return response.text(); // Đọc phản hồi dưới dạng văn bản
                })
                .then(function (responseText) {
                    var response = JSON.parse(responseText); // Phân tích văn bản thành đối tượng JSON

                    if (response.success) {
                        var discountValue = parseFloat(response.discountValue);
                        var finalAmount = parseFloat(response.finalAmount);
                        // Cập nhật giảm giá và tổng tiền cuối cùng trong UI
                        document.querySelector("#discountAmount").textContent = discountValue.toLocaleString('vi-VN', {
                            style: 'currency',
                            currency: 'VND'
                        });

                        document.querySelector("#totalAmount").textContent = finalAmount.toLocaleString('vi-VN', {
                            style: 'currency',
                            currency: 'VND'
                        });
                        alert('Áp voucher thành công');
                        document.getElementById('voucherModal').style.display = 'none';
                        document.body.classList.remove('modal-open');
                        $('#voucherCode').val('');
                        document.querySelector("#error").textContent = '';

                        // Thêm sự kiện click cho nút xóa
                        $(".voucher-remove").on("click", function () {
                            $(this).closest(".voucher-item").remove();
                            document.querySelector("#discountAmount").textContent = "0 VND";
                            document.querySelector("#totalAmount").textContent = totalAmount.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'});
                        });
                    } else {
                        document.querySelector("#error").textContent = response.message;
                    }
                })
                .catch(function (error) {
                    console.error('Lỗi:', error);
                });
        }

        // Áp dụng voucher tìm kiếm
        $('#applySearchVoucher').on('click', applySearchVoucher);

        function updateQuantity(key, action) {
            $.ajax({
                type: "GET",
                url: action + "?key=" + key,
                success: function (response) {
                    // Cập nhật số lượng trong UI
                    $("#updates_" + key).val(response.quantity);
                    // Cập nhật tổng giá tiền cho sản phẩm
                    $(".total-price_" + key).text(response.totalPrice + "₫");
                    // Cập nhật tổng tiền trong UI
                    var totalAmount = response.totalAmount;
                    $('.total-amount').text(totalAmount + '₫');
                    $('.total-amount2').text(totalAmount + '₫');
                    $("#discountAmount").text('0');
                    // // Cập nhật giảm giá nếu có voucher
                    // var selectedVoucherId = $("input[name='voucher']:checked").val();
                    // var voucherCode = document.getElementById('voucherCode').value;
                    // if (selectedVoucherId) {
                    //     applyVoucher(selectedVoucherId, totalAmount);
                    // }
                    // if (voucherCode) {
                    //     applySearchVoucher();
                    // }
                    checkEmptyCart();
                }
            });
        }

        // Function to handle increasing quantity
        $(".increase-btn").click(function (e) {
            e.preventDefault();
            var key = $(this).data("key");
            updateQuantity(key, "IncreaseQuantityControll");
        });

        // Function to handle decreasing quantity
        $(".decrease-btn").click(function (e) {
            e.preventDefault();
            var key = $(this).data("key");
            updateQuantity(key, "DecreaseQuantityControll");
        });

        // Function to handle removing item from the cart
        $(".remove-btn").click(function (e) {
            e.preventDefault();
            var key = $(this).data("key");
            $.ajax({
                type: "GET",
                url: "DeleteOrderControll?key=" + key,
                success: function (response) {
                    // Remove the row from the cart in the UI
                    $("#row_" + key).remove();
                    // Update the total amount in the UI
                    var totalAmount = response.totalAmount;
                    $('.total-amount').text(totalAmount + '₫');
                    $('.total-amount2').text(totalAmount + '₫');
                    // Update cart size in the UI
                    $("#cart-count").text(response.sizeCart);
                    // Cập nhật giảm giá nếu có voucher
                    var selectedVoucherId = $("input[name='voucher']:checked").val();
                    if (selectedVoucherId) {
                        applyVoucher(selectedVoucherId, totalAmount);
                    }
                    if (voucherCode) {
                        applySearchVoucher();
                    }
                    // Kiểm tra giỏ hàng rỗng
                    checkEmptyCart();
                }
            });
        });

        function toggleCartDisplay(isEmpty) {
            var cartTable = $(".shoping__cart__table");
            var emptyCartMessage = $(".empty-cart-message");
            var checkoutSection = $(".shoping__checkout");
            var continueSection = $(".shoping__continue");
            var cartBtnsSection = $(".shoping__cart__btns");

            if (isEmpty) {
                cartTable.hide();
                emptyCartMessage.show();
                checkoutSection.hide();
                continueSection.hide();
                cartBtnsSection.hide();
            } else {
                cartTable.show();
                emptyCartMessage.hide();
                checkoutSection.show();
                continueSection.show();
                cartBtnsSection.show();
            }
        }

        // Hàm kiểm tra hạn sử dụng của voucher
        function checkVoucherExpiry() {
            $(".voucher-item").each(function () {
                var expiryDate = $(this).data("expiry");
                if (expiryDate) {
                    var currentDate = new Date();
                    var expiry = new Date(expiryDate);
                    if (expiry < currentDate) {
                        $(this).css("display", "none");
                    }
                }
            });
        }

        function checkEmptyCart() {
            var cartTable = $(".shoping__cart__table");
            var isEmpty = $(".shoping__cart__table table tbody tr").length === 0;

            toggleCartDisplay(isEmpty);
        }
    });

</script>
</body>
</html>