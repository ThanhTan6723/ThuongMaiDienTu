package controller.admin.review;

import dao.client.ProductDAO;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "SaveReplyReviewControll", value = "/SaveReplyReviewControll")
public class SaveReplyReviewControll extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String id = request.getParameter("id");
        String replyText = request.getParameter("replyText");

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);

        boolean updateSuccess = ProductDAO.updateReviewReply(Integer.parseInt(id), replyText, formattedDateTime);

        if (updateSuccess) {
            String message = "Đã cập nhật câu trả lời cho review ID " + id + " vào lúc " + formattedDateTime;
            String jsonResponse = "{ \"success\": true, \"message\": \"" + message + "\", \"dateReply\": \"" + formattedDateTime + "\", \"replyText\": \"" + replyText + "\" }";
            out.println(jsonResponse);
        } else {
            String errorMessage = "Cập nhật câu trả lời không thành công cho review ID " + id;
            String jsonResponse = "{ \"success\": false, \"message\": \"" + errorMessage + "\" }";
            out.println(jsonResponse);

            System.err.println(errorMessage);
        }
    }
}
