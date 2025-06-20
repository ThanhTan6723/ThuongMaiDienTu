package controller.admin.orders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dao.client.OrderDAO;
import dao.client.VerifyDAO;
import model.Account;
import model.Email;
import model.Order;
import security.sign.SHA;

@WebServlet("/BillControll")
public class BillControll extends HttpServlet {
    private static final long serialVersionUID = 1L;

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
                orderData.put("totalMoney", order.getTotalMoney());
                orderData.put("ship", order.getShip());
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
            error.put("error", "Lỗi server: " + e.getMessage());
            response.getWriter().write(new Gson().toJson(error));
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private String determineOrderStatus(Order order) {
        try {
            String status = order.getOrderStatus();
            if (status.equals("Đơn hàng đang chờ xác nhận") || !VerifyDAO.isOrderSigned(order.getId())) {
                if (VerifyDAO.isOrderChanged(order.getId())) {
                    return "Đơn hàng bị chỉnh sửa";
                }
                String storedHash = VerifyDAO.getHashDataOrder(order.getId());
                if (storedHash == null || storedHash.isEmpty()) {
                    return "Chưa xác thực";
                }
                String orderDataFromDB = VerifyDAO.getOrderData(order.getId());
                String currentHash = SHA.hashData(orderDataFromDB);
                if (!currentHash.equals(storedHash)) {
                    sendOrderChangedEmail(order);
                    VerifyDAO.updateOrderStatus(order.getId());
                    return "Đơn hàng bị chỉnh sửa";
                }
                boolean isVerified = VerifyDAO.verifyOrder(order.getId());
                return isVerified ? "Đã xác thực" : "Chưa xác thực";
            }
            return "Đã xác thực";
        } catch (Exception e) {
            System.err.println("Lỗi xử lý Order ID " + order.getId() + ": " + e.getMessage());
            return "Lỗi xử lý";
        }
    }

    private void sendOrderChangedEmail(Order order) {
        Account account = order.getAccount();
        String recipientEmail = account.getEmail();
        String emailTitle = "[Golden Fields] Thông báo thay đổi đơn hàng";
        String emailContent = "Kính gửi " + account.getName() + ",\n\n"
                + "Đơn hàng của bạn với mã số " + order.getId() + " đã bị thay đổi. "
                + "Vui lòng kiểm tra lại chi tiết đơn hàng trên hệ thống của chúng tôi.\n\nTrân trọng.";

        new Thread(() -> {
            try {
                boolean emailSent = Email.sendEmail(recipientEmail, emailTitle, emailContent);
                System.out.println(emailSent ? "Gửi thông báo thay đổi đơn hàng thành công."
                        : "Gửi thông báo thay đổi đơn hàng thất bại.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}