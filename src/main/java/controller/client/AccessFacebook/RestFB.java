package controller.client.AccessFacebook;

import java.io.IOException;

import controller.client.AccessGoogle.Constants;
import model.Account;
import org.apache.http.client.ClientProtocolException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

public class RestFB {

    public static String getToken(String code) throws ClientProtocolException, IOException {
        String response = Request.Post(Constants.FACEBOOK_LINK_GET_TOKEN)
                .bodyForm(Form.form()
                        .add("client_id", Constants.FACEBOOK_APP_ID)
                        .add("client_secret", Constants.FACEBOOK_APP_SECRET)
                        .add("redirect_uri",Constants.FACEBOOK_REDIRECT_URL)
                        .add("code", code)
                        .build()).execute().returnContent().asString();
        JsonObject jsonObject = new Gson().fromJson(response,JsonObject.class);
        String accessToken = jsonObject.get("access_token").toString().replaceAll("\"","");
        return accessToken;
    }

    public static AccountForFb getUserInfo(String accessToken) throws IOException {
        String link = Constants.FACEBOOK_LINK_GET_USER_INFO + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();

        // Log the user info response for debugging
        System.out.println("User Info response: " + response);

        AccountForFb googlePojo = new Gson().fromJson(response, AccountForFb.class);
        System.out.println(googlePojo);
        return googlePojo;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(getToken("AQBa0RNnhe8qpXHvJss3_xwx2kUWgWqdYDDX2B9XVJ9dn-4XWwUSRfI0MHV32D2edPiKhiXsiP4i8GpMqnMaqqIQt-Cf6Ckn77fVq364eRIVAmeVTCQMaXnCm4B53fyImy7UOYvu0vx9eX5cAOaMJ05gIHdRVssVBI1AG4LildM8iw_0VM3nK4q0UDl735Thj1E7bOzgKAruwLAc5MUHwsZhZpKlZ4LkTuM9_3_v3YNnyXc6NdTeMaU2fQPfmEwAkIxGoBqvy-RFHhAuJ3nANQ9YqshU23YkE1JdCEa7m4FB7LHqJ7_sl-t4EnAWOO8ch6vr_8Q4vfEqxtrxGggKmhaQTWi-37buTOUP2MDt7GSFgNBbBSmsPxDsCERydzk009JkJ8ETxvyhEeDs70dxyXFFZzhBMQShXVW9f5BODnHbKA"));
    }
}