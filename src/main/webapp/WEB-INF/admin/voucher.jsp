<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh Sách Voucher</title>
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

        .modal {
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

        .modal-contents {
            background-color: #191c24;
            margin: 80px auto; /* Cách phía trên 100px và tự động căn giữa ngang */
            padding: 20px;
            border: 1px solid #f5f5f5;
            color: #ffffff;
            width: 42%; /* Độ dài 50% */
        }

        /* Close button */
        .close-x {
            color: #ababab;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }

        .close-x:hover,
        .close-x:focus {
            color: #707070;
            text-decoration: none;
            cursor: pointer;
        }

        body.modal-open {
            overflow: hidden;
        }

        #create-voucher-content h4 {
            text-align: center;
            margin-bottom: 10px;
        }

        #voucherForm {
            display: flex;
            flex-direction: column;
        }

        #voucherForm label {
            font-size: 14px;
            /*margin-bottom: 0px;*/
            /*margin-right: 20px;*/
        }

        #voucherForm button {
            margin-bottom: 15px;
            /*padding: 10px;*/
            margin-right: 20px;
            width: 100px;
            font-size: 16px;
            border: 1px solid #ccc;
        }

        #voucherForm input,
        #voucherForm select {
            margin-bottom: 15px;
            background-color: #e8e8e8;
            margin-right: 20px;
            width: auto;
            font-size: 14px;
            border: 1px solid #ccc;
        }

        #voucherForm button {
            /*margin-right:;*/
            background-color: #00d25b;
            color: white;
            border: none;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        #voucherForm button:hover {
            background-color: #00d25b;
        }

        .dropdown-input {
            position: relative;
            font-size: 14px;
        }

        .dropdown-input input[type="number"] {
            width: calc(100% - 22px);
            display: inline-block;
        }

        .dropdown-input select {
            position: absolute;
            right: 0;
            top: 0;
            bottom: 0;
            border: none;
            /*padding: 10px;*/
            /*border-radius: 5px;*/
            cursor: pointer;
        }

        .button {
            margin-top: 20px;
            margin-bottom: 5px;
            display: flex;
            justify-content: flex-end;
        }

        .error {
            color: #ff0f0f;
            font-size: 14px;
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
                <div class="tab-bar">
                    <button class="add-voucher-btn" onclick="openModal()">Tạo Voucher</button>
                </div>
                <br>
                <!-- Tabs -->
                <div class="tabs">
                    <div class="tab active" data-tab="all">All</div>
                    <div class="tab" data-tab="product">Product</div>
                    <div class="tab" data-tab="category">Category</div>
                </div>

                <!-- Table Sections -->
                <div class="table-section active" id="all">
                    <div class="row">
                        <div class="col-12 grid-margin">
                            <div class="card">
                                <div class="card-body" style="background-color: white">
                                    <div class="table-responsive">
                                        <h4 style="color: black">Danh Sách Voucher</h4>
                                        <table id="example1" class="display" style="width:100%;">
                                            <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Code</th>
                                                <th>Percentage</th>
                                                <th>Quantity</th>
                                                <th>Start Date</th>
                                                <th>End Date</th>
                                                <th>Đơn tối thiểu</th>
                                                <th>Giảm tối đa</th>
                                                <th>Action</th>
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

                <div class="table-section" id="product">
                    <div class="row">
                        <div class="col-12 grid-margin">
                            <div class="card">
                                <div class="card-body" style="background-color: white">
                                    <div class="table-responsive">
                                        <h4 style="color: black">Danh Sách Voucher</h4>
                                        <table id="example2" class="display" style="width:100%;">
                                            <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Code</th>
                                                <th>Image</th>
                                                <th>Product Name</th>
                                                <th>Percentage</th>
                                                <th>Quantity</th>
                                                <th>Start Date</th>
                                                <th>End Date</th>
                                                <th>Đơn tối thiểu</th>
                                                <th>Giảm tối đa</th>
                                                <th>Action</th>
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

                <div class="table-section" id="category">
                    <div class="row">
                        <div class="col-12 grid-margin">
                            <div class="card">
                                <div class="card-body" style="background-color: white">
                                    <div class="table-responsive">
                                        <h4 style="color: black">Danh Sách Voucher</h4>
                                        <table id="example3" class="display" style="width:100%;">
                                            <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Code</th>
                                                <th>Category Name</th>
                                                <th>Percentage</th>
                                                <th>Quantity</th>
                                                <th>Start Date</th>
                                                <th>End Date</th>
                                                <th>Đơn tối thiểu</th>
                                                <th>Giảm tối đa</th>
                                                <th>Action</th>
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

            </div>
        </div>
    </div>
</div>
<div id="myModal" class="modal">
    <div class="modal-contents">
        <div class="exit"><span class="close-x">&times;</span></div>
        <div id="create-voucher-content">
            <h4>Tạo Voucher</h4>
            <form id="voucherForm">
                <div class="form-row">
                    <div class="form-group">
                        <button type="button" style="width:auto" id="generateCodeButton">Nhập Code Tay</button>
                    </div>
                    <div class="code">
                        <div id="manualCodeDiv" style="display:none;">
                            <label for="manualCode">Nhập Code:</label>
                            <input type="text" id="manualCode" name="manualCode">
                            <span class="error" id="manualCodeError"></span>
                        </div>
                    </div>
                </div>
                <div class="form-row">
                    <div class="type">
                        <label for="voucherType">Loại Voucher:</label>
                        <select id="voucherType" name="voucherType">
                            <option value="">Chọn 1 loại</option>
                            <option value="all">All</option>
                            <option value="product">Product</option>
                            <option value="category">Category</option>
                        </select>
                        <span class="error" id="voucherTypeError"></span>
                    </div>
                    <div class="type">
                        <div id="productCategoryDiv" style="display:none;">
                            <label for="productCategory">Chọn Sản Phẩm/Loại Sản Phẩm:</label>
                            <select id="productCategory" name="productCategory">
                                <!-- Options sẽ được thêm vào từ JavaScript -->
                            </select>
                            <span class="error" id="productCategoryError"></span>
                        </div>
                    </div>
                </div>
                <div class="percent">
                    <div class="dropdown-input">
                        <label for="discount">Giảm Giá (%):</label>
                        <input type="number" id="discount" name="discount" min="0" max="100">
                        <span class="error" id="discountError"></span>
                    </div>
                </div>
                <div class="quantity">
                    <div class="dropdown-input">
                        <label for="quantity">Số Lượng:</label>
                        <input type="number" id="quantity" name="quantity" min="1">
                        <span class="error" id="quantityError"></span>
                    </div>
                </div>
                <div class="form-row">
                    <div class="date">
                        <label for="startDate">Ngày Bắt Đầu:</label>
                        <input type="date" id="startDate" name="startDate">
                        <span class="error" id="startDateError"></span>
                    </div>
                    <div class="date">
                        <label for="endDate">Ngày Kết Thúc:</label>
                        <input type="date" id="endDate" name="endDate">
                        <span class="error" id="endDateError"></span>
                    </div>
                </div>
                <div class="minimum-value">
                    <div class="dropdown-input">
                        <label for="minimum">Đơn tối thiểu (đ):</label>
                        <input type="number" id="minimum" name="minimum" min="0" step="1000" placeholder="0">
                        <span class="error" id="minimumError"></span>
                    </div>
                </div>
                <div class="maximum-discount">
                    <div class="dropdown-input">
                        <label for="maximum">Giảm tối đa (đ):</label>
                        <input type="number" id="maximum" name="maximum" min="1000" step="1000" placeholder="0">
                        <span class="error" id="maximumError"></span>
                    </div>
                </div>
                <div class="button">
                    <button type="button" style="background-color: #8c8c8c; color: white" id="cancelButton">Hủy</button>
                    <button type="button" id="saveVoucherButton">Lưu</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- jQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<!-- DataTables JS -->
<script src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.min.js"></script>

<script>
    // Lấy modal
    var modal = document.getElementById('myModal');

    // Lấy nút mở modal
    var btn = document.querySelector('.add-voucher-btn');

    // Lấy nút đóng modal
    var span = document.getElementsByClassName('close-x')[0];

    // Hàm mở modal
    function openModal() {
        modal.style.display = 'block';
    }

    // Hàm đóng modal
    function closeModal() {
        modal.style.display = 'none';
        $('#voucherForm')[0].reset();
        $('#manualCodeDiv').hide();
        $('#productCategoryDiv').hide();
        $('.error').text('');
        $('#voucherType').html(`
                <option value="">Chọn 1 loại</option>
                <option value="all">All</option>
                <option value="product">Product</option>
                <option value="category">Category</option>
            `);
    }

    // Đóng modal khi nhấn nút đóng (x)
    span.onclick = function () {
        closeModal();
    };

    // Đóng modal khi nhấn ngoài modal
    window.onclick = function (event) {
        if (event.target == modal) {
            closeModal();
        }
    };

</script>
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

            if (tab === 'all') {
                table1 = $('#example1').DataTable({
                    ajax: {
                        url: 'ManageVoucherControll?type=all',
                        dataSrc: ''
                    },
                    columns: [
                        {data: 'id'},
                        {data: 'code'},
                        {data: 'discountPercentage'},
                        {data: 'quantity'},
                        {data: 'startDate'},
                        {data: 'endDate'},
                        {data: 'minimumOrderValue'},
                        {data: 'maximumDiscount'},
                        {
                            data: null, render: function (data, type, row) {
                                return `
                                <div class="buttons">
                                    <button class="btn btn-danger refuse-btn" data-id="">Delete</button>
                                </div>
                            `;
                            }
                        },
                    ]
                });
            } else if (tab === 'product') {
                table2 = $('#example2').DataTable({
                    ajax: {
                        url: 'ManageVoucherControll?type=product',
                        dataSrc: ''
                    },
                    columns: [
                        {data: 'id'},
                        {data: 'code'},
                        {
                            data: 'product.image',
                            render: function (data, type, row) {
                                return `<img src="${data}" alt="image" style="width:30px;height:30px;">`;
                            }
                        },
                        {data: 'product.name'},
                        {data: 'discountPercentage'},
                        {data: 'quantity'},
                        {data: 'startDate'},
                        {data: 'endDate'},
                        {data: 'minimumOrderValue'},
                        {data: 'maximumDiscount'},
                        {
                            data: null, render: function (data, type, row) {
                                return `
                                <div class="buttons">
                                    <button class="btn btn-danger refuse-btn" data-id="">Delete</button>
                                </div>
                            `;
                            }
                        },
                    ]
                });
            } else if (tab === 'category') {
                table3 = $('#example3').DataTable({
                    ajax: {
                        url: 'ManageVoucherControll?type=category',
                        dataSrc: ''
                    },
                    columns: [
                        {data: 'id'},
                        {data: 'code'},
                        {data: 'category.name'},
                        {data: 'discountPercentage'},
                        {data: 'quantity'},
                        {data: 'startDate'},
                        {data: 'endDate'},
                        {data: 'minimumOrderValue'},
                        {data: 'maximumDiscount'},
                        {
                            data: null, render: function (data, type, row) {
                                return `
                                <div class="buttons">
                                    <button class="btn btn-danger refuse-btn" data-id="">Delete</button>
                                </div>
                            `;
                            }
                        },
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

        // Set the initial tab and initialize the DataTable
        var initialTab = 'all';
        $('.tab[data-tab="' + initialTab + '"]').addClass('active');
        $('#' + initialTab).addClass('active');
        initDataTable(initialTab);

    });

</script>
<script>
    $(document).ready(function () {
        $('#generateCodeButton').click(function () {
            $('#manualCodeDiv').toggle();
        });

        $('#voucherType').change(function () {
            $('#voucherTypeError').text(''); // Xóa thông báo lỗi khi người dùng thay đổi loại voucher
            var selectedType = $(this).val();
            if (selectedType == 'product' || selectedType == 'category') {
                $('#productCategoryDiv').show();
                // Gọi Ajax để lấy danh sách sản phẩm hoặc loại sản phẩm
                $.ajax({
                    url: 'ProductCategoryControll', // URL của servlet lấy danh sách
                    method: 'GET',
                    data: { type: selectedType },
                    success: function (data) {
                        $('#productCategory').empty();

                        if (selectedType == 'product') {
                            $.each(data.products, function (index, item) {
                                $('#productCategory').append($('<option>', {
                                    value: item.id,
                                    text: item.name
                                }));
                            });
                        } else if (selectedType == 'category') {
                            $.each(data.categories, function (index, item) {
                                $('#productCategory').append($('<option>', {
                                    value: item.id,
                                    text: item.name
                                }));
                            });
                        }
                    },
                    error: function (xhr, status, error) {
                        console.error('Error fetching data:', error);
                    }
                });
            } else {
                $('#productCategoryDiv').hide();
            }
        });

        // Xóa thông báo lỗi khi người dùng nhập vào trường manualCode
        $('#manualCode').on('input', function () {
            $('#manualCodeError').text('');
        });

        // Xóa thông báo lỗi khi người dùng thay đổi giá trị của discount
        $('#discount').on('input', function () {
            $('#discountError').text('');
        });

        // Xóa thông báo lỗi khi người dùng thay đổi giá trị của quantity
        $('#quantity').on('input', function () {
            $('#quantityError').text('');
        });

        // Xóa thông báo lỗi khi người dùng chọn ngày bắt đầu
        $('#startDate').change(function () {
            $('#startDateError').text('');
        });

        // Xóa thông báo lỗi khi người dùng chọn ngày kết thúc
        $('#endDate').change(function () {
            $('#endDateError').text('');
        });

        $('#saveVoucherButton').click(function () {
            // Clear previous errors
            $('.error').text('');

            var valid = true;

            var manualCodeDivVisible = $('#manualCodeDiv').is(':visible');
            var manualCode = $('#manualCode').val();
            var selectedType = $('#voucherType').val();
            var productCategory = $('#productCategory').val();
            var discount = $('#discount').val();
            var quantity = $('#quantity').val();
            var startDate = $('#startDate').val();
            var endDate = $('#endDate').val();
            var currentDate = new Date().toISOString().split('T')[0]; // Get current date in YYYY-MM-DD format
            var minimumValue = $('#minimum').val();
            var maximumDiscount = $('#maximum').val();

            // Validation
            if (manualCodeDivVisible && !manualCode) {
                $('#manualCodeError').text('Vui lòng nhập code.');
                valid = false;
            }
            if (!selectedType) {
                $('#voucherTypeError').text('Vui lòng chọn loại voucher.');
                valid = false;
            }
            if ((selectedType === 'product' || selectedType === 'category') && !productCategory) {
                $('#productCategoryError').text('Vui lòng chọn sản phẩm hoặc loại sản phẩm.');
                valid = false;
            }
            if (!discount || discount <= 0 || discount > 100) {
                $('#discountError').text('Vui lòng nhập giảm giá hợp lệ (1-100%).');
                valid = false;
            }
            if (!quantity || quantity <= 0) {
                $('#quantityError').text('Vui lòng nhập số lượng hợp lệ (lớn hơn 0).');
                valid = false;
            }
            if (!startDate) {
                $('#startDateError').text('Vui lòng chọn ngày bắt đầu.');
                valid = false;
            }
            if (!endDate) {
                $('#endDateError').text('Vui lòng chọn ngày kết thúc.');
                valid = false;
            }
            if (startDate && endDate) {
                if (startDate < currentDate) {
                    $('#startDateError').text('Ngày bắt đầu phải lớn hơn hoặc bằng ngày hiện tại.');
                    valid = false;
                }
                if (endDate <= startDate ) {
                    $('#endDateError').text('Ngày kết thúc phải lớn hơn ngày bắt đầu.');
                    valid = false;
                }
            }

            if (!minimumValue || minimumValue <= 0) {
                $('#minimumError').text('Vui lòng nhập số tiền hợp lệ (lớn hơn 0).');
                valid = false;
            }
            if (!maximumDiscount || maximumDiscount <= 0) {
                $('#maximumError').text('Vui lòng nhập số tiền hợp lệ (lớn hơn 0).');
                valid = false;
            }

            if (valid) {
                var formData = $('#voucherForm').serialize();
                $.ajax({
                    url: 'CreateVoucherControll', // URL của servlet để lưu voucher
                    method: 'POST',
                    data: formData,
                    success: function (response) {
                        alert('Voucher được lưu thành công');
                        closeModal();
                        // Reinitialize DataTable based on the current active tab
                        if (selectedType === 'All') {
                            table1.clear().rows.add(response).draw(); // Update example1 DataTable
                        } else if (selectedType === 'Product') {
                            table2.clear().rows.add(response).draw(); // Update example2 DataTable
                        } else if (selectedType === 'Category') {
                            table3.clear().rows.add(response).draw(); // Update example3 DataTable
                        }

                    },
                    error: function () {
                        alert('Có lỗi xảy ra khi lưu voucher');
                    }
                });
            }
        });

        $('#cancelButton').click(function () {
            closeModal();
        });
    });
</script>
</body>
</html>
