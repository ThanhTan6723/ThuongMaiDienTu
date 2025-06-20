package controller.admin.product;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LoadProductsPage", value = "/LoadProductsPage")
public class LoadProductsPage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
               request.setCharacterEncoding("utf-8");
               response.setContentType("text/html;charset=utf-8");
               request.getRequestDispatcher("WEB-INF/admin/show-product.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}