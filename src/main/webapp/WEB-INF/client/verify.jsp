<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html>
<head>
    <jsp:include page="link/link-verify.jsp"></jsp:include>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>OTP Verification Form</title>

    <style type="text/css">
        .container {
            width: 60vh;
            height: 80vh;
        }
    </style>
    <!-- Boxicons CSS -->
    <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet"/>
    <title>Verify</title>
</head>
<body>
<c:url var="verify" value="/VerifyControll"></c:url>
<c:url var="resend" value="/VerifyControll?action=resend"></c:url>

<div class="containerL">
    <div class="container">
        <header>
            <i class="bx bxs-check-shield"></i>
        </header>
        <h4>Xác thực</h4>
        <form action="${verify}" method="post" id="form">
            <div class="input-field">
                <input id="n1" name="num1" type="tel" maxlength="1"/>
                <input id="n2" name="num2" type="tel" maxlength="1" disabled/>
                <input id="n3" name="num3" type="tel" maxlength="1" disabled/>
                <input id="n4" name="num4" type="tel" maxlength="1" disabled/>
            </div>
            <br>
            <div class="input-field">
                <div class="g-recaptcha" data-sitekey="6LckfeEpAAAAANP4T8SSXYGGdY-n86aJvX-DcREy"></div>
            </div>
            <span id="noti" style="color: red;">${error}</span>
            <button type="submit">Submit</button>
        </form>
        <div>
            <span id="timer">01:00</span>
            <button id="resendBtn" onclick="resendCode()" style="display: none;">Gửi lại mã</button>
        </div>
    </div>
</div>
<script src="https://www.google.com/recaptcha/api.js"></script>

<script>
    window.onload = function () {
        const form = document.getElementById("form");
        const noti = document.getElementById("noti");
        const timerElement = document.getElementById("timer");
        const resendBtn = document.getElementById("resendBtn");

        form.addEventListener("submit", function (event) {
            event.preventDefault();
            const response = grecaptcha.getResponse();
            console.log(response);

            if (response) {
                form.submit();
            } else {
                noti.innerHTML = "Vui lòng kiểm tra";
            }
        });

        startTimer(60, timerElement, resendBtn);
    };

    function startTimer(duration, display, resendBtn) {
        var timer = duration, minutes, seconds;
        var interval = setInterval(function () {
            minutes = parseInt(timer / 60, 10);
            seconds = parseInt(timer % 60, 10);

            minutes = minutes < 10 ? "0" + minutes : minutes;
            seconds = seconds < 10 ? "0" + seconds : seconds;

            display.textContent = minutes + ":" + seconds;

            if (--timer < 0) {
                clearInterval(interval);
                display.textContent = "00:00";
                resendBtn.style.display = "inline";
            }
        }, 1000);
    }

    function resendCode() {
        window.location.href = "${resend}";
    }

    const inputs = document.querySelectorAll("input[type='tel']"),
        button = document.querySelector("button[type='submit']");

    inputs.forEach((input, index1) => {
        input.addEventListener("keyup", (e) => {
            const currentInput = input,
                nextInput = input.nextElementSibling,
                prevInput = input.previousElementSibling;

            if (currentInput.value.length > 1) {
                currentInput.value = "";
                return;
            }

            if (nextInput && nextInput.hasAttribute("disabled") && currentInput.value !== "") {
                nextInput.removeAttribute("disabled");
                nextInput.focus();
            }

            if (e.key === "Backspace") {
                inputs.forEach((input, index2) => {
                    if (index1 <= index2 && prevInput) {
                        input.setAttribute("disabled", true);
                        input.value = "";
                        prevInput.focus();
                    }
                });
            }

            if (!inputs[3].disabled && inputs[3].value !== "") {
                button.classList.add("active");
                return;
            }
            button.classList.remove("active");
        });
    });

    window.addEventListener("load", () => inputs[0].focus());
</script>
</body>
</html>
