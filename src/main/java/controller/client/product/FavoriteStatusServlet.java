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

@WebServlet("/FavoriteStatusServlet")
public class FavoriteStatusServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            response.getWriter().print("not_favorited"); // Chưa đăng nhập thì mặc định là chưa yêu thích
            return;
        }

        int productId = Integer.parseInt(request.getParameter("productId"));
        PrintWriter out = response.getWriter();

        // Kiểm tra xem sản phẩm này đã được yêu thích chưa
        boolean isFavorited = ProductDAO.isProductFavorited(account.getId(), productId);

        if (isFavorited) {
            out.print("favorited");
        } else {
            out.print("not_favorited");
        }

        out.flush();
    }
}
