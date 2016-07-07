package com.tvxmpp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

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
import android.util.Log;

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
			USER_NAME = getMacAddress();
			if(USER_NAME == null){
				USER_NAME = "hmg1003";
			}else{
				USER_NAME = "hmg1003@message.localserver/"+USER_NAME;
				sp.setXmppName(USER_NAME);
			}
		}
		L.d("USER_NAME:" + USER_NAME);
	}

//	public String getMacAddress(){
//		WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = wifi.getConnectionInfo();
//        //NetworkInterface. getHardwareAddress ()
//        return info.getMacAddress().replace(":", "");
//	}
	
	/* 
	  *****************************************************************
	  *                       子函数：获得本地MAC地址
	  *****************************************************************                        
	 */   
	 public String getMacAddress(){   
	     String result = "";     
	     String Mac = "";
	     result = callCmd("busybox ifconfig","HWaddr");
	      
	     //如果返回的result == null，则说明网络不可取
	     if(result==null){
	         return null;
	     }
	      
	     //对该行数据进行解析
	     //例如：eth0      Link encap:Ethernet  HWaddr 00:16:E8:3E:DF:67
	     if(result.length()>0 && result.contains("HWaddr")==true){
	         Mac = result.substring(result.indexOf("HWaddr")+6, result.length()-1);
	         Log.i("test","Mac:"+Mac+" Mac.length: "+Mac.length());
	          
	         if(Mac.length()>1){
	             Mac = Mac.replaceAll(" ", "");
	             result = "";
	             String[] tmp = Mac.split(":");
	             for(int i = 0;i<tmp.length;++i){
	                 result +=tmp[i];
	             }
	         }
	         Log.i("test",result+" result.length: "+result.length());            
	     }
	     return result;
	 }   
	 
	  
	 public String callCmd(String cmd,String filter) {   
	     String result = "";   
	     String line = "";   
	     try {
	         Process proc = Runtime.getRuntime().exec(cmd);
	         InputStreamReader is = new InputStreamReader(proc.getInputStream());   
	         BufferedReader br = new BufferedReader (is);   
	          
	         //执行命令cmd，只取结果中含有filter的这一行
	         while ((line = br.readLine ()) != null && line.contains(filter)== false) {   
	             //result += line;
	             Log.i("getMac","line: "+line);
	         }
	          
	         result = line;
	         Log.i("getMac","result: "+result);
	     }   
	     catch(Exception e) {   
	         e.printStackTrace();   
	     }   
	     return result;   
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
