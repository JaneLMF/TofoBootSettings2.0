package com.tvxmpp;


import com.tvxmpp.smack.SmackImpl;
import com.tvxmpp.ui.TableShowView;
import com.tvxmpp.util.L;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class WindowService extends Service {
	public static TableShowView showWin;

	String showText = "";
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public void onCreate() {
		super.onCreate();
	}

	public static void hidWind() {
		if (showWin != null){
			showWin.hidewin();
		}
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		// 创建service时实例化一个TableShowView对象并且调用他的fun()方法把它注册到windowManager
		
		if(intent != null){
			showText = intent.getStringExtra("showstr");
		}
		
		if (showWin == null){
			showWin = new TableShowView(getApplicationContext());
		}
		L.d(WindowService.class, "showWin.fun:" + showText);
		showWin.fun(showText);

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		hidWind();
		super.onDestroy();
	}
	
}
