package controller.client.voucher;

import dao.client.VoucherDAO;
import model.Account;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "SaveVoucherControll", value = "/SaveVoucherControll")
public class SaveVoucherControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        if (account != null) {
            int voucherId = Integer.parseInt(request.getParameter("voucherId"));
            int accountId = account.getId();

            VoucherDAO eVoucherDAO = new VoucherDAO();
            boolean success = eVoucherDAO.saveEVoucher(accountId, voucherId);

            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            if (success) {
                out.print("success");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("fail");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
