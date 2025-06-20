package controller.admin.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.admin.AccountsDAO;
import dao.client.AccountDAO;
import model.Account;

@WebServlet("/DeleteUserControll")
public class DeleteUserControll extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String id = request.getParameter("id");
		System.out.println("ma nguoi dung:"+id);
		Account account = AccountDAO.getAccountById(Integer.parseInt(id));
		System.out.println(account);
		AccountsDAO.removeAccount(Integer.parseInt(id));
		request.getRequestDispatcher("WEB-INF/admin/user.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
