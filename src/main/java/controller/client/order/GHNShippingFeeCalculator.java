package controller.client.order;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GHNShippingFeeCalculator {
    private static final String API_KEY = "88886949-1f43-11ef-a834-92c5c8e2c58a"; // Thay YOUR_API_KEY bằng API Key của bạn

    public static void main(String[] args) {
        try {
            // URL endpoint của API GHN để tính phí vận chuyển
            URL url = new URL("https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee");

            // Tạo kết nối HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Token", API_KEY);
            connection.setDoOutput(true);

            // Dữ liệu yêu cầu JSON
            String jsonInputString = "{"
                    + "\"service_id\": 53320,"  // ID dịch vụ giao hàng
                    + "\"insurance_value\": 1000000,"  // Giá trị hàng hóa
                    + "\"to_ward_code\": \"20308\","  // Mã phường/xã đích đến
                    + "\"to_district_id\": 1447,"  // ID quận/huyện đích đến
                    + "\"from_district_id\": 1451,"  // ID quận/huyện gửi
                    + "\"weight\": 1000,"  // Khối lượng gói hàng (gram)
                    + "\"length\": 20,"  // Chiều dài gói hàng (cm)
                    + "\"width\": 20,"  // Chiều rộng gói hàng (cm)
                    + "\"height\": 20"  // Chiều cao gói hàng (cm)
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
                System.out.println("Phí vận chuyển: " + response.toString());
            } else {
                System.out.println("Yêu cầu thất bại: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

