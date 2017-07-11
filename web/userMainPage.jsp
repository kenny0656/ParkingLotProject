<%@ page import="model.User" %><%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2017/6/29
  Time: 8:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <link rel="stylesheet" href="css/amazeui.min.css">
    <script src="js/jquery.min.js"></script>
    <title>用户主界面</title>

</head>
<body>
<%! User user=null; %>
<%
    user= (User) request.getSession().getAttribute("user");
    if(user==null){
       response.sendRedirect("login.jsp");
    }
%>
<div class="am-panel am-panel-default">
    <header class="am-panel-hd" style="text-align: center">
        <h3 class="am-panel-title">用户主界面</h3>
    </header>
    <div class="am-panel-bd">
        <div class="am-container">
            <div></div>
            <div style="float:left;width:25%;height:35%;margin-left: 20%;margin-right: 5%">
            <img src="img/head.gif"  class="am-img-thumbnail am-radius" alt="头像">
            </div>
            <div>
                <p>用户名：<%= user.getUserName()%></p>
                <p>手机号码：<%= user.getPhone()%></p>
                <p>剩余金额：<%= user.getMoney()%></p>
            </div>
        </div>
        <div>
            <table  class="am-table am-table-bordered am-table-radius am-table-striped">
                <tr>
                    <td>新增车牌:</td>
                    <td>
                        输入车牌号：<input id="in_license"/><button id="btn_addLicense" onclick="addLicense()">确认</button>
                    </td>
                </tr>
            </table>
            <p>拥有的车牌：</p>
            <table id="tb_license" class="am-table am-table-bordered am-table-radius am-table-striped">
            </table>
        </div>
    </div>
</div>
</body>
<script>
    /*
    * 当DOM加载完成，获取用户的车牌信息
    * */
    $(document).ready(function () {
        getLicenseData();
    });
    
    /*
    *获取用户车牌信息的函数
    * 通过ajax与后台传递数据，插入html到表格
     *  */
    function getLicenseData() {
        $.ajax({
           url:"/GetLicenseDataServlet",
            type:"POST",
            dataType:"html",
            data:{
               username:"<%= user.getUserName()%>"
            },
            error:function () {
                alert("error");
            },
            success:function (data) {
               $("#tb_license").html(data);
            }
            
        });
    }
    /*
    * 点击"申请预订"的触发事件
    * button的id就是车牌号,通过ajax传到后台放进session
    * 跳转到ParkingMap.jsp
    * */
    function clickBooking(btn) {
        var id=btn.id;
        $.ajax({
            url:'SetSessionServlet',
            dataType:'html',
            data:{
                name:'licenseID',
                value:id
            },
            async:false,  //设置为同步请求
            error:function () {
                alert("error");
            },
            success:function () {
                window.location.href="parkingMap.jsp";
            }
        });

    }
    /*
    * "取消预订" 触发函数
    *
     *  */
    function clickCancel(btn) {
        $.ajax({
            url:"/cancelBookingServlet",
            type:"GET",
            dataType:"html",
            data:{
                licenseId:btn.id
            },
            async:false,
            error:function () {
              alert("error");
            },
            success:function (message) {
                if(confirm(message)){
                    window.location.href="userMainPage.jsp";
                }else {
                    window.location.href="userMainPage.jsp";
                }

            }
        });
    }

    /*
    * 新增车牌
    * 获取输入的车牌号，传送后台处理
    * */
    function addLicense() {
        var licenseNumber=$("#in_license").val();
        if(licenseNumber==null||licenseNumber==""||typeof (licenseNumber)=="undefined"||licenseNumber=="false"){
            alert("车牌号不能为空！")
            return;
        }
        $.ajax({
            url:"/AddLicenseServlet",
            type:"POST",
            dataType:"html",
            data:{
                licenseNumber:licenseNumber,
                username:"<%= user.getUserName() %>"
            },
            error:function () {
                alert("addLicense error");
            },
            success:function (message) {
                alert(message);
                getLicenseData();
            }
        });
    }

</script>
</html>
