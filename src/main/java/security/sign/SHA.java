package security.sign;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SHA {

    public static String hashData(String data) {
        try {
            // Tạo instance của MessageDigest với thuật toán SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Chuyển đổi dữ liệu thành mảng byte và tính hash
            byte[] encodedHash = digest.digest(data.getBytes(StandardCharsets.UTF_8));

            // Mã hóa kết quả hash thành chuỗi Base64 để dễ lưu trữ
            return Base64.getEncoder().encodeToString(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            // Nếu SHA-256 không được hỗ trợ, ném lỗi
            throw new RuntimeException("Lỗi: Thuật toán SHA-256 không khả dụng.", e);
        }
    }

    public static String hashFile(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }

            byte[] encodedHash = digest.digest();
            return Base64.getEncoder().encodeToString(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi: Thuật toán SHA-256 không khả dụng.", e);
        }
    }


    public static boolean verifyHash(String originalData, String hashedData) {
        // Hash lại dữ liệu gốc và so sánh với giá trị hash đã lưu
        String recalculatedHash = hashData(originalData);
        return recalculatedHash.equals(hashedData);
    }

    public static void main(String[] args) {
//        // Ví dụ sử dụng lớp SHA
//        String orderData = "OrderID:101|AccountID:456|Total:999.99|Date:2024-12-13";
//
//        // Tạo hash từ dữ liệu đơn hàng
//        String hashedData = SHA.hashData(orderData);
//        System.out.println("Hashed Data: " + hashedData);
//
//        // Kiểm tra hash
//        boolean isVerified = SHA.verifyHash(orderData, hashedData);
//        System.out.println("Is Verified: " + isVerified);

        // Hash tệp tin
//            File file = new File("example.txt"); // Thay bằng đường dẫn tệp của bạn
//            String fileHash = SHA.hashFile(file);
//            System.out.println("File Hash: " + fileHash);
        String txt = "{\"id\": 9, \"booking_date\": \"2024-12-23 13:51:50.0\", \"delivery_date\": \"null\", \"account_id\": 13, \"consignee_name\": \"Mai Th? S??ng\", \"consignee_phone\": \"0337673319\", \"ship\": 45000.000, \"discountValue\": 0.000, \"totalMoney\": 75000.000, \"address\": \"Ph??ng Linh Trung, Thành Ph? Th? ??c,Thành Ph? H? Chí Minh, Xã Long Châu, Huy?n Yên Phong, T?nh B?c Ninh\", \"orderNotes\": \"\", \"payment_id\": 2}";
        System.out.println(hashData(txt));

    }
}
