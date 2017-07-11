<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2017/7/2
  Time: 1:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="js/jquery.min.js"></script>
    <title>test</title>
</head>
<body>
<button id="btn_test" onclick="clickTest()">test</button>
</body>
<script>
    function clickTest() {
        $.ajax({
            url:"/GetPeriodIncomeServlet",
            type:"POST",
            dataType:"html",
            error:function () {
                alert("error");
            },
            success:function (message) {
               alert(message);
            }
        });
    }
</script>
</html>
