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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "HashOrderOPControll", value = "/HashOrderOPControll")
public class HashOrderOPControll extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");
            int orderIdParam = (int) session.getAttribute("orderId");

            if (account == null) {
                sendErrorResponse(response, "Bạn chưa đăng nhập.");
                return;
            }
            if (orderIdParam ==0) {
                sendErrorResponse(response, "orderId không hợp lệ.");
                return;
            }


            String keyType = request.getParameter("keyType");
            if (keyType == null) {
                sendErrorResponse(response, "Thiếu thông tin keyType.");
                return;
            }

            String orderData;
            try {
                orderData = VerifyDAO.getOrderData(orderIdParam);
            } catch (Exception e) {
                sendErrorResponse(response, "Lỗi khi lấy dữ liệu đơn hàng: " + e.getMessage());
                return;
            }

            String formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String hashData = null;

            if ("system".equals(keyType)) {
                // Sinh key mới, lưu public key, gửi private key qua email
                try {
                    hashData = handleSystemKey(account, orderData, formattedTime);
                } catch (Exception ex) {
                    sendErrorResponse(response, "Lỗi tạo và gửi key hệ thống: " + ex.getMessage());
                    return;
                }
            } else if ("personal".equals(keyType)) {
                // Đã có private key: chỉ hash đơn hàng, không tạo key mới, không lưu thêm public key
                try {
                    hashData = SHA.hashData(orderData);
                } catch (Exception ex) {
                    sendErrorResponse(response, "Lỗi hash đơn hàng: " + ex.getMessage());
                    return;
                }
            } else {
                sendErrorResponse(response, "Loại key không hợp lệ.");
                return;
            }

            try {
                VerifyDAO.insertHashOrder(orderIdParam, account.getId(), hashData, formattedTime);
            } catch (Exception ex) {
                sendErrorResponse(response, "Lỗi lưu hash vào DB: " + ex.getMessage());
                return;
            }

            sendSuccessResponse(response, "Hash đơn hàng thành công", hashData);

        } catch (Exception e) {
            sendErrorResponse(response, "Đã xảy ra lỗi ngoài dự kiến: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String handleSystemKey(Account account, String orderData, String formattedTime) throws Exception {
        Key key = new Key();
        key.generateKey();

        String publicKey = Key.publicKeyToBase64(key.getPublicKey());
        KeyDao.insertPublicKey(account.getId(), publicKey, formattedTime, null);

        String privateKey = Key.privateKeyToBase64(key.getPrivateKey());
        sendPrivateKeyEmail(account, privateKey);

        return SHA.hashData(orderData);
    }

    private void sendPrivateKeyEmail(Account account, String privateKey) throws IOException {
        File tempFile = File.createTempFile("private_key_for_ID_" + account.getId(), ".txt");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(privateKey);
        }

        String emailTitle = "[Handora] Private key for your account";
        String emailContent = String.format("Kính gửi %s,\n\nVui lòng tải file đính kèm chứa khóa riêng (private key) của bạn để tạo chữ ký điện tử cho đơn hàng. Hãy giữ nó an toàn và không chia sẻ với bất kỳ ai.\n\nTrân trọng.", account.getName());

        sendEmailAsync(account.getEmail(), emailTitle, emailContent, tempFile);
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
                System.out.println("Lỗi khi gửi email: " + e.getMessage());
                e.printStackTrace();
            } finally {
                if (attachment != null && attachment.exists()) {
                    attachment.delete();
                }
            }
        }).start();
    }

    private void sendSuccessResponse(HttpServletResponse response, String message, String hashData) throws IOException {
        String json = String.format(
                "{\"status\":\"success\", \"message\":\"%s\", \"hashData\":\"%s\"}",
                escapeJson(message), escapeJson(hashData));
        response.getWriter().write(json);
        System.out.println("SUCCESS: " + json);
    }

    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        String json = String.format(
                "{\"status\":\"error\", \"message\":\"%s\"}",
                escapeJson(errorMessage));
        response.getWriter().write(json);
        System.out.println("ERROR: " + json);
    }

    private String escapeJson(String s) {
        return s == null ? "" : s.replace("\"", "\\\"");
    }
}