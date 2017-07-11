package model.dao;

import model.BookingSeat;
import util.DataBaseUtil;

import java.sql.*;
import java.util.Date;


public class BookingSeatDao {
/*
* 插入一行预订数据
* @param seatId
* @param licenseId
* @return boolean true-成功插入  false-插入失败
* */
    public boolean insertData(int seatId,int licenseId){
        BookingSeat bookingSeat=null;
        Connection con=DataBaseUtil.getConnection();
        String sql="insert into booking_seat (seatId,licenseId,startTime) values (?,?,?)";
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            Date date=new Date();
            Timestamp timestamp=new Timestamp(date.getTime());
            ps.setInt(1,seatId);
            ps.setInt(2,licenseId);
            ps.setTimestamp(3,timestamp);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseUtil.closeConnection(con);
        }
        return false;
    }
    /*
    * 通过licenseId查询是否已经预订车位
    * @param int licenseId
    * @return int seatId  seatId=-1未预订，seatId>0已预订
    * */
    public int isBooked(int licenseId){
        int seatId=-1;
        Connection con=DataBaseUtil.getConnection();
        String sql="select * from booking_seat where licenseId = ?";
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setInt(1,licenseId);
            ResultSet rs=ps.executeQuery();
            //如果结果集不为空，则说明已预订
            if(rs.next()){
                seatId=rs.getInt("seatId");
                return seatId;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseUtil.closeConnection(con);
        }
        return seatId;  //seatId=-1 未预订
    }

    /*
    * 取消预订，删除该行
    * @param int licneseId
    * @return boolean  ture-取消成功  false-取消失败
    * */
    public boolean cancelBooking(int licenseId){
        Connection con=DataBaseUtil.getConnection();
        String sql="delete from booking_seat where licenseId = ?";
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setInt(1,licenseId);
            int rs=ps.executeUpdate();
            //rs>0，说明删除成功
            if(rs>0){
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseUtil.closeConnection(con);
        }


        return false;
    }

    /*
    * 查询所有预订信息
    * @return BookingSeat seats[]
    * */
    public BookingSeat[] getAllSeats(){
        BookingSeat seats[]=null;
        Connection con=DataBaseUtil.getConnection();
        String sql="select * from booking_seat";
        try {
            PreparedStatement ps=con.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ResultSet rs=ps.executeQuery();
            rs.last();
            int count=rs.getRow();  //获得总个数
            seats=new BookingSeat[count];
            rs.beforeFirst();
            int i=0;
            while (rs.next()){
                seats[i]=new BookingSeat();
                seats[i].setId(rs.getInt("id"));
                seats[i].setLicenseId(rs.getInt("licenseId"));
                seats[i].setStartTime(rs.getTimestamp("startTime"));
                seats[i].setEndTime(rs.getTimestamp("endTime"));
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseUtil.closeConnection(con);
        }

        return seats;
    }

}
