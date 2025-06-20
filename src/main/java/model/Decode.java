package model;

import java.util.Base64;

public class Decode {
    public static void main(String[] args) {
        String encodedString = "nK+K1RB80K2vJUKyh61tPzT0nH8=";
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);

        // In ra các byte đã được giải mã
        for (byte b : decodedBytes) {
            System.out.printf("%02x ", b);
        }

        // Nếu dữ liệu là chuỗi có thể đọc được
        String decodedString = new String(decodedBytes);
        System.out.println("Decoded string: " + decodedString);
    }
}