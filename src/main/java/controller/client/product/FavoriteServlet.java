package controller.client.product;

import dao.client.ProductDAO;
import model.Account;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/FavoriteServlet")
public class FavoriteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            request.getRequestDispatcher("/LoginControll").forward(request,response);
            return;
        }

        int userId = account.getId();
        int productId = Integer.parseInt(request.getParameter("productId"));
        PrintWriter out = response.getWriter();

        // Kiểm tra xem sản phẩm này đã được yêu thích chưa
        boolean isFavorited = ProductDAO.isProductFavorited(userId, productId);

        if (isFavorited) {
            ProductDAO.deleteFavoriteProduct(userId, productId);
            out.print("removed");
        } else {
            ProductDAO.insertFavoriteProduct(userId, productId);
            out.print("added");
        }

        out.flush();
    }
}

