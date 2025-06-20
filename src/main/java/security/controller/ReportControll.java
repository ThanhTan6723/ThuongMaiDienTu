package security.controller;

import dao.client.AccountDAO;
import dao.client.JDBCUtil;
import model.Account;
import model.Email;
import security.key.Key;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.PrivateKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;

import static dao.client.KeyDao.getActivePublicKey;
import static dao.client.KeyDao.insertPublicKey;

@WebServlet(name = "ReportControll", value = "/ReportControll")
public class ReportControll extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy thông tin tài khoản từ session
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }
        int accountId = account.getId();

        // Gửi thông tin về view để hiển thị
        request.setAttribute("accountId", account.getId());
        request.getRequestDispatcher("/WEB-INF/client/report-key.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Thiết lập mã hóa và kiểu nội dung
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        int accountId = account.getId();

        // Nếu không có tài khoản trong session
        if (account == null) {
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Bạn cần đăng nhập trước.\"}");
            return;
        }
        String reportTime = LocalDateTime.now().toString();
//        String reportTime = request.getParameter("reportTime");
//
//        // Kiểm tra thông tin đầu vào
//        if (reportTime == null || reportTime.trim().isEmpty()) {
//            response.getWriter().write("{\"status\":\"error\", \"message\":\"Thời gian báo mất không được để trống.\"}");
//            return;
//        }



        try {
            // Gọi hàm xử lý báo mất key
            boolean result = reportKey(accountId, reportTime);

            if (result) {
                response.getWriter().write("{\"status\":\"success\", \"message\":\"Báo mất key thành công. Private key mới đã được gửi qua email.\"}");
            } else {
                response.getWriter().write("{\"status\":\"error\", \"message\":\"Không thể báo mất key. Vui lòng thử lại.\"}");
            }
        } catch (SQLException e) {
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Lỗi xử lý: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Lỗi không xác định: " + e.getMessage() + "\"}");
        }
    }


    public static boolean reportKey(int accountId, String reportTime) throws SQLException {
        try {
            if (getActivePublicKey(accountId) == null) {
                System.out.println("Không có public key nào đang hoạt động để báo mất.");
                return false;
            }

            // Cập nhật end_time của public key hiện tại
            try (Connection connection = JDBCUtil.getConnection()) {
                String sql = "UPDATE PublicKeyForUser  SET end_time = ? WHERE account_id = ? AND end_time IS NULL";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, reportTime);
                    pstmt.setInt(2, accountId);
                    int rowsUpdated = pstmt.executeUpdate();

                    if (rowsUpdated == 0) {
                        System.out.println("Không có key nào để báo mất.");
                        return false;
                    }
                }
            }

            // Tạo và lưu cặp key mới
            Key key = new Key();
            key.generateKey();

            String newPublicKey = Base64.getEncoder().encodeToString(key.getPublicKey().getEncoded());
            insertPublicKey(accountId, newPublicKey, reportTime, null);

            // Lưu private key vào file
            String privateKeyContent = Base64.getEncoder().encodeToString(key.getPrivateKey().getEncoded());
            File privateKeyFile = new File("private_key_" + accountId + ".txt");
            try (FileWriter writer = new FileWriter(privateKeyFile)) {
                writer.write(privateKeyContent);
            }

            // Gửi email bất đồng bộ
            Account userAccount = AccountDAO.getAccountById(accountId);
            if (userAccount != null) {
                String emailTitle = "[Golden Fields] Private Key Replacement Notification";
                String emailContent = "Kính gửi " + userAccount.getName() + ",\n\n"
                        + "Một khóa bí mật mới đã được tạo cho tài khoản của bạn. Vui lòng tìm khóa bí mật được đính kèm.\n\n"
                        + "Xin hãy đảm bảo giữ khóa này an toàn.\n\n"
                        + "Trân trọng,\nĐội ngũ Bảo mật của bạn";


                sendEmailAsync(userAccount.getEmail(), emailTitle, emailContent, privateKeyFile);
            }

            // Xóa file sau khi gửi email
//            if (privateKeyFile.exists()) {
//                privateKeyFile.delete();
//            }

            return true;

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            return false;
        }
    }

    private static void sendEmailAsync(String to, String title, String content, File attachment) {
        new Thread(() -> {
            try {
                boolean emailSent = Email.sendEmailWithAttachment(to, title, content, attachment);
                if (emailSent) {
                    System.out.println("Private key sent to " + to);
                    // Xóa file sau khi gửi email thành công
                    if (attachment.exists()) {
                        boolean deleted = attachment.delete();
                        if (deleted) {
                            System.out.println("Private key file deleted successfully.");
                        } else {
                            System.err.println("Failed to delete private key file.");
                        }
                    }
                } else {
                    System.err.println("Failed to send private key to " + to);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


}
