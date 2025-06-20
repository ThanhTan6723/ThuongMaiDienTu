package controller.client.order;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class GHNCreateOrder {
    private static final String API_KEY = "88886949-1f43-11ef-a834-92c5c8e2c58a"; // Thay YOUR_API_KEY bằng API Key của bạn
    private static final String SHOP_ID = "5102207"; // Thay YOUR_SHOP_ID bằng ShopId của bạn

    public static void main(String[] args) {
        try {
            // URL endpoint của API GHN để tạo đơn hàng
            URL url = new URL("https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create");

            // Tạo kết nối HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Token", API_KEY);
            connection.setRequestProperty("ShopId", SHOP_ID);
            connection.setDoOutput(true);

            // Dữ liệu yêu cầu JSON đơn giản nhất
            String jsonInputString = "{"
                    + "\"payment_type_id\": 2,"
                    + "\"note\": \"Giao hàng nhanh\","
                    + "\"required_note\": \"KHONGCHOXEMHANG\","
                    + "\"from_name\": \"Nguyen Van A\","
                    + "\"from_phone\": \"0123456789\","
                    + "\"from_address\": \"123 Đường ABC, Phường 15, Quận 10, HCM\","
                    + "\"from_ward_name\": \"Phường 15\","
                    + "\"from_district_name\": \"Quận 10\","
                    + "\"from_province_name\": \"HCM\","
                    + "\"to_name\": \"Nguyen Van B\","
                    + "\"to_phone\": \"0987654321\","
                    + "\"to_address\": \"456 Đường XYZ, Phường 10, Quận 5, HCM\","
                    + "\"to_ward_code\": \"20000\","
                    + "\"to_district_id\": 1451,"
                    + "\"cod_amount\": 0,"
                    + "\"content\": \"Sách vở\","
                    + "\"weight\": 500,"
                    + "\"length\": 10,"
                    + "\"width\": 10,"
                    + "\"height\": 10,"
                    + "\"insurance_value\": 50000,"
                    + "\"service_type_id\": 2"
                    + "}";

            // Gửi dữ liệu yêu cầu
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Kiểm tra mã phản hồi (response code)
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Đọc dữ liệu phản hồi từ API
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // In ra kết quả
                System.out.println("Tạo đơn hàng thành công: " + response.toString());
            } else {
                // Đọc lỗi phản hồi từ API
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // In ra lỗi
                System.out.println("Yêu cầu thất bại: " + responseCode + " " + response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


