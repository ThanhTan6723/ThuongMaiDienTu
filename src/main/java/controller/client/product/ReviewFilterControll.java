package controller.client.product;

import com.google.gson.Gson;
import dao.client.ProductDAO;
import model.Review;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@WebServlet(name = "ReviewFilterControll", value = "/ReviewFilterControll")
public class ReviewFilterControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Review> reviews = ProductDAO.getListReviews();

        String ratingStr = request.getParameter("rating");
        String sortStr = request.getParameter("sort");

        if (ratingStr != null && !ratingStr.equals("all")) {
            int rating = Integer.parseInt(ratingStr);
            reviews.removeIf(review -> review.getRating() != rating);
        }

        if (sortStr != null) {
            switch (sortStr) {
                case "high-rating":
                    reviews.sort(Comparator.comparingInt(Review::getRating).reversed());
                    break;
                case "low-rating":
                    reviews.sort(Comparator.comparingInt(Review::getRating));
                    break;
                case "newest":
                    reviews.sort(Comparator.comparing(Review::getDateCreated).reversed());
                    break;
            }
        }

        System.out.println(reviews);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String json = gson.toJson(reviews);
        response.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
