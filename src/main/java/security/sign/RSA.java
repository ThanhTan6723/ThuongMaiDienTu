package security.sign;

import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSA {

    // Phương thức ký dữ liệu, nhận privateKey làm tham số
    public static String sign(String hashData, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey); // Khởi tạo với khóa riêng
        signature.update(hashData.getBytes()); // Cập nhật dữ liệu cần ký
        byte[] signedData = signature.sign(); // Ký dữ liệu
        return Base64.getEncoder().encodeToString(signedData); // Mã hóa chữ ký sang Base64
    }

    // Phương thức xác minh chữ ký, nhận publicKey từ DB
    public static boolean verify(String hashData, String signedData, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey); // Khởi tạo với khóa công khai
        signature.update(hashData.getBytes()); // Cập nhật dữ liệu gốc
        byte[] decodedSignedData = Base64.getDecoder().decode(signedData); // Giải mã chữ ký từ Base64
        return signature.verify(decodedSignedData); // Xác minh chữ ký
    }

    // Phương thức để chuyển khóa công khai từ Base64 string sang PublicKey
    public static PublicKey loadPublicKeyFromString(String base64PublicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64PublicKey); // Giải mã Base64
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes); // Tạo spec từ byte[]
        KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // Khởi tạo KeyFactory
        return keyFactory.generatePublic(spec); // Trả về PublicKey
    }

}
