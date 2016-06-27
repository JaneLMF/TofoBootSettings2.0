package com.tvxmpp.util;


public class MessageUtil {

	public static String MessageType_INFO = "Info";
	public static String MessageType_WARN = "Warning";
	public static String MessageType_DEBUG = "Debug";
	public static String MessageType_ERROR = "Error";

	public static String messegebox[][] = {
			{"CA", "1002", "节目未授权", "Info"},
			{"CA", "1007", "未插入智能卡", "Warning"},
			{"CA", "1008", "无效智能卡", "Warning"},
			{"CA", "1014", "智能卡插入", "Info"},
			{"CA", "1016", "ATR复位失败", "Info"},
			{"CA", "1020", "智能卡读取失败", "Warning"},
			{"DTV", "2005", "直播节目TS不存在", "Info"},
			{"DTV", "2050", "直播信号恢复", "Info"},
			{"VOD", "3120", "播放VOD节目时，建立会话失败", "Debug"},
			{"VOD", "3135", "EDS调度失败", "Warning"},
			{"VOD", "3137", "IPQAM申请失败", "Warning"},
			{"VOD", "3138", "回看节目鉴权失败", "Warning"},
			{"VOD", "3139", "时移节目鉴权失败", "Warning"},
			{"SM", "4035", "插件模块宕机", "Error"},
			{"SSU", "4040", "系统软件下载成功", "Info"},
			{"SSU", "4041", "系统软件下载失败", "Error"},
			{"SSU", "4042", "系统软件校验失败", "Error"},
			{"SM", "4043", "插件下载成功", "Info"},
			{"SM", "4044", "插件下载失败", "Error"},
			{"SM", "4045", "插件安装成功", "Info"},
			{"SM", "4046", "插件安装失败", "Error"},
			{"SM", "4047", "插件校验失败", "Error"}
	};
	
	public static String getShowString(String strID){
		String returnValue = null;
		for (int i = 0; i < messegebox.length; i++){
			if (strID.equals(messegebox[i][1])){
				returnValue = messegebox[i][2];
			}
		}
		return returnValue;
	}
}
