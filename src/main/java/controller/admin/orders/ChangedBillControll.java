package controller.admin.orders;

import com.google.gson.Gson;
import dao.client.OrderDAO;
import model.Order;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ChangedBillControll", value = "/ChangedBillControll")
public class ChangedBillControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        String status = "Đơn hàng bị chỉnh sửa";
        List<Order> listOrders = OrderDAO.getAllOrders(status);

        String json = new Gson().toJson(listOrders);
        response.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}