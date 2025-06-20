package controller.client.show;

import dao.client.IndexDAO;
import dao.client.ProductDAO;
import dao.client.VerifyDAO;
import model.Account;
import model.Product;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "IndexControll", value = "/IndexControll")
public class IndexControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        List<Product> listTop = IndexDAO.getTop8();
        List<Product> list4 = IndexDAO.listRand4Product();

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account != null) {
            String publicKey = null;
            try {
                publicKey = VerifyDAO.getPublicKey(account.getId());
            } catch (Exception e) {
            }
            System.out.println("public key: "+publicKey);
            if(publicKey != null) {
                request.setAttribute("hasKey", publicKey);
            }
        }

        request.setAttribute("list4Rand", list4);
        request.setAttribute("listTop", listTop);
        request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    }

    @Override

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
