package com.txbox.settings.launcher.bootsettings;

import com.txbox.settings.common.TXbootApp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

public class RestartTxPlayerActivity extends Activity{
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		
		TXbootApp app = (TXbootApp) getApplicationContext();
		app.initPlaySdk(getApplicationContext());
		
		finish();
		
		 
	}
}
