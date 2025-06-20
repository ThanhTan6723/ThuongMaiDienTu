package controller.client.voucher;

import dao.client.VoucherDAO;
import model.Account;
import model.OrderDetail;
import model.Voucher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ApplySearchVoucher", value = "/ApplySearchVoucher")
public class ApplySearchVoucher extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String voucherCode = request.getParameter("voucherCode");
        BigDecimal totalAmount = new BigDecimal(request.getParameter("totalAmount"));

        HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        Map<Integer, OrderDetail> cart = (Map<Integer, OrderDetail>) session.getAttribute("cart");
        Account account = (Account) session.getAttribute("account");
        request.setAttribute("vcode",voucherCode);

        if (account == null) {
            response.getWriter().write("{\"success\": false, \"message\": \"Bạn cần đăng nhập để áp dụng voucher.\"}");
            return;
        }
        try {
            // Kiểm tra xem mã voucher có tồn tại không
            Voucher voucher = VoucherDAO.getVoucherByCode(voucherCode);
            System.out.println(voucher);

            if (voucher == null) {
                response.getWriter().write("{\"success\": false, \"message\": \"Mã voucher không tồn tại.\"}");
                return;
            }
            // Kiểm tra xem mã voucher đã được lưu chưa
            List<Integer> savedVoucherIds = VoucherDAO.getSavedVouchers(account.getId());
            boolean isSaved = savedVoucherIds.contains(voucher.getId());

            if (isSaved) {
                // Kiểm tra xem mã voucher đã được sử dụng chưa
                int checkUsage = VoucherDAO.checkUsageVoucher(account.getId(), voucher.getId());
                if (checkUsage == 0) {
                    response.getWriter().write("{\"success\": false, \"message\": \"Mã voucher đã được sử dụng.\"}");
                    return;
                }
                // Kiểm tra mã voucher có khớp với sản phẩm trong giỏ hàng không
                boolean isMatching = isVoucherApplicable(voucher, cart);
                if (isMatching) {
                    BigDecimal discountValue = calculateDiscount(voucher, totalAmount, cart);
                    BigDecimal finalAmount = totalAmount.subtract(discountValue);
                    String jsonResponse = "{\"success\": true, \"message\": \"\", \"discountValue\": \"" + discountValue.toPlainString() + "\", \"finalAmount\": \"" + finalAmount.toPlainString() + "\"}";
                    response.getWriter().write(jsonResponse);
                } else {
                    response.getWriter().write("{\"success\": false, \"message\": \"Không khớp với sản phẩm hiện có.\"}");
                }
            } else {
                // Nếu chưa lưu, tiếp tục kiểm tra và áp dụng mã voucher
                boolean isMatching = isVoucherApplicable(voucher, cart);

                if (isMatching) {
                    boolean savedVoucher = VoucherDAO.saveEVoucher(account.getId(), voucher.getId());
                    if (savedVoucher) {
                        BigDecimal discountValue = calculateDiscount(voucher, totalAmount, cart);
                        BigDecimal finalAmount = totalAmount.subtract(discountValue);
                        String jsonResponse = "{\"success\": true, \"message\": \"\", \"discountValue\": \"" + discountValue.toPlainString() + "\", \"finalAmount\": \"" + finalAmount.toPlainString() + "\"}";
                        response.getWriter().write(jsonResponse);
                    } else {
                        response.getWriter().write("{\"success\": false, \"message\": \"Lỗi khi lưu voucher.\"}");
                    }
                } else {
                    response.getWriter().write("{\"success\": false, \"message\": \"Không khớp với sản phẩm hiện có.\"}");
                }
            }
        } catch (Exception e) {
            response.getWriter().write("{\"success\": false, \"message\": \"Đã xảy ra lỗi khi xử lý yêu cầu.\"}");
            e.printStackTrace(); // In production code, log the exception properly
        }
    }

    private boolean isVoucherApplicable(Voucher voucher, Map<Integer, OrderDetail> cart) {
        if ("All".equals(voucher.getDiscountType().getType())) {
            return true;
        }

        for (OrderDetail orderDetail : cart.values()) {
            if ("Product".equals(voucher.getDiscountType().getType())) {
                if (voucher.getProduct().getId() == orderDetail.getProduct().getId()) {
                    return true;
                }
            } else if ("Category".equals(voucher.getDiscountType().getType())) {
                if (voucher.getCategory().getId() == orderDetail.getProduct().getCategory().getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    private BigDecimal calculateDiscount(Voucher voucher, BigDecimal totalAmount, Map<Integer, OrderDetail> cart) {
        BigDecimal discountValue = BigDecimal.ZERO;

        switch (voucher.getDiscountType().getType()) {
            case "Product":
                BigDecimal productDiscountPercentage = voucher.getDiscountPercentage().divide(BigDecimal.valueOf(100));
                for (OrderDetail orderDetail : cart.values()) {
                    if (orderDetail.getProduct().getId() == voucher.getProduct().getId()) {
                        BigDecimal totalProductPrice = new BigDecimal(orderDetail.getPrice());
                        discountValue = totalProductPrice.multiply(productDiscountPercentage);
                        break; // Assuming one product discount should be applied once
                    }
                }
                break;
            case "Category":
                BigDecimal categoryDiscountPercentage = voucher.getDiscountPercentage().divide(BigDecimal.valueOf(100));
                BigDecimal totalCategoryPrice = BigDecimal.ZERO;
                for (OrderDetail orderDetail : cart.values()) {
                    if (orderDetail.getProduct().getCategory().getId() == voucher.getCategory().getId()) {
                        BigDecimal productPrice = new BigDecimal(orderDetail.getPrice());
                        totalCategoryPrice = totalCategoryPrice.add(productPrice);
                    }
                }
                discountValue = totalCategoryPrice.multiply(categoryDiscountPercentage);
                break;
            case "All":
                BigDecimal allDiscountPercentage = voucher.getDiscountPercentage().divide(BigDecimal.valueOf(100));
                discountValue = totalAmount.multiply(allDiscountPercentage);
                break;
        }

        return discountValue;
    }
}
