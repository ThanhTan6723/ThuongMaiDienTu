package controller.admin.product;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import dao.admin.AccountsDAO;
import dao.client.AccountDAO;
import dao.client.ProductDAO;
import model.Account;
import model.Product;

@WebServlet("/DeleteProductControll")
public class DeleteProductControll extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String pid = request.getParameter("id");
		ProductDAO.removeProduct(Integer.parseInt(pid));
		request.getRequestDispatcher("WEB-INF/admin/show-product.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
