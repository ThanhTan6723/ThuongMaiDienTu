<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh Sách Đơn Hàng</title>
    <!-- DataTables CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.10.24/css/jquery.dataTables.min.css">
    <!-- FontAwesome CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        /* Custom styles for DataTables */
        table.dataTable thead th {
            background-color: #343a40; /* Header background color */
            color: #ffffff; /* Header text color */
        }

        table.dataTable tbody td {
            color: black; /* Data text color */
        }

        tr:hover {
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); /* Shadow effect on hover */
            transition: box-shadow 0.3s ease; /* Smooth transition */
        }

        /* Style for account details section */
        .account-details {
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

        .account-details-content{
            background-color: #888888;
            margin: 100px auto;
            padding: 20px;
            border: 1px solid #888;
            width: 300px;
        }
        /* Style for the close button */
        .close-btn {
            position: absolute;
            top: 10px;
            right: 10px;
            cursor: pointer;
        }


        .table-container {
            position: relative;
            margin-top: 150px; /* Adjust as needed */
        }

        /* Flex container for buttons */
        .action-buttons {
            display: flex;
            gap: 10px; /* Space between buttons */
        }

        /* Style for the tabs */
        .tabs {
            display: flex;
            border-bottom: 1px solid #ddd;
            margin-bottom: 20px;
        }

        .tab {
            padding: 10px 20px;
            cursor: pointer;
            border-bottom: none;
            background-color: #111111;
            font-size: 15px;
        }

        .tab.active {
            background-color: #232323;
            border-bottom: 2px solid #37e737;
            color: #37e737;
        }

        .table-section {
            display: none; /* Hide all sections by default */
        }

        .table-section.active {
            display: block; /* Show the active section */
        }

        .orderModal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgb(239, 239, 239);
            background-color: rgba(0, 0, 0, 0.4);
        }

        .modalDetail-content {
            background-color: #191c24;
            margin: 100px auto; /* Cách phía trên 100px và tự động căn giữa ngang */
            padding: 20px;
            border: 1px solid #888;
            width: 60%; /* Độ dài 50% */
        }
        .close-x {
            color: #dadada;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }

        .close-x:hover,
        .close-x:focus {
            color: #f5f5f5;
            text-decoration: none;
            cursor: pointer;
        }


        body.orderModal-open {
            overflow: hidden;
        }

    </style>
    <!-- Include common links -->
    <jsp:include page="./link/link.jsp"></jsp:include>
</head>
<body>
<div class="container-scroller">
    <!-- Include sidebar -->
    <jsp:include page="./header/sidebar.jsp"></jsp:include>
    <div class="container-fluid page-body-wrapper">
        <!-- Include navbar -->
        <jsp:include page="./header/navbar.jsp"></jsp:include>
        <div class="main-panel">
            <div class="content-wrapper">
                <!-- Tabs -->
                <div class="tabs">
                    <div class="tab active" data-tab="all">All</div>
                    <div class="tab" data-tab="acceptw">Chờ xác nhận</div>
                    <div class="tab" data-tab="packing">Chờ đóng gói</div>
                    <div class="tab" data-tab="shipping">Chờ vận chuyển</div>
                    <div class="tab" data-tab="delivering">Vận chuyển</div>
                    <div class="tab" data-tab="completed">Hoàn thành</div>
                    <div class="tab" data-tab="rqcancelled">Yêu cầu hủy</div>
                    <div class="tab" data-tab="cancelled">Đã hủy</div>
                    <div class="tab" data-tab="refused">Bị từ chối </div>
                    <div class="tab" data-tab="changed">Bị chỉnh sửa</div>

                <%--                    <div class="tab" data-tab="return-refund">Trả hàng/Hoàn tiền</div>--%>
                </div>

                <!-- Table Sections -->
                <div class="table-section active" id="all">
                    <div class="row">
                        <div class="col-12 grid-margin">
                            <div class="card">
                                <div class="card-body" style="background-color: white">
                                    <div class="table-responsive">
                                        <h4 style="color: black">Danh Sách Đơn Hàng Chờ Xác Nhận</h4>
                                        <table id="example0" class="display" style="width:100%;">
                                            <thead>
                                            <tr>
                                                <th>Mã đơn hàng</th>
                                                <th style="width: 110px">Ngày đặt</th>
                                                <th>Account thực hiện</th>
                                                <th>Tên người nhận</th>
                                                <th>SĐT người nhận</th>
                                                <th>Địa chỉ giao hàng</th>
                                                <th>Tổng tiền hàng</th>
                                                <th>Ship</th>
                                                <th>Giảm giá</th>
                                                <th>Tổng tiền</th>
                                                <th>Ghi chú đơn hàng</th>
                                                <th>Verify</th>
<%--                                                <th>Hành động</th>--%>
                                                <th>Xem chi tiết</th>
                                            </tr>
                                            </thead>
                                            <tbody id="order-table-body">
                                            <!-- Data will be populated by DataTables -->
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="table-section" id="acceptw">
                    <div class="row">
                        <div class="col-12 grid-margin">
                            <div class="card">
                                <div class="card-body" style="background-color: white">
                                    <div class="table-responsive">
                                        <h4 style="color: black">Danh Sách Đơn Hàng Chờ Xác Nhận</h4>
                                        <table id="example1" class="display" style="width:100%;">
                                            <thead>
                                            <tr>
                                                <th>Mã đơn hàng</th>
                                                <th style="width: 110px">Ngày đặt</th>
                                                <th>Account thực hiện</th>
                                                <th>Tên người nhận</th>
                                                <th>SĐT người nhận</th>
                                                <th>Địa chỉ giao hàng</th>
                                                <th>Tổng tiền hàng</th>
                                                <th>Ship</th>
                                                <th>Giảm giá</th>
                                                <th>Tổng tiền</th>
                                                <th>Ghi chú đơn hàng</th>
                                                <th>Verify</th>
                                                <th>Hành động</th>
                                                <th>Xem chi tiết</th>
                                            </tr>
                                            </thead>
                                            <tbody id="order-table-body">
                                            <!-- Data will be populated by DataTables -->
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="table-section" id="packing">
                    <div class="row">
                        <div class="col-12 grid-margin">
                            <div class="card">
                                <div class="card-body" style="background-color: white">
                                    <div class="table-responsive">
                                        <h4 style="color: black">Danh Sách Đơn Chờ Đóng Gói</h4>
                                        <table id="example2" class="display" style="width:100%;">
                                            <thead>
                                            <tr>
                                                <th>Mã đơn hàng</th>
                                                <th style="width: 110px">Ngày đặt</th>
                                                <th>Account thực hiện</th>
                                                <th>Tên người nhận</th>
                                                <th>SĐT người nhận</th>
                                                <th>Địa chỉ giao hàng</th>
                                                <th>Tổng tiền hàng</th>
                                                <th>Ship</th>
                                                <th>Giảm giá</th>
                                                <th>Tổng tiền</th>
                                                <th>Ghi chú đơn hàng</th>
                                                <th>Hành động</th>
                                                <th>Xem chi tiết</th>
                                            </tr>
                                            </thead>
                                            <tbody id="order-table-body">
                                            <!-- Data will be populated by DataTables -->
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="table-section" id="shipping">
                    <div class="row">
                        <div class="col-12 grid-margin">
                            <div class="card">
                                <div class="card-body" style="background-color: white">
                                    <div class="table-responsive">
                                        <h4 style="color: black">Danh Sách Đơn Hàng Bị Từ Chối</h4>
                                        <table id="example3" class="display" style="width:100%;">
                                            <thead>
                                            <tr>
                                                <th>Mã đơn hàng</th>
                                                <th style="width: 110px">Ngày đặt</th>
                                                <th>Account thực hiện</th>
                                                <th>Tên người nhận</th>
                                                <th>SĐT người nhận</th>
                                                <th>Địa chỉ giao hàng</th>
                                                <th>Tổng tiền hàng</th>
                                                <th>Ship</th>
                                                <th>Giảm giá</th>
                                                <th>Tổng tiền</th>
                                                <th>Ghi chú đơn hàng</th>
                                                <th>Xem chi tiết</th>
                                                <th>Hành động</th>
                                            </tr>
                                            </thead>
                                            <tbody id="order-table-body">
                                            <!-- Data will be populated by DataTables -->
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="table-section" id="delivering">
                    <div class="row">
                        <div class="col-12 grid-margin">
                            <div class="card">
                                <div class="card-body" style="background-color: white">
                                    <div class="table-responsive">
                                        <h4 style="color: black">Danh Sách Đơn Hàng Bị Từ Chối</h4>
                                        <table id="example4" class="display" style="width:100%;">
                                            <thead>
                                            <tr>
                                                <th>Mã đơn hàng</th>
                                                <th style="width: 110px">Ngày đặt</th>
                                                <th>Account thực hiện</th>
                                                <th>Tên người nhận</th>
                                                <th>SĐT người nhận</th>
                                                <th>Địa chỉ giao hàng</th>
                                                <th>Tổng tiền hàng</th>
                                                <th>Ship</th>
                                                <th>Giảm giá</th>
                                                <th>Tổng tiền</th>
                                                <th>Ghi chú đơn hàng</th>
                                                <th>Xem chi tiết</th>
                                            </tr>
                                            </thead>
                                            <tbody id="order-table-body">
                                            <!-- Data will be populated by DataTables -->
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="table-section" id="completed">
                    <div class="row">
                        <div class="col-12 grid-margin">
                            <div class="card">
                                <div class="card-body" style="background-color: white">
                                    <div class="table-responsive">
                                        <h4 style="color: black">Danh Sách Đơn Hàng Bị Từ Chối</h4>
                                        <table id="example5" class="display" style="width:100%;">
                                            <thead>
                                            <tr>
                                                <th>Mã đơn hàng</th>
                                                <th style="width: 110px">Ngày đặt</th>
                                                <th style="width: 110px">Ngày giao</th>
                                                <th>Account thực hiện</th>
                                                <th>Tên người nhận</th>
                                                <th>SĐT người nhận</th>
                                                <th>Địa chỉ giao hàng</th>
                                                <th>Tổng tiền hàng</th>
                                                <th>Ship</th>
                                                <th>Giảm giá</th>
                                                <th>Tổng tiền</th>
                                                <th>Ghi chú đơn hàng</th>
                                                <th>Xem chi tiết</th>
                                            </tr>
                                            </thead>
                                            <tbody id="order-table-body">
                                            <!-- Data will be populated by DataTables -->
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="table-section" id="rqcancelled">
                    <div class="row">
                        <div class="col-12 grid-margin">
                            <div class="card">
                                <div class="card-body" style="background-color: white">
                                    <div class="table-responsive">
                                        <h4 style="color: black">Danh Sách Đơn Hàng Bị Từ Chối</h4>
                                        <table id="example6" class="display" style="width:100%;">
                                            <thead>
                                            <tr>
                                                <th>Mã đơn hàng</th>
                                                <th style="width: 110px">Ngày đặt</th>
                                                <th>Account thực hiện</th>
                                                <th>Tên người nhận</th>
                                                <th>SĐT người nhận</th>
                                                <th>Địa chỉ giao hàng</th>
                                                <th>Tổng tiền hàng</th>
                                                <th>Ship</th>
                                                <th>Giảm giá</th>
                                                <th>Tổng tiền</th>
                                                <th>Ghi chú đơn hàng</th>
                                                <th>Xem chi tiết</th>
                                                <th>Hành động</th>
                                            </tr>
                                            </thead>
                                            <tbody id="order-table-body">
                                            <!-- Data will be populated by DataTables -->
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="table-section" id="cancelled">
                    <div class="row">
                        <div class="col-12 grid-margin">
                            <div class="card">
                                <div class="card-body" style="background-color: white">
                                    <div class="table-responsive">
                                        <h4 style="color: black">Danh Sách Đơn Hàng Bị Từ Chối</h4>
                                        <table id="example7" class="display" style="width:100%;">
                                            <thead>
                                            <tr>
                                                <th>Mã đơn hàng</th>
                                                <th style="width: 110px">Ngày đặt</th>
                                                <th>Account thực hiện</th>
                                                <th>Tên người nhận</th>
                                                <th>SĐT người nhận</th>
                                                <th>Địa chỉ giao hàng</th>
                                                <th>Tổng tiền hàng</th>
                                                <th>Ship</th>
                                                <th>Giảm giá</th>
                                                <th>Tổng tiền</th>
                                                <th>Ghi chú đơn hàng</th>
                                                <th>Xem chi tiết</th>
                                            </tr>
                                            </thead>
                                            <tbody id="order-table-body">
                                            <!-- Data will be populated by DataTables -->
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="table-section" id="refused">
                    <div class="row">
                        <div class="col-12 grid-margin">
                            <div class="card">
                                <div class="card-body" style="background-color: white">
                                    <div class="table-responsive">
                                        <h4 style="color: black">Danh Sách Đơn Hàng Bị Từ Chối</h4>
                                        <table id="example8" class="display" style="width:100%;">
                                            <thead>
                                            <tr>
                                                <th>Mã đơn hàng</th>
                                                <th style="width: 110px">Ngày đặt</th>
                                                <th>Account thực hiện</th>
                                                <th>Tên người nhận</th>
                                                <th>SĐT người nhận</th>
                                                <th>Địa chỉ giao hàng</th>
                                                <th>Tổng tiền hàng</th>
                                                <th>Ship</th>
                                                <th>Giảm giá</th>
                                                <th>Tổng tiền</th>
                                                <th>Ghi chú đơn hàng</th>
                                                <th>Xem chi tiết</th>
                                                <th>Hành động</th>
                                            </tr>
                                            </thead>
                                            <tbody id="order-table-body">
                                            <!-- Data will be populated by DataTables -->
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="table-section" id="changed">
                    <div class="row">
                        <div class="col-12 grid-margin">
                            <div class="card">
                                <div class="card-body" style="background-color: white">
                                    <div class="table-responsive">
                                        <h4 style="color: black">Danh Sách Đơn Bị Chỉnh Sửa</h4>
                                        <table id="example9" class="display" style="width:100%;">
                                            <thead>
                                            <tr>
                                                <th>Mã đơn hàng</th>
                                                <th style="width: 110px">Ngày đặt</th>
                                                <th>Account thực hiện</th>
                                                <th>Tên người nhận</th>
                                                <th>SĐT người nhận</th>
                                                <th>Địa chỉ giao hàng</th>
                                                <th>Tổng tiền hàng</th>
                                                <th>Ship</th>
                                                <th>Giảm giá</th>
                                                <th>Tổng tiền</th>
                                                <th>Ghi chú đơn hàng</th>
                                                <th>Hành động</th>
                                                <th>Xem chi tiết</th>
                                            </tr>
                                            </thead>
                                            <tbody id="order-table-body">
                                            <!-- Data will be populated by DataTables -->
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Add more table sections for other tabs here -->
            </div>
        </div>
    </div>
</div>
<!-- Modal Structure -->
<div id="orderModal" class="orderModal">
    <div class="modalDetail-content">
        <div class="exit"><span class="close-x">&times;</span></div>
        <div id="order-details-content">
            <!-- Order details will be populated here -->
        </div>
    </div>
</div>
<!-- Account Details Section -->
<div class="account-details" id="account-details">
    <div class="exit"><span class="close-x">&times;</span></div>
    <div class="account-details-content"></div>
</div>
<!-- jQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<!-- DataTables JS -->

<script src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.min.js"></script>
<script>
    $(document).ready(function () {
        // Initialize DataTables
        var table0, table1, table2, table3, table4, table5, table6, table7, table8, table9;

        function initDataTable(tab) {
            if ($.fn.DataTable.isDataTable('#example0')) {
                $('#example0').DataTable().destroy();
            }
            if ($.fn.DataTable.isDataTable('#example1')) {
                $('#example1').DataTable().destroy();
            }
            if ($.fn.DataTable.isDataTable('#example2')) {
                $('#example2').DataTable().destroy();
            }
            if ($.fn.DataTable.isDataTable('#example3')) {
                $('#example3').DataTable().destroy();
            }
            if ($.fn.DataTable.isDataTable('#example4')) {
                $('#example4').DataTable().destroy();
            }
            if ($.fn.DataTable.isDataTable('#example5')) {
                $('#example5').DataTable().destroy();
            }
            if ($.fn.DataTable.isDataTable('#example6')) {
                $('#example6').DataTable().destroy();
            }
            if ($.fn.DataTable.isDataTable('#example7')) {
                $('#example7').DataTable().destroy();
            }
            if ($.fn.DataTable.isDataTable('#example8')) {
                $('#example8').DataTable().destroy();
            }
            if ($.fn.DataTable.isDataTable('#example9')) {
                $('#example9').DataTable().destroy();
            }


            if (tab === 'all') {
                table0 = $('#example0').DataTable({
                    ajax: {
                        url: 'BillControll',
                        dataSrc: '',
                        error: function(xhr, error, thrown) {
                            console.error('Lỗi Ajax:', error, thrown);
                            alert('Lỗi tải dữ liệu. Vui lòng kiểm tra console để biết chi tiết.');
                        }
                    },
                    columns: [
                        { data: 'id' },
                        { data: 'bookingDate' },
                        {
                            data: 'account.name',
                            render: function(data, type, row) {
                                return '<a href="#" class="account-detail" data-account-id="' + row.account.id + '">' + data + '</a>';
                            }
                        },
                        { data: 'consigneeName' },
                        { data: 'consigneePhone' },
                        { data: 'address' },
                        { data: 'totalMoney' },
                        { data: 'ship' },
                        { data: 'discountValue' },
                        { data: 'totalMoney' },
                        { data: 'orderNotes' },
                        {
                            data: 'orderStatus',
                            render: function(orderStatus) {
                                if (orderStatus === "Đơn hàng bị chỉnh sửa") {
                                    return '<span style="color: orange;">Đơn hàng bị chỉnh sửa</span>';
                                } else if (orderStatus === "Đã xác thực") {
                                    return '<span style="color: green;">Đã xác thực</span>';
                                } else if (orderStatus === "Chưa xác thực") {
                                    return '<span style="color: red;">Chưa xác thực</span>';
                                } else if (orderStatus === "Lỗi xử lý") {
                                    return '<span style="color: gray;">Lỗi xác thực</span>';
                                }
                                return orderStatus;
                            }
                        },
                        {
                            data: null,
                            render: function(data, type, row) {
                                return `
                    <div class="view-order-details" data-order-id="${row.id}">
                        <i class="fa-solid fa-eye" style="color:black; font-size: 20px; cursor: pointer;"></i>
                    </div>
                `;
                            }
                        }
                    ]
                });
            }if (tab === 'acceptw') {
                table1 = $('#example1').DataTable({
                    ajax: {
                        url: 'WaitingAcceptControll',
                        dataSrc: ''
                    },
                    columns: [
                        {data: 'id'},
                        {data: 'bookingDate'},
                        {
                            data: 'account.name',
                            render: function (data, type, row) {
                                // Tạo một link có class để xác định khi nhấp vào tên account
                                return '<a href="#" class="account-detail" data-account-id="' + row.account.id + '">' + data + '</a>';
                            }
                        },
                        {data: 'consigneeName'},
                        {data: 'consigneePhone'},
                        {data: 'address'},
                        {data: 'totalMoney'},
                        {data: 'ship'},
                        {data: 'discountValue'},
                        {data: 'totalMoney'},
                        {data: 'orderNotes'},
                        {
                            data: 'orderStatus',  // Truyền vào orderStatus từ response JSON
                            render: function (orderStatus, type, row) {
                                // Kiểm tra giá trị của orderStatus để hiển thị đúng thông báo
                                if (orderStatus === "Đơn hàng bị chỉnh sửa") {
                                    return '<span style="color: orange;">Đơn hàng bị chỉnh sửa</span>';
                                } else if (orderStatus === "Đã xác thực") {
                                    return '<span style="color: green;">Đã xác thực</span>';
                                } else if (orderStatus === "Chưa xác thực") {
                                    return '<span style="color: red;">Chưa xác thực</span>';
                                } else if (orderStatus === "Lỗi xử lý") {
                                    return '<span style="color: gray;">Lỗi xác thực</span>';
                                }
                            }
                        }
                        ,
                        {
                            data: null,
                            render: function (data, type, row) {
                                // Kiểm tra giá trị của orderStatus
                                if (row.orderStatus === "Đơn hàng bị chỉnh sửa") {
                                    return `
                <div class="buttons">
                    <button class="btn btn-success accept-btn" data-id="${row.id}">Remove</button>
                </div>
            `;
                                }
                                else if (row.orderStatus === "Đã xác thực") {
                                    return `
                <div class="buttons">
                     <button class="btn btn-success accept-btn" data-id="${row.id}" >Accept</button>
                    <button class="btn btn-danger refuse-btn" data-id="${row.id}" >Refuse</button>
                                    </div>
            `;
                                }else {
                                    return `
                <div class="buttons">
                    <button class="btn btn-success accept-btn" data-id="${row.id}" disabled >Accept</button>
                    <button class="btn btn-danger refuse-btn" data-id="${row.id}" disabled>Refuse</button>
                </div>
            `;
                                }
                            }
                        }
                        ,
                        {
                            data: null, render: function (data, type, row) {
                                return `
                                 <div class="view-order-details" data-order-id="${row.id}">
                                    <i class="fa-solid fa-eye" style="color:black; font-size: 20px; cursor: pointer;"></i>
                                </div>
                            `;
                            }
                        },
                    ]
                });
            } else if (tab === 'packing') {
                table2 = $('#example2').DataTable({
                    ajax: {
                        url: 'WaitPackingBillControll',
                        dataSrc: ''
                    },
                    columns: [
                        {data: 'id'},
                        {data: 'bookingDate'},
                        {
                            data: 'account.name',
                            render: function (data, type, row) {
                                // Tạo một link có class để xác định khi nhấp vào tên account
                                return '<a href="#" class="account-detail" data-account-id="' + row.account.id + '">' + data + '</a>';
                            }
                        },
                        {data: 'consigneeName'},
                        {data: 'consigneePhone'},
                        {data: 'address'},
                        {data: 'totalMoney'},
                        {data: 'ship'},
                        {data: 'discountValue'},
                        {data: 'totalMoney'},
                        {data: 'orderNotes'},
                        {
                            data: null, render: function (data, type, row) {
                                return `
                                 <div class="buttons">
                                   <button class="btn btn-success packing-btn" data-id="${row.id}">Packing</button>
                                </div>
                                `;
                            }
                        },
                        {
                            data: null, render: function (data, type, row) {
                                return `
                                 <div class="view-order-details" data-order-id="${row.id}">
                                    <i class="fa-solid fa-eye" style="color:black; font-size: 20px; cursor: pointer;"></i>
                                </div>
                            `;
                            }
                        },
                    ]
                });
            }else if (tab === 'shipping') {
                table3 = $('#example3').DataTable({
                    ajax: {
                        url: 'WaitingShippingControll',
                        dataSrc: ''
                    },
                    columns: [
                        {data: 'id'},
                        {data: 'bookingDate'},
                        {
                            data: 'account.name',
                            render: function (data, type, row) {
                                // Tạo một link có class để xác định khi nhấp vào tên account
                                return '<a href="#" class="account-detail" data-account-id="' + row.account.id + '">' + data + '</a>';
                            }
                        },
                        {data: 'consigneeName'},
                        {data: 'consigneePhone'},
                        {data: 'address'},
                        {data: 'totalMoney'},
                        {data: 'ship'},
                        {data: 'discountValue'},
                        {data: 'totalMoney'},
                        {data: 'orderNotes'},
                        {
                            data: null, render: function (data, type, row) {
                                return `
                                <div class="buttons">
                                   <button class="btn btn-success shipping-btn" data-id="${row.id}">Shipping</button>
                                </div>
                                `;
                            }
                        },
                        {
                            data: null, render: function (data, type, row) {
                                return `
                                 <div class="view-order-details" data-order-id="${row.id}">
                                    <i class="fa-solid fa-eye" style="color:black; font-size: 20px; cursor: pointer;"></i>
                                </div>
                            `;
                            }
                        },
                    ]
                });
            }

            else if (tab === 'delivering') {
                table4 = $('#example4').DataTable({
                    ajax: {
                        url: 'DeliveryBillControll',
                        dataSrc: ''
                    },
                    columns: [
                        {data: 'id'},
                        {data: 'bookingDate'},
                        {
                            data: 'account.name',
                            render: function (data, type, row) {
                                // Tạo một link có class để xác định khi nhấp vào tên account
                                return '<a href="#" class="account-detail" data-account-id="' + row.account.id + '">' + data + '</a>';
                            }
                        },
                        {data: 'consigneeName'},
                        {data: 'consigneePhone'},
                        {data: 'address'},
                        {data: 'totalMoney'},
                        {data: 'ship'},
                        {data: 'discountValue'},
                        {data: 'totalMoney'},
                        {data: 'orderNotes'},
                        {
                            data: null, render: function (data, type, row) {
                                return `
                                 <div class="view-order-details" data-order-id="${row.id}">
                                    <i class="fa-solid fa-eye" style="color:black; font-size: 20px; cursor: pointer;"></i>
                                </div>
                            `;
                            }
                        },

                    ]
                });
            }
            else if (tab === 'completed') {
                table5 = $('#example5').DataTable({
                    ajax: {
                        url: 'DeliverySuccessfulControll',
                        dataSrc: ''
                    },
                    columns: [
                        {data: 'id'},
                        {data: 'bookingDate'},
                        {data: 'diliveryDate'},
                        {
                            data: 'account.name',
                            render: function (data, type, row) {
                                // Tạo một link có class để xác định khi nhấp vào tên account
                                return '<a href="#" class="account-detail" data-account-id="' + row.account.id + '">' + data + '</a>';
                            }
                        },
                        {data: 'consigneeName'},
                        {data: 'consigneePhone'},
                        {data: 'address'},
                        {data: 'totalMoney'},
                        {data: 'ship'},
                        {data: 'discountValue'},
                        {data: 'totalMoney'},
                        {data: 'orderNotes'},
                        {
                            data: null, render: function (data, type, row) {
                                return `
                                 <div class="view-order-details" data-order-id="${row.id}">
                                    <i class="fa-solid fa-eye" style="color:black; font-size: 20px; cursor: pointer;"></i>
                                </div>
                            `;
                            }
                        },

                    ]
                });
            }else if (tab === 'rqcancelled') {
                table6 = $('#example6').DataTable({
                    ajax: {
                        url: 'LoadRequestCancelBill',
                        dataSrc: ''
                    },
                    columns: [
                        {data: 'id'},
                        {data: 'bookingDate'},
                        {
                            data: 'account.name',
                            render: function (data, type, row) {
                                // Tạo một link có class để xác định khi nhấp vào tên account
                                return '<a href="#" class="account-detail" data-account-id="' + row.account.id + '">' + data + '</a>';
                            }
                        },
                        {data: 'consigneeName'},
                        {data: 'consigneePhone'},
                        {data: 'address'},
                        {data: 'totalMoney'},
                        {data: 'ship'},
                        {data: 'discountValue'},
                        {data: 'totalMoney'},
                        {data: 'orderNotes'},
                        {
                            data: null, render: function (data, type, row) {
                                return `
                                 <div class="view-order-details" data-order-id="${row.id}">
                                    <i class="fa-solid fa-eye" style="color:black; font-size: 20px; cursor: pointer;"></i>
                                </div>
                            `;
                            }
                        },
                        {
                            data: null, render: function (data, type, row) {
                                return `
                                <div class="buttons">
                                    <button class="btn btn-success approve-cancel-btn" data-id="${row.id}">Accept</button>
                                </div>
                            `;
                            }
                        }

                    ]
                });
            }else if (tab === 'cancelled') {
                table7 = $('#example7').DataTable({
                    ajax: {
                        url: 'LoadCancelBill',
                        dataSrc: ''
                    },
                    columns: [
                        {data: 'id'},
                        {data: 'bookingDate'},
                        {
                            data: 'account.name',
                            render: function (data, type, row) {
                                // Tạo một link có class để xác định khi nhấp vào tên account
                                return '<a href="#" class="account-detail" data-account-id="' + row.account.id + '">' + data + '</a>';
                            }
                        },
                        {data: 'consigneeName'},
                        {data: 'consigneePhone'},
                        {data: 'address'},
                        {data: 'totalMoney'},
                        {data: 'ship'},
                        {data: 'discountValue'},
                        {data: 'totalMoney'},
                        {data: 'orderNotes'},
                        {
                            data: null, render: function (data, type, row) {
                                return `
                                 <div class="view-order-details" data-order-id="${row.id}">
                                    <i class="fa-solid fa-eye" style="color:black; font-size: 20px; cursor: pointer;"></i>
                                </div>
                            `;
                            }
                        },

                    ]
                });
            }

            else if (tab === 'refused') {
                table8 = $('#example8').DataTable({
                    ajax: {
                        url: 'ListRefusedControll',
                        dataSrc: ''
                    },
                    columns: [
                        {data: 'id'},
                        {data: 'bookingDate'},
                        {
                            data: 'account.name',
                            render: function (data, type, row) {
                                // Tạo một link có class để xác định khi nhấp vào tên account
                                return '<a href="#" class="account-detail" data-account-id="' + row.account.id + '">' + data + '</a>';
                            }
                        },
                        {data: 'consigneeName'},
                        {data: 'consigneePhone'},
                        {data: 'address'},
                        {data: 'totalMoney'},
                        {data: 'ship'},
                        {data: 'discountValue'},
                        {data: 'totalMoney'},
                        {data: 'orderNotes'},
                        {
                            data: null, render: function (data, type, row) {
                                return `
                                 <div class="view-order-details" data-order-id="${row.id}">
                                    <i class="fa-solid fa-eye" style="color:black; font-size: 20px; cursor: pointer;"></i>
                                </div>
                            `;
                            }
                        },
                        {
                            data: null, render: function (data, type, row) {
                                return `
                                <a href="?id=${row.id}">
                                   <button class="btn btn-danger refuse-btn">Delete</button>
                                </a>
                                `;
                            }
                        },
                    ]
                });
            }
            // Khai báo mảng lưu trữ ID các hàng đã bị xóa
            let deletedRows = []; // Mảng lưu trữ các ID đã xóa

// Tải mảng từ localStorage nếu có
            const storedDeletedRows = localStorage.getItem('deletedRows');
            if (storedDeletedRows) {
                deletedRows = JSON.parse(storedDeletedRows);
            }

// Hàm khởi tạo bảng DataTable
            function initializeTable() {
                $('#example9').DataTable({
                    ajax: {
                        url: 'ChangedBillControll',
                        dataSrc: function (json) {
                            // Lọc bỏ các hàng có ID nằm trong danh sách deletedRows
                            return json.filter(item => !deletedRows.includes(item.id));
                        }
                    },
                    columns: [
                        { data: 'id' },
                        { data: 'bookingDate' },
                        {
                            data: 'account.name',
                            render: function (data, type, row) {
                                return '<a href="#" class="account-detail" data-account-id="' + row.account.id + '">' + data + '</a>';
                            }
                        },
                        { data: 'consigneeName' },
                        { data: 'consigneePhone' },
                        { data: 'address' },
                        { data: 'totalMoney' },
                        { data: 'ship' },
                        { data: 'discountValue' },
                        { data: 'totalMoney' },
                        { data: 'orderNotes' },
                        {
                            data: null, render: function (data, type, row) {
                                return `
                        <div class="view-order-details" data-order-id="${row.id}">
                            <i class="fa-solid fa-eye" style="color:black; font-size: 20px; cursor: pointer;"></i>
                        </div>
                    `;
                            }
                        },
                        {
                            data: null, render: function (data, type, row) {
                                return `
                        <div class="buttons">
                            <button class="btn btn-success accept-btn" data-id="${row.id}">Remove</button>
                        </div>
                    `;
                            }
                        },
                    ]
                });
            }

// Gọi hàm khởi tạo bảng khi tab được chọn
            if (tab === 'changed') {
                initializeTable();
            }

// Xử lý sự kiện khi nhấn nút "Remove"
            $('#example9').on('click', '.accept-btn', function () {
                const row = $(this).closest('tr'); // Lấy hàng cha gần nhất
                const rowId = $('#example9').DataTable().row(row).data().id; // Lấy ID của hàng

                // Thêm ID vào mảng deletedRows nếu chưa tồn tại
                if (!deletedRows.includes(rowId)) {
                    deletedRows.push(rowId);
                }

                // Lưu mảng deletedRows vào localStorage
                localStorage.setItem('deletedRows', JSON.stringify(deletedRows));

                // Xóa hàng khỏi DataTable và vẽ lại bảng
                $('#example9').DataTable().row(row).remove().draw();

            });
            function loadAccountDetails(accountId) {
                $.ajax({
                    url: 'AccountDetailControll',
                    type: 'GET',
                    data: {id: accountId},
                    success: function (data) {
                        var details = `
                    <h4>Thông Tin Tài Khoản</h4>
                    <h5>Name: <span>${data.name}</span></h5>
                    <h5>Email: <span>${data.email}</span></h5>
                    <h5>Phone: <span>${data.telephone}</span></h5>
                `;
                        $('.account-details-content').html(details);
                        $('#account-details').show();
                    }
                });
            }

            window.closeAccountDetails = function () {
                $('.account-details').hide();
                $('.account-details-overlay').hide();
            }

            $(document).on('click', '.account-detail', function (e) {
                e.preventDefault();
                var accountId = $(this).data('account-id');
                loadAccountDetails(accountId);
            });

            // Handle accept button click
            $('#example1 tbody').on('click', '.accept-btn', function () {
                var orderId = $(this).data('id');
                $.ajax({
                    url: 'AcceptControll',
                    type: 'POST',
                    data: {id: orderId},
                    success: function (response) {
                        // Remove the row from the table
                        table1.row($(this).parents('tr')).remove().draw();
                    }.bind(this),
                    error: function (xhr, status, error) {
                        console.error('Error accepting order:', error);
                    }
                });
            });

            // Handle refuse button click
            $('#example1 tbody').on('click', '.refuse-btn', function () {
                var orderId = $(this).data('id');
                $.ajax({
                    url: 'RefuseBillControll',
                    type: 'POST',
                    data: {id: orderId},
                    success: function (response) {
                        // Remove the row from the table
                        table1.row($(this).parents('tr')).remove().draw();
                    }.bind(this),
                    error: function (xhr, status, error) {
                        console.error('Error refusing order:', error);
                    }
                });
            });

            $('#example2 tbody').on('click', '.packing-btn', function () {
                var orderId = $(this).data('id');
                $.ajax({
                    url: 'PackingBillControll',
                    type: 'POST',
                    data: {id: orderId},
                    success: function (response) {
                        // Remove the row from the table
                        table2.row($(this).parents('tr')).remove().draw();
                    }.bind(this),
                    error: function (xhr, status, error) {
                        console.error('Error refusing order:', error);
                    }
                });
            });

            $('#example3 tbody').on('click', '.shipping-btn', function () {
                var orderId = $(this).data('id');
                $.ajax({
                    url: 'ShippingControll',
                    type: 'POST',
                    data: {id: orderId},
                    success: function (response) {
                        // Remove the row from the table
                        table3.row($(this).parents('tr')).remove().draw();
                    }.bind(this),
                    error: function (xhr, status, error) {
                        console.error('Error refusing order:', error);
                    }
                });
            });

            $('#example6 tbody').on('click', '.approve-cancel-btn', function () {
                var orderId = $(this).data('id');
                $.ajax({
                    url: 'ApproveCancelBill',
                    type: 'POST',
                    data: {id: orderId},
                    success: function (response) {
                        // Remove the row from the table
                        table6.row($(this).parents('tr')).remove().draw();
                    }.bind(this),
                    error: function (xhr, status, error) {
                        console.error('Error refusing order:', error);
                    }
                });
            });
        }
        $('#example6 tbody').on('click', '.packing-btn', function () {
            var orderId = $(this).data('id');
            $.ajax({
                url: 'PackingBillControll',
                type: 'POST',
                data: {id: orderId},
                success: function (response) {
                    // Remove the row from the table
                    table2.row($(this).parents('tr')).remove().draw();
                }.bind(this),
                error: function (xhr, status, error) {
                    console.error('Error refusing order:', error);
                }
            });
        });
        // Initialize the first DataTable on page load
        // initDataTable('all');

        // Tab navigation logic
        $('.tab').on('click', function () {
            $('.tab').removeClass('active');
            $(this).addClass('active');
            var tab = $(this).data('tab');
            $('.table-section').removeClass('active');
            $('#' + tab).addClass('active');
            initDataTable(tab);
        });

        // Set the initial tab and initialize the DataTable
        var initialTab = 'all';
        $('.tab[data-tab="' + initialTab + '"]').addClass('active');
        $('#' + initialTab).addClass('active');
        initDataTable(initialTab);

        // Modal
        const modal = document.getElementById("orderModal");
        const span = document.getElementsByClassName("close-x")[0];
        const orderDetailsContent = document.getElementById("order-details-content");

        // Xử lý sự kiện click cho icon xem chi tiết đơn hàng
        $(document).on('click', '.view-order-details', function(event) {
            event.preventDefault();
            const orderId = $(this).data('order-id');
            fetchOrderDetails(orderId);
        });

        // Đóng modal
        span.onclick = function() {
            modal.style.display = "none";
            document.body.classList.remove('orderModal-open');

        };

        window.onclick = function(event) {
            if (event.target == modal) {
                modal.style.display = "none";
                document.body.classList.remove('modal-open');
            }
        };

        function fetchOrderDetails(orderId) {
            $.ajax({
                url: `/BillDetailControll?id=${orderId}`,
                type: 'GET',
                success: function(response) {
                    populateModal(response);
                    modal.style.display = "block";
                },
                error: function(xhr, status, error) {
                    console.error('Error fetching order details:', error);
                }
            });
        }

        function populateModal(response) {
            const { listBillDetails, sumQ } = response;
            const order = listBillDetails[0].order;

            let content = `
        <h4 class="card-title">Chi tiết đơn hàng</h4>
        <p>Mã đơn hàng: <span style="color: springgreen">${order.id}</span></p>
        <p>Tổng số lượng: ${sumQ}</p>
        <p>Tổng tiền: ${order.totalMoney}</p>
        <p>Trạng thái: <span style="color: #00d25b;">${order.orderStatus}</span></p>
        <table class="table table-hover custom-table">
            <thead>
            <tr>
                <th>STT</th>
                <th>Tên sản phẩm</th>
                <th>Hình ảnh</th>
                <th>Giá</th>
                <th>Số lượng</th>
                <th>Thành tiền</th>
            </tr>
            </thead>
            <tbody>
        `;

            listBillDetails.forEach((b, index) => {
                content += `
            <tr>
                <td>${index + 1}</td>
                <td>${b.product.name}</td>
                <td><img style="with=50px; height=50px" src="${b.product.image}" alt="${b.product.name}"></td>
                <td>${b.product.price}</td>
                <td>${b.quantity}</td>
                <td>${b.price}</td>
            </tr>
            `;
            });

            content += `
            </tbody>
        </table>
        `;
            orderDetailsContent.innerHTML = content;
        }
    });
</script>
</body>
</html>