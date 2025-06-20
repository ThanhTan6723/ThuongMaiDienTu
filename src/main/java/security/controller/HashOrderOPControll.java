package security.controller;

import dao.client.KeyDao;
import dao.client.VerifyDAO;
import model.Account;
import model.Email;
import security.key.Key;
import security.sign.SHA;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "HashOrderOPControll", value = "/HashOrderOPControll")
public class HashOrderOPControll extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        // Lấy orderId từ request
        String orderIdParam = (String) session.getAttribute("orderId");
        System.out.println("Hash"+orderIdParam);
        if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
            sendErrorResponse(response, "orderId không hợp lệ.");
            return;
        }

        int orderId = Integer.parseInt(orderIdParam);
        String keyType = request.getParameter("keyType");
        String keyContent = request.getParameter("keyContent");
        System.out.println("keyType: " + keyType);
        System.out.println("keyContent: " + keyContent);

        String orderData;
        try {
            orderData = VerifyDAO.getOrderData(orderId);
        } catch (Exception e) {
            sendErrorResponse(response, "Lỗi khi lấy dữ liệu đơn hàng");
            return;
        }

        String formattedTime;
        String hashData = null;

        try {
            if ("system".equals(keyType)) {
                formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                hashData = handleSystemKey(account, orderData, formattedTime);
                formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                VerifyDAO.insertHashOrder(orderId, account.getId(), hashData, formattedTime);
                sendSuccessResponse(response, "Sử dụng key do hệ thống cung cấp", hashData);
            } else if ("personal".equals(keyType)) {
                if (keyContent == null || keyContent.trim().isEmpty()) {
                    sendErrorResponse(response, "Key cá nhân không được để trống.");
                } else {
                    formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    hashData = handlePersonalKey(account, keyContent, orderData, formattedTime);
                    VerifyDAO.insertHashOrder(orderId, account.getId(), hashData, formattedTime);
                    sendSuccessResponse(response, "Sử dụng key cá nhân", hashData);
                }
            } else if ("external".equals(keyType)) {
                if (keyContent == null || keyContent.trim().isEmpty()) {
                    sendErrorResponse(response, "Vui lòng tải khóa cá nhân từ file.");
                } else {
                    System.out.println(keyContent);
                    formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    hashData = handleExternalKey(account, keyContent, orderData, formattedTime);
                    VerifyDAO.insertHashOrder(orderId, account.getId(), hashData, formattedTime);
                    sendSuccessResponse(response, "Sử dụng khóa cá nhân tải lên", hashData);
                }
            } else {
                sendErrorResponse(response, "Lựa chọn không hợp lệ");
            }
        } catch (Exception e) {
            sendErrorResponse(response, "Đã xảy ra lỗi: " + e.getMessage());
        }
    }

    private String handleSystemKey(Account account, String orderData, String formattedTime) throws Exception {
        // Tạo key mới
        Key key = new Key();
        key.generateKey();

        // Chuyển public key sang dạng Base64 và lưu vào cơ sở dữ liệu
        String publicKey = Key.publicKeyToBase64(key.getPublicKey());
        KeyDao.insertPublicKey(account.getId(), publicKey, formattedTime, null);

        // Chuyển private key sang dạng Base64
        String privateKey = Key.privateKeyToBase64(key.getPrivateKey());
        System.out.println(privateKey);

        // Lấy email của người dùng từ đối tượng account
        String recipientEmail = account.getEmail();
        String emailTitle = "[Golden Fields] Private key for your account";
        String emailContent = "Kính gửi " + account.getName() + ",\n\n"
                + "Vui lòng tải file đính kèm chứa khóa riêng (private key) của bạn để tạo chữ ký điện tử cho đơn hàng. "
                + "Hãy giữ nó an toàn và không chia sẻ với bất kỳ ai.\n\nTrân trọng.";

        // Tạo file .txt chứa private key trong thư mục tạm
        File tempFile = File.createTempFile("private_key_for_ID_"+account.getId(), ".txt");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(privateKey);
        }

        // Gửi email kèm file đính kèm bất đồng bộ
        sendEmailAsync(recipientEmail, emailTitle, emailContent, tempFile);

        // Trả về dữ liệu đã băm từ orderData
        return SHA.hashData(orderData);
    }

    private void sendEmailAsync(String to, String title, String content, File attachment) {
        new Thread(() -> {
            try {
                boolean emailSent = Email.sendEmailWithAttachment(to, title, content, attachment);
                if (emailSent) {
                    System.out.println("Gửi private key thành công.");
                } else {
                    System.out.println("Gửi private key thất bại.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Xóa tệp sau khi gửi email
                if (attachment.exists()) {
                    attachment.delete();
                }
            }
        }).start();
    }


    // Xử lý key cá nhân
    private String handlePersonalKey(Account account, String keyContent, String orderData, String formattedTime) throws Exception {
        PublicKey publicKey = Key.generatePublicKeyFromPrivateKey(Key.base64ToPrivateKey(keyContent));
        String publicKeyString = Key.publicKeyToBase64(publicKey);

        KeyDao.insertPublicKey(account.getId(), publicKeyString, formattedTime, null);


        return SHA.hashData(orderData);
    }

    private String handleExternalKey(Account account, String keyContent, String orderData, String formattedTime) throws Exception {
        return SHA.hashData(orderData);
    }


    // Gửi phản hồi thành công
    private void sendSuccessResponse(HttpServletResponse response, String message, String hashData) throws IOException {
        response.getWriter().write(String.format(
                "{\"status\":\"success\", \"message\":\"%s\", \"hashData\":\"%s\"}", message, hashData));
    }

    // Gửi phản hồi lỗi
    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        response.getWriter().write(String.format(
                "{\"status\":\"error\", \"message\":\"%s\"}", errorMessage));
    }
}