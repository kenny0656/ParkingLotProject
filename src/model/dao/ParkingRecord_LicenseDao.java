package model.dao;

import model.ParkingRecord_License;
import util.DataBaseUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by admin on 2017/7/2.
 */
public class ParkingRecord_LicenseDao {
    /*
    * 查询正在使用的车位信息  内连接查询 parking_record和license
    * @return ParkingRecord_LicenseDao[]
    * */
    public ParkingRecord_License[] getUsingData(){
        ParkingRecord_License []pL=null;
        Connection con= DataBaseUtil.getConnection();
        //recordId  licenseNumber   userName  seatId  enterTime
        String sql="select p.id as recordId,l.licenseNumber as licenseNumber,l.userName as userName,p.seatId as seatId,p.enterTime as enterTime" +
                " from parking_record AS p left JOIN license as l ON p.license_id = l.id WHERE p.isFinished =  0";
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            //求结果集总数
            rs.last();
            int count=rs.getRow();
            rs.beforeFirst();
            pL=new ParkingRecord_License[count];
            //获取当前时间
            Date date=new Date();
            long to=date.getTime();
            //赋值pL
            int i=0;
            while(rs.next()){
                pL[i]=new ParkingRecord_License();
                pL[i].setRecordId(rs.getInt("recordId"));
                pL[i].setLicenseNumber(rs.getString("licenseNumber"));
                pL[i].setUserName(rs.getString("userName"));
                pL[i].setEnterTime(rs.getTimestamp("enterTime"));
                pL[i].setSeatId(rs.getInt("seatId"));
                //计算当前费用
                long from=pL[i].getEnterTime().getTime();
                double cost=(double) (to-from)/(1000*60*60)*DataBaseUtil.price;
                BigDecimal temp=new BigDecimal(cost);
                cost=temp.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();//结果四舍五入
                pL[i].setCost(cost);
                i++;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseUtil.closeConnection(con);
        }

        return pL;
    }
}
