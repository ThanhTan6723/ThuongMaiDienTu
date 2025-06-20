package controller.admin.log;

import com.google.gson.Gson;
import dao.client.LogDAO;
import model.Log;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "LogAuthControll", value = "/LogAuthControll")
public class LogAuthControll extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String type = request.getParameter("type");
        System.out.println(type);

        List<Log> log1 = LogDAO.getLogsByActionType("LOGIN");
        List<Log> log2 = LogDAO.getLogsByActionType("LOGIN FACEBOOK");
        List<Log> log3 = LogDAO.getLogsByActionType("LOGIN GOOGLE");
        List<Log> logs = new ArrayList<>();
        logs.addAll(log1);
        logs.addAll(log2);
        logs.addAll(log3);

        System.out.println(logs);
        String json = new Gson().toJson(logs);
        response.getWriter().write(json);
    }
}