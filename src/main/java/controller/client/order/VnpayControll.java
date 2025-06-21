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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "VnpayControll", value = "/VnpayControll")
public class VnpayControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
                System.out.println(fieldName + ": " + fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        String signData = VNPayConfig.hashAllFields(fields);
        String calculatedHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, signData);

        String vnp_TxnRef = request.getParameter("vnp_TxnRef");
        System.out.println(vnp_TxnRef);
        Order order = OrderDAO.getOrderByVn_TxnRef(Long.parseLong(vnp_TxnRef));

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        List<OrderDetail> orderDetailList = (List<OrderDetail>) session.getAttribute("billDetail");

        if (calculatedHash.equals(vnp_SecureHash)) {
            String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
            if ("00".equals(vnp_ResponseCode)) {
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
            }else{
                if (account != null) {
                    Cart.deleteCartToCookies(request, response, account.getId());
                    session.setAttribute("size", 0);
                }
                request.setAttribute("paymentError", "Thanh toán không thành công hoặc bị huỷ!");
                request.getRequestDispatcher("/WEB-INF/client/payment.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("paymentError", "Thanh toán không thành công hoặc bị huỷ!");
            request.getRequestDispatcher("/WEB-INF/client/payment.jsp").forward(request, response);
        }
    }
}