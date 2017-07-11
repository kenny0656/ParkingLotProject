package servlet;

import model.ParkingRecord_License;
import model.dao.ParkingRecord_LicenseDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

@WebServlet(name = "GetUsingDataServlet",value = "/GetUsingDataServlet")
public class GetUsingDataServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    /*
    * 生成正在使用的车位信息 的表格行
    * 前台通过ajax获得返回的结果
    * 车牌 用户名 正在使用的车位 进入时间  当前费用 操作(离开并结算)
    * */
    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        //获取数据
        ParkingRecord_LicenseDao parkingRecord_licenseDao=new ParkingRecord_LicenseDao();
        ParkingRecord_License[] pL=parkingRecord_licenseDao.getUsingData();
        PrintWriter out=response.getWriter();
        //生成列名
        out.print("<tr><th>车牌</th><th>用户名</th><th>使用的车位</th><th>进入时间</th><th>当前费用(元)</th><th>操作</th></tr>");
        //生成行数据
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i=0;i<pL.length;i++){
            out.print("<tr>");
            out.print("<td>"+pL[i].getLicenseNumber()+"</td>");
            out.print("<td>"+pL[i].getUserName()+"</td>");
            out.print("<td>"+pL[i].getSeatId()+"</td>");
            //转换时间格式,去除无效尾数
            String enterTime=dateFormat.format(pL[i].getEnterTime());
            out.print("<td>"+enterTime+"</td>");
            out.print("<td>"+pL[i].getCost()+"</td>");
            out.print("<td><button id=\"recordId_"+pL[i].getRecordId()+"\" recordId=\""+pL[i].getRecordId()+"\" username=\""+pL[i].getUserName()+"\" onclick=\"finishDeal(this)\">放行并结算</button></td>");
            out.print("</tr>");
        }
        out.flush();
        out.close();
    }
}
