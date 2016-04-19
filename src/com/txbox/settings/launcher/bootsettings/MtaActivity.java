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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class MtaActivity extends Activity{
	String eventID = null;
	String pr = null;
	List<KV> list = new ArrayList<KV>();
	String TAG="MatActivity";
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		String guid = GlobalInfo.getGUID();
		if (guid==null || guid.length()<=0) {
			AuthImpl authImpl = new AuthImpl(this,null);
			GuidInfoBean guidinfo = authImpl.GetGudiInfo(ConfigManager.GUID_INII_PATH);
			if (guidinfo!=null && guidinfo.getGuid()!=null) {
				GlobalInfo.setGUID(guidinfo.getGuid());
			}
		}
		
		Intent i = getIntent();
		Uri uri= i.getData();
		String urlstr = uri.getAuthority() + uri.getPath();
		getAppPamams(urlstr);
		TXbootApp app = (TXbootApp) this.getApplicationContext();
		if (eventID!=null) {
			if (pr == null || pr.length()<=0) {
				pr = "VIDEO";
			}
			ReportHelper.reportMta(app,eventID,pr,list);
		}
		finish();
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
