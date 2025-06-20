package controller.client.auth;

import model.Log;
import dao.client.LOG_LEVEL;
import dao.client.Logging;
import model.Account;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LogoutControll")
public class LogoutControll extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        session.invalidate();
        String userAgent = request.getHeader("User-Agent");
        String sourceIp = request.getRemoteAddr();

        // Tạo log
        Log log = new Log();
        log.setUserAgent(userAgent);
        log.setSourceIP(sourceIp);
        log.setActionType("LOGOUT");
        log.setModule("controller/auth/LogoutControll");
        log.setAccount(account);
        log.setLogContent("Logout success");
        log.setLogLevel(LOG_LEVEL.INFO);
        Logging.login(log);

        response.sendRedirect(request.getContextPath() + "/IndexControll");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

}
