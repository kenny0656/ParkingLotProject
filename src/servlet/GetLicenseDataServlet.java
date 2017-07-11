package servlet;

import model.License;
import model.ParkingRecord;
import model.dao.BookingSeatDao;
import model.dao.LicenseDao;
import model.dao.ParkingRecordDao;
import util.DataBaseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by admin on 2017/6/29.
 */
@WebServlet(name = "GetLicenseDataServlet",value = "/GetLicenseDataServlet")
public class GetLicenseDataServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }
    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        String username=request.getParameter("username");
        //如果用户未登录，则跳转到登录界面
        if(username==null||"".equals(username)){
            request.getRequestDispatcher("login.jsp");
            return;
        }
        //获取用户拥有的车牌
        LicenseDao licenseDao=new LicenseDao();
        License licenses[]=licenseDao.getLicense(username);
        //如果licenses=null,后台提示
        if(licenses==null){
            System.out.println("license=null");
            return;
        }
        //用于查询licenseId是否已预订和正在使用车位，生成不同的button
        BookingSeatDao bookingSeatDao=new BookingSeatDao();
        ParkingRecordDao parkingRecordDao=new ParkingRecordDao();
        //生成html行，返回车牌信息
        StringBuffer stringBuffer=new StringBuffer("");
        for(int i=0;i<licenses.length;i++){
            stringBuffer.append("<tr>");
            stringBuffer.append("<td>"+licenses[i].getLicenseNumber()+"</td>");
            //如果已预订
            int seatId=bookingSeatDao.isBooked(licenses[i].getId());
            if(seatId>0){
                stringBuffer.append("<td><button id=\""+licenses[i].getId()+"\" onclick=\"clickCancel(this)\">取消预订</button><label>&nbsp;&nbsp;预订的车位号:"+seatId+"</label></td>");
            }else {   //查询是否正在使用车位
                ParkingRecord record[]=parkingRecordDao.selectByLicenseId(licenses[i].getId(),0);
                if(record==null){  //未使用车位
                    stringBuffer.append("<td><button id=\""+licenses[i].getId()+"\" onclick=\"clickBooking(this)\">申请预定</button></td>");
                }else {   //正使用车位
                    long to=new Date().getTime();
                    long from=record[0].getEnterTime().getTime();
                    double cost =(double)(to-from)/(1000*60*60)* DataBaseUtil.price;
                    BigDecimal b=new BigDecimal(cost);  //四舍五入
                    cost=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                    stringBuffer.append("<td>正在使用车位号:"+record[0].getSeatId()+"&nbsp;&nbsp;费用:"+cost+"</td>");

                }

            }

            stringBuffer.append("</tr>");
        }
        PrintWriter out=response.getWriter();
        out.print(stringBuffer.toString());
        out.flush();
        out.close();
    }
}
