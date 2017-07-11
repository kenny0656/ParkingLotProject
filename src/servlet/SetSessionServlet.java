package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by admin on 2017/6/29.
 */
@WebServlet(name = "SetSessionServlet",value = "/SetSessionServlet")
public class SetSessionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }
    private void process(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/html;charset=utf-8");
        String name=request.getParameter("name");
        String value=request.getParameter("value");
        HttpSession session=request.getSession();
        session.setAttribute(name,value);
        request.getSession().setMaxInactiveInterval(3600);
    }
}
