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

function getExcelList(){
	//右键菜单
	menus = $.ligerMenu({
		width : 120,
		items : [ {
			text : '饼状图分析',
			click : function(item,i){
				alert("pieChart");
			},
			icon : 'add'
		}, {
			text : '折线图分析',
			click : function(item,i){
				alert("lineChart");
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
			$("#maingrid").ligerGrid({
				title: 'Excel报表信息',
				allowAdjustColWidth:true,
				width: '100%',
				height: '55%',
		        columns: columns,
		        /*onSelectRow: function(rowdata, rowid, rowobj ,edom){
		        	return bindRowAction(edom,rowdata);
		        },*/
		        data: { Rows: data.Rows },
				onRClickToSelect: true,	//开启右键菜单
				onContextmenu: function(param,e){
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

$(function(){
	var menu = $(".rm-nav").rMenu({
		minWidth : "960px",
	});
	getExcelList();
});