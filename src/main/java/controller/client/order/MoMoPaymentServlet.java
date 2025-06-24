package controller.client.order;

import dao.client.OrderDAO;
import model.MoMoPayment;
import model.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/MoMoPaymentServlet")
public class MoMoPaymentServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy dữ liệu đơn hàng
        Order order = (Order) request.getSession().getAttribute("bill");
        if (order == null) {
            response.sendRedirect("payment.jsp");
            return;
        }


        try {
            order.setVnp_TxnRef(Long.parseLong(String.valueOf(System.currentTimeMillis())));
            order.setOrderStatus("Đơn hàng đang chờ xác nhận");
            order.setPayment(OrderDAO.getPayment(4));
            OrderDAO.insertOrder(order);


            String redirectUrl = MoMoPayment.createPaymentUrl(order); // Gọi hàm tạo URL MoMo
            // Tạo order
//            String txnRef = String.valueOf(System.currentTimeMillis());


            response.sendRedirect(redirectUrl); // Redirect người dùng đến trang thanh toán MoMo
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
