package controller.admin.orders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.client.OrderDAO;
import model.Order;
import model.OrderDetail;

@WebServlet("/AcceptControll")
public class AcceptBillControll extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		String id = request.getParameter("id");
		int oid = Integer.parseInt(id);
		System.out.println(oid);

		HttpSession session = request.getSession();

		String orderStatus = "Đơn hàng đã được xác nhận và chờ đóng gói";
		OrderDAO.updateOrderStatus(oid, orderStatus);

		response.sendRedirect(request.getContextPath()+"/BillControll");

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

