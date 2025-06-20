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
import model.Product;

@WebServlet("/ListProductsControll")
public class ListProductsControll extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Product> productList = ProductAdminDAO.getListProducts();
		System.out.println(productList.toString());
		JsonArray jsonArray = new JsonArray();
		for (Product product : productList) {

			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("id", product.getId());
			jsonObject.addProperty("name", product.getName());
			jsonObject.addProperty("price", product.getPrice());
			jsonObject.addProperty("image", product.getImage());
			jsonObject.addProperty("description", product.getDescription());
			jsonObject.addProperty("categoryName", ProductDAO.getCategoryById(product.getCategory().getId())); // Thêm tên category vào JSON
			
			jsonArray.add(jsonObject);
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonArray.toString());

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
