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

    <title>Đăng ký</title>
    <!-- CSS -->
    <jsp:include page="./link/link-register.jsp"></jsp:include>

    <!-- Boxicons CSS -->
    <link href='https://unpkg.com/boxicons@2.1.2/css/boxicons.min.css'
          rel='stylesheet'>
</head>
<body>

<div class="container forms">
    <c:set var="sign" value="/SignupControll"></c:set>
    <!-- Signup Form -->

    <div class="form signup">
        <div class="form-content">
            <header>Signup</header>
            <form action="${sign}" method="post" onsubmit="return validateForm()">
                <div class="field input-field">
                    <input name="name" id="name" type="text" placeholder="Tên" value="${name}" class="input" oninput="clearError('error2'); validateName()">
                    <span class="notify" id="error2">${error2}</span>
                </div>
                <div class="field input-field">
                    <input id="mail" name="email" type="email" placeholder="Email" value="${email}" class="input" oninput="clearError('error3'); validateEmail()">
                    <span class="notify" id="error3">${error3}</span>
                </div>
                <div class="field input-field">
                    <input name="phone" type="tel" placeholder="Số điện thoại" value="${phone}" class="input" id="tele" oninput="clearError('error4'); validatePhone()">
                    <span class="notify" id="error4">${error4}</span>
                </div>

                <div class="field input-field">
                    <input name="passw" type="password" placeholder="Mật khẩu" value="${passw}" class="password" id="pass" oninput="clearError('error5'); validatePassword()">
                    <i class='bx bx-hide eye-icon'></i>
                    <span class="notify" id="error5">${error5}</span>
                </div>

                <div class="field input-field">
                    <input name="repassw" type="password" placeholder="Xác nhận mật khẩu" class="password" id="re" oninput="clearError('error6'); validateRePass()">
                    <i class='bx bx-hide eye-icon'></i>
                    <span class="notify" id="error6">${error6}</span>
                </div>

                <div class="field button-field">
                    <button>Đăng ký</button>
                </div>
            </form>
            <c:url var="login" value="LoginControll"></c:url>
            <div class="form-link">
                <span>Bạn đã có tài khoản? <a href="${pageContext.request.contextPath}/${login}" class="link login-link">Đăng nhập</a></span>
            </div>
        </div>
    </div>
</div>

<!-- JavaScript -->
<script src="../client/assets/js/script.js"></script>
<script type="text/javascript">

    var isDeleting = false;

    function validateName() {
        var nameInput = document.getElementById("name");
        var nameError = document.getElementById("error2");
        var name = nameInput.value;

        if (!name) {
            nameError.innerHTML = "Vui lòng nhập tên";
        } else {
            nameError.innerHTML = "";
        }
    }

    function validateEmail() {
        var emailInput = document.getElementById("mail");
        var emailError = document.getElementById("error3");
        var email = emailInput.value;
        var emailRegex = /^\w+@\w+(\.\w+)+(\.\w+)*$/;

        if (!email) {
            emailError.innerHTML = "Vui lòng nhập email";
        } else if (!emailRegex.test(email)) {
            emailError.innerHTML = "Email không đúng định dạng";
        } else {
            emailError.innerHTML = "";
        }
    }

    function validatePhone() {
        var phoneNum = document.getElementById("tele").value;
        var phoneError = document.getElementById("error4");
        var phoneNumPattern = /^0\d{9}$/;

        if (!phoneNum) {
            phoneError.innerHTML = "Vui lòng nhập số điện thoại";
        } else if (!phoneNumPattern.test(phoneNum)) {
            phoneError.innerHTML = "Số điện thoại sai định dạng";
        } else {
            phoneError.innerHTML = "";
        }
    }

    function validatePassword() {
        var password = document.getElementById("pass").value;
        var passwordError = document.getElementById("error5");

        if (!password) {
            passwordError.innerHTML = "Vui lòng nhập mật khẩu";
            return;
        }

        if (password.length < 8) {
            passwordError.innerHTML = "Mật khẩu phải chứa 8 kí tự";
            return;
        }

        var uppercaseRegex = /[A-Z]/;
        var lowercaseRegex = /[a-z]/;
        var numberRegex = /[0-9]/;
        var specialCharacterRegex = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/;

        if (!uppercaseRegex.test(password)) {
            passwordError.innerHTML = "Mật khẩu phải chứa ít nhất 1 kí tự viết hoa";
        } else if (!lowercaseRegex.test(password)) {
            passwordError.innerHTML = "Mật khẩu phải chứa ít nhất 1 kí tự viết thường";
        } else if (!numberRegex.test(password)) {
            passwordError.innerHTML = "Mật khẩu phải chứa ít nhất 1 số";
        } else if (!specialCharacterRegex.test(password)) {
            passwordError.innerHTML = "Mật khẩu phải chứa ít nhất 1 kí tự đặc biệt";
        } else {
            passwordError.innerHTML = "";
        }
    }

    function validateRePass() {
        var passW = document.getElementById("pass").value;
        var rePass = document.getElementById("re").value;
        var rePassError = document.getElementById("error6");

        if (!rePass) {
            rePassError.innerHTML = "Vui lòng xác nhận mật khẩu";
        } else if (passW !== rePass) {
            rePassError.innerHTML = "Mật khẩu không trùng khớp";
        } else {
            rePassError.innerHTML = "";
        }
    }

    function clearError(elementId) {
        document.getElementById(elementId).innerHTML = "";
    }

    function validateForm() {
        validateName();
        validateEmail();
        validatePhone();
        validatePassword();
        validateRePass();

        var errors = document.getElementsByClassName("notify");
        for (var i = 0; i < errors.length; i++) {
            if (errors[i].innerHTML !== "") {
                return false;
            }
        }
        return true;
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
