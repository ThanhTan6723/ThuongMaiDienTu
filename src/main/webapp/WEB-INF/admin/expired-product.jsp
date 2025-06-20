<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Admin-Dashboard</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.10.24/css/jquery.dataTables.min.css">
    <style>
        .page-btn {
            padding: 10px 23px;
            border-radius: 5px;
            border: none;
            background-color: whitesmoke;
            color: black;
            font-weight: 700;
        }
        .page-btn.active {
            background-color: #7fad39;
            color: white;
        }
        table.dataTable thead th {
            background-color: #343a40;
            color: white;
        }
        table.dataTable tbody td {
            color: black;
        }
        tr:hover {
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.5);
            transition: box-shadow 0.3s ease;
        }
    </style>
    <jsp:include page="./link/link.jsp"></jsp:include>

</head>
<body >
<div class="container-scroller">
    <jsp:include page="./header/sidebar.jsp"></jsp:include>
    <div class="container-fluid page-body-wrapper">
        <jsp:include page="./header/navbar.jsp"></jsp:include>
        <div class="col-lg-12 grid-margin stretch-card">
            <div class="card">
                <div class="card-body" style="background-color: whitesmoke">
                    <h4 class="card-title">Danh Sách Sản Phẩm Hết Hạn</h4>
                    <br><br>
                    <div class="table-responsive">
                        <table id="expiredProductTable" class="display" style="width:100%;">
                            <thead>
                            <tr style="text-align: center">
                                <th>Mã</th>
                                <th>Tên sản phẩm</th>
                                <th>Hình ảnh</th>
                                <th>Giá tiền</th>
                                <th>Mã lô</th>
                                <th>Tên lô</th>
                                <th>Ngày nhập</th>
                                <th>Số lượng</th>
                                <th>Xóa lô</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="./footer/footer.jsp"></jsp:include>
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.min.js"></script>
<script>
    $(document).ready(function() {
        var table = $('#expiredProductTable').DataTable({
            ajax: {
                url: 'ExpiredProduct',
                dataSrc: ''
            },
            columns: [
                { data: 'id' },
                { data: 'name' },
                {
                    data: 'image',
                    render: function(data, type, row) {
                        return '<img src="' + data + '" alt="' + row.name + '" style="width: 80px;height: 70px;border-radius: 5%">';
                    }
                },
                { data: 'price' },
                { data: 'batches[0].id' },
                { data: 'batches[0].name' },
                { data: 'batches[0].dateOfImporting' },
                { data: 'batches[0].currentQuantity' },
                {
                    data: null,
                    render: function(data, type, row) {
                        return `<i class="fa-solid fa-trash delete-icon" data-id="${row.batches[0].id}" style="color: red; font-size: 20px; cursor: pointer; margin-right: 15px;"></i>`;
                    }
                }
            ]
        });

        $('#expiredProductTable').on('click', '.delete-icon', function() {
            var id = $(this).data('id');
            $.ajax({
                url: 'DeleteExpiredProduct',
                type: 'get',
                data: { id: id },
                success: function(response) {
                    table.ajax.reload(); // Tải lại dữ liệu bảng
                },
                error: function(xhr, status, error) {
                    console.error(xhr.responseText); // In lỗi ra console để kiểm tra
                }
            });
        });
    });
</script>

</body>
</html>
