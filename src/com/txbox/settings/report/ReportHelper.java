package com.txbox.settings.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.tencent.dmproxy.logic.SdkConfig;
import com.tencent.omg.stat.StatServiceImpl;
import com.tencent.stat.StatService;
import com.txbox.settings.bean.GuidInfoBean;
import com.txbox.settings.common.TXbootApp;
import com.txbox.settings.impl.AuthImpl;
import com.txbox.settings.utils.ConfigManager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * report stat even
 * @author hardyxin 
 */
public class ReportHelper {
	private static String eventID = null;
	private static String pr = null;
	//private static List<KV> list = new ArrayList<KV>();
	private static String TAG="ReportHelper";
	/**
	 * 统计每一次拉起apk
	 * @param ctx
	 * @param postion
	 * @param cid
	 * @param vid
	 */
	public synchronized static void report(Context context,String urlstr) {
		String guid = GlobalInfo.getGUID();
		if (guid==null || guid.length()<=0) {
			AuthImpl authImpl = new AuthImpl(context,null);
			GuidInfoBean guidinfo = authImpl.GetGudiInfo(ConfigManager.GUID_INII_PATH);
			if (guidinfo!=null && guidinfo.getGuid()!=null) {
				GlobalInfo.setGUID(guidinfo.getGuid());
			}
		}
		List<KV> list = new ArrayList<KV>();
		getAppPamams(urlstr,list);
		if (eventID!=null) {
			if (pr == null || pr.length()<=0) {
				pr = "VIDEO";
			}
			reportMta(context,eventID,pr,list);
		}
	}
	public synchronized static void playreport(Context context,String urlstr) {
		Log.d(TAG, "play report params = " + urlstr);
		List<KV> list = new ArrayList<KV>();
		getAppPamams(urlstr,list);
		StatServiceImpl.trackCustomKVEvent(context, eventID, CommonParams.getPlayerCustomProps(context, pr, list), MtaOptions.getMtaSpecifInfo());
	}
	private static void getAppPamams(String urlstr,List<KV> list){
		Log.d(TAG, "params = " + urlstr);
		String[] str = urlstr.split("&");
		for (int i = 0; i < str.length; i++) {
			int index = str[i].indexOf("=");
			if(index>=0){
				String key  = str[i].substring(0, index);
				String value  = str[i].substring(index+1);
				Log.d(TAG, "key= " + key + " value = " + value);
				if (key.equals("eventid")) {
					eventID = value;
				} else if (key.equals("pr")) {
					pr = value;
				} else {
					KV kv = new KV(key,value);
					list.add(kv);
				}
			}
		}
	}
	public static void reportMta(Context ctx,String eventID,String pr,List<KV> list) {
		StatService.trackCustomKVEvent(ctx, eventID, CommonParams.getCustomProps(ctx,pr,list));
	}
}
