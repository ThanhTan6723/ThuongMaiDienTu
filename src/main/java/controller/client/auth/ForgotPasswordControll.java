package controller.client.auth;

import java.io.IOException;
import java.time.Instant;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.client.AccountDAO;
import model.Account;
import model.Email;

@WebServlet("/ForgotControll")
public class ForgotPasswordControll extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static Random random = new Random();

    public static int generateRandomCode() {
        return random.nextInt(9000) + 1000;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String email = request.getParameter("email");

        if (email == null || email.trim().isEmpty()) {
            // Nếu không có email được cung cấp, không cần set lỗi ở đây.
            // Để form hiển thị trang `forgot-password.jsp` tự xử lý thông báo lỗi.
            request.getRequestDispatcher("/WEB-INF/client/forgot-password.jsp").forward(request, response);
            return;
        }

        boolean checkMail = AccountDAO.checkFieldExists("email", email);

        if (!checkMail) {
            request.setAttribute("error", "Email không tồn tại");
            request.getRequestDispatcher("/WEB-INF/client/forgot-password.jsp").forward(request, response);
            return;
        }

        int randomCode = generateRandomCode();
        String code = String.valueOf(randomCode);
        System.out.println("Code: " + code);
        Email.sendEmail(email, "Xác thực", code);
        request.getSession().setAttribute("email", email);
        request.getSession().setAttribute("randomCode", randomCode);
        request.getSession().setAttribute("codeGeneratedTime", Instant.now());
        response.sendRedirect(request.getContextPath() + "/VerifyControll");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
