package controller.client.auth;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.client.AccountDAO;
import model.Account;

@WebServlet("/UpdateProfileControll")
public class UpdateProfileControll extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        request.setAttribute("name", account.getName());
        request.setAttribute("email", account.getEmail());
        request.setAttribute("phone", account.getTelephone());

        request.getRequestDispatcher("/WEB-INF/client/profile.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account != null) {
            request.setAttribute("name", account.getName());
            request.setAttribute("email", account.getEmail());
            request.setAttribute("phone", account.getTelephone());

            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phoneNumber = request.getParameter("phone");

            boolean checkName = name.trim().isEmpty();
            boolean checkEmail = email.trim().isEmpty();
            boolean checkPhone = phoneNumber.trim().isEmpty();

            if (name == null || checkName) {
                request.setAttribute("error2", "Enter your name");
            }
            if (email == null || checkEmail) {
                request.setAttribute("error3", "Enter your email");
            }
            if (phoneNumber == null || checkPhone) {
                request.setAttribute("error4", "Enter your phone number");
            }
            boolean checkNameExits = AccountDAO.checkUserName(name);
            if (checkNameExits) {
                request.setAttribute("error2", "Name is already exits");
            }
            boolean checkEmailAvailable = AccountDAO.checkFieldExists("email", email);
            if (checkEmailAvailable) {
                request.setAttribute("error3", "Email available");
            }

            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("phone", phoneNumber);

            if (name != null && email != null && phoneNumber != null && !checkName && !checkEmail && !checkPhone
                    && !checkNameExits && !checkEmailAvailable) {

                account.setName(name);
                account.setEmail(email);
                account.setTelephone(phoneNumber);
                System.out.println(account);
                AccountDAO.updateProfile(account);
                response.sendRedirect(request.getContextPath() + "/IndexControll");
                return;
            }

            request.getRequestDispatcher("/WEB-INF/client/profile.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/WEB-INF/client/login.jsp").forward(request, response);
        }
    }

}
