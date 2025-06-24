package controller.client.order;

import dao.client.OrderDAO;
import dao.client.VerifyDAO;
import model.*;
import security.sign.SHA;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "MoMoReturnServlet", value = "/momo-return")
public class MoMoReturnServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderId = request.getParameter("orderId");
        String resultCode = request.getParameter("resultCode");
        String requestId = request.getParameter("requestId");
//         Kiểm tra null hoặc không phải số
        if (requestId == null || !requestId.matches("\\d+")) {
            request.setAttribute("paymentError", "Giao dịch bị huỷ hoặc không hợp lệ!");
            request.getRequestDispatcher("/WEB-INF/client/payment.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        List<OrderDetail> orderDetailList = (List<OrderDetail>) session.getAttribute("billDetail");

        Order order = OrderDAO.getOrderByVn_TxnRef(Long.valueOf(requestId)); // Bạn cần thêm hàm này trong OrderDAO

        if ("0".equals(resultCode)) {
            OrderDAO.updateOrderStatus(order.getId(), "Đơn hàng đã được xác nhận và chờ đóng gói");
            OrderDAO.setCurrentIdBill(order);

            for (OrderDetail od : orderDetailList) {
                od.setOrder(order);
                OrderDAO.insertOrderdetail(od);
            }

            if (account != null) {
                Cart.deleteCartToCookies(request, response, account.getId());
                session.setAttribute("size", 0);
            }

            try {
                String data = VerifyDAO.getOrderData(order.getId());
                if (data != null) {
                    String hashData = SHA.hashData(data);
                    String formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    VerifyDAO.insertHashOrder(order.getId(), account.getId(), hashData, formattedTime);
                }
            } catch (Exception e) {
                throw new RuntimeException("Lỗi khi hash đơn hàng: " + e.getMessage(), e);
            }

            response.sendRedirect(request.getContextPath() + "/CheckOutSuccessControll");
        } else {
            request.setAttribute("paymentError", "Thanh toán không thành công hoặc bị huỷ!");
            request.getRequestDispatcher("/WEB-INF/client/payment.jsp").forward(request, response);
        }
    }
}

