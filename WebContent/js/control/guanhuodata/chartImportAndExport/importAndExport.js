$(function(){
	//菜单init
	var menu = $(".rm-nav").rMenu({
		minWidth : "960px",
	});
	//日期控件init
	$.datepicker.setDefaults({
		showOn: "both",
		buttonImageOnly: true,
		buttonImage: "img/calendar/calendar.png",
		buttonText: "Calendar"
	});
	$("#startTime").datepicker({
		closeText: '关闭',  
        prevText: '<上月',  
        nextText: '下月>',  
        currentText: '今天',
		//showButtonPanel:true,//是否显示按钮面板  
        dateFormat: 'yy-mm-dd',//日期格式
        clearText:"清除",//清除日期的按钮名称  
        closeText:"关闭",//关闭选择框的按钮名称  
        yearSuffix: '年', //年的后缀  
        showMonthAfterYear:true,//是否把月放在年的后面
		monthNames: ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'],
		dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],  
        dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],  
        dayNamesMin: ['日','一','二','三','四','五','六']
	});
	$("#endTime").datepicker({
		closeText: '关闭',  
        prevText: '<上月',  
        nextText: '下月>',  
        currentText: '今天',
        //showButtonPanel:true,//是否显示按钮面板  
        dateFormat: 'yy-mm-dd',//日期格式
        clearText:"清除",//清除日期的按钮名称  
        closeText:"关闭",//关闭选择框的按钮名称  
        yearSuffix: '年', //年的后缀  
        showMonthAfterYear:true,//是否把月放在年的后面
		monthNames: ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'],
		dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],  
        dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],  
        dayNamesMin: ['日','一','二','三','四','五','六']
	});
});

function ajaxFileUpload(){
	$.ajaxFileUpload({  
	    url: getContextPath() + '/action?d='+(new Date().getMilliseconds()) + '&actionid=excelAction&type=uploadExcel',            //需要链接到服务器地址  
	    secureuri: false,  
	    fileElementId: 'fileLoad',                        //文件选择框的id属性  
	    dataType: 'JSON',                                     //服务器返回的格式，可以是json  
	    success: function (data, status){
	    	//var tmp = $.parseJSON(data);
	    	 if(data == '0'){
	    		 $.ligerDialog.success('文件上传成功.');
	    		 //window.location.href=getContextPath() + '/action?d='+(new Date().getMilliseconds()) + '&actionid=showUploadedExcel';
	    	 }
	    	 if(data == '-1'){
	    		 $.ligerDialog.warn('上传文件不是Excel文件，目前只支持Excel，其他文件上传功能后续开放.');
	    	 }
	    	 if(data == '-2'){
	    		 $.ligerDialog.error('文件上传出现异常.');
	    	 }
        	/*var options = "";
        	for(var i = 0;i < tmp.Rows.length;i++){
        		var len = tmp.Rows[i].radiusInfomation.length;
        		var radiusInfo = (tmp.Rows[i].radiusInfomation).substring(1,len-1);
        		var array = radiusInfo.split(",");
        		for(var j = 0;j<array.length;j++){
        			options += "<option>" + array[j] + "</option>";
            		arr.push("<option>" + array[j] + "</option>");
            		$("#radiusList").html(options);
        		}
        		$("#appIp").val(tmp.Rows[i].appIp);
        		$("#appPort").val(tmp.Rows[i].appPort);
        		$("#sendTimes").val(tmp.Rows[i].sendTimes);
        		$("#sendInterval").val(tmp.Rows[i].sendInterval);
        		$("#sharedKey").val(tmp.Rows[i].sharedKey);
        	}
        		$("#textfield").val("");*/
        	},  
        	error: function (data, status, e){            //相当于java中catch语句块的用法    
        		$.ligerDialog.error('加载失败.');
        		$("#textfield").val("");
        	}  
        });  
}

//上传
function uploadFunc(){
	if($("#fileLoad").val() == ""){
		 $.ligerDialog.error('请选择需要上传的报表文件.');
		return;
	}else{
		ajaxFileUpload();
	}
}

function preViewChartData(){
	var startTime = $("#startTime").datepicker("getDate");
	var endTime = $("#endTime").datepicker("getDate");
	var startTimeFmt = $.datepicker.formatDate('yy-mm-dd',$("#startTime").datepicker("getDate"));
	var endTimeFmt = $.datepicker.formatDate('yy-mm-dd',$("#endTime").datepicker("getDate"));
	var begin = new Date($.datepicker.formatDate('yy/mm/dd',$("#startTime").datepicker("getDate")));
	var end = new Date($.datepicker.formatDate('yy/mm/dd',$("#endTime").datepicker("getDate")));
	if(startTime == "" || startTime == null && endTime == "" || endTime == null){
		$.ligerDialog.error('请选择日期.');
	}else{
		if(begin - end >0){
			$.ligerDialog.warn('开始日期不能大于结束日期.');
		}else{
			var url = getContextPath() + '/action?actionid=forwardAction&type=preViewChartDataPage';
			layer.open({
			    type: 2,	//iframe层
			    title: '报表数据预览',
			    shadeClose: false,	//是否点击遮罩层就关闭
			    shade: 0.8,	//遮罩层颜色
			    area: ['880px', '33%'],
			    closeBtn: 2,	//关闭按钮类型
			    icon: 6,
			    content: url //iframe的url
			}); 
		}
	}
}

//重置
function reset(){
	$("#startTime").val("");
	$("#endTime").val("");
	$("#choiceNum").html("0");
}