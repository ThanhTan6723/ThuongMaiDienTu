<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Log System</title>
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
            background-color: #191c24;
            border: 1px solid #dedede;
            color: #f8f8f8;
            padding: 50px;
            margin-top: 20px;
            position: fixed;
            /*border-radius: 8px;*/
            top: 30%; /* Center vertically */
            left: 50%; /* Center horizontally */
            transform: translate(-50%, -50%);
            z-index: 1000; /* Ensure it's on top */
            display: none; /* Initially hide the account details */
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

        /* Close button */
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
                    <div class="tab active" data-tab="auth">Auth</div>
                    <div class="tab" data-tab="lock-unlock">Lock/Unlock</div>
                    <div class="tab" data-tab="update-role">Update Role</div>
<%--                    <div class="tab" data-tab="shipping">Voucher</div>--%>
<%--                    <div class="tab" data-tab="delivering">Review</div>--%>
<%--                    <div class="tab" data-tab="completed">Product</div>--%>
<%--                    <div class="tab" data-tab="rqcancelled">Ware House</div>--%>
                </div>

                <!-- Table Sections -->
                <div class="table-section active" id="auth">
                    <div class="row">
                        <div class="col-12 grid-margin">
                            <div class="card">
                                <div class="card-body" style="background-color: white">
                                    <div class="table-responsive">
                                        <h4 style="color: black">Log for login-logout</h4>
                                        <table id="example1" class="display" style="width:100%;">
                                            <thead>
                                            <tr>
                                                <th>Log ID</th>
                                                <th style="width: 110px">Times tamp</th>
                                                <th>Log Level</th>
                                                <th>Module</th>
                                                <th>Action Type</th>
                                                <th>Account created</th>
                                                <th>Log Content</th>
                                                <th>Source IP</th>
                                                <th>User Agent</th>
                                                <th>Affected Table</th>
<%--                                                <th>Action</th>--%>
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

                <div class="table-section" id="lock-unlock">
                    <div class="row">
                        <div class="col-12 grid-margin">
                            <div class="card">
                                <div class="card-body" style="background-color: white">
                                    <div class="table-responsive">
                                        <h4 style="color: black">Log for lock/unlock</h4>
                                        <table id="example2" class="display" style="width:100%;">
                                            <thead>
                                            <tr>
                                                <th>Log ID</th>
                                                <th style="width: 110px">Times tamp</th>
                                                <th>Log Level</th>
                                                <th>Module</th>
                                                <th>Action Type</th>
                                                <th>Account created</th>
                                                <th>Log Content</th>
                                                <th>Source IP</th>
                                                <th>User Agent</th>
                                                <th>Affected Table</th>
<%--                                                <th>Action</th>--%>
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


                <div class="table-section" id="update-role">
                    <div class="row">
                        <div class="col-12 grid-margin">
                            <div class="card">
                                <div class="card-body" style="background-color: white">
                                    <div class="table-responsive">
                                        <h4 style="color: black">Log for update role</h4>
                                        <table id="example3" class="display" style="width:100%;">
                                            <thead>
                                            <tr>
                                                <th>Log ID</th>
                                                <th style="width: 110px">Times tamp</th>
                                                <th>Log Level</th>
                                                <th>Module</th>
                                                <th>Action Type</th>
                                                <th>Account created</th>
                                                <th>Log Content</th>
                                                <th>Source IP</th>
                                                <th>User Agent</th>
                                                <th>Affected Table</th>
                                                <th>Before Data</th>
                                                <th>After Data</th>
<%--                                                <th>Action</th>--%>
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
    <div class="close-btn" onclick="closeAccountDetails()">X</div>
    <h2>Thông Tin Tài Khoản</h2>
    <div class="account-details-content"></div>
</div>
<!-- jQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<!-- DataTables JS -->

<script src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.min.js"></script>
<script>
    $(document).ready(function () {
            // Initialize DataTables
            var table1, table2, table3;

            function initDataTable(tab) {
                if ($.fn.DataTable.isDataTable('#example1')) {
                    $('#example1').DataTable().destroy();
                }
                if ($.fn.DataTable.isDataTable('#example2')) {
                    $('#example2').DataTable().destroy();
                }
                if ($.fn.DataTable.isDataTable('#example3')) {
                    $('#example3').DataTable().destroy();
                }

                if (tab === 'auth') {
                    table1 = $('#example1').DataTable({
                        ajax: {
                            type: 'POST',
                            url: 'LogAuthControll',
                            dataSrc: ''
                        },
                        columns: [
                            {data: 'id'},
                            {data: 'timesTamp'},
                            {data: 'logLevel'},
                            {data: 'module'},
                            {data: 'actionType'},
                            {
                                data: 'account',
                                render: function (data, type, row) {
                                    return data ? data.name : 'null';
                                }
                            },
                            {data: 'logContent'},
                            {data: 'sourceIP'},
                            {data: 'userAgent'},
                            {data: 'affectedTable'},
                        ]
                    });
                } else if (tab === 'lock-unlock') {
                    table2 = $('#example2').DataTable({
                        ajax: {
                            type: 'POST',
                            url: 'LogAccountControll',
                            dataSrc: ''
                        },
                        columns: [
                            {data: 'id'},
                            {data: 'timesTamp'},
                            {data: 'logLevel'},
                            {data: 'module'},
                            {data: 'actionType'},
                            {
                                data: 'account',
                                render: function (data, type, row) {
                                    return data ? data.name : 'null';
                                }
                            },
                            {data: 'logContent'},
                            {data: 'sourceIP'},
                            {data: 'userAgent'},
                            {data: 'affectedTable'}
                        ]
                    });
                } else if (tab === 'update-role') {
                    table3 = $('#example3').DataTable({
                        ajax: {
                            type: 'POST',
                            url: 'LogRoleControll',
                            dataSrc: ''
                        },
                        columns: [
                            {data: 'id'},
                            {data: 'timesTamp'},
                            {data: 'logLevel'},
                            {data: 'module'},
                            {data: 'actionType'},
                            {
                                data: 'account',
                                render: function (data, type, row) {
                                    return data ? data.name : 'null';
                                }
                            },
                            {data: 'logContent'},
                            {data: 'sourceIP'},
                            {data: 'userAgent'},
                            {data: 'affectedTable'},
                            {data: 'beforeData'},
                            {data: 'afterData'}
                        ]
                    });
                }

            }

            // Tab navigation logic
            $('.tab').on('click', function () {
                $('.tab').removeClass('active');
                $(this).addClass('active');
                var tab = $(this).data('tab');
                $('.table-section').removeClass('active');
                $('#' + tab).addClass('active');
                initDataTable(tab);
            });

            // Initialize the first DataTable on page load
            initDataTable('all');

            // Logic điều hướng tab
            $('.tab').on('click', function () {
                $('.tab').removeClass('active');
                $(this).addClass('active');
                var tab = $(this).data('tab');
                $('.table-section').removeClass('active');
                $('#' + tab).addClass('active');
                initDataTable(tab);
                // Lưu trữ tab hiện tại vào localStorage
                localStorage.setItem('activeTab', tab);
            });

            // Khởi tạo DataTable dựa trên tab hiện tại lưu trữ trong localStorage
            var activeTab = localStorage.getItem('activeTab') || 'auth';
            $('.tab[data-tab="' + activeTab + '"]').addClass('active');
            $('#' + activeTab).addClass('active');
            initDataTable(activeTab);

            // Lấy tham số tab từ URL
            var urlParams = new URLSearchParams(window.location.search);
            var tab = urlParams.get('tab') || localStorage.getItem('activeTab') || 'auth';

            // Đặt tab hiện tại và khởi tạo DataTable
            $('.tab').removeClass('active');
            $('.tab[data-tab="' + tab + '"]').addClass('active');
            $('.table-section').removeClass('active');
            $('#' + tab).addClass('active');
            initDataTable(tab);

        }
    )
    ;
</script>
</body>
</html>