package com.pivos.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.os.RemoteException;
import android.util.Log;
import android.util.Xml;

import com.baustem.service.vodstream.IVODStream;
import com.google.gson.Gson;
import com.pivos.beans.PlayInfoEntity;

public class VodStreamUtil {
	private static IVODStream m_ivodStreamService = null;
	public static void setVodStreamService(IVODStream service){ m_ivodStreamService = service;}
	
	public static String _getIntanceId(String playUrl){
		PlayInfoEntity playInfoEntity = null;

		try {
			String res = m_ivodStreamService.getPlayInfo(playUrl);
			Log.i("res", "res:"+res);
			XmlPullParser parser = Xml.newPullParser();
			try {
				parser.setInput(new ByteArrayInputStream(res.getBytes("UTF-8")), "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
						case XmlPullParser.START_DOCUMENT:
							break;
						case XmlPullParser.START_TAG:
							if (parser.getName().equals("instanceid")) {
								playInfoEntity = new PlayInfoEntity();
								playInfoEntity.setInstanceid(parser.nextText());
							}else if(parser.getName().equals("connection_url") && playInfoEntity != null){
								playInfoEntity.setConnection_url(parser.nextText());
							}
							break;
						case XmlPullParser.END_TAG:
							break;
					}
					eventType = parser.next();
				}
			}catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return playInfoEntity.getInstanceid();
	}
	
	public static String play(String instanceId, int speed){
		try {
			String res = m_ivodStreamService.play(instanceId, speed);
			Log.i("play res", "play res:"+res);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{}";
	}
	
	public static String stop(String instanceId){
		try {
			String res = m_ivodStreamService.stop(instanceId);
			Log.i("res", "res:"+res);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{}";
	}
	
	//@time 格式:00:00:00
	public static String seek(String instanceId, String time){
		try {
			m_ivodStreamService.seek(instanceId, time);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{}";
	}
	
	public static String pause(String instanceId){
		try {
			String res = m_ivodStreamService.pause(instanceId);
			Log.i("res", "res:"+res);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{}";
	}
	public static String getPlayStatus(String instanceId){
		String status = "";
		try {
			String res = m_ivodStreamService.getPlayStatus(instanceId);
			Log.i("res", "res:"+res);
			XmlPullParser parser = Xml.newPullParser();
			try {
				parser.setInput(new ByteArrayInputStream(res.getBytes("UTF-8")), "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
						case XmlPullParser.START_DOCUMENT:
							break;
						case XmlPullParser.START_TAG:
							if (parser.getName().equals("status")) {
								status = parser.nextText();
							}
							break;
						case XmlPullParser.END_TAG:
							break;
					}
					eventType = parser.next();
				}
			}catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return "{\"status\":"+status+"}";
	}
	
	public static String getPlayProgress(String instanceId){
		String progress = "";
		try {
			String res = m_ivodStreamService.getPlayProgress(instanceId);
			Log.i("res", "res:"+res);
			XmlPullParser parser = Xml.newPullParser();
			try {
				parser.setInput(new ByteArrayInputStream(res.getBytes("UTF-8")), "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
						case XmlPullParser.START_DOCUMENT:
							break;
						case XmlPullParser.START_TAG:
							if (parser.getName().equals("progress")) {
								progress = parser.nextText();
							}
							break;
						case XmlPullParser.END_TAG:
							break;
					}
					eventType = parser.next();
				}
			}catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return "{\"progress\":"+progress+"}";
	}
}
