
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <link href="css/fonts.googleapis.css" rel='stylesheet' type='text/css'>
    <link rel="stylesheet" href="css/jquery.seat-charts.css">
    <link rel="stylesheet" href="css/style.css">
    <script src="js/jquery.min.js"></script>
    <script src="js/jquery.seat-charts.js"></script>
    <title>我的服务</title>
</head>
<body>
<div class="wrapper">
    <div class="container">
        <div id="seat-map">
            <div class="front-indicator">停车场</div>

        </div></div>
        <div class="booking-details">
        <h2>操作</h2>
               <font style="font-size:15px">车位号:</font><input type="number" id="in_seatID" style="width: 50px">
                <button type="submit" onclick="bookSeat()" class="checkout-button">确认预订</button>
            <button id="btn_UpdateData" onclick="updateData()">刷新车位信息</button>
            <div id="legend"></div>
    </div>


</div>
<script>
    var strMap=new Array();
    /*
    * 获取车位数据的函数
    * */
    function getData() {
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
    * 当页面和DMO加载完成，生成停车位地图
    * */
    var firstSeatLabel = 1;
    $(document).ready(function() {
       getData();
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
    });

    /*
    * 刷新页面，更新数据
    * */
    function updateData() {
        window.location="/parkingMap.jsp";
    }

    /*
    * “确认预定”触发事件
    * 通过ajax向后台传递seatID,后台处理后返回message
    * */
    function bookSeat() {
        var seatID=$("#in_seatID").val();
        if(seatID==null){return;}
        $.ajax({
            url:'/BookingSeatServlet',
            type:'POST',
            dataType:'html',
            async: false,
            data:{
                seatID:seatID
            },
            error:function () {
                alert("bookSeat error");
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
</script>



</body>
</html>
