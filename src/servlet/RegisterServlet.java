package servlet;

import model.User;
import model.dao.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by admin on 2017/6/27.
 */
@WebServlet(name = "RegisterServlet",value = "/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       request.setCharacterEncoding("UTF-8");

        String username=request.getParameter("username");
        String password=request.getParameter("password");
        String phone=request.getParameter("phone");
        //如果个人信息空白，则提醒
        if(username==null||username.equals("")||password==null||password.equals("")||phone==null||phone.equals("")){
            request.setAttribute("message","以下信息不能为空！");
            request.getRequestDispatcher("register.jsp").forward(request,response);
            return;
        }
        //查询用户名是否可用,如果可用则保存，不可用则提醒
        UserDao userdao=new UserDao();
        if(!userdao.userIsExist(username)){
            User user=new User();
            user.setUserName(username);
            user.setPassword(password);
            user.setPhone(phone);
            userdao.saveUser(user);
            request.getSession().setAttribute("username",username);
            request.setAttribute("message","注册成功！");
            request.getRequestDispatcher("register.jsp").forward(request,response);
        }else{
            request.setAttribute("message","用户名已被使用！");
            request.getRequestDispatcher("register.jsp").forward(request,response);
            return;
        }

    }
}
