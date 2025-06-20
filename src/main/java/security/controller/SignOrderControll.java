package security.controller;

import com.google.gson.JsonObject;
import dao.client.VerifyDAO;
import model.Account;
import model.Order;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "SignOrderControll", value = "/SignOrderControll")
public class SignOrderControll extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        Order order = (Order) session.getAttribute("bill");

        if (account == null || order == null) {
            response.sendRedirect(request.getContextPath() + "/LoginControll");
            return;
        } else {
            String signData = request.getParameter("signData");
            System.out.println("sign data: " + signData);
            int res;
            String formattedTime;
            if (signData != null) {
                formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                res = VerifyDAO.insertOrderSignature(order.getId(), account.getId(), signData, formattedTime);
                System.out.println(res);
                if (res != 0) {
                    // Sau khi lưu thành công, chuyển hướng tới CheckOutSuccessControll
                    response.sendRedirect(request.getContextPath() + "/CheckOutSuccessControll");
                    return;
                } else {
                    // Nếu lưu thất bại, trả về lỗi
                    sendErrorResponse(response, "Insert Order Signature Failed");
                }
            } else {
                sendErrorResponse(response, "Insert Order Signature Failed");
            }
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.format(
                "{\"status\":\"error\", \"message\":\"%s\"}", errorMessage));
    }
}
