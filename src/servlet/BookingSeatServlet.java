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

@WebServlet(name = "BookingSeatServlet",value = "/BookingSeatServlet")
public class BookingSeatServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    process(request,response);
    }
    /*
    * 获取前台ajax传递的seatID,检查车位是否可用
    * 如果车位可用,则进行预订操作，返回message
    * 如果不可用，则直接返回message
    * */
    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out=response.getWriter();
        String strSeatID=request.getParameter("seatID");
        //判断seatID是否正确
        if(strSeatID==null||"".equals(strSeatID)){
            System.out.println("seatID=null !");
            return;
        }
        int seatId=Integer.parseInt(strSeatID);
        int licenseId=Integer.parseInt((String) request.getSession().getAttribute("licenseID"));
        //判断seatID是否已可用
        ParkingSeatDao seatDao=new ParkingSeatDao();
        //如果可用，进行预订  修改booking_seat,parking_seat
        if(seatDao.isAvail(seatId)){
            BookingSeatDao bookingSeatDao=new BookingSeatDao();
            boolean rs1=bookingSeatDao.insertData(seatId,licenseId);
            if(rs1){
                out.print("成功预订！");
            }else {
                out.print("预订失败！");
            }
        }else {
            out.print("车位不可用！");
        }
        out.flush();
        out.close();
    }
}
