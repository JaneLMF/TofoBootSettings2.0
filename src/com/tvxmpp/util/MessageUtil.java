package com.tvxmpp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.tvxmpp.model.Messege;

public class MessageUtil {
	public static String MessageType_INFO = "Info";
	public static String MessageType_WARN = "Warning";
	public static String MessageType_DEBUG = "Debug";
	public static String MessageType_ERROR = "Error";

	public static ArrayList<Messege> messegebox = null;
	public static Map<String, Messege> checkMsg = null;
	static{
		messegebox = new ArrayList<Messege>();
		messegebox.add(new Messege("CA", "1002", "节目未授权", "Info", true));
		messegebox.add(new Messege("CA", "1007", "未插入智能卡", "Warning"));
		messegebox.add(new Messege("CA", "1008", "无效智能卡", "Warning"));
		messegebox.add(new Messege("CA", "1014", "智能卡插入", "Info"));
		messegebox.add(new Messege("CA", "1016", "ATR复位失败", "Info"));
		messegebox.add(new Messege("CA", "1020", "智能卡读取失败", "Warning"));
		messegebox.add(new Messege("DTV", "2005", "直播节目TS不存在", "Info"));
		messegebox.add(new Messege("DTV", "2050", "直播信号恢复", "Info"));
		messegebox.add(new Messege("VOD", "3120", "播放VOD节目时，建立会话失败", "Debug"));
		messegebox.add(new Messege("VOD", "3135", "EDS调度失败", "Warning"));
		messegebox.add(new Messege("VOD", "3137", "IPQAM申请失败", "Warning"));
		messegebox.add(new Messege("VOD", "3138", "回看节目鉴权失败", "Info", true));
		messegebox.add(new Messege("VOD", "3139", "时移节目鉴权失败", "Info", true));
		messegebox.add(new Messege("SM", "4035", "插件模块宕机", "Error"));
		messegebox.add(new Messege("SSU", "4040", "系统软件下载成功", "Info"));
		messegebox.add(new Messege("SSU", "4041", "系统软件下载失败", "Error"));
		messegebox.add(new Messege("SSU", "4042", "系统软件校验失败", "Error"));
		messegebox.add(new Messege("SM", "4043", "插件下载成功", "Info"));
		messegebox.add(new Messege("SM", "4044", "插件下载失败", "Error"));
		messegebox.add(new Messege("SM", "4045", "插件安装成功", "Info"));
		messegebox.add(new Messege("SM", "4046", "插件安装失败", "Error"));
		messegebox.add(new Messege("SM", "4047", "插件校验失败", "Error"));
		messegebox.add(new Messege("VOD", "3007", "点播节目鉴权失败", "Error", true));
		
		checkMsg = new HashMap<String, Messege>();
		checkMsg.put("1002", new Messege("CA", "1002", "节目未授权", "Info", true));
		checkMsg.put("3138", new Messege("VOD", "3138", "回看节目鉴权失败", "Info", true));
		checkMsg.put("3139", new Messege("VOD", "3139", "时移节目鉴权失败", "Info", true));
		checkMsg.put("3007", new Messege("VOD", "3007", "点播节目鉴权失败", "Error", true));
	}
	
	public static Messege getShowString(String strID){
		Messege msg = null;
		for (int i = 0; i < messegebox.size(); i++){
			msg = messegebox.get(i);
			if (strID.equals(msg.getMsgId())){
				break;
			}
			msg = null;
		}
		return msg;
	}
	
	public static void sendBraodCast(){
		
	}
}
