package model.dao;

import model.User;
import util.DataBaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDao {
    /*
    * 用户账户充值
    * @param username  用户名
    * @param money   充值的金额
    * return boolean
    * */
    public boolean recharge(String username,double money){
        Connection con=DataBaseUtil.getConnection();
        String sql="update user set money = (money + ?) where userName = ?";
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setDouble(1,money);
            ps.setString(2,username);
            int rs=ps.executeUpdate();
            if(rs>0){    //更新成功
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseUtil.closeConnection(con);
        }
        return false;  //更新失败
    }

    /*
    * 查询用户信息
    * @param keyword  如果keyword=all,查询所有用户；否则keyword作为username来查询
    * @return User[]
    * */
    public User[] selectByKeyword(String keyword) throws SQLException {
        User users[] = null;
        Connection con = DataBaseUtil.getConnection();
        String sql_all = "select * from user";
        String sql_keyword = "select * from user where userName like ?";
        PreparedStatement ps;
        ResultSet rs;
        if (keyword.equals("all")) {  //查询所有用户
            ps = con.prepareStatement(sql_all, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            //结果集总数
            rs.last();
            int count = rs.getRow();
            rs.beforeFirst();
            if (count < 1) {  //结果集小于1，返回users=null
                rs.close();
                ps.close();
                DataBaseUtil.closeConnection(con);
                return users;
            } else {     //结果集大于等于1
                users = new User[count];
                int i = 0;
                while (rs.next()) {
                    users[i] = new User();
                    users[i].setUserName(rs.getString("userName"));
                    users[i].setPassword(rs.getString("password"));
                    users[i].setMoney(rs.getDouble("money"));
                    users[i].setPhone(rs.getString("phone"));
                    users[i].setName(rs.getString("name"));
                    i++;
                }

            }
        } else {   //keyword!=all,模糊查询keyword
            ps = con.prepareStatement(sql_keyword, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            keyword = "%" + keyword + "%";    //模糊查询字符串
            ps.setString(1, keyword);
            rs = ps.executeQuery();
            //计算结果集总数
            rs.last();
            int count = rs.getRow();
            rs.beforeFirst();
            if (count < 1) {  //结果集小于1，返回users=null
                rs.close();
                ps.close();
                DataBaseUtil.closeConnection(con);
                return users;
            }else {
                users = new User[count];
                int i=0;
                while (rs.next()){
                    users[i]=new User();
                    users[i].setUserName(rs.getString("userName"));
                    users[i].setPassword(rs.getString("password"));
                    users[i].setMoney(rs.getDouble("money"));
                    users[i].setPhone(rs.getString("phone"));
                    users[i].setName(rs.getString("name"));
                    i++;
                }
            }
        }
        rs.close();
        ps.close();
        DataBaseUtil.closeConnection(con);
        return users;
    }


    /*
    * 检查用户名是否已存在
    * @param username
    * @return true/false
    * */
    public boolean userIsExist(String username) {
        Connection con = DataBaseUtil.getConnection();
        String sql = "select * from user where userName = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            //如果用户名存在，返回true
            if (rs.next()) {
                rs.close();
                ps.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseUtil.closeConnection(con);
        }
        //用户名不存在，返回false
        return false;
    }

    /*
    * 创建新用户，保存用户数据于数据库
    * @param User
    * */
    public void saveUser(User user) {
        //获取数据库连接对象
        Connection con = DataBaseUtil.getConnection();
        String sql = "insert into user(userName,password,phone) values(?,?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            //动态赋值
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getPhone());
            //执行更新
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseUtil.closeConnection(con);
        }
    }

    /*
    *验证登录信息，如果查询匹配则返回con，否则con等于null
    * @param username
    * @param password
    * @return User
     */
    public User login(String username, String password) {
        User user = null;
        //获取数据库临界Connection对象
        Connection con = DataBaseUtil.getConnection();
        //根据用户名和密码查询
        String sql = "select * from user where userName = ? and password = ?";
        try {
            //获取PreparedStatement对象
            PreparedStatement ps = con.prepareStatement(sql);
            //对SQL语句的占位符参数进行动态赋值
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            //判断结果集是否有效
            if (rs.next()) {
                user = new User();
                user.setPhone(rs.getString("phone"));
                user.setUserName(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
                user.setMoney(rs.getDouble("money"));
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭数据库连接
            DataBaseUtil.closeConnection(con);
        }
        return user;
    }

    /*
    * 查询用户账户金额
    * @param username
    * @return double
    * */
    public double selectMoney(String username) {
        double money = 0;
        Connection con = DataBaseUtil.getConnection();
        String sql = "select money from user where userName = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                money = rs.getDouble("money");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseUtil.closeConnection(con);
        }

        return money;
    }
}
