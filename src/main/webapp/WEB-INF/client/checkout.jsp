<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Check Out</title>
    <!-- Css Styles -->
    <jsp:include page="./link/link.jsp"></jsp:include>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            padding-bottom: 5vh;
        }

        td {
            padding-top: 10px;
            padding-bottom: 20px;
            text-align: center;
        }

        .suggestion {
            padding: 10px;
            border: 1px solid #ddd;
            cursor: pointer;
        }

        .suggestion:hover {
            background-color: #f0f0f0;
        }

        .xBNaac {
            background-image: repeating-linear-gradient(45deg, #6fa6d6, #6fa6d6 33px, transparent 0, transparent 41px, #f18d9b 0, #f18d9b 74px, transparent 0, transparent 82px);
            background-position-x: -30px;
            background-size: 116px 3px;
            height: 3px;
            width: 100%;
            margin-bottom: 25px;
        }

        /* Add this CSS to your stylesheet */
        .checkout__input {
            margin-bottom: 20px;
            margin-right: 20px;
            /*display: flex;*/
            /*flex-direction: row;*/
            position: relative;
        }

        .checkout__input select {
            display: block;
            width: 100%;
            padding: 10px;
            /*margin-right: 20px;*/
            font-size: 14px;
            line-height: 1.5;
            color: #495057;
            background-color: #fff;
            background-clip: padding-box;
            border: 1px solid #ced4da;
            border-radius: 4px;
            transition: border-color .15s ease-in-out, box-shadow .15s ease-in-out;
            /*margin-bottom: 15px;*/
            /*appearance: none;*/
        }

        .checkout__input select:focus {
            border-color: #80bdff;
            outline: 0;
            /*box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, .25);*/
        }

        .checkout__input .dropdown-content {
            display: none;
            position: absolute;
            background-color: #cce9ff;
            min-width: 100%;
            box-shadow: 0px 8px 16px 0px #cce9ff;
            z-index: 1;
        }

        .checkout__input .dropdown-content select {
            display: block;
            width: 100%;
            border: none;
            box-shadow: none;
            padding: 10px;
            margin: 0;
            background-color: #cce9ff;
        }

        .checkout__input .dropdown-content select:hover {
            background-color: #cce9ff;
        }

        .radio-item {
            margin-left: 10px;
            margin-top: 10px;

        }

        .radio-group {
            display: flex;
            flex-direction: column;
            gap: 10px; /* Khoảng cách giữa các nút radio */
            margin-top: 10px; /* Khoảng cách phía trên */
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
            color: #ffcc00;
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
            background-color: rgb(0, 0, 0);
            background-color: rgba(0, 0, 0, 0.4);
            justify-content: center;
            align-items: center;
        }

        #qrModalContent {
            background-color: #3c506f;
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
        }

        .error-messages {
            color: red;
            font-size: 16px;
            /*margin-top: 10px;*/
        }
    </style>
    <!-- Include Google Places API -->
    <script src="https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&libraries=places"></script>

</head>
<body>
<jsp:include page="./header/header.jsp"></jsp:include>
<c:url var="pay" value="/OrderControll"></c:url>

<!-- Checkout Section Begin -->
<section class="checkout spad">
    <div class="container">
        <div class="checkout__form">
            <h4>Thanh toán</h4>
            <div class="xBNaac"></div>
            <form action="${pay}" method="get" onsubmit="return validate()">
                <div class="row" style="background-color: white; padding: 10px">
                    <div class="col-lg-8 col-md-6" style="max-width: 63.666667%">
                        <div class="row">
                            <div class="col-lg-6">
                                <div class="checkout__input">
                                    <p>Họ và tên<span>*</span></p>
                                    <input type="text" name="name" id="c-name" value="${name}"
                                           oninput="clearError('name-error')">
                                    <span class="error-message" id="name-error"></span>
                                </div>
                            </div>
                            <div class="col-lg-6">
                                <div class="checkout__input">
                                    <p>Số điện thoại<span>*</span></p>
                                    <input type="tel" name="phone" id="c-phone" value="${phone}"
                                           oninput="clearError('phone-error')">
                                    <span class="error-message" id="phone-error"></span>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="checkout__input" style="margin-left: 12px">
                                <p>Chọn tỉnh thành<span>*</span></p>
                                <div class="dropdown">
                                    <select class="form-select form-select-sm mb-3" name="city" id="city"
                                            aria-label=".form-select-sm">
                                        <option value="" selected>Chọn tỉnh thành</option>
                                    </select>
                                    <div class="dropdown-content" id="dropdown-city">
                                        <!-- Options will be dynamically inserted here -->
                                    </div>
                                </div>
                                <input type="hidden" name="cityName" id="cityName">
                                <div class="error-messages" id="city-error"></div>
                            </div>
                            <div class="checkout__input">
                                <p>Chọn quận/huyện<span>*</span></p>
                                <div class="dropdown">
                                    <select class="form-select form-select-sm mb-3" name="district" id="district"
                                            aria-label=".form-select-sm">
                                        <option value="" selected>Chọn quận/huyện</option>
                                    </select>
                                    <div class="dropdown-content" id="dropdown-district">
                                        <!-- Options will be dynamically inserted here -->
                                    </div>
                                </div>
                                <input type="hidden" name="districtName" id="districtName">
                                <div class="error-messages" id="district-error"></div>
                            </div>
                            <div class="checkout__input">
                                <p>Chọn phường/xã<span>*</span></p>
                                <div class="dropdown">
                                    <select class="form-select form-select-sm mb-3" name="ward" id="ward"
                                            aria-label=".form-select-sm">
                                        <option value="" selected>Chọn phường/xã</option>
                                    </select>
                                    <div class="dropdown-content" id="dropdown-ward">
                                        <!-- Options will be dynamically inserted here -->
                                    </div>
                                </div>
                                <input type="hidden" name="wardName" id="wardName">
                                <div class="error-messages" id="ward-error"></div>
                            </div>
                        </div>
                        <div class="checkout__input">
                            <p>Địa chỉ nhà<span>*</span></p>
                            <input type="text" id="address" name="address" placeholder="Địa chỉ nhà"
                                   class="checkout__input__add" oninput="clearError('address-error')">
                            <span class="error-message" id="address-error"></span>
                        </div>
                        <div id="suggestions"></div>

                        <div class="checkout__input">
                            <p>Ghi chú cho đơn hàng<span></span></p>
                            <input type="text" id="notes" name="notes" value="" placeholder="Ghi chú">
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-6">
                        <div class="checkout__order" style="width: 400px;overflow: hidden;">
                            <h5 style="text-align: center"><b>Danh Sách Sản Phẩm Đã Đặt</b></h5>
                            <table>
                                <thead>
                                <tr>
                                    <th class="shoping__product">Sản phẩm</th>
                                    <th>Giá</th>
                                    <th>Số lượng</th>
                                    <th>Thành tiền</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${cart}" var="o">
                                    <c:set var="orderDetail" value="${o.value}"/>
                                    <tr>
                                        <td class="shoping__cart__item"
                                            style="text-align: left;">${orderDetail.product.name}</td>
                                        <td class="shoping__cart__price">
                                            <fmt:formatNumber value="${orderDetail.product.price}"
                                                              pattern="#,###.### ₫"/>
                                        </td>
                                        <td class="shoping__cart__quantity">
                                                ${orderDetail.quantity}
                                        </td>
                                        <td class="shoping__cart__total">
                                            <fmt:formatNumber value="${orderDetail.price}" pattern="#,###.### ₫"/>
                                        </td>

                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                            <div class="checkout__order__total">Tạm tính<span id="total"><fmt:formatNumber
                                    value="${total}"
                                    pattern="#,###.### ₫"/></span>
                            </div>
                            <c:if test="${not empty discount}">
                                <div class="checkout__order__total">Giảm giá<span id="voucher"><fmt:formatNumber
                                        value="${discount}"
                                        pattern="#,###.### ₫"/></span>
                                </div>
                            </c:if>
                            <c:if test="${not empty discount}">
                            <div class="checkout__order__total">Thành tiền<span id="lastTotal"><fmt:formatNumber
                                    value="${total - discount}"
                                    pattern="#,###.### ₫"/></span>
                                </c:if>
                            </div>
                            <input type="hidden" id="total-weight" name="total-weight" value="${totalWeight}">
                            <input type="hidden" id="discount-value" name="discount-value" value="${discountValue}">
                            <div style="text-align: center; margin-top: -100px;margin-left: 65px">
                                <input style="border: none; margin-top: 45px;margin-right: 10px" type="submit"
                                       id="submit-btn"
                                       class="primary-btn" value="Tiếp tục thanh toán">  </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</section>
<!-- Checkout Section End -->

<script src="https://cdnjs.cloudflare.com/ajax/libs/axios/0.21.1/axios.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        var citySelect = document.getElementById('city');
        var districtSelect = document.getElementById('district');
        var wardSelect = document.getElementById('ward');

        citySelect.addEventListener('click', function () {
            var dropdownCity = document.getElementById('dropdown-city');
            dropdownCity.style.display = 'block';
        });

        districtSelect.addEventListener('click', function () {
            var dropdownDistrict = document.getElementById('dropdown-district');
            dropdownDistrict.style.display = 'block';
        });

        wardSelect.addEventListener('click', function () {
            var dropdownWard = document.getElementById('dropdown-ward');
            dropdownWard.style.display = 'block';
        });

        window.onclick = function (event) {
            if (!event.target.matches('.form-select')) {
                var dropdowns = document.getElementsByClassName("dropdown-content");
                for (var i = 0; i < dropdowns.length; i++) {
                    var openDropdown = dropdowns[i];
                    if (openDropdown.style.display === 'block') {
                        openDropdown.style.display = 'none';
                    }
                }
            }
        };

        var Parameter = {
            url: "https://raw.githubusercontent.com/kenzouno1/DiaGioiHanhChinhVN/master/data.json",
            method: "GET",
            responseType: "application/json",
        };
        var promise = axios(Parameter);
        promise.then(function (result) {
            renderCity(result.data);
        });

        function renderCity(data) {
            for (const x of data) {
                var option = document.createElement('option');
                option.value = x.Id;
                option.text = x.Name;
                citySelect.appendChild(option);
            }
            citySelect.onchange = function () {
                districtSelect.length = 1;
                wardSelect.length = 1;
                if (this.value != "") {
                    const result = data.filter(n => n.Id === this.value);
                    for (const k of result[0].Districts) {
                        var option = document.createElement('option');
                        option.value = k.Id;
                        option.text = k.Name;
                        districtSelect.appendChild(option);
                    }
                }
            };
            districtSelect.onchange = function () {
                wardSelect.length = 1;
                const dataCity = data.filter((n) => n.Id === citySelect.value);
                if (this.value != "") {
                    const dataWards = dataCity[0].Districts.filter(n => n.Id === this.value)[0].Wards;
                    for (const w of dataWards) {
                        var option = document.createElement('option');
                        option.value = w.Id;
                        option.text = w.Name;
                        wardSelect.appendChild(option);
                    }
                }
            };
        }

        // Submit form data to servlet
        document.getElementById('submit-btn').addEventListener('click', function () {
            var cityName = citySelect.options[citySelect.selectedIndex].text;
            var districtName = districtSelect.options[districtSelect.selectedIndex].text;
            var wardName = wardSelect.options[wardSelect.selectedIndex].text;

            document.getElementById('cityName').value = cityName;
            document.getElementById('districtName').value = districtName;
            document.getElementById('wardName').value = wardName;

            document.getElementById('location-form').submit();
        });
    });


    function validate() {
        const nameInput = document.getElementById('c-name');
        const phoneInput = document.getElementById('c-phone');
        const citySelect = document.getElementById('city');
        const districtSelect = document.getElementById('district');
        const wardSelect = document.getElementById('ward');
        const addressInput = document.getElementById('address');


        const nameError = document.getElementById('name-error');
        const phoneError = document.getElementById('phone-error');
        const cityError = document.getElementById('city-error');
        const districtError = document.getElementById('district-error');
        const wardError = document.getElementById('ward-error');
        const addressError = document.getElementById('address-error');


        let valid = true;

        if (nameInput.value.trim() === '') {
            nameError.textContent = 'Vui lòng nhập tên';
            valid = false;
        } else {
            nameError.textContent = '';
        }

        if (phoneInput.value.trim() === '') {
            phoneError.textContent = 'Vui lòng nhập số điện thoại';
            valid = false;
        } else {
            phoneError.textContent = '';
        }

        if (citySelect.value.trim() === '') {
            cityError.textContent = 'Chưa chọn tỉnh thành';
            valid = false;
        } else {
            cityError.textContent = '';
        }

        if (districtSelect.value.trim() === '') {
            districtError.textContent = 'Chưa chọn quận/huyện';
            valid = false;
        } else {
            districtError.textContent = '';
        }

        if (wardSelect.value.trim() === '') {
            wardError.textContent = 'Chưa chọn phường/xã';
            valid = false;
        } else {
            wardError.textContent = '';
        }

        if (addressInput.value.trim() === '') {
            addressError.textContent = 'Địa chỉ nhà là bắt buộc.';
            valid = false;
        } else {
            addressError.textContent = '';
        }

        return valid;
    }

    document.getElementById('city').addEventListener('change', function () {
        clearError('city-error');
    });

    document.getElementById('district').addEventListener('change', function () {
        clearError('district-error');
    });
    document.getElementById('ward').addEventListener('change', function () {
        clearError('ward-error');
    });

    function clearError(elementId) {
        document.getElementById(elementId).textContent = "";
    }

</script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        var citySelect = document.getElementById('city');
        var districtSelect = document.getElementById('district');
        var wardSelect = document.getElementById('ward');

        citySelect.addEventListener('click', function () {
            var dropdownCity = document.getElementById('dropdown-city');
            dropdownCity.style.display = 'block';
        });

        districtSelect.addEventListener('click', function () {
            var dropdownDistrict = document.getElementById('dropdown-district');
            dropdownDistrict.style.display = 'block';
        });

        wardSelect.addEventListener('click', function () {
            var dropdownWard = document.getElementById('dropdown-ward');
            dropdownWard.style.display = 'block';
        });

        window.onclick = function (event) {
            if (!event.target.matches('.form-select')) {
                var dropdowns = document.getElementsByClassName("dropdown-content");
                for (var i = 0; i < dropdowns.length; i++) {
                    var openDropdown = dropdowns[i];
                    if (openDropdown.style.display === 'block') {
                        openDropdown.style.display = 'none';
                    }
                }
            }
        };

        // Load options from JSON and populate select elements
        var Parameter = {
            url: "https://raw.githubusercontent.com/kenzouno1/DiaGioiHanhChinhVN/master/data.json",
            method: "GET",
            responseType: "application/json",
        };
        var promise = axios(Parameter);
        promise.then(function (result) {
            renderCity(result.data);
        });

        function renderCity(data) {
            for (const x of data) {
                var option = document.createElement('option');
                option.value = x.Id;
                option.text = x.Name;
                citySelect.appendChild(option);
            }
            citySelect.onchange = function () {
                districtSelect.length = 1;
                wardSelect.length = 1;
                if (this.value != "") {
                    const result = data.filter(n => n.Id === this.value);
                    for (const k of result[0].Districts) {
                        var option = document.createElement('option');
                        option.value = k.Id;
                        option.text = k.Name;
                        districtSelect.appendChild(option);
                    }
                }
            };
            districtSelect.onchange = function () {
                wardSelect.length = 1;
                const dataCity = data.filter((n) => n.Id === citySelect.value);
                if (this.value != "") {
                    const dataWards = dataCity[0].Districts.filter(n => n.Id === this.value)[0].Wards;
                    for (const w of dataWards) {
                        var option = document.createElement('option');
                        option.value = w.Id;
                        option.text = w.Name;
                        wardSelect.appendChild(option);
                    }
                }
            };
        }
    });

</script>

<!-- Js Plugins -->
<jsp:include page="./footer/footer.jsp"></jsp:include>
</body>
</html>
