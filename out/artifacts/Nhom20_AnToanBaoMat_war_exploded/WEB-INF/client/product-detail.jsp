<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8"/>
    <title>Product detail</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

    <style type="text/css">
        .product__details__pic__slider__nav {
            position: relative;
        }

        .owl-prev,
        .owl-next {
            position: absolute;
            top: 50%;
            transform: translateY(-50%);
            z-index: 1;
            margin: 0 -30px;
        }

        .owl-next {
            right: 0;
        }

        .hidden {
            display: none;
        }

        .review-statistics {
            margin-bottom: 20px;
        }

        .review-statistics h4 {
            font-size: 24px;
            margin-bottom: 10px;
        }

        .stars {
            display: flex;
            flex-direction: column;
            gap: 10px;
        }

        .stars .rating-row {
            display: flex;
            align-items: center;
            font-size: 16px;
        }

        .stars .rating-row span {
            width: 30px;
            text-align: center;
        }

        .stars .rating-row .bar {
            flex-grow: 0.5;
            height: 10px;
            background-color: #e0e0e0;
            margin: 0 10px;
            border-radius: 5px;
            position: relative;
        }

        .stars .rating-row .bar .fill {
            height: 100%;
            background-color: #ffa600;
            border-radius: 5px;
        }

        .stars .rating-row .percentage {
            width: 50px;
            text-align: right;
        }

        .star-icon {
            color: #ffa600;
        }

        .review-container {
            max-width: 800px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .reviews {
            margin-top: 20px;
        }

        .review-item {
            border-bottom: 1px solid #e0e0e0;
            padding: 15px 0;
        }

        .review-item:last-child {
            border-bottom: none;
        }

        .review-item h3 {
            margin: 0;
            font-size: 16px;
            display: flex;
            align-items: center;
        }

        .review-item h3 span {
            background-color: #ffa600;
            color: #fff;
            padding: 2px 5px;
            border-radius: 3px;
            margin-left: 10px;
            font-size: 14px;
        }

        .review-item p {
            margin: 10px 0;
            /*border-bottom: 1px solid #d9d9d9;*/
        }

        .review-item .response {
            background-color: #f1f1f1;
            padding: 10px;
            border-radius: 5px;
        }

        .review-item .response p {
            margin: 0;

        }

        .review-item .response .response-date {
            font-size: 12px;
            color: #888;
        }

        .review-footer {
            display: flex;
            justify-content: flex-start;
            margin-top: 20px;
        }

        .review-footer button {
            background-color: #7fad39;
            color: whitesmoke;
            margin-right: 20px;
            border: none;
            padding: 10px 30px;
            border-radius: 8px;
            cursor: pointer;
        }

        .review-footer button:hover {
            background-color: #77a233;
        }

        .review-form {
            display: none;
            margin-top: 20px;
        }

        .stars {
            cursor: pointer;
            font-size: 2em;
            color: gray;
            margin: 0 6px;
            border-radius: 30px;
        }


        .stars.selected {
            color: #ffa600;
        }

        .star {
            cursor: pointer;
            font-size: 2em;
            color: gray;
            margin: 0 6px;
            border-radius: 30px;
        }


        .star.selected {
            color: #ffa600;
        }

        /* Modal Styles */
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
            margin: 3% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 80%;
            max-width: 600px;
            /*border-radius: 15px;*/
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        /* CSS để ẩn scrollbar */
        body.modal-open {
            overflow: hidden;
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

        .modal-content form {
            display: flex;
            flex-direction: column;
        }

        .modal-content form label {
            margin-top: 10px;
        }

        .modal-content form textarea {
            width: 100%;
            border-radius: 10px;
            padding: 10px;
            margin-top: 5px;
            border: 1px solid #ccc;
        }

        .modal-content form button {
            align-self: flex-end;
            margin-top: 20px;
            background-color: #7fad39;
            color: #fff;
            border: none;
            padding: 10px 30px;
            border-radius: 40px;
            cursor: pointer;
        }

        .modal-content form button:hover {
            background-color: #8fbe41;
        }

        /* CSS cho hình ảnh xem trước */
        #image-preview {
            margin-top: 10px;
        }

        /* Đảm bảo ô nhập "Tên" và "Số điện thoại" nằm trên cùng một hàng và cách nhau ra */
        #reviewModal form .form-row {
            display: flex;
            justify-content: space-between; /* Cách nhau ra */
            align-items: center;
            /*margin-bottom: 5px;*/
        }

        #reviewModal form .form-group {
            flex: 0 0 calc(48% - 5px); /* Độ rộng của mỗi ô */
        }

        #reviewModal form .form-group label {
            display: block;
            /*margin-bottom: 5px;*/
        }

        #reviewModal form .form-group input[type="text"],
        #reviewModal form .form-group input[type="tel"] {
            width: 100%;
            padding: 5px;
            border-radius: 5px;
            border: 1px solid #ccc;
            box-sizing: border-box;
        }

        .preview-image {
            max-width: 50px; /* Kích thước mặc định */
            height: 50px;
            margin-right: 10px;
            margin-bottom: 10px;
        }

        .error {
            color: red;
            font-size: 12px;
        }
        .favorite-icon {
            fill: white;
            stroke: red;
            stroke-width: 2;
            cursor: pointer;
            margin-left: 5px; /* Khoảng cách giữa chữ và biểu tượng */
        }

        .favorite-icon.active .heart {
            fill: red;
            stroke: none;
        }

        .favorite-icon {
            fill: white;
            cursor: pointer;
            transition: fill 0.3s;
        }

        .favorite-icon.favorited {
            fill: red;
        }



        .reply {
            /*margin-left: 40px;*/
            width: 550px;
            padding: 5px;
            background-color: #f6f6f6;
            border-radius: 5px;
            margin-top: 10px;
        }

        .filter-container {
            display: none;
            justify-content: space-between;
            margin-top: 20px;
            margin-bottom: 20px;
        }

        .filter span, .sort span {
            margin-right: 10px;
            font-weight: bold;
        }

        .filter-btn {
            border: none;
            background-color: #f0f0f0;
            padding: 5px 10px;
            margin-right: 5px;
            cursor: pointer;
            border-radius: 30px;
        }

        .filter-btn:hover {
            background-color: #8fbe41;
            color: white;
        }

        .filter-btn.active {
            background-color: #8fbe41;
            color: white;
        }

        .sort {
            display: flex;
            flex-direction: row;
            align-items: center;
        }

        .sort select {
            padding: 5px;
            border-radius: 5px;
            border: 1px solid #ccc;
        }
        .disabled {
            opacity: 0.5; /* Make it faded */
            cursor: not-allowed; /* Change cursor to indicate it's not clickable */
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
            align-items: center;
            gap: 10px;
        }

        .notification-content span {
            font-weight: bold;
        }
    </style>
    <jsp:include page="./link/link.jsp"></jsp:include>
</head>

<body>
<div class="page">
    <span class="header__fixed">
        <jsp:include page="header/header.jsp"></jsp:include>
    </span>
    <section class="breadcrumb-section set-bg" data-setbg="assets/img/breadcrumb.jpg">
        <div class="container">
            <div class="row">
                <div class="col-lg-12 text-center">
                    <div class="breadcrumb__text">
                        <h2>Chi tiết sản phẩm</h2>
                        <div class="breadcrumb__option">
                            <a href="./IndexControll">Trang chủ</a>
                            <span>${detail.name} </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <div class="product-details spad">
        <div class="container">
            <div class="row">
                <div class="col-lg-6 col-md-6">
                    <div class="product__details__pic">
                        <div class="product__details__pic__item">
                            <img class="product__details__pic__item--large" src="${detail.image}" alt="">
                        </div>
                        <div class="product__details__pic__slider__nav">
                            <div class="owl-prev"><i class="fa fa-angle-left"></i></div>
                            <div class="product__details__pic__slider owl-carousel">
                                <c:forEach var="o" items="${listImageProduct}">
                                    <div class="product__details__pic__slider__item">
                                        <img data-imgbigurl="img/product/details/product-details-3.jpg" src="${o.url}"
                                             alt="">
                                    </div>
                                </c:forEach>
                            </div>
                            <div class="owl-next"><i class="fa fa-angle-right"></i></div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-6 col-md-6">
                    <div class="product__details__text">
                        <h3>${detail.name}</h3>
                        <div class="product__details__rating">
                            <i class="fa fa-star"></i>
                            <i class="fa fa-star"></i>
                            <i class="fa fa-star"></i>
                            <i class="fa fa-star"></i>
                            <i class="fa fa-star-half-o"></i>
                            <c:if test="${not empty reviews}"> <span>(${allreviews} Đánh giá)</span></c:if>
                        </div>
                        <h6>Lượt xem sản phẩm:${detail.viewCount} </h6>
                        <h6>Đã bán: ${daban}</h6>
                        <div class="product__details__price">

                            <fmt:formatNumber value="${detail.price}" pattern="#,###.### ₫" />
                            / ${detail.weight}kg
                        </div>
                        <b>Phân loại: </b> ${nameCategory}<br>
                        <b>Kho: </b>
                        <span id="batchQuantity">
<%--                            ${productCurrentQuantities == 0 ? 'Hết hàng' : productCurrentQuantities}--%>
                            ${productCurrentQuantities}
                        </span>
                        <c:url var="addToCart" value="AddToCartControl"></c:url>
                        <br>
                        <form id="addToCartForm" action="${addToCart}?pid=${detail.id}" method="post">
                            <b>Số lượng:</b> <input style="width: 80px; border-radius: 5px; text-align: center;"
                                                    type="number" class="single-input-selector" value="1"
                                                    min="1" max="99" name="quantity"
                                                    placeholder="">
                            <br>
                            <br>
<%--                            <button style="padding: 10px 23px; border-radius: 30px; border: none; background-color: #7fad39; text-transform: uppercase; font-weight: 700; color: #fff;--%>
<%--                                    opacity: ${productCurrentQuantities == 0 ? '0.5' : '1'};--%>
<%--                                    cursor: ${productCurrentQuantities == 0 ? 'not-allowed' : 'pointer'};"--%>
<%--                                    type="submit"--%>
<%--                                    class="button"--%>
<%--                                    title="<c:out value='Đặt hàng' />"--%>
<%--                            ${productCurrentQuantities == 0 ? 'disabled' : ''}>--%>
<%--                                <span><c:out value="Đặt hàng"/></span>--%>
<%--                            </button>--%>

                            <button style="padding: 10px 23px; border-radius: 30px; border: none; background-color: #7fad39; text-transform: uppercase; font-weight: 700; color: #fff;
                                   ;"
                                    type="submit"
                                    class="button"
                                    title="<c:out value='Đặt hàng' />">
                            <span><c:out value="Đặt hàng"/></span>
                            </button>
                        </form>
                        <form id="favoriteForm">

                            <ul>
                                <input type="hidden" id="productId" value="${detail.id}" />
                                <li>
                                    <b>Yêu thích: </b>
                                    <svg class="favorite-icon" viewBox="0 0 24 24" width="24" height="24">
                                        <path class="heart" d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" />
                                    </svg>
                                </li>
                                <li><b>Share on</b>
                                    <div class="share">
                                        <a href="#"><i class="fa fa-facebook"></i></a>
                                        <a href="#"><i class="fa fa-twitter"></i></a>
                                        <a href="#"><i class="fa fa-instagram"></i></a>
                                        <a href="#"><i class="fa fa-pinterest"></i></a>
                                    </div>
                                </li>
                            </ul>
                        </form>

                    </div>
                </div>
                <div class="col-lg-12">
                    <div class="product__details__tab">
                        <ul class="nav nav-tabs" role="tablist">
                            <li class="nav-item">
                                <a class="nav-link active" data-toggle="tab" href="#tabs-1" role="tab">Mô tả</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link " data-toggle="tab" href="#tabs-2" role="tab">Nhà cung cấp</a>
                            </li>

                            <li class="nav-item">
                                <a class="nav-link" data-toggle="tab" href="#tabs-3" role="tab">Đánh giá</a>
                            </li>
                        </ul>
                        <div class="tab-content">
                            <div class="tab-pane active" id="tabs-1" role="tabpanel">
                                <div class="product__details__tab__desc">
                                    <h6>Thông tin sản phẩm</h6>
                                    <p>Cân nặng : ${detail.weight} kg</p>
                                    <p>Mô tả : ${detail.description}</p>
                                </div>
                            </div>
                            <div class="tab-pane " id="tabs-2" role="tabpanel">
                                <div class="product__details__tab__desc">
                                    <h6>Thông tin về nhà cung cấp</h6>
                                    <c:forEach var="o" items="${listProvider}">
                                        <p>-Tên nhà cung cấp: ${o.name}</p>
                                        <p>Địa chỉ: ${o.address}</p>
                                    </c:forEach>
                                </div>
                            </div>

                            <div class="tab-pane" id="tabs-3" role="tabpanel">
                                <div class="product__details__tab__desc">
                                    <div class="review-container">
                                        <div class="reviews">
                                            <h4><b>Đánh giá cho sản phẩm ${detail.name}</b></h4>
                                            <br>
                                            <c:if test="${empty reviews}"><h5 style="color: gray">Chưa có đánh giá nào
                                                cho sản phẩm này</h5></c:if>
                                            <c:if test="${not empty reviews}">
                                                <div class="review-statistics">
                                                    <h5><b>${averageRating} <i
                                                            class="fa fa-star star-icon"></i> ${allreviews} Đánh giá</b>
                                                    </h5>
                                                    <br>
                                                    <div class="stars">
                                                        <c:forEach var="entry" items="${ratingPercentage}">
                                                            <c:set var="i" value="${entry.key}"/>
                                                            <div class="rating-row">
                                                                <span>${i} <i class="fa fa-star star-iconS"
                                                                              style="color:#9a9a9a;"></i></span>
                                                                <div class="bar">
                                                                    <div class="fill"
                                                                         style="width: ${entry.value}%;"></div>
                                                                </div>
                                                                <span class="percentage">${entry.value}%</span>
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                    <div class="filter-container" id="filterContainer">
                                                        <div class="filter">
                                                            <span>Lọc đánh giá</span>
                                                            <button class="filter-btn" data-rating="all">Tất cả</button>
                                                            <button class="filter-btn" data-rating="1">1 <i
                                                                    class="fa fa-star star-icon"></i></button>
                                                            <button class="filter-btn" data-rating="2">2 <i
                                                                    class="fa fa-star star-icon"></i></button>
                                                            <button class="filter-btn" data-rating="3">3 <i
                                                                    class="fa fa-star star-icon"></i></button>
                                                            <button class="filter-btn" data-rating="4">4 <i
                                                                    class="fa fa-star star-icon"></i></button>
                                                            <button class="filter-btn" data-rating="5">5 <i
                                                                    class="fa fa-star star-icon"></i></button>
                                                        </div>
                                                        <div class="sort">
                                                            <span>Xếp theo:</span>
                                                            <select id="sort-options">
                                                                <option value="newest">Mới nhất</option>
                                                                <option value="high-rating">Đánh giá cao</option>
                                                                <option value="low-rating">Đánh giá thấp</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div id="review-list">
                                                    <!-- Reviews will be updated here -->
                                                    <c:forEach var="review" items="${reviews}" varStatus="loop">
                                                        <div class="review-item ${loop.index > 1 ? 'hidden' : ''}">
                                                            <h3><b>${review.nameCommenter}</b>
                                                                <c:forEach var="i" begin="1" end="${review.rating}">
                                                                    <i class="fa fa-star stars selected"></i>
                                                                </c:forEach>
                                                                <c:forEach var="i" begin="${review.rating + 1}" end="5">
                                                                    <i class="fa fa-star stars"></i>
                                                                </c:forEach>
                                                            </h3>
                                                            <c:if test="${not empty review.image}">
                                                                <br>
                                                                <img style="width: 100px;height: 100px;border-radius: 6px"
                                                                     src="${review.image}">
                                                            </c:if>
                                                            <div class="cmt"
                                                                 style="background-color: #fff5e3; margin-top: 15px;padding: 10px; width:60%; border-radius: 5px">
                                                                <h3>${review.comment}</h3>
                                                            </div>
                                                            <p>${review.dateCreated}</p>
                                                            <c:if test="${not empty review.reply}">
                                                                <span><img src="/images/tick.png"></span><span
                                                                    style="font-size: 16px"><b>Golden Fields</b></span>
                                                                <span>Đã trả lời</span>
                                                                <div class="reply">
                                                                    <h3>${review.reply}</h3>
                                                                    <p class="reply-date">${review.dateReply}</p>
                                                                </div>
                                                            </c:if>
                                                        </div>
                                                    </c:forEach>
                                                </div>
                                            </c:if>
                                        </div>
                                        <div class="review-footer">
                                            <c:if test="${not empty reviews and allreviews >= 3}">
                                                <button id="showAllReviewsBtn"
                                                        style="background-color: #f6f6f6;color: black;"><b>Xem tất
                                                    cả ${allreviews} đánh giá</b></button>
                                            </c:if>
                                            <button id="writeReviewBtn">Viết đánh giá</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="reviewModal" class="modal">
                        <div class="modal-content">
                            <div class="exit"><span class="close">&times;</span></div>
                            <h3 style="text-align: center;margin-bottom: 20px">Viết đánh giá của bạn</h3>
                            <form id="reviewForm" action="ReviewControll" method="post" enctype="multipart/form-data">
                                <input type="hidden" id="id" name="id" value="${detail.id}">
                                <%--                                <label for="rating">Đánh giá</label>--%>
                                <div id="rating" style="text-align: center;">
                                    <i class="fa fa-star star" style="font-size: 25px" data-value="1"></i>
                                    <i class="fa fa-star star" style="font-size: 25px" data-value="2"></i>
                                    <i class="fa fa-star star" style="font-size: 25px" data-value="3"></i>
                                    <i class="fa fa-star star" style="font-size: 25px" data-value="4"></i>
                                    <i class="fa fa-star star" style="font-size: 25px" data-value="5"></i>
                                </div>
                                <span class="error" id="ratingError"></span>
                                <input type="hidden" id="ratingInput" name="rating" value="0">
                                <label for="comment">Bình luận</label>
                                <textarea id="comment" name="comments" rows="6"
                                          placeholder="Mời bạn chia sẻ cảm nhận..."></textarea>
                                <span class="error" id="commentError"></span>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="name">Họ tên (bắt buộc)</label>
                                        <input type="text" id="name" name="name">
                                        <span class="error" id="nameError"></span>
                                    </div>
                                    <div class="form-group">
                                        <label for="phone">Số điện thoại (bắt buộc)</label>
                                        <input type="tel" id="phone" name="phone">
                                        <span class="error" id="phoneError"></span>
                                    </div>
                                </div>
                                <label for="images">Gửi ảnh thực tế</label>
                                <span id="image-preview"></span>
                                <input type="file" id="images" name="images" accept="image/*" multiple>
                                <button type="submit">Gửi</button>
                            </form>
                        </div>
                    </div>

                </div>
            </div>

        </div>
    </div>
    <div class="row">
        <div class="col-lg-12">
            <div class="section-title">
                <h2>Các sản phẩm cùng loại</h2>
            </div>
        </div>
    </div>
    <c:url var="detail" value="DetailControl"></c:url>

    <section class="categories">
        <div class="container">
            <div class="row">
                <div class="categories__slider owl-carousel">
                    <c:forEach var="p" items="${listProductSame}">
                        <div class="col-lg-3">
                            <div class="categories__item set-bg">
                                <a href="${detail}?pid=${p.id}"><img src="${p.image}"></a>
                                <h5><a href="${detail}?pid=${p.id}">${p.name}</a></h5>
                            </div>
                        </div>

                    </c:forEach>
                </div>
            </div>
        </div>
    </section>
</div>

<jsp:include page="footer/footer.jsp"></jsp:include>
</div>
<script src="assets/js/jquery-3.3.1.min.js"></script>
<script src="assets/js/bootstrap.min.js"></script>
<script src="assets/js/jquery.nice-select.min.js"></script>
<script src="assets/js/jquery-ui.min.js"></script>
<script src="assets/js/jquery.slicknav.js"></script>
<script src="assets/js/mixitup.min.js"></script>
<script src="assets/js/owl.carousel.min.js"></script>
<script src="assets/js/main.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const stars = document.querySelectorAll('.star');
        const ratingInput = document.getElementById('ratingInput');
        const ratingError = document.getElementById('ratingError');
        const comment = document.getElementById('comment');
        const commentError = document.getElementById('commentError');
        const name = document.getElementById('name');
        const nameError = document.getElementById('nameError');
        const phone = document.getElementById('phone');
        const phoneError = document.getElementById('phoneError');
        const reviewForm = document.getElementById('reviewForm');

        stars.forEach(star => {
            star.addEventListener('click', () => {
                const value = star.getAttribute('data-value');
                ratingInput.value = value;
                stars.forEach(s => s.classList.remove('selected'));
                for (let i = 0; i < value; i++) {
                    stars[i].classList.add('selected');
                }
                ratingError.textContent = '';
            });
        });

        comment.addEventListener('input', () => {
            if (comment.value.trim() !== '') {
                commentError.textContent = '';
            }
        });

        name.addEventListener('input', () => {
            if (name.value.trim() !== '') {
                nameError.textContent = '';
            }
        });

        phone.addEventListener('input', () => {
            if (phone.value.trim() !== '') {
                phoneError.textContent = '';
            }
        });

        reviewForm.addEventListener('submit', (e) => {
            let isValid = true;

            // Check rating
            if (ratingInput.value === '0') {
                ratingError.textContent = 'Vui lòng chọn đánh giá';
                isValid = false;
            } else {
                ratingError.textContent = '';
            }

            // Check comment
            if (comment.value.trim() === '') {
                commentError.textContent = 'Vui lòng nhập bình luận';
                isValid = false;
            } else {
                commentError.textContent = '';
            }

            // Check name
            if (name.value.trim() === '') {
                nameError.textContent = 'Vui lòng nhập họ tên';
                isValid = false;
            } else {
                nameError.textContent = '';
            }

            // Check phone
            if (phone.value.trim() === '') {
                phoneError.textContent = 'Vui lòng nhập số điện thoại';
                isValid = false;
            } else {
                phoneError.textContent = '';
            }

            if (!isValid) {
                e.preventDefault();
            }
        });
    });
</script>
<script>
    $(document).ready(function () {
        $(".product__details__pic__slider").owlCarousel({
            items: 1,
            nav: false,
            dots: false,
            loop: true,
            autoplay: true,
            autoplayTimeout: 5000,
            autoplayHoverPause: true,
        });
        $(".owl-prev").click(function () {
            $(".product__details__pic__slider").trigger("prev.owl.carousel");
        });
        $(".owl-next").click(function () {
            $(".product__details__pic__slider").trigger("next.owl.carousel");
        });
    });

    var batchQuantities = {};

    // Xử lý form submission với AJAX
    $('#addToCartForm').on('submit', function (e) {
        e.preventDefault(); // Ngăn chặn form submit mặc định

        const form = $(this);
        const actionUrl = form.attr('action');
        const formData = form.serialize();
        const quantityInput = form.find('.single-input-selector');

        $.ajax({
            type: 'POST',
            url: actionUrl,
            data: formData,
            success: function (response) {
                // Xử lý phản hồi từ server
                // Hiển thị thông báo tùy chỉnh
                showCustomNotification('Sản phẩm đã được thêm vào Giỏ hàng');
                // Cập nhật kích thước giỏ hàng
                $('#cart-count').text(response.size);
                // Đặt lại giá trị input về 1
                quantityInput.val(1);
            },
            error: function (xhr) {
                if (xhr.status === 401) { // Kiểm tra mã lỗi 401
                    alert('Bạn cần đăng nhập để thêm sản phẩm vào giỏ hàng.');
                    window.location.href = '/LoginControll'; // Điều hướng đến trang đăng nhập
                } else {
                    alert('Có lỗi xảy ra. Vui lòng thử lại.');
                }
            }
        });
    });

    // Hàm hiển thị thông báo tùy chỉnh
    function showCustomNotification(message) {
        // Tạo một div thông báo
        var notification = document.createElement('div');
        notification.className = 'custom-notification';
        notification.innerHTML = `
        <div class="notification-content">
            <span>Thêm sản phẩm thành công</span>
        </div>
        `;

        // Thêm thông báo vào body
        document.body.appendChild(notification);

        // Tự động ẩn thông báo sau 2 giây
        setTimeout(function () {
            notification.classList.add('hide');
            setTimeout(function () {
                notification.remove();
            }, 300); // Thời gian cho animation ẩn
        }, 1000);
    }

    function updateQuantity() {
        var batchSelect = document.getElementById("batchSelect");
        var selectedBatchId = batchSelect.value;
        var quantity = batchQuantities[selectedBatchId] || 0;
        document.getElementById("batchQuantity").textContent = quantity;
    }

    window.onload = function () {
        var batches = document.querySelectorAll('#batchSelect option');
        batches.forEach(function (batch) {
            var batchId = batch.value;
            var quantity = batch.getAttribute('data-quantity');
            batchQuantities[batchId] = quantity;
        });
        updateQuantity();
    }


    $(document).ready(function () {
        // Xử lý đánh giá sao
        $('.star').click(function () {
            var rating = $(this).data('value');
            $('#ratingInput').val(rating);
            $('.star').removeClass('selected');
            $(this).prevAll().addBack().addClass('selected');
        });

        // Xử lý gửi form bằng AJAX
        $('#reviewForm').submit(function (event) {
            event.preventDefault(); // Ngăn chặn form gửi đi mặc định

            // Kiểm tra điều kiện trước khi gửi đánh giá
            if (!validateForm()) {
                return;
            }

            var formData = new FormData(this);

            $.ajax({
                url: $(this).attr('action'),
                type: $(this).attr('method'),
                data: formData,
                contentType: false,
                processData: false,
                success: function (response) {
                    // Xử lý phản hồi thành công từ server
                    if (response.success) {
                        alert('Đánh giá của bạn đã được gửi đi để phê duyệt');
                        $('#reviewModal').hide() // Đóng modal khi gửi thành công
                        $('#reviewForm')[0].reset(); // Reset form
                        $('#image-preview').text('');
                        $('.star').removeClass('selected'); // Xóa các đánh giá đã chọn

                    } else {
                        // Hiển thị thông báo lỗi từ server
                        $('#ratingError').text(response.errors.rating || '');
                        $('#commentError').text(response.errors.comments || '');
                        $('#nameError').text(response.errors.name || '');
                        $('#phoneError').text(response.errors.phone || '');
                    }
                },
                error: function (xhr, status, error) {
                    console.error('Error:', error);
                    alert('Đã xảy ra lỗi trong quá trình gửi đánh giá.');
                }
            });
        });

        // Đóng modal khi click ra bên ngoài
        $('.modal').click(function (event) {
            if ($(event.target).hasClass('modal')) {
                $('#reviewModal').modal('hide');
                resetForm(); // Reset form khi đóng modal
            }
        });


        // Đóng modal khi click vào nút đóng (x)
        $('.close').click(function () {
            $('#reviewModal').modal('hide');
            resetForm(); // Reset form khi đóng modal
        });

        // Xử lý khi input trong form thay đổi để xóa thông báo lỗi
        $('#comment').on('input', function () {
            $('#commentError').text('');
        });

        $('#name').on('input', function () {
            $('#nameError').text('');
        });

        $('#phone').on('input', function () {
            $('#phoneError').text('');
        });

        // Hàm kiểm tra và bắt lỗi trước khi gửi đánh giá
        function validateForm() {
            var isValid = true;

            // Check rating
            if ($('#ratingInput').val() === '0') {
                $('#ratingError').text('Vui lòng chọn đánh giá');
                isValid = false;
            } else {
                $('#ratingError').text('');
            }
            // Check comment
            if ($('#comment').val().trim() === '') {
                $('#commentError').text('Vui lòng nhập bình luận');
                isValid = false;
            } else {
                $('#commentError').text('');
            }
            // Check name
            if ($('#name').val().trim() === '') {
                $('#nameError').text('Vui lòng nhập họ tên');
                isValid = false;
            } else {
                $('#nameError').text('');
            }
            // Check phone
            if ($('#phone').val().trim() === '') {
                $('#phoneError').text('Vui lòng nhập số điện thoại');
                isValid = false;
            } else {
                $('#phoneError').text('');
            }
            return isValid;
        }

        // Hàm reset form và xóa thông báo lỗi
        function resetForm() {
            $('#reviewForm')[0].reset();
            $('.star').removeClass('selected');
            $('#ratingError').text('');
            $('#commentError').text('');
            $('#nameError').text('');
            $('#phoneError').text('');
            $('#image-preview').text('');
        }
    });

    function updateQuantity() {
        var batchSelect = document.getElementById("batchSelect");
        var selectedBatchId = batchSelect.value;
        var quantity = batchQuantities[selectedBatchId] || 0;
        document.getElementById("batchQuantity").textContent = quantity;
    }

    window.onload = function () {
        var batches = document.querySelectorAll('#batchSelect option');
        batches.forEach(function (batch) {
            var batchId = batch.value;
            var quantity = batch.getAttribute('data-quantity');
            batchQuantities[batchId] = quantity;
        });
        updateQuantity();
    }

    document.addEventListener('DOMContentLoaded', (event) => {
        const modal = document.getElementById("reviewModal");
        const btn = document.getElementById("writeReviewBtn");
        const span = document.getElementsByClassName("close")[0];

        btn.onclick = function () {
            modal.style.display = "block";
            document.body.classList.add("modal-open");
        }

        span.onclick = function () {
            modal.style.display = "none";
            document.body.classList.remove("modal-open");
        }

        window.onclick = function (event) {
            if (event.target == modal) {
                modal.style.display = "none";
                document.body.classList.remove("modal-open");
            }
        }


        const stars = document.querySelectorAll('.star');
        const ratingInput = document.getElementById('ratingInput');

        stars.forEach(star => {
            star.addEventListener('click', (e) => {
                const rating = e.target.getAttribute('data-value');
                ratingInput.value = rating;

                stars.forEach(star => {
                    if (star.getAttribute('data-value') <= rating) {
                        star.classList.add('selected');
                    } else {
                        star.classList.remove('selected');
                    }
                });
            });
        });
    });

    document.addEventListener('DOMContentLoaded', function () {
        const imagesInput = document.getElementById('images');
        const imagePreview = document.getElementById('image-preview');

        // Xử lý sự kiện khi người dùng chọn ảnh
        imagesInput.addEventListener('change', function () {
            // Xóa các hình ảnh hiện có trước đó
            imagePreview.innerHTML = '';

            // Lặp qua các tệp đã chọn
            for (let i = 0; i < imagesInput.files.length; i++) {
                const file = imagesInput.files[i];
                const reader = new FileReader();

                // Đọc tệp và hiển thị hình ảnh
                reader.onload = function (e) {
                    const img = document.createElement('img');
                    img.src = e.target.result;
                    img.alt = file.name;
                    img.classList.add('preview-image');
                    imagePreview.appendChild(img);
                }

                // Đọc tệp ảnh dưới dạng URL
                reader.readAsDataURL(file);
            }
        });
    });

    document.addEventListener('DOMContentLoaded', function () {
        const showAllReviewsBtn = document.getElementById('showAllReviewsBtn');
        const reviewItems = document.querySelectorAll('.review-item');

        showAllReviewsBtn.addEventListener('click', function () {
            // Hiển thị tất cả các đánh giá
            reviewItems.forEach(function (item) {
                item.classList.remove('hidden');
            });
            // Ẩn nút "Xem tất cả đánh giá" sau khi đã hiển thị tất cả
            showAllReviewsBtn.style.display = 'none';
        });
    });

    // document.addEventListener('DOMContentLoaded', function () {
    //     const showAllReviewsBtn = document.getElementById('showAllReviewsBtn');
    //     const reviewItems = document.querySelectorAll('.review-item');
    //     const writeReviewBtn = document.getElementById('writeReviewBtn');
    //
    //     showAllReviewsBtn.addEventListener('click', function () {
    //         // Hiển thị tất cả các đánh giá
    //         reviewItems.forEach(function (item) {
    //             item.classList.remove('hidden');
    //         });
    //         // Ẩn nút "Xem tất cả đánh giá" sau khi đã hiển thị tất cả
    //         showAllReviewsBtn.style.display = 'none';
    //         // Hiển thị nút "Viết đánh giá"
    //         writeReviewBtn.classList.remove('hidden');
    //     });
    // });
    var productId = $('#productId').val();

    $(document).ready(function() {
        // Hàm để kiểm tra và cập nhật trạng thái yêu thích khi trang được tải lại
        function updateFavoriteIcon(productId) {
            $.ajax({
                url: 'FavoriteStatusServlet',
                type: 'GET',
                data: { productId: productId },
                success: function(response) {
                    if (response === 'favorited') {
                        $('.favorite-icon').addClass('favorited');
                    } else {
                        $('.favorite-icon').removeClass('favorited');
                    }
                },
                error: function() {
                    console.error('Lỗi khi kiểm tra trạng thái yêu thích.');
                }
            });
        }

        // Xử lý khi trang được tải lại
        updateFavoriteIcon(productId);

        // Xử lý khi người dùng nhấn vào biểu tượng trái tim
        $(document).on('click', '.favorite-icon', function() {
            var productId = $('#productId').val();
            var $icon = $(this);

            $.ajax({
                url: 'FavoriteServlet',
                type: 'POST',
                data: { productId: productId },
                success: function(response) {
                    if (response === 'added') {
                        $icon.addClass('favorited');
                    } else if (response === 'removed') {
                        $icon.removeClass('favorited');
                    } else {
                        window.location.href = '${pageContext.request.contextPath}/LoginControll';
                    }
                },
                error: function() {
                    window.location.href = '${pageContext.request.contextPath}/LoginControll';
                }
            });
        });
    });



</script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    function addToFavorites(productId) {
        $.post('./FavoriteProduct', { id: productId }, function(data) {
            if (data.favoriteCount !== undefined) {
                $('#love-count').text(data.favoriteCount); // Update the count
            } else {
                alert('Failed to add to favorites. Please try again.');
            }
        }, 'json').fail(function() {
            alert('Error occurred while adding to favorites.');
        });
    }
</script>

<script src="../../assets/js/review.js"></script>
</body>
</html>