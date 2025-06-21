package model;

import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MoMoPayment {
    public static String createPaymentUrl(Order order) throws Exception {
        String endpoint = "https://test-payment.momo.vn/v2/gateway/api/create";
        String partnerCode = "MOMO";
        String accessKey = "F8BBA842ECF85";
        String secretKey = "K951B6PE1waDMi640xX08PD3vg6EkVlz";

//        String orderId =  String.valueOf(order.getId());
        String orderId = UUID.randomUUID().toString();
        String requestId = String.valueOf(order.getVnp_TxnRef());
//        String requestId = UUID.randomUUID().toString();
        String amount = String.valueOf((int) order.getTotalMoney());
        String orderInfo = "Thanh toan don hang " + order.getId();
        String returnUrl = "http://localhost:8080/momo-return";
        String notifyUrl = "http://localhost:8080/momo-notify";

        String rawHash = "accessKey=" + accessKey +
                "&amount=" + amount +
                "&extraData=" +
                "&ipnUrl=" + notifyUrl +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&partnerCode=" + partnerCode +
                "&redirectUrl=" + returnUrl +
                "&requestId=" + requestId +
                "&requestType=captureWallet";

        String signature = hmacSHA256(secretKey, rawHash);

        JSONObject json = new JSONObject();
        json.put("partnerCode", partnerCode);
        json.put("accessKey", accessKey);
        json.put("requestId", requestId);
        json.put("amount", amount);
        json.put("orderId", orderId);
        json.put("orderInfo", orderInfo);
        json.put("returnUrl", returnUrl);
        json.put("notifyUrl", notifyUrl);
        json.put("extraData", "");
        json.put("requestType", "captureWallet");
        json.put("signature", signature);
        json.put("redirectUrl", returnUrl);
        json.put("ipnUrl", notifyUrl);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("MoMo response body: " + response.body());

        if (!response.body().trim().startsWith("{")) {
            throw new RuntimeException("MoMo trả về HTML lỗi hoặc kết nối thất bại: " + response.body());
        }

        JSONObject responseBody = new JSONObject(response.body());
        if (responseBody.has("payUrl")) {
            return responseBody.getString("payUrl");
        } else {
            throw new RuntimeException("MoMo không trả về payUrl: " + responseBody.toString());
        }
    }

    private static String hmacSHA256(String key, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secretKey);
        byte[] hmacBytes = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : hmacBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
