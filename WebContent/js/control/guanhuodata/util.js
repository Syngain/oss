function getContextPath() {
  var wl = window.location;
  var contextPath = wl.pathname.split("/")[1];
  return contextPath.length < 0 ? '' : '/' + contextPath;
}

function forwardToChartPerspective(){
	window.location.href = getContextPath() + '/action?actionid=forwardAction&type=chartPerspectivePage';
}

function forwardToChartImpAndExp(){
	window.location.href = getContextPath() + '/action?actionid=forwardAction&type=chartImpAndExpPage';
}

function forwardToMaterialPage(){
	window.location.href = getContextPath() + '/action?actionid=forwardAction&type=photoMaterialPage';
}

function createMenu(){
	$("body").append("<header><div class='wrapper'><div class='wrapper'><div class='brand'></div><div class='rm-container'><a class='rm-toggle rm-button rm-nojs' href='#''>导航菜单</a>")
	.append("<nav class='rm-nav rm-nojs rm-lighten'><ul><li><a href='#'>系统管理</a><ul><li><a href='#'>角色管理</a><ul><li><a href='#'>角色列表</a></li></ul></li><li><a href='#'>用户管理</a>")
	.append("<ul><li><a href='#'>用户列表</a></li></ul></li><li><a href='#'>暂未开放菜单</a><ul><li><a href='#'>暂未开放子菜单</a></li><li><a href='#'>暂未开放子菜单</a></li>")
	.append("<li><a href='#'>暂未开放子菜单</a></li><li><a href='#'>暂未开放子菜单</a></li><li><a href='#'>暂未开放子菜单</a></li></ul></li><li><a href='#'>暂未开放菜单</a></li>")
	.append("<li><a href='#'>暂未开放菜单</a></li></ul></li><li><a href='#'>报表分析</a><ul><li><a href='excelDocumentUpload.html' id='excelManager'>Excel报表管理</a></li><li><a href='#excelAnalysis'>Excel报表分析</a>")
	.append("<ul><li><a href='#'>饼状图分析</a></li><li><a href='#'>折线图分析</a></li><li><a href='#'>暂未开放子菜单</a></li><li><a href='#'>暂未开放子菜单</a></li><li><a href='#'>暂未开放子菜单</a></li>")
	.append("</ul></li><li><a href='#'>暂未开放菜单</a></li><li><a href='#'>暂未开放菜单</a></li><li><a href='#'>暂未开放菜单</a></li></ul></li><li><a href='#'>素材管理</a><ul>")
	.append("<li><a href='#'>素材列表</a></li><li><a href='#'>素材分析</a></li><li><a href='#'>暂未开放菜单</a><ul><li><a href='#'>暂未开放子菜单</a></li><li><a href='#'>暂未开放子菜单</a></li>")
	.append("<li><a href='#'>暂未开放子菜单</a></li></ul></li><li><a href='#'>暂未开放菜单</a></li><li><a href='#'>暂未开放菜单</a></li></ul></li><li><a href='#'>其他功能</a></li>")
	.append("<li><a href='#'>其他功能</a><ul><li><a href='#'>暂未开放子菜单</a></li><li><a href='#'>暂未开放子菜单</a><ul><li><a href='#'>暂未开放子菜单</a></li><li><a href='#'>暂未开放子菜单</a></li>")
	.append("<li><a href='#'>暂未开放子菜单</a></li></ul></li><li><a href='#'>暂未开放子菜单</a></li><li><a href='#'>暂未开放子菜单</a></li><li><a href='#'>暂未开放子菜单</a></li></ul></li>")
	.append("</ul></nav></div></div></header>");
}