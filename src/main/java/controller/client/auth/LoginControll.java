package controller.client.auth;

import model.*;
import dao.client.AccountDAO;
import dao.client.LOG_LEVEL;
import dao.client.Logging;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.json.JSONObject;

@WebServlet(name = "LoginControll", value = "/LoginControll")
public class LoginControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("identifier")) {
                    request.setAttribute("identifier", URLDecoder.decode(cookie.getValue(), "UTF-8"));
                }
                if (cookie.getName().equals("passW")) {
                    request.setAttribute("password", URLDecoder.decode(cookie.getValue(), "UTF-8"));
                }
            }
        }
        request.getRequestDispatcher("/WEB-INF/client/login.jsp").forward(request, response);
    }

    private String readResponseBody(HttpURLConnection connection) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String identifier = request.getParameter("identifier");
        String passWord = request.getParameter("password");
        String userAgent = request.getHeader("User-Agent");
//        String sourceIp = GetNationalAddress.getClientIp(request);
        // Lấy địa chỉ IP của người dùng
        String ipAddress = request.getRemoteAddr();
//        String country = GetNationalAddress.getCountryFromIp(sourceIp);
        // Gửi yêu cầu đến API FreeGeoIP
        URL url = new URL("https://geoip.svc.nvidia.com/json/" + ipAddress);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Xử lý phản hồi từ API
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            String responseBody = readResponseBody(connection);
            JSONObject jsonObject = new JSONObject(responseBody);
            String countryCode = jsonObject.getString("country_code");
            String countryName = jsonObject.getString("country_name");

            System.out.println("Quốc gia của bạn: " + countryName + " (" + countryCode + ")");
        } else {
            // Xử lý lỗi
            System.err.println("Lỗi khi kết nối với API FreeGeoIP");
        }


        boolean checkSpaceIdentifier = identifier.trim().isEmpty();
        boolean checkSpacePass = passWord.trim().isEmpty();
        boolean checkEmailExist = false, checkPhoneExist = false;

        if (identifier == null || checkSpaceIdentifier) {
            String errorIdenty = "Vui lòng nhập email hoặc số điện thoại";
            request.setAttribute("errorIdenty", errorIdenty);
            System.out.println(errorIdenty);
        }
        if (passWord == null || checkSpacePass) {
            String errorP = "Vui lòng nhập mật khẩu";
            request.setAttribute("errorP", errorP);
            request.setAttribute("identifier", identifier);
            System.out.println(errorP);
        }
        if (identifier != null && passWord != null && !checkSpaceIdentifier && !checkSpacePass) {
            String enpass = Encode.toSHA1(passWord);
            Account account = null;
            AccountDAO dao = new AccountDAO();
            if (isValidEmail(identifier)) {
                checkEmailExist = AccountDAO.checkFieldExists("email", identifier);
                if (checkEmailExist) {
                    if (AccountDAO.isAccountLocked("email", identifier)) {
                        String error = "Tài khoản của bạn đã bị khóa do đăng nhập sai quá nhiều lần. Vui lòng thử lại sau 15 phút.";
                        request.setAttribute("error", error);
                        request.setAttribute("identifier", identifier);
                        request.getRequestDispatcher("/WEB-INF/client/login.jsp").forward(request, response);
                        return;
                    }
                    account = dao.getAccountByField("email", identifier, enpass);
                }
            } else if (isValidPhone(identifier)) {
                checkPhoneExist = AccountDAO.checkFieldExists("phonenumber", identifier);
                if (checkPhoneExist) {
                    if (AccountDAO.isAccountLocked("phonenumber", identifier)) {
                        String error = "Tài khoản của bạn đã bị khóa do đăng nhập sai quá nhiều lần. Vui lòng thử lại sau 15 phút.";
                        request.setAttribute("error", error);
                        request.setAttribute("identifier", identifier);
                        request.getRequestDispatcher("/WEB-INF/client/login.jsp").forward(request, response);
                        return;
                    }
                    account = AccountDAO.getAccountByField("phonenumber", identifier, enpass);
                }
            }

            Log log = new Log();
            log.setSourceIP(ipAddress);
            log.setUserAgent(userAgent);
//            log.setNational(country);
            log.setActionType("LOGIN");
            log.setModule("/controller/auth/LoginControll");

            System.out.println(account);
            if (account != null) {
                // Đặt lại số lần đăng nhập thất bại
                AccountDAO.resetFailedAttempts(account.getId());

                log.setLogLevel(LOG_LEVEL.INFO);
                log.setLogContent("Login success");
                log.setAccount(account);
                Logging.login(log);

                // Tạo session cho người dùng
                HttpSession session = request.getSession();
                session.setAttribute("account", account);
                session.setMaxInactiveInterval(60 * 60);

                // Đọc giỏ hàng từ cookies
                Map<Integer, OrderDetail> cart = Cart.readCartFromCookies(request, account.getId());
                int sizeCart = cart.size();
                session.setAttribute("size", sizeCart);

                // Tạo cookies cho thông tin đăng nhập
                Cookie c1 = new Cookie("identifier", URLEncoder.encode(identifier, "UTF-8"));
                Cookie c2 = new Cookie("passW", URLEncoder.encode(passWord, "UTF-8"));
                c1.setMaxAge(60 * 60 * 24 * 30);
                c2.setMaxAge(60 * 60 * 24 * 30);
                response.addCookie(c1);
                response.addCookie(c2);

              if(account.getRole().getId()==0){
                  response.sendRedirect(request.getContextPath() + "IndexControll");
              }
              if(account.getRole().getId()==1){
                  response.sendRedirect(request.getContextPath() + "IndexAdminControll");
              }
            } else {
                if (checkEmailExist || checkPhoneExist) {
                    String field = checkEmailExist ? "email" : "phonenumber";
                    String value = identifier;
                    int failedAttempts = AccountDAO.getFailedAttempts(field, value);

                    if (failedAttempts < 5) {
                        if (failedAttempts == 4) {
                            log.setLogLevel(LOG_LEVEL.WARNING);
                            log.setLogContent("Login limit is about to be reached");
                            Logging.login(log);

                            AccountDAO.incrementFailedAttempts(field, value);
                            AccountDAO.lockAccount(field, value);
                            String error = "Tài khoản của bạn đã bị khóa do đăng nhập sai quá nhiều lần. Vui lòng thử lại sau 15 phút.";
                            request.setAttribute("error", error);
                            request.setAttribute("identifier", identifier);
                            request.getRequestDispatcher("/WEB-INF/client/login.jsp").forward(request, response);
                        } else {
                            AccountDAO.incrementFailedAttempts(field, value);
                            String error = "Tài khoản hoặc mật khẩu không đúng.";
                            request.setAttribute("error", error);
                            request.setAttribute("identifier", identifier);
                            request.getRequestDispatcher("/WEB-INF/client/login.jsp").forward(request, response);
                        }
                    } else {
                        log.setLogLevel(LOG_LEVEL.DANGER);
                        log.setLogContent("Account is locked due to too many failed login attempts");
                        Logging.login(log);

                        String error = "Tài khoản của bạn đã bị khóa do đăng nhập sai quá nhiều lần. Vui lòng thử lại sau 15 phút.";
                        request.setAttribute("error", error);
                        request.setAttribute("identifier", identifier);
                        request.getRequestDispatcher("/WEB-INF/client/login.jsp").forward(request, response);
                    }
                } else {
                    String error = "Tài khoản hoặc mật khẩu không đúng.";
                    request.setAttribute("error", error);
                    request.setAttribute("identifier", identifier);
                    request.getRequestDispatcher("/WEB-INF/client/login.jsp").forward(request, response);
                }
            }
        } else {
            request.getRequestDispatcher("/WEB-INF/client/login.jsp").forward(request, response);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPhone(String phone) {
        String phoneRegex = "^(\\+84|0)\\d{9,10}$";
        return phone.matches(phoneRegex);
    }
}
