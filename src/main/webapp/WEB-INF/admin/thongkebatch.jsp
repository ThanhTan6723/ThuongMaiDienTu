<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh Sách Người Dùng</title>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.10.24/css/jquery.dataTables.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

    <style>

        table.dataTable thead th {
            background-color: #343a40; /* Màu nền của tiêu đề cột */
            color: white; /* Màu chữ của tiêu đề cột */
        }

        table.dataTable tbody td {
            color: black; /* Màu chữ của dữ liệu */
        }

        tr:hover {
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.5); /* Đổ bóng khi rê chuột vào */
            transition: box-shadow 0.3s ease; /* Hiệu ứng chuyển đổi mượt mà */
        }

    </style>
    <jsp:include page="./link/link.jsp"></jsp:include>

</head>
<body>
<div class="container-scroller">
    <jsp:include page="./header/sidebar.jsp"></jsp:include>
    <div class="container-fluid page-body-wrapper">
        <jsp:include page="./header/navbar.jsp"></jsp:include>
        <div class="main-panel">
            <div class="content-wrapper">
                <div class="row">
                    <div class="col-12 grid-margin">
                        <div class="card">
                            <div class="card-body" style="background-color: white">
                                <div class="table-responsive">
                                    <h4 class="card-title" style="color: black">Thống kê nhập kho hôm nay</h4>
                                    <table id="example" class="display" style="width:100%;">
                                        <thead>
                                        <tr style="text-align: center">
                                            <th>Mã</th>
                                            <th>Tên</th>
                                            <th>Hình ảnh</th>
                                            <th>Giá</th>
                                            <th>Số lượng</th>
                                            <th>Ngày nhập</th>
                                            <th>Mã lô</th>
                                            <th>Tên lô</th>
                                        </tr>
                                        </thead>
                                        <tbody id="user-table-body" style="text-align: center">
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row"> <!-- Thêm hàng mới cho bảng thứ hai -->
                    <div class="col-12 grid-margin">
                        <div class="card">
                            <div class="card-body" style="background-color: white">
                                <div class="table-responsive">
                                    <h4 class="card-title" style="color: black">Thống kê nhập kho 10 ngày trước</h4>
                                    <table id="example-1" class="display" style="width:100%;">
                                        <thead>
                                        <tr style="text-align: center">
                                            <th>Mã</th>
                                            <th>Tên</th>
                                            <th>Hình ảnh</th>
                                            <th>Giá</th>
                                            <th>Số lượng</th>
                                            <th>Ngày nhập</th>
                                            <th>Mã lô</th>
                                            <th>Tên lô</th>
                                        </tr>
                                        </thead>
                                        <tbody id="user-table-body-1" style="text-align: center">
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div> <!-- Kết thúc hàng mới -->

            </div>
        </div>
    </div>
</div>
</body>


<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.min.js"></script>
<script>
    $(document).ready(function() {
        var table = $('#example').DataTable({
            ajax: {
                url: 'ThongKe',
                dataSrc: ''
            },
            columns: [
                { data: 'id' },
                { data: 'name' },
                {
                    data: 'image',
                    render: function(data, type, row) {
                        return '<img src="' + data + '" alt="' + row.name + '" style="max-width:100px; max-height:80px;">';
                    }
                },
                { data: 'price' },
                { data: 'quantity' }, // Added quantity column
                {
                    data: 'dateOfImporting',
                    render: function(data) {
                        return new Date(data).toLocaleDateString('vi-VN'); // Format date
                    }
                },
                { data: 'batch_id' }, // Added batch ID column
                { data: 'batch_name' }  // Added batch name column
            ]
        });
    });

    $(document).ready(function() {
        var table = $('#example-1').DataTable({
            ajax: {
                url: 'ThongKeBatch',
                dataSrc: ''
            },
            columns: [
                { data: 'id' },
                { data: 'name' },
                {
                    data: 'image',
                    render: function(data, type, row) {
                        return '<img src="' + data + '" alt="' + row.name + '" style="max-width:100px; max-height:80px;">';
                    }
                },
                { data: 'price' },
                { data: 'quantity' }, // Added quantity column
                {
                    data: 'dateOfImporting',
                    render: function(data) {
                        return new Date(data).toLocaleDateString('vi-VN'); // Format date
                    }
                },
                { data: 'batch_id' }, // Added batch ID column
                { data: 'batch_name' }  // Added batch name column
            ]
        });
    });
</script>
</body>
</html>