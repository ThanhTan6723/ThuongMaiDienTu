package controller.admin.orders;

import com.google.gson.Gson;
import dao.client.OrderDAO;
import dao.client.VerifyDAO;
import model.Order;
import security.sign.SHA;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "WaitingShippingControll", value = "/WaitingShippingControll")
public class WaitingShippingControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        // Trạng thái đơn hàng cần truy vấn
        String status = "Đơn hàng đã được đóng gọi và chờ vận chuyển";
        List<Order> listOrders = OrderDAO.getAllOrders(status);

        // Danh sách chứa dữ liệu trả về
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (Order order : listOrders) {
            Map<String, Object> orderData = new HashMap<>();

            // Thông tin cơ bản của đơn hàng
            orderData.put("id", order.getId());
            orderData.put("account", Map.of(
                    "id", order.getAccount().getId(),
                    "name", order.getAccount().getName()
            ));
            orderData.put("bookingDate", order.getBookingDate());
            orderData.put("consigneeName", order.getConsigneeName());
            orderData.put("consigneePhone", order.getConsigneePhone());
            orderData.put("address", order.getAddress());
            orderData.put("totalMoney", order.getTotalMoney());
            orderData.put("ship", order.getShip());
            orderData.put("discountValue", order.getDiscountValue());
            orderData.put("orderNotes", order.getOrderNotes());
            orderData.put("status", status);
            orderData.put("payment", order.getPayment().getMethodName());
            orderData.put("total_product", OrderDAO.calculateTotalPrice(order.getId()));

            try {
                // Lấy dữ liệu hiện tại từ cơ sở dữ liệu để kiểm tra tình trạng xác thực của đơn hàng
                String storedHash = VerifyDAO.getHashDataOrder(order.getId());

                if (storedHash == null || storedHash.isEmpty()) {
                    orderData.put("orderStatus", "Chưa xác thực");
                } else {
                    // Hash dữ liệu hiện tại
                    String orderDataFromDB = VerifyDAO.getOrderData(order.getId());
                    String currentHash = SHA.hashData(orderDataFromDB);

                    if (!currentHash.equals(storedHash)) {
                        orderData.put("orderStatus", "Đơn hàng bị chỉnh sửa");
                    } else {
                        orderData.put("orderStatus", "Đã xác thực");
                    }
                }
            } catch (Exception e) {
                orderData.put("orderStatus", "Lỗi xử lý");
                System.err.println("Lỗi khi xử lý Order ID " + order.getId() + ": " + e.getMessage());
            }

            responseList.add(orderData);
        }

        // Chuyển danh sách thành JSON và trả về
        String json = new Gson().toJson(responseList);
        response.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
