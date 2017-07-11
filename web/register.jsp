<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2017/6/26
  Time: 16:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <link rel="stylesheet" href="css/amazeui.min.css">
    <title>用户注册</title>
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
</head>
<body>
<div class="header">
    <div class="am-g">
        <h1>用户注册</h1>
        <p>${message}</p>
    </div>
</div>
<div class="am-g">
    <div class="am-u-lg-6 am-u-md-8 am-u-sm-centered">
        <form action="RegisterServlet" method="post" class="am-form">
            <label for="username">用户名:</label>
            <input type="text" name="username" id="username" value="">
            <br>
            <label for="password">密码:</label>
            <input type="password" name="password" id="password" value="">
            <br>
            <label for="phone">手机号码:</label>
            <input type="text" name="phone" id="phone" value="">
            <br>
            <div class="am-cf">
                <input type="submit" name="" value="注册" id="btn_register" class="am-btn am-btn-primary am-btn-sm am-center">
                <input type="button" name="" value="登录" onclick="clickLogin()" class="am-btn am-btn-default am-btn-sm am-fr">
            </div>
        </form>
    </div>
</div>
</body>
<script>
    function clickLogin() {
        window.location="login.jsp";
    }
</script>
</html>
