<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="zxx">
<head>
    <meta charset="UTF-8">
    <title>Xác Thực Đơn Hàng</title>
    <link rel="stylesheet" href="/assets/css/verify-order.css">
    <jsp:include page="./link/link.jsp"></jsp:include>
</head>
<body>
<jsp:include page="./header/header.jsp"/>
<section class="checkout spad">
    <div class="container">
        <div class="checkout__form">
            <!-- Modal Xác Thực -->
            <div class="modal-container" id="modal-key" style="max-width: 380px">
                <div class="modalV-content">
                    <div class="modalV-header">Khóa</div>
                    <div class="radio-group">
                        <div class="case2">
                            <div class="radio-item">
                                <input type="radio" id="system-key" name="key-option" value="system" checked>
                                <label for="system-key">Chưa có private key</label>
                            </div>
                            <div class="radio-item">
                                <input type="radio" id="personal-key" name="key-option" value="personal">
                                <label for="personal-key">Đã có private key</label>
                            </div>
                        </div>
                    </div>
                    <div class="modalV-footer">
                        <button id="continue-btn1" class="confirm">Tiếp tục</button>
                    </div>
                </div>
            </div>
            <!-- Modal Thông Tin -->
            <div class="modal-container" id="modal-info" style="display: none;">
                <div class="modalI-header">Thông tin tạo chữ ký</div>
                <div class="modalI-body">
                    <label for="hash-data">Hash data</label>
                    <div class="hash-input">
                        <input type="text" id="hash-data" readonly/>
                        <button type="button" id="copy-btn" disabled>Copy</button>
                    </div>
                    <label for="tool-link">Tool tạo chữ ký</label>
                    <a href="https://drive.google.com/file/d/1bJ8_JngGNPTMXe4R7NxR389swiUOPYhd/view?usp=sharing" download>Download Tool</a>
                    <p class="note">
                        Để tạo chữ ký điện tử cho đơn hàng bạn cần download tool ở đường link phía trên về máy (bỏ qua nếu đã thực hiện trước đó) và copy dữ liệu hash của đơn hàng. Sau đó khởi chạy tool rồi nhập private key cùng dữ liệu hash vào để tạo chữ ký.
                    </p>
                </div>
                <div class="modalI-footer">
                    <button type="button" class="continue-btn" id="continue-btn2">Tiếp Tục</button>
                </div>
            </div>
            <!-- Modal Sign -->
            <div class="modal-container" id="modal-verify" style="display: none;">
                <div class="modalI-header">Chữ ký số cho đơn hàng</div>
                <div class="modalI-body" style="padding: 20px">
                    <div class="hash-input" style="margin-bottom: 0px;">
                        <input type="text" id="sign-input" placeholder="Nhập chữ ký điện tử" name="personal-key"/>
                        <input type="file" id="file-input3" style="display: none;"/>
                        <button type="button" id="load-sign-btn">Load Sign</button>
                    </div>
                </div>
                <div class="modalV-footer" style="padding: 15px;margin-top: 0px">
                    <button type="button" class="back-btn" id="back-btn2">Trở Lại</button>
                    <button type="button" class="continue-btn" id="verify-btn">Xác Thực</button>
                </div>
            </div>
        </div>
    </div>
</section>
<jsp:include page="./footer/footer.jsp"/>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<!-- Scripts -->
<script>
    $(function () {
        // Xử lý AJAX cho nút Tiếp tục
        $("#continue-btn1").on("click", function () {
            let keyType = $("input[name='key-option']:checked").val();
            let data = { keyType: keyType };

            $.ajax({
                url: "HashOrderOPControll",
                type: "POST",
                data: data,
                dataType: "json",
                success: function (response) {
                    if (response.status === "success") {
                        if (response.hashData) {
                            $("#hash-data").val(response.hashData);
                            $("#copy-btn").prop("disabled", false);
                        }
                        $("#modal-key").hide();
                        $("#modal-info").show();
                    } else {
                        alert(response.message || "Có lỗi xảy ra khi xử lý");
                    }
                },
                error: function (xhr, status, err) {
                    alert("Đã xảy ra lỗi trong quá trình gửi yêu cầu. " + (xhr.responseText || err));
                }
            });
        });

        // Chuyển modal tiếp tục
        $("#continue-btn2").on("click", function () {
            $("#modal-info").hide();
            $("#modal-verify").show();
        });

        // Chuyển modal trở lại
        $("#back-btn2").on("click", function () {
            $("#modal-verify").hide();
            $("#modal-info").show();
        });

        // Sao chép hashData
        $("#copy-btn").on("click", function () {
            const hashDataInput = document.getElementById("hash-data");
            hashDataInput.select();
            document.execCommand("copy");
            showCustomNotification("Đã sao chép hash data của đơn hàng");
        });

        // Xác thực chữ ký
        $("#verify-btn").on("click", function () {
            let signData = $("#sign-input").val().trim();
            if (!signData) {
                alert("Chữ ký không được để trống.");
                return;
            }
            // Gửi form xác thực chữ ký
            const form = $("<form>", {
                "method": "POST",
                "action": "SignOrderDetailControll"
            });
            form.append($("<input>", {
                "type": "hidden",
                "name": "signData",
                "value": signData
            }));
            form.appendTo("body").submit();
        });

        // Notification helper
        function showCustomNotification(message) {
            var notification = document.createElement('div');
            notification.className = 'custom-notification';
            var notificationContent = document.createElement('div');
            notificationContent.className = 'notification-content';
            var messageSpan = document.createElement('span');
            messageSpan.textContent = message;
            notificationContent.appendChild(messageSpan);
            notification.appendChild(notificationContent);
            document.body.appendChild(notification);
            setTimeout(function () {
                notification.style.opacity = '0';
                setTimeout(function () {
                    notification.remove();
                }, 300);
            }, 1000);
        }
    });
</script>
</body>
</html>