<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html; charset=UTF-8" %><!DOCTYPE html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <%@ page isELIgnored="false" %>
    <meta charset="utf-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Admin-Dashboard</title>
    <!-- plugins:css -->
    <jsp:include page="link/link.jsp"></jsp:include>
    <style>
        .btn-upload {
            background-color: #d4d5d7; /* Màu nền */
            border: none; /* Bỏ viền */
            padding: 5px 10px; /* Khoảng cách bên trong */
            cursor: pointer; /* Con trỏ khi rê chuột */
            border-radius: 4px; /* Bo góc */
            transition: background-color 0.3s ease; /* Hiệu ứng chuyển đổi màu nền */
            margin-left: 10px;
        }

        .btn-upload:hover {
            background-color: #bbb5b5; /* Màu nền khi hover */
        }

    </style>
</head>
<body>
<div class="container-scroller">
    <!-- partial:partials/_sidebar.html -->
    <jsp:include page="header/sidebar.jsp"></jsp:include>

    <!-- partial -->
    <div class="container-fluid page-body-wrapper">
        <!-- partial:partials/_navbar.html -->
        <jsp:include page="header/navbar.jsp"></jsp:include>

        <!-- partial -->
        <div class="main-panel">
            <div class="content-wrapper">
                <div class="col-lg-12 grid-margin stretch-card">
                    <div class="card">
                        <div class="card-body">
                            <h4 class="card-title">Bordered table</h4>
                            <form action="UploadExcel" method="post" enctype="multipart/form-data">
                                <input type="file" name="excelFile" accept=".xlsx, .xls" class="input-file">
                                <button type="submit" class="btn-upload" ><img style="width: 35px; height: 35px;color: #a11515" src="./images/upload-file.png" alt=""></button>

                                <button type="button" class="btn-upload" onclick="resetFileInput()">
                                    <img style="width: 35px; height: 35px;color: #a11515" src="./images/remove.png" alt="">
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- content-wrapper ends -->
            <!-- partial:partials/_footer.html -->
            <jsp:include page="./footer/footer.jsp"></jsp:include>

            <!-- partial -->
        </div>
        <!-- main-panel ends -->
    </div>
    <!-- page-body-wrapper ends -->
</div>

<script src="assetsAdmin/vendors/js/vendor.bundle.base.js"></script>
<!-- endinject -->
<!-- Plugin js for this page -->
<script src="assetsAdmin/vendors/chart.js/Chart.min.js"></script>
<script src="assetsAdmin/vendors/progressbar.js/progressbar.min.js"></script>
<script src="assetsAdmin/vendors/jvectormap/jquery-jvectormap.min.js"></script>
<script src="assetsAdmin/vendors/jvectormap/jquery-jvectormap-world-mill-en.js"></script>
<script src="assetsAdmin/vendors/owl-carousel-2/owl.carousel.min.js"></script>
<!-- End plugin js for this page -->
<!-- inject:js -->
<script src="assetsAdmin/js/off-canvas.js"></script>
<script src="assetsAdmin/js/hoverable-collapse.js"></script>
<script src="assetsAdmin/js/misc.js"></script>
<script src="assetsAdmin/js/settings.js"></script>
<script src="assetsAdmin/js/todolist.js"></script>
<!-- endinject -->
<!-- Custom js for this page -->
<script src="assetsAdmin/js/dashboard.js"></script>

<script>
    function resetFileInput() {
        const inputFile = document.querySelector('.input-file');
        inputFile.value = ''; // Đặt lại giá trị của input file
    }
</script>

</body>
</html>
