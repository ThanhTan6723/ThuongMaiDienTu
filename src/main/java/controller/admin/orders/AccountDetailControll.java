package controller.admin.orders;

import com.google.gson.Gson;
import dao.client.AccountDAO;
import model.Account;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

import com.google.gson.Gson;
import dao.client.AccountDAO;
import model.Account;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "AccountDetailControll", value = "/AccountDetailControll")
public class AccountDetailControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String id = request.getParameter("id");
        if (id != null && !id.isEmpty()) { // Check if id is not null and not empty
            Account acc = AccountDAO.getAccountById(Integer.parseInt(id));
            if (acc != null) {
                // Convert the account object to JSON and send it as the response
                String json = new Gson().toJson(acc);
                response.getWriter().write(json);
            } else {
                // If account with the specified id is not found, return a not found response
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Account not found with id: " + id);
            }
        } else {
            // Handle the case when id is null or empty
            // For example, you can return an error response or redirect the user
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID parameter is missing or empty");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
