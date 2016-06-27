package com.txbox.settings.launcher.bootsettings;

import org.cybergarage.util.ServiceUtils;

import com.txbox.settings.utils.ShellUtils;

import android.app.Activity;
import  android.os.Bundle;
import android.util.Log;

public class StartServiceActivity extends Activity {
	private static final String TAG = StartServiceActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		//检测BootSetting是否启动
		new Thread(){
			public void run() {
				ShellUtils.CommandResult commandResult = ShellUtils.execCommand("netstat -al |grep 2222", false);
		        Log.d(TAG, "onCreate check BootSetting " + commandResult);
		        if(commandResult.successMsg == null || commandResult.successMsg.equals("")){
		            new ServiceUtils(getApplicationContext()).start();//启动bootSetting
		            Log.d(TAG, "to start bootSetting");
		        }
			};
		}.start();
		this.finish();
	}
}
