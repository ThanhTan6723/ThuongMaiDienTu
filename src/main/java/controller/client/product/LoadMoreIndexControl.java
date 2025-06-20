package controller.client.product;

import dao.client.IndexDAO;
import model.Product;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "LoadMoreControl", value = "/LoadMoreControl")
public class LoadMoreIndexControl extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String amount = request.getParameter("exits");
        int iamount = Integer.parseInt(amount);
        System.out.println("so nè "+iamount);
        List<Product> list = IndexDAO.getNext4Product(iamount);
        System.out.println(list);
        PrintWriter out = response.getWriter();
        for (Product o : list) {
            out.println("<div style=\"height:400px\" class=\"product col-lg-3 col-md-4 col-sm-6 mix oranges fresh-meat\">\n" +
                    "<div class=\"featured__item\">\n" +
                    "<div class=\"featured__item__pic set-bg\">\n" +
                    "<a href=\"/DetailControl?pid=" + o.getId() + "\">\n" +
                    "<img src=\"" + o.getImage() + "\" alt=\"" + o.getName() + "\">\n" +
                    "</a>\n" +
                    "</div>\n" +
                    "<div class=\"featured__item__text\">\n" +
                    "<a class=\"product-name\" href=\"/DetailControl?pid=" + o.getId() + "\" style=\"color: black\">\n" +
                    o.getName() + "</a>\n" +
                    "<h5>" + o.getPrice() + "</h5>\n" +
                    "</div>\n" +
                    "<div class=\"text-center\">\n" +
                    "<form action=\"/AddToCartControl?pid=" + o.getId() + "\" method=\"post\" enctype=\"multipart/form-data\">\n" +
                    "<button style=\"padding: 10px 23px; border-radius: 5px; border: none; background-color: #7fad39; font-weight: 700\" type=\"submit\">\n" +
                    "<span style=\"color:#ffffff\">MUA NGAY</span>\n" +
                    "</button>\n" +
                    "</form>\n" +
                    "</div>\n" +
                    "</div>\n" +
                    "</div>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Triển khai nếu cần
    }
}
