package controller.client.cart;

import com.google.gson.Gson;
import model.Account;
import model.OrderDetail;
import model.Cart;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Map;

@WebServlet("/DeleteOrderControll")
public class DeleteOrderControll extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        String key = request.getParameter("key");
        int k = Integer.parseInt(key);
        System.out.println("key"+k);

        Map<Integer, OrderDetail> cart = Cart.readCartFromCookies(request,account.getId());
        if (cart.containsKey(k)) {
            cart.remove(k);
//            Cart.writeCartToCookies(request, response,cart);

            int sizeCart = (int) session.getAttribute("size");
            sizeCart -= 1;
            session.setAttribute("size", sizeCart);

            Cart.deleteCartItemToCookies(request,response,account.getId(),k);
            double totalAmount = cart.values().stream().mapToDouble(od -> od.getQuantity() * od.getProduct().getPrice()).sum();
            String jsonResponse = new Gson().toJson(new Response(sizeCart, totalAmount));
            response.getWriter().write(jsonResponse);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private static class Response {
        private int sizeCart;
        private double totalAmount;

        public Response(int sizeCart, double totalAmount) {
            this.sizeCart = sizeCart;
            this.totalAmount = totalAmount;
        }

        // Getters and setters (if needed)
    }
}
