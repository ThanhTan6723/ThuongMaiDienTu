package controller.admin.user;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import dao.client.AccountDAO; // Import DAO để lấy trạng thái khóa

@WebServlet(name = "GetUserLockedStatus", value = "/GetUserLockedStatus")
public class GetUserLockedStatus extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Không cần xử lý yêu cầu GET, vì chúng ta chỉ muốn lấy trạng thái khóa trong yêu cầu POST
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain"); // Đặt loại nội dung của phản hồi là văn bản thuần túy

        // Lấy ID người dùng từ tham số yêu cầu
        String userIdStr = request.getParameter("userId");

        // Kiểm tra xem userId có null không và có thể chuyển đổi thành số nguyên không
        if (userIdStr != null && userIdStr.matches("\\d+")) {
            int userId = Integer.parseInt(userIdStr);

            // Gọi DAO để lấy trạng thái khóa của người dùng
            boolean isLocked = AccountDAO.isUserLocked(userId);

            // Trả về trạng thái khóa dưới dạng phản hồi
            response.getWriter().write(String.valueOf(isLocked));
        } else {
            // Nếu userId không hợp lệ, trả về phản hồi lỗi
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid user ID");
        }
    }
}
