package com.tvxmpp;

import com.tvxmpp.exception.XXException;
import com.tvxmpp.smack.SmackImpl;
import com.tvxmpp.ui.DialogShowView;
import com.tvxmpp.util.L;
import com.txbox.settings.data.PivosSharedPreferencesFactory;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;

public class XMPPService extends Service{

	//private Context mContext;
	SmackImpl impl = null;
	
	private String USER_NAME = null;
	private final String PASSWORD = "hmg1003";
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		PivosSharedPreferencesFactory sp = new PivosSharedPreferencesFactory(getApplicationContext());
		USER_NAME = sp.getXmppName();
		if(USER_NAME == null){
			USER_NAME = "hmg1003@message.localserver/"+getMacAddress();
		}
		L.d("USER_NAME:" + USER_NAME);
	}

	public String getMacAddress(){
		WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress().replace(":", "");
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
		if (impl.isAuthenticated()){
			impl.logout();
		}
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		//mContext = getApplicationContext();
		startXMPPService();
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void startDialog(){
		Intent i = new Intent(this, DialogShowView.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(i);
	}
	private void startXMPPService() {
		L.d("startXMPPService");
		impl = new SmackImpl(this);
		
        Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
								
				while(true){
					
					try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (!impl.isAuthenticated()){
						try {
							if (!impl.login(USER_NAME, PASSWORD)){
								L.d("login failed");
							}
							
						} catch (XXException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					//impl.Test();
					
					//startDialog();
					
				}
			}
		});
		t.start();
		
	}
}
