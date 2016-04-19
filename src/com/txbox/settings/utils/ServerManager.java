package com.txbox.settings.utils;

public class ServerManager {
	/*第三方需要在用户登录入口引用下面的 URL（扫码登录的页面)*/
	/*正式环境：*/
	//private String LoginServer ="http://film.qq.com/opentv/login_pivos.html";
	/*
	?tvid={TVID}&appver={APPVER}&bid=
	{BID}&appid={APPID}
	*/
	/*测试环境：*/
	public static String LoginServer = "http://tv.t002.ottcn.com/i-tvbin/pay/tpl?proj=pay&page=tvlogin";
	//public static String LoginServer = "http://tv.ptyg.gitv.tv/tv-html/tv/qrcode-login.html";
	//public static String LoginServer ="http://film.qq.com/box/tbox/login.html";
	//public static String LoginServer = "http://p30.video.qq.com/box/pivos/login.html";
	/*?tvid={TVID}&appver={APPVER}
	&bid={BID}&appid={APPID}
	*/
	
	//开通会员页面一地址：
	/*正式环境*/
	public static String VipServer = "http://tv.t002.ottcn.com/i-tvbin/tpl?proj=newpay&page=vip";
	//public static String VipServer = "http://film.qq.com/box/tbox/vip.html";
	/*测试环境*/
	//public static String VipServer = "http://p30.video.qq.com/box/pivos/vip.html";
	
	
	//开通会员页面二
	/*正式环境*/
	public static String PayServer = "http://tv.t002.ottcn.com/i-tvbin/pay/tpl?proj=pay&page=pay";
	//public static String PayServer = "http://film.qq.com/box/tbox/pay.html";
	/*测试环境*/
	//public static String PayServer = "http://p30.video.qq.com/box/pivos/pay.html";
	
	//观影券支付页面
	/*正式环境*/
	public static String TicketServer = "http://tv.t002.ottcn.com/i-tvbin/tpl?proj=newpay&page=ticket";
	//public static String TicketServer = "http://film.qq.com/box/tbox/ticket.html";
	/*测试环境*/
	//public static String TicketServer = "http://p30.video.qq.com/box/pivos/ticket.html";
	
	//单片支付页面
	/*正式环境*/
	public static String BuyServer = "http://tv.t002.ottcn.com/tv-html/tv/buy.html";
	//public static String BuyServer = "http://film.qq.com/box/tbox/buy.html";
	/*测试环境*/
	//public static String BuyServer = "http://p30.video.qq.com/box/pivos/buy.html";

	
	/*鉴权票据接口地址*/
	public static String AUTH_SERVER = "http://tv.t002.ottcn.com/pivos-tvbin/auth";
	
	/*配置系统接口地址*/
	public static String SYSCFG_SERVER = "http://tv.t002.ottcn.com/pivos-tvbin/cfg";
	
//	/*获取升级信息接口地址*/
//	public static String ROM_UPGRADE_SERVER = "https://tvcms.video.qq.com/pivos-tvbin/upgrade_rom";
	/*获取升级信息接口地址*/
	public static String ROM_UPGRADE_SERVER = "http://tv.t002.ottcn.com/i-tvbin/upgrade_rom_test";
	//public static String ROM_UPGRADE_SERVER = "http://tofuos.com/PhpApi/Public/go3c/";
	/*获取相册数据接口地址*/
	public static String ALBUM_SERVER = "http://tv.t002.ottcn.com/pivos-tvbin/album";
	
	/*消息推送服务器接口地址*/
	//public static String PUSH_SERVER_ADDRESS="newtest.mpush.qq.com";
	public static String PUSH_SERVER_ADDRESS="p.conn.ptyg.gitv.tv";
	public static int PUSH_SERVER_PORT=7512;
	//public static int PUSH_SERVER_PORT=9977;
	public static String PUSH_SERVICE_BiD="10015";
	/*用户反馈二维码内容服务器地址*/
	public static String SUPPORT_QRCODE_SERVER = "http://tv.t002.ottcn.com/tools/support/view";
	/*根据盒子请求IP地址，自动返回盒子地理位置信息接口地址*/
	public static String LBS_SERVER="http://tv.t002.ottcn.com/pivos-tvbin/lbs/geo_info";
	/*获取天气接口*/
	public static String WEATHER_SERVER="http://tv.t002.ottcn.com/pivos-tvbin/weather";
	
	/*OpenApi接口地址*/
	public static String OPENAPI_SERVER="https://graph.qq.com/v3/video";
	//提交crash信息
		public static String REPORT_SERVER = "http://www.go3c.tv:8060/go3cauth";
	//服务器获取游戏列表，便于存储游戏类型
		public static String GAMELIST_SERVER = "http://tv.t002.ottcn.com/pivos-tvbin/game/get_game_list?platform=8&subplatform=41&format=json&start_index=0&req_num=20";
    //pivos 注册，鉴权服务器
	//public static String PIVOS_AUTH_SERVER = "http://116.77.70.162/PhpApi/Public/go3c/";
	public static String PIVOS_AUTH_SERVER = "http://tofuos.com/PhpApi/Public/go3c/";
	public static String PIVOS_AUTH_QRCODE_SERVER = "http://116.77.70.163:3000/auth/";
}
