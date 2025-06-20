
package controller.admin.orders;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.client.OrderDAO;
import model.Order;

@WebServlet("/RefuseBillControll")
public class RefuseBillControll extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");

		String id = request.getParameter("id");
		int oid = Integer.parseInt(id);

		HttpSession session = request.getSession();

		String orderStatus = "Đơn hàng bị từ chối";
		OrderDAO.updateOrderStatus(oid, orderStatus);

		response.sendRedirect(request.getContextPath() + "/BillControll");
//		request.getRequestDispatcher("/admin/bill.jsp").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

