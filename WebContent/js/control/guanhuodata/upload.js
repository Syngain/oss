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
$(function(){
	$("#uploadBtn").click(function(){
    	if($("#fileLoad").val() == ""){
    		 $.ligerDialog.warn('请选择需要上传的报表文件.');
    		return;
    	}else{
    		ajaxFileUpload();
    	}
    });
});