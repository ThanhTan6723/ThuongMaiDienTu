package model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

public class VNPayConfig {
    public static final String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static final String vnp_ReturnUrl = "http://localhost:8080/VnpayControll";
    public static final String vnp_TmnCode = "P7TABG2A";
    public static final String vnp_HashSecret = "UL7JQX57FEZCV2965IMRJ42XPCFZ4F6P";
    public static final String vnp_ApiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        if ("0:0:0:0:0:0:0:1".equals(ipAddress)) {
            ipAddress = "127.0.0.1";
        }
        return ipAddress;
    }

    // Tạo chuỗi hash theo chuẩn VNPay: sort key, nối bằng &, không encode, không có vnp_SecureHash/SecureHashType
    public static String hashAllFields(Map<String, String> fields) throws UnsupportedEncodingException {
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames); // Sắp xếp tham số theo thứ tự bảng chữ cái
        StringBuilder hashData = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);
            // Loại bỏ vnp_SecureHash và vnp_SecureHashType
            if ((fieldValue != null) && (fieldValue.length() > 0) &&
                    !fieldName.equals("vnp_SecureHash") && !fieldName.equals("vnp_SecureHashType")) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    hashData.append('&');
                }
            }
        }
        return hashData.toString();
    }

    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes(StandardCharsets.UTF_8);
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}