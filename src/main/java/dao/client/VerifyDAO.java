package dao.client;

import security.sign.RSA;

import java.security.PublicKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VerifyDAO {
//    private static Connection connection = dao.client.JDBCUtil.getConnection();


    public static int insertHashOrder(int orderId, int accountId, String hashData, String createTime) {
        int result = 0;
        try {
            Connection connection = dao.client.JDBCUtil.getConnection();
            String sql = "Insert into HashOrders(order_id, account_id, hash_data, created_at) values(?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, orderId);
            preparedStatement.setInt(2, accountId);
            preparedStatement.setString(3, hashData);
            preparedStatement.setString(4, createTime);
            result = preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static int insertOrderSignature(int orderId, int accountId, String signData, String createTime) {
        int result = 0;
        try {
            Connection connection = dao.client.JDBCUtil.getConnection();
            String sql = "Insert into OrderSignatures(order_id, account_id, digital_signature, created_at) values(?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, orderId);
            preparedStatement.setInt(2, accountId);
            preparedStatement.setString(3, signData);
            preparedStatement.setString(4, createTime);
            result = preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static String getOrderData(int orderId) throws Exception {
        String orderQuery = "SELECT id, booking_date, delivery_date, account_id, consignee_name, " +
                "consignee_phone, ship, discountValue, totalMoney, address, orderNotes, " +
                "payment_id, vn_TnxRef " +
                "FROM Orders WHERE id = ?";

        String detailQuery = "SELECT id, order_id, product_id, product_price, quantity, priceWithQuantity " +
                "FROM OrderDetails WHERE order_id = ?";

        try (Connection connection = dao.client.JDBCUtil.getConnection();
             PreparedStatement orderStmt = connection.prepareStatement(orderQuery);
             PreparedStatement detailStmt = connection.prepareStatement(detailQuery)) {

            // Lấy thông tin đơn hàng
            orderStmt.setInt(1, orderId);
            StringBuilder orderData = new StringBuilder();

            try (ResultSet orderRs = orderStmt.executeQuery()) {
                if (orderRs.next()) {
                    orderData.append("{")
                            .append("\"id\": ").append(orderRs.getInt("id")).append(", ")
                            .append("\"booking_date\": \"").append(orderRs.getTimestamp("booking_date")).append("\", ")
                            .append("\"delivery_date\": \"").append(orderRs.getTimestamp("delivery_date")).append("\", ")
                            .append("\"account_id\": ").append(orderRs.getInt("account_id")).append(", ")
                            .append("\"consignee_name\": \"").append(escapeJson(orderRs.getString("consignee_name"))).append("\", ")
                            .append("\"consignee_phone\": \"").append(escapeJson(orderRs.getString("consignee_phone"))).append("\", ")
                            .append("\"ship\": ").append(orderRs.getBigDecimal("ship")).append(", ")
                            .append("\"discountValue\": ").append(orderRs.getBigDecimal("discountValue")).append(", ")
                            .append("\"totalMoney\": ").append(orderRs.getBigDecimal("totalMoney")).append(", ")
                            .append("\"address\": \"").append(escapeJson(orderRs.getString("address"))).append("\", ")
                            .append("\"orderNotes\": \"").append(escapeJson(orderRs.getString("orderNotes"))).append("\", ")
                            .append("\"payment_id\": ").append(orderRs.getInt("payment_id")).append(", ")
                            .append("\"vn_TnxRef\": ").append(orderRs.getLong("vn_TnxRef")).append(", ")
                            .append("\"orderDetails\": [");

                    // Lấy thông tin chi tiết đơn hàng
                    detailStmt.setInt(1, orderId);
                    try (ResultSet detailRs = detailStmt.executeQuery()) {
                        boolean firstDetail = true;
                        while (detailRs.next()) {
                            if (!firstDetail) {
                                orderData.append(", ");
                            }
                            orderData.append("{")
                                    .append("\"id\": ").append(detailRs.getInt("id")).append(", ")
                                    .append("\"order_id\": ").append(detailRs.getInt("order_id")).append(", ")
                                    .append("\"product_id\": ").append(detailRs.getInt("product_id")).append(", ")
                                    .append("\"product_price\": ").append(detailRs.getBigDecimal("product_price")).append(", ")
                                    .append("\"quantity\": ").append(detailRs.getInt("quantity")).append(", ")
                                    .append("\"priceWithQuantity\": ").append(detailRs.getBigDecimal("priceWithQuantity"))
                                    .append("}");
                            firstDetail = false;
                        }
                    }

                    orderData.append("]}");
                    return orderData.toString();
                } else {
                    throw new Exception("Order not found for id: " + orderId);
                }
            }
        }
    }

    // Hàm phụ để escape chuỗi JSON
    private static String escapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\"", "\\\"")
                .replace("\\", "\\\\")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public static String getSignedData(int orderId) throws Exception {

        String query = "SELECT digital_signature FROM OrderSignatures WHERE order_id = ?";

        try (Connection connection = dao.client.JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Thiết lập giá trị cho tham số trong câu truy vấn
            preparedStatement.setInt(1, orderId);

            // Thực thi câu truy vấn và lấy kết quả
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Lấy chữ ký của đơn hàng từ cột "digital_signature"
                    return resultSet.getString("digital_signature");
                } else {
                    // Nếu không tìm thấy chữ ký cho orderId này
                    throw new Exception("No signature found for order id: " + orderId);
                }
            }
        } catch (SQLException e) {
            // Xử lý lỗi khi kết nối cơ sở dữ liệu hoặc thực thi truy vấn
            throw new Exception("Database error occurred while retrieving signature.", e);
        }
    }

    public static String getHashDataOrder(int orderId) throws Exception {
        String query = "SELECT hash_data FROM HashOrders WHERE order_id = ?";
        try (Connection connection = dao.client.JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, orderId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("hash_data");
                } else {
                    return null; // Không tìm thấy hash_data
                }
            }
        }
    }

    public static String getPublicKey(int accountId) throws Exception {

        String query = "SELECT public_key FROM PublicKeyForUser WHERE account_id = ? and end_time is null"; // Truy vấn lấy khóa công khai

        try (Connection connection = dao.client.JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, accountId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("public_key");
                } else {
                    throw new Exception("No public key found for account id: " + accountId);
                }
            }
        }
    }

    // Phương thức xác thực chữ ký số
    public static boolean verifyOrder(int orderId) throws Exception {
        // 1. Lấy dữ liệu đơn hàng
        String hashedOrderData = getHashDataOrder(orderId);
        if (hashedOrderData == null) {
            return false; // Nếu không lấy được hashedOrderData, trả về false
        }

        // 2. Lấy chữ ký số của đơn hàng
        String digitalSignature = getSignedData(orderId);
        if (digitalSignature == null) {
            return false; // Nếu không lấy được digitalSignature, trả về false
        }

        // 3. Lấy khóa công khai từ tài khoản liên quan đến đơn hàng
        int accountId = getOrderAccountId(orderId);
        String publicKeyString = getPublicKey(accountId);
        if (publicKeyString == null) {
            return false; // Nếu không lấy được publicKeyString, trả về false
        }

        // 4. Kiểm tra chữ ký số
        return verifySignature(hashedOrderData, digitalSignature, publicKeyString);
    }

    // Lấy account_id từ đơn hàng
    private static int getOrderAccountId(int orderId) throws Exception {
        String query = "SELECT account_id FROM Orders WHERE id = ?";
        try (Connection connection = dao.client.JDBCUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("account_id");
                } else {
                    throw new Exception("No account found for order id: " + orderId);
                }
            }
        }
    }


    // Phương thức xác minh chữ ký số
    public static boolean verifySignature(String data, String signature, String publicKeyString) {
        try {
            // 1. Tạo đối tượng RSA để sử dụng phương thức verify
            RSA rsa = new RSA();

            // 2. Chuyển đổi chuỗi publicKeyString thành đối tượng PublicKey
            PublicKey publicKey = rsa.loadPublicKeyFromString(publicKeyString);

            // 3. Xác minh chữ ký
            boolean isVerified = rsa.verify(data, signature, publicKey);

            // Trả về kết quả xác minh
            return isVerified;
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi trong quá trình xác minh
        }
    }
    public static void updateOrderStatus(int orderId) {
        String sql = "UPDATE Orders SET orderStatus = ? WHERE id = ?";

        try (Connection connection = dao.client.JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set giá trị của orderStatus thành "Đơn hàng bị chỉnh sửa"
            preparedStatement.setString(1, "Đơn hàng bị chỉnh sửa");
            preparedStatement.setInt(2, orderId);

            // Thực thi câu lệnh SQL để cập nhật orderStatus
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            // Ghi log lỗi nếu có (hoặc in ra console)
            System.err.println("Lỗi khi cập nhật orderStatus của đơn hàng với ID " + orderId + ": " + e.getMessage());
            throw new RuntimeException("Lỗi khi cập nhật orderStatus của đơn hàng với ID " + orderId, e);
        }
    }
    public static boolean isOrderChanged(int orderId) {
        String query = "SELECT orderStatus FROM Orders WHERE id = ?";
        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, orderId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String status = resultSet.getString("orderStatus"); // Đảm bảo đúng tên cột
                    // Kiểm tra nếu trạng thái là "Đơn hàng bị chỉnh sửa"
                    return "Đơn hàng bị chỉnh sửa".equals(status);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isOrderSigned(int orderId) {
        String query = "SELECT COUNT(*) AS count FROM OrderSignatures WHERE order_id = ?";
        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, orderId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0; // Nếu có ít nhất một bản ghi, trả về true
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu xảy ra lỗi hoặc không có bản ghi nào
    }


    public static void main(String[] args) throws Exception {
//        System.out.println(VerifyDAO.getOrderData(4));
//        System.out.println(VerifyDAO.getSignedData(1));
//        System.out.println(VerifyDAO.getPublicKey(1));
 //       System.out.println(VerifyDAO.insertHashOrder(2, 1, "hashData", "2024-12-19"));
  //      updateOrderStatus(1);
//        System.out.println(isOrderChanged(1));
        System.out.println(VerifyDAO.verifyOrder(10));
    }
}