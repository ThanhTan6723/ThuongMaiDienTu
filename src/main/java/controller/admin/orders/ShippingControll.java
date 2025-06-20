package controller.admin.orders;

import com.google.gson.Gson;
import dao.client.OrderDAO;
import model.Order;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ShippingControll", value = "/ShippingControll")
public class ShippingControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String id = request.getParameter("id");
        int oid = Integer.parseInt(id);
        HttpSession session = request.getSession();

        String orderStatus = "Đang giao hàng";
        OrderDAO.updateOrderStatus(oid, orderStatus);

        response.sendRedirect(request.getContextPath()+"/BillControll");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
// TODO Auto-generated method stub
        doGet(request, response);
    }
}