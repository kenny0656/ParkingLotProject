package model.dao;

import model.BookingSeat_License;
import util.DataBaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by admin on 2017/7/2.
 */
public class BookingSeat_LicenseDao {
    /*
    * 多表查询booking_seat和licnese
    * 返回 内连接 结果集
    *@return Bookingseat_License BL[]
    * */
    public BookingSeat_License[] getAllData(){
        BookingSeat_License BL[]=null;
        Connection con= DataBaseUtil.getConnection();
        String sql="select b.id as 'bookingSeatId',b.seatId as 'seatId',b.startTime as 'startTime'," +
                "b.endTime as 'endTime',l.id as 'licenseId',l.userName as 'userName'," +
                "l.licenseNumber as 'licenseNumber' from booking_seat as b left join license as l on b.licenseId = l.id";
        try {
            PreparedStatement ps=con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ResultSet rs=ps.executeQuery();
            rs.last();
            int count = rs.getRow();  //数据行数
            rs.beforeFirst();   //回到第一条记录前
            BL=new BookingSeat_License[count];
            int i=0;
            while(rs.next()){
                BL[i]=new BookingSeat_License();
                BL[i].setBookingSeatId(rs.getInt("bookingSeatId"));
                BL[i].setSeatId(rs.getInt("seatId"));
                BL[i].setStartTime(rs.getTimestamp("startTime"));
                BL[i].setEndTime(rs.getTimestamp("endTime"));
                BL[i].setUserName(rs.getString("userName"));
                BL[i].setLicenseNumber(rs.getString("licenseNumber"));
                BL[i].setLicenseId(rs.getInt("licenseId"));
                i++;
            }
            rs.close();
            ps.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseUtil.closeConnection(con);
        }
        return BL;
    }
}
