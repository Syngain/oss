$(function() {
	var usernameObj = $("input[name=userName]"), 
	imgCodeObj = $("input[name=imgCode]"), 
	username = usernameObj.val(),
	passwordObj = $("input[name=password]"),
	password = $("input[name=password]").val(), 
	imgCode = imgCodeObj.val(), 
	errTextPre = '请输入', 
	errText = [], 
	returnVal = null;
	/*if (!username || (username && username === usernameObj.attr('title')))
		errText.push('用户名');
	if (!password && password === passwordObj.attr('title'))
		errText.push('密码');
	if (!imgCode || (imgCode && imgCode === imgCodeObj.attr('title')))
		errText.push('验证码');*/
	$(usernameObj).focus(function(){
		$(this).val("");
	});
	$(passwordObj).focus(function(){
		//alert($(this).text());
		$(this).val("");
	});
	$(imgCodeObj).focus(function(){
		$(this).val("");
	});
	/*$(usernameObj).blur(function(){
		$(this).val("用户名");
	});
	$(passwordObj).blur(function(){
		$(this).val("密码");
	});
	$(imgCodeObj).blur(function(){
		$(this).val("验证码");
	});*/
	$.ajax({
		url : getContextPath() + '/action?actionid=image&type=check&imageCoke=' + imgCode,
		type : 'GET',
		async : false,
		dataType : 'text',
		data : {
		},success:function(data){
		},error:function(data){
		}
	});
	$("img[name=imgPhoto]").click(function(){
		changeImgCode(imgCode);
	});
	$("#loginBtn").click(function(){
		login();
	});
	document.onkeydown = function(e){
		if(e.keyCode == 13){
			login();
		}
	}
});
function changeImgCode(imgCode){
//	window.location.href="login.html";
	$("img[name=imgPhoto]").attr('src','action?actionid=image&t='+ Math.random());
}
function login(){
	var usernameObj = $("input[name=userName]"), 
	imgCodeObj = $("input[name=imgCode]"), 
	username = usernameObj.val(),
	passwordObj = $("input[name=password]"),
	password = $("input[name=password]").val(), 
	imgCode = imgCodeObj.val();
	if(username != '' && password != '' && imgCode != ''){
		$.ajax({
			url : getContextPath() + '/action?actionid=login',
			type : 'GET',
			async : false,
			dataType : 'json',
			data : {
				username: username,
				password: password,
				imgCode: imgCode
			},success:function(data){
				if(data == '0'){
					window.location.href = getContextPath() + '/action?actionid=forwardAction&type=forwardToMainPage';
				}
				if(data == '-1'){
					layer.msg('验证码不正确,请重新输入',{icon: 5},function(){
						//关闭后
						window.location.href = getContextPath() + '/action?actionid=forwardAction&type=enterCheck';
					});
				}
				if(data == '-2'){
					//关闭后
					layer.msg('密码不正确,请重试',{icon: 5},function(){
						window.location.href = getContextPath() + '/action?actionid=forwardAction&type=enterCheck';
					});
				}
				if(data == '-3'){
					//关闭后
					layer.msg('用户名不正确,请重试',{icon: 5},function(){
						window.location.href = getContextPath() + '/action?actionid=forwardAction&type=enterCheck';
					});
				}
			},error:function(data){
				layer.msg('登陆出现异常,请重试',{icon: 5});
			}
		});
	}else{
		layer.msg('登陆信息不完整,请继续填写',{icon: 5});
	}
}