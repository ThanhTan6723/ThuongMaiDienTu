package controller.admin.orders;

import controller.client.order.APIGetShippingFee;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ShippingCalculateControll", value = "/ShippingCalculateControll")
public class ShippingCalculateControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Không cần xử lý phương thức GET trong trường hợp này
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Thiết lập mã hóa ký tự và kiểu nội dung
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        // Lấy thông tin từ request
        String city = request.getParameter("city");
        String district = request.getParameter("district");
        String ward = request.getParameter("ward");
        String street = request.getParameter("address");
        String totalParam = request.getParameter("total");
        String totalWeightParam = request.getParameter("total-weight");
        System.out.println(city);
        System.out.println(district);
        System.out.println(ward);
        System.out.println(street);
        System.out.println(totalParam);
        System.out.println(totalWeightParam);

        // Kiểm tra và xử lý các giá trị không xác định
        int total = 0;
        if (totalParam != null && !totalParam.equals("") && !totalParam.equals("undefined")) {
            total = Integer.parseInt(totalParam.replace(",", ""));
        }

        int totalWeight = 0;
        if (totalWeightParam != null && !totalWeightParam.equals("") && !totalWeightParam.equals("undefined")) {
            totalWeight = Integer.parseInt(totalWeightParam);
        }

        try {
            // Gọi hàm tính phí vận chuyển từ API
            double shippingFee = APIGetShippingFee.calculateShippingFee(city, district, ward, street, totalWeight, total);

            // Gửi kết quả về cho client
            response.getWriter().write(String.valueOf(shippingFee));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
