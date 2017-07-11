<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2017/7/1
  Time: 23:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <link rel="stylesheet" href="css/amazeui.min.css">
    <script src="js/jquery.min.js"></script>
    <title>用户管理</title>
</head>
<body>
<%! String manager; %>
<%
    manager=(String) request.getSession().getAttribute("manager");
    if(manager==null||"".equals(manager)){
        response.sendRedirect("managerLogin.jsp");
    }
%>
<jsp:include page="nav.jsp"/>
<div>
    <div style="margin: 0 auto;text-align: right">
    <span>
        <input id="input_search"/><button id="button_search" onclick="searchUser()">搜索</button>
    </span>
    </div>
    <table class="am-table am-table-bordered am-table-striped am-table-hover">
        <thead>
        <tr>
            <th>用户名</th>
            <th>联系电话</th>
            <th>余额</th>
            <th>充值</th>
        </tr>
        </thead>
        <tbody id="tbody">
        </tbody>
    </table>

</div>
</body>
<script>
    /*
     * 加载用户信息
     * @param keyword  如果keyword=all,加载所有用户
     * */
    function loadUserInfo(keyword) {
        $.ajax({
            url: "/LoadUserInfoServlet",
            dataType: "html",
            type: "POST",
            data: {
                keyword: keyword
            },
            error: function () {
                alert("loadUesrInfo error!")
            },
            success: function (data) {
                $("#tbody").html(data);
            }
        });
    }
    /*
     * “搜索”按钮触发事件
     * 获取搜索框的值，作为keyword传入loadUserInfo(keyword)
     * 判断keyword是否为空，如果为空，将keyword=all
     * */
    function searchUser() {
        var keyword = $("#input_search").val();
        if (keyword == null || keyword == "" || typeof(keyword) == "undefined") {
            keyword = "all";
        }
        loadUserInfo(keyword);
    }
    $(document).ready(function () {
        loadUserInfo("all");
    });

    /*
     * 充值 触发事件
     * 获取input输入内容,把充值金额传到后台进行处理
     * */
    function recharge(btn) {
        var inputId = $("#" + btn.id).attr("inputId");
        var username = $("#" + btn.id).attr("username"); //用户名
        var money = $("#" + inputId).val();  //获取金额
        $.ajax({
            url: "/RechargeServlet",
            dataType: "html",
            type: "POST",
            async:false,
            data: {
                money: money,
                username:username
            },
            error:function () {
                alert("recharge error！");
            },
            success:function (message) {
                alert(message);
                loadUserInfo("all");
            }
        });
    }

</script>

</html>
