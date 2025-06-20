package controller.admin.user;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

//import model.GetNationalAddress;
import model.Log;
import dao.client.AccountDAO;
import com.google.gson.JsonObject;
import dao.client.LOG_LEVEL;
import dao.client.Logging;
import model.Account;

@WebServlet(name = "LockAccountControll", value = "/LockAccountControll")
public class LockAccountControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Chuyển hướng các yêu cầu GET tới phương thức doPost
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        // Lấy các tham số từ yêu cầu
        String userId = request.getParameter("userId");
        String action = request.getParameter("action"); // Hành động có thể là "lock" hoặc "unlock"
        String userAgent = request.getHeader("User-Agent");
        String sourceIp = request.getRemoteAddr();
//        String country = GetNationalAddress.getCountryFromIp(sourceIp);

        // Khởi tạo đối tượng JSON để lưu trạng thái của hành động
        JsonObject jsonResponse = new JsonObject();

        // Tạo log
        Log log = new Log();
        log.setUserAgent(userAgent);
        log.setSourceIP(sourceIp);
        log.setModule("controller/user/LockAccountControll");
//        log.setNational(country);

        // Kiểm tra xem các tham số userId và action có khác null không
        if (userId != null && action != null) {
            // Thực hiện hành động khóa hoặc mở khóa dựa trên tham số action
            boolean success = false;
            if (action.equals("lock")) {
                success = AccountDAO.lockAccount("id",userId); // Gọi phương thức DAO để khóa người dùng
                if(success==true){
                    //Ghi log
                    log.setAccount(account);
                    log.setActionType("LOCK ACCOUNT");
                    log.setLogContent("Locked account " +"{ID: "+userId+"}"+" successfully!");
                    log.setLogLevel(LOG_LEVEL.INFO);
                    Logging.login(log);
                }else{
                    //Ghi log
                    log.setAccount(account);
                    log.setActionType("LOCK ACCOUNT");
                    log.setLogContent("Locked account " +"{ID: "+userId+"}"+" faied!");
                    log.setLogLevel(LOG_LEVEL.INFO);
                    Logging.login(log);
                }

            } else if (action.equals("unlock")) {
                success = AccountDAO.unlockAccount("id",userId); // Gọi phương thức DAO để mở khóa người dùng
                if(success==true){
                    //Ghi log
                    log.setAccount(account);
                    log.setActionType("UNLOCK ACCOUNT");
                    log.setLogContent("Unlocked account " +"{ID: "+userId+"}"+" successfully!");
                    log.setLogLevel(LOG_LEVEL.INFO);
                    Logging.login(log);
                }else{
                    //Ghi log
                    log.setAccount(account);
                    log.setActionType("UNLOCK ACCOUNT");
                    log.setLogContent("Unlocked account " +"{ID: "+userId+"}"+" faied!");
                    log.setLogLevel(LOG_LEVEL.INFO);
                    Logging.login(log);
                }
            }
            // Đặt thông báo phản hồi dựa trên kết quả của hành động
            String message = success ? "Hành động thành công" : "Hành động thất bại";

            // Thêm trạng thái của hành động vào đối tượng JSON
            jsonResponse.addProperty("success", success);
            jsonResponse.addProperty("message", message);
        } else {
            // Nếu userId hoặc action là null, đặt thông báo lỗi vào đối tượng JSON
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Lỗi: Thiếu thông tin yêu cầu");
        }
        // Gửi đối tượng JSON như phản hồi cho yêu cầu AJAX
        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
    }
}
