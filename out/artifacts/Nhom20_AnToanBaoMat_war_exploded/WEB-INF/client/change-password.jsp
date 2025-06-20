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

    <title>Change Password</title>
    <!-- CSS -->
    <jsp:include page="./link/link-register.jsp"></jsp:include>

    <!-- Boxicons CSS -->
    <link href='https://unpkg.com/boxicons@2.1.2/css/boxicons.min.css'
          rel='stylesheet'>
</head>
<body>

<div class="container forms">
    <c:set var="oldPass" value="Old password"></c:set>
    <c:set var="createPass" value="Create password"></c:set>
    <c:set var="confirmPass" value="Confirm password"></c:set>
    <!-- Signup Form -->

    <div class="form signup">
        <div class="form-content">
            <header>Đổi mật khẩu</header>
            <div class="form-link">
                <span style="font-size: 18px;color: #3472ac;"><b>Vui lòng nhập lại mật khẩu cũ để đặt mật khẩu mới</b></span>
            </div>
            <c:url var="change" value="/ChangePassword"></c:url>
            <form action="${change}" method="post">
                <div class="field input-field">
                    <input name="oldpass" type="password" placeholder="Mật khẩu cũ" class="password" id="old">
                    <i class='bx bx-hide eye-icon'></i>
                    <span style="color: red;">${errorOP}</span>
                </div>
                <div class="field input-field">
                    <input name="newpass" type="password" onkeyup="validatePassword()" placeholder="Mật khẩu mới"
                           class="password" id="pass">
                    <i class='bx bx-hide eye-icon'></i>
                    <p id="noti1" style="color: red;">${errorNP}</p>
                </div>

                <div class="field input-field">
                    <input name="repass" type="password" onkeyup="validateRePass()" placeholder="Xác nhận mật khẩu"
                           class="password" id="re">
                    <i class='bx bx-hide eye-icon'></i>
                    <span id="noti2" style="color: red; ">${errorCFP}</span>
                </div>

                <div class="field button-field">
                    <button>Submit</button>
                </div>
            </form>

        </div>
    </div>

    <!-- JavaScript -->
    <script src="../client/assets/js/script.js"></script>
    <script type="text/javascript">

        function validateRePass() {
            var passW = document.getElementById("pass").value;
            var rePass = document.getElementById("re").value;
            if (passW != rePass) {
                document.getElementById("noti2").innerHTML = "Password incorrect";
                return false;
            } else {
                document.getElementById("noti2").innerHTML = "";
                return true;
            }
        }

        function validatePassword() {
            var password = document.getElementById("pass").value;

            // Kiểm tra độ dài của mật khẩu
            if (password.length < 8) {
                document.getElementById("noti1").innerHTML = "Mật khẩu phải chứa 8 kí tự";
                return false;
            }

            // Kiểm tra xem mật khẩu có chứa ít nhất một chữ cái viết hoa, một chữ cái viết thường, một số và một ký tự đặc biệt hay không
            var uppercaseRegex = /[A-Z]/;
            var lowercaseRegex = /[a-z]/;
            var numberRegex = /[0-9]/;
            var specialCharacterRegex = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/;

            if (!uppercaseRegex.test(password)) {
                document.getElementById("noti1").innerHTML = "Mật khẩu phải chứa ít nhất 1 kí tự viết hoa";
                return false;
            } else if (!lowercaseRegex.test(password)) {
                document.getElementById("noti1").innerHTML = "Mật khẩu phải chứa ít nhất 1 kí tự viết thường";
                return false;
            } else if (!numberRegex.test(password)) {
                document.getElementById("noti1").innerHTML = "Mật khẩu phải chứa ít nhất 1 số";
                return false;
            } else if (!specialCharacterRegex.test(password)) {
                document.getElementById("noti1").innerHTML = "Mật khẩu phải chứa ít nhất 1 kí tự đặc biệt";
                return false;
            } else {
                document.getElementById("noti1").innerHTML = "";
                return true;
            }
        }

        const pwShowHide = document.querySelectorAll(".eye-icon");

        pwShowHide.forEach(eyeIcon => {
            eyeIcon.addEventListener("click", () => {
                const passwordField = eyeIcon.previousElementSibling; // Get the previous sibling which is the password input

                if (passwordField.type === "password") {
                    passwordField.type = "text";
                    eyeIcon.classList.replace("bx-hide", "bx-show");
                } else {
                    passwordField.type = "password";
                    eyeIcon.classList.replace("bx-show", "bx-hide");
                }
            });
        });

    </script>
</body>
</html>