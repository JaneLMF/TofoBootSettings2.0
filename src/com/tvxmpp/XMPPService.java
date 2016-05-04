package com.tvxmpp;

import com.tvxmpp.exception.XXException;
import com.tvxmpp.smack.SmackImpl;
import com.tvxmpp.ui.DialogShowView;
import com.tvxmpp.util.L;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class XMPPService extends Service{

	//private Context mContext;
	SmackImpl impl = null;
	
	private final String USER_NAME = "hmg1003";
	private final String PASSWORD = "hmg1003";
	
	private boolean bServiceStarted = false;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		L.d("XMPPService onCreate");
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
		if (impl.isAuthenticated()){
			impl.logout();
		}
		bServiceStarted = false;
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		//mContext = getApplicationContext();
		L.d("XMPPService onStartCommand");
		if (!bServiceStarted){
			startXMPPService();
			bServiceStarted = true;
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void startDialog(){
		Intent i = new Intent(this, DialogShowView.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(i);
	}
	private void startXMPPService() {
		L.d("XMPPService startXMPPService");
		impl = new SmackImpl(this);
		
        Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
								
				while(true){
					
					try {
						Thread.sleep(6000);
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
