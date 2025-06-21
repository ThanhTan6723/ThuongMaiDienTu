package controller.client.order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.client.OrderDAO;
import dao.client.VerifyDAO;
import model.Account;
import model.Order;
import model.OrderDetail;
import security.sign.SHA;

@WebServlet("/OrderPlaceControl")
public class OrderPlaceControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");


        List<OrderDetail> orderDetails = OrderDAO.getListOrder(account.getId());
//        List<String> verifies = new ArrayList<String>();
//
//        int currentOrderId = 0;
//        for (OrderDetail orderDetail : orderDetails) {
//            if (currentOrderId != orderDetail.getOrder().getId()) {
//                currentOrderId = orderDetail.getOrder().getId();
//            }
//            try {
//                // Lấy dữ liệu hiện tại và hash
//                String orderDataFromDB = VerifyDAO.getOrderData(currentOrderId);
//                String currentHash = SHA.hashData(orderDataFromDB);
//                System.out.println(currentHash);
//                // Lấy hash đã lưu từ cơ sở dữ liệu
//                String storedHash = VerifyDAO.getHashDataOrder(currentOrderId);
//
//                if (!currentHash.equals(storedHash)) {
//                    // Nếu dữ liệu bị thay đổi, trả về true
//                    verifies.add("Đơn hàng bị chỉnh sửa");
//                } else {
//                    // Nếu dữ liệu không bị thay đổi, kiểm tra xác thực
//                    boolean isVerified = VerifyDAO.verifyOrder(currentOrderId);
//                    System.out.println("verified " + isVerified);
//                    if (isVerified) {
//                        verifies.add("Đã xác thực");
//                    } else {
//                        verifies.add("Chưa xác thực");
//                    }
//                }
//
//            } catch (Exception e) {
//                System.err.println("Lỗi khi xử lý Order ID " + currentOrderId + ": " + e.getMessage());
//            }
//        }
//
//        request.setAttribute("verify", verifies);
        System.out.println(orderDetails);
        request.setAttribute("listProductOrder", orderDetails);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/client/order-placed.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
