$(document).ready(function () {
    let isSuccess = false;
    let payInterval;
    let timeOut;
    let countdownInterval;
    let modal = document.getElementById('qrModal');
    let countdownElement = document.getElementById('countdown');
    let initialCountdownTime = 300; // 5 phút
    let countdownTime = initialCountdownTime;
    const MY_BANK = {
        BANK_ID: '970422', // Mã ngân hàng MB Bank theo VietQR (nên dùng mã số, không dùng tên)
        BANK_NO: '0000001262665',
        ACCOUNT_NAME: 'CAO THANH TAN'
    };

    document.getElementById('qr').addEventListener('change', async function () {
        var img = document.getElementById('QRCODE-Img');
        // var paidPrice = document.getElementById('total').textContent;
        var paidPrice = '2000';
        let formattedNumber = paidPrice.replace(/[, ₫]/g, '');
        // Sinh mã 4 số ngẫu nhiên
        let randomCode = generateRandomCode();
        var paidContent = `THANH TAN CHUYEN KHOAN ${randomCode}`;

        // Hiển thị modal
        modal.style.display = 'flex';
        document.body.style.overflow = 'hidden';

        // Gọi API VietQR để lấy mã QR động
        const qrDataUrl = await generateQRCode(formattedNumber, paidContent);
        if (qrDataUrl) {
            img.src = qrDataUrl;
            // Đặt lại thời gian đếm ngược
            countdownTime = initialCountdownTime;
            updateCountdownDisplay(countdownTime);

            // Đặt interval để kiểm tra thanh toán
            timeOut = setTimeout(() => {
                payInterval = setInterval(() => {
                    checkPay(formattedNumber, paidContent);
                }, 1000);
            }, 2000);

            // Thiết lập thời gian đếm ngược
            countdownInterval = setInterval(() => {
                if (countdownTime > 0) {
                    countdownTime--;
                    updateCountdownDisplay(countdownTime);
                } else {
                    clearInterval(countdownInterval);
                    closeModal();
                }
            }, 1000);
        } else {
            showCustomNotification('Không thể tạo mã QR. Vui lòng thử lại.');
            closeModal();
        }
    });

    window.addEventListener('click', function (event) {
        if (event.target === modal) {
            closeModal();
        }
    });

    let isCheckingPayment = false;

    async function generateQRCode(amount, content) {
        try {
            const response = await fetch('https://api.vietqr.io/v2/generate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    accountNo: MY_BANK.BANK_NO,
                    accountName: MY_BANK.ACCOUNT_NAME,
                    acqId: MY_BANK.BANK_ID,
                    amount: amount,
                    addInfo: content,
                    format: 'jpg',
                    template: 'compact'
                })
            });

            const data = await response.json();
            if (data.code === '00') {
                return data.data.qrDataURL;
            } else {
                return null;
            }
        } catch (error) {
            console.error('Error generating QR:', error);
            return null;
        }
    }

    async function checkPay(total, content) {
        if (isSuccess || isCheckingPayment) {
            return;
        }
        isCheckingPayment = true;
        try {
            const response = await fetch(
                'https://script.google.com/macros/s/AKfycbxxkHpsxmSHWtQryJpvk1zyz8Vs8aq557OzYsFPDPxSus9UEXTQUjrMxERS44Az1jqw/exec'
            );
            const data = await response.json();
            const lastPay = data.data[data.data.length - 1];
            const lastTotal = lastPay['Giá trị'];
            const lastContent = lastPay['Mô tả'];

            if (lastTotal >= total && lastContent.includes(content.trim().toUpperCase())) {
                isSuccess = true;
                isCheckingPayment = false;

                clearInterval(payInterval);
                closeModal();

                showCustomNotification('Thanh toán thành công!');
                setTimeout(() => {
                    showConfirmationModal();
                }, 2000);

                await fetch('/PaymentByQRCodeControll', {
                    method: 'POST',
                });
            }
        } catch (error) {
            console.error('ERROR:', error);
        } finally {
            isCheckingPayment = false;
        }
    }

    function closeModal() {
        modal.style.display = 'none';
        document.body.style.overflow = '';
        clearInterval(payInterval);
        clearTimeout(timeOut);
        clearInterval(countdownInterval);
        isSuccess = false;
        isCheckingPayment = false;
    }

    function generateRandomCode() {
        return Math.floor(1000 + Math.random() * 9000);
    }

    function showCustomNotification(message) {
        var notification = document.createElement('div');
        notification.className = 'custom-notification';
        notification.innerHTML = `
        <div class="notification-content">
            <img src="/assets/img/tick.png" alt="Notification Image" class="notification-image">
            <span>${message}</span>
        </div>
        `;
        document.body.appendChild(notification);
        setTimeout(function () {
            notification.classList.add('hide');
            setTimeout(function () {
                notification.remove();
            }, 300);
        }, 1000);
    }

    function showConfirmationModal() {
        const confirmationModal = document.getElementById('confirmationModal');
        confirmationModal.style.display = 'flex';
        document.body.style.overflow = 'hidden';
    }

    function updateCountdownDisplay(time) {
        let minutes = Math.floor(time / 60);
        let seconds = time % 60;
        countdownElement.textContent = `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
    }
});