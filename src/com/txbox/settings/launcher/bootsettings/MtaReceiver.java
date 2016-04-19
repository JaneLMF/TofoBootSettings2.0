package com.txbox.settings.launcher.bootsettings;

import java.util.ArrayList;
import java.util.List;

import com.txbox.settings.bean.GuidInfoBean;
import com.txbox.settings.common.TXbootApp;
import com.txbox.settings.impl.AuthImpl;
import com.txbox.settings.report.GlobalInfo;
import com.txbox.settings.report.KV;
import com.txbox.settings.report.ReportHelper;
import com.txbox.settings.utils.ConfigManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MtaReceiver extends BroadcastReceiver{
	String TAG="MatActivity";
	String eventID = null;
	String pr = null;
	List<KV> list = new ArrayList<KV>();
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (action.equals("com.tencent.mtareport")) {
			String data = intent.getStringExtra("data");
			String guid = GlobalInfo.getGUID();
			if (guid==null || guid.length()<=0) {
				AuthImpl authImpl = new AuthImpl(context,null);
				GuidInfoBean guidinfo = authImpl.GetGudiInfo(ConfigManager.GUID_INII_PATH);
				if (guidinfo!=null && guidinfo.getGuid()!=null) {
					GlobalInfo.setGUID(guidinfo.getGuid());
				}
			}
			
			getAppPamams(data);
			TXbootApp app = (TXbootApp) context.getApplicationContext();
			if (eventID!=null) {
				if (pr == null || pr.length()<=0) {
					pr = "VIDEO";
				}
				ReportHelper.reportMta(app,eventID,pr,list);
			}
		}
	}
	
	private void getAppPamams(String urlstr){
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
}
