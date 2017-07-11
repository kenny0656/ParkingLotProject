<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2017/6/26
  Time: 11:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <link rel="stylesheet" href="css/amazeui.min.css">
    <title>用户登录</title>
</head>
<style>
    .header {
        text-align: center;
    }
    .header h1 {
        font-size: 200%;
        color: #333;
        margin-top: 30px;
    }
    .header p {
        font-size: 14px;
    }
</style>
<body>
<div class="header">
    <div class="am-g">
        <h1>欢迎来到停车场服务系统</h1>
        <p>${message}</p>
    </div>
</div>
<div class="am-g">
    <div class="am-u-lg-6 am-u-md-8 am-u-sm-centered">
        <form action="LoginServlet" method="post" class="am-form" >
            <label for="username">用户名:</label>
            <input type="text" name="username" id="username" value="">
            <br>
            <label for="password">密码:</label>
            <input type="password" name="password" id="password" value="">
            <br>
            <div class="am-cf">
                <input type="submit" name="" value="登 录" class="am-btn am-btn-primary am-btn-sm am-fl">&nbsp;&nbsp;
                <input type="button" name="" value="注册" id="btn_register" onclick="clickRegister()" class="am-btn am-btn-primary am-btn-sm">
                <input type="button" name="" value="忘记密码 ^_^? " class="am-btn am-btn-default am-btn-sm am-fr">
            </div>
        </form>
    </div>
</div>
</body>
<script>
    function clickRegister() {
        window.location="register.jsp";
    }
</script>
</html>
