package model.dao;

import model.License;
import util.DataBaseUtil;

import java.sql.*;

public class LicenseDao {

    /*
    * 新增车牌
    * @param username
    * @param licenseNumber
    * return boolean
    * */
    public boolean addLicense(String username,String licenseNumber){
        Connection con=DataBaseUtil.getConnection();
        String sql="insert into license (userName,licenseNumber) values (?,?)";
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,licenseNumber);
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
    * 通过用户名获取其拥有的车牌信息
    * @param username
    * @return licenses
    * */
    public License[] getLicense(String username) {
        License licenses[] = null;
        Connection con = DataBaseUtil.getConnection();
        String sql = "select * from license where userName = ?";
        try {
            //可前后回滚
            PreparedStatement ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            //获取结果集数量
            rs.last();
            int rowCount = rs.getRow();
            licenses = new License[rowCount];
            rs.beforeFirst();
            int i = 0;
            //把数据导入license
            while (rs.next()) {
                licenses[i] = new License();
                licenses[i].setId(rs.getInt("id"));
                licenses[i].setUserName(rs.getString("userName"));
                licenses[i].setLicenseNumber(rs.getString("licenseNumber"));
                i++;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseUtil.closeConnection(con);
        }
        return licenses;
    }
}
