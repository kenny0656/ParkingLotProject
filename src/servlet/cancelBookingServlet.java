package servlet;

import model.dao.BookingSeatDao;
import model.dao.ParkingSeatDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by admin on 2017/6/30.
 */
@WebServlet(name = "cancelBookingServlet",value = "/cancelBookingServlet")
public class cancelBookingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }
    /*
    * 取消预订车位
    * 接受前台ajax传来的licenseId,删除booking_seat相应的行
    * */
    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out=response.getWriter();
        int licenseId=Integer.parseInt(request.getParameter("licenseId"));
        BookingSeatDao bookingSeatDao=new BookingSeatDao();
        boolean result=bookingSeatDao.cancelBooking(licenseId);
        if(result){
            out.print("取消成功！");
        }else {
            out.print("取消失败！");
        }
        out.flush();
        out.close();
    }
}
