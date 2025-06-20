package controller.client.auth;

import model.Email;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "VerifyControll", value = "/VerifyControll")
public class VerifyControll extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if ("resend".equals(action)) {
			resendCode(request, response);
		} else {
			verifyCode(request, response);
		}
	}

	private void verifyCode(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String num1 = request.getParameter("num1");
		String num2 = request.getParameter("num2");
		String num3 = request.getParameter("num3");
		String num4 = request.getParameter("num4");

		String sum = (num1 + num2 + num3 + num4);
		System.out.println(sum);

		String code = String.valueOf(request.getSession().getAttribute("randomCode"));
		Instant codeGeneratedTime = (Instant) request.getSession().getAttribute("codeGeneratedTime");

		if (codeGeneratedTime != null && Duration.between(codeGeneratedTime, Instant.now()).toMinutes() < 1) {
			if (sum.equals(code)) {
				System.out.println("Xác thực thành công");
				response.sendRedirect(request.getContextPath() + "/ResetPassword");
				return;
			} else {
				System.out.println("Xác thực thất bại");
				request.setAttribute("error", "Mã OTP không đúng");
			}
		} else {
			System.out.println("Mã OTP đã hết hiệu lực");
			request.setAttribute("error", "Mã OTP đã hết hiệu lực. Vui lòng yêu cầu mã mới.");
		}

		getServletContext().getRequestDispatcher("/WEB-INF/client/verify.jsp").forward(request, response);
	}

	private void resendCode(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = (String) request.getSession().getAttribute("email");
		if (email != null) {
			int randomCode = ForgotPasswordControll.generateRandomCode();
			String code = String.valueOf(randomCode);
			System.out.println("Resent Code: " + code);
			Email.sendEmail(email, "Xác thực lại", code);
			request.getSession().setAttribute("randomCode", randomCode);
			request.getSession().setAttribute("codeGeneratedTime", Instant.now());
		}
		request.setAttribute("resent", true);
		getServletContext().getRequestDispatcher("/WEB-INF/client/verify.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
