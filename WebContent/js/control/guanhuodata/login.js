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
	if (!username || (username && username === usernameObj.attr('title')))
		errText.push('用户名');
	if (!password && password === passwordObj.attr('title'))
		errText.push('密码');
	if (!imgCode || (imgCode && imgCode === imgCodeObj.attr('title')))
		errText.push('验证码');
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
});
function changeImgCode(imgCode){
//	window.location.href="login.html";
	$("img[name=imgPhoto]").attr('src','action?actionid=image&t='+ Math.random());
}