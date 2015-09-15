var contextMenuData = null;//右键菜单的数据全局变量
var columns = [
               		{ name: 'id', display: '序号', width: '' },
               		{ name: 'fileName', display: '报表文件名称', width: '' },
               		{ name: 'operator', display: '操作者', width: '' },
               		{ name: 'operateTime', display: '操作日期', width: '' }
               		//{ name: 'pieChart', display: '饼状图分析', width: '',isAllowHide: true},
               		//{ name: 'lineChart', display: '操作日期', width: '', isAllowHide: true}
];
// 列表项目操作按钮
var bindRowAction= function(edom,rowdata){
	var actionType = $(edom).attr('name');
    switch (actionType){
    	case 'pieChart': // 饼状图分析
    		pieChart(rowdata);
    		break;
        case 'lineChart': // 柱状图分析
        	lineChart(rowdata);
        	break;
    };
};
$(function(){
	var menu = $(".rm-nav").rMenu({
		minWidth : "960px",
	});
	getExcelList();
	$("#chartMgr").click(function(){
		forwardToChartPerspective();
	});
});

function hightChart(data){
	$('#container').highcharts({
        title: {
            text: '关注用户',
            x: -20, //center标题位置
            style: { //标题样式
            	"color": "blue",
            	"font-size":"25px"
            }
        },
        /*subtitle: { //辅标题
            text: '15天点击回报率',
            x: -20
        },*/
        credits: { //highcharts水印
        	enabled: false //是否启用水印
        },
        xAxis: { //x轴
            categories: ['2015-09-07', '2015-09-08', '2015-09-09', '2015-09-10', '2015-09-11', '2015-09-12',
                '2015-09-13', '2015-09-14', '2015-09-15', '2015-09-16', '2015-09-17', '2015-09-18'],
            tickmarkPlacement: "on" //x轴分类(categories)显示位置
        },
        yAxis: { //y轴
            title: {
                text: '15天点击回报率'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        /*tooltip: { //y轴的显示
            valueSuffix: 'Temperature (°C)'
        },*/
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },
        series: [{ //数据
            name: '施华蔻官方旗舰店',
            data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
        }, {
            name: '凤凰色官方旗舰店',
            data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
        }, {
            name: '雀巢官方旗舰店',
            data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
        }, {
            name: '雪肌精官方旗舰店',
            data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
        }]
    });
}

function getExcelList(){
	//右键菜单
	menus = $.ligerMenu({
		width : 120,
		items : [ {
			text : '饼状图分析',
			click : function(item,i){
				window.location.href = getContextPath + '/action?actionid=excelAction&type=getPieChart',
				$.ligerDialog.success('pieChart.');
			},
			icon : 'add'
		}, {
			text : '折线图分析',
			click : function(item,i){ //右键菜单点击事件
				//window.location.href = getContextPath + '/action?actionid=excelAction&type=getLineChart',
				//window.location.href = "pages/view/viewByLDP.html";
				//$("#container").show();
				$.ligerDialog.success('lineChart.');
			},
			icon: 'modify'
		}, {
			line : true
		}]
	});
	$.ajax({
		url : getContextPath() + '/action?actionid=excelAction&type=getExcelList',
		type : 'GET',
		async : false,
		dataType : 'json',
		data : {
		},success:function(data){
			//$.ligerDialog.success('请求数据成功.');
			hightChart(data);
			$("#maingrid").ligerGrid({
				title: 'Excel报表信息',
				allowAdjustColWidth:true,
				width: '99.9%',
				height: '55%',
		        columns: columns,
		        /*onSelectRow: function(rowdata, rowid, rowobj ,edom){
		        	return bindRowAction(edom,rowdata);
		        },*/
		        dataAction: 'local',
		        data: { Rows: data.Rows },
				onRClickToSelect: true,	//开启右键菜单
				onContextmenu: function(param,e){ //右键菜单显示
					contextMenuData = param.data;
					menus.show({
						top: e.pageY, left: e.pageX 
					});
					return false;
				}
		    }); 
		},error:function(data){
			$.ligerDialog.error('请求数据列表失败.');
		}
	});
}