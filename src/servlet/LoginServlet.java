package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.User;
import model.dao.UserDao;

/**
 * Created by admin on 2017/6/26.
 */
@WebServlet(name = "LoginServlet",value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    //处理请求函数
    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        //如果用户名或密码为空，则返回
        if(username==null||username.equals("")||password==null||password.equals("")){
            request.setAttribute("message","用户名或密码不能为空！");
            request.getRequestDispatcher("login.jsp").forward(request,response);
            return;
        }else {
            //查询用户名和密码是否正确
            UserDao userdao=new UserDao();
            User user=userdao.login(username,password);
            if(user==null){     //错误，返回空值
                request.setAttribute("message","用户名或密码错误！");
                request.getRequestDispatcher("login.jsp").forward(request,response);
                return;
            }else {      //正确，返回匹配到的查询结果
            request.getSession().setAttribute("user",user);
            request.getSession().setMaxInactiveInterval(3600);
            request.getRequestDispatcher("userMainPage.jsp").forward(request,response);
            }

        }


    }
}
