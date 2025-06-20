package controller.admin.orders;

import model.Account;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "LoadBillControll", value = "/LoadBillControll")
public class LoadBillControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account != null) {
            if (account.getRole().getId() == 3 || account.getRole().getId() == 1) {
                request.getRequestDispatcher("WEB-INF/admin/bill.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/IndexControll");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/LoginControll");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}