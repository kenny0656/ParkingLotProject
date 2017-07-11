package servlet;

import model.ParkingRecord;
import model.dao.ParkingRecordDao;
import model.dao.UserDao;
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

/*
* "放行并结算"处理
* 前台ajax传入recordId,username
* 计算当前费用——检查用户账户金额是否足以付款——付款/返回余额不足提示
* */
@WebServlet(name = "finishDealServlet",value = "/finishDealServlet")
public class finishDealServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out=response.getWriter();
        String strRecordId=request.getParameter("recordId");
        String username=request.getParameter("username");
        //检查数据正确性
        if(strRecordId==null||"".equals(strRecordId)||username==null||"".equals(username)){
            out.print("数据不完整！");
            out.flush();
            out.close();
            return;
        }
        int recordId=Integer.parseInt(strRecordId);
        //获取recordId的record
        ParkingRecordDao parkingRecordDao=new ParkingRecordDao();
        ParkingRecord record=parkingRecordDao.selectRecord(recordId);
        if(record==null){return;}
        //计算费用
        long from=record.getEnterTime().getTime();
        long to=new Date().getTime();
        double cost=(double)(to-from)/(1000*60*60)* DataBaseUtil.price;
        BigDecimal b=new BigDecimal(cost);
        cost=b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        //获取用户账户金额
        UserDao userDao=new UserDao();
        double money=userDao.selectMoney(username);

        //判断余额是否充足
        if(money<cost){  //余额不足
        out.print("账户余额不足！");
        out.flush();
        out.close();
        return;
        }else {      //余额充足,扣除费用，结束交易
            ParkingRecordDao parkingRecordDao1=new ParkingRecordDao();
            boolean rs1=parkingRecordDao.finishDeal(recordId,cost);  //更新记录
            if(!rs1){   //更新失败
                out.print("操作失败！");
                out.flush();
                out.close();
                return;
            }
            out.print("操作成功！");
            out.flush();
            out.close();

        }

    }
}
