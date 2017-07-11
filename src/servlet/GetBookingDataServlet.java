package servlet;

import model.BookingSeat;
import model.BookingSeat_License;
import model.dao.BookingSeatDao;
import model.dao.BookingSeat_LicenseDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

/*
* 用于生成seatManage.jsp的 已预订的车位 的列数据
*
* */
@WebServlet(name = "GetBookingDataServlet",value = "/GetBookingDataServlet")
public class GetBookingDataServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }
    /*
    * 每列：车牌 用户名 预定的车位 预订时间 操作(放行进入，取消预订)
    * */
    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        BookingSeat_LicenseDao bookingSeat_licenseDao=new BookingSeat_LicenseDao();
        BookingSeat_License bL[]=bookingSeat_licenseDao.getAllData(); //查询所有预订的车位和证件信息
        if(bL==null){
            return;
        }
        PrintWriter out=response.getWriter();
        out.print("<tr><th>车牌</th><th>用户名</th><th>预订的车位</th><th>预订时间</th><th>操作</th></tr>");
        //生成列
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=0;i<bL.length;i++){
            out.print("<tr>");
            out.print("<td>"+bL[i].getLicenseNumber()+"</td>");
            out.print("<td>"+bL[i].getUserName()+"</td>");
            out.print("<td>"+bL[i].getSeatId()+"</td>");
            //转换时间格式,去除无效尾数
            String startTime=dateFormat.format(bL[i].getStartTime());
            out.print("<td>"+startTime+"</td>");
            out.print("<td><button id=\""+bL[i].getLicenseId()+"\" onclick=\"passBooking(this)\">进入并计时</button>&nbsp;");
            out.print("<button id=\""+bL[i].getLicenseId()+"\" onclick=\"cancelBooking(this)\">取消预订</button></td>");
            out.print("</tr>");
        }

        out.flush();
        out.close();
    }
}
