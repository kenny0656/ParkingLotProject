package model.dao;

import util.DataBaseUtil;

import java.sql.*;

import model.ParkingSeat;
public class ParkingSeatDao {

    /*
    * 获取所有车位信息   0可使用，1已被预定，2已被占用
    * @return ParkingSeat[]
    * */
    public ParkingSeat[] getAllSeat(){
        ParkingSeat seats[]=null;
        Connection con=DataBaseUtil.getConnection();
        String sql="select * from parking_seat";   //查询所有车位信息
        String sql1="select count(*) record from parking_seat"; //查询车位总数
        int rowCount=0;
        try {
            Statement sm=con.createStatement();
            ResultSet rs=sm.executeQuery(sql1);
            if(rs.next()){    //获取车位总数
                rowCount=rs.getInt("record");
            }
            seats=new ParkingSeat[rowCount];
            rs=sm.executeQuery(sql);
            int i=0;
            while (rs.next()){   //获取车位信息
                seats[i]=new ParkingSeat();
                seats[i].setId(rs.getInt("id"));
                seats[i].setNumber(rs.getString("number"));
                seats[i].setStatus(rs.getInt("status"));
                i++;
            }
            rs.close();
            sm.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseUtil.closeConnection(con);
        }
        return seats;
    }

    /*
    * 判断车位是否可用
    * @param seatId
    * @return boolean
    * */
    public boolean isAvail(int seatId){
        Connection con=DataBaseUtil.getConnection();
        String sql="select * from parking_seat where id = ? and status = ?";
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setInt(1,seatId);
            ps.setInt(2,0);
            ResultSet rs=ps.executeQuery();
            //如果存在
            if(rs.next()){
                return true;  //车位可用
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseUtil.closeConnection(con);
        }

        return false;//车位不可用
    }
    /*
    * 修改车位状态
    * @param int status, int id
    * @return boolean
    * */
    public boolean changeStatus(int id,int status){
        Connection con=DataBaseUtil.getConnection();
        String sql="update parking_seat set status = ? where id = ?";
        PreparedStatement ps= null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1,status);
            ps.setInt(2,id);
            int rs=ps.executeUpdate();
            ps.close();
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

}
