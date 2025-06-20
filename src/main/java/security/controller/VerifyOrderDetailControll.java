package security.controller;

import dao.client.KeyDao;
import dao.client.OrderDAO;
import model.Account;
import model.Cart;
import model.Order;
import model.OrderDetail;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "VerifyOrderDetailControll", value = "/VerifyOrderDetailControll")
public class VerifyOrderDetailControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Đặt encoding và loại nội dung
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession();

        Account account = (Account) session.getAttribute("account");
        // Lấy orderId từ request
        String orderIdParam = request.getParameter("orderId");
        System.out.println(orderIdParam);
        if (orderIdParam == null || orderIdParam.trim().isEmpty()) {
            return;
        }

        int orderId = Integer.parseInt(orderIdParam);
        session.setAttribute("orderId", orderId);
        boolean hasPublicKey = KeyDao.hasPublicKey(account.getId());
        request.setAttribute("hasPublicKey", hasPublicKey);

        // Chuyển hướng người dùng tới trang sign.jsp
        request.getRequestDispatcher("/WEB-INF/client/verify-order-detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}