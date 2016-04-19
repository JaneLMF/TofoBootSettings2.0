package com.txbox.settings.utils;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


/**
 * 通用方法
 * 
 * @author lxf
 */
public class MessageUtils {

	public static Context _ctx;
	public static OutputStream os;
	public static Socket client;
	public static final String ADDRESS="127.0.0.1";
	public static final int PORT=59894;
	//public static final int PORT=4567;
	public static final int MAX_BUFFER_LEN = 4096;

	
	// 指令推送
	public static void send(final String orders) {
		try {
			Timer timerOrder = new Timer();
			timerOrder.schedule(new TimerTask() {	
				@Override
				public void run() {
					try {
						client = new Socket(ADDRESS, PORT);
						os = client.getOutputStream();
						//os.write((orders + "\r\n").getBytes("utf-8"),0, temp);
						//int len = orders.length();
						
						int sendLen = 0;
						int dataLen = orders.length();
						System.out.println("data len = " + dataLen);
						byte buffer [] = new byte[MAX_BUFFER_LEN];
						byte readbuf[] = new byte[MAX_BUFFER_LEN];
						
						while(sendLen < dataLen){
							int index = 0;
							if(dataLen <MAX_BUFFER_LEN){
								buffer = (orders+"\r\n").getBytes("utf-8");
								index = orders.length();
							}else{
								int end = 0;
								if((sendLen+MAX_BUFFER_LEN)>dataLen){
									end = dataLen;
									buffer = (orders.substring(sendLen, end)+"\r\n").getBytes("utf-8");
								}else{
									end = sendLen+MAX_BUFFER_LEN;
									buffer = (orders.substring(sendLen, end)).getBytes("utf-8");
								}
								index = MAX_BUFFER_LEN;
							}
							os.write(buffer);
							int len = client.getInputStream().read(readbuf);
							if(len >0){
								String result = new String(readbuf,0,len);
								if(result.contains("ACK")||result.contains("COMPLETE")){
									sendLen +=index;
									continue;
								}
							}else{
								break;
							}
						}
						
						os.flush();
						client.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}, 300);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void PushMessage(Context context,String message) {
		//send(message);
		Intent intent = new Intent();  
		intent.setAction("android.intent.action.pushMessage"); 
		intent.setData(Uri.parse("data:"+message));
		context.sendBroadcast(intent);
	}
	public static void MultScreenMessage(Context context,String message) {
		//send(message);
		Intent intent = new Intent();  
		intent.setAction("android.intent.action.TencentMultiScreen"); 
		intent.setData(Uri.parse("data:"+message));
		context.sendBroadcast(intent);
	}
	public static void onlogin(Context context,String nick,String face,String openid,String accesstoken){
		JSONObject object = new JSONObject();// 一个user对象，使用一个JSONObject对象来装
		JSONObject data = new JSONObject();
		JSONArray jsonArray = null;
		jsonArray = new JSONArray();
		JSONObject push_scope = new JSONObject();
		
		try{
			data.put("nick", nick);
			data.put("face", face);
			data.put("openid", openid);
			data.put("accesstoken", accesstoken);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		try{
			push_scope.put("pushplat", "system");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		jsonArray.put(push_scope);
		try {
			object.put("action_name", "login");
			object.put("badge", 1);
			object.put("data", data);
			object.put("push_scope", jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String jsonString = null;
		jsonString = object.toString(); // 把JSONObject转换成json格式的字符串
		
		Intent intent = new Intent();  
		intent.setAction("android.intent.action.onLogin"); 
		intent.setData(Uri.parse("data:"+jsonString));
		context.sendBroadcast(intent);
		
		//sendMessage(jsonString);
	}
	public static void  onPay(Context context,String openid,String month){
		JSONObject object = new JSONObject();// 一个user对象，使用一个JSONObject对象来装
		JSONObject data = new JSONObject();
		JSONArray jsonArray = null;
		jsonArray = new JSONArray();
		JSONObject push_scope = new JSONObject();
		
		try{
			data.put("openid", openid);
			data.put("month", month);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		try{
			push_scope.put("pushplat", "system");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		jsonArray.put(push_scope);
		try {
			object.put("action_name", "login");
			object.put("badge", 1);
			object.put("data", data);
			object.put("push_scope", jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String jsonString = null;
		jsonString = object.toString(); // 把JSONObject转换成json格式的字符串
		//sendMessage(jsonString);
		Intent intent = new Intent();  
		intent.setAction("android.intent.action.onPay"); 
		intent.setData(Uri.parse("data:"+jsonString));
		context.sendBroadcast(intent);
	}
	public static void  onBuy(Context context,String openid,String cid){
		
		JSONObject object = new JSONObject();// 一个user对象，使用一个JSONObject对象来装
		JSONObject data = new JSONObject();
		JSONArray jsonArray = null;
		jsonArray = new JSONArray();
		JSONObject push_scope = new JSONObject();
		
		try{
			data.put("openid", openid);
			data.put("cid", cid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		try{
			push_scope.put("pushplat", "system");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		jsonArray.put(push_scope);
		try {
			object.put("action_name", "login");
			object.put("badge", 1);
			object.put("data", data);
			object.put("push_scope", jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String jsonString = null;
		jsonString = object.toString(); // 把JSONObject转换成json格式的字符串
		//sendMessage(jsonString);
		Intent intent = new Intent();  
		intent.setAction("android.intent.action.onBuy"); 
		intent.setData(Uri.parse("data:"+jsonString));
		context.sendBroadcast(intent);
	}
	public static void  onTry(Context context,String cid,String vid){
		JSONObject object = new JSONObject();// 一个user对象，使用一个JSONObject对象来装
		JSONObject data = new JSONObject();
		JSONArray jsonArray = null;
		jsonArray = new JSONArray();
		try{
			data.put("vid", vid);
			data.put("cid", cid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject push_scope = new JSONObject();
		try{
			push_scope.put("pushplat", "system");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		jsonArray.put(push_scope);
		try {
			object.put("action_name", "try");
			object.put("badge", 1);
			object.put("data", data);
			object.put("push_scope", jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String jsonString = null;
		jsonString = object.toString(); // 把JSONObject转换成json格式的字符串
		//sendMessage(jsonString);
		Intent intent = new Intent();  
		intent.setAction("android.intent.action.onTry"); 
		intent.setData(Uri.parse("data:"+jsonString));
		context.sendBroadcast(intent);
	}
}
