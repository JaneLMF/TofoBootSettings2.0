package com.txbox.settings.launcher.bootsettings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.txbox.txsdk.R;
import com.txbox.settings.common.LocalInfor;
import com.txbox.settings.common.NetworkManager;
import com.txbox.settings.common.TXbootApp;
import com.txbox.settings.impl.SyscfgImpl;
import com.txbox.settings.mbx.api.NetManager;
import com.txbox.settings.mbx.api.NetManager.NetworkDetailInfo;
import com.txbox.settings.utils.ConfigManager;
import com.txbox.settings.utils.DecodeImgUtil;
import com.txbox.settings.utils.WebServer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class WelcomeActivity extends Activity{
	
	
	private static final int FIRSTENTERBOX = 1000;
	private static final int DETECTENTHERNET_SUCCESS = 5;
	private static final int DETECTWIRELESS = 7;
	private static final int NOCONNECT = 9;
	private static final int DISCONNECTETHERNET = 12;
	private static final int DISCONNECTWIFI = 8;
	private NetworkManager networkMgr;
	private Context mContext;
	private TXbootApp app;
	private NetManager netMgr;
	private ArrayList<Map<String, String>> networkInfo = new ArrayList<Map<String,String>>();
	Map<String, String> map = null;
	private NetworkDetailInfo netInfor = null;
	public boolean isBootup = false;//是否是开机自动启动，默认为false,只有从BootActivity启动该Activity时为true
	private static final String ANDROID_PATH = "/sdcard/Android/";
	private LinearLayout main_ll;
	private Bitmap b;
	Handler h = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case FIRSTENTERBOX:
				//startRemoteGuide();
				startScreenSettings();
				break;
			case DETECTENTHERNET_SUCCESS:
				break;
			case DETECTWIRELESS:
				break;
			case NOCONNECT:
			case DISCONNECTETHERNET:
			case DISCONNECTWIFI:
				break;
			default:
				break;
			}
			
		};
	};
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.welcome_activity);
		mContext = this;
		main_ll = (LinearLayout) findViewById(R.id.main_ll);
		b = new DecodeImgUtil(mContext, R.raw.userguide1).getBitmap();
		BitmapDrawable bd= new BitmapDrawable(b);
		main_ll.setBackground(bd);
		networkMgr = new NetworkManager(mContext, h);
		networkMgr.setWifiEnable(true);
		//networkMgr.regiestbroadcast();
		Intent intent = getIntent();
		isBootup = intent.getBooleanExtra("isBootup", false);
		app = (TXbootApp) getApplicationContext();
		initData();
		createFile();
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(app.GetExit()==true) finish();
		else h.sendEmptyMessageDelayed(FIRSTENTERBOX, 3000);
	}
	
	private void startRemoteGuide(){
		Intent i = new Intent();
		i.setClass(mContext, RemoteGuide1.class);
		startActivity(i);
		overridePendingTransition(R.anim.zoout, R.anim.zoin);
		//WelcomeActivity.this.finish();
	}
	
	private void startScreenSettings(){
		Intent i = new Intent();
		i.setClass(mContext, DetectNetworkActivity.class);
		i.putExtra("isBootup", isBootup);
		startActivity(i);
		overridePendingTransition(R.anim.zoout, R.anim.zoin);
		//WelcomeActivity.this.finish();
	}
	
	private void initData(){
		networkInfo.clear();
		map = new HashMap<String, String>();
		map.put("netName", getResources().getString(R.string.detect_network_detectedNetworkTip_wireless));
		map.put("netState", getResources().getString(R.string.detect_network_wirelessTip_disconnected));
		map.put("hasConnected", "false");
		networkInfo.add(map);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		/*
		if(networkMgr!=null)
			networkMgr.unreigest();
		*/
		if(b!=null) b.recycle();
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch (keyCode) {
		case KeyEvent.KEYCODE_ESCAPE:
		case KeyEvent.KEYCODE_BACK:			
			if(isBootup)//如果是开机自动启动则不响应返回键
				 return true;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
		
	private void createFile(){
		File f = new File(ANDROID_PATH);
		if(f.exists()){
			return;
		}
		f.mkdir();
		
	}
}
