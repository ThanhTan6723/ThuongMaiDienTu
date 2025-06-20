package controller.client.cart;

import dao.client.ProductDAO;
import dao.client.VoucherDAO;
import model.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CartControll", value = "/CartControll")
public class CartControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            response.sendRedirect(request.getContextPath() + "/LoginControll");
        } else {
            // Đọc giỏ hàng từ cookies
            Map<Integer, OrderDetail> cart = Cart.readCartFromCookies(request,account.getId());

            // Tính tổng tiền của giỏ hàng và cập nhật giá tiền cho từng OrderDetail
            double total = 0;
            for (OrderDetail orderDetail : cart.values()) {
                // Tính giá tiền của mỗi OrderDetail
                double totalPrice = orderDetail.getQuantity() * orderDetail.getProduct().getPrice();
                orderDetail.setPrice(totalPrice);
                total += totalPrice;
            }

            // Lưu lại giỏ hàng vào cookies sau khi cập nhật giá tiền
            Cart.writeCartToCookies(request, response, cart,account.getId());

            VoucherDAO voucherDAO = new VoucherDAO();
            List<Voucher> savedVouchers = voucherDAO.getVoucherForAccount(account.getId());
            request.setAttribute("savedVouchers", savedVouchers);
            request.setAttribute("cart", cart);
            request.setAttribute("total", total);
            request.getRequestDispatcher("/WEB-INF/client/cart.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
