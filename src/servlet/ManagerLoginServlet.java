package servlet;

import model.dao.ManagerDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 管理员登录处理
 */
@WebServlet(name = "ManagerLoginServlet",value = "/ManagerLoginServlet")
public class ManagerLoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    /*
    * 验证管理员用户名和密码
    * */
    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        if(username==null||"".equals(username)||password==null||"".equals(password)){
            request.setAttribute("message","用户名或密码不能为空！");
            request.getRequestDispatcher("managerLogin.jsp").forward(request,response);
            return;
        }
        ManagerDao managerDao=new ManagerDao();
        //查询结果
        boolean result= managerDao.loginCheck(username,password);
        if(result){
            //username存入session
            request.getSession().setAttribute("manager",username);
            request.getSession().setMaxInactiveInterval(36000);
            request.getRequestDispatcher("nav.jsp").forward(request,response);
        }else {
            request.setAttribute("message","用户名或密码错误！");
            request.getRequestDispatcher("managerLogin.jsp").forward(request,response);
        }

    }
}
