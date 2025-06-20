package controller.client.order;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GHNApiExample {
    private static final String API_KEY = "88886949-1f43-11ef-a834-92c5c8e2c58a"; // Thay YOUR_API_KEY bằng API Key của bạn

    public static void main(String[] args) {
        try {
            // URL endpoint của API GHN
            URL url = new URL("https://online-gateway.ghn.vn/shiip/public-api/master-data/province");

            // Tạo kết nối HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Thiết lập headers
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Token", API_KEY);

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
                System.out.println("Danh sách các tỉnh thành: " + response.toString());
            } else {
                System.out.println("Yêu cầu thất bại: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

