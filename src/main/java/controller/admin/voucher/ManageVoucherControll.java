package controller.admin.voucher;

import com.google.gson.Gson;
import dao.client.VoucherDAO;
import model.Voucher;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ManageVoucherControll", value = "/ManageVoucherControll")
public class ManageVoucherControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String type = request.getParameter("type");
        List<Voucher> voucherList = null;

        if ("product".equals(type)) {
            voucherList = VoucherDAO.getVoucherByType("Product");
        } else if ("category".equals(type)) {
            voucherList = VoucherDAO.getVoucherByType("Category");
        } else if("all".equals(type)){
            voucherList = VoucherDAO.getVoucherByType("All");
        }
        String json = new Gson().toJson(voucherList);
        response.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
