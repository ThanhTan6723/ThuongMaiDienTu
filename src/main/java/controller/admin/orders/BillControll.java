package controller.admin.orders;

import com.google.gson.Gson;
import dao.client.OrderDAO;
import dao.client.VerifyDAO;
import model.Account;
import model.Email;
import model.Order;
import model.OrderDetail;
import security.sign.SHA;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/BillControll")
public class BillControll extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            List<Order> listOrders = OrderDAO.getAllOrder();
            List<Map<String, Object>> responseList = new ArrayList<>();

            for (Order order : listOrders) {
                Map<String, Object> orderData = new HashMap<>();
                orderData.put("id", order.getId());
                orderData.put("account", Map.of(
                        "id", order.getAccount().getId(),
                        "name", order.getAccount().getName()
                ));
                orderData.put("bookingDate", order.getBookingDate());
                orderData.put("consigneeName", order.getConsigneeName());
                orderData.put("consigneePhone", order.getConsigneePhone());
                orderData.put("address", order.getAddress());
                orderData.put("total_product", OrderDAO.calculateTotalPrice(order.getId()));
                orderData.put("totalMoney", order.getTotalMoney());
                orderData.put("ship", order.getShip());
                orderData.put("payment", order.getPayment().getMethodName());
                orderData.put("discountValue", order.getDiscountValue());
                orderData.put("orderNotes", order.getOrderNotes());
                orderData.put("status", order.getOrderStatus());

                String orderStatus = determineOrderStatus(order);
                orderData.put("orderStatus", orderStatus);

                responseList.add(orderData);
            }

            String json = new Gson().toJson(responseList);
            response.getWriter().write(json);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Server error: " + e.getMessage());
            response.getWriter().write(new Gson().toJson(error));
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // Delegate POST to GET for simplicity
    }

    private String determineOrderStatus(Order order) {
        try {
            String status = order.getOrderStatus();
            int orderId = order.getId();

            // If order is pending confirmation or not signed, verify further
            if ("Đơn hàng đang chờ xác nhận".equals(status) || !VerifyDAO.isOrderSigned(orderId)) {
                // Check if order was changed
                if (VerifyDAO.isOrderChanged(orderId)) {
                    sendOrderChangedEmail(order);
                    return "Đơn hàng bị chỉnh sửa";
                }

                // Retrieve stored hash for the order
                String storedHash = VerifyDAO.getHashDataOrder(orderId);
                if (storedHash == null || storedHash.isEmpty()) {
                    return "Chưa xác thực";
                }

                // Compare current order data hash with stored hash
                String orderDataFromDB = VerifyDAO.getOrderData(orderId);
                if (orderDataFromDB == null) {
                    return "Lỗi dữ liệu";
                }

                String currentHash = SHA.hashData(orderDataFromDB);
                if (!currentHash.equals(storedHash)) {
                    VerifyDAO.updateOrderStatus(orderId); // Mark as changed
                    sendOrderChangedEmail(order);
                    return "Đơn hàng bị chỉnh sửa";
                }

                // Verify if the order is digitally signed
                boolean isVerified = VerifyDAO.verifyOrder(orderId);
                return isVerified ? "Đã xác thực" : "Chưa xác thực";
            }

            // Default case for orders not in pending status
            return "Đã xác thực";
        } catch (Exception e) {
            System.err.println("Error processing Order ID " + order.getId() + ": " + e.getMessage());
            return "Lỗi xử lý";
        }
    }

    private void sendOrderChangedEmail(Order order) {
        Account account = order.getAccount();
        if (account == null || account.getEmail() == null) {
            System.err.println("Cannot send email: Account or email is null for Order ID " + order.getId());
            return;
        }

        String recipientEmail = account.getEmail();
        String emailTitle = "[Handora] Thông báo thay đổi đơn hàng";
        String emailContent = String.format(
                "Kính gửi %s,\n\nĐơn hàng của bạn với mã số %d đã bị thay đổi. " +
                        "Vui lòng kiểm tra lại chi tiết đơn hàng trên hệ thống của chúng tôi.\n\nTrân trọng.",
                account.getName(), order.getId()
        );

        new Thread(() -> {
            try {
                boolean emailSent = Email.sendEmail(recipientEmail, emailTitle, emailContent);
                System.out.println(emailSent
                        ? "Successfully sent order change notification for Order ID " + order.getId()
                        : "Failed to send order change notification for Order ID " + order.getId());
            } catch (Exception e) {
                System.err.println("Error sending email for Order ID " + order.getId() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
}