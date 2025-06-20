package controller.admin.review;

import dao.client.OrderDAO;
import dao.client.ProductDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ApproveReviewControll", value = "/ApproveReviewControll")
public class ApproveReviewControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String id = request.getParameter("id");
        int rid = Integer.parseInt(id);

        // Assuming ProductDAO.acceptReview returns a boolean indicating success
        boolean reviewAccepted = ProductDAO.acceptReview(rid);

        // Check if reviewAccepted is true/false based on your DAO logic
        if (reviewAccepted) {
            // Redirect to load manage review page
            response.sendRedirect(request.getContextPath() + "/LoadManageReviewControll");
        } else {
            // Handle failure scenario, perhaps send an error response or redirect
            response.getWriter().println("Failed to accept the review.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}