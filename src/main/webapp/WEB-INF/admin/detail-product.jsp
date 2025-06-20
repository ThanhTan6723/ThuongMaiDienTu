<%--suppress ALL --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ page isELIgnored="false" %>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Edit Product</title>
    <!-- plugins:css -->
    <jsp:include page="./link/link.jsp"></jsp:include>
      <style>
          body{
              color: black;
          }
          /* Thay đổi font và màu chữ */
          body {
              font-family: Arial, sans-serif;
              color: #333; /* Màu chữ đen */
          }
          label{
              font-weight: bold;
          }


      </style>
</head>
<body>
<div class="container-scroller">
    <jsp:include page="./header/sidebar.jsp"></jsp:include>
    <div class="container-fluid page-body-wrapper">
        <jsp:include page="./header/navbar.jsp"></jsp:include>
        <div class="main-panel">

            <div class="content-wrapper">
                <div class="container-fluid">

                    <div class="row mt-3">
                        <div class="col-lg-12">
                            <div class="card" style="background-color: white">
                                <div class="card-body">
                                    <div class="card-title" style="color:#000;">Thông tin sản phẩm</div>

                                    <hr>


                                        <div class="form-group">
                                            <label >1.Mã sản phẩm</label>
                                            <p>${product.id}</p>

                                        </div>

                                        <div class="form-group">
                                            <label >2.Tên sản phẩm</label>
                                            <p>${product.name}</p>

                                        </div>

                                        <div class="form-group">
                                            <label >3.Giá</label>
                                            <p>${product.price}</p>
                                        </div>
                                    <div class="form-group">
                                        <label >4.Cân nặng</label>
                                        <p>${product.weight}</p>
                                    </div>
                                        <div class="form-group">
                                            <label>5.Hình chính</label>
                                           <p><img src="${product.image}" alt="${product.name}" style="width: 130px;height: 130px" ></p>
                                        </div>
                                    <div class="form-group">
                                        <label>6.Danh sách hình phụ</label><br>
                                        <c:forEach var="image" items="${listImage}">
                                            <img src="${image.url}"  style="width: 130px;height: 130px">
                                        </c:forEach>
                                    </div>

                                        <div class="form-group">
                                            <label class="col-form-label">7.Mô tả</label>
                                            <p>${product.description}</p>

                                        </div>

                                        <div class="form-group">
                                            <label >8.Phân loại</label>
                                         <p>    Mã loại sản phẩm:${category.id}</p>
                                           <p> Tên loại sản phẩm: ${category.name}</p>

                                        </div>

                                    <div class="form-group">
                                        <label >9.Thông tin các lô hàng</label>
                                        <ul>
                                            <c:forEach var="o" items="${listBatch}" varStatus="loop">
                                                <li><b>Lô hàng ${loop.index + 1}</b>
                                                    <ul>
                                                        <li>Mã lô: ${o.id}</li>
                                                        <li>Tên lô: ${o.name}${loop.index + 1}</li>
                                                        <li>Ngày sản xuất: ${o.manufacturingDate}</li>
                                                        <li>Hạn sử dụng: ${o.expiryDate}</li>
                                                        <li>Ngày nhập hàng: ${o.dateOfImporting}</li>
                                                        <li>Số lượng: ${o.quantity}</li>
                                                        <li>Nhà cung cấp:
                                                            <ul>
                                                                <li>Mã nhà cung cấp: ${o.provider.id}</li>
                                                                <li>Tên nhà cung cấp: ${o.provider.name}</li>
                                                                <li>Địa chỉ nhà cung cấp: ${o.provider.address}</li>
                                                            </ul>
                                                        </li>
                                                        <li>Giá nhập: ${o.priceImport}</li>
                                                    </ul>
                                                </li>
                                            </c:forEach>
                                        </ul>


                                    </div>


                                        <div class="form-footer">
                                            <a class="btn btn-danger" id="exitButton">Thoát</a>


                                        </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="overlay toggle-menu"></div>
            </div>
        </div>
    </div>
    <!-- main-panel ends -->
</div>
<!-- page-body-wrapper ends -->
</div>
<jsp:include page="./footer/footer.jsp"></jsp:include>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<!-- Thêm liên kết tới DataTables JS (nếu cần) -->
<script src="https://cdn.datatables.net/1.10.25/js/jquery.dataTables.min.js"></script>

<script>
    $(document).ready(function() {
        // Lấy URL của trang trước đó
        var previousUrl = document.referrer;

        // Kiểm tra nếu có URL của trang trước đó
        if (previousUrl) {
            $('#exitButton').attr('href', previousUrl);
        } else {
            // Nếu không có trang trước đó, điều hướng đến một trang mặc định
            $('#exitButton').attr('href', './LoadProductsPage');
        }
    });
</script>
</body>
</html>