package security.controller;

import dao.client.OrderDAO;
import dao.client.VerifyDAO;
import security.sign.RSA;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

import static dao.client.VerifyDAO.*;
import static security.sign.SHA.hashData;

@WebServlet(name = "CheckOrderControll", value = "/CheckOrderControll")
public class CheckOrderControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Không dùng GET để kiểm tra đơn hàng, chỉ dùng POST
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET method is not supported.");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Thiết lập mã hóa UTF-8 cho request và response
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        // Lấy tham số từ request
        String orderIdParam = request.getParameter("orderId");
        String accountIdParam = request.getParameter("accountId");

        if (orderIdParam == null || accountIdParam == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing orderId or accountId\"}");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdParam);
            int accountId = Integer.parseInt(accountIdParam);

            // Lấy thông tin từ cơ sở dữ liệu
            String orderData = getOrderData(orderId);
            String signedData = getSignedData(orderId);
            String publicKeyStr = getPublicKey(accountId);
            String savedHashData = getHashDataOrder(orderId); // Lấy hash_data đã lưu từ bảng HashOrders

            if (orderData == null || signedData == null || publicKeyStr == null || savedHashData == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Order, signature, public key, or hash not found.\"}");
                return;
            }

            // Hash dữ liệu orderData hiện tại
            String currentHashedOrderData = hashData(orderData);

            // So sánh hash hiện tại với hash đã lưu
            if (!currentHashedOrderData.equals(savedHashData)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Hash mismatch. Order data may have been tampered with.\"}");
                return;
            }

            // Chuyển publicKey từ String sang PublicKey
            RSA rsa = new RSA();
            boolean isVerified = rsa.verify(currentHashedOrderData, signedData, rsa.loadPublicKeyFromString(publicKeyStr));

            if (isVerified) {
                // hợp lệ
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"Order is valid.\"}");
            } else {
                // không hợp lệ
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Order verification failed.\"}");
            }

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid orderId or accountId format\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"An error occurred during verification.\"}");
        }
    }

}