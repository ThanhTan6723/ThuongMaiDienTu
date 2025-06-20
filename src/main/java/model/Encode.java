package model;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;

public class Encode {
    // md5
    // sha-1
    public static String toSHA1(String input) {
        String salt = "asjrlkmcoewj@tjle;oxqskjhdjksjf1jurVn";
        String result = null;

        input = input + salt;
        try {
            byte[] dataBytes = input.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            result = Base64.encodeBase64String(md.digest(dataBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(toSHA1("06072003"));
    }
}
