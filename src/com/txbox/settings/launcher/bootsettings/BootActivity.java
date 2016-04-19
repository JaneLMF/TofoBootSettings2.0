package com.txbox.settings.launcher.bootsettings;

import com.txbox.settings.common.TXbootApp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
 
public class BootActivity extends Activity {
	private TXbootApp app;
	@Override  
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		app = (TXbootApp) getApplicationContext();
		Intent intent = new Intent(BootActivity.this, WelcomeActivity.class);
		intent.putExtra("isBootup", true);
		this.startActivity(intent);

		//finish();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(app.GetExit()==true) finish();
	}
	
}
