<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Xác Thực Đơn Hàng</title>
    <!-- Include CSS -->
    <jsp:include page="./link/link.jsp"></jsp:include>

    <link href='https://unpkg.com/boxicons@2.1.2/css/boxicons.min.css' rel='stylesheet'>
    <style>
        /* Dòng kẻ màu */
        .xBNaac {
            background-image: repeating-linear-gradient(45deg, #6fa6d6, #6fa6d6 33px, transparent 0, transparent 41px, #f18d9b 0, #f18d9b 74px, transparent 0, transparent 82px);
            background-position-x: -30px;
            background-size: 116px 3px;
            height: 3px;
            width: 100%;
            margin-bottom: 25px;
        }

        /* Modal Container */
        .modal-container {
            margin: 0 auto; /* Canh giữa modal */
            padding-top: 10px;
            width: 100%;
            max-width: 400px;
        }

        /* Modal Content */
        .modalV-content {
            background: #fff;
            text-align: center;
            border-radius: 12px;
            box-shadow: 0px 0px 8px 0px rgba(0, 0, 0, 0.2); /* Thêm shadow */
            border: none; /* Loại bỏ viền */
            margin: 0; /* Đảm bảo không có khoảng trắng ngoài modal */
        }

        /* Modal Header */
        .modalV-header {
            background-color: #6fa6d6;
            color: white;
            font-size: 18px;
            padding: 10px;
            font-weight: bold;
            border-radius: 12px 12px 0 0;
        }

        /* Modal Body */
        .radio-group {
            display: flex;
            flex-direction: column;
            margin-left: 20px;
            margin-top: 20px;
        }

        .radio-item {
            display: flex;
            align-items: center;
            justify-content: flex-start;
        }

        .radio-item input {
            margin-top: -5px;
            margin-left: 20px;
            margin-right: 10px;
        }

        .radio-item label {
            font-size: 16px;
            color: #333;
        }

        /* Input Key Section */
        #key-input-section {
            display: none;
            flex-direction: column;
            align-items: flex-start;
            margin: 15px 20px;
        }

        #key-input {
            width: calc(100% - 20px);
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 5px;
            margin-bottom: 10px;
        }

        #load-key-btn {
            background-color: #dcdcdc;
            color: white;
            border: none;
            padding: 6px 12px;
            border-radius: 8px;
            cursor: pointer;
        }

        #load-key-btn:hover {
            background-color: #dcdcdc;
        }

        /* Modal Footer */
        .modalV-footer {
            text-align: right;
            margin-top: 15px;
        }

        .confirm {
            background-color: #6fa6d6;
            color: white;
            border: none;
            margin: 0px 15px 15px 0px;
            padding: 8px 16px;
            border-radius: 40px;
            cursor: pointer;
        }

        .confirm:hover {
            background-color: #6ca1cf;
        }

        /* Modal Container */
        /* Modal Container */
        .modal-container {
            background-color: #fff;
            border-radius: 12px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
            max-width: 450px;
            width: 100%;
            padding: 0;
            overflow: hidden;
        }

        /* Header */
        .modal-header {
            background-color: #6fa6d6;
            color: white;
            font-size: 16px;
            text-align: center;
            font-weight: bold;
            padding: 16px 0; /* Đảm bảo tiêu đề có khoảng cách dọc */
            display: flex;
            justify-content: center; /* Căn giữa theo chiều ngang */
            align-items: center; /* Căn giữa theo chiều dọc */
            height: 60px; /* Chiều cao cố định */
        }

        /* Body */
        .modal-body {
            padding: 20px;
            text-align: left;
        }

        .modal-body label {
            font-size: 14px;
            margin-bottom: 5px;
            display: block;
            color: #333;
        }

        .hash-input {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
        }

        .hash-input input {
            flex: 1;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 8px;
            font-size: 16px;
        }

        .hash-input button {
            background-color: #d9d9d9;
            color: #a0a0a0;
            border: none;
            padding: 8px 12px;
            border-radius: 8px;
            cursor: not-allowed;
            font-size: 16px;
            margin-left: 10px;
        }

        .tool-link {
            color: #ff6a6a;
            text-decoration: none;
            font-size: 16px;
        }

        .tool-link:hover {
            text-decoration: underline;
        }

        .note {
            font-size: 16px;
            color: #555;
            line-height: 1.5;
            margin-top: 15px;
        }

        /* Footer */
        .modal-footer {
            text-align: right;
            padding: 10px 20px 20px;
        }

        .back-btn {
            background-color: #6fa6d6;
            color: white;
            border: none;
            padding: 8px 12px;
            border-radius: 20px;
            cursor: pointer;
            font-size: 14px;
        }

        .back-btn:hover {
            background-color: #ff6767;
        }

        .back-btn {
            background-color: #f1a7a7;
        }

        .continue-btn {
            background-color: #6fa6d6;
            color: white;
            border: none;
            padding: 8px 18px;
            border-radius: 20px;
            cursor: pointer;
            font-size: 16px;
        }

        .continue-btn:hover {
            background-color: #4a8bc4;
        }


    </style>
</head>
<body>
<!-- Header -->
<jsp:include page="./header/header.jsp"></jsp:include>
<c:url var="sign" value="/SignOrderControll"></c:url>

<!-- Checkout Section -->
<section class="checkout spad">
    <div class="container">
        <div class="checkout__form">
            <h4>Xác thực đơn hàng</h4>
            <!-- Dòng kẻ màu -->
            <div class="xBNaac"></div>

            <!-- Modal Xác Thực -->
            <div class="modal-container" id="modal-key">
                <div class="modalV-content">
                    <!-- Header -->
                    <div class="modalV-header">Chọn khóa</div>
                    <!-- Body -->
                    <div class="radio-group">
                        <!-- Radio: Key hệ thống -->
                        <div class="radio-item">
                            <input type="radio" id="system-key" name="key-option" value="system" checked>
                            <label for="system-key">Sử dụng key do hệ thống cung cấp</label>
                        </div>
                        <!-- Radio: Key cá nhân -->
                        <div class="radio-item">
                            <input type="radio" id="personal-key" name="key-option" value="personal">
                            <label for="personal-key">Sử dụng key cá nhân</label>
                        </div>
                        <!-- Input Key -->
                        <div id="key-input-section">
                            <input type="text" id="key-input" placeholder="Nhập key cá nhân" name="personal-key"/>
                            <button type="button" id="load-key-btn">Load Key</button>
                        </div>
                    </div>
                    <!-- Footer -->
                    <div class="modalV-footer">
                        <button id="continue-btn1" class="confirm">Tiếp tục</button>
                    </div>
                </div>
            </div>

            <!-- Modal Thông Báo -->
            <div class="modal-container" id="modal-info" style="display: none;">
                <div class="modalV-content">
                    <div class="modal-header">Thông tin tạo chữ ký</div>
                    <!-- Body -->
                    <div class="modal-body">
                        <!-- Hash Input -->
                        <label for="hash-data">Hash data</label>
                        <div class="hash-input">
                            <input type="text" id="hash-data" placeholder="" readonly/>
                            <button type="button" id="copy-btn" disabled>Copy</button>
                        </div>
                        <!-- Tool tạo chữ ký -->
                        <label for="tool-link">Tool tạo chữ ký</label>
                        <a href="https://www.figma.com/design" target="_blank" class="tool-link">https://www.figma.com/design</a>
                        <!-- Ghi chú -->
                        <p class="note" style="margin-top: 15px; font-size: 16px;color: #5e5e5e">
                            *Để tạo chữ ký cho đơn hàng bạn cần download tool ở đường link phía trên về máy và copy dữ
                            liệu hash của đơn hàng. Sau đó khởi chạy tool và nhập private key cùng dữ liệu hash vào rồi
                            tạo chữ ký điện tử.
                        </p>
                    </div>
                    <!-- Footer -->
                    <div class="modal-footer">
                        <button type="button" class="back-btn" id="back-btn">Trở lại</button>
                        <button type="button" class="continue-btn" id="continue-btn2">Tiếp tục</button>
                    </div>
                </div>
            </div>

        </div>
    </div>
</section>

<!-- Footer -->
<jsp:include page="./footer/footer.jsp"></jsp:include>

<script>
    const personalKeyRadio = document.getElementById("personal-key");
    const systemKeyRadio = document.getElementById("system-key");
    const keyInputSection = document.getElementById("key-input-section");

    // Thiết lập trạng thái ban đầu khi load trang
    window.addEventListener("load", function () {
        if (systemKeyRadio.checked) {
            keyInputSection.style.display = "none";
        } else if (personalKeyRadio.checked) {
            keyInputSection.style.display = "flex";
        }
    });

    // Hiển thị ô nhập key nếu chọn "key cá nhân"
    personalKeyRadio.addEventListener("change", function () {
        if (personalKeyRadio.checked) {
            keyInputSection.style.display = "flex";
        }
    });

    // Ẩn ô nhập key nếu chọn "key hệ thống"
    systemKeyRadio.addEventListener("change", function () {
        if (systemKeyRadio.checked) {
            keyInputSection.style.display = "none";
        }
    });

    // Xử lý nút "Tiếp tục" chuyển modal
    document.getElementById("continue-btn1").addEventListener("click", function () {
        document.querySelector(".modalV-content").style.display = "none";
        document.getElementById("modal-info").style.display = "block";

        // Dữ liệu giả lập
        const privateKey = "PRIVATE_KEY_EXAMPLE_123456";
        const hashValue = btoa("DATA_TO_HASH");

        const privateKeyBlob = new Blob([privateKey], {type: "text/plain"});
        const privateKeyURL = URL.createObjectURL(privateKeyBlob);
        document.getElementById("download-private-key").href = privateKeyURL;
        document.getElementById("hash-output").value = hashValue;
    });

    const modalKey = document.getElementById("modal-key");
    const modalInfo = document.getElementById("modal-info");
    // Quay lại modal Chọn Key khi nhấn Trở lại
    document.getElementById("back-btn").addEventListener("click", () => {
        modalInfo.style.display = "none";
        modalKey.style.display = "flex";
    });
</script>
</body>
</html>

