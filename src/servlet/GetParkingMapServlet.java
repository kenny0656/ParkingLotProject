package servlet;

import model.ParkingSeat;
import model.dao.ParkingSeatDao;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by admin on 2017/6/27.
 */
@WebServlet(name = "GetParkingMapServlet",value = "/GetParkingMapServlet")
public class GetParkingMapServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }
    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置响应的内容类型及编码
        response.setContentType("text/html;charset=utf-8");
        //获取所有车位信息
        ParkingSeatDao psdao=new ParkingSeatDao();
        ParkingSeat seats[]=psdao.getAllSeat();
        String map[]=new String[seats.length/6+1];  //车位信息地图
        for(int i=0;i<map.length;i++){   //初始化map
            map[i]=new String();
        }
        int line;
        for(int i=0;i<seats.length;i++){  //生成map
            line=i/5;   //每五个车位一行
            switch (seats[i].getStatus()){
                case 0:
                    map[line]+="e";
                    break;
                case 1:
                    map[line]+="b";
                    break;
                default:
                    map[line]+="o";
            }
        }
        PrintWriter out=response.getWriter();
        //JSONArray.fromObject()将对象转换成json传输
       out.print(JSONArray.fromObject(map));
       out.flush();
       out.close();

    }
}
