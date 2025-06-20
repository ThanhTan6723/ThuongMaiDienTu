package controller.admin.voucher;

import com.google.gson.Gson;
import dao.client.ProductDAO;
import model.Category;
import model.Product;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ProductCategoryControll", value = "/ProductCategoryControll")
public class ProductCategoryControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String type = request.getParameter("type");
        List<Product> productList = new ArrayList<>();
        List<Category> categoryList = new ArrayList<>();

        if ("product".equals(type)) {
            productList = ProductDAO.getAllProduct();
//            System.out.println(productList);
        } else if ("category".equals(type)) {
            categoryList = ProductDAO.getListCategory();
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        Map<String, Object> result = new HashMap<>();
        result.put("products", productList);
        result.put("categories", categoryList);

        out.print(gson.toJson(result));
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle POST requests here if needed
    }
}
