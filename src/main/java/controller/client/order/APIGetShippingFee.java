package controller.client.order;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import org.json.JSONObject;

public class APIGetShippingFee {
    private static final String API_URL = "https://services.giaohangtietkiem.vn/services/shipment/fee";
    private static final String TOKEN = "ee42b44d5c4e4824e2f7d0d1cc74af58d328ccee";
    private static final String SHOP_PROVINCE = "Hồ Chí Minh";
    private static final String SHOP_DISTRICT = "Thủ Đức";
    private static final String SHOP_WARD = "Phường Linh Trung";
    private static final String SHOP_STREET = "Khu phố 6";

    public static double calculateShippingFee(String city, String district, String ward, String address,
                                              double weight, double value) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        JSONObject requestBodyJson = new JSONObject();
        requestBodyJson.put("pick_province", SHOP_PROVINCE);
        requestBodyJson.put("pick_district", SHOP_DISTRICT);
        requestBodyJson.put("pick_ward", SHOP_WARD);
        requestBodyJson.put("pick_street", SHOP_STREET);
        requestBodyJson.put("province", city);
        requestBodyJson.put("district", district);
        requestBodyJson.put("ward", ward);
        requestBodyJson.put("address", address);
        requestBodyJson.put("weight", weight);
        requestBodyJson.put("value", value);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(API_URL))
                .header("Token", TOKEN)
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(requestBodyJson.toString()))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String responseBody = response.body();
            JSONObject jsonResponse = new JSONObject(responseBody);
            System.out.println(jsonResponse);
            return jsonResponse.getJSONObject("fee").getJSONObject("options").getDouble("shipMoney");
        } else {
            throw new RuntimeException("Request failed with status code: " + response.statusCode() + "\nResponse body: " + response.body());
        }
    }

    public static void main(String[] args) {
        try {
            double shipMoney = calculateShippingFee("Bến Tre", "Huyện Ba Tri", "Xã An Ngãi Trung",
                    "", 8000, 250000);
            System.out.println("Ship Money: " + shipMoney);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
