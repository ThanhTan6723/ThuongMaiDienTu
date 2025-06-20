package controller.client.voucher;

import dao.client.VoucherDAO;
import model.Account;
import model.Voucher;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "VoucherWareHouse", value = "/VoucherWareHouse")
public class VoucherWareHouse extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        List<Voucher> voucherList = VoucherDAO.getAllVouchers();
        List<Integer> integers = VoucherDAO.getSavedVouchers(account.getId());
        List<Voucher> listSaved = new ArrayList<>();
        for(Voucher v: voucherList){
            for(Integer ev: integers){
                if(v.getId()==ev){
                    listSaved.add(v);
                }
            }
        }
        System.out.println(listSaved);
        request.setAttribute("listSaved",listSaved);
        request.getRequestDispatcher("/WEB-INF/client/voucher-warehouse.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}