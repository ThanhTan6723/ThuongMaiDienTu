package dao.client;

import security.key.Key;
import security.key.PublicKeyForUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Base64;

public class KeyDao {
    //Lưu public key vào database
    public static void insertPublicKey(int accountId, String publicKey, String createTime, String endTime) throws SQLException {
        try {
            Connection connection = JDBCUtil.getConnection();
            String sql = "INSERT INTO PublicKeyForUser(account_id,create_time,end_time,public_key) " + "VALUES(?,?,?,?)";
            PreparedStatement prs = connection.prepareStatement(sql);
            prs.setInt(1, accountId);
            prs.setString(2, createTime);
            prs.setString(3, endTime);
            prs.setString(4, publicKey);

            int res = prs.executeUpdate();
            System.out.println(res > 0 ? "Insert thành công" : "Insert không thành công");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static boolean hasPublicKey(int accountId) {
        boolean exists = false;
        try {
            Connection connection = JDBCUtil.getConnection();
            String sql = "SELECT COUNT(*) AS key_count FROM PublicKeyForUser WHERE account_id = ?";
            PreparedStatement prs = connection.prepareStatement(sql);
            prs.setInt(1, accountId);
            ResultSet rs = prs.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("key_count");
                exists = count > 0;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra public key: " + e.getMessage());
        }
        return exists;
    }


    public static PublicKeyForUser getPublicKey(int accountId) {
        PublicKeyForUser publicKeyForUser = null;
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKeyForUser;
    }

    public static boolean reportKey(int accountId, String reportTime) throws SQLException {
        boolean success = false;
        try {
            // Kiểm tra định dạng thời gian đầu vào
            try {
                LocalDateTime.parse(reportTime);
            } catch (DateTimeParseException e) {
                System.err.println("Định dạng thời gian không hợp lệ: " + reportTime);
                return false;
            }

            // Kiểm tra nếu có public key đang hoạt động
            if (getActivePublicKey(accountId) == null) {
                System.out.println("Không có public key nào đang hoạt động để báo mất.");
                return false;
            }

            // 1. Báo mất key hiện tại (cập nhật end_time của public key)
            Connection connection = JDBCUtil.getConnection();
            String sql = "UPDATE PublicKeyForUser SET end_time = ? WHERE account_id = ? AND end_time IS NULL";
            PreparedStatement prs = connection.prepareStatement(sql);
            prs.setString(1, reportTime);  // Thời điểm báo mất
            prs.setInt(2, accountId);

            int rowsUpdated = prs.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Key bị báo mất thành công. end_time được cập nhật: " + reportTime);
            } else {
                System.out.println("Không có key nào để báo mất.");
                return false;
            }

            // 2. Tạo cặp key mới cho người dùng
            Key key = new Key();
            key.generateKey();  // Tạo key mới với kích thước 2048 bit

            // 3. Lưu public key mới vào database
            String newPublicKey = Base64.getEncoder().encodeToString(key.getPublicKey().getEncoded());
            insertPublicKey(accountId, newPublicKey, reportTime, null);  // Lưu public key mới với createTime là thời điểm báo mất

            // 4. Lưu private key mới vào file
            String privateKeyPath = "D://private_key_" + accountId + ".pem";  // Đảm bảo lưu private key vào đúng vị trí
            key.savePrivateKeyToFile(privateKeyPath);  // Gọi phương thức savePrivateKeyToFile đã có trong lớp Key
            System.out.println("Cặp khóa mới đã được tạo và lưu vào database.");

            success = true;
        } catch (SQLException e) {
            System.err.println("Lỗi khi báo mất khóa: " + e.getMessage());
            throw new SQLException("Không thể cập nhật khóa. Lỗi: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Lỗi khi tạo hoặc lưu cặp khóa mới: " + e.getMessage());
        }

        return success;
    }


    public static PublicKeyForUser getActivePublicKey(int accountId) {
        PublicKeyForUser publicKeyForUser = null;
        try {
            Connection connection = JDBCUtil.getConnection();
            String sql = "SELECT * FROM PublicKeyForUser WHERE account_id = ? AND end_time IS NULL";
            PreparedStatement prs = connection.prepareStatement(sql);
            prs.setInt(1, accountId);
            ResultSet rs = prs.executeQuery();

            if (rs.next()) {
                publicKeyForUser = new PublicKeyForUser();
                publicKeyForUser.setId(rs.getInt("id"));
                publicKeyForUser.setAccount(AccountDAO.getAccountById(rs.getInt("account_id")));
                publicKeyForUser.setPublicKey(rs.getString("public_key"));
                publicKeyForUser.setCreateTime(rs.getString("create_time"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKeyForUser;
    }

    public static void main(String[] args) {
//        try {
//            // 1. Tạo đối tượng Key
//            Key key = new Key();
//
//            // 2. Tạo cặp khóa với độ dài 2048-bit
//            System.out.println("Dang tao cap khoa RSA...");
//            key.generateKey(2048);
//
//            // 3. Lưu PrivateKey vào file
//            String privateKeyPath = "D://private_key.pem";
//            key.savePrivateKeyToFile(privateKeyPath);
//            System.out.println("Private key da luu tai: " + privateKeyPath);
//
//            // 4. Lưu PublicKey vào database
//            int accountId = 13; // ID tài khoản của người dùng
//            String createTime = LocalDateTime.now().toString(); // Thời gian tạo khóa
//            String endTime = null; // null vì khóa này đang hoạt động
//
//            System.out.println("Dang luu publickey vao database...");
//            key.savePublicKeyToDatabase(accountId, createTime, endTime);
//            System.out.println("Public key da được lưu vào database.");
//
//            // 5. Lấy PublicKey từ database để kiểm tra
//            System.out.println("Đang lấy public key đang hoạt động từ database...");
//            var publicKeyFromDb = KeyDao.getActivePublicKey(accountId);
//            if (publicKeyFromDb != null) {
//                System.out.println("Public key đang hoạt động:");
//                System.out.println("ID: " + publicKeyFromDb.getId());
//                System.out.println("Account ID: " + publicKeyFromDb.getAccount_id());
//                System.out.println("Public Key: " + publicKeyFromDb.getPublicKey());
//                System.out.println("Create Time: " + publicKeyFromDb.getCreateTime());
//                System.out.println("End Time: " + publicKeyFromDb.getEndTime());
//            } else {
//                System.out.println("Không tìm thấy public key đang hoạt động.");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            // 1. Định nghĩa các tham số test
            int accountId = 13; // ID tài khoản người dùng
            String reportTime = LocalDateTime.now().toString(); // Thời điểm báo mất khóa (hiện tại)

            // 2. Kiểm tra trước khi báo mất (Public Key hiện tại)
            System.out.println("==> Public Key trước khi báo mất:");
            PublicKeyForUser activeKeyBefore = KeyDao.getActivePublicKey(accountId);
            if (activeKeyBefore != null) {
                System.out.println("Public Key ID: " + activeKeyBefore.getId());
                System.out.println("Public Key: " + activeKeyBefore.getPublicKey());
                System.out.println("Create Time: " + activeKeyBefore.getCreateTime());
                System.out.println("End Time: " + activeKeyBefore.getEndTime());
            } else {
                System.out.println("Không có public key nào đang hoạt động.");
            }

            // 3. Báo mất public key
            System.out.println("\n==> Báo mất public key...");
            boolean reportSuccess = KeyDao.reportKey(accountId, reportTime);
            if (reportSuccess) {
                System.out.println("Báo mất public key thành công!");
            } else {
                System.out.println("Không thể báo mất public key!");
                return;
            }

            // 4. Kiểm tra lại Public Key sau khi báo mất
            System.out.println("\n==> Public Key sau khi báo mất:");
            PublicKeyForUser activeKeyAfter = KeyDao.getActivePublicKey(accountId);
            if (activeKeyAfter != null) {
                System.out.println("Public Key ID: " + activeKeyAfter.getId());
                System.out.println("Public Key: " + activeKeyAfter.getPublicKey());
                System.out.println("Create Time: " + activeKeyAfter.getCreateTime());
                System.out.println("End Time: " + activeKeyAfter.getEndTime());
            } else {
                System.out.println("Không có public key nào đang hoạt động sau khi báo mất.");
            }

            // 5. Kiểm tra trong database xem có thêm một cặp khóa mới được tạo
            System.out.println("\n==> Kiểm tra cặp khóa mới trong database...");
            PublicKeyForUser newPublicKey = KeyDao.getActivePublicKey(accountId);
            if (newPublicKey != null && newPublicKey.getEndTime() == null) {
                System.out.println("Cặp khóa mới đã được tạo và lưu vào database.");
            } else {
                System.out.println("Không có cặp khóa mới được tạo sau khi báo mất.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

