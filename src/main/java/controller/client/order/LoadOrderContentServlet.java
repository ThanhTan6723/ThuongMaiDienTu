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
            return;
        }

        String orderStatus = request.getParameter("status");
        if (orderStatus == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu trạng thái đơn hàng.");
            return;
        }

        List<OrderDetail> orderDetails;
        if ("all".equals(orderStatus)) {
            orderDetails = OrderDAO.getListOrder(account.getId());
        } else {
            orderDetails = OrderDAO.getOrderDetailsByStatus(orderStatus);
        }

        if (orderDetails == null || orderDetails.isEmpty()) {
            response.getWriter().write("[]");
            return;
        }

        List<OrderResponse> responseList = new ArrayList<>();

        for (OrderDetail orderDetail : orderDetails) {
            if (orderDetail == null || orderDetail.getOrder() == null) continue;

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
                    } else {
                        if (!storedHash.equals(currentHash)) {
                            verifyStatus = "Đơn hàng bị chỉnh sửa";
                            return;
                        } else {
                            if (!VerifyDAO.isOrderSigned(orderId)) {
                                verifyStatus = "Chưa xác thực";
                            } else {
                                boolean isVerified = VerifyDAO.verifyOrder(orderId);
                                if (isVerified) {
                                    verifyStatus = "Đã xác thực";
                                } else {
                                    verifyStatus = "Không chính chủ";
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                verifyStatus = "Lỗi khi xử lý đơn hàng";
            }

            // Thêm thông tin vào danh sách phản hồi
            responseList.add(new OrderResponse(orderDetail, verifyStatus));
        }

        String json = new Gson().toJson(responseList);
        response.getWriter().write(json);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
