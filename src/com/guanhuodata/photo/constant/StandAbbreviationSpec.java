package com.guanhuodata.photo.constant;

/**
 * 
 * @author fudk
 * 展位简称与展位对应枚举类：展位简称&展位尺寸
 *
 */
public enum StandAbbreviationSpec {

		//无线640
		OFFLINE640("无线640","640*200"),
		//淘宝app3旧
		TAOBAOAPP3OLD("淘宝app3旧","592*296"),
		//淘宝首焦
		TAOBAOSHOUJIAO("淘宝首焦","520*280"),
		//淘宝首焦右b2
		TAOBAOSHOUJIAORB2("淘宝首焦右b2","170*200"),
		//淘宝首页2屏右
		TAOBAOFPAGE2SCREENR("淘宝首页2屏右","300*250"),
		//天猫精选
		TMALLSELECTION("天猫精选","260*200"),
		//3屏通栏大b
		SCREEN3GREETB("3屏通栏大b","375*130"),
		//淘宝首页3屏通栏
		TAOBAOFPAGE3SCREENCOMMON("淘宝首页3屏通栏","728*90"),
		//淘宝首页3屏小图
		TAOBAOFPAGE3SCREENSMALLIMG("淘宝首页3屏小图","190*90"),
		//天猫首焦2
		TMALLSHOUJIAO2("天猫首焦2","810*480"),
		//天猫首焦4
		TMALLSHOUJIAO4("天猫首焦4","810*480"),
		//天猫通栏
		TMALLCOMMON("天猫通栏","1190*90"),
		//淘金币通栏
		TAOGOLDCOINCOMMON("淘金币通栏","990*95"),
		//我的淘宝右b
		MYTAOBAORB("我的淘宝右b","300*125"),
		//收藏夹底部通栏
		FAVORITEBOTTOMCOMMON("收藏夹底部通栏","740*230"),
		//天猫收货成功通栏
		TMALLTAKEOVERSUCCESSCOMMON("天猫收货成功通栏","990*90"),
		//我要逛右b
		IWANTROAMRB("我要逛右b","190*90"),
		//我要买通栏
		IWANTBUYCOMMON("我要买通栏","728*90"),
		//站内01
		INSTATION1("站内01","168*175"),
		//站内02
		INSTATION2("站内02","168*76"),
		//凤凰网首页通栏03
		FENGHUANGNETFPAGECOMMON3("凤凰网首页通栏03","750*90"),
		//腾讯首页通栏
		TENCENTFPAGECOMMON("腾讯首页通栏","700*90"),
		//新浪首页左侧画中画
		SINAFPAGELEFTHZH("新浪首页左侧画中画","240*355"),
		//新浪网图集通栏
		SINAIMGCOMMON("新浪网图集通栏","1000*90"),
		//腾讯文章页画中画
		TENCENTWENZHANGYEHUAZHONGHUA("腾讯文章页画中画","300*250");
	
	
		private final String standAbbreviation;
		private final String standSize;
		
		StandAbbreviationSpec(String standAbbreviation,String standSize){
			this.standAbbreviation = standAbbreviation;
			this.standSize = standSize;
		}

		public String getStandSize() {
			return standSize;
		}
		
		public String getStandAbbreviation(){
			return standAbbreviation;
		}
}
