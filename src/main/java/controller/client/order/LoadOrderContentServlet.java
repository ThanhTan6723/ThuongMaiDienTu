package controller.client.order;

import com.google.gson.Gson;
import dao.client.OrderDAO;
import dao.client.VerifyDAO;
import model.Account;
import model.OrderDetail;
import model.OrderResponse;
import security.sign.SHA;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;

@WebServlet("/LoadOrderContentServlet")
public class LoadOrderContentServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Bạn chưa đăng nhập.");
            System.err.println("ERROR: Chưa đăng nhập.");
            return;
        }

        String orderStatus = request.getParameter("status");
        if (orderStatus == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu trạng thái đơn hàng.");
            System.err.println("ERROR: Thiếu trạng thái đơn hàng.");
            return;
        }

        List<OrderDetail> orderDetails;
        try {
            if ("all".equals(orderStatus)) {
                orderDetails = OrderDAO.getListOrder(account.getId());
            } else {
                orderDetails = OrderDAO.getOrderDetailsByStatus(orderStatus);
            }
        } catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi truy vấn danh sách đơn hàng: " + ex.getMessage());
            ex.printStackTrace();
            return;
        }

        if (orderDetails == null || orderDetails.isEmpty()) {
            response.getWriter().write("[]");
            System.out.println("INFO: Không có đơn hàng nào phù hợp.");
            return;
        }

        List<OrderResponse> responseList = new ArrayList<>();

        for (OrderDetail orderDetail : orderDetails) {
            if (orderDetail == null || orderDetail.getOrder() == null) {
                System.err.println("WARNING: Đơn hàng null hoặc dữ liệu không hợp lệ.");
                continue;
            }

            int orderId = orderDetail.getOrder().getId();
            String verifyStatus = null;

            try {
                String orderDataFromDB = VerifyDAO.getOrderData(orderId);
                String currentHash = SHA.hashData(orderDataFromDB);
                String storedHash = VerifyDAO.getHashDataOrder(orderId);
                if (storedHash == null || storedHash.isEmpty()) {
                    verifyStatus = "Chưa xác thực";
                } else {
                    if (VerifyDAO.isOrderChanged(orderId)) {
                        verifyStatus = "Đơn hàng bị chỉnh sửa";
                        System.err.println("INFO: Đơn hàng " + orderId + " bị chỉnh sửa (isOrderChanged=true)");
                    } else {
                        if (!storedHash.equals(currentHash)) {
                            verifyStatus = "Đơn hàng bị chỉnh sửa";
                            System.err.println("ERROR: Hash không khớp cho orderId=" + orderId
                                    + ". storedHash=" + storedHash + ", currentHash=" + currentHash);
                            // Không return ở đây, tiếp tục xử lý các đơn khác
                        } else {
                            if (!VerifyDAO.isOrderSigned(orderId)) {
                                verifyStatus = "Chưa xác thực";
                                System.out.println("INFO: Đơn hàng " + orderId + " chưa xác thực (chưa có chữ ký).");
                            } else {
                                boolean isVerified = VerifyDAO.verifyOrder(orderId);
                                if (isVerified) {
                                    verifyStatus = "Đã xác thực";
                                } else {
                                    verifyStatus = "Không chính chủ";
                                    System.err.println("ERROR: Đơn hàng " + orderId + " không chính chủ (verifyOrder=false)");
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                verifyStatus = "Lỗi khi xử lý đơn hàng";
                System.err.println("ERROR: Lỗi khi xử lý đơn hàng " + orderId + ": " + e.getMessage());
                e.printStackTrace();
            }

            // Thêm thông tin vào danh sách phản hồi
            responseList.add(new OrderResponse(orderDetail, verifyStatus));
        }

        String json = new Gson().toJson(responseList);
        System.out.println("KẾT QUẢ JSON: " + json);
        response.getWriter().write(json);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}