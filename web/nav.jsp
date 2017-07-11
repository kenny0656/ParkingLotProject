<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2017/6/30
  Time: 20:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <link rel="stylesheet" href="css/am-nav.css">
<%! String manager; %>
<%
    manager=(String) request.getSession().getAttribute("manager");
    if(manager==null||"".equals(manager)){
        response.sendRedirect("managerLogin.jsp");
    }
%>
<div style="text-align: center"><h3>管理员主界面</h3></div>
<ul class="am-nav am-nav-tabs">
    <li><a href="/seatManage.jsp">车位管理</a></li>
    <li><a href="/userManage.jsp">用户管理</a></li>
    <li><a href="/statisticsData.jsp">统计数据</a></li>
</ul>

