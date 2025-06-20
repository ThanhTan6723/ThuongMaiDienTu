package controller.admin.orders;

import com.google.gson.Gson;
import dao.client.OrderDAO;
import dao.client.VerifyDAO;
import model.Account;
import model.Email;
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

@WebServlet(name = "WaitingAcceptControll", value = "/WaitingAcceptControll")
public class WaitingAcceptControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String status = "Đơn hàng đang chờ xác nhận";
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

            try {
                // Kiểm tra nếu đơn hàng đã ở trạng thái "Đơn hàng bị chỉnh sửa"
                if (VerifyDAO.isOrderChanged(order.getId())) {
                    orderData.put("orderStatus", "Đơn hàng bị chỉnh sửa");
                } else {
                    // Lấy dữ liệu hiện tại từ cơ sở dữ liệu
                    String storedHash = VerifyDAO.getHashDataOrder(order.getId());
                    if (storedHash == null || storedHash.isEmpty()) {
                        orderData.put("orderStatus", "Chưa xác thực");
                    } else {
                        String orderDataFromDB = VerifyDAO.getOrderData(order.getId());
                        String currentHash = SHA.hashData(orderDataFromDB);

                        if (!currentHash.equals(storedHash)) {
                            // Gửi mail nếu trạng thái vừa thay đổi
                            sendOrderChangedEmail(order);
                            orderData.put("orderStatus", "Đơn hàng bị chỉnh sửa");
                            VerifyDAO.updateOrderStatus(order.getId());
                        } else {
                            boolean isVerified = VerifyDAO.verifyOrder(order.getId());
                            if (isVerified) {
                                orderData.put("orderStatus", "Đã xác thực");
                            } else {
                                orderData.put("orderStatus", "Chưa xác thực");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // Xử lý lỗi và thiết lập trạng thái "Lỗi xử lý"
                orderData.put("orderStatus", "Lỗi xử lý");
                System.err.println("Lỗi khi xử lý Order ID " + order.getId() + ": " + e.getMessage());
            }

            responseList.add(orderData);
        }

        // Chuyển danh sách thành JSON và trả về
        String json = new Gson().toJson(responseList);
        response.getWriter().write(json);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void sendOrderChangedEmail(Order order) {
        Account account = order.getAccount();
        String recipientEmail = account.getEmail();
        System.out.println("gửi về email tên : "+recipientEmail);
        String emailTitle = "[Golden Fields] Thông báo thay đổi đơn hàng";
        String emailContent = "Kính gửi " + account.getName() + ",\n\n"
                + "Đơn hàng của bạn với mã số " + order.getId() + " đã bị thay đổi. "
                + "Vui lòng kiểm tra lại chi tiết đơn hàng trên hệ thống của chúng tôi.\n\nTrân trọng.";

        new Thread(() -> {
            try {
                boolean emailSent = Email.sendEmail(recipientEmail, emailTitle, emailContent);
                if (emailSent) {
                    System.out.println("Gửi thông báo thay đổi đơn hàng thành công.");
                } else {
                    System.out.println("Gửi thông báo thay đổi đơn hàng thất bại.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}