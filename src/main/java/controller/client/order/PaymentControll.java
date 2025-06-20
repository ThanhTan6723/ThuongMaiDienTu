package controller.client.order;

import dao.client.OrderDAO;
import model.Account;
import model.Order;
import model.OrderDetail;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "PaymentControll", value = "/PaymentControll")
public class PaymentControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        if (session != null && session.getAttribute("account") != null) {
            Account account = (Account) session.getAttribute("account");
            int account_id = account.getId();
            List<OrderDetail> list = OrderDAO.getListOrder(account_id);
            request.setAttribute("orders", list);
            request.setAttribute("order",list.get(1));
        }
        request.getRequestDispatcher("/WEB-INF/client/payment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}