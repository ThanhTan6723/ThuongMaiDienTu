package controller.admin.user;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.google.gson.Gson;
import dao.client.AccountDAO;
import dao.client.LOG_LEVEL;
import dao.client.Logging;
import model.Account;
//import model.GetNationalAddress;
import model.Log;

@WebServlet(name = "UpdateRoleControll", value = "/UpdateRoleControll")
public class UpdateRoleControll extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        String userAgent = request.getHeader("User-Agent");
        String sourceIp = request.getRemoteAddr();
        int userId = Integer.parseInt(request.getParameter("userId"));
        String role = request.getParameter("roles");
//        String country = GetNationalAddress.getCountryFromIp(sourceIp);

        // Tạo log
        Log log = new Log();
        log.setUserAgent(userAgent);
        log.setSourceIP(sourceIp);
        log.setModule("controller/user/LockAccountControll");
        log.setBeforeData(AccountDAO.getAccountById(Integer.parseInt(String.valueOf(userId))).getRole().getRoleName());
//        log.setNational(country);

        int roleId = getRoleId(role);
        int updateResult = AccountDAO.updateRole(userId, roleId);

        if (updateResult > 0) {
            //Ghi log
            log.setAccount(account);
            log.setActionType("UPDATE ROLE");
            log.setLogContent("Update account " +"{ID: "+userId+"}"+" successfully!");
            log.setLogLevel(LOG_LEVEL.INFO);
            log.setAffterData(role);
            Logging.login(log);
            response.getWriter().write(new Gson().toJson(new ResponseMessage(true, "Cập nhật vai trò thành công.")));
        } else {
            //Ghi log
            log.setAccount(account);
            log.setActionType("UPDATE ACCOUNT");
            log.setLogContent("Update account " +"{ID: "+userId+"}"+" faied!");
            log.setLogLevel(LOG_LEVEL.INFO);
            Logging.login(log);
            response.getWriter().write(new Gson().toJson(new ResponseMessage(false, "Cập nhật vai trò thất bại.")));
        }
    }

    private int getRoleId(String roleName) {
        switch (roleName) {
            case "Admin":
                return 1;
            case "Manager Account":
                return 2;
            case "Manager Order":
                return 3;
            case "Manager Warehouse":
                return 4;
            case "Manager Voucher":
                return 5;
            case "Manager Review":
                return 6;
            case "Customer":
                return 0;
            default:
                throw new IllegalStateException("Unexpected value: " + roleName);
        }
    }

    class ResponseMessage {
        private boolean success;
        private String message;

        public ResponseMessage(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}
