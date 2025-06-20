package controller.client.order;

import dao.client.OrderDAO;
import model.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "CancelOrderServlet", urlPatterns = {"/CancelOrderServlet"})
public class CancelOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET method is not supported.");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));

            Order order = OrderDAO.getOrderforOrderDetail(orderId);
            String currentStatus = order.getOrderStatus();
            String newStatus = "";

            if (currentStatus != null) {
                if (currentStatus.equals("Đơn hàng đang chờ xác nhận")) {
                    newStatus = "Đơn hàng đã được hủy";
                } else if (currentStatus.equals("Đơn hàng đã được xác nhận và chờ đóng gói")) {
                    newStatus = "Yêu cầu hủy";
                } else if (currentStatus.equals("Yêu cầu hủy")) {
                    newStatus = "Đơn hàng đã được xác nhận và chờ đóng gói";
                }

                if (!newStatus.isEmpty()) {
                    OrderDAO.updateOrderStatus(orderId, newStatus);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return;
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Không thể hủy đơn hàng với trạng thái hiện tại.");
                    return;
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đơn hàng.");
                return;
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID đơn hàng không hợp lệ.");
        }
    }
}
