package com.pivos.utils;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import android.util.Xml;

import com.baustem.service.dvbnavigation.IDVBNavigation;
import com.baustem.service.epgnavigation.IEPGNavigation;
import com.baustem.service.vodnavigation.IVODNavigation;
import com.baustem.service.vodstream.IVODStream;
import com.pivos.beans.ChannelEntity;
import com.pivos.beans.EpgEntity;
import com.pivos.beans.LiveChannelEntity;
import com.pivos.beans.PlayInfoEntity;
import com.pivos.beans.ShiftProgramEntity;
import com.pivos.beans.VODProgramEntity;
import com.pivos.beans.VodCategoryEntity;
import com.txbox.settings.common.TXbootApp;
import com.txbox.settings.report.ReportHelper;
import com.google.gson.Gson;

import org.cybergarage.http.HTTPRequest;
import org.cybergarage.http.HTTPResponse;
import org.cybergarage.http.HTTPServerList;
import org.cybergarage.http.HTTPStatus;
import org.cybergarage.http.ParameterList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.net.UnknownHostException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class WebServer extends Thread implements org.cybergarage.http.HTTPRequestListener
{
	private HTTPServerList httpServerList = null;
	
	private static int m_bindPort = 2222;
	
	private static String m_bindIP = "127.0.0.1";

	private String TAG = "WebServer";
	
	private Context mContext = null;

	private static IDVBNavigation m_idvbService = null;
	private static IEPGNavigation m_iepgService = null;
	private static IVODNavigation m_ivodService = null;
	private static IVODStream m_ivodStreamService = null;
	
	private int ContentType_Vod = 0;//点播
	private int ContentType_BackWatch = 1;//回看
	private int ContentType_TimeShift = 4;//时移
	
	public WebServer(Context context) {
		mContext = context;
	}
	
	public static String getBindIP()
	{
		return m_bindIP;
	}

	public void setBindIP(String bindIP)
	{
		m_bindIP = bindIP;
	}

	public HTTPServerList getHttpServerList()
	{
		return httpServerList;
	}

	public void setHttpServerList(HTTPServerList httpServerList)
	{
		this.httpServerList = httpServerList;
	}

	public static int getBindPort()
	{
		return m_bindPort;
	}

	public void setBindPort(int hTTPPort)
	{
		m_bindPort = hTTPPort;
	}


	public static void setDvbService(IDVBNavigation service){ m_idvbService = service; }
	public static void setEpgService(IEPGNavigation service){ m_iepgService = service; }
	public static void setVodService(IVODNavigation service){
		m_ivodService = service;
		BackWatchUtil.setVodService(m_ivodService);
	}
	public static void setVodStreamService(IVODStream service){
		m_ivodStreamService = service;
		VodStreamUtil.setVodStreamService(m_ivodStreamService);
	}

	@Override
	public void run()
	{
		super.run();
		
		int retryCnt = 0;
		int bindPort = getBindPort();
		
		InetAddress[] addrList = new InetAddress[1];
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName("127.0.0.1");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addrList[0] = addr;
		httpServerList = new HTTPServerList(addrList, bindPort);
		
		HTTPServerList hsl = getHttpServerList();
		while (hsl.open(bindPort) == false)
		{
			retryCnt++;
			if (100 < retryCnt)
			{
				Log.i(TAG, "bind faile");
				return;
			}
			setBindPort(bindPort + 1);
			bindPort = getBindPort();
		}

		hsl.addRequestListener(this);
		
		hsl.start(); 
		
		m_bindIP = hsl.getHTTPServer(0).getBindAddress();
		m_bindPort = hsl.getHTTPServer(0).getBindPort();

		Log.i(TAG, "m_bindIP:"+m_bindIP);
		Log.i(TAG, "bindPort:" + bindPort);
	}

	@Override
	public void httpRequestRecieved(HTTPRequest httpReq)
	{
		String uri = httpReq.getURI();

		try
		{
			uri = URLDecoder.decode(uri, "UTF-8");
		}
		catch (UnsupportedEncodingException e1)
		{ 
			e1.printStackTrace();
		}

		Log.i(TAG, "uri:" + uri);

		if (uri.startsWith("/pivos/") == false)
		{
			httpReq.returnBadRequest();
			return;
		}
		String[] func = uri.split("/");
		if(func.length < 3){
			httpReq.returnBadRequest();
			return;
		}

		String requestName = func[2];
		if(requestName.contains("?")){
			requestName = requestName.substring(0, requestName.indexOf("?"));
		}
		String requestParams = func[2].substring(func[2].indexOf("?")+1);

		ParameterList params = httpReq.getParameterList();

		HTTPResponse httpRes = new HTTPResponse();
		httpRes.setContentType("application/json;charset=utf-8");
		httpRes.setStatusCode(HTTPStatus.OK);
		//httpRes.setContentLength(contentLen);

		if(requestName.equals( "getall")) { //获取频道列表
			String channelList = getAllChannel();
			httpRes.setContent(channelList);
		}
		else if(requestName.equals("getepg")) {//获取指定频道的epg信息
				String channelNr = params.getParameter("channelnr").getValue();
				String startTime = params.getParameter("starttime") != null? params.getParameter("starttime").getValue():null;
				String endTime = params.getParameter("endtime") != null? params.getParameter("endtime").getValue():null;
				int startIndex = Integer.parseInt(params.getParameter("startindex").getValue());
				int maxCount = Integer.parseInt(params.getParameter("maxcount").getValue());
				String epgList = getEpgList(channelNr, startTime, endTime, startIndex, maxCount);
				httpRes.setContent(epgList);
		}
		else if(requestName.equals("getpf")) {//获取指定频道的pf信息
				String pf = getPf(params.getParameter("channelnr").getValue());
				httpRes.setContent(pf);
		}
		else if(requestName.equals("gettimeshiftprograms")){//获取指定指定频道的时移节目
			String channelNr = params.getParameter("serviceid").getValue();
			int startIndex = Integer.parseInt(params.getParameter("startindex").getValue());
			int maxCount = Integer.parseInt(params.getParameter("maxcount").getValue());
			String tsp = getTimeShiftPrograms(channelNr, ContentType_TimeShift, startIndex,maxCount);
			httpRes.setContent(tsp);
		}
		else if(requestName.equals("getbackwatchprograms")) {//获取指定指定频道的回看节目列表
			String channelNr = params.getParameter("serviceid").getValue();
			int day = Integer.parseInt(params.getParameter("day").getValue());//回看天数1-7
			//String parentID = params.getParameter("parentID").getValue();
			//int type = Integer.parseInt(params.getParameter("type").getValue());
			int startIndex = Integer.parseInt(params.getParameter("startindex").getValue());
			int maxCount = Integer.parseInt(params.getParameter("maxcount").getValue());
			String tsp = BackWatchUtil.getBackWatchPrograms(channelNr, day, ContentType_BackWatch, startIndex,maxCount);
			httpRes.setContent(tsp);
		}
		else if(requestName.equals("getvodcategory")){//获取点播节目分类,parentID为空为一级分类，不为空为二级分类
			String parentID = params.getParameter("parentID").getValue();
			int startIndex = Integer.parseInt(params.getParameter("startindex").getValue());
			int maxCount = Integer.parseInt(params.getParameter("maxcount").getValue());
			String tsp = getVodCategory(parentID, ContentType_Vod, startIndex,maxCount);
			httpRes.setContent(tsp);
		}
		else if(requestName.equals("getvodprograms")){//获取点播节目列表，parentID为二级分类的id
			String parentID = params.getParameter("parentID").getValue();
			int startIndex = Integer.parseInt(params.getParameter("startindex").getValue());
			int maxCount = Integer.parseInt(params.getParameter("maxcount").getValue());
			String tsp = getVodPrograms(parentID, ContentType_Vod, startIndex,maxCount);
			httpRes.setContent(tsp);
		}
		else if(requestName.equals("getRecommendList")){//获取推荐节目
			String id = params.getParameter("id").getValue();//vod节目id
			String tsp = getRecommendList(id);
			httpRes.setContent(tsp);
		}
		else if(requestName.equals("getplayinfo")){
			String playUrl = params.getParameter("playUrl").getValue();
			try {
                                playUrl = URLDecoder.decode(playUrl, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                                Log.e("WebServer", "error", e);
                        }
			Log.i("getplayinfo", "playUrl:"+playUrl);
			String tsp = getPlayInfo(playUrl);
			httpRes.setContent(tsp);
		}
		else if(requestName.equals("play")){//播放vod节目，可以指定倍速
			String instanceID = params.getParameter("instanceid").getValue();
			int speed = Integer.parseInt(params.getParameter("speed").getValue());
			String tsp = VodStreamUtil.play(instanceID, speed);
			httpRes.setContent(tsp);
		}
		else if(requestName.equals("pause")){//暂停播放vod节目
			String instanceID = params.getParameter("instanceid").getValue();
			String tsp = VodStreamUtil.pause(instanceID);
			httpRes.setContent(tsp);
		}
		else if(requestName.equals("stop")){//停止播放vod节目
			String instanceID = params.getParameter("instanceid").getValue();
			String tsp = VodStreamUtil.stop(instanceID);
			httpRes.setContent(tsp);
		}
		else if(requestName.equals("seek")){//进度选择
			String instanceID = params.getParameter("instanceid").getValue();
			String time = params.getParameter("time").getValue();//格式:00:00:00
			String tsp = VodStreamUtil.seek(instanceID, time);
			httpRes.setContent(tsp);
		}
		else if(requestName.equals("getplayprogress")){//获取播放进度
			String instanceID = params.getParameter("instanceid").getValue();
			String tsp = VodStreamUtil.getPlayProgress(instanceID);
			httpRes.setContent(tsp);
		}
		else if(requestName.equals("getplaystatus")){//获取播放状态
			String instanceID = params.getParameter("instanceid").getValue();
			String tsp = VodStreamUtil.getPlayStatus(instanceID);
			httpRes.setContent(tsp);
		}
		else if(requestName.equals("playreport")) {
			TXbootApp app = (TXbootApp) mContext.getApplicationContext();
			ReportHelper.playreport(app, requestParams);
			httpRes.setContent("{\"code\":0,\"ret\":0,descript}");
		}
		else if(requestName.equals("report")) {
			TXbootApp app = (TXbootApp) mContext.getApplicationContext();
			ReportHelper.report(app, requestParams);
			httpRes.setContent("{\"code\":0,\"ret\":0,descript}");
		}
		else {
			httpRes.setContent("not found " + requestName + " !");
		}

		httpReq.post(httpRes);
	}

	//获取频道列表
	private String getAllChannel(){
		ArrayList<ChannelEntity> channelList = new ArrayList<ChannelEntity>();
		ChannelEntity channel = null;
		int CategoryNum = 0;
		for (int i = 0; i < 3 ; i ++) {
			channel = new ChannelEntity();
			channelList.add(channel);
		}

		try {
			String res = m_idvbService.getChannelList("");
			//Log.i("res", "res:"+res);
			XmlPullParser parser = Xml.newPullParser();
			try{
				parser.setInput(new ByteArrayInputStream(res.getBytes("UTF-8")), "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
						case XmlPullParser.START_DOCUMENT:
							break;
						case XmlPullParser.START_TAG:
							if (parser.getName().equals("DVBContainer")) {
								channel = new ChannelEntity();
								channel.setCategoryID(parser.getAttributeValue(null, "id"));
								channel.setCategoryName(parser.getAttributeValue(null, "name"));
							}
							break;
						case XmlPullParser.END_TAG:
							if (parser.getName().equals("DVBContainer")) {
								channel.setLiveChannels(getLiveChannelList(channel.getCategoryID()));
								if(CategoryNum==0){
									channelList.add(0,channel);
									channelList.remove(3);
								}else if(CategoryNum==1) {
									channelList.add(0,channel);
									channelList.remove(3);
								}else if(CategoryNum==2){
									channelList.add(2,channel);
									channelList.remove(3);
								}
								CategoryNum++;
							}
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

		return new Gson().toJson(channelList);
	}

	//获取直播频道信息
	private ArrayList<LiveChannelEntity> getLiveChannelList(String id){
		ArrayList<LiveChannelEntity> m_ret = new ArrayList<LiveChannelEntity>();
		LiveChannelEntity liveChannel = null;
		try {
			String res = m_idvbService.getChannelList(id);
			XmlPullParser parser = Xml.newPullParser();
			try {
				parser.setInput(new ByteArrayInputStream(res.getBytes("UTF-8")), "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
						case XmlPullParser.START_DOCUMENT:
							break;
						case XmlPullParser.START_TAG:
							if (parser.getName().equals("Channel")) {
								int channelId = Integer.parseInt(parser.getAttributeValue(null, "channelNr"));
								//频道过滤
								if((channelId >= 1 && channelId <= 79) || (channelId >= 100 && channelId <= 145) 
										|| (channelId >= 147 && channelId <= 156) || (channelId >= 177 && channelId <= 179)
										|| (channelId >= 200 && channelId <= 201) ||(channelId >= 210 && channelId <= 213) 
										|| (channelId >= 215 && channelId <= 220) || (channelId >= 200 && channelId <= 201)
										|| channelId == 222 || channelId == 224 || channelId == 226 || channelId == 288
										|| (channelId >= 230 && channelId <= 239) || (channelId >= 260 && channelId <= 282)){
									liveChannel = new LiveChannelEntity();
									liveChannel.setChannelID(parser.getAttributeValue(null, "id"));
									liveChannel.setChannelName(parser.getAttributeValue(null, "channelName"));
									liveChannel.setPlayURL(parser.getAttributeValue(null, "playURL"));
									liveChannel.setChannelNr(parser.getAttributeValue(null, "channelNr"));
									liveChannel.setServiceid(parser.getAttributeValue(null, "serviceid"));
								}
							}
							break;
						case XmlPullParser.END_TAG:
							if (parser.getName().equals("Channel") && liveChannel != null) {
								m_ret.add(liveChannel);
								liveChannel = null;
							}
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
		/*
		Collections.sort(m_ret, new Comparator<LiveChannelEntity>() {

			@Override
			public int compare(LiveChannelEntity arg0, LiveChannelEntity arg1) {
				Log.i("res", "arg0 channelNr:"+ arg0.getChannelNr() + "arg1 channelNr: " + arg1.getChannelNr());
				if(Integer.parseInt(arg0.getChannelNr()) > Integer.parseInt(arg1.getChannelNr()) ){
					return 1;
				}else{
					return 0;
				}
			}
		
		});
		*/
		return m_ret;
	}

	//获取直播peg
	private String getEpgList(String channelNr, String startTime, String endTime, int startIndex, int maxCount){
		ArrayList<EpgEntity> m_ret = new ArrayList<EpgEntity>();
		EpgEntity epgEntity = null;
		try {
			String res = m_iepgService.getEPGList(channelNr, startTime, endTime, startIndex, maxCount);
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
							if (parser.getName().equals("EPGEvent")) {
								epgEntity = new EpgEntity();
								epgEntity.setChannelID(parser.getAttributeValue(null, "channelNr"));
								epgEntity.setTitle(parser.getAttributeValue(null, "eventName"));
								epgEntity.setStartTimestamp(parser.getAttributeValue(null, "startTime"));
								epgEntity.setEndTimestamp(parser.getAttributeValue(null, "endTime"));
								epgEntity.setServiceid(parser.getAttributeValue(null, "serviceid"));
							}
							break;
						case XmlPullParser.END_TAG:
							if (parser.getName().equals("EPGEvent")) {
								m_ret.add(epgEntity);
							}
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

		return new Gson().toJson(m_ret);
	}

	//获取直播pf数据
	private String getPf(String channelNr){
		ArrayList<EpgEntity> m_ret = new ArrayList<EpgEntity>();
		EpgEntity epgEntity = null;

		try {
			String res = m_iepgService.getPF(channelNr);
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
							if (parser.getName().equals("EPGEvent")) {
								epgEntity = new EpgEntity();
								epgEntity.setChannelID(parser.getAttributeValue(null, "channelNr"));
								epgEntity.setTitle(parser.getAttributeValue(null, "eventName"));
								epgEntity.setStartTimestamp(parser.getAttributeValue(null, "startTime"));
								epgEntity.setEndTimestamp(parser.getAttributeValue(null, "endTime"));
							}
							break;
						case XmlPullParser.END_TAG:
							if (parser.getName().equals("EPGEvent")) {
								m_ret.add(epgEntity);
							}
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

		return new Gson().toJson(m_ret);
	}

	//获取指定逻辑频道号的时移节目列表
	public String getTimeShiftPrograms(String serviceId,int contentType,int startIndex,int maxCount){
		ArrayList<ShiftProgramEntity> m_ret = new ArrayList<ShiftProgramEntity>();
		ShiftProgramEntity shiftProgramEntity = null;

		try {
			String res = m_ivodService.getContentList("",contentType,startIndex,maxCount);
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
							if (parser.getName().equals("TimeShiftProgram")) {
								String _serviceid = parser.getAttributeValue(null, "serviceid");
								String[] ids = _serviceid.split(",");
								if(ids.length > 0){
									_serviceid = ids[ids.length - 1];
								}
					
								if(_serviceid.equals(serviceId)){
									shiftProgramEntity = new ShiftProgramEntity();
									shiftProgramEntity.setId(parser.getAttributeValue(null, "id"));
									shiftProgramEntity.setName(parser.getAttributeValue(null, "name"));
									shiftProgramEntity.setChannelName(parser.getAttributeValue(null, "channelName"));
									shiftProgramEntity.setPlayUrls(parser.getAttributeValue(null, "playURLs"));
									shiftProgramEntity.setStartTime(parser.getAttributeValue(null, "startTime"));
									shiftProgramEntity.setEndTime(parser.getAttributeValue(null, "endTime"));
									shiftProgramEntity.setServiceid(parser.getAttributeValue(null, "serviceid"));
								}
							}
							break;
						case XmlPullParser.END_TAG:
							if (parser.getName().equals("TimeShiftProgram") && shiftProgramEntity != null) {
								m_ret.add(shiftProgramEntity);
								shiftProgramEntity = null;
							}
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

		return new Gson().toJson(m_ret);
	}
	public String gettfprograms(String channelnr) {
		ArrayList<ShiftProgramEntity> m_ret = new ArrayList<ShiftProgramEntity>();
		try {
                        Log.i("res", "res getTimeShiftPrograms: channelnr = "+channelnr);
                        String res = m_ivodService.getTimeShiftPrograms(channelnr);
                        Log.i("res", "res:"+res);
			return res;
			/*
                        XmlPullParser parser = Xml.newPullParser();
                        try {
                                parser.setInput(new ByteArrayInputStream(res.getBytes("UTF-8")), "UTF-8");
                                int eventType = parser.getEventType();
                                while (eventType != XmlPullParser.END_DOCUMENT) {
                                        switch (eventType) {
                                                case XmlPullParser.START_DOCUMENT:
                                                        break;
                                                case XmlPullParser.START_TAG:
                                                        if (parser.getName().equals("TimeShiftProgram")) {
                                                                String _serviceid = parser.getAttributeValue(null, "serviceid");
                                                                _serviceid = _serviceid.split(",")[2];
                                                                if(_serviceid.equals(serviceId)){
                                                                        shiftProgramEntity = new ShiftProgramEntity();
                                                                        shiftProgramEntity.setId(parser.getAttributeValue(null, "id"));
                                                                        shiftProgramEntity.setName(parser.getAttributeValue(null, "name"));
                                                                        shiftProgramEntity.setChannelName(parser.getAttributeValue(null, "channelName"));
                                                                        shiftProgramEntity.setPlayUrls(parser.getAttributeValue(null, "playURLs"));
                                                                        shiftProgramEntity.setStartTime(parser.getAttributeValue(null, "startTime"));
                                                                        shiftProgramEntity.setEndTime(parser.getAttributeValue(null, "endTime"));
                                                                        shiftProgramEntity.setServiceid(parser.getAttributeValue(null, "serviceid"));
                                                                }
                                                        }
                                                        break;
                                                case XmlPullParser.END_TAG:
                                                        if (parser.getName().equals("TimeShiftProgram") && shiftProgramEntity != null) {
                                                                m_ret.add(shiftProgramEntity);
                                                                shiftProgramEntity = null;
                                                        }
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
			*/
                } catch (RemoteException e) {
                        e.printStackTrace();
                }

                return new Gson().toJson(m_ret);
	}
	public String gettfprogramsbysid(String serviceid) {
		ArrayList<ShiftProgramEntity> m_ret = new ArrayList<ShiftProgramEntity>();
		try {
                        Log.i("res", "res getTimeShiftByServiceId: serviceid = "+serviceid);
                        String res = m_ivodService.getTimeShiftProgramsByServiceId(serviceid);
                        Log.i("res", "res:"+res);
			return res;
                        /*
                        XmlPullParser parser = Xml.newPullParser();
                        try {
                                parser.setInput(new ByteArrayInputStream(res.getBytes("UTF-8")), "UTF-8");
                                int eventType = parser.getEventType();
                                while (eventType != XmlPullParser.END_DOCUMENT) {
                                        switch (eventType) {
                                                case XmlPullParser.START_DOCUMENT:
                                                        break;
                                                case XmlPullParser.START_TAG:
                                                        if (parser.getName().equals("TimeShiftProgram")) {
                                                                String _serviceid = parser.getAttributeValue(null, "serviceid");
                                                                _serviceid = _serviceid.split(",")[2];
                                                                if(_serviceid.equals(serviceId)){
                                                                        shiftProgramEntity = new ShiftProgramEntity();
                                                                        shiftProgramEntity.setId(parser.getAttributeValue(null, "id"));
                                                                        shiftProgramEntity.setName(parser.getAttributeValue(null, "name"));
                                                                        shiftProgramEntity.setChannelName(parser.getAttributeValue(null, "channelName"));
                                                                        shiftProgramEntity.setPlayUrls(parser.getAttributeValue(null, "playURLs"));
                                                                        shiftProgramEntity.setStartTime(parser.getAttributeValue(null, "startTime"));
                                                                        shiftProgramEntity.setEndTime(parser.getAttributeValue(null, "endTime"));
                                                                        shiftProgramEntity.setServiceid(parser.getAttributeValue(null, "serviceid"));
                                                                }
                                                        }
                                                        break;
                                                case XmlPullParser.END_TAG:
                                                        if (parser.getName().equals("TimeShiftProgram") && shiftProgramEntity != null) {
                                                                m_ret.add(shiftProgramEntity);
                                                                shiftProgramEntity = null;
                                                        }
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
                        */
                } catch (RemoteException e) {
                        e.printStackTrace();
                }

                return new Gson().toJson(m_ret);
	}
	
	//获取点播节目分类
	public String getVodCategory(String parentId, int contentType,int startIndex,int maxCount){
		ArrayList<VodCategoryEntity> m_ret = new ArrayList<VodCategoryEntity>();
		VodCategoryEntity vodCategoryEntity = null;
		try {
			String res = m_ivodService.getContentList(parentId,contentType,startIndex,maxCount);
			XmlPullParser parser = Xml.newPullParser();
			try {
				parser.setInput(new ByteArrayInputStream(res.getBytes("UTF-8")), "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
						case XmlPullParser.START_DOCUMENT:
							break;
						case XmlPullParser.START_TAG:
							if (parser.getName().equals("GeneralContainer")) {
								vodCategoryEntity = new VodCategoryEntity();
								vodCategoryEntity.setId(parser.getAttributeValue(null, "id"));
								vodCategoryEntity.setName(parser.getAttributeValue(null, "name"));
							}
							break;
						case XmlPullParser.END_TAG:
							if (parser.getName().equals("GeneralContainer") && vodCategoryEntity != null) {
								m_ret.add(vodCategoryEntity);
								vodCategoryEntity = null;
							}
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

		return new Gson().toJson(m_ret);
	}
	
	//获取点播节目列表
	public String getVodPrograms(String parentId, int contentType,int startIndex,int maxCount){
		ArrayList<VODProgramEntity> m_ret = new ArrayList<VODProgramEntity>();
		VODProgramEntity vodProgramEntity = null;
		try {
			String res = m_ivodService.getContentList(parentId,contentType,startIndex,maxCount);
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
							if (parser.getName().equals("VODProgram")) {
								vodProgramEntity = new VODProgramEntity();
								vodProgramEntity.setIsSerial(false);
								vodProgramEntity.setId(parser.getAttributeValue(null, "id"));
								vodProgramEntity.setName(parser.getAttributeValue(null, "name"));
								vodProgramEntity.setDescription(parser.getAttributeValue(null, "description"));
								vodProgramEntity.setDirector(parser.getAttributeValue(null, "director"));
								vodProgramEntity.setActor(parser.getAttributeValue(null, "actor"));
								vodProgramEntity.setDuration(parser.getAttributeValue(null, "duration"));
								vodProgramEntity.setPlayUrls(parser.getAttributeValue(null, "playURLs"));
								vodProgramEntity.setPosterURLs(parser.getAttributeValue(null, "posterURLs"));
							}else if(parser.getName().equals("SerialContainer")){
								vodProgramEntity = new VODProgramEntity();
								vodProgramEntity.setIsSerial(true);
								vodProgramEntity.setId(parser.getAttributeValue(null, "id"));
								vodProgramEntity.setName(parser.getAttributeValue(null, "name"));
								vodProgramEntity.setDescription(parser.getAttributeValue(null, "description"));
								vodProgramEntity.setDirector(parser.getAttributeValue(null, "director"));
								vodProgramEntity.setActor(parser.getAttributeValue(null, "actor"));
								vodProgramEntity.setDuration(parser.getAttributeValue(null, "duration"));
								vodProgramEntity.setPosterURLs(parser.getAttributeValue(null, "posterURLs"));
							}
							break;
						case XmlPullParser.END_TAG:
							if ((parser.getName().equals("VODProgram") || parser.getName().equals("SerialContainer")) 
									&& vodProgramEntity != null) {
								m_ret.add(vodProgramEntity);
								vodProgramEntity = null;
							}
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

		return new Gson().toJson(m_ret);
	}
	
	//获得推荐节目列表 @id vod节目id
	public String getRecommendList(String id){
		ArrayList<VODProgramEntity> m_ret_1 = new ArrayList<VODProgramEntity>();
		VODProgramEntity vodProgramEntity = null;
		ArrayList<ShiftProgramEntity> m_ret_2 = new ArrayList<ShiftProgramEntity>();
		ShiftProgramEntity timeShiftProgramEntity = null;
		
		try {
			String res = m_ivodService.getRecommendList(id);
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
							if (parser.getName().equals("VODProgram")) {
								vodProgramEntity = new VODProgramEntity();
								vodProgramEntity.setIsSerial(false);
								vodProgramEntity.setId(parser.getAttributeValue(null, "id"));
								vodProgramEntity.setName(parser.getAttributeValue(null, "name"));
								vodProgramEntity.setDescription(parser.getAttributeValue(null, "description"));
								vodProgramEntity.setDirector(parser.getAttributeValue(null, "director"));
								vodProgramEntity.setActor(parser.getAttributeValue(null, "actor"));
								vodProgramEntity.setDuration(parser.getAttributeValue(null, "duration"));
								vodProgramEntity.setPlayUrls(parser.getAttributeValue(null, "playURLs"));
								vodProgramEntity.setPosterURLs(parser.getAttributeValue(null, "posterURLs"));
							}else if(parser.getName().equals("SerialContainer")){
								vodProgramEntity = new VODProgramEntity();
								vodProgramEntity.setIsSerial(true);
								vodProgramEntity.setId(parser.getAttributeValue(null, "id"));
								vodProgramEntity.setName(parser.getAttributeValue(null, "name"));
								vodProgramEntity.setDescription(parser.getAttributeValue(null, "description"));
								vodProgramEntity.setDirector(parser.getAttributeValue(null, "director"));
								vodProgramEntity.setActor(parser.getAttributeValue(null, "actor"));
								vodProgramEntity.setDuration(parser.getAttributeValue(null, "duration"));
								vodProgramEntity.setPosterURLs(parser.getAttributeValue(null, "posterURLs"));
							}else if (parser.getName().equals("TimeShiftProgram")) {
								timeShiftProgramEntity = new ShiftProgramEntity();
								timeShiftProgramEntity.setId(parser.getAttributeValue(null, "id"));
								timeShiftProgramEntity.setName(parser.getAttributeValue(null, "name"));
								timeShiftProgramEntity.setChannelName(parser.getAttributeValue(null, "channelName"));
								timeShiftProgramEntity.setPlayUrls(parser.getAttributeValue(null, "playURLs"));
								timeShiftProgramEntity.setStartTime(parser.getAttributeValue(null, "startTime"));
								timeShiftProgramEntity.setEndTime(parser.getAttributeValue(null, "endTime"));
							}
							break;
						case XmlPullParser.END_TAG:
							if (parser.getName().equals("VODProgram") || parser.getName().equals("SerialContainer")) {
								m_ret_1.add(vodProgramEntity);
								vodProgramEntity = null;
							}else if (parser.getName().equals("TimeShiftProgram") && timeShiftProgramEntity != null) {
								m_ret_2.add(timeShiftProgramEntity);
								timeShiftProgramEntity = null;
							}
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
		
		if(m_ret_1.size() != 0){
			return new Gson().toJson(m_ret_1);
		}else if(m_ret_2.size() != 0){
			return new Gson().toJson(m_ret_2);
		}
		return "{}";
	}

	//获取播放信息
	public String getPlayInfo(String playUrl){
		ArrayList<PlayInfoEntity> m_ret = new ArrayList<PlayInfoEntity>();
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
							if (parser.getName().equals("Response")) {
								m_ret.add(playInfoEntity);
							}
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

		return new Gson().toJson(m_ret);
	}

}
