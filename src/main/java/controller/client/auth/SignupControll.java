package controller.client.auth;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.client.AccountDAO;
import model.Account;
import model.Encode;
import model.Role;

@WebServlet(name = "SignupControll", value = "/SignupControll")
public class SignupControll extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/client/signup.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phone");
        String password = request.getParameter("passw");
        String rePassword = request.getParameter("repassw");

        boolean checkName = name.trim().isEmpty();
        boolean checkEmail = email.trim().isEmpty();
        boolean checkPhone = phoneNumber.trim().isEmpty();
        boolean checkPass = password.trim().isEmpty();
        boolean checkRepass = rePassword.trim().isEmpty();

        if (checkName) {
            request.setAttribute("error2", "Vui lòng nhập tên");
        }
        if (checkEmail) {
            request.setAttribute("error3", "Vui lòng nhập email");
        }
        if (checkPhone) {
            request.setAttribute("error4", "Vui lòng nhập sđt");
        }
        if (checkPass) {
            request.setAttribute("error5", "Vui lòng nhập mật khẩu");
        }
        if (checkRepass) {
            request.setAttribute("error6", "Vui lòng nhập xác nhận mật khẩu");
        }
        boolean checkNameExists = AccountDAO.checkUserName(name);
        if (checkNameExists) {
            request.setAttribute("error2", "Tên đã tồn tại");
        }
        boolean checkEmailAvailable = AccountDAO.checkFieldExists("email",email);
        if (checkEmailAvailable) {
            request.setAttribute("error3", "Email đã tồn tại");
        }

        String passwordValidationResult = validatePassword(password);

        if (!passwordValidationResult.isEmpty()) {
            request.setAttribute("error5", passwordValidationResult);
            request.setAttribute("passw", password);
        }

        boolean checkRetype = password.equals(rePassword);
        if (!checkRetype) {
            request.setAttribute("error6", "Mật khẩu không trùng khớp");
            request.setAttribute("passw", password);
        }
        request.setAttribute("name", name);
        request.setAttribute("email", email);
        request.setAttribute("phone", phoneNumber);

        if (!checkName && !checkEmail && !checkPhone && !checkPass && !checkRepass &&
                !checkNameExists && !checkEmailAvailable && checkRetype && passwordValidationResult.isEmpty()) {

            String enpass = Encode.toSHA1(password);

            Account account = new Account();
            account.setName(name);
            account.setPassword(enpass);
            account.setEmail(email);
            account.setTelephone(phoneNumber);
            Role role = new Role();
            role.setId(0);
            account.setRole(role);
            AccountDAO.insertAccount(account);
            response.sendRedirect(request.getContextPath() + "/LoginControll");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/client/signup.jsp").forward(request, response);
    }

    private String validatePassword(String password) {
        StringBuilder errorMessage = new StringBuilder();

        if (password == null || password.isEmpty()) {
            return "Mật khẩu không được để trống.<br>";
        }

        if (password.length() < 8) {
            errorMessage.append("Mật khẩu phải chứa ít nhất 8 kí tự.<br>");
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else {
                String specialCharacters = "!@#$%^&*()_+-=[]{}|;:,.<>?";
                if (specialCharacters.contains(String.valueOf(c))) {
                    hasSpecialChar = true;
                }
            }
        }

        if (!hasUppercase) {
            errorMessage.append("Mật khẩu phải chứa ít nhất một chữ cái viết hoa.<br>");
        }
        if (!hasLowercase) {
            errorMessage.append("Mật khẩu phải chứa ít nhất một chữ cái viết thường.<br>");
        }
        if (!hasDigit) {
            errorMessage.append("Mật khẩu phải chứa ít nhất một số.<br>");
        }
        if (!hasSpecialChar) {
            errorMessage.append("Mật khẩu phải chứa ít nhất một kí tự đặc biệt.<br>");
        }

        return errorMessage.toString();
    }
}
