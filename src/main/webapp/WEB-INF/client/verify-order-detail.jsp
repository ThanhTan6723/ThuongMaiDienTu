<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ page isELIgnored="false" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="description" content="Ogani Template">
    <meta name="keywords" content="Ogani, unica, creative, html">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Xác Thực Đơn Hàng</title>
    <!-- Include CSS -->
    <link rel="stylesheet" href="/assets/css/verify-order.css">
    <jsp:include page="./link/link.jsp"></jsp:include>
    <style>
        .tool-link {
            color: #ff6a6a;
            text-decoration: none;
            font-size: 16px;
            vertical-align: middle; /* Đảm bảo link căn giữa với label */
        }

        .tool-link:hover {
            text-decoration: underline;
            color: #ff6060;
        }
    </style>
</head>
<body>
<!-- Header -->
<jsp:include page="./header/header.jsp"/>

<!-- Checkout Section -->
<section class="checkout spad">
    <div class="container">
        <div class="checkout__form">
            <!-- Modal Xác Thực -->
            <div class="modal-container" id="modal-key" style="max-width: 380px">
                <div class="modalV-content">
                    <div class="modalV-header">Khóa</div>
                    <div class="radio-group">
                        <div class="case2" style="${hasPublicKey ? 'display: blocl;' : 'display: none;'}">
                            <span>Vui lòng nhập private key của bạn</span>
                            <div id="key-input-section1">
                                <input type="text" id="key-input1" placeholder="Nhập key cá nhân"
                                       name="personal-key"/>
                                <input type="file" id="file-input1" style="display: none;"/>
                                <button type="button" id="load-key-btn1">Load key</button>
                            </div>
                        </div>
                        <div class="case2" style="${hasPublicKey ? 'display: none;' : 'display: block;'}">
                            <div class="radio-item">
                                <input type="radio" id="system-key" name="key-option" value="system" checked>
                                <label for="system-key">Chưa có private key</label>
                            </div>
                            <div class="radio-item">
                                <input type="radio" id="personal-key" name="key-option" value="personal">
                                <label for="personal-key">Sử dụng private key bên ngoài</label>
                            </div>
                            <div id="key-input-section2" style="display: none; margin-top: 15px;">
                                <input type="text" id="key-input2" placeholder="Nhập key cá nhân" name="personal-key"/>
                                <input type="file" id="file-input2" style="display: none;"/>
                                <button type="button" id="load-key-btn2">Load key</button>
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
                    <%--                    https://drive.google.com/file/d/1Y9EoYTxjvAvzFhTAnR7Cj0J_BlSWgFft/view?usp=sharing--%>
                    <a href="https://drive.google.com/file/d/1bJ8_JngGNPTMXe4R7NxR389swiUOPYhd/view?usp=sharing" download>Download Tool</a>
                    <p class="note">Để tạo chữ ký điện tử cho đơn hàng bạn cần download tool ở đường link phía trên về
                        máy(Bỏ qua nếu đã thực hiện trước đó) và
                        copy dữ liệu hash của đơn hàng. Sau đó khởi chạy tool rồi nhập private key cùng dữ liệu hash vào
                        để tạo chữ ký.</p>
                </div>
                <div class="modalI-footer">
                    <%--                    <button type="button" class="back-btn" id="back-btn">Trở Lại</button>--%>
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

<!-- Footer -->
<jsp:include page="./footer/footer.jsp"/>

<!-- Scripts -->
<script>
    document.addEventListener("DOMContentLoaded", () => {
        const toggleKeyInput = () => {
            const personalKeySelected = document.getElementById("personal-key").checked;
            const keyInputSection = document.getElementById("key-input-section2");
            const keyInput = document.getElementById("key-input2");

            keyInputSection.style.display = personalKeySelected ? "flex" : "none";
            keyInput.value = "";
            keyInput.readOnly = !personalKeySelected;
        };

        const setupRadioToggle = () => {
            const personalKeyRadio = document.getElementById("personal-key");
            const systemKeyRadio = document.getElementById("system-key");
            [personalKeyRadio, systemKeyRadio].forEach(radio =>
                radio.addEventListener("change", toggleKeyInput)
            );
        };

        toggleKeyInput();
        setupRadioToggle();

        const setupFileLoader = (triggerBtnId, fileInputId, targetInputId, successMessage) => {
            const triggerBtn = document.getElementById(triggerBtnId);
            const fileInput = document.getElementById(fileInputId);
            const targetInput = document.getElementById(targetInputId);

            triggerBtn.addEventListener("click", () => fileInput.click());

            fileInput.addEventListener("change", () => {
                const file = fileInput.files[0];
                if (!file) return alert("Vui lòng chọn một tệp hợp lệ.");

                const reader = new FileReader();
                reader.onload = event => {
                    const content = event.target.result.trim();
                    if (!content) return alert("Tệp không chứa dữ liệu hợp lệ.");
                    targetInput.value = content;
                    targetInput.type = "password";
                    showCustomNotification(successMessage);
                };
                reader.onerror = () => alert("Đã xảy ra lỗi khi đọc tệp.");
                reader.readAsText(file);
            });
        };

        // Cấu hình các bộ load file
        setupFileLoader("load-key-btn1", "file-input1", "key-input1", "Đã load key từ tệp.");
        setupFileLoader("load-key-btn2", "file-input2", "key-input2", "Đã load key từ tệp.");
        setupFileLoader("load-sign-btn", "file-input3", "sign-input", "Đã load chữ ký từ tệp.");

        // Xử lý AJAX cho nút Tiếp tục
        $("#continue-btn1").on("click", () => {
            let keyType = $("input[name='key-option']:checked").val();

            let keyContent = keyType === "personal" ? $("#key-input2").val() : "";
            if ($("#key-input1").val().trim()) {
                keyType = 'external';
                keyContent = $("#key-input1").val().trim();
            }
            console.log(keyType);
            // Kiểm tra điều kiện keyContent và keyType
            const isPrivateKeyRequired = keyType === "personal" && !keyContent;
            const isKeyInputEmpty = !$("#key-input1").val().trim() && keyType === "external";

            // Nếu một trong hai điều kiện trên là đúng, hiển thị thông báo
            if (isPrivateKeyRequired || isKeyInputEmpty) {
                alert("Bạn cần nhập private key để tiếp tục");
            } else {
                // Gửi AJAX request
                $.ajax({
                    url: "HashOrderOPControll?orderId=${sessionScope.orderId}",
                    type: "POST",
                    data: {keyType, keyContent},
                    dataType: "json",
                    success: response => {
                        if (response.status === "success") {
                            // Gán hashData vào input và enable nút Copy nếu có dữ liệu
                            if (response.hashData) {
                                $("#hash-data").val(response.hashData);
                                $("#copy-btn").prop("disabled", false);
                            }

                            // Hiển thị modal-info và ẩn modal-key
                            $("#modal-key").hide();
                            $("#modal-info").show();
                        } else {
                            alert(response.message); // Thông báo lỗi từ server
                        }
                    },
                    error: () => {
                        alert("Đã xảy ra lỗi trong quá trình gửi yêu cầu.");
                    },
                });
            }
        });

        $("#verify-btn").on("click", () => {
            let signData = $("#sign-input").val().trim();  // Loại bỏ khoảng trắng thừa

            // Kiểm tra nếu signData trống
            if (!signData) {
                alert("Chữ ký không được để trống.");
                return;
            }

            // Tạo một form và gửi trực tiếp (không dùng AJAX)
            const form = $("<form>", {
                "method": "POST",
                "action": "SignOrderDetailControll"
            });

            // Thêm dữ liệu chữ ký vào form
            form.append($("<input>", {
                "type": "hidden",
                "name": "signData",
                "value": signData
            }));

            // Gửi form
            form.appendTo("body").submit();
        });


        // Xử lý sự kiện sao chép hashData
        $("#copy-btn").on("click", () => {
            const hashDataInput = document.getElementById("hash-data");
            hashDataInput.select();
            document.execCommand("copy");
            showCustomNotification("Đã sao chép hash data của đơn hàng");
            // alert("Đã sao chép hash data: " + hashDataInput.value);
        });
        // Xử lý chuyển modal
        const setupModalNavigation = (btnId, hideModalId, showModalId) => {
            document.getElementById(btnId).addEventListener("click", () => {
                document.getElementById(hideModalId).style.display = "none";
                document.getElementById(showModalId).style.display = "block";
            });
        };

        setupModalNavigation("continue-btn2", "modal-info", "modal-verify");
        // setupModalNavigation("back-btn", "modal-info", "modal-key");
        setupModalNavigation("back-btn2", "modal-verify", "modal-info");

        // Hàm hiển thị thông báo tùy chỉnh
        function showCustomNotification(message) {
            // Tạo một div thông báo
            var notification = document.createElement('div');
            notification.className = 'custom-notification';

            // Thêm nội dung thông báo vào notification
            var notificationContent = document.createElement('div');
            notificationContent.className = 'notification-content';

            var messageSpan = document.createElement('span');
            messageSpan.textContent = message;

            notificationContent.appendChild(messageSpan);
            notification.appendChild(notificationContent);

            // Thêm thông báo vào body của trang
            document.body.appendChild(notification);

            // Tạo hiệu ứng mờ dần và xóa thông báo sau 5 giây
            setTimeout(function () {
                notification.style.opacity = '0';
                setTimeout(function () {
                    notification.remove();
                }, 300); // Sau 0.5 giây để hoàn tất hiệu ứng
            }, 1000); // Hiển thị trong 5 giây
        }

    });
</script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/axios/0.21.1/axios.min.js"></script>
</body>
</html>
