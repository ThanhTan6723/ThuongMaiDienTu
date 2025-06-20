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

@WebServlet(name = "VerifyOrderControll", value = "/VerifyOrderControll")
public class VerifyOrderControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Đặt encoding và loại nội dung
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();

        Account account = (Account) session.getAttribute("account");
        Order order = (Order) session.getAttribute("bill");
        List<OrderDetail> orderDetail = (List<OrderDetail>) session.getAttribute("billDetail");
        String data = order.toString() + orderDetail.toString();
        boolean hasPublicKey = KeyDao.hasPublicKey(account.getId());
        request.setAttribute("hasPublicKey", hasPublicKey);

        if (order != null && orderDetail != null) {
            OrderDAO.insertOrder(order);
            OrderDAO.setCurrentIdBill(order);
            for (OrderDetail od : orderDetail) {
                od.setOrder(order);
                OrderDAO.insertOrderdetail(od);
            }
            Cart.deleteCartToCookies(request, response, account.getId());
            session.setAttribute("size", 0);
        }
        // Chuyển hướng người dùng tới trang sign.jsp
        request.getRequestDispatcher("/WEB-INF/client/verify-order.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}