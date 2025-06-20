package controller.admin.product;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dao.admin.ProductAdminDAO;
import dao.client.ProductDAO;
import model.Batch;
import model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "InventoryProduct", value = "/InventoryProduct")
public class InventoryProduct extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> productList = ProductAdminDAO.getListProducts();

        // Map để lưu tổng số lượng và số lượng hiện tại của từng sản phẩm
        Map<Integer, Integer> productTotalQuantities = new HashMap<>();
        Map<Integer, Integer> productCurrentQuantities = new HashMap<>();
        Map<Integer, Integer> pAlmostExpired = new HashMap<>();
        Map<Integer, Integer> pStillExpired = new HashMap<>();
        Map<Integer, Integer> pExpired = new HashMap<>();

        // Tính tổng số lượng và số lượng hiện tại của từng sản phẩm
        for (Product product : productList) {
            product = ProductAdminDAO.getProductWithBatchesById(product.getId());
            int totalQuantity = 0;
            int currentQuantity = 0;
            for (Batch batch : product.getBatches()) {
                totalQuantity += batch.getQuantity();
                currentQuantity += batch.getCurrentQuantity();
            }

            productTotalQuantities.put(product.getId(), totalQuantity);
            productCurrentQuantities.put(product.getId(), currentQuantity);
        }
        //sản phẩm còn hạn
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            product = ProductAdminDAO.getProductStillExpiredById(product.getId());
            int totalQuantities = 0;
            for (Batch batch : product.getBatches()) {
                totalQuantities += batch.getCurrentQuantity();
            }
            pStillExpired.put(product.getId(), totalQuantities);
            productList.set(i, product);
        }
        //sản phẩm hết hạn
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            product = ProductAdminDAO.getProductExpiredById(product.getId());

            int expiredCurrentQuantity = 0;
            for (Batch batch : product.getBatches()) {
                expiredCurrentQuantity += batch.getCurrentQuantity();
            }
            pExpired.put(product.getId(), expiredCurrentQuantity);
            productList.set(i, product);
        }
        //sản phẩm sắp hết hạn
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            product = ProductAdminDAO.getProductAlmostExpiredById(product.getId());
            int almostExpiredCurrentQuantity = 0;
            for (Batch batch : product.getBatches()) {
                almostExpiredCurrentQuantity += batch.getCurrentQuantity();
            }
            pAlmostExpired.put(product.getId(), almostExpiredCurrentQuantity);
            productList.set(i, product);
        }

        // Tạo mảng JSON để trả về cho client
        JsonArray jsonArray = new JsonArray();
        for (Product product : productList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", product.getId());
            jsonObject.addProperty("name", product.getName());
            jsonObject.addProperty("price", product.getPrice());
            jsonObject.addProperty("image", product.getImage());
            jsonObject.addProperty("description", product.getDescription());
            jsonObject.addProperty("categoryName", ProductDAO.getCategoryById(product.getCategory().getId())); // Thêm tên category vào JSON
            jsonObject.addProperty("totalQuantity", productTotalQuantities.get(product.getId()));
            jsonObject.addProperty("currentQuantity", productCurrentQuantities.get(product.getId()));
            jsonObject.addProperty("pStillExpired",pStillExpired.get(product.getId()));
            jsonObject.addProperty("pAlmostExpired",pAlmostExpired.get(product.getId()));
            jsonObject.addProperty("pExpired",pExpired.get(product.getId()));
            jsonArray.add(jsonObject);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonArray.toString());
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}