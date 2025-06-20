package controller.client.voucher;

import dao.client.VoucherDAO;
import model.Account;
import model.Voucher;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "VoucherControll", value = "/VoucherControll")
public class VoucherControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        VoucherDAO voucherDAO = new VoucherDAO();
        List<Voucher> vouchers = voucherDAO.getAllVouchers();

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        System.out.println(account);

        List<Integer> savedVoucherIds = null;
        if (account != null) {
            savedVoucherIds = voucherDAO.getSavedVouchers(account.getId());
        }

        request.setAttribute("vouchers", vouchers);
        request.setAttribute("savedVoucherIds", savedVoucherIds);
        request.getRequestDispatcher("/WEB-INF/client/voucher.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
