<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="description" content="Thông báo về việc tạo lại private key">
    <meta name="keywords" content="private key, xác nhận, email">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Xác Nhận Tạo Lại Private Key</title>

    <link rel="stylesheet" href="./assets/css/report-key.css">
    <jsp:include page="./link/link.jsp"></jsp:include>
</head>
<body>
<!-- Header -->
<jsp:include page="./header/header.jsp"/>

<section class="report-section spad">
    <div class="container">
        <div class="report-container">

            <!-- Modal Confirm -->
            <div class="modal-overlay" id="modal-confirm">
                <div class="modalV-content">
                    <div class="modalV-header">Xác Nhận</div>
                    <div class="modalV-body">
                        <p>Bạn chắc chắn muốn tạo lại private key mới? Sau khi xác nhận, key mới sẽ được gửi tới email
                            của bạn.</p>
                    </div>
                    <div class="modalV-footer">
                        <button id="confirm-btn" class="confirm">Xác Nhận</button>
                        <button id="cancel-btn" class="cancel">Hủy</button>
                    </div>
                </div>
            </div>

            <!-- Thông báo gửi thành công -->
            <div class="modal-overlay hidden" id="success-message-container">
                <div class="modalV-content">
                    <div class="modalV-header">Gửi Thành Công</div>
                    <div class="modalV-body">
                        <p>Private key đã được gửi thành công đến email của bạn!</p>
                    </div>
                    <div class="modalV-footer">
                        <button id="go-home-btn" class="confirm">Quay Lại Trang Chủ</button>
                    </div>
                </div>
            </div>

            <div id="message"></div>
        </div>
    </div>
</section>

<jsp:include page="./footer/footer.jsp"/>

<script src="/assets/js/report.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const messageDiv = document.getElementById("message");
        const successMessageContainer = document.getElementById("success-message-container");
        const goHomeBtn = document.getElementById("go-home-btn");

        const accountId = "${accountId}"; // Lấy accountId từ JSP
        console.log("DOM đã sẵn sàng");
        console.log("Tìm nút:", document.getElementById("confirm-btn")); // Log để kiểm tra nút
        console.log("accountId:", accountId); // Log giá trị

        $("#confirm-btn").on("click", function () {
            const accountId = "${accountId}";
            const reportTime = "${reportTime}";

            $.ajax({
                url: "/ReportControll",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify({
                    accountId: accountId,
                    reportTime: reportTime
                }),
                success: function (response) {
                    console.log("Response từ server:", response);

                    if (response.status === "success") {
                        successMessageContainer.classList.remove('hidden');
                        $("#modal-confirm").hide();
                    } else {
                        messageDiv.innerHTML = `<p class="error-message">${response.message}</p>`;
                    }
                },
                error: function (xhr, status, error) {
                    console.error("AJAX Error:", error);
                    messageDiv.innerHTML = `<p class="error-message">Có lỗi xảy ra khi gửi yêu cầu: ${error}</p>`;
                }
            });
        });


        // Hủy và quay lại trang chủ
        $("#cancel-btn").on("click", function () {
            window.location.href = "/";
        });

        // Khi người dùng nhấn nút "Quay Lại Trang Chủ", chuyển hướng đến trang chủ
        goHomeBtn.addEventListener("click", function () {
            window.location.href = "/";
        });
    });
</script>
</body>
</html>
