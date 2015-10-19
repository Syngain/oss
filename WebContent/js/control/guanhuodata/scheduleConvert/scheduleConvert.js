//导入按钮点击事件
function importChartBtn() {
	if ($("#fileLoad").val() == "") {
		//$.ligerDialog.error('请选择需要上传的报表文件.');
		layer.tips('请选择需要上传的报表文件!','#importChartBtn',{
			tips: [2,'##FF6347']
		});
		return;
	} else {
		//先请求后台查看是否session中包含上传后的zip文件
		getFileNameInfoFromSession();
	}
}

function getFileNameInfoFromSession(){
	$.ajax({
		url : getContextPath() + '/action?actionid=scheduleConvertAction&type=getFileNameInfoFromSession',
		type : 'GET',
		async : false,
		dataType : 'json',
		data : {
		},success:function(data){
			if(data == '0'){
				ajaxFileUpload();
			}
			if(data == '-1'){
				layer.msg('请先选择素材图片，得到创意名称后再来导入吧',{icon: 5});
			}if(data == '-2'){
				layer.msg('获取创意名称失败',{icon: 5});
			}
		},error:function(data){
			layer.msg('获取创意名称出现异常，请重试',{icon: 5});
		}
	});
}

function ajaxFileUpload() {
	$.ajaxFileUpload({
		url : getContextPath() + '/action?d=' + (new Date().getMilliseconds()) + '&actionid=scheduleConvertAction&type=handleScheduleChart', //需要链接到服务器地址  
		secureuri : false,
		fileElementId : 'fileLoad', //文件选择框的id属性  
		dataType : 'JSON', //服务器返回的格式，可以是json  
		success : function(data, status) {
			//layer.load(3);
			if (data == '0') {
				/*$.ligerDialog.success('文件上传成功.');*/
				var form = $("#downloadForm");
				form.attr('target','');
				form.attr('method','POST');
				form.attr('action',getContextPath()+'/action?actionid=scheduleConvertAction&type=downloadConvertedChart');
				form.submit();
			}
			if (data == '-1') {
				$.ligerDialog.warn('上传报表不是Excel文件，目前只支持Excel，其他文件上传功能后续开放.');
			}
			if (data == '-2') {
				$.ligerDialog.error('报表文件上传出现异常.');
			}
		},
		error : function(data, status, e) {
			$.ligerDialog.error('上传报表文件失败.');
		}
	});
}

//读取按钮的点击事件
function readFileNameBtn(){
	layer.open({
	    type: 2,
	    title: '创意名称读取页面',
	    shadeClose: false,
	    shade: 0.8,
	    //closeBtn: 2,
	    area: ['400px', '60%'],
	    content: getContextPath() + "/action?actionid=forwardAction&type=readFileNameInfoPage",
	    cancel: function(index){
	    	forwardToScheduleConvertUtil();
	    }
	}); 
	/*if ($("#fileNameLoad").val() == "") {
		//$.ligerDialog.error('请选择需要上传的报表文件.');
		layer.tips('请选择需要上传的素材图片压缩包!','#readFileNameBtn',{
			tips: [2,'##FF6347']
		});
		return;
	}*/
}