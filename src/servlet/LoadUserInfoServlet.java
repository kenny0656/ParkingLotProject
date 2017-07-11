package servlet;

import model.User;
import model.dao.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/*
* 加载用户信息,返回html行数据
* @param keyword  关键字 如果keyword=all,加载所有用户信息
* return html
* */
@WebServlet(name = "LoadUserInfoServlet", value = "/LoadUserInfoServlet")
public class LoadUserInfoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            process(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        response.setContentType("test/html;charset=utf-8");
        String keyword = request.getParameter("keyword");
        PrintWriter out = response.getWriter();
        UserDao userDao = new UserDao();
        User users[] = userDao.selectByKeyword(keyword);  //查询用户信息
        if (users == null) {
            out.print("用户不存在！");
            out.flush();
            out.close();
            return;
        } else {
            //生成table   用户名	联系电话	余额	充值
            for (int i = 0; i < users.length; i++) {
                out.print("<tr>");
                out.print("<td>"+users[i].getUserName()+"</td>");
                out.print("<td>"+users[i].getPhone()+"</td>");
                out.print("<td>"+users[i].getMoney()+"</td>");
                out.print("<td><input type=\"number\" id=\"in_"+users[i].getUserName()+"\"/><button id=\"btn_"+users[i].getUserName()+"\"" +
                        " onclick=\"recharge(this)\" inputId=\"in_"+users[i].getUserName()+"\" username=\""+users[i].getUserName()+"\">确认充值</button></td>");
                out.print("</tr>");
            }
        }
        out.flush();
        out.close();

    }
}
