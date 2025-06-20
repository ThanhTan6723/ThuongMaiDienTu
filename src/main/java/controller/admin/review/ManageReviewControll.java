package controller.admin.review;

import com.google.gson.Gson;
import dao.client.ProductDAO;
import dao.client.VoucherDAO;
import model.Review;
import model.Voucher;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ManageReviewControll", value = "/ManageReviewControll")
public class ManageReviewControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        List<Review> reviewList = ProductDAO.getListReviews();

        System.out.println(reviewList);
        String json = new Gson().toJson(reviewList);
        response.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}