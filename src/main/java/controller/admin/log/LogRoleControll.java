package controller.admin.log;

import com.google.gson.Gson;
import dao.client.LogDAO;
import model.Log;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "LogRoleControll", value = "/LogRoleControll")
public class LogRoleControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String type = request.getParameter("type");
        List<Log> logs = LogDAO.getLogsByActionType("UPDATE ROLE");

        System.out.println(logs);
        String json = new Gson().toJson(logs);
        response.getWriter().write(json);
    }
}