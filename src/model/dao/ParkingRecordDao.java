package model.dao;


import model.ParkingRecord;
import util.DataBaseUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;

public class ParkingRecordDao {

    /*
    * 获取指定时间段内的收入
    * @param fromTime Date
    * @param toTime  Date
    * return double
    * */
    public double getPeriodIncome(String fromTime,String toTime){
        double income=0;
        Connection con=DataBaseUtil.getConnection();
        String sql="select cost from parking_record WHERE unix_timestamp(enterTime) >= unix_timestamp(?) AND " +
                "unix_timestamp(outTime) <= unix_timestamp(?)";
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,fromTime);
            ps.setString(2,toTime);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                income+=rs.getDouble("cost");
            }
            BigDecimal b = new BigDecimal(income);
            income = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseUtil.closeConnection(con);
        }
        return income;
    }



    /*
    * 插入一行新数据
    * @param licenseId
    * @param seatId
    * @return boolean
    * */
    public boolean insertRow(int licenseId,int seatId){
        Connection con= DataBaseUtil.getConnection();
        String sql="insert into parking_record (license_id,enterTime,seatId) values (?,?,?)";
        try {
            PreparedStatement ps= con.prepareStatement(sql);
            Date date=new Date();
            Timestamp enterTime=new Timestamp(date.getTime());
            ps.setInt(1,licenseId);
            ps.setTimestamp(2,enterTime);
            ps.setInt(3,seatId);
            int rs=ps.executeUpdate();
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
    * 通过recordId查询对应parking_record的行的数据
    * @param recordId
    * @return ParkingRecord
    * */
    public ParkingRecord selectRecord(int recordId){
        ParkingRecord record=null;
        Connection con =DataBaseUtil.getConnection();
        String sql="select * from parking_record where id = ?";
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setInt(1,recordId);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                record=new ParkingRecord();
                record.setId(rs.getInt("id"));
                record.setLicenseId(rs.getInt("license_id"));
                record.setEnterTime(rs.getTimestamp("enterTime"));
                record.setSeatId(rs.getInt("seatId"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseUtil.closeConnection(con);
        }
        return record;
    }

    /*
    * 完成交易操作，修改user.money,seat.status
    * 插入cost,outTime,修改isFinished
    * @param recordId
    * @param cost
    * @return boolean
    * */
    public boolean finishDeal(int recordId,double cost){
        Connection con=DataBaseUtil.getConnection();
        String sql="update parking_record set cost = ? ,outTime = ? ,isFinished = ? where id = ?";
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setDouble(1,cost);
            //获取当前时间
            Timestamp outTime=new Timestamp(new Date().getTime());
            ps.setTimestamp(2,outTime);
            ps.setInt(3,1);
            ps.setInt(4,recordId);
            int rs=ps.executeUpdate();
            if(rs<=0){
                return false;  //交易失败
            }
            //通过recordId查询licenseId
            sql="SELECT license_id,seatId FROM parking_record WHERE id = ?";
            ps=con.prepareStatement(sql);
            ps.setInt(1,recordId);
            ResultSet resultSet=ps.executeQuery();
            int licenseId=0;
            int seatId=-1;
            if(resultSet.next()){
                licenseId=resultSet.getInt("license_id");
                seatId=resultSet.getInt("seatId");
            }else {
                return false;
            }
            //通过licenseId查询username
            sql="select userName FROM license WHERE id = ?";
            ps=con.prepareStatement(sql);
            ps.setInt(1,licenseId);
            resultSet=ps.executeQuery();
            String username;
            if(resultSet.next()){
                username=resultSet.getString("userName");
            }else {
                return false;
            }

            //从user.money中扣除费用
            sql="UPDATE user SET money = (money - ?) WHERE userName = ?";
            ps=con.prepareStatement(sql);
            ps.setDouble(1,cost);
            ps.setString(2,username);
            rs=ps.executeUpdate();
            if(rs<1){
                return false;
            }
            //修改seat.status状态为0
            if(seatId==-1){return false;}
            sql="UPDATE parking_seat SET status = 0 WHERE id = ?";
            ps=con.prepareStatement(sql);
            ps.setInt(1,seatId);
            rs=ps.executeUpdate();
            if(rs>0){
                resultSet.close();
                ps.close();
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseUtil.closeConnection(con);
        }

        return false;  //交易失败
    }

    /*
   * 通过licenseId,isFinished查询对应parking_record的行的数据集合
   * @param licenseId
   * @param isFinished
   * @return ParkingRecord[]
   * */
    public ParkingRecord[] selectByLicenseId(int licenseId,int isFinished) {
        ParkingRecord record[] = null;
        Connection con =DataBaseUtil.getConnection();
        String sql="select * from parking_record where license_id = ? and isFinished = ?";
        try {
            PreparedStatement ps=con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ps.setInt(1,licenseId);
            ps.setInt(2,isFinished);
            ResultSet rs=ps.executeQuery();
            //获取结果集数目
            rs.last();
            int count = rs.getRow();
            rs.beforeFirst();
            if(count<1){
                return record;
            }
            record=new ParkingRecord[count];
            int i=0;
            while(rs.next()){
                record[i]=new ParkingRecord();
                record[i].setId(rs.getInt("id"));
                record[i].setLicenseId(rs.getInt("license_id"));
                record[i].setEnterTime(rs.getTimestamp("enterTime"));
                record[i].setOutTime(rs.getTimestamp("outTime"));
                record[i].setCost(rs.getDouble("cost"));
                record[i].setIsFinished(rs.getInt("isFinished"));
                record[i].setSeatId(rs.getInt("seatId"));
                i++;
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseUtil.closeConnection(con);
        }

        return record;
    }
}
