<%--
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thông tin lô hàng</title>
    <jsp:include page="./link/link.jsp"></jsp:include>

</head>
<body >
<div class="container-scroller">
    <jsp:include page="./header/sidebar.jsp"></jsp:include>
    <div class="container-fluid page-body-wrapper">
        <jsp:include page="./header/navbar.jsp"></jsp:include>
        <div class="main-panel">
            <div class="content-wrapper">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="card">
                                <div class="card-body">
                                    <h4 class="card-title">Thông tin lô hàng vừa nhập</h4>
                                    <div class="table-responsive">
                                        <table class="table table-bordered">
                                            <thead>
                                            <tr>
                                                <th>STT</th>
                                                <th>Tên sản phẩm</th>
                                                <th>Giá</th>
                                                <th>Hình ảnh chính</th>
                                                <th>Mô tả</th>
                                                <th>Phân loại</th>
                                                <th>Tên lô hàng</th>
                                                <th>Ngày sản xuất</th>
                                                <th>Hạn sử dụng</th>
                                                <th>Ngày nhập hàng</th>
                                                <th>Số lượng nhập</th>
                                                <th>Giá nhập</th>
                                                <th>Nhà cung cấp</th>
                                                <th>Địa chỉ NCC</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                           &lt;%&ndash; <c:forEach var="product" items="${addedProducts}">
                                                <tr>
                                                    <td>${product.index + 1}</td>
                                                    <td>${product.name}</td>
                                                    <td>${product.price}</td>
                                                    <td><img src="${product.image}" style="max-width: 100px; max-height: 100px;"></td>
                                                    <td>${product.description}</td>
                                                    <td>${product.category}</td>
                                                    <td>${product.batchName}</td>
                                                    <td><fmt:formatDate value="${product.manufacturingDate}" pattern="dd/MM/yyyy"/></td>
                                                    <td><fmt:formatDate value="${product.expiryDate}" pattern="dd/MM/yyyy"/></td>
                                                    <td><fmt:formatDate value="${product.dateOfImporting}" pattern="dd/MM/yyyy"/></td>
                                                    <td>${product.quantity}</td>
                                                    <td>${product.priceImport}</td>
                                                    <td>${product.providerName}</td>
                                                    <td>${product.providerAddress}</td>
                                                </tr>
                                            </c:forEach>&ndash;%&gt;
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="./footer/footer.jsp"></jsp:include>
</body>
</html>
--%>
