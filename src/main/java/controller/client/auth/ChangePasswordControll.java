package controller.client.auth;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.client.AccountDAO;
import model.Account;
import model.Encode;

@WebServlet("/ChangePassword")
public class ChangePasswordControll extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getRequestDispatcher("/WEB-INF/client/change-password.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		HttpSession session = request.getSession();
		Account account = (Account) session.getAttribute("account");
		if(account!=null) {

			String oldPass = request.getParameter("oldpass");
			String newPass = request.getParameter("newpass");
			String cfPass = request.getParameter("repass");

			String errorOP = validatePassword(oldPass, "Enter password");
			String errorNP = validatePassword(newPass, "Enter new pass");
			String errorCFP = validatePassword(cfPass, "Enter confirm pass");

			String err = errorOP + errorNP + errorCFP;

			if (err.isEmpty()) {
				String enOldPass = Encode.toSHA1(oldPass);
				String enNewPass = Encode.toSHA1(newPass);
				boolean checkPass = account.getPassword().equals(enOldPass);
				System.out.println(checkPass);
				if (checkPass == true) {
					if (!enOldPass.equals(enNewPass) && newPass.equals(cfPass)) {
						account.setPassword(enNewPass);
						AccountDAO accDao = new AccountDAO();
						int re = accDao.update(account);
						session.setAttribute("account", account);
						System.out.println(re);
						response.sendRedirect(request.getContextPath() + "/ShowProductControl");
						return;
					}
				} else
					request.setAttribute("errorOP", "Password incorrect");
			} else {

				request.setAttribute("errorOP", errorOP);
				request.setAttribute("errorNP", errorNP);
				request.setAttribute("errorCFP", errorCFP);
			}
			request.getRequestDispatcher("/WEB-INF/client/change-password.jsp").forward(request, response);
		}else {
			request.getRequestDispatcher("/WEB-INF/client/login.jsp").forward(request, response);
		}
	}

	private String validatePassword(String password, String errorMessage) {
		if (password == null || password.trim().isEmpty()) {
			return errorMessage;
		}
		return "";
	}

}
