package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.mysql.jdbc.Driver;
/**
 * Created by admin on 2017/6/26.
 */
public class DataBaseUtil {
    public static final double price = 10;
    /*
    * 获取数据连接
    * @return Connection对象
    * */
    public static Connection getConnection(){
        Connection con=null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String url="jdbc:mysql://localhost:3306/parkinglot";
            con= DriverManager.getConnection(url,"root","112233.");
        }catch (Exception e){
            e.printStackTrace();
        }
        return con;
    }
    public static void closeConnection(Connection con){
        if(con!=null){
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
