<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ page isELIgnored="false" %>

<html>
<head>

    <style>.container .row .col-lg-6 ul li {
        margin-right: 30px;
    }

    </style>

</head>
<body>
<c:set var="log" value="Login/Sign up"></c:set>


<div class="header__top">
    <div class="container">
        <div class="row">
            <div class="col-lg-6">
                <div class="header__top__left">
                    <ul>
                        <li><i class="fa fa-envelope"></i> hello@colorlib.com</li>
                        <li>Free Shipping for all Order of $99</li>
                    </ul>
                </div>
            </div>
            <div class="col-lg-6">
                <div class="header__top__right">
                    <div class="header__top__right__social">
                        <a href="#"><i class="fa fa-facebook"></i></a>
                        <a href="#"><i class="fa fa-twitter"></i></a>
                        <a href="#"><i class="fa fa-linkedin"></i></a>
                        <a href="#"><i class="fa fa-pinterest-p"></i></a>
                    </div>
                    <div class="header__top__right__language">
                        <img src="assets/img/usa.png" alt="">
                        <img src="assets/img/vietnam.png" alt="">
                        <div>Vietnamese</div>
                        <span class="arrow_carrot-down"></span>
                        <ul>
                            <li><a href="#">Korean</a></li>
                            <li><a href="#">English</a></li>
                        </ul>
                    </div>
                    <c:if test="${empty sessionScope.account}">
                        <div class="header__top__right__auth">
                            <a href="./LoginControll"><i><img
                                    src="assets/img/login.png" alt=""></i>Đăng nhập/Đăng ký</a>
                        </div>
                    </c:if>

                    <c:if test="${not empty sessionScope.account}">
                        <div class="header__top__right__auth">
                            <!-- Dropdown for Logged-in User -->
                            <div class="dropdown">
                                <c:url var="indexAd" value="IndexAdminControll"></c:url>
                                <c:url var="profile" value="UpdateProfileControll"></c:url>
                                <c:url var="changepass" value="ChangePassword"></c:url>
                                <c:url var="logout" value="LogoutControll"></c:url>
                                <c:url var="report" value="ReportControll"></c:url>

                                <ul>
                                    <i><img src="assets/img/avatar.png" alt=""><span
                                            id="customer-name">${sessionScope.account.name}</span><i
                                            class="fa fa-caret-down"></i></i>
                                </ul>
                                <div class="dropdown-content">
                                    <c:set var="role" value="${sessionScope.account.role}"></c:set>
                                    <c:if test="${role.id == 1 or role.id==2 or role.id == 3 or role.id == 4 or role.id ==5}">
                                        <a href="./IndexAdminControll">
                                            <b>Quản lý</b></a>
                                    </c:if>
                                    <a href="./OrderPlaceControl">
                                        <b>Đơn đã đặt</b></a>
                                    <a href="./VoucherWareHouse">
                                        <b>Kho voucher</b></a>
                                    <c:if test="${not empty sessionScope.account.telephone}">
                                        <a href="${pageContext.request.contextPath}/${profile}">
                                            <b>Tài khoản</b></a>
                                        <a href="${pageContext.request.contextPath}/${changepass}">
                                            <b>Đổi mật khẩu</b></a>
                                    </c:if>
                                    <c:if test="${not empty hasKey}">
                                    <a href="${pageContext.request.contextPath}/${report}">
                                        <b>Report key</b></a>
                                    </c:if>
                                    <a href="${pageContext.request.contextPath}/${logout}">
                                        <b>Đăng xuất</b></a>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Header Bottom -->
<div class="container">
    <div class="row">
        <!-- Logo Section -->
        <div class="col-lg-3">
            <div class="header__logo">
                <a href="./IndexControll"><img src="assets/img/logo.png" alt="logo"></a>
            </div>
        </div>
        <!-- Navigation Section -->
        <div class="col-lg-6">
            <nav class="header__menu">
                <ul>
                    <li><a href="./IndexControll">Trang chủ</a></li>
                    <li><a href="./ShowProductControl?cid=0">Sản phẩm</a>
                        <!-- Dropdown Menu for Product Categories -->
                        <ul class="header__menu__dropdown">
                            <li class=""><a href="./ShowProductControl?cid=0">Tất cả</a></li>
                            <li class=""><a href="./ShowProductControl?cid=1">Rau lá</a></li>
                            <li class=""><a href="./ShowProductControl?cid=2">Củ quả</a></li>
                            <li class=""><a href="./ShowProductControl?cid=3">Trái cây</a></li>
                            <li class=""><a href="./ShowProductControl?cid=4">Nấm</a></li>

                        </ul>
                    </li>
                    <li><a href="./VoucherControll">Voucher</a></li>
                    <%--                <c:if test="${sessionScope.account!=null}">
                                        <li><a href="/OrderPlaceControl">
                                            <b>Đơn đã đặt</b>
                                        </a></li>
                                    </c:if>--%>
                    <li><a href="./BlogControll">Blog</a></li>
                    <li><a href="./ContactControll">Liên hệ</a></li>
                </ul>
            </nav>
        </div>
        <!-- Cart Section -->
        <div class="col-lg-3">
            <div class="header__cart">
                <ul>
                    <li><a href="./FavoriteProduct"> <img style="width: 35px; height: 35px;color: #a11515"
                                                          src="assets/img/love.png" alt=""></i> <%--<span>1</span>--%>
                    </a></li>
                    <li><a href="./CartControll">
                        <i><img style="width: 40px; height: 40px;" src="assets/img/cart.svg" alt=""></i>
                        <c:if test="${empty sessionScope.size}"><span id="cart-count">0</span></c:if>
                        <c:if test="${not empty sessionScope.size}"><span
                                id="cart-count">${sessionScope.size}</span></c:if>
                    </a></li>
                </ul>
            </div>

        </div>
    </div>
</div>
</header>

<section class="hero hero-normal">
    <div class="container">
        <div class="row">
            <div class="col-lg-3">
                <div class="hero__categories">
                    <div class="hero__categories__all">
                        <i class="fa fa-bars"></i>
                        <span>Những loại rau quả</span>
                    </div>
                    <ul>
                        <li><a href="#">Rau xanh lá</a></li>
                        <li><a href="#">Quả dâu</a></li>
                        <li><a href="#">Quả lựu</a></li>
                        <li><a href="#">Quả lê</a></li>
                        <li><a href="#">Quả táo</a></li>
                        <li><a href="#">Cà chua</a></li>
                        <li><a href="#">Cà rốt</a></li>
                        <li><a href="#">Bí đỏ</a></li>
                        <li><a href="#">Bơ</a></li>
                        <li><a href="#">Dưa chuột</a></li>
                        <li><a href="#">Cải bắp cải</a></li>
                    </ul>
                </div>
            </div>
            <!-- Hero Search -->
            <div class="col-lg-9">
                <div class="hero__search">
                    <div class="hero__search__form">
                        <!-- Search URL -->
                        <form action="/SearchControl" method="get" class="form-inline sk-search-in-nav">
                            <!-- Search Categories Dropdown -->
                            <div class="hero__search__categories">
                                <h6 style="padding-bottom: 10px"><b>Tất cả danh mục</b></h6>
                            </div>
                            <!-- Search Input -->
                            <input oninput="searchByName(this)" value="${txtSearch}" type="text"
                                   placeholder="Bạn cần gì?"
                                   name="query">
                            <!-- Search Button -->
                            <%--                           <button class="site-btn">
                                                           Tìm kiếm
                                                       </button>--%>
                        </form>
                    </div>
                    <!-- Hero Phone Contact -->
                    <div class="hero__search__phone">
                        <div class="hero__search__phone__icon">
                            <i class="fa fa-phone"></i>

                        </div>
                        <div class="hero__search__phone__text">
                            <h5>+65 11.188.888</h5>
                            <span>Hỗ trợ 24/7</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- Messenger Chat Plugin -->
    <div id="fb-root"></div>
    <div id="fb-customer-chat" class="fb-customerchat"></div>

</section>
</body>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
    var chatbox = document.getElementById('fb-customer-chat');
    chatbox.setAttribute("page_id", "YOUR_PAGE_ID");
    chatbox.setAttribute("attribution", "biz_inbox");

    window.fbAsyncInit = function () {
        FB.init({
            xfbml: true,
            version: 'v11.0'
        });
    };

    (function (d, s, id) {
        var js, fjs = d.getElementsByTagName(s)[0];
        if (d.getElementById(id)) return;
        js = d.createElement(s);
        js.id = id;
        js.src = 'https://connect.facebook.net/en_US/sdk/xfbml.customerchat.js';
        fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));

    function searchByName(param) {
        var txtSearch = param.value;
        $.ajax({
            url: "/SearchControl",
            type: "get",
            data: {
                query: txtSearch
            },
            success: function (data) {
                var row = document.getElementById("content");
                row.innerHTML = data;
            },
            error: function (xhr) {

            }
        });

    }
</script>
<script>
    (function (d, w, c) {
        w.ChatraID = 'j9M9yfx67fg6YvW5J';

        var s = d.createElement('script');
        w[c] = w[c] || function () {
            (w[c].q = w[c].q || []).push(arguments);
        };
        s.async = true;
        s.src = 'https://call.chatra.io/chatra.js';
        if (d.head) d.head.appendChild(s);
    })(document, window, 'Chatra');
</script>

</html>