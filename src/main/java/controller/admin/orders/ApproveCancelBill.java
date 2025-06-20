package controller.admin.orders;

import dao.client.OrderDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ApproveCancelBill", value = "/ApproveCancelBill")
public class ApproveCancelBill extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String id = request.getParameter("id");
        int oid = Integer.parseInt(id);

        String orderStatus = "Đơn hàng đã được hủy";
        OrderDAO.updateOrderStatus(oid, orderStatus);

        response.sendRedirect(request.getContextPath()+"/BillControll");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
// TODO Auto-generated method stub
        doGet(request, response);
    }
}