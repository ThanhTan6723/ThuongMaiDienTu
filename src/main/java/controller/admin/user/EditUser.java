package controller.admin.user;

import dao.admin.AccountsDAO;
import dao.client.AccountDAO;
import model.Account;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "EditUser", value = "/EditUser")
public class EditUser extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                   request.setCharacterEncoding("utf-8");
                   response.setContentType("test/html;charset = utf-8");
                   String ids = request.getParameter("id");
                   int id = Integer.parseInt(ids);
                   Account account = AccountDAO.getAccountById(id);
                   request.setAttribute("account",account);
                   request.getRequestDispatcher("WEB-INF/admin/edit-user.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("test/html;charset = utf-8");
        String id = request.getParameter("account-id");
                 String name = request.getParameter("account-name");
                 String password = request.getParameter("account-password");
                 String email = request.getParameter("account-email");
                 String phone = request.getParameter("account-phone");
                 Account account = new Account();
                 account.setId(Integer.parseInt(id));
                 account.setName(name);
                 account.setEmail(email);
                 account.setPassword(password);
                 account.setTelephone(phone);
                 System.out.println(account);
                 AccountsDAO.updateAccount(account);
                 response.sendRedirect("./LoadUserPage");
    }
}