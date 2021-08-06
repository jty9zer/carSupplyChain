<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <%--    <title>第一个 ECharts 实例</title>--%>
    <%--    <script src="/js/jquery-1.8.2.min.js"></script>--%>
    <%--    <!-- 引入 echarts.js -->--%>
    <%--    <script scr="/js/echarts.js"></script>--%>
    <script src="https://cdn.staticfile.org/jquery/2.2.4/jquery.min.js"></script>
    <!-- 引入 echarts.js -->
    <script src="https://cdn.staticfile.org/echarts/4.3.0/echarts.min.js"></script>
    <%--    <script src="https://cdn.staticfile.org/echarts/4.3.0/echarts.min.js"></script>--%>
</head>
<body>
<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
<div id="main" style="width: 600px;height:400px;"></div>

<div id="main1" style="width: 600px;height:400px;"></div>
<script type="text/javascript">
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));
    $.get('/cargoods/getRepertoryData', function (data) {
        myChart.setOption(
            {
                title: {
                    text: '订单统计',       //大标题
                    subtext: '柱状图',                //类似于副标题
                    x: 'center'                 //标题位置   居中
                },
                tooltip: {},
                xAxis: {
                    data: data.data[0]
                },
                yAxis: {},
                series: [{
                    name: '商品名称',
                    type: 'bar',
                    data: data.data[1]
                }]
            })
    }, 'json')

    var myChart1 = echarts.init(document.getElementById('main1'));
    $.get('/cargoods/getRepertoryData2', function (data) {
        myChart1.setOption({
            title: {
                text: '订单统计',       //大标题
                subtext: '饼状图',                //类似于副标题
                x: 'center'                 //标题位置   居中
            },
            tooltip: {
                trigger: 'item',           //数据项图形触发，主要在散点图，饼图等无类目轴的图表中使用。
                formatter: "{a} <br/>{b} : {c} ({d}%)"   //{a}（系列名称），{b}（数据项名称），{c}（数值）, {d}（百分比）用于鼠标悬浮时对应的显示格式和内容
            },
            legend: {                           //图例组件。
                orient: 'vertical',             //图例列表的布局朝向
                left: 'left',
                data: data.data[1]
            },
            series: [              //系列列表。每个系列通过 type 决定自己的图表类型
                {
                    name: '商品名称',
                    type: 'pie',
                    radius: '55%',
                    center: ['50%', '60%'],
                    data: data.data[0],
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        })
    }, 'json')
</script>
</body>
</html>