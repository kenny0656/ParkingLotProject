package model.dao;

import util.DataBaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by admin on 2017/7/1.
 */
public class ManagerDao {
    /*
    * 验证登录信息
    * @param String username
    * @param String password
    * @return boolean  true-信息正确  false--信息错误
    * */
    public boolean loginCheck(String username,String password){
        Connection con= DataBaseUtil.getConnection();
        String sql="select * from manager where userName = ? and password = ?";
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,password);
            ResultSet rs=ps.executeQuery();
            //如果查询存在
            if(rs.next()){
                return true;   //返回true
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseUtil.closeConnection(con);
        }

        return false;
    }
}
