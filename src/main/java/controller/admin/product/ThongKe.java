package controller.admin.product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dao.admin.AccountsDAO;
import dao.admin.ProductAdminDAO;
import dao.client.ProductDAO;
import model.Account;
import model.Batch;
import model.Product;
@WebServlet(name = "ThongKe", value = "/ThongKe")
public class ThongKe extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        for(Product p : ProductDAO.getListProductBatch1()){
            System.out.println(p.toString());
        }
        JsonArray jsonArray = new JsonArray();

        for (Product product : ProductDAO.getListProductBatch1()) {
            if (product.getBatches() != null) {
                for (Batch batch : product.getBatches()) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("id", product.getId());
                    jsonObject.addProperty("name", product.getName());
                    jsonObject.addProperty("price", product.getPrice());
                    jsonObject.addProperty("image", product.getImage());
                    jsonObject.addProperty("quantity", batch.getQuantity());
                    jsonObject.addProperty("dateOfImporting", batch.getDateOfImporting().toString());
                    jsonObject.addProperty("batch_id", batch.getId());
                    jsonObject.addProperty("batch_name", batch.getName());

                    jsonArray.add(jsonObject);
                }
            }
        }

        // Set response type to JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonArray.toString());
    }



    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
