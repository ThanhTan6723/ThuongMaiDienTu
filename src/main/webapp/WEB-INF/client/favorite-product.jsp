<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="zxx">

<head>
    <%@ page isELIgnored="false" %>
    <meta charset="UTF-8">
    <meta name="description" content="Ogani Template">
    <meta name="keywords" content="Ogani, unica, creative, html">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Golden Fields</title>

    <!-- Google Font -->
    <link href="https://fonts.googleapis.com/css2?family=Cairo:wght@200;300;400;600;900&display=swap" rel="stylesheet">
    <jsp:include page="link/link.jsp"></jsp:include>
    <style>
        .product__pagination .pagination .page-item .page-link {
            display: inline-block;
            width: 40px;
            height: 40px;
            line-height: 40px;
            text-align: center;
            margin: 0;
            padding: 0;
        }

        .page-item.active .page-link {
            background-color: #7fad39;
            color: white; /* Đổi màu chữ nếu cần */
        }

        /* Modal background */
        .modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            z-index: 1000; /* Sit on top */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
            padding-top: 40px;
        }

        /* Modal content */
        .modal-content {
            background-color: #fefefe;
            margin: 5% auto; /* 15% from the top and centered */
            padding: 20px;
            border: 1px solid #888;
            width: 80%; /* Could be more or less, depending on screen size */
            max-width: 500px;
            border-radius: 15px;
            position: relative;
        }

        /* Close button */
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

        /* Form styles */
        .data form {
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        #modalProductName {
            font-size: 24px;
            margin-bottom: 20px;
        }

        #modalProductImage {
            margin-bottom: 20px;
        }

        label {
            font-size: 18px;
            margin-bottom: 10px;
        }

        #quantity {
            width: 50px;
            padding: 5px;
            font-size: 16px;
            margin-bottom: 20px;
        }

        button[type="button"] {
            background-color: #8fbe41;
            color: white;
            padding: 15px 20px;
            border: none;
            cursor: pointer;
            font-size: 18px;
            border-radius: 30px;
        }
        button[type="button"]:hover {
            background-color: #94c94d;
        }



        .featured__item__pic img {
            width: 100%;
            border-radius: 10px 10px 0 0;
        }

        .featured__item__text {
            margin: -30px 10px 30px 10px; /* Khoảng cách giữa văn bản và khung */
            text-align: center; /* Căn giữa văn bản */
        }

        .buy-now-btn {
            padding: 10px 23px;
            border-radius: 30px;
            border: none;
            background-color: #7fad39;
            font-weight: 700;
            color: #ffffff;
            margin-top: 5px; /* Khoảng cách trên giữa nút và văn bản */
        }

        .product-col {
            flex: 0 0 calc(20% - 10px);
            margin-right: 10px;
            box-sizing: border-box;
        }

        .product-frame {
            border: 2px solid #d4d5d7;
            padding: 20px 10px 0px 10px;
            box-sizing: border-box;
            border-radius: 15px;
            box-shadow: 0 4px 8px rgba(0.1, 0, 0, 0.3);
            height: 270px;

        }


        .product-col:nth-child(5n) {
            margin-right: 0;
        }

    </style>


</head>

<body>
<c:url var="detail" value="DetailControl"></c:url>

<span class="header__fixed">
	<jsp:include page="header/header.jsp"></jsp:include>
</span>


<!-- Product Section Begin -->
<section class="product spad" style="padding-left: 300px">
    <div class="col-lg-9 col-md-7">
        <div class="edit-button-container">
<%--            <div class="img" style="text-align: center;margin-top: -100px;">
                <img alt="" src="./images/love.png" style="vertical-align: middle; width: 150px;height: 150px;">
                <h3><b>Sản phẩm yêu thích rỗng</b></h3>
                <p>Hiện tại bạn chưa có sản phẩm nào trong mục sản phẩm yêu thích. Hãy dạo quanh cửa hàng để chọn được sản
                    phẩm ưng ý nhé!</p>
            </div>--%>

            <div class="row" id="content">
                <c:forEach items="${listProduct}" var="o">
                    <div class="product-col">
                        <div class="featured__item">
                            <div class="product-frame">
                                <div class="text-right">
                                    <button class="delete-btn btn btn-danger"
                                            data-product-id="${o.id}"
                                            style="border: none; background: transparent; color: #ff0000; font-size: 20px; margin-top: -50px;margin-right: -9px">
                                        &times; <!-- or use an <i> or <svg> icon here -->
                                    </button>
                                </div>
                                <div class="featured__item__pic set-bg">
                                    <a href="${detail}?pid=${o.id}">
                                        <img src="${o.image}" alt="${o.name}">
                                    </a>
                                </div>
                                <div class="featured__item__text">
                                    <a class="product-name" href="${detail}?pid=${o.id}" style="color: black">
                                            ${o.name}
                                    </a>
                                    <h5>${o.price}</h5>
                                </div>
                            </div>
                            <div class="text-center">
                                <c:url var="addToCart" value="/AddToCartControl"></c:url>
                                <form action="${addToCart}?pid=${o.id}" method="post" enctype="multipart/form-data">
                                    <button class="buy-now-btn"
                                            data-product-id="${o.id}"
                                            data-product-name="${o.name}"
                                            data-product-image="${o.image}"
                                            style="padding: 10px 23px; border-radius: 30px; border: none; background-color: #7fad39; font-weight: 700; color: #ffffff">
                                        MUA NGAY
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

        </div>

        <div id="myModal" class="modal">
            <div class="modal-content">
                <div class="exit"><span class="close">&times;</span></div>
                <div class="data">
                    <form id="addToCartForm" action="/AddToCartControl" method="post">
                        <input type="hidden" id="productId" name="pid">
                        <h3 id="modalProductName"></h3>
                        <img id="modalProductImage" src="" style="width: 30vh;height: 30vh" alt="Product Image">
                        <label for="quantity">Số lượng:</label>
                        <input type="number" id="quantity" name="quantity" value="1" min="1">
                        <button type="button" id="addToCartButton" >Thêm vào giỏ hàng</button>
                    </form>
                </div>
            </div>
        </div>
        <!-- Pagination -->

    </div>
</section>

<!-- Product Section End -->
<jsp:include page="footer/footer.jsp"></jsp:include>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
    $(document).ready(function(){
        $(".delete-btn").click(function(){
            var productId = $(this).data("product-id");
            var parent = $(this).closest(".product-col");

            $.ajax({
                url: "FavoriteProduct",
                type: "POST",
                data: { id: productId },
                success: function(response) {
                    if(response === 'success') {
                        parent.remove();
                    } else {
                        alert("Error deleting product");
                    }
                },
                error: function() {
                    alert("An error occurred");
                }
            });
        });
    });
</script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        var modal = document.getElementById('myModal');
        var span = document.getElementsByClassName('close')[0];

        document.querySelectorAll('.buy-now-btn').forEach(function (button) {
            button.addEventListener('click', function (event) {
                event.preventDefault();
                var productId = button.getAttribute('data-product-id');
                var productName = button.getAttribute('data-product-name');
                var productImage = button.getAttribute('data-product-image');

                document.getElementById('productId').value = productId;
                document.getElementById('modalProductName').textContent = productName;
                document.getElementById('modalProductImage').setAttribute('src', productImage);

                modal.style.display = 'block';
            });
        });

        span.onclick = function () {
            modal.style.display = 'none';
        };

        window.onclick = function (event) {
            if (event.target == modal) {
                modal.style.display = 'none';
            }
        };
    });

    document.addEventListener('DOMContentLoaded', function() {
        // Lắng nghe sự kiện click vào nút "Thêm vào giỏ hàng"
        document.getElementById('addToCartButton').addEventListener('click', function() {
            // Lấy giá trị của productId và quantity từ form
            var productId = document.getElementById('productId').value;
            var quantity = document.getElementById('quantity').value;

            // Tạo đối tượng XMLHttpRequest để gửi request Ajax
            var xhr = new XMLHttpRequest();
            xhr.open('POST', '/AddToCartControl', true);
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

            // Xử lý khi nhận được phản hồi từ server
            xhr.onreadystatechange = function() {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status === 200) {
                        // Xử lý phản hồi từ server (nếu cần)
                        var response = JSON.parse(xhr.responseText);

                        // Ví dụ: hiển thị thông báo cho người dùng
                        // alert('Sản phẩm đã được thêm vào giỏ hàng thành công!');

                        // Cập nhật size giỏ hàng
                        document.getElementById('cart-count').innerText = response.size;

                        // Đặt lại giá trị của trường số lượng về 1
                        document.getElementById('quantity').value = 1;

                        // Đóng modal (nếu có)
                        closeModal();
                    } else {
                        // Xử lý lỗi (nếu có)
                        alert('Đã xảy ra lỗi khi thêm sản phẩm vào giỏ hàng.');
                    }
                }
            };

            // Gửi dữ liệu form lên server
            var formData = 'pid=' + encodeURIComponent(productId) + '&quantity=' + encodeURIComponent(quantity);
            xhr.send(formData);
        });

        // Hàm đóng modal
        function closeModal() {
            var modal = document.getElementById('myModal');
            modal.style.display = 'none';
        }
    });

</script>


<script>
    document.addEventListener('DOMContentLoaded', function () {
        var buttons = document.querySelectorAll('.page-btn');
        buttons.forEach(function (button) {
            button.addEventListener('click', function () {
                buttons.forEach(function (btn) {
                    btn.classList.remove('active');
                });
                this.classList.add('active');
            });
        });
    });

</script>


<!-- Js Plugins -->
<script src="assets/js/jquery-3.3.1.min.js"></script>
<script src="assets/js/bootstrap.min.js"></script>
<script src="assets/js/jquery.nice-select.min.js"></script>
<script src="assets/js/jquery-ui.min.js"></script>
<script src="assets/js/jquery.slicknav.js"></script>
<script src="assets/js/mixitup.min.js"></script>
<script src="assets/js/owl.carousel.min.js"></script>
<script src="assets/js/main.js"></script>
</body>

</html>