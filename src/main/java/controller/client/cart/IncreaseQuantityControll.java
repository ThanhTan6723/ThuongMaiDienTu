package controller.client.cart;

import com.google.gson.Gson;
import model.Account;
import model.OrderDetail;
import model.Cart;
import dao.client.ProductDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

@WebServlet("/IncreaseQuantityControll")
public class IncreaseQuantityControll extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String key = request.getParameter("key");
        int k = Integer.parseInt(key);
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        Map<Integer, OrderDetail> cart = Cart.readCartFromCookies(request,account.getId());

        if (cart.containsKey(k)) {
            OrderDetail orderDetail = cart.get(k);
            orderDetail.setQuantity(orderDetail.getQuantity() + 1);
            orderDetail.setPrice(orderDetail.getQuantity() * orderDetail.getProduct().getPrice());

            cart.put(k, orderDetail);
            Cart.writeCartToCookies(request, response, cart,account.getId());

            double totalAmount = cart.values().stream().mapToDouble(od -> od.getQuantity() * od.getProduct().getPrice()).sum();
            String jsonResponse = new Gson().toJson(new Response(orderDetail.getQuantity(), orderDetail.getPrice(), totalAmount));
            response.getWriter().write(jsonResponse);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private static class Response {
        private int quantity;
        private double totalPrice;
        private double totalAmount;

        public Response(int quantity, double totalPrice, double totalAmount) {
            this.quantity = quantity;
            this.totalPrice = totalPrice;
            this.totalAmount = totalAmount;
        }

        // Getters and setters (if needed)
    }
}
