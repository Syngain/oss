var params = {};
var originalityParams = {};
var arr = new Array();
var originalityArray = new Array();
$(function(){
	
	/*$("#summarySheetTitle :input").each(function(){
		alert($(this).val());
	});*/
	
	//$("input[type='checkbox']").each(function(){
	$("#summarySheetTitle :input").each(function(){
		$(this).click(function(){
			if($(this).attr("checked") == "checked"){
				$(this).removeAttr("checked");
				arr.pop($(this).attr("name"));
				/*var s = $(this).attr("name");
				delete arr.s;*/
				delete params[$(this).attr("name")];
				var name = $(this).val();
				$(".seq").each(function(){
					if($(this).text() == name)
						$(this).remove();
				});
			}else{
				$(this).attr("checked","checked");
				arr.push($(this).attr("name"));
				$("#conditionSequence").append("<span style='color: red;' class='seq'>" + $(this).val() + "</span>");
			}
		});
	});
	//$("input[type='checkbox']").each(function(){
	$("#originalitySummarySheetTitle :input").each(function(){
		$(this).click(function(){
			if($(this).attr("checked") == "checked"){
				$(this).removeAttr("checked");
				originalityArray.pop($(this).attr("name"));
				/*var s = $(this).attr("name");
				delete arr.s;*/
				delete originalityParams[$(this).attr("name")];
				var name = $(this).val();
				$(".seqo").each(function(){
					if($(this).text() == name)
						$(this).remove();
				});
			}else{
				$(this).attr("checked","checked");
				originalityArray.push($(this).attr("name"));
				$("#oconditionSequence").append("<span style='color: red;' class='seqo'>" + $(this).val() + "</span>");
			}
		});
	});
});

function getTitles(){
	params = {};
	if(arr.length > 0){
		for(var i =0;i< arr.length;i++){
			params[arr[i]] = arr[i];
			//params[name] = arr[i];
		}
	}else{
		$.ligerDialog.error('请选择需要汇总的表头.');
	}
}

function getOriginalityTitles(){
	originalityParams = {};
		if(originalityArray.length > 0){
			for(var i =0;i< originalityArray.length;i++){
				originalityParams[originalityArray[i]] = originalityArray[i];
				//params[name] = arr[i];
			}
		}else{
			$.ligerDialog.error('请选择需要汇总的表头.');
		}
	}

//定向报表导入按钮点击事件
function importDirectFileBtn() {
	if ($("#directFileUpload").val() == "") {
		$.ligerDialog.error('请选择需要上传的定向报表.');
		return;
	} else {
		getTitles();
		var s = "";
		for(var param in params){
			//alert(params[param]);
			s += params[param] + ",";
		}
		var paramPoly = s.substring(0,s.length - 1);
		//alert(paramPoly);
		/*var param;
		for(var i = 0;i < arr.length;i++){
			alert(arr[i]);
		}*/
		/*for(var param in params){
			alert(params[param]);
		}*/
		ajaxFileUpload_direct(paramPoly);
	}
}

//创意报表导入按钮点击事件
function importOriginalityFileBtn() {
	if ($("#originalityFileUpload").val() == "") {
		$.ligerDialog.error('请选择需要上传的创意报表.');
		return;
	} else {
		getTitles();
		var s = "";
		for(var param in params){
			//alert(params[param]);
			s += params[param] + ",";
		}
		var paramPoly = s.substring(0,s.length - 1);
		ajaxFileUpload_originality(paramPoly);
	}
}

function ajaxFileUpload_direct(paramPoly) {
	$.ajaxFileUpload({
		url : getContextPath() + '/action?d=' + (new Date().getMilliseconds()) + '&actionid=summarySheetAction&type=uploadDirectSheet', //需要链接到服务器地址  
		secureuri : false,
		fileElementId : 'directFileUpload', //文件选择框的id属性  
		dataType : 'JSON', //服务器返回的格式，可以是json  
		success : function(data, status) {
			//layer.load(3);
			if (data == '0') {
				//$.ligerDialog.success('文件上传成功.');
				var form = $("#downloadForm");
				form.attr('target','');
				form.attr('method','POST');
				form.attr('action',getContextPath()+'/action?actionid=summarySheetAction&type=downloadDirectChart&paramPoly=' + paramPoly);
				form.submit();
			}
			if (data == '-1') {
				$.ligerDialog.warn('上传报表不是Excel文件，目前只支持Excel，其他文件上传功能后续开放.');
			}
			if (data == '-2') {
				$.ligerDialog.error('定向报表上传出现异常.');
			}
			if (data == '-3') {
				$.ligerDialog.error('上传报表表头不包含定向名称无法解析(UNKNOWN).');
			}
			if (data == '-4') {
				$.ligerDialog.error('上传报表成功,但是写入数据库失败(UNKNOWN).');
			}
		},
		error : function(data, status, e) {
			$.ligerDialog.error('定向报表上传失败.');
		}
	});
}

function ajaxFileUpload_originality(paramPoly) {
	$.ajaxFileUpload({
		url : getContextPath() + '/action?d=' + (new Date().getMilliseconds()) + '&actionid=summarySheetAction&type=uploadOriginalitySheet', //需要链接到服务器地址  
		secureuri : false,
		fileElementId : 'originalityFileUpload', //文件选择框的id属性  
		dataType : 'JSON', //服务器返回的格式，可以是json  
		success : function(data, status) {
			//layer.load(3);
			if (data == '0') {
				/*$.ligerDialog.success('文件上传成功.');*/
				var form = $("#downloadForm");
				form.attr('target','');
				form.attr('method','POST');
				form.attr('action',getContextPath()+'/action?actionid=summarySheetAction&type=downloadOriginalityChart&paramPoly=' + paramPoly);
				form.submit();
			}
			if (data == '-1') {
				$.ligerDialog.warn('上传报表不是Excel文件，目前只支持Excel，其他文件上传功能后续开放.');
			}
			if (data == '-2') {
				$.ligerDialog.error('创意报表上传出现异常.');
			}
			if (data == '-3') {
				$.ligerDialog.error('上传报表表头不包含创意名称无法解析(UNKNOWN).');
			}
		},
		error : function(data, status, e) {
			$.ligerDialog.error('创意报表上传请求失败.');
		}
	});
}

function exportDirectFileBtn(){
	getTitles();
	var s = "";
	for(var param in params){
		//alert(params[param]);
		s += params[param] + ",";
	}
	var paramPoly = s.substring(0,s.length - 1);
	if(paramPoly == null || paramPoly == ''){
		$.ligerDialog.error('请选择需要汇总的表头.');
	}else{
		var form = $("#downloadForm");
		form.attr('target','');
		form.attr('method','POST');
		form.attr('action',getContextPath()+'/action?actionid=summarySheetAction&type=downloadDirectChart&paramPoly=' + paramPoly);
		form.submit();
	}
}

function exportOriginalityFileBtn(){
	getTitle();
}