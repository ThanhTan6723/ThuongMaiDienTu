package controller.admin.voucher;

import com.google.gson.Gson;
import dao.client.ProductDAO;
import dao.client.VoucherDAO;
import model.DiscountType;
import model.Product;
import model.Category;
import model.Voucher;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import java.sql.Date; // Import java.sql.Date

@WebServlet(name = "CreateVoucherControll", value = "/CreateVoucherControll")
public class CreateVoucherControll extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        try {
            String manualCode = request.getParameter("manualCode");
            String voucherType = request.getParameter("voucherType");
            String productCategory = request.getParameter("productCategory");
            int discount = Integer.parseInt(request.getParameter("discount"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            double minimumValue = Double.parseDouble(request.getParameter("minimum"));
            double maximumDisount = Double.parseDouble(request.getParameter("maximum"));

            // Validate and convert dates
            Date start = parseDate(startDate);
            Date end = parseDate(endDate);

            if (start == null || end == null) {
                throw new IllegalArgumentException("Invalid date format.");
            }

            Voucher voucher = createVoucher(manualCode, voucherType, productCategory, discount, quantity, start, end, minimumValue, maximumDisount);

            boolean success = VoucherDAO.insertVoucher(voucher);
            List<Voucher> list = VoucherDAO.getAllVouchers();
            String json = new Gson().toJson(success ? list : "Failed to save voucher");
            response.getWriter().write(json);
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write(new Gson().toJson("Error: " + e.getMessage()));
        }
    }

    private Date parseDate(String date) {
        try {
            return new Date(new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Voucher createVoucher(String manualCode, String voucherType, String productCategory, int discount, int quantity, Date startDate, Date endDate, double minimum, double maximum) throws Exception {
        Voucher voucher = new Voucher();
        String code = generateVoucherCode(manualCode, voucherType, productCategory);
        System.out.println(code);

        voucher.setCode(code);
        voucher.setDiscountPercentage(BigDecimal.valueOf(discount));
        voucher.setQuantity(quantity);
        voucher.setStartDate(startDate);
        voucher.setEndDate(endDate);
        voucher.setMinimumOrderValue(minimum);
        voucher.setMaximumDiscount(maximum);

        setDiscountTypeAndProductOrCategory(voucher, voucherType, productCategory);

        System.out.println("Voucher create: " + voucher);
        return voucher;
    }

    private String generateVoucherCode(String manualCode, String voucherType, String productCategory) throws Exception {
        if (manualCode != null && !manualCode.isEmpty()) {
            return manualCode;
        }

        if ("product".equals(voucherType)) {
            Product product = ProductDAO.getProductById(Integer.parseInt(productCategory));
            if (product != null) {
                return removeAccents(product.getName()).toUpperCase().replaceAll("\\s+", "") + generateRandomDigits(4);
            } else {
                return "PRODUCT" + generateRandomDigits(4);
            }
        } else if ("category".equals(voucherType)) {
            Category category = ProductDAO.getCategoryById1(Integer.parseInt(productCategory));
            if (category != null) {
                return removeAccents(category.getName()).toUpperCase().replaceAll("\\s+", "") + generateRandomDigits(4);
            } else {
                return "CATEGORY" + generateRandomDigits(4);
            }
        } else {
            return "ALL" + generateRandomDigits(4);
        }
    }

    private void setDiscountTypeAndProductOrCategory(Voucher voucher, String voucherType, String productCategory) throws Exception {
        if ("product".equals(voucherType)) {
            voucher.setDiscountType(new DiscountType(3, "Product"));
            Product product = ProductDAO.getProductById(Integer.parseInt(productCategory));
            voucher.setProduct(product);
        } else if ("category".equals(voucherType)) {
            voucher.setDiscountType(new DiscountType(2, "Category"));
            Category category = ProductDAO.getCategoryById1(Integer.parseInt(productCategory));
            voucher.setCategory(category);
        } else {
            voucher.setDiscountType(new DiscountType(1, "All"));
        }
    }

    private String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private String removeAccents(String text) {
        String temp = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }
}
