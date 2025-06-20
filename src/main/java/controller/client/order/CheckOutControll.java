package controller.client.order;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Account;
import model.Cart;
import model.OrderDetail;

@WebServlet("/CheckOutControll")
public class CheckOutControll extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        Map<Integer, OrderDetail> cart = Cart.readCartFromCookies(request, account.getId());
////        double discountValue =Double.parseDouble((String) session.getAttribute("discount")) ;
//        System.out.println(discountValue);
//        if (discountValue !=0) {
//            request.setAttribute("discountValue", discountValue);
//        }

        if (account == null) {
            response.sendRedirect(request.getContextPath() + "/LoginControll");
            return;
        } else {
            Set<Integer> key = cart.keySet();
            double total = 0;
            double totalWeight = 0;
            for (Integer k : key) {
                total += cart.get(k).getPrice();
                totalWeight += cart.get(k).getProduct().getWeight() * cart.get(k).getQuantity();
            }

            request.setAttribute("totalWeight", totalWeight);
            request.setAttribute("cart", cart);
            request.setAttribute("total", total);
            request.setAttribute("name", account.getName());
            request.setAttribute("phone", account.getTelephone());
        }
        request.getRequestDispatcher("/WEB-INF/client/checkout.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
