package controller.admin.product;

import dao.client.ProductDAO;
import model.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

@WebServlet(name = "DetailProductControll", value = "/DetailProductControll")
public class DetailProductControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
           request.setCharacterEncoding("utf-8");
           response.setContentType("text/html;charset=utf-8");
           String id = request.getParameter("id");
           Product product = ProductDAO.getProductById(Integer.parseInt(id));
           Category category = ProductDAO.getCategoryById1(product.getCategory().getId());
           List<Image> listImage = ProductDAO.listImageProduct(Integer.parseInt(id));
           List<Batch> listBatch = ProductDAO.getListBatchById(Integer.parseInt(id));
           request.setAttribute("listBatch",listBatch);
           request.setAttribute("product",product);
           request.setAttribute("category",category);

           request.setAttribute("listImage",listImage);
           request.getRequestDispatcher("WEB-INF/admin/detail-product.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
