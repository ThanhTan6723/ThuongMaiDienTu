<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh Sách Người Dùng</title>
    <!-- DataTables CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.10.24/css/jquery.dataTables.min.css">
    <!-- FontAwesome CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        /* Custom styles for DataTables */
        table.dataTable thead th {
            background-color: #343a40; /* Header background color */
            color: white; /* Header text color */
        }

        table.dataTable tbody td {
            color: black; /* Data text color */
        }

        tr:hover {
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); /* Shadow effect on hover */
            transition: box-shadow 0.3s ease; /* Smooth transition */
        }

        .lock-toggle {
            cursor: pointer;
            font-size: 20px;
            margin-right: 10px;
        }

        .delete-icon {
            cursor: pointer;
            font-size: 20px;
            margin-right: 10px;
        }
        .status-active {
            color: #00e700;
            font-weight: bold;
        }

        .status-inactive {
            color: #858585;
            font-weight: bold;
        }
        /* Modal styles */
        .roleModal {
            display: none;
            position: fixed;
            z-index: 1000;
            padding-top: 100px;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgb(0,0,0);
            background-color: rgba(0,0,0,0.4);
        }

        .modal-contents {
            background-color: #343a40;
            margin-top: 80px;
            margin: auto;
            padding: 20px;
            border: 1px solid #888;
            color: white;
            width: 20%;
        }

        .close {
            color: #d3d3d3;
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

        .modal-footer {
            display: flex;
            justify-content: flex-end;
        }

        .change-role{
            background-color: #5f74af;
            color: #ffffff;
            padding: 3px;
            border-radius: 20px;
            border: none;
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
                <div class="row">
                    <div class="col-12 grid-margin">
                        <div class="card">
                            <div class="card-body" style="background-color: white">
                                <div class="table-responsive">
                                    <h4 class="card-title" style="color:#000;">Danh Sách Người Dùng</h4>
                                    <table id="example" class="display" style="width:100%;">
                                        <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Tên</th>
                                            <th>Email</th>
                                            <th>Số điện thoại</th>
                                            <th>Trạng thái</th>
                                            <th>Role</th>
                                            <th>Khóa/Mở khóa</th>
                                            <th>Nâng/Hạ cấp</th>
                                            <th>Hành động</th>
                                        </tr>
                                        </thead>
                                        <tbody id="user-table-body">
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
<!-- The Modal -->
<div id="roleModal" class="roleModal">
    <div class="modal-contents">
        <div class="exit"><span class="close">&times;</span></div>
        <h2>Change Role</h2>
        <form id="roleForm">
            <label for="roles">Select Role:</label>
            <select id="roles" name="roles">
                <option value="Admin">Admin</option>
                <option value="Customer">Customer</option>
                <option value="Manager Account">Manager Account</option>
                <option value="Manager Order">Manager Order</option>
                <option value="Manager Warehouse">Manager Warehouse</option>
                <option value="Manager Voucher">Manager Voucher</option>
                <option value="Manager Review">Manager Review</option>

            </select>
            <input type="hidden" id="userId" name="userId">
            <div class="modal-footer">
                <button type="button" class="cancel">Cancel</button>
                <button type="submit">Submit</button>
            </div>
        </form>
    </div>
</div>

<!-- jQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<!-- DataTables JS -->
<script src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.min.js"></script>
<script>
    $(document).ready(function () {
        var table = $('#example').DataTable({
            ajax: {
                url: 'ListUsersControll',
                dataSrc: ''
            },
            columns: [
                { data: 'id' },
                { data: 'name' },
                { data: 'email' },
                { data: 'telephone', defaultContent: '' }, // Set default content for 'telephone' column if data is null
                {
                    data: 'isLocked',
                    render: function (isLocked, type, row) {
                        var statusClass = isLocked ? 'status-inactive' : 'status-active';
                        var statusText = isLocked ? 'Inactive' : 'Active';
                        return `<span class="${statusClass}">${statusText}</span>`;
                    }
                },
                { data: 'role.roleName' },
                {
                    data: null, render: function (data, type, row) {
                        var iconClass = row.isLocked ? 'fa-lock-open' : 'fa-lock';
                        var iconColor = row.isLocked ? 'color: green;' : 'color: #505050;';
                        return `<i class="fa ${iconClass} lock-toggle" data-id="${row.id}" data-status="${row.isLocked ? 'unlock' : 'lock'}" style="${iconColor}"></i>`;
                    }
                },
                {
                    data: null, render: function (data, type, row) {
                        return `<button class="change-role" data-id="${row.id}">Change Role</button>`;
                    }
                },
                {
                    data: null, render: function (data, type, row) {
                        return `
                        <i class="fa-solid fa-trash delete-icon" data-id="${row.id}" style="color: red;"></i>
                        <a href="EditUser?id=${row.id}">
                            <i class="fa-solid fa-pen-to-square" style="color:#efe63a;"></i>
                        </a>`;
                    }
                },
            ],
            // columnDefs: [
            //     {
            //         targets: 8,
            //         visible: roleId == 1 // Set visibility of the column based on roleId
            //     }
            // ]
        });
        // Add event listener for lock/unlock action
        $('#example').on('click', '.lock-toggle', function () {
            var icon = $(this);
            var id = icon.data('id');
            var status = icon.data('status');
            var action = status === 'lock' ? 'lock' : 'unlock'; // Set the action corresponding to servlet

            if (confirm(`Bạn có chắc chắn muốn ${action === 'lock' ? 'khóa' : 'mở khóa'} tài khoản này không?`)) {
                $.ajax({
                    url: 'LockAccountControll', // Path to servlet
                    type: 'POST',
                    data: { userId: id, action: action }, // Information to send to servlet
                    dataType: 'json', // Define the data type received as JSON
                    success: function (response) {
                        if (response.success) { // Check the status returned from the servlet
                            var rowData = table.row(icon.closest('tr')).data();
                            // Update the isLocked value in the row data
                            rowData.isLocked = action === 'lock';
                            // Update the row data in the table
                            table.row(icon.closest('tr')).data(rowData).draw(false);
                        } else {
                            alert(response.message); // Show error message from servlet
                        }
                    },
                    error: function () {
                        alert('Đã xảy ra lỗi. Vui lòng thử lại.');
                    }
                });
            }
        });

        // Get the modal
        var modal = document.getElementById("roleModal");

        // Get the <span> element that closes the modal
        var span = document.getElementsByClassName("close")[0];

        // Add event listener for change role button
        $('#example').on('click', '.change-role', function () {
            var button = $(this);
            var id = button.data('id');
            $('#userId').val(id); // Set the user ID in the hidden input field
            modal.style.display = "block"; // Open the modal
        });

        // When the user clicks on <span> (x), close the modal
        span.onclick = function () {
            modal.style.display = "none";
        }

        // When the user clicks on the cancel button, close the modal
        $('.cancel').click(function () {
            modal.style.display = "none";
        });

        // When the user submits the form
        $('#roleForm').submit(function (event) {
            event.preventDefault();
            $.ajax({
                url: 'UpdateRoleControll',
                type: 'POST',
                data: $(this).serialize(),
                dataType: 'json',
                success: function (response) {
                    if (response.success) {
                        // Update role in DataTable
                        var userId = $('#userId').val();
                        var role = $('#roles').val();
                        var rowData = table.rows().data().toArray().find(row => row.id == userId);
                        if (rowData) {
                            rowData.role.roleName = role;
                            table.row($(`.change-role[data-id="${userId}"]`).closest('tr')).data(rowData).draw(false);
                        }
                        alert('Cập nhật vai trò thành công.');
                        modal.style.display = "none";
                    } else {
                        alert('Cập nhật vai trò thất bại.');
                    }
                },
                error: function () {
                    alert('Đã xảy ra lỗi. Vui lòng thử lại.');
                }
            });
        });

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function (event) {
            if (event.target == modal) {
                modal.style.display = "none";
            }
        }
    });
</script>
</body>
</html>
