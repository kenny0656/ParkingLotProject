package servlet;

import model.dao.ParkingRecordDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by admin on 2017/7/3.
 */
@WebServlet(name = "GetPeriodIncomeServlet",value = "/GetPeriodIncomeServlet")
public class GetPeriodIncomeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out=response.getWriter();
        String fromTime=request.getParameter("fromTime");
        String toTime=request.getParameter("toTime")+"-23-59-59";
        ParkingRecordDao parkingRecordDao=new ParkingRecordDao();
        double income=parkingRecordDao.getPeriodIncome(fromTime,toTime);
        out.print(income);
        out.flush();
        out.close();
    }
}
