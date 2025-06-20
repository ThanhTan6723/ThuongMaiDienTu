package security.key;

import dao.client.KeyDao;

import javax.crypto.Cipher;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.SQLException;
import java.util.Base64;

public class Key {
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public Key() {
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    // Tạo cặp khóa RSA với key size mặc định là 2048
    public void generateKey() throws NoSuchAlgorithmException {
        int keySize = 2048; // Key size mặc định là 2048 bit
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(keySize);  // Khởi tạo với key size mặc định
        KeyPair keyPair = keyGen.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    // Phát sinh public key từ private key
    public static PublicKey generatePublicKeyFromPrivateKey(PrivateKey privateKey) throws Exception {
        // Chuyển private key sang định dạng BigInteger để lấy các tham số RSA
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec rsaPublicKeySpec = keyFactory.getKeySpec(privateKey, RSAPublicKeySpec.class);

        // Lấy modulus và public exponent
        BigInteger modulus = rsaPublicKeySpec.getModulus();
        BigInteger publicExponent = rsaPublicKeySpec.getPublicExponent();

        // Tạo public key từ modulus và public exponent
        return keyFactory.generatePublic(new RSAPublicKeySpec(modulus, publicExponent));
    }

    // 2. Lưu private key xuống máy người dùng
    public void savePrivateKeyToFile(String filePath) throws IOException {
        if (privateKey == null) {
            throw new IllegalArgumentException("Private key là null. Không thể lưu vào tệp.");
        }

        Path file = Paths.get(filePath);
        if (Files.exists(file) && Files.isDirectory(file)) {
            throw new IllegalArgumentException("Đường dẫn cung cấp là thư mục, không phải tệp.");
        }
        if (!Files.isDirectory(file.getParent())) {
            throw new IllegalArgumentException("Thư mục chứa tệp không tồn tại.");
        }

        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        Files.createDirectories(file.getParent());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(privateKeyString);
            writer.newLine();
            System.out.println("Private key đã được lưu thành công vào tệp: " + filePath);
        } catch (IOException e) {
            throw new IOException("Lỗi khi lưu private key vào tệp: " + filePath, e);
        }
    }

    // 3. Lưu khóa công khai vào database
    public void savePublicKeyToDatabase(int accountId, String createTime, String endTime) throws SQLException {
        //Chuyển public key thành chuỗi Base64
        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        //Lưu public key vào db
        KeyDao.insertPublicKey(accountId, publicKeyString, createTime, endTime);
    }

    // 4. Tải private key từ tệp
    public void loadPrivateKeyFromFile(String filePath) throws Exception {
        // Kiểm tra đường dẫn tệp
        Path file = Paths.get(filePath);
        if (!Files.exists(file) || Files.isDirectory(file)) {
            throw new IllegalArgumentException("Tệp không tồn tại hoặc đường dẫn là thư mục.");
        }

        // Đọc nội dung từ tệp
        StringBuilder keyContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                keyContent.append(line);
            }
        } catch (IOException e) {
            throw new IOException("Lỗi khi đọc tệp private key: " + filePath, e);
        }

        // Giải mã chuỗi Base64
        byte[] keyBytes = Base64.getDecoder().decode(keyContent.toString());

        // Chuyển đổi thành đối tượng PrivateKey
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        privateKey = keyFactory.generatePrivate(keySpec);

        System.out.println("Private key đã được tải thành công từ tệp: " + filePath);
    }

    // Phương thức tải Public Key từ tệp
    public void loadPublicKeyFromFile(String filePath) throws Exception {
        // Kiểm tra đường dẫn tệp
        Path file = Paths.get(filePath);
        if (!Files.exists(file) || Files.isDirectory(file)) {
            throw new IllegalArgumentException("Tệp không tồn tại hoặc đường dẫn là thư mục.");
        }

        // Đọc nội dung từ tệp
        StringBuilder keyContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                keyContent.append(line);
            }
        } catch (IOException e) {
            throw new IOException("Lỗi khi đọc tệp public key: " + filePath, e);
        }

        // Giải mã chuỗi Base64
        byte[] keyBytes = Base64.getDecoder().decode(keyContent.toString());

        // Chuyển đổi thành đối tượng PublicKey
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        publicKey = keyFactory.generatePublic(keySpec);

        System.out.println("Public key đã được tải thành công từ tệp: " + filePath);
    }


    //Chuyển PublicKey thành chuỗi Base64
    public static String publicKeyToBase64(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }


    //Chuyển PrivateKey thành chuỗi Base64
    public static String privateKeyToBase64(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }


    //Chuyển chuỗi Base64 thành PublicKey
    public static PublicKey base64ToPublicKey(String base64Key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // Có thể thay đổi thuật toán nếu cần
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return keyFactory.generatePublic(keySpec);
    }


    //Chuyển chuỗi Base64 thành PrivateKey
    public static PrivateKey base64ToPrivateKey(String base64Key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // Có thể thay đổi thuật toán nếu cần
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        return keyFactory.generatePrivate(keySpec);
    }

    public static void main(String[] args) throws Exception {
        Key key = new Key();
        key.generateKey();
        System.out.println();

//         Lưu khóa riêng xuống file
        key.savePrivateKeyToFile("D://private_key.txt");

        // Tải private key từ tệp
        key.loadPrivateKeyFromFile("D://private_key.txt");
        // In ra private key vừa tải
        String privateKeyString = Base64.getEncoder().encodeToString(key.privateKey.getEncoded());
        System.out.println("Private Key (Base64): " + privateKeyString);

    }

}