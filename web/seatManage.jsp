
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <link href='http://fonts.googleapis.com/css?family=Lato:400,700' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" href="css/jquery.seat-charts.css">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/tabStyle.css">
    <link rel="stylesheet" href="css/am-table.css">
    <script src="js/jquery.min.js"></script>
    <script src="js/jquery.seat-charts.js"></script>
    <script src="js/tabScript.js"></script>
    <title>车位管理</title>
    <script>
        $(function(){
            loadTab();
        });
    </script>
</head>
<%! String manager; %>
<%
    manager=(String) request.getSession().getAttribute("manager");
    if(manager==null||"".equals(manager)){
        response.sendRedirect("managerLogin.jsp");
    }
%>
<body style="width: 100%;position: relative;">
<jsp:include page="nav.jsp"/>
<div class="wrapper">
    <div class="container">
        <div id="seat-map">
            <div class="front-indicator">停车场</div>

        </div></div>
    <div class="booking-details">

        <h2>操作</h2>
        <button id="btn_UpdateData" onclick="updateData()">刷新车位信息</button><br><br>
        <div id="legend"></div>
    </div>
    <div style="width: 60%;margin: 0 auto;text-align: left;">
        <ul class="tabs">
            <li><a href="#" name=".bookedSeat">已经预订的车位</a></li>
            <li><a href="#" name=".usingSeat">正在使用的车位</a></li>

        </ul>
        <div class="content">
            <div class="bookedSeat">
            <table id="tb_bookedSeat" class="am-table am-table-striped am-table-hover">
            </table>
            </div>
            <div class="usingSeat">
                <table id="tb_usingSeat" class="am-table am-table-striped am-table-hover">
                </table>
            </div>

        </div>
    </div>

</div>


</body>

<script>
    var strMap=new Array();
    /*
     * 获取车位数据的函数
     * */
    function getSeatsData() {
        $.ajax({
            url:"/GetParkingMapServlet",
            type:"POST",
            dateType:"json",
            async:false,   //设置为同步请求方式
            error:function () {
                alert("error");
            },
            success: function(data){
                strMap=$.parseJSON(data);
            }
        });
    }
    /*
    * 获取预订信息
    * */
    function getBookingData() {
        $.ajax({
            url:"/GetBookingDataServlet",
            type:"POST",
            dataType:"html",
            error:function () {
              alert("getBookingData() error!");
            },
            success:function (data) {
                $("#tb_bookedSeat").html(data);
            }
        });

    }

    /*
    * 加载 正在使用的车位 信息
    * 车牌 用户名 正在使用的车位 进入时间 当前费用 操作
    * */
    function getUsingData() {
        $.ajax({
            url:"/GetUsingDataServlet",
            type:"POST",
            dataType:"html",
            error:function () {
                alert("getUsingData() error!");
            },
            success:function (data) {
                $("#tb_usingSeat").html(data);
            }
        });
    }


    /*
     * 当页面和DMO加载完成，生成停车位地图
     * */
    var firstSeatLabel = 1;
    $(document).ready(function() {
        getSeatsData();
        var sc = $('#seat-map').seatCharts({
            map: strMap,
            seats: {
                b:{         //已被预定
                    classes : 'first-class', //your custom CSS class
                    category: 'First Class'
                },
                e: {   //可供使用
                    classes : 'economy-class', //your custom CSS class
                    category: 'Economy Class'
                },
                o:{         //已被占用
                    classes:'occupy',
                    category: 'occupy Class'
                }

            },
            naming : {
                top : false,
                getLabel : function (character, row, column) {
                    return firstSeatLabel++;
                },
            },
            legend : {     //定义图例
                node : $('#legend'),
                items : [
                    [ 'e', 'available',   '可使用'],
                    [ 'b', 'booking', '已预定'],
                    ['o','occupied','已使用']
                ]
            },
            click: function () {    //点击事件
                if (this.status() == 'available') {
                    return 'available';
                } else if (this.status() == 'selected') {
                    //seat has been vacated
                    return 'selected';
                } else if (this.status() == 'unavailable') {
                    //seat has been already booked
                    return 'unavailable';
                } else {
                    return this.style();
                }
            }
        });
        //加载预订的车位信息表格
        getBookingData();
        //加载正在使用的车位信息表格
        getUsingData();

    });

    /*
     * 刷新页面，更新数据
     * */
    function updateData() {
        window.location="/seatManage.jsp";
    }

    /*
     * “放行进入”触发事件
     * 通过ajax向后台传递licenseId,后台处理后返回结果
     * */
    function passBooking(btn) {
        var licenseId=btn.id;
        $.ajax({
            url:'/PassBookingServlet',
            type:'POST',
            dataType:'html',
            async: false,
            data:{
                licenseId:licenseId
            },
            error:function () {
                alert(" passBooking error");
            },
            success:function (message) {
                alert(message);
                updateData();
            }

        });
    }
    /*
    * "取消预订" 触发事件
    *通过ajax发送请求到cancelBookingServlet，来实现操作
    * */
    function cancelBooking(btn) {
        var licenseId=btn.id;
        $.ajax({
            url:"/cancelBookingServlet",
            type:"post",
            async:false,
            dataType:"html",
            data:{
                licenseId:licenseId
            },
            error:function () {
                alert("cancelBooking error");
            },
            success:function (message) {
                alert(message);
                updateData();
            }
        });
    }

    /*
    * "放行并结算"按钮 触发事件
    * 通过ajax请求后台处理
    * */
    function finishDeal(btn) {
        var recordId=$("#"+btn.id).attr("recordId");
        var username=$("#"+btn.id).attr("username");
        $.ajax({
            url:"/finishDealServlet",
            async:false,
            type:"POST",
            dataType:"html",
            data:{
                recordId:recordId,
                username:username
            },
            error:function () {
                alert("finishDeal error");
            },
            success:function (message) {
                alert(message);
                getUsingData();
            }
        });
    }
</script>
</html>