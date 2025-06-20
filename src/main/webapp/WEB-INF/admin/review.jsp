<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh Sách Review</title>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.10.24/css/jquery.dataTables.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
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

        .modalReply {
            display: none;
            position: fixed;
            z-index: 1000;
            padding-top: 100px;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgb(0, 0, 0);
            background-color: rgba(0, 0, 0, 0.4);
        }

        .modal-contents {
            background-color: #191c24;
            margin: auto;
            padding: 20px;
            border: 1px solid #888;
            width: 40%;
        }

        .close-x {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }

        .close-x:hover,
        .close-x:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
        }

        #reply-content h4 {
            text-align: center;
            margin-bottom: 20px;
        }

        #replyDropdown {
            width: auto;
            margin-bottom: 10px;
        }

        #replyButton, #composeButton {
            margin-bottom: 10px;
            margin-top: 10px;
        }

        .modalReply-footer {
            display: flex;
            justify-content: flex-end;
        }

        .infor {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
        }

        #commenter-phone, #commenter-name {
            color: #f6f6f6;
        }

        .error-message {
            color: red;
            font-size: 12px;
            display: none;
        }
        .approved {
            color: green;
        }

        .pending {
            color: gray;
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
                                    <h4 class="card-title" style="color:#000;">Danh Sách Review</h4>
                                    <table id="example" class="display" style="width:100%;">
                                        <thead>
                                        <tr style="text-align: center">
                                            <th>ID</th>
                                            <th>Tên</th>
                                            <th>Số điện thoại</th>
                                            <th>Sản phẩm review</th>
                                            <th>Sao</th>
                                            <th>Hình ảnh</th>
                                            <th>Đánh giá</th>
                                            <th>Ngày giờ đánh giá</th>
                                            <th>Trạng thái</th>
                                            <th>Reply</th>
                                            <th>Ngày giờ reply</th>
                                            <th>Hành động</th>
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
            </div>
        </div>
    </div>
</div>
<!-- Modal -->
<div id="replyModal" class="modalReply">
    <div class="modal-contents">
        <div class="exit"><span class="close-x">&times;</span></div>
        <div id="reply-content">
            <h4>Reply</h4>
            <form id="replyForm">
                <div class="infor">
                    <div class="name">Tên: <span id="commenter-name"></span></div>
                    <div class="phone">Số điện thoại: <span id="commenter-phone"></span></div>
                </div>
                <div class="cmt">
                    <div class="name">Đánh giá: <span id="comment"></span></div>
                </div>
                <div class="choose-message">
                    <label for="replyDropdown">Chọn câu trả lời:</label>
                    <select class="form-control" id="replyDropdown" style="color:#ffffff;">
                    </select>
                    <span id="dropdown-error" class="error-message">Vui lòng chọn câu trả lời.</span>
                </div>
                <div class="message-rep">
                    <label for="customReply">Nhập câu trả lời:</label>
                    <textarea class="form-control" id="customReply" style="height: 200px; color: white" rows="3"></textarea>
                    <span id="textarea-error" class="error-message">Vui lòng nhập văn bản.</span>
                </div>
            </form>
            <div class="modalReply-footer">
                <button type="button" id="replyButton" class="btn btn-success">Reply</button>
            </div>
        </div>
    </div>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.min.js"></script>
<script>
    $(document).ready(function() {
        var table = $('#example').DataTable({
            ajax: {
                url: 'ManageReviewControll',
                dataSrc: ''
            },
            columns: [
                { data: 'id' },
                { data: 'nameCommenter' },
                { data: 'phoneNumberCommenter' },
                { data: 'productEvaluated.name' },
                { data: 'rating' },
                {
                    data: 'image',
                    render: function(data, type, row) {
                        return data ? '<img src="' + (data) + '" style="max-width:100px; max-height:80px;">':'null';
                    }
                },
                { data: 'comment' },
                { data: 'dateCreated' },
                {
                    data: 'isAccept',
                    render: function (data, type, row) {
                        if (data) {
                            return '<span class="approved">Đã phê duyệt</span>';
                        } else {
                            return '<span class="pending">Chờ phê duyệt</span>';
                        }
                    }
                },
                { data: 'reply', defaultContent: '' },
                { data: 'dateReply', defaultContent: '' },
                {
                    data: null,
                    render: function(data, type, row) {
                        if (row.isAccept) {
                            return `
                            <a href="#" class="message-icon" data-id="${row.id}">
                                <i class="fa-solid fa-message" style="color:#37e737; font-size: 20px; cursor: pointer;"></i>
                            </a>
                            <a href="DeleteProductControll?id=${row.id}" class="delete-icon" data-id="${row.id}">
                                <i class="fa-solid fa-trash" style="color:#efe63a; font-size: 20px; cursor: pointer; margin-right: 15px;"></i>
                            </a>`;
                        } else {
                            return `
                            <button type="button" class="btn btn-success approve-review" data-id="${row.id}">Approve</button>
                        `;
                        }
                    }
                }
            ]
        });

        $('#example').on('click', '.approve-review', function () {
            var reviewId = $(this).data('id');

            $.ajax({
                type: 'GET',
                url: 'ApproveReviewControll',
                data: { id: reviewId },
                success: function (response) {
                        table.ajax.reload();
                        alert("Phê duyệt thành công");
                }.bind(this),
                error: function (xhr, status, error) {
                    console.error('Error refusing order:', error);
                }
            });
        });

        var modal = document.getElementById('replyModal');
        var span = document.getElementsByClassName('close-x')[0];

        function openModal() {
            modal.style.display = 'block';
        }

        function closeModal() {
            modal.style.display = 'none';
            $('#replyForm')[0].reset();
            $('.error-message').hide();
        }

        span.onclick = function() {
            closeModal();
        };

        window.onclick = function(event) {
            if (event.target == modal) {
                closeModal();
            }
        };

        var responses = [
            "Chào {name}, cảm ơn bạn đã đánh giá!",
            "Chào {name}, chúng tôi rất tiếc về trải nghiệm của bạn.",
            "Cảm ơn {name}, chúng tôi sẽ cải thiện dịch vụ.",
            "Xin lỗi {name} vì sự cố này."
        ];

        var $replyDropdown = $('#replyDropdown');
        $replyDropdown.append('<option value="">Chọn câu trả lời</option>'); // Thêm tùy chọn mặc định
        responses.forEach(function(response) {
            $replyDropdown.append('<option value="' + response + '">' + response + '</option>');
        });

        // Sự kiện khi click vào biểu tượng tin nhắn trong bảng
        $('#example').on('click', '.message-icon', function() {
            var rowData = table.row($(this).parents('tr')).data();
            var id = rowData.id;

            // Cập nhật thông tin người đánh giá trong modal
            $('#commenter-name').text(rowData.nameCommenter);
            $('#commenter-phone').text(rowData.phoneNumberCommenter);
            $('#comment').text(rowData.comment);

            // Gán id của review vào data attribute của modal
            $('#replyModal').data('reviewId', id); // Đặt reviewId vào modal
            console.log('Review ID set in modal: ' + id); // In ra để kiểm tra
            console.log($('#replyModal').data()); // In ra tất cả các thuộc tính dữ liệu của modal

            // Cập nhật dropdown với các lựa chọn có tên người đánh giá thay thế cho {name}
            $replyDropdown.empty().append('<option value="">Chọn câu trả lời</option>');
            responses.forEach(function(response) {
                var populatedResponse = response.replace('{name}', rowData.nameCommenter);
                $replyDropdown.append('<option value="' + populatedResponse + '">' + populatedResponse + '</option>');
            });

            openModal();
        });

        // Sự kiện khi thay đổi lựa chọn trong dropdown
        $('#replyDropdown').change(function() {
            var selectedResponse = $(this).val();
            if (selectedResponse) {
                $('#customReply').val(selectedResponse);  // Cập nhật nội dung văn bản trong textarea
                $('#dropdown-error').hide();
                $('#textarea-error').hide();
            }
        });

        // Sự kiện khi người dùng nhập vào textarea
        $('#customReply').on('input', function() {
            if ($(this).val()) {
                $('#textarea-error').hide();
                $('#dropdown-error').hide();
            }
        });

        // Xử lý sự kiện khi nhấn nút "Reply"
        $('#replyButton').click(function() {
            var selectedDropdown = $('#replyDropdown').val();
            var replyText = $('#customReply').val();
            var id = $('#replyModal').data('reviewId'); // Lấy reviewId từ modal

            if (!selectedDropdown && !replyText) {
                $('#dropdown-error').show();
                $('#textarea-error').show();
                return;
            }

            // Gửi dữ liệu đến Servlet để lưu trữ reply và ngày giờ
            $.ajax({
                type: 'POST',
                url: 'SaveReplyReviewControll',
                data: {
                    id: id,
                    replyText: replyText || selectedDropdown
                },
                success: function(response) {
                    // Cập nhật dữ liệu trong DataTable
                    var currentDate = new Date();
                    var formattedDate = currentDate.getFullYear() + '-' + (currentDate.getMonth() + 1) + '-' + currentDate.getDate() + ' ' +
                        currentDate.getHours() + ':' + currentDate.getMinutes() + ':' + currentDate.getSeconds();

                    table.rows().every(function(rowIdx, tableLoop, rowLoop) {
                        var rowData = this.data();
                        if (rowData.id == id) {
                            rowData.reply = replyText || selectedDropdown;
                            rowData.dateReply = formattedDate;
                            this.data(rowData).draw();
                        }
                    });

                    alert('Đã gửi phản hồi: ' + (replyText || selectedDropdown));
                    closeModal();
                },
                error: function(xhr, status, error) {
                    alert('Đã xảy ra lỗi: ' + error);
                }
            });
        });

    });
</script>

</body>
</html>

