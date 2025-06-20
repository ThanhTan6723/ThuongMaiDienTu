package controller.client.voucher;

import dao.client.VoucherDAO;
import model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@WebServlet(name = "ApplyVoucherControll", urlPatterns = {"/ApplyVoucherControll"})
public class ApplyVoucherControll extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int voucherId = Integer.parseInt(request.getParameter("voucherId"));
        BigDecimal totalAmount = new BigDecimal(request.getParameter("totalAmount"));

        HttpSession session = request.getSession();
        Account account = (Account) request.getAttribute("account");
        request.setAttribute("vid", voucherId);

        Voucher voucher = VoucherDAO.getVoucherById(voucherId);
        BigDecimal discountValue = BigDecimal.ZERO;

        System.out.println(voucher);

        if (voucher != null) {
            Object obj = session.getAttribute("cart");
            Map<Integer, OrderDetail> cart = (Map<Integer, OrderDetail>) obj;
            System.out.println(cart);

            switch (voucher.getDiscountType().getType()) {
                case "Product":
                    BigDecimal productDiscountPercentage = voucher.getDiscountPercentage().divide(new BigDecimal(100));
                    for (OrderDetail orderDetail : cart.values()) {
                        if (orderDetail.getProduct().getId() == voucher.getProduct().getId()) {
                            BigDecimal totalProductPrice = new BigDecimal(orderDetail.getPrice());
                            discountValue = totalProductPrice.multiply(productDiscountPercentage);
                        }
                    }
                    break;
                case "Category":
                    BigDecimal categoryDiscountPercentage = voucher.getDiscountPercentage().divide(new BigDecimal(100));
                    BigDecimal totalCategoryPrice = BigDecimal.ZERO;
                    for (OrderDetail orderDetail : cart.values()) {
                        if (orderDetail.getProduct().getCategory().getId() == voucher.getCategory().getId()) {
                            BigDecimal productPrice = new BigDecimal(orderDetail.getPrice());
                            totalCategoryPrice = totalCategoryPrice.add(productPrice);
                        }
                    }
                    BigDecimal categoryDiscount = totalCategoryPrice.multiply(categoryDiscountPercentage);
                    discountValue = discountValue.add(categoryDiscount);
                    break;
                case "All":
                    BigDecimal allDiscountPercentage = voucher.getDiscountPercentage().divide(new BigDecimal(100));
                    discountValue = totalAmount.multiply(allDiscountPercentage);
                    break;
            }
        }

        // Kiểm tra nếu số tiền giảm giá lớn hơn maximumDiscount
        BigDecimal maximumDiscount = BigDecimal.valueOf(voucher.getMaximumDiscount());
        if (discountValue.compareTo(maximumDiscount) > 0) {
            discountValue = maximumDiscount;
        }

        BigDecimal finalAmount = totalAmount.subtract(discountValue);
        session.setAttribute("discount", discountValue.toPlainString());

        response.setContentType("application/json");
        response.getWriter().write("{\"discountValue\": \"" + discountValue.toPlainString() + "\", \"finalAmount\": \"" + finalAmount.toPlainString() + "\", " +
                "\"voucher\": {" +
                "\"id\": \"" + voucher.getId() + "\", " +
                "\"code\": \"" + voucher.getCode() + "\", " +
                "\"discountPercentage\": \"" + voucher.getDiscountPercentage().toPlainString() + "\", " +
                "\"discountType\": {\"type\": \"" + voucher.getDiscountType().getType() + "\"}, " +
                "\"endDate\": \"" + voucher.getEndDate() + "\"" +
                "}}");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}