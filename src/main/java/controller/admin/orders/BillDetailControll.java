package controller.admin.orders;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

import dao.client.OrderDAO;
import model.OrderDetail;

@WebServlet(name = "BillDetailControll", value = "/BillDetailControll")
public class BillDetailControll extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String id = request.getParameter("id");
        System.out.println(id);
        List<OrderDetail> listDetail = OrderDAO.getOrderDetailByBid(Integer.parseInt(id));
        int sumQ = OrderDAO.getQuantityWithOderId(Integer.parseInt(id));
        System.out.println(listDetail);
        // Tạo một object để chứa thông tin trả về
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse(listDetail, sumQ);

        // Sử dụng Gson để chuyển đổi object thành JSON
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(orderDetailResponse);

        // Gửi JSON về client
        response.getWriter().write(jsonResponse);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    // Class để chứa thông tin trả về
    private class OrderDetailResponse {
        private List<OrderDetail> listBillDetails;
        private int sumQ;

        public OrderDetailResponse(List<OrderDetail> listBillDetails, int sumQ) {
            this.listBillDetails = listBillDetails;
            this.sumQ = sumQ;
        }
    }
}
