package controller.client.auth;

import model.*;
import controller.client.AccessGoogle.GooglePojo;
import controller.client.AccessGoogle.GoogleUtils;
import dao.client.AccountDAO;
import dao.client.LOG_LEVEL;
import dao.client.Logging;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "login-google", value = "/login-google")
public class LoginGoogleControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        String userAgent = request.getHeader("User-Agent");
        String sourceIp = request.getRemoteAddr();
//        String country = GetNationalAddress.getCountryFromIp(sourceIp);

        // Tạo session cho người dùng
        HttpSession session = request.getSession();
        // Tạo log
        Log log = new Log();
        log.setUserAgent(userAgent);
        log.setSourceIP(sourceIp);
        log.setActionType("LOGIN GOOGLE");
        log.setModule("controller/auth/LoginGoogleControll");
//        log.setNational(country);

        if (code == null || code.isEmpty()) {
            request.getRequestDispatcher("/WEB-INF/client/login.jsp").forward(request, response);
        } else {
            String accessToken = GoogleUtils.getToken(code);
            GooglePojo googlePojo = GoogleUtils.getUserInfo(accessToken);

            AccountDAO accountDAO = new AccountDAO();
            Account account = accountDAO.findByEmail(googlePojo.getEmail().trim());
            System.out.println(googlePojo.getEmail());
            System.out.println("Find acc: "+account);

            if (account == null) {
                // Tạo tài khoản mới
                account = new Account();
                account.setName(googlePojo.getName());
                account.setEmail(googlePojo.getEmail());
                // Mật khẩu để trống hoặc đặt một giá trị mặc định
                String password = Encode.toSHA1("login-google/"+ googlePojo.getId());
                account.setPassword(password);
                Role role = new Role();
                role.setId(1);
                account.setRole(role);
                System.out.println(account);
                accountDAO.insertAccount(account);
                account=AccountDAO.getAccountByEmail(googlePojo.getEmail());
                System.out.println(account);

                session.setAttribute("account", account);
                session.setMaxInactiveInterval(60 * 60);
            }else{
                if(!account.isLocked()) {
                    // Đọc giỏ hàng từ cookies
                    Map<Integer, OrderDetail> cart = Cart.readCartFromCookies(request,account.getId());
                    int sizeCart = cart.size();
                    session.setAttribute("size", sizeCart);

                    //Ghi log
                    log.setAccount(account);
                    log.setLogContent("Login success");
                    log.setLogLevel(LOG_LEVEL.INFO);
                    Logging.login(log);

                    System.out.println(account);
                    session.setAttribute("account", account);
                    session.setMaxInactiveInterval(60 * 60);
                }else{
                    //Ghi log
                    log.setAccount(account);
                    log.setLogContent("Login fail! Account is locked");
                    log.setLogLevel(LOG_LEVEL.INFO);
                    Logging.login(log);

                    String error = "Tài khoản của bạn đã bị khóa";
                    request.setAttribute("error", error);
                    request.getRequestDispatcher("/WEB-INF/client/login.jsp").forward(request, response);
                }
            }

//            // Ghi log đăng nhập
//            Logging.login(account);

           // Chuyển hướng tới trang chính
//            RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/index.jsp");
//            dis.forward(request, response);
            response.sendRedirect(request.getContextPath() + "/IndexControll");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}