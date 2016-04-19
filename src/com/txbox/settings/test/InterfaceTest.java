package com.txbox.settings.test;

import com.txbox.txsdk.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class InterfaceTest extends Activity{

	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(arg0);
		setContentView(R.layout.network_detect_settings);
	}
}
