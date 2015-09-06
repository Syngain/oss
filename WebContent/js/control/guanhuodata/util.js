/**
 * 全局命名空间
 * @type {*|Object}
 */
var SX = window.SX || {};
/**
 * 全局缓存命名空间，可以在其中缓存全局公用的
 * @type {Object}
 */
SX.cache = {};
/**
 * 子模块js类命名空间
 * @type {Object}
 */
SX.Model = {};
/**
 * 自定义的公共方法、组件的命名空间
 * @type {Object}
 */
SX.Util = {};

/**
 * [applyPopWindow 加载弹窗,top/left/bottom/right只需传入一对值,例如top,left]
 * @param  {[Object]} p [参数对象]
 * @param {[Number]} p.width [宽度]
 * @param {[Number]} p.height [高度]
 * @param {[Number]} p.top [距离浏览器上边的高度, 默认居中]
 * @param {[Number]} p.left [距离浏览器左边的高度, 默认居中]
 * @param {[Number]} p.bottom [距离浏览器下边的高度]
 * @param {[Number]} p.right [距离浏览器右边的高度]
 * @param {[String]} p.url [将要加载的静态页片段地址]
 * @param {[String]} [p.content] [将要加载的html字符串]
 * @param {[String]} p.title [弹窗标题]
 * @param {[String]} p.icon [弹窗icon]
 * @param {[String]} p.footer [弹窗底部按钮]
 * @param {[Object]} p.onLoad [加载完成后的回调函数,会将弹窗对象传入,可以据此比例弹窗内的dom并初始化内容]
 * @param {[Object]} p.onCloseWin [关闭弹窗的自定义事件]
 * @param {[Object]} p.data [传给弹窗的数据]
 * @return {[Null]}   [description]
 */
(function($) {
  $.fn.applyPopWindow = function(p) {
    var defaults = {
      htmlFrame: 'model/template/window.html',
      title: '提示信息',
      ico: '',
      width: 800,
      height: 600,
      top: document.body.offsetHeight / 2,
      left: document.body.offsetWidth / 2,
      footer: '',
      container: 'body',
      // 挂到哪个容器下
      mask: true //是否有遮罩
    };
    this.each(function() {
      p = $.extend({}, defaults, p || {});
      var html_content = this;
      // 公共方法
      var g = {
        /**
         * [loading 加载弹窗页面]
         * @return {[Null]} [无返回]
         */
        loading: function() {
          // $(html_content).load(p.htmlFrame, {}, function(){
          // , "<div class='tip_window_header'>", "<div class='tip_window_header_left'>", "<img src='" + p.icon + "' />", "</div>", "<div class='tip_window_header_center'>", "<label>" + p.title + "</label>", "</div>", "<div class='tip_window_header_right'>", "</div>", "</div>", "<div class='tip_window_content'></div>", "<div class='tip_window_footer'>", "<div class='tip_window_footer_left'></div>", "<div class='tip_window_footer_center'>" + p.footer + "</div>", "<div class='tip_window_footer_right'></div>", "</div>", "</div>"
          // var dialog = $('<div class="mauto bcFFF windowBox l-dialog"><div class="tit1 cFFF overh l-dialog-tc-inner"><div class="l-dialog-icon ' + p.ico + '"></div><h3 class="l-dialog-title"></h3><div class="l-dialog-winbtns"><div class="l-dialog-winbtn l-dialog-close"></div></div></div><div class="l-dialog-body"><div class="l-dialog-image"></div><div class="l-dialog-content"></div><div class=" bcBlue l-dialog-buttons"><div class="l-dialog-buttons-inner"></div></div></div></div>');
          var html = $('<div class="l-dialog l-dialog-win"></div>');
          g.window = html;
          g.window.append('<table class="l-dialog-table" cellpadding="0" cellspacing="0" border="0"><tbody><tr><td class="l-dialog-tc"><div class="l-dialog-tc-inner"><div class="l-dialog-icon"></div><div class="l-dialog-title"></div><div class="l-dialog-winbtns"><div class="l-dialog-winbtn l-dialog-close"></div></div></div></td></tr><tr><td class="l-dialog-cc"><div class="l-dialog-body"><div class="l-dialog-image"></div> <div class="l-dialog-content"></div><div class="l-dialog-buttons"><div class="l-dialog-buttons-inner"></div></td></tr></tbody></table>');
          g.window.content = $(".l-dialog-body", g.window);
          g.window.header = $(".l-dialog-tc-inner", g.window);

          if(p.url) {
            $(g.window.content).load(p.url, {}, function() {
              var w = $(g.window);
              g.init(w);
              if(p.onLoad) p.onLoad(w, p.data, g);
            });
          } else if(p.content) {
            $(g.window.content).html(p.content);
            var w = $(g.window);
            g.init(w);
            if(p.onLoad) p.onLoad(w, p.data, g);
          }
          // });
        },
        /**
         * [init 初始化弹窗样式和基本事件]
         * @param  {[Object]} window [弹窗对象]
         * @return {[Null]}        [无返回]
         */
        init: function(window) {
          g.switchWindow(window[0]);
          $(p.container).append(window);

          g.window.show();

          //遮罩层
          if(p.mask) {
            g.mask(window);
          }

          // css
          if(p.left) window.css('left', p.left - p.width / 2);
          if(p.right) window.css('right', p.right);
          if(p.top) window.css('top', p.top - p.height / 2);
          if(p.bottom) window.css('bottom', p.bottom);
          if(p.width) window.width(p.width).find('.l-dialog-title').width(p.width - 75).end().find('.tip_window_content').width(p.width).end().find('.tip_window_footer_center').width(p.width - 16);
          if(p.height) g.window.content.height(p.height - 40);
          if(p.title) $(".l-dialog-title", g.window.header).html(p.title);

          // 拖动支持
          if($.fn.ligerDrag) {
        	 if('版本生成'!=p.title&&'应用组同步'!=p.title){
        	  window.ligerDrag({
              handler: '.l-dialog-title',
              onStartDrag: function() {
                g.switchWindow(window[0]);
                window.addClass("l-window-dragging");
                g.window.content.children().hide();
              },
              onStopDrag: function() {
                window.removeClass("l-window-dragging");
                g.window.content.children().show();
              }
            });
        	 }
        	 }
          // 关闭事件
          $('.l-dialog-close', g.window.header).click(function() {
            g.close();
            // g.unmask(window);
          });
        },
        /**
         * [switchWindow 允许多个弹窗的存在,可以切换弹窗在最上层展示]
         * @param  {[type]} window [弹窗对象]
         * @return {[Null]}        [无返回]
         */
        switchWindow: function(window) {
          $(window).css("z-index", "9001").siblings(".tip_window").css("z-index", "9000");
        },
        /**
         * @description 关闭弹窗
         * @param {Object}
         *            window
         */
        /**
         * [close 关闭弹窗]
         * @param  {[Object]} window [弹窗对象]
         * @return {[Null]}        [无返回]
         */
        close: function() {
          if(p.onCloseWin) p.onCloseWin(g.window, p.data, g);
          g.window.remove();
          g.unmask(g.window);
        },

        mask: function(win) {
          var maskObj = $('body > .user-defined-mask:visible');
          if(maskObj.length > 0) {
            return false;
          }

          function setHeight() {
            if(maskObj.length < 1) return;
            var h = $(window).height() + $(window).scrollTop();
            maskObj.height(h);
          }
          if(maskObj.length < 1) {
            maskObj = $("<div class='l-window-mask user-defined-mask' style='display: block;'></div>").appendTo('body');
            $(window).bind('resize.ligeruiwin', setHeight);
            $(window).bind('scroll', setHeight);
          }
          maskObj.show();
          setHeight();
        },

        //取消遮罩
        unmask: function(win) {
          var jwins = $("body > .user-defined-dialog:visible");
          var maskObj = $('body > .user-defined-mask:visible');
          if(maskObj && jwins.length === 0) maskObj.remove();
        }
      };
      // 私有方法
      var po = {

      };
      g.loading();
      // if (this.id == undefined || this.id == "") this.id = "CTFO_UI_" +
      // new Date().getTime();
      // CTFO.Util.UIManagers[this.id + "_Window"] = g;
    });
  };
})(jQuery);

SX.Util.popWindow = function (p) {
  p = p || {};
  if (p.url || p.content) {
    var win = $('<div>');
    $(win).applyPopWindow($.extend({}, p));
  }
};

/**
 * [menuParser 菜单渲染器]
 * @param  {[Object]} p [参数对象]
 * @param  {[String]} p.uid [登录用户id]
 * @param  {[Object]} p.data [菜单数据]
 * @param  {[String]} p.url [菜单数据请求接口]
 * @return {[type]}   [description]
 */
SX.Util.menuParser = function (p) {
  p = p || {};
  var init = function () {
   // if (p.data) {
     // parseHtml(p.data);
   // } else if (p.url && p.uid) {
    // TODO
    // 从后台读取菜单数据，并在done函数中执行菜单数据的渲染工作
     $.ajax({
       url : getContextPath()+ '/action?actionid=menuCreator',
       type : 'POST',
       dataType: 'json',
       data: {param1: 'value1'}, // TODO 数据请求接口的传参待填
    })
    .done(function(data) {
      parseHtml(data);
     })
    .fail(function() {
       console.log("error");
     })
    .always(function() {
       console.log("complete");
     });
    //} else {
    //  return false;
   // }
  };
  var parseHtml = function (menus) { 
    var tmpl = $('#menu_tmpl').html();
    var doTtmpl = doT.template(tmpl);
    menuIteration(menus, doTtmpl, p.menuContainer);
    bindMenuEvent();
  };
  /**
   * [menuIteration 菜单迭代加载]
   * @param  {[Object]} d         [数据]
   * @param  {[Object]} doTtmpl   [模板对象]
   * @param  {[Object]} container [模板生成的html加载的容器]
   */
  var menuIteration = function(d, doTtmpl, container) {
    $(container).append(doTtmpl(d));
    var subData = d.top || d.modules;
    $(subData).each(function(i) {
      var m = this.module,
        pid = this.module.id.split('.')[0];
      if (m.modules && m.modules.length > 0) {
        menuIteration(m, doTtmpl, $(p.menuContainer).find('.nav .li_' + pid));
      }
    });
  };
  var bindMenuEvent = function () {
	    var subHover = p.menuContainer.find('ul.nav li');
	    var subnav = subHover.find('ul.subnav');
	    var subnavLength = subnav.length;
	    for (var i = 0 ;i < subnavLength  ; i++) {
	     $(subnav).eq(i).css({'width':$(subnav).eq(i).outerWidth()+12+'px','marginLeft': -$(subnav).eq(i).outerWidth()/2+25+'px'})
	    };
	    subnav.css({visibility: "hidden"});
	    subHover.hover(
	      function(){
	        
	        $(this).find('ul.subnav').css({visibility: "visible",display: "none"}).slideDown(400);
	        
	        /*IE6 subnav:hover*/
	        $(this).find('ul.subnav ol').hover(
	          function(){
	              $(this).addClass('ol-hover');
	          },
	          function(){
	              $(this).removeClass('ol-hover');
	          });
	      },
	      function(){
	        $(this).find('ul.subnav').css({visibility: "hidden"});
	      }
	    );
	    p.menuContainer.find('ul.nav li, ol').bind('click', function(e) {
	        var mid = $(this).attr('mid'),
	            url = $(this).attr('url'),
	            isFrame = +$(this).attr('isframe');
	        if(!$(this).hasClass('menu_selected')) {
	          $(this).addClass('menu_selected').siblings().removeClass('menu_selected');
	        }
	        if (currentShowModel === mid) return false;
	        if (mid && url) changeModel(mid, url, null, isFrame);
	        else $(this).find('ol:eq(0)').trigger('click');
	        e.stopPropagation();
	      });
	      subHover.eq(0).trigger('click'); // TODO 待定默认打开的子模块
	  };
  init();
};

/**
 * [userInfoParser description]
 * @param  {[Object]}   p [参数对象]
 * @param  {[String]}   p.url [请求用户信息接口]
 * @param  {[String]}   p.uid [用户id]
 * @param  {[Object]}   p.data [用户信息数据]
 * @param  {[Function]} p.callback [回调函数]
 * @return {[type]}   [description]
 */
SX.Util.userInfoParser = function (p) {
  p = p || {};
  var init = function () {
   // if (p.data) {
    //  parseHtml(p.data);
   // } else if (p.url) {
    // TODO 从后台读取登录用户的基本信息，在done回调函数中执行渲染工作
     $.ajax({
       url: getContextPath()+ '/action?actionid=sysUserAction',
       type: 'POST',
       dataType: 'json',
       data: {'aid':'showDetail'}, // TODO 传参待修改
     })
     .done(function(d) {
       parseHtml(d);
       if (p.callback) p.callback(d);
     })
     .fail(function() {
       console.log("error");
     })
     .always(function() {
       console.log("complete");
     });
   // }
  };
  var parseHtml = function (d) {
    p.userInfoContainer.find('.endDate').text(d.lastLoginTime).end()
    .find('.userName').text(d.username).end().find('.logoutButton').click(function (){window.location.href='/ctoms-web';});
    p.userInfoContainer.find('.modifyButton').click(function(){
    	SX.Util.popWindow({
        height: 400,
        url: 'home/updatePassword.html',
        width: 600,
        onLoad: initPwdConfigWin,
        title: '修改密码'
      });});
    p.userInfoContainer.find('.endDate').hide();
    $("#endDate").hide();
  };
  /**
   * [initGroupConfigWin 初始化组配置弹窗]
   * @param  {[Obejct]} w [弹窗Dom对象]
   * @param  {[Object]} d [传入的数据对象]
   * @param  {[Object]} g [弹窗js对象]
   * @return {[type]}   [description]
   */
  var initPwdConfigWin = function(w, d, g) { };
  init();
};


SX.Util.parseGrid = function (options, container) {
  return $(container).ligerGrid(options);
};
SX.Util.contextMenu = $.ligerMenu;
SX.Util.dialogWindow = $.ligerDialog;




function getContextPath() {
  var wl = window.location;
  var contextPath = wl.pathname.split("/")[1];
  return contextPath.length < 0 ? '' : '/' + contextPath;
}

SX.cache.actionBaseUrl = getContextPath() + '/action?d=' + new Date().getMilliseconds();

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