<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2017/7/1
  Time: 23:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <link rel="stylesheet" href="css/amazeui.min.css">
    <script src="js/jquery.min.js"></script>
    <script src="js/amazeui.min.js"></script>
    <title>统计数据</title>
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
    <div class="am-alert am-alert-danger" id="my-alert" style="display: none">
        <p>开始日期应小于结束日期！</p>
    </div>
    <div class="am-g">
        <div class="am-u-sm-3"><font style="font-size: 20px">计算起始时间内的收入:</font></div>
        <div class="am-u-sm-3">
            <button type="button" class="am-btn am-btn-default am-margin-right" id="my-start">开始日期</button>
            <span id="my-startDate">2017-07-01</span>
        </div>
        <div class="am-u-sm-3">
            <button type="button" class="am-btn am-btn-default am-margin-right" id="my-end">结束日期</button>
            <span id="my-endDate">2017-07-04</span>
        </div>
        <div style="font-size: 20px">
            <font>期间收入：</font><label id="lb_income"></label>
        </div>

    </div>

</div>
</body>
<script>
    $(function () {
        var startDate = new Date(2017, 06, 01);
        var endDate = new Date(2017, 06, 04);
        var $alert = $('#my-alert');
        $('#my-start').datepicker().on('changeDate.datepicker.amui', function (event) {
            if (event.date.valueOf() > endDate.valueOf()) {
                $alert.find('p').text('开始日期应小于结束日期！').end().show();
            } else {
                $alert.hide();
                startDate = new Date(event.date);
                $('#my-startDate').text($('#my-start').data('date'));
                //更新期间收入
                getIncomeData();
            }
            $(this).datepicker('close');
        });

        $('#my-end').datepicker().on('changeDate.datepicker.amui', function (event) {
            if (event.date.valueOf() < startDate.valueOf()) {
                $alert.find('p').text('结束日期应大于开始日期！').end().show();
            } else {
                $alert.hide();
                endDate = new Date(event.date);
                $('#my-endDate').text($('#my-end').data('date'));
                //更新期间收入
                getIncomeData();
            }
            $(this).datepicker('close');
        });
    });

    /*
     * 获取期间收入
     * */
    function getIncomeData() {
        //获取后台统计数据
        var fromTime = $('#my-start').data('date');
        var toTime = $('#my-end').data('date');
        $.ajax({
            url: "/GetPeriodIncomeServlet",
            type: "POST",
            dataType: "html",
            data: {
                fromTime: fromTime,
                toTime: toTime
            },
            error: function () {
                alert("loadIncome error");
            },
            success: function (message) {
                $("#lb_income").text(message);
            }
        });
    }
</script>
</html>
