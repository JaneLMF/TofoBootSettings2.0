package com.txbox.settings.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.SntpClient;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class NtpService extends Service{
	private String TAG = "NtpService";
	private static final int NTP_SYNC_START = 10;
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case NTP_SYNC_START:
				SyncTime();
				break;
			default:
				break;
			}
		};
	};
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.d("NtpService", "onCreate");
		super.onCreate();
		SyncTime();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.d("NtpService", "onDestory");
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d("NtpService", "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}
	private void SyncTime(){
		Runnable syncTimeRunner = new Runnable() { 
			@Override 
		    public void run() { 
				SyncTimeLock(); 
		     } 
		}; 
	    /* 启动线程负责网络通信 */
	    new Thread(syncTimeRunner).start(); 
	}
	protected void SyncTimeLock(){
		int 	NTP_TIMEOUT = 30000;
		String   setNtpServers[] ={"pool.ntp.org"};
		SntpClient client = new SntpClient();
		for (int i = 0; i< setNtpServers.length; i++){
			Log.d(TAG, "client.requestTime(server:" + setNtpServers[i]+", timeout:"+NTP_TIMEOUT+")");
			boolean flag = client.requestTime(setNtpServers[i], NTP_TIMEOUT);
			Log.d(TAG, flag + " = client.requestTime(server:" + setNtpServers[i]+", timeout:"+NTP_TIMEOUT+")");
			if(flag){
				long now = client.getNtpTime() +SystemClock.elapsedRealtime()-client.getNtpTimeReference();
				SystemClock.setCurrentTimeMillis(now);
				break;
			}else {
				mHandler.sendEmptyMessageDelayed(NTP_SYNC_START, 5000);
			}
		}
	}
}
