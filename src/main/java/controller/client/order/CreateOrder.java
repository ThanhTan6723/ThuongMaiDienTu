package controller.client.order;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;

public class CreateOrder {

    public static void main(String[] args) {
        try {
            // API endpoint của GHTK để tạo đơn hàng
            String url = "https://services.giaohangtietkiem.vn/services/shipment/order/?ver=1.5";

            // API Token của bạn
            String token = "ee42b44d5c4e4824e2f7d0d1cc74af58d328ccee";

            // Dữ liệu đơn hàng dưới dạng JSON
            String jsonInputString = "{"
                    + "\"products\": [{"
                    + "\"name\": \"bút\","
                    + "\"weight\": 0.1,"
                    + "\"quantity\": 1,"
                    + "\"product_code\": 1241"
                    + "}, {"
                    + "\"name\": \"tẩy\","
                    + "\"weight\": 0.2,"
                    + "\"quantity\": 1,"
                    + "\"product_code\": 1254"
                    + "}],"
                    + "\"order\": {"
                    + "\"id\": \"a554\","
                    + "\"pick_name\": \"HCM-nội thành\","
                    + "\"pick_address\": \"590 CMT8 P.11\","
                    + "\"pick_province\": \"TP. Hồ Chí Minh\","
                    + "\"pick_district\": \"Quận 3\","
                    + "\"pick_ward\": \"Phường 1\","
                    + "\"pick_tel\": \"0865603890\","
                    + "\"tel\": \"0911222334\","
                    + "\"name\": \"GHTK - HCM - Noi Thanh\","
                    + "\"address\": \"123 nguyễn chí thanh\","
                    + "\"province\": \"TP. Hồ Chí Minh\","
                    + "\"district\": \"Quận 1\","
                    + "\"ward\": \"Phường Bến Nghé\","
                    + "\"hamlet\": \"Khác\","
                    + "\"is_freeship\": \"1\","
                    + "\"pick_date\": \"2024-07-15\","
                    + "\"pick_money\": 25000,"
                    + "\"note\": \"Khối lượng tính cước tối đa: 1.00 kg\","
                    + "\"value\": 150000, // Đảm bảo giá trị hàng hóa nằm trong khoảng cho phép"
                    + "\"transport\": \"road\","
                    + "\"pick_option\": \"cod\","
                    + "\"tags\": [1, 7],"
                    + "}"
                    + "}";

            // Tạo một client HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Tạo một request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
                    .header("Token", token)
                    .POST(BodyPublishers.ofString(jsonInputString))
                    .build();

            // Gửi request và nhận response
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            // In ra response
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
