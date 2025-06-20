package controller.client.product;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.client.IndexDAO;
import dao.client.ProductDAO;
import model.Product;
import org.json.JSONObject;

@WebServlet("/ShowProductControl")
public class ShowProductControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int RECORDS_PER_PAGE = 12; // Number of records per page

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        try {
            String sort = request.getParameter("sort");
            if (sort == null || sort.isEmpty()) {
                sort = "id-asc";
            }
            int categoryId = 0;
            if (request.getParameter("cid") != null && !request.getParameter("cid").isEmpty()) {
                categoryId = Integer.parseInt(request.getParameter("cid"));
            }
            StringTokenizer s = new StringTokenizer(sort, "-");
            String sortName = s.nextToken();
            String sortType = s.nextToken();

            int page = 1; // Default to page 1
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    page = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    // Handle exception
                }
            }

            boolean applyFilter = false;
            String category = request.getParameter("category");
            String priceFrom = request.getParameter("price_from");
            String priceTo = request.getParameter("price_to");
            String provider = request.getParameter("provider");

            applyFilter = (category != null && !category.isEmpty()) ||
                    (priceFrom != null && !priceFrom.isEmpty()) ||
                    (priceTo != null && !priceTo.isEmpty()) ||
                    (provider != null && !provider.isEmpty());

            List<Product> listSale = IndexDAO.getTop8();
            List<Product> productListForPage;
            int endPage;

            if (!applyFilter) {
                List<Product> productList = ProductDAO.pagingProduct(categoryId, sortName, sortType);
                productListForPage = getProductListForPage(productList, page);
                endPage = (int) Math.ceil((double) productList.size() / RECORDS_PER_PAGE);
            } else {
                Double priceFromVal = (priceFrom != null && !priceFrom.isEmpty()) ? Double.parseDouble(priceFrom) : null;
                Double priceToVal = (priceTo != null && !priceTo.isEmpty()) ? Double.parseDouble(priceTo) : null;
                Integer categoryVal = (category != null && !category.isEmpty()) ? Integer.parseInt(category) : null;
                Integer providerVal = (provider != null && !provider.isEmpty()) ? Integer.parseInt(provider) : null;
                List<Product> filteredProducts = ProductDAO.getFilteredProducts(categoryVal, providerVal, priceFromVal, priceToVal);
                productListForPage = getProductListForPage(filteredProducts, page);
                endPage = (int) Math.ceil((double) filteredProducts.size() / RECORDS_PER_PAGE);
            }

            // Create a Map to store the current quantities of products
            Map<Integer, Integer> productCurrentQuantities = new HashMap<>();
            for (Product product : productListForPage) {
                int totalQuantity = ProductDAO.getTotalQuantityP(product.getId());
                productCurrentQuantities.put(product.getId(), totalQuantity);
            }
            request.setAttribute("productCurrentQuantities", productCurrentQuantities);

            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("html", generateProductHtml(productListForPage, productCurrentQuantities));
                jsonResponse.put("currentPage", page);
                jsonResponse.put("totalPages", endPage);
                jsonResponse.put("productCurrentQuantities", new JSONObject(productCurrentQuantities));
                response.getWriter().write(jsonResponse.toString());
            } else {
                request.setAttribute("listProducts", productListForPage);
                request.setAttribute("endPage", endPage);
                request.setAttribute("listSale", listSale);
                request.setAttribute("sort", sort);
                request.setAttribute("cid", categoryId);
                request.setAttribute("category", category);
                request.setAttribute("price_from", priceFrom);
                request.setAttribute("price_to", priceTo);
                request.setAttribute("provider", provider);
                request.getRequestDispatcher("/WEB-INF/client/menu.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Product> getProductListForPage(List<Product> list, int page) {
        int startIndex = (page - 1) * RECORDS_PER_PAGE;
        int endIndex = Math.min(startIndex + RECORDS_PER_PAGE, list.size());
        return list.subList(startIndex, endIndex);
    }

    private String generateProductHtml(List<Product> productList, Map<Integer, Integer> productQuantities) {
        StringBuilder htmlBuilder = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("#,###.### ₫");
        decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));

        for (Product product : productList) {
            String formattedPrice = decimalFormat.format(product.getPrice());
            htmlBuilder.append("<div class=\"col-lg-3 col-md-4 col-sm-6 mix oranges fresh-meat\">")
                    .append("<div class=\"product-col\">")
                    .append("<div class=\"featured__item\">")
                    .append("<div class=\"product-frame\">");

            // Check product quantity and add overlay if necessary
//            if (productQuantities.get(product.getId()) <= 0) {
//                htmlBuilder.append("<div class=\"out-of-stock-overlay\">")
//                        .append("<span class=\"out-of-stock-text\">Hết hàng</span>")
//                        .append("</div>");
//            }

            htmlBuilder.append("<div class=\"featured__item__pic set-bg\">")
                    .append("<a href=\"DetailControl?pid=").append(product.getId()).append("\">")
                    .append("<img src=\"").append(product.getImage()).append("\" alt=\"").append(product.getName()).append("\">")
                    .append("</a>")
                    .append("</div>")
                    .append("<div class=\"featured__item__text\">")
                    .append("<a class=\"product-name\" href=\"DetailControl?pid=").append(product.getId()).append("\" style=\"color: black\">")
                    .append(product.getName()).append("</a>")
                    .append("<h5>").append(formattedPrice).append("</h5>")
                    .append("</div>")
                    .append("</div>")
                    .append("<div class=\"text-center\">")
                    .append("<c:url var=\"addToCart\" value=\"/AddToCartControl\"></c:url>")
                    .append("<form action=\"DetailControl?pid=").append(product.getId()).append("\" method=\"post\" enctype=\"multipart/form-data\">")
                    .append("<button class=\"buy-now-btn\" ")
                    .append("data-product-id=\"").append(product.getId()).append("\" ")
                    .append("data-product-name=\"").append(product.getName()).append("\" ")
                    .append("data-product-image=\"").append(product.getImage()).append("\" ")
                    .append("style=\"padding: 10px 23px; border-radius: 30px; border: none; ")
                    .append("background-color: #7fad39; font-weight: 700; color: #ffffff\">")
                    .append("MUA NGAY")
                    .append("</button>")
                    .append("</form>")
                    .append("</div>")
                    .append("</div>")
                    .append("</div>")
                    .append("</div>");
        }
        return htmlBuilder.toString();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
