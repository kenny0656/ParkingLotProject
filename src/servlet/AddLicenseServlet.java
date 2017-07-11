package servlet;

import model.dao.LicenseDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by admin on 2017/7/4.
 */
@WebServlet(name = "AddLicenseServlet",value = "/AddLicenseServlet")
public class AddLicenseServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out =response.getWriter();
        String licenseNumber=request.getParameter("licenseNumber");
        String username=request.getParameter("username");
        LicenseDao licenseDao=new LicenseDao();
        boolean rs=licenseDao.addLicense(username,licenseNumber);
        if(rs){
            out.print("操作成功！");
        }else {
            out.print("操作失败！");
        }
        out.flush();
        out.close();

    }
}
