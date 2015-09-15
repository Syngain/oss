$(function() {
	// 初始化菜单
	var menu = $(".rm-nav").rMenu({
		minWidth : "960px",
	});
	// 初始化日期控件
	$.datepicker.setDefaults({
		showOn : "both",
		buttonImageOnly : true,
		buttonImage : "img/calendar/calendar_g.png",
		buttonText : "Calendar"
	});
	$("#dateTime").datepicker(
			{
				closeText : '关闭',
				prevText : '<上月',
				nextText : '下月>',
				currentText : '今天',
				// showButtonPanel:true,//是否显示按钮面板
				dateFormat : 'yy-mm-dd',// 日期格式
				clearText : "清除",// 清除日期的按钮名称
				closeText : "关闭",// 关闭选择框的按钮名称
				yearSuffix : '年', // 年的后缀
				showMonthAfterYear : true,// 是否把月放在年的后面
				monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月',
						'九月', '十月', '十一月', '十二月' ],
				dayNames : [ '星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六' ],
				dayNamesShort : [ '周日', '周一', '周二', '周三', '周四', '周五', '周六' ],
				dayNamesMin : [ '日', '一', '二', '三', '四', '五', '六' ]
			});

	// 为条件选择框添加onclick事件
	$("#materialCondition tr td").not(
			$("#materialCondition tr td:nth-child(1)")).each(function() {
		$(this).attr("onClick", "conditionClick(this);");
	});
	// 条件选择：默认每行中条件<全部>选中睿智0
	$("#materialCondition tr td:nth-child(2)").css("color", "#6495ED");
	//点击率做为默认条件
	$("#CTRCondition").css("color","#FF8C00");
	//为条件：点击率 展现 消耗 展示ROI 点出ROI CPC添加DOM初始化事件
	$("#conditionDiv span").each(function(){
	}).click(function(){
		$(this).css("color","#FF8C00");
		$(this).siblings("span").removeAttr("style");
	});
	
	
	// get请求服务器端图片路径&名称
	var url = getContextPath() + "/action?actionid=photoMaterialAction&type=getImgPaths";
		$.ajax({
				url : url,
				type : 'GET',
				async : false,
				dataType : 'json',
				data : {},
				success : function(data) {
					for (var i = 0; i < data.Rows.length; i++) {
						$("#viewImg").append("<a href='action?actionid=photoMaterialAction&type=getImageByName&imageName=" + data.Rows[i].fileName + "' rel='prettyPhoto' title='This is the description' style='padding:15px;'><img width='160' height='160' style='padding:10px;' alt='This is the title' src='action?actionid=photoMaterialAction&type=getImageByName&imageName=" + data.Rows[i].fileName + "'/></a>");
					}
				},
				error : function(data) {
					$.ligerDialog.error('请求数据列表失败.');
				}
			});
	// prettyphoto 图片预览初始化
	$("a[rel*='prettyPhoto']").prettyPhoto({
		show_title : false
	});
	var tipDiv;
	//为每个图片创建提示
	$("#viewImg a").each(function() {
				var hrefAttr = $(this).attr("href");
				var qImgName = hrefAttr.substring(hrefAttr.lastIndexOf('&') + 1, hrefAttr.length).split('=')[1];
				//根据素材图片名称请求素材提示内容
				$.ajax({
					url: getContextPath() + '/action?actionid=photoMaterialAction&type=getQImgInfo',
					type : 'GET',
					async : false,
					dataType : 'json',
					data : {
						qImgName: qImgName
					},
					success:function(data){
						tipDiv = "<div style='width: 350px;height: 240px;font-family: cursive;font-size: 8px;font-weight: bold;'><table cellpadding='0' cellspacing='0' name='tables'><tr style='height:60px;'><td id='shop'>" + data.shop + "</td><td id='title'>" + data.title + "</td><td id='imageSize'>" + data.imageSize + "</td></tr><tr style='height:35px;'><td>点击率：</td><td colspan='2' id='CTR'>" + data.CTR + "%</td></tr><tr style='height:35px;'><td>ROI：</td><td colspan='2' id='ROI'>" + data.ROI + "%</td></tr><tr style='height:35px;'><td>投放时间：</td><td colspan='2' id='putInTime'>" + data.putInTime + "</td></tr><tr style='height:35px;'><td>投放人群：</td><td colspan='2' id='putInCrowd'>" + data.putInCrowd + "</td></tr><tr style='height:35px;'><td>链接页面：</td><td colspan='2' id='linkAddress'>" + data.linkAddress + "</td></tr></table></div>"
					},
					error:function(){
						$.ligerDialog.error('获取素材信息失败.');
					}
				});
				$(this).qtip({
					content : {
						text : tipDiv,	//qTips提示内容
					},
					position : {			//qTips提示框位置
						my : 'left center',
						at : 'right top'
					// target: 'mouse'	//qTips是否跟随鼠标
					},
					style : {			//qTips样式
						classes : 'qtip-blue ui-tooltip-rounded qtip-bootstrap'
					},
					show : {			//是否延迟加载qTips
						delay : 100
					}
				});
			});
});

//暂时未使用
function prettyPhotoClick(obj) {
	var imgID = obj.id;
	alert("imgID: " + imgID)
	var imgUrl = getContextPath() + "/" + $("#" + imgID + " img:only-child").attr("src");
	alert("imgUrl: " + imgUrl);
	obj.href = imgUrl;
}

// 条件点击事件
function conditionClick(val) {
	if ($(val).text() != "全部") {
		if ($(val).text() == "") {
			$(val).siblings().each(function() {
				if ($(this).text() == "全部") {
					$(this).css("color", "#6495ED");
				} else {
					$(this).removeAttr("style");
				}
			});
		} else {
			$(val).siblings().each(function() {
				$(this).removeAttr("style");
			});
			if ($(val).attr("style") == undefined) {
				$(val).css("color", "#6495ED");
			}
		}
	} else {
		$(val).css("color", "#6495ED");
		$(val).siblings().each(function() {
			$(this).removeAttr("style");
		});
	}
}

function ajaxFileUpload(){
	//layer.load(3);
	$.ajaxFileUpload({  
	    url: getContextPath() + '/action?d='+(new Date().getMilliseconds()) + '&actionid=photoMaterialAction&type=uploadMaterialExcel',            //需要链接到服务器地址  
	    secureuri: false,  
	    fileElementId: 'fileLoad',                        //文件选择框的id属性  
	    dataType: 'JSON',                                     //服务器返回的格式，可以是json  
	    success: function (data, status){
	    	//layer.load(3);
	    	 if(data == '0'){
	    		 $.ligerDialog.success('文件上传成功.');
	    	 }
	    	 if(data == '-1'){
	    		 $.ligerDialog.warn('上传文件不是Excel文件，目前只支持Excel，其他文件上传功能后续开放.');
	    	 }
	    	 if(data == '-2'){
	    		 $.ligerDialog.error('文件上传出现异常.');
	    	 }
	    	 if(data == '-3'){
	    		 $.ligerDialog.error('上传文件成功，但是读取数据添加DB出现错误.');
	    	 }
        	},  
        	error: function (data, status, e){            //相当于java中catch语句块的用法    
        		$.ligerDialog.error('加载失败.');
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

//搜索按钮点击事件
function searchBtn(){
	alert("searchBtn!!!");
}

//导入按钮点击事件
function importChartBtn(){
	if($("#fileLoad").val() == ""){
		 $.ligerDialog.error('请选择需要上传的报表文件.');
		return;
	}else{
		ajaxFileUpload();
	}
}

//导出按钮点击事件
function exportChartBtn(){
	alert("export!!!");
}