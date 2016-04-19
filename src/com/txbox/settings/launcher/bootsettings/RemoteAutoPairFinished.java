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

public class RemoteAutoPairFinished extends Activity{
	private LinearLayout main_ll;
	private Bitmap b;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.welcome_activity);
		main_ll = (LinearLayout) findViewById(R.id.main_ll);
		b = new DecodeImgUtil(this, R.raw.userguide5).getBitmap();
		BitmapDrawable bd= new BitmapDrawable(b);
		main_ll.setBackground(bd);
	}
	private void startScreenSettings(){
		Intent i = new Intent();
		i.setClass(this, ScreenSettingsActivity.class);
		startActivity(i);
		overridePendingTransition(R.anim.zoout, R.anim.zoin);
		RemoteAutoPairFinished.this.finish();
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
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
			startScreenSettings();
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
		
}
