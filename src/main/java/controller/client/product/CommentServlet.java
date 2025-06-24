package controller.client.product;

import dao.client.CommentDAO;
import model.Account;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "CommentServlet", value = "/CommentServlet")
public class CommentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("account");

        if (acc == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"status\":\"unauthorized\", \"message\":\"Bạn cần đăng nhập để bình luận.\"}");
            return;
        }

        String content = request.getParameter("comment");
        int productId = Integer.parseInt(request.getParameter("productId"));

        if (content == null || content.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Nội dung không được để trống.\"}");
            return;
        }

        // Lưu bình luận
        CommentDAO.addComment(acc.getId(), productId, content);

        response.getWriter().write("{\"status\":\"success\", \"message\":\"Đã lưu bình luận thành công.\"}");
    }


//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json;charset=UTF-8");
//
//        HttpSession session = request.getSession();
//        Account acc = (Account) session.getAttribute("account");
//
//        if (acc == null) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
//            response.getWriter().write("{\"status\":\"unauthorized\", \"message\": \"Bạn cần đăng nhập để bình luận.\"}");
//            return;
//        }
//
//        int accountId = acc.getId();
//        int productId = Integer.parseInt(request.getParameter("productId"));
//        String content = request.getParameter("comment");
//
//        if (content == null || content.trim().isEmpty()) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            response.getWriter().write("{\"status\":\"error\", \"message\": \"Nội dung bình luận không được để trống.\"}");
//            return;
//        }
//
//        // Lưu bình luận
//        CommentDAO.addComment(accountId, productId, content);
//
//        // Trả phản hồi JSON
//        String safeContent = content.replace("\"", "\\\""); // tránh lỗi JSON nếu có dấu nháy
//        response.getWriter().write("{\"status\":\"success\", \"createdAt\":\"Vừa xong\", \"content\":\"" + safeContent + "\"}");
//    }



}