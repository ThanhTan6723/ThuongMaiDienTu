package controller.admin.orders;

import com.google.gson.Gson;
import dao.client.OrderDAO;
import model.Order;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ListRefusedControll", value = "/ListRefusedControll")
public class ListRefusedControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        String status = "Đơn hàng bị từ chối";
        List<Order> listOrders = OrderDAO.getAllOrders(status);

        String json = new Gson().toJson(listOrders);
        response.getWriter().write(json);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}