package com.pivos.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.os.RemoteException;
import android.util.Xml;

import com.baustem.service.vodnavigation.IVODNavigation;
import com.google.gson.Gson;
import com.pivos.beans.ShiftProgramEntity;

public class BackWatchUtil {
	private static IVODNavigation m_ivodService = null;
	
	public static void setVodService(IVODNavigation service){ m_ivodService = service; }
	
	//根据channelnr获取对应的id
	public static String _getIdByChannelNr(String serviceid, int contentType,int startIndex,int maxCount){
		String _id = "";
		try {
			String res = m_ivodService.getContentList("",contentType,startIndex,maxCount);
			XmlPullParser parser = Xml.newPullParser();
			try {
				parser.setInput(new ByteArrayInputStream(res.getBytes("UTF-8")), "UTF-8");
				int eventType = parser.getEventType();
				boolean flag_finished = false;
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
						case XmlPullParser.START_DOCUMENT:
							break;
						case XmlPullParser.START_TAG:
							if (parser.getName().equals("GeneralContainer")) {
								String _serviceId = parser.getAttributeValue(null, "serviceid");
								_serviceId = _serviceId.split(",")[2];
								if(_serviceId.equals(serviceid)){
									_id = parser.getAttributeValue(null, "id");
								}
							}
							break;
						case XmlPullParser.END_TAG:
							if (parser.getName().equals("GeneralContainer") && !_id.equals("")) {
								flag_finished = true;
							}
							break;
					}
					eventType = parser.next();
					if(flag_finished){
						break;
					}
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
		
		return _id;
	}
	
	//获取七天所有的回看节目的id
	public static ArrayList<String> _getAllBackWatchIds(String parentId, int contentType,int startIndex,int maxCount){
		ArrayList<String> m_ids = new ArrayList<String>();
		try {
			String res = m_ivodService.getContentList(parentId, contentType,startIndex,maxCount);
			XmlPullParser parser = Xml.newPullParser();
			try {
				parser.setInput(new ByteArrayInputStream(res.getBytes("UTF-8")), "UTF-8");
				int eventType = parser.getEventType();
				boolean flag_finished = false;
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
						case XmlPullParser.START_DOCUMENT:
							break;
						case XmlPullParser.START_TAG:
							if (parser.getName().equals("GeneralContainer")) {
								m_ids.add(parser.getAttributeValue(null, "id"));
							}
							break;
						case XmlPullParser.END_TAG:
							break;
					}
					eventType = parser.next();
					if(flag_finished){
						break;
					}
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
		return m_ids;
	}
	
	//根据day获取对应的回看节目id
	public static String _getBackWatchIdByDay(String parentId, int day, int contentType,int startIndex,int maxCount){
		String _id = "";
		day = 8 - day;
		int _day = 1;
		try {
			String res = m_ivodService.getContentList(parentId, contentType,startIndex,maxCount);
			XmlPullParser parser = Xml.newPullParser();
			try {
				parser.setInput(new ByteArrayInputStream(res.getBytes("UTF-8")), "UTF-8");
				int eventType = parser.getEventType();
				boolean flag_finished = false;
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
						case XmlPullParser.START_DOCUMENT:
							break;
						case XmlPullParser.START_TAG:
							if (parser.getName().equals("GeneralContainer")) {
								if(_day == day){
									_id = parser.getAttributeValue(null, "id");
								}
							}
							break;
						case XmlPullParser.END_TAG:
							if (parser.getName().equals("GeneralContainer")) {
								if(!_id.equals("")){
									flag_finished = true;
								}
								_day++;
							}
							break;
					}
					eventType = parser.next();
					if(flag_finished){
						break;
					}
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
		
		return _id;
	}
	
	//获取具体某一天的回看节目列表
	public static ArrayList<ShiftProgramEntity> _getBackWatcherProgramsId(String id, int contentType,int startIndex,int maxCount){
		ArrayList<ShiftProgramEntity> m_ret = new ArrayList<ShiftProgramEntity>();
		ShiftProgramEntity shiftProgramEntity = null;
		try {
			String res = m_ivodService.getContentList(id, contentType,startIndex,maxCount);
			XmlPullParser parser = Xml.newPullParser();
			try {
				parser.setInput(new ByteArrayInputStream(res.getBytes("UTF-8")), "UTF-8");
				int eventType = parser.getEventType();
				boolean flag_finished = false;
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
						case XmlPullParser.START_DOCUMENT:
							break;
						case XmlPullParser.START_TAG:
							if (parser.getName().equals("TimeShiftProgram")) {
								shiftProgramEntity = new ShiftProgramEntity();
								shiftProgramEntity.setId(parser.getAttributeValue(null, "id"));
								shiftProgramEntity.setName(parser.getAttributeValue(null, "name"));
								shiftProgramEntity.setChannelName(parser.getAttributeValue(null, "channelName"));
								shiftProgramEntity.setPlayUrls(parser.getAttributeValue(null, "playURLs"));
								shiftProgramEntity.setStartTime(parser.getAttributeValue(null, "startTime"));
								shiftProgramEntity.setEndTime(parser.getAttributeValue(null, "endTime"));
							}
							break;
						case XmlPullParser.END_TAG:
							if (parser.getName().equals("TimeShiftProgram")) {
								m_ret.add(shiftProgramEntity);
							}
							break;
					}
					eventType = parser.next();
					if(flag_finished){
						break;
					}
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
		return m_ret;
	}
	
	//获取指定逻辑频道号的回看节目列表
	public static String getBackWatchPrograms(String serviceid, int day, int contentType,int startIndex,int maxCount){
		ArrayList<ShiftProgramEntity> m_ret = new ArrayList<ShiftProgramEntity>();
		
		//获取channelnr所对应的id
		String _id = _getIdByChannelNr(serviceid, contentType,startIndex,maxCount);
		
		if(day >= 1){
			_id = _getBackWatchIdByDay(_id, day, contentType,startIndex,maxCount);
			m_ret = _getBackWatcherProgramsId(_id, contentType, startIndex, maxCount);
		}else if(day == 0){//天数传0时，返回七天所有的回看节目
			ArrayList<ArrayList<ShiftProgramEntity>> m_rets = new ArrayList<ArrayList<ShiftProgramEntity>>();
			ArrayList<String> ids = _getAllBackWatchIds(_id, contentType, startIndex, maxCount);
			for(int i=0 ; i < ids.size(); i++){
//				try {
//					m_rets.add(_getBackWatcherProgramsId(ids.get(i), contentType, startIndex, maxCount));
//					//jsonObj.put((i+1)+"", _getBackWatcherProgramsId(ids.get(i), contentType, startIndex, maxCount));
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				m_rets.add(_getBackWatcherProgramsId(ids.get(i), contentType, startIndex, maxCount));
			}
			return new Gson().toJson(m_rets);
		}

		return new Gson().toJson(m_ret);
	}
}
