package servlet;

import model.dao.BookingSeatDao;
import model.dao.ParkingRecordDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/*
* "放行进入" 处理
* 前台ajax传入licenseId，在数据表parking_record中插入一行数据，通过触发器删除booking_seat对应数据
* 返回message
* */
@WebServlet(name = "PassBookingServlet",value = "/PassBookingServlet")
public class PassBookingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        String strLicenseId=request.getParameter("licenseId");  //获取licenseId
        if(strLicenseId==null||"".equals(strLicenseId)){
            return;
        }
        int licenseId=Integer.parseInt(strLicenseId);
        BookingSeatDao bookingSeatDao=new BookingSeatDao(); //获取seatId
        int seatId=bookingSeatDao.isBooked(licenseId);
        //向parking_record插入数据  license_id,seatId
        ParkingRecordDao parkingRecordDao=new ParkingRecordDao();
        boolean rs=parkingRecordDao.insertRow(licenseId,seatId);
        PrintWriter out=response.getWriter();
        if(rs){
            out.print("操作成功！");
        }else {
            out.print("操作失败！");
        }
        out.flush();
        out.close();


    }
}
