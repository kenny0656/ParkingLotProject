package servlet;

import model.dao.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/*
* 用户充值
* @param username
* @param money
* @return message
* */
@WebServlet(name = "RechargeServlet",value = "/RechargeServlet")
public class RechargeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out =response.getWriter();
        String username=request.getParameter("username");  //用户名
        String strMoney=request.getParameter("money");  //充值的金额
        double money=Double.parseDouble(strMoney);
        UserDao userDao=new UserDao();
        boolean rs = userDao.recharge(username,money);
        if(rs){  //充值成功
            out.print("充值成功！");
        }else {   //充值失败
            out.print("充值失败！");
        }
        out.flush();
        out.close();
    }
}
