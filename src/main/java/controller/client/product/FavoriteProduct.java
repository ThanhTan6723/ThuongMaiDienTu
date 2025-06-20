package controller.client.product;

import dao.client.ProductDAO;
import model.Account;
import model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "FavoriteProduct", value = "/FavoriteProduct")
public class FavoriteProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            request.getRequestDispatcher("/LoginControll").forward(request,response);
            return;
        }
        List<Product> listProduct = ProductDAO.listFavoriteProducts(account.getId());

        request.setAttribute("listProduct", listProduct);
        request.getRequestDispatcher("WEB-INF/client/favorite-product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        int productId = Integer.parseInt(request.getParameter("id"));
        try {
            ProductDAO.deleteFavoriteProduct(account.getId(),productId);
            response.getWriter().write("success");
        } catch (Exception e) {
            response.getWriter().write("error");
        }
    }

}
