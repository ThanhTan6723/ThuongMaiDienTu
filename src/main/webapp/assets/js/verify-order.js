document.addEventListener("DOMContentLoaded", () => {
    const keyInputSection = document.getElementById("key-input-section");
    const personalKeyRadio = document.getElementById("personal-key");
    const systemKeyRadio = document.getElementById("system-key");
    const keyInput = document.getElementById("key-input");
    const fileInput = document.getElementById("file-input");
    const fileInput2 = document.getElementById("file-input2");
    const signInput = document.getElementById("sign-input");

    const toggleKeyInput = () => {
        const isPersonal = personalKeyRadio.checked;
        keyInputSection.style.display = isPersonal ? "flex" : "none";
        keyInput.value = "";
        keyInput.readOnly = !isPersonal;
        keyInput.type = "text";
    };

    toggleKeyInput();
    [personalKeyRadio, systemKeyRadio].forEach(radio => radio.addEventListener("change", toggleKeyInput));

    if ($("#key-input1").val().trim()) {
        keyContent = $("#key-input1").val().trim();
        sendAjaxRequest("external", keyContent);
        return;
    }

    const sendAjaxRequest = (keyType, keyContent) => {
        $.ajax({
            url: "HashOrderControll",
            type: "POST",
            data: {keyType, keyContent},
            dataType: "json",
            success: response => {
                if (response.status === "success") {
                    if (response.hashData) {
                        $("#hash-data").val(response.hashData);
                        $("#copy-btn").prop("disabled", false);
                    }
                    $("#modal-key").hide();
                    $("#modal-info").show();
                } else {
                    alert(response.message);
                }
            },
            error: () => alert("Đã xảy ra lỗi trong quá trình gửi yêu cầu."),
        });
    };

    if (keyType === "personal") {
        keyContent = $("#key-input2").val().trim();
        if (!keyContent) {
            const fileInput = $("#file-input2")[0].files[0];
            if (fileInput) {
                const reader = new FileReader();
                reader.onload = event => {
                    keyContent = event.target.result;
                    sendAjaxRequest(keyType, keyContent);
                };
                reader.readAsText(fileInput);
            } else {
                alert("Vui lòng nhập khóa cá nhân hoặc tải file khóa.");
            }
        } else {
            sendAjaxRequest(keyType, keyContent);
        }
    } else if (keyType === "system") {
        console.log("key type "+keyType);
        sendAjaxRequest(keyType, "");
    }

    // Xử lý AJAX cho nút Tiếp tục
    $("#continue-btn1").on("click", () => {
        const keyType = $("input[name='key-option']:checked").val();
        const keyContent = keyType === "personal" ? $("#key-input").val() : "";

        // Gửi AJAX request
        $.ajax({
            url: "HashOrderControll",
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
    });

    // Xử lý sự kiện sao chép hashData
    $("#copy-btn").on("click", () => {
        const hashDataInput = document.getElementById("hash-data");
        hashDataInput.select();
        document.execCommand("copy");
        alert("Đã sao chép hash data: " + hashDataInput.value);
    });


    // Xử lý chuyển modal
    const setupModalNavigation = (btnId, hideModalId, showModalId) => {
        document.getElementById(btnId).addEventListener("click", () => {
            document.getElementById(hideModalId).style.display = "none";
            document.getElementById(showModalId).style.display = "block";
        });
    };

    setupModalNavigation("continue-btn2", "modal-info", "modal-verify");
    setupModalNavigation("back-btn", "modal-info", "modal-key");
    setupModalNavigation("back-btn2", "modal-verify", "modal-info");
});
