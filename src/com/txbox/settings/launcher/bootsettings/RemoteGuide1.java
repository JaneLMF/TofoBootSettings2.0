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

public class RemoteGuide1 extends Activity{
	private LinearLayout main_ll;
	private Bitmap b;
	private static final int START_REMOTE_CMD = 1000;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case START_REMOTE_CMD:
				startRemoteGuide();
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
		main_ll = (LinearLayout) findViewById(R.id.main_ll);
		b = new DecodeImgUtil(this, R.raw.userguide2).getBitmap();
		BitmapDrawable bd= new BitmapDrawable(b);
		main_ll.setBackground(bd);
		handler.sendEmptyMessageDelayed(START_REMOTE_CMD, 3000);
	}
	
	private void startRemoteGuide(){
		Intent i = new Intent();
		i.setClass(this, RemoteGuide.class);
		startActivity(i);
		overridePendingTransition(R.anim.zoout, R.anim.zoin);
		RemoteGuide1.this.finish();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
                if(b!=null) b.recycle();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch (keyCode) {
		case KeyEvent.KEYCODE_ESCAPE:
		case KeyEvent.KEYCODE_BACK:			
				 return true;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
		
}
