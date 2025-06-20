/*
package controller.admin.product;

import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.*;

@WebServlet("/InforBatch")
public class InforBatch extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<Product> addedProducts = (List<Product>) session.getAttribute("addedProducts");

        if (addedProducts != null) {
            for(Product p: addedProducts){
                System.out.println(p.toString());
            }
            // Xử lý danh sách sản phẩm vừa thêm (hiển thị hoặc thực hiện thao tác khác)
            request.setAttribute("addedProducts", addedProducts);
            // Xóa danh sách sản phẩm khỏi session sau khi đã lấy ra
            session.removeAttribute("addedProducts");
            System.out.println("co");
        }else{
            System.out.println("khong");
        }

        // Forward tới trang JSP hoặc xử lý logic tiếp theo
        request.getRequestDispatcher("WEB-INF/admin/inforBatch.jsp").forward(request, response);
    }
}
*/
