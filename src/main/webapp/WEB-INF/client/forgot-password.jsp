<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Forgot password</title>
    <!-- CSS -->
    <jsp:include page="./link/link-register.jsp"></jsp:include>
    <!-- Boxicons CSS -->
    <link href='https://unpkg.com/boxicons@2.1.2/css/boxicons.min.css'
          rel='stylesheet'>
    <style type="text/css">
        .container {
            padding-bottom: 150px;
        }
    </style>
</head>
<body>
<c:url var="forgot" value="/ForgotControll"></c:url>
<c:set var="email" value="Email"></c:set>

<div class="container forms">
    <div class="form login">
        <div class="form-content">
            <header>Forgot password</header>
            <div class="form-link">
                <span style="font-size: 18px; color: #3472ac;"><b>Vui lòng nhập email của bạn để lấy lại mật khẩu</b></span>
            </div>
            <form id="forgotForm" action="${forgot}" method="get">
                <div class="field input-field">
                    <input type="email" id="mail" name="email" placeholder="${email}" class="input" required="required">
                    <span id="notify" style="color: red; "></span>
                </div>
                <div class="form-link"></div>
                <div class="field button-field">
                    <button type="submit">Gửi</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- JavaScript -->
<script type="text/javascript">
    document.getElementById("forgotForm").addEventListener("submit", function(event) {
        var emailInput = document.getElementById("mail");
        var emailError = document.getElementById("notify");
        var email = emailInput.value.trim();
        var emailRegex = /^\w+@\w+(\.\w+)+(\.\w+)*$/;

        if (email.length === 0) {
            emailError.innerHTML = "Vui lòng nhập email của bạn";
            event.preventDefault(); // Ngăn chặn form submit khi có lỗi
        } else {
            if (!emailRegex.test(email)) {
                emailError.innerHTML = "Email không đúng định dạng";
                event.preventDefault(); // Ngăn chặn form submit khi có lỗi
            } else {
                emailError.innerHTML = "";
            }
        }
    });
</script>

<script src="../client/assets/js/script.js"></script>
</body>
</html>
