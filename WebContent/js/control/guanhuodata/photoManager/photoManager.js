var params = {
		pageNumber:1,shopName:"all",standSize:"all",activityName:"all",putInCrowd:"all",putInDateTime:null,CTR:"CTR",CTROrder:"desc",
		reveal:null,revealOrder:null,consume:null,consumeOrder:null,showROI:null,showROIOrder:null,clickOutROI:null,clickOutROIOrder:null,
		CPC:null,CPCOrder:null
	};

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
		buttonText : "投放时间"
	});
	$("#dateTime").datepicker({
		prevText : '<上月',
		nextText : '下月>',
		currentText : '今天',
		//showButtonPanel:true,//是否显示按钮面板
		dateFormat : 'yy-mm-dd',// 日期格式
		//clearText : "清除",// 清除日期的按钮名称
		closeText : "关闭",// 关闭选择框的按钮名称
		//closeText : "清除",// 利用close事件来清除日期
		yearSuffix : '年', // 年的后缀
		/*onClose: function(dateText,inst){
			dateText = "";
		},*/
		showMonthAfterYear : true,// 是否把月放在年的后面
		monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月',
				'九月', '十月', '十一月', '十二月' ],
		dayNames : [ '星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六' ],
		dayNamesShort : [ '周日', '周一', '周二', '周三', '周四', '周五', '周六' ],
		dayNamesMin : [ '日', '一', '二', '三', '四', '五', '六' ]
	});
	//初始化：品牌、展位、推广类型、投放人群的查询条件
	initConditions();
	getImgPaths();
	// 为条件选择框添加onclick事件
	$("#materialCondition tr td").not(
			$("#materialCondition tr td:nth-child(1)")).each(function() {
		$(this).attr("onClick", "conditionClick(this);");
	});
	// 条件选择：默认每行中条件<全部>选中
	$("#materialCondition tr td:nth-child(2)").css("color", "#6495ED");
	//点击率做为默认条件
	$("#CTRCondition").css("color","#FF8C00");
	//为条件：点击率 展现 消耗 展示ROI 点出ROI CPC添加DOM初始化事件
	$("#conditionDiv span").each(function(){
	}).click(function(){
		$(this).css("color","#FF8C00");
		$(this).siblings("span").removeAttr("style");
		//在配置参数前需要清除默认条件中的CTR与CTROrder
		clearParams();
		//配置:点击率 展现 消耗 展示ROI 点出ROI CPC查询参数
		if($(this).index() == 0){
			params.CTR = "CTR";
			params.CTROrder = "desc";
		}
		if($(this).index() == 1){
			params.reveal = "reveal";
			params.revealOrder = "desc";
		}
		if($(this).index() == 2){
			params.consume = "consume";
			params.consumeOrder = "desc";
		}
		if($(this).index() == 3){
			params.showROI = "showRateOfReturn_15";
			params.showROIOrder = "desc";
		}
		if($(this).index() == 4){
			params.clickOutROI = "rateOfReturn_15";
			params.clickOutROIOrder = "desc";
		}
		if($(this).index() == 5){
			params.CPC = "unitPriceOfClick";
			params.CPCOrder = "desc";
		}
		if($("#dateTime").val() != ""){
			params.putInDateTime = $("#dateTime").val();
		}
		//请求分页信息
		$.ajax({
			url: getContextPath() + '/action?actionid=photoMaterialAction&type=getPaginationInfoByCondition',
			type : 'GET',
			async : false,
			dataType : 'json',
			data : params,
			success:function(data){
				//alert(data.totalRecords);
				if(data == '-1'){
					$.ligerDialog.error('加载分页信息失败.');
				}else{
					if(data.totalRecords == 0){
						//alert("无记录！");
						$("#viewImg").empty();
						$("#viewImg").append("<span style='font-family: cursive;font-size: 18px;font-weight: bolder;color: red;width:100%; position:absolute; text-align:center;'>没有查询到对应素材记录.</span>");
						$("#paginationDiv").hide();
					}else{
						$("#viewImg").empty();
						$("#paginationDiv").show();
						getImgPaths();
						// prettyphoto 图片预览初始化
						$("a[rel*='prettyPhoto']").prettyPhoto({
							show_title : false,
							//modal: true,
							default_width: 300,
							default_height: 144
						});
						tips();
						//createPage(pageSize, buttons, total)：pageSize(每页记录数)/buttons(按钮数目)/total(记录总数)
						createPage(data.pageSize, 5, data.totalRecords);
					}
				}
			},
			error:function(){
				$.ligerDialog.error('加载分页信息出现异常,请重试.');
			}
		});
	});
	
	// prettyphoto 图片预览初始化
	$("a[rel*='prettyPhoto']").prettyPhoto({
		show_title : false,
		//modal: true,
		default_width: 300,
		default_height: 144
	});
	tips();
	//请求分页信息
	$.ajax({
		url: getContextPath() + '/action?actionid=photoMaterialAction&type=getPaginationInfo',
		type : 'GET',
		async : false,
		dataType : 'json',
		data : {
			pageNumber: 1
		},
		success:function(data){
			if(data == '-1'){
				$.ligerDialog.error('加载分页信息失败.');
			}else{
				//createPage(pageSize, buttons, total)：pageSize(每页记录数)/buttons(按钮数目)/total(记录总数)
				createPage(data.pageSize, 5, data.totalRecords);
			}
		},
		error:function(){
			$.ligerDialog.error('加载分页信息出现异常,请重试.');
		}
	});
	//监听投放时间input value变化
	$("#dateTime").change(function(){
		params.putInDateTime = $(this).val();
		//请求分页信息
		$.ajax({
			url: getContextPath() + '/action?actionid=photoMaterialAction&type=getPaginationInfoByCondition',
			type : 'GET',
			async : false,
			dataType : 'json',
			data : params,
			success:function(data){
				//alert(data.totalRecords);
				if(data == '-1'){
					$.ligerDialog.error('加载分页信息失败.');
				}else{
					if(data.totalRecords == 0){
						$("#viewImg").empty();
						$("#viewImg").append("<span style='font-family: cursive;font-size: 18px;font-weight: bolder;color: red;width:90%; position:absolute; text-align:center;'>没有查询到对应素材记录.</span>");
						$("#paginationDiv").hide();
					}else{
						$("#viewImg").empty();
						$("#paginationDiv").show();
						getImgPaths();
						// prettyphoto 图片预览初始化
						$("a[rel*='prettyPhoto']").prettyPhoto({
							show_title : false,
							//modal: true,
							default_width: 300,
							default_height: 144
						});
						tips();
						//createPage(pageSize, buttons, total)：pageSize(每页记录数)/buttons(按钮数目)/total(记录总数)
						createPage(data.pageSize, 5, data.totalRecords);
					}
				}
			},
			error:function(){
				$.ligerDialog.error('加载分页信息出现异常,请重试.');
			}
		});
	});
	//进行下拉框选择的监听，如果选中了批量上传图片，页面跳转
	$("#select").change(function(){
		if($(this).val() == 1)
			layer.open({
			    type: 2,
			    title: '素材图片上传页',
			    shadeClose: false,
			    shade: 0.8,
			    //closeBtn: 2,
			    area: ['400px', '60%'],
			    content: getContextPath() + "/action?actionid=forwardAction&type=uploadifyPage",
			    cancel: function(index){
			    	forwardToMaterialPage();
			    }
			}); 
	});
});

//清除初始化参数params默认的条件
function clearParams(){
	params.pageNumber = 1;
	params.CTR = null;
	params.CTROrder = null;
	params.reveal = null;
	params.revealOrder = null;
	params.consume = null;
	params.consumeOrder = null;
	params.showROI = null;
	params.showROIOrder = null;
	params.clickOutROI = null;
	params.clickOutROIOrder = null;
	params.CPC = null;
	params.CPCOrder = null;
}

//初始化：品牌、展位、推广类型、投放人群的查询条件
function initConditions(){
	$.ajax({
		url: getContextPath() + '/action?actionid=photoMaterialAction&type=initConditions',
		type : 'GET',
		async : false,
		dataType : 'json',
		data : {},
		success:function(data){
			if(data == '-1'){
				$.ligerDialog.error('初始化条件失败.');
			}else{
				var max = getMax(data);
				//固定的列：15
				var fix = 15;
				for(var i =0;i < data.shopNameList.length;i++){
					$("#shopNameAll").append("<td class='ctd'>" + data.shopNameList[i] + "</td>");
				}
				$("#shopNameAll").last().append("<td class='ctd' colspan='" + (fix - data.shopNameList.length) +"'></td>");
				for(var j = 0;j < data.materialStandAbbreviationList.length;j++){
					$("#standAll").append("<td class='ctd'>" + data.materialStandAbbreviationList[j] + "</td>");
				}
				//$("#standAll").last().append("<td class='ctd' colspan='" + (fix - data.materialStandAbbreviationList.length) +"'></td>");
				for(var m = 0;m < data.materialThemeList.length;m++){
					$("#activityAll").append("<td class='ctd'>" + data.materialThemeList[m] + "</td>");
				}
				//$("#activityAll").last().append("<td class='ctd' colspan='" + (fix - data.materialThemeList.length) +"'></td>");
				for(var n = 0;n < data.materialCrowdList.length;n++){
					$("#putInCrowdAll").append("<td class='ctd'>" + data.materialCrowdList[n] + "</td>");
				}
				//$("#putInCrowdAll").last().append("<td class='ctd' colspan='" + (fix - data.materialCrowdList.length) +"'></td>");
			}
		},
		error:function(){
			$.ligerDialog.error('初始化条件出现异常,请重试.');
		}
	});
	//监听投放时间input value变化
	$("#dateTime").change(function(){
		params.putInDateTime = $(this).val();
	});
}

function sortMethod(arr) {
	   // 交换两个数组项的位置
	   var swap = function(firstIndex, secondIndex) {
	        var temp = arr[firstIndex];
	        arr[firstIndex] = arr[secondIndex];
	        arr[secondIndex] = temp;
	    };
	   
	   // 升序排列
	    var bubbleSort = function() {
	         var i, j, stop, len = arr.length;
	         for (i=0; i<len; i=i+1) {
	             for (j=0, stop = len - i; j<stop; j=j+1) {
	                  // 将这里的'>'换成'<'即为降序排列
	                  if (arr[j] > arr[j+1]) { swap(j, j+1); }
	             }
	         }
	     }();
	}

//获得条件中最大的值
function getMax(data){
	var arr = new Array(4);
	arr[0] = data.shopNameList.length;
	arr[1] = data.materialCrowdList.length;
	arr[2] = data.materialThemeList.length;
	arr[3] = data.materialStandAbbreviationList.length;
	sortMethod(arr);
	return arr;
}

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
		//alert("not!");
		$(val).css("color", "#6495ED");
		$(val).siblings().each(function() {
			$(this).removeAttr("style");
		});
	}
	//品牌
	if($(val).parent().index() == 0){
		if($(val).attr("style") != undefined){
			if($(val).text() == "全部"){
				params.shopName = "all";
			}else{
				params.shopName = $(val).text();
			}
		}
	}
	//展位
	if($(val).parent().index() == 1){
		if($(val).attr("style") != undefined){
			if($(val).text() == "全部"){
				params.standSize = "all";
			}else{
				params.standSize = $(val).text();
			}
		}
	}
	//推广类型
	if($(val).parent().index() == 2){
		if($(val).attr("style") != undefined){
			if($(val).text() == "全部"){
				params.activityName = "all";
			}else{
				params.activityName = $(val).text();
			}
		}
	}
	//投放人群
	if($(val).parent().index() == 3){
		if($(val).attr("style") != undefined){
			if($(val).text() == "全部"){
				params.putInCrowd = "all";
			}else{
				params.putInCrowd = $(val).text();
			}
		}
	}
	//投放时间
	if($(val).parent().index() == 4){
		if($(this).children("input").val() != ""){
			params.putInDateTime = $(this).children("input").val();
		}
	}
	params.pageNumber = 1;
	//请求分页信息
	$.ajax({
		url: getContextPath() + '/action?actionid=photoMaterialAction&type=getPaginationInfoByCondition',
		type : 'GET',
		async : false,
		dataType : 'json',
		data : params,
		success:function(data){
			//alert(data.totalRecords);
			if(data == '-1'){
				$.ligerDialog.error('加载分页信息失败.');
			}else{
				if(data.totalRecords == 0){
					$("#viewImg").empty();
					$("#viewImg").append("<span style='font-family: cursive;font-size: 18px;font-weight: bolder;color: red;width:90%; position:absolute; text-align:center;'>没有查询到对应素材记录.</span>");
					$("#paginationDiv").hide();
				}else{
					$("#viewImg").empty();
					$("#paginationDiv").show();
					getImgPaths();
					// prettyphoto 图片预览初始化
					$("a[rel*='prettyPhoto']").prettyPhoto({
						show_title : false,
						//modal: true,
						default_width: 300,
						default_height: 144
					});
					tips();
					//createPage(pageSize, buttons, total)：pageSize(每页记录数)/buttons(按钮数目)/total(记录总数)
					createPage(data.pageSize, 5, data.totalRecords);
				}
			}
		},
		error:function(){
			$.ligerDialog.error('加载分页信息出现异常,请重试.');
		}
	});
}

// get请求服务器端图片路径&名称
function getImgPaths(){
	var url = getContextPath() + "/action?actionid=photoMaterialAction&type=getImgPaths";
	$.ajax({
		url : url,
		type : 'GET',
		async : false,
		dataType : 'json',
		data : params,
		success : function(data) {
			if(data == '-1'){
				$.ligerDialog.error('获取素材图片信息失败.');
			}else{
				for (var i = 0; i < data.Rows.length; i++) {
					$("#viewImg").append("<a href='action?actionid=photoMaterialAction&type=getImageById&imageId=" + data.Rows[i].id + "' rel='prettyPhoto' title='创意名称：" + data.Rows[i].fileName + ",店铺：" +  data.Rows[i].shopName + ",活动：" + data.Rows[i].materialTheme + ",展位：" + data.Rows[i].materialStandAbbreviation + ",点击率：" + data.Rows[i].CTR + ",点击数：" + data.Rows[i].click + ",展现数：" + data.Rows[i].reveal + ",ROI：" + data.Rows[i].showRateOfReturn_15 + ",消耗：" + data.Rows[i].consume + ",投放时间：" + data.Rows[i].dateTime + ",投放人群：" + data.Rows[i].materialCrowd + ",链接页面：" + data.Rows[i].materialContinuePage + "' style='padding:15px;'><img width='160' height='160' style='padding:10px;' alt='Image not found' src='imageRepo/" + data.Rows[i].fileName + ".jpg'/></a>");
				}
			}
		},
		error : function(data) {
			$.ligerDialog.error('请求素材图片信息出现异常.');
		}
	});
}

function ajaxFileUpload(){
	//layer.load(3);
	$.ajaxFileUpload({  
	    url: getContextPath() + '/action?d='+(new Date().getMilliseconds()) + '&actionid=photoMaterialAction&type=uploadMaterialExcel',	//需要链接到服务器地址  
	    secureuri: false,  
	    fileElementId: 'fileLoad',                        //文件选择框的id属性  
	    dataType: 'JSON',                                     //服务器返回的格式，可以是json  
	    success: function (data, status){
	    	//layer.load(3);
	    	 if(data == '0'){
	    		 //$.ligerDialog.success(content, title, onBtnClick)	 	成功提示框
	    		 $.ligerDialog.success('文件上传成功.','上传提示',forwardToMaterialPage());
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
        	error: function (data, status, e){ 
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

function tips(){
	var tipDiv;
		//为每个图片创建提示
		$("#viewImg a").each(function() {
					var hrefAttr = $(this).attr("href");
					var qImgId = hrefAttr.substring(hrefAttr.lastIndexOf('&') + 1, hrefAttr.length).split('=')[1];
					//根据素材图片名称请求素材提示内容
					$.ajax({
						url: getContextPath() + '/action?actionid=photoMaterialAction&type=getQImgInfo',
						type : 'GET',
						async : false,
						dataType : 'json',
						data : {
							qImgId: qImgId
						},
						success:function(data){
							if(data == '-1'){
								$.ligerDialog.error('创建素材Tips失败.');
							}else{
								tipDiv = "<div style='width: 380px;height: 175px;font-family: cursive;font-size: 8px;font-weight: bold;'><table cellpadding='0' cellspacing='0' name='tables'><tr style='height:40px;'><td id='shop'>" + data.shop + "</td><td id='title'>" + data.title + "</td><td id='imageSize' style='text-algin: right;'>&nbsp;&nbsp;&nbsp;&nbsp;" + data.imageSize + "</td></tr><tr style='height:20px;'><td>点击率：</td><td colspan='2' id='CTR'>" + data.CTR + "%</td></tr><tr style='height:20px;'><td>ROI：</td><td colspan='2' id='ROI'>" + data.ROI + "%</td></tr><tr style='height:20px;'><td>投放时间：</td><td colspan='2' id='putInTime'>" + data.putInTime + "</td></tr><tr style='height:20px;'><td>投放人群：</td><td colspan='2' id='putInCrowd'>" + data.putInCrowd + "</td></tr><tr style='height:20px;'><td>链接页面：</td><td colspan='2' id='linkAddress'>" + data.linkAddress + "</td></tr><tr style='height:20px;'><td>创意名称：</td><td colspan='2' id='originalityName'>" + data.originalityName + "</td></tr></table></div>"
							}
						},
						error:function(){
							$.ligerDialog.error('创建素材Tips出现异常,请重试.');
						}
					});
					//提示框显示位置判断：提示框位置默认为右侧显示，如果是每行的最后两个素材时提示框显示在素材图片左侧
					//先来判读分辨率
					if(screen.height == 900 && screen.width == 1600){
						if($(this).index() == 5 || $(this).index() == 6 || $(this).index() == 12 || $(this).index() == 13 ||  $(this).index() == 19 || $(this).index() == 20){
							$(this).qtip({
								content : {
									text : tipDiv,	//qTips提示内容
								},
								position : {			//qTips提示框位置
									my : 'right center',
									at : 'left top'
								// target: 'mouse'	//qTips是否跟随鼠标
								},
								style : {			//qTips样式
									classes : 'qtip-blue ui-tooltip-rounded qtip-bootstrap'
								},
								show : {			//是否延迟加载qTips
									delay : 100
								}
							});
						}else{
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
						}
					}
					if(screen.height == 1080 && screen.width == 1920){
						if($(this).index() == 6 || $(this).index() == 7 || $(this).index() == 14 ||  $(this).index() == 15){
							$(this).qtip({
								content : {
									text : tipDiv,	//qTips提示内容
								},
								position : {			//qTips提示框位置
									my : 'right center',
									at : 'left top'
								// target: 'mouse'	//qTips是否跟随鼠标
								},
								style : {			//qTips样式
									classes : 'qtip-blue ui-tooltip-rounded qtip-bootstrap'
								},
								show : {			//是否延迟加载qTips
									delay : 100
								}
							});
						}else{
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
						}
					}
				});
}

//页面素材图片创建成功后开始创建分页组件
function createPage(pageSize, buttons, total) {
    $(".pagination").jBootstrapPage({
        pageSize : pageSize,
        total : total,
        maxPageButton:buttons,
        onPageClicked: function(obj, pageIndex) {
          $('#pageIndex').html('您选择了第<font color=red>'+(pageIndex+1)+'</font>页');
          //添加分页时选择的条件
          conditionParams();
          params.pageNumber = pageIndex + 1;
          $.ajax({
      		url: getContextPath() + '/action?actionid=photoMaterialAction&type=getImgPaths',
      		type : 'GET',
      		async : false,
      		dataType : 'json',
      		data : params,
      		success:function(data){
      			if(data == '-1'){
      				$.ligerDialog.error('加载分页信息失败.');
      			}else{
      				$("#viewImg").empty();
      				for (var i = 0; i < data.Rows.length; i++) {
      					$("#viewImg").append("<a href='action?actionid=photoMaterialAction&type=getImageById&imageId=" + data.Rows[i].id + "' rel='prettyPhoto' title='创意名称：" + data.Rows[i].fileName + ",店铺：" +  data.Rows[i].shopName + ",活动：" + data.Rows[i].materialTheme + ",展位：" + data.Rows[i].materialStandAbbreviation + ",点击率：" + data.Rows[i].CTR + ",点击数：" + data.Rows[i].click + ",展现数：" + data.Rows[i].reveal + ",ROI：" + data.Rows[i].showRateOfReturn_15 + ",消耗：" + data.Rows[i].consume + ",投放时间：" + data.Rows[i].dateTime + ",投放人群：" + data.Rows[i].materialCrowd + ",链接页面：" + data.Rows[i].materialContinuePage + "' style='padding:15px;'><img width='160' height='160' style='padding:10px;' alt='Image not found' src='imageRepo/" + data.Rows[i].fileName + ".jpg'/></a>");
      				}
      				//createPage(pageSize, buttons, total)：pageSize(每页记录数)/buttons(按钮数目)/total(记录总数)
      				//createPage(data.pageSize, 5, data.totalRecords);
      				// prettyphoto 图片预览初始化
      				$("a[rel*='prettyPhoto']").prettyPhoto({
      					show_title : false,
      					//modal: true,
      					default_width: 300,
      					default_height: 144
      				});
      				tips();
      			}
      		},
      		error:function(){
      			$.ligerDialog.error('加载分页信息出现异常,请重试.');
      		}
      	});
        }
    });
}

//封装任何点击事件的参数
function conditionParams(){
	//找出所有被选中的条件
	$("#materialCondition tr td").not($("#materialCondition tr td:nth-child(1)")).each(function() {
		//品牌
		if($(this).parent().index() == 0){
			if($(this).attr("style") != undefined){
				if($(this).text() == "全部"){
					params.shopName = "all";
				}else{
					params.shopName = $(this).text();
				}
			}
		}
		//展位
		if($(this).parent().index() == 1){
			if($(this).attr("style") != undefined){
				if($(this).text() == "全部"){
					params.standSize = "all";
				}else{
					params.standSize = $(this).text();
				}
			}
		}
		//推广类型
		if($(this).parent().index() == 2){
			if($(this).attr("style") != undefined){
				if($(this).text() == "全部"){
					params.activityName = "all";
				}else{
					params.activityName = $(this).text();
				}
			}
		}
		//投放人群
		if($(this).parent().index() == 3){
			if($(this).attr("style") != undefined){
				if($(this).text() == "全部"){
					params.putInCrowd = "all";
				}else{
					params.putInCrowd = $(this).text();
				}
			}
		}
		//投放时间
		if($(this).parent().index() == 4){
			if($("#dateTime").val() != ""){
				params.putInDateTime = $("#dateTime").val();
			}
		}
	});
	$("#conditionDiv span").each(function(){
		if($(this).attr("style") != undefined){
			if($(this).index() == 0){
				params.CTR = "CTR";
				params.CTROrder = "desc";
			}
			if($(this).index() == 1){
				params.reveal = "reveal";
				params.revealOrder = "desc";
			}
			if($(this).index() == 2){
				params.consume = "consume";
				params.consumeOrder = "desc";
			}
			if($(this).index() == 3){
				params.showROI = "showRateOfReturn_15";
				params.showROIOrder = "desc";
			}
			if($(this).index() == 4){
				params.clickOutROI = "rateOfReturn_15";
				params.clickOutROIOrder = "desc";
			}
			if($(this).index() == 5){
				params.CPC = "unitPriceOfClick";
				params.CPCOrder = "desc";
			}
		}
	});
}

function findByName(){
	var imgName = $("#search").val();
	var url = getContextPath() + "/action?actionid=photoMaterialAction&type=findByName";
	$.ajax({
		url : url,
		type : 'GET',
		async : false,
		dataType : 'json',
		data : {
			pageNumber: 1,
			originalityName: imgName
		},
		success : function(data) {
			if(data == '-1'){
				$.ligerDialog.error('获取素材图片信息失败.');
			}else{
				for (var i = 0; i < data.Rows.length; i++) {
					$("#viewImg").append("<a href='action?actionid=photoMaterialAction&type=getImageById&imageId=" + data.Rows[i].id + "' rel='prettyPhoto' title='创意名称：" + data.Rows[i].fileName + ",店铺：" +  data.Rows[i].shopName + ",活动：" + data.Rows[i].materialTheme + ",展位：" + data.Rows[i].materialStandAbbreviation + ",点击率：" + data.Rows[i].CTR + ",点击数：" + data.Rows[i].click + ",展现数：" + data.Rows[i].reveal + ",ROI：" + data.Rows[i].showRateOfReturn_15 + ",消耗：" + data.Rows[i].consume + ",投放时间：" + data.Rows[i].dateTime + ",投放人群：" + data.Rows[i].materialCrowd + ",链接页面：" + data.Rows[i].materialContinuePage + "' style='padding:15px;'><img width='160' height='160' style='padding:10px;' alt='Image not found' src='imageRepo/" + data.Rows[i].fileName + ".jpg'/></a>");
				}
			}
		},
		error : function(data) {
			$.ligerDialog.error('请求素材图片信息出现异常.');
		}
	});
}

//搜索按钮点击事件
function searchBtn(){
	//alert("searchBtn!!!");
	var imgName = $("#search").val();
	//请求分页信息
	$.ajax({
		url: getContextPath() + '/action?actionid=photoMaterialAction&type=getPaginationInfoByName',
		type : 'GET',
		async : false,
		dataType : 'json',
		data : {
			pageNumber: 1,
			originalityName: imgName
		},
		success:function(data){
			//alert(data.totalRecords);
			if(data == '-1'){
				$.ligerDialog.error('加载分页信息失败.');
			}else{
				if(data.totalRecords == 0){
					//alert("无记录！");
					$("#viewImg").empty();
					$("#viewImg").append("<span style='font-family: cursive;font-size: 18px;font-weight: bolder;color: red;width:100%; position:absolute; text-align:center;'>没有查询到对应素材记录.</span>");
					$("#paginationDiv").hide();
				}else{
					$("#viewImg").empty();
					$("#paginationDiv").show();
					findByName();
					// prettyphoto 图片预览初始化
					$("a[rel*='prettyPhoto']").prettyPhoto({
						show_title : false,
						//modal: true,
						default_width: 300,
						default_height: 144
					});
					tips();
					//createPage(pageSize, buttons, total)：pageSize(每页记录数)/buttons(按钮数目)/total(记录总数)
					createPage(data.pageSize, 5, data.totalRecords);
				}
			}
		},
		error:function(){
			$.ligerDialog.error('加载分页信息出现异常,请重试.');
		}
	});
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
	if($("#select").val() == 2){
		//询问框
		layer.confirm('批量导出图片',{
			title: '批量导出图片',
			closeBtn: false,
			btn: ['确定','取消'] //按钮
		},function(index,layerobj){
			//批量下载素材图片
			var form = $("#downloadForm");
			form.attr('target','');
			form.attr('method','POST');
			form.attr('action',getContextPath()+'/action?actionid=photoMaterialAction&type=batchDownloadMaterialImage');
			var inputs = $("#downloadForm").children();
			inputs[0].value = params.shopName;
			inputs[1].value = params.standSize;
			inputs[2].value = params.activityName;
			inputs[3].value = params.putInCrowd
			inputs[4].value = params.putInDateTime;
			inputs[5].value = params.CTR;
			inputs[6].value = params.CTROrder;
			inputs[7].value = params.reveal;
			inputs[8].value = params.revealOrder;
			inputs[9].value = params.consume;
			inputs[10].value = params.consumeOrder;
			inputs[11].value = params.showROI;
			inputs[12].value = params.showROIOrder;
			inputs[13].value = params.clickOutROI;
			inputs[14].value = params.clickOutROIOrder;
			inputs[15].value = params.CPC;
			inputs[16].value = params.CPCOrder;
			form.submit();
			layer.msg('图片导出成功', {icon: 1});
		},function(index,layerobj){
			layer.msg('取消', {icon: 1},{shift: 0});
		});
	}
	if($("#select").val() == 3){
		//询问框
		layer.confirm('请选择导出报表类型:自用/客户', {
			title: '导出报表类型',
			closeBtn: false,
			btn: ['客户','自用','取消'] //按钮
			,btn3: function(index,layerobj){
				//取消按钮的回调
				layer.msg('取消', {icon: 1});
			}
		}, function(index,layerobj){
			//客户报表下载
			var form = $("#downloadForm");
			form.attr('target','');
			form.attr('method','POST');
			form.attr('action',getContextPath()+'/action?actionid=photoMaterialAction&type=downloadCustomerExcel');
			var inputs = $("#downloadForm").children();
			inputs[0].value = params.shopName;
			inputs[1].value = params.standSize;
			inputs[2].value = params.activityName;
			inputs[3].value = params.putInCrowd
			inputs[4].value = params.putInDateTime;
			inputs[5].value = params.CTR;
			inputs[6].value = params.CTROrder;
			inputs[7].value = params.reveal;
			inputs[8].value = params.revealOrder;
			inputs[9].value = params.consume;
			inputs[10].value = params.consumeOrder;
			inputs[11].value = params.showROI;
			inputs[12].value = params.showROIOrder;
			inputs[13].value = params.clickOutROI;
			inputs[14].value = params.clickOutROIOrder;
			inputs[15].value = params.CPC;
			inputs[16].value = params.CPCOrder;
			form.submit();
		    layer.msg('客户报表下载完成', {icon: 1});
		}, function(index,layerobj){
			//自用报表下载
			var form = $("#downloadForm");
			form.attr('target','');
			form.attr('method','POST');
			form.attr('action',getContextPath()+'/action?actionid=photoMaterialAction&type=downloadSelfExcel');
			var inputs = $("#downloadForm").children();
			inputs[0].value = params.shopName;
			inputs[1].value = params.standSize;
			inputs[2].value = params.activityName;
			inputs[3].value = params.putInCrowd
			inputs[4].value = params.putInDateTime;
			inputs[5].value = params.CTR;
			inputs[6].value = params.CTROrder;
			inputs[7].value = params.reveal;
			inputs[8].value = params.revealOrder;
			inputs[9].value = params.consume;
			inputs[10].value = params.consumeOrder;
			inputs[11].value = params.showROI;
			inputs[12].value = params.showROIOrder;
			inputs[13].value = params.clickOutROI;
			inputs[14].value = params.clickOutROIOrder;
			inputs[15].value = params.CPC;
			inputs[16].value = params.CPCOrder;
			form.submit();
			layer.msg('自用报表下载完成', {icon: 1},{shift: 0});
		});
	}
}