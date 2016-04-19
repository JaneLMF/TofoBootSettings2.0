package com.txbox.settings.common;

import java.util.ArrayList;
import java.util.List;

import com.txbox.settings.mbx.api.NetManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManagerPolicy;

public class NetworkManager {

	private Context mContext;
	NetManager netMgr = null;
	private MyReceiver myReceiver;
	public static final int KEYBOARD = 0;
	private static final int NET_REFRESH_WIFI_LIST = 1;
	private static final int NET_REFRESH_WIFI_STATE_INFO = 2;
	private final static int WIFI_CONNECTION_FAIL = 3;
	private boolean disconnectByUser = false;
	private boolean isAuthFailed = false;
	ScanResult conecting_ap;
	ScanResult selected_ap;
	ScanResult conected_ap;
	private WifiInfo currentWifiInfo;
	private List<ScanResult> scanResultList; 
	int wifiIndex = 0;
	private CheckWifiConnectionThread  checkWifiThread = null;
	private Handler myHandler;
	private static final int SHOWWIFILIST = 4;
	private static final int DETECTENTHERNET_SUCCESS = 5;
	private static final int DETECTENTHERNET_FIAL = 6;
	private static final int DETECTWIRELESS = 7;
	private static final int DISCONNECTWIFI = 8;
	private TXbootApp app;
	private static final int NOCONNECT = 9;
	private static final int SHOWENTERPASSWORD = 10;
	private static final int SHOWWIFICONNECTING = 11;
	private static final int DISCONNECTETHERNET = 12;
	private boolean lastIsEthernet = false;
	public NetworkManager(Context mContext,Handler myHandler){
		this.mContext = mContext;
		this.myHandler = myHandler;
		netMgr = new NetManager(mContext);
		app = (TXbootApp) mContext.getApplicationContext();
		app.setNetMgr(netMgr);
	}
	
	public void setWifiEnable(boolean value) {
		if(!netMgr.isEthDeviceAdded()) {
			if(value == true) {
				enableWifi();
			}
		}
	}
	public void setEthEnable(){
		Log.d("NetworkManager", "=======setEthEnable ======= isEthDevice added = " +netMgr.isEthDeviceAdded() );
		if(netMgr.isEthDeviceAdded()) {
			disableWifi();
			netMgr.enableEthernet(true);
		}else{
			enableWifi();
		}
	}
	public void regiestbroadcast(){
		scanResultList = netMgr.getWifiMgr().getScanResults();
		if (scanResultList == null) {
			scanResultList = new ArrayList<ScanResult>();
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);

		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
		//filter.addAction(WindowManagerPolicy.ACTION_HDMI_HW_PLUGGED);
		filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.LINK_CONFIGURATION_CHANGED_ACTION);
		

		myReceiver = new MyReceiver();
		mContext.registerReceiver(myReceiver, filter);
		
	}
	
	private void displayWifiList(int t){
		Message msg = new Message();
		msg.what = t;
		List<ScanResult> data = scanResultList;
		Log.d("MyReceiver", "=======displayWifiList ======= list size = " + data.size());
		//app.setScanResult(data);
		msg.obj = data;
		myHandler.removeMessages(t);
		myHandler.sendMessage(msg);
	}
	
	private  List<ScanResult> getSortlist(){
		currentWifiInfo = netMgr.getWifiMgr().getConnectionInfo();
		if(currentWifiInfo!=null&&currentWifiInfo.getSSID()!=null){
			for (int i = 0; i <scanResultList.size(); i++) {
				ScanResult result = scanResultList.get(i);
				if(("\"" + result.SSID + "\"").equals(currentWifiInfo.getSSID())){
					scanResultList.remove(i);
					scanResultList.add(0,result);
				}
			}
		}
		
		return scanResultList;
	}
	
	class MyReceiver extends BroadcastReceiver {
		String TAG = "MyReceiver";

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d("MyReceiver", "=======action========" + action);
			if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
				if (netMgr.isEthEnable()) {
					Log.d("MyReceiver", "=======isEthEnable========" + action);
					if(netMgr.isWifiConnected()){ // wifi is connected  show wifi list
						Log.d("MyReceiver", "=======WIFI is connected  show wifi list ========" + action);
						myHandler.removeMessages(DISCONNECTETHERNET);
						myHandler.sendEmptyMessage(DISCONNECTETHERNET);
						if(scanResultList.size()>0){
							displayWifiList(SHOWWIFILIST);//huang
						}
						new ReFreshWifiDetailThread().start();
						lastIsEthernet = false;
						myHandler.removeMessages(NOCONNECT);
						myHandler.removeMessages(DETECTWIRELESS);
						myHandler.sendEmptyMessage(DETECTWIRELESS);
						    
					}else if(netMgr.isEthConnected()){//  Ethernet is connect  
						Log.d("MyReceiver", "=======Ethernet is connect ========" + action);
						displayCurEthInfo();
						disableWifi();
						disconnectByUser = false;
						lastIsEthernet = true ;
					}else {
						 if (!netMgr.isEthDeviceAdded() && disconnectByUser == false) {// wire is out
							 //Log.d("MyReceiver", "=======Wired is plugout =start WIFI=======" + action);
								NetworkInfo info = netMgr.getActiveNetwork();
								if(info != null){
									int type = info.getType();
									if(type == ConnectivityManager.TYPE_ETHERNET){
										Log.d("MyReceiver", "===++++++++====Activi is Ethernet  ========" + action);
										Log.d("MyReceiver", "===++++++++====Connect state  ========" + netMgr.getEtherConnected());
										
									}else if(type == ConnectivityManager.TYPE_WIFI){
										Log.d("MyReceiver", "===++++++++====Activi is WIFI  ========" + action);
										Log.d("MyReceiver", "===++++++++====Connect state  ========" + netMgr.getWifiConnectState());
									}
									     
								}
	                         
	                         if(lastIsEthernet){  //huang
	                              enableWifi();
	                         }
	                         lastIsEthernet = false;//huang
//	                         
						}else {
							Log.d("MyReceiver", "=======XXX  show wifi list ========" + action);
						
						}
						
					}
				
  
				} else if (netMgr.isWIFIEnable()) {
					Log.d("MyReceiver", "============isWifiConnected:==" + netMgr.isWifiConnected());
					
				}
			} else if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)||
					action.equals(WifiManager.LINK_CONFIGURATION_CHANGED_ACTION)) {
				procesWIFIScanResult();
			} else if (action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
				processWifiState(intent);
			}else if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
				processWifiConnectChange(intent);
			}else if(!netMgr.isEthDeviceAdded()&&!netMgr.isWifiConnected()){
				myHandler.sendEmptyMessageDelayed(NOCONNECT, 1000);
				myHandler.removeMessages(DISCONNECTETHERNET);
			    myHandler.sendEmptyMessage(DISCONNECTETHERNET);
			}
		}
	}
	
	private void displayCurEthInfo(){
		if (netMgr.isEthDeviceAdded()) {
			 myHandler.removeMessages(DISCONNECTWIFI);
			myHandler.sendEmptyMessage(DISCONNECTWIFI);
			 myHandler.removeMessages(DETECTENTHERNET_SUCCESS);
			myHandler.sendEmptyMessage(DETECTENTHERNET_SUCCESS);
		}else{
			myHandler.removeMessages(DETECTENTHERNET_FIAL);
			myHandler.sendEmptyMessage(DETECTENTHERNET_FIAL);
		}
	}
	private void disableWifi()
	{
		netMgr.enableWIFI(false);
		if (scanResultList != null) {
			 scanResultList.clear();
		}
	}
	
	private void enableWifi() {	
		if (!netMgr.isWIFIEnable())
			netMgr.enableWIFI(true);
	}
	public void startScan() {
		if(!netMgr.isEthDeviceAdded())
			enableWifi();
		netMgr.getWifiMgr().startScan();
	}
	
	

	
	class ReFreshWifiDetailThread extends Thread
	{
		@Override
		public void run() {
			int strength = 0;
			String ipaddress ="";
			for (int i = 0; i < 10; i++) {
				currentWifiInfo = netMgr.getWifiMgr().getConnectionInfo();// �õ�������Ϣ
				int ip = currentWifiInfo.getIpAddress();
				ipaddress = (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + (ip >> 24 & 0xFF);
				if (ip != 0 && ( ipaddress.length() > 4)) {
					break;
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			myHandler.sendEmptyMessage(NET_REFRESH_WIFI_STATE_INFO);
		}
	}
	
	private void procesWIFIScanResult() {
//		displayWifiScanView(false);
		ArrayList<ScanResult> accessPoints = new ArrayList<ScanResult>();
		final List<ScanResult> results = netMgr.getWifiMgr().getScanResults();
		if (results != null) {
			for (ScanResult result : results) {
				if (TextUtils.isEmpty(result.SSID) || result.capabilities.contains("[IBSS]")) {
					continue;
				}
				Log.d("procesWIFIScanResult", "SSID" + result.SSID);
				boolean found = false;
	            for (ScanResult ap : accessPoints) {
	            	if (result.SSID.equals(ap.SSID)){
	            		if(NetManager.getSecurityType(result)==NetManager.getSecurityType(ap)){
	            			found = true;
	                	 }
	                  }
	            }
	            if (!found) {
	            	accessPoints.add(result);
	            }
			}
		}

		if (accessPoints.size() > 0 && scanResultList.size() != accessPoints.size()) {
			scanResultList.clear();
			scanResultList.addAll(accessPoints);
			if(scanResultList.size()>0){
				displayWifiList(NET_REFRESH_WIFI_LIST);//huang
			}
		}
	}
	
	public void processWifiListClicked(int position) {
		if(!netMgr.getWifiMgr().isWifiEnabled())
		{
				Log.w("processWifiListClicked", "Clicked AP ,when Wifi is off!");
				netMgr.enableWIFI(true);
		}
		if (position >= scanResultList.size()) {
			 Log.e("processWifiListClicked", " Select AP out of WIFI List:"+position +"/"+ scanResultList.size() );
		} else {
			ScanResult result = scanResultList.get(position);
			wifiIndex = position;
//			pushTofront(wifiIndex);
			
			final int secureType = netMgr.getSecurityType(result);
			
			 Log.e("processWifiListClicked", " Select AP :"+result.SSID );
			
			currentWifiInfo = netMgr.getWifiMgr().getConnectionInfo();// �õ�������Ϣ
			if (netMgr.isWifiConnected() && currentWifiInfo.getBSSID() != null && !"".equals(currentWifiInfo.getBSSID()) && currentWifiInfo.getBSSID().equals(scanResultList.get(position).BSSID)) {

			} else {
				String ssid = currentWifiInfo.getSSID();
				ssid =ssid.substring(1,ssid.length() -1);
				//conecting_ap =  netMgr.getScanResultBySSID(ssid);
				     
				if((conecting_ap!= null && conecting_ap.SSID.equals(result.SSID))  || (result.SSID.equals(ssid) && netMgr.getWifiConnectState() == State.CONNECTING)){ // current try connecting 
					 System.out.println("+++++++++++++++++++++++++Selected wifi is connecting++++++++++");
		         	if(isAuthFailed)
					 return ;
				}
				
				if (netMgr.isWifiSaved(result) /*&& !netMgr.isWifiConfDisable(result)*/) {
					  Log.d("processWifiListClicked", "AP: " + result.SSID + " Has Conf");
					  selected_ap = result;
					  
					   disconnectByUser = true;
				   	   conecting_ap = selected_ap;
				   	   Log.d("processWifiListClicked", "Select AP "  + " To Connect" );
				   	   doWifiConnect(selected_ap,null,null);
					 					  					 
					  return;
				}else {
					
				} 

				if (secureType == NetManager.SECURITY_NONE) {
					 selected_ap = result;
//					 hidNetDetail(true);//huang
					 disconnectByUser = true;
					 doWifiConnect(selected_ap,null,null);
				} else {
					selected_ap = result; //���������

					Message msg = new Message();
					msg.what = SHOWENTERPASSWORD ;
					msg.obj = selected_ap;
					myHandler.sendMessage(msg);
					
				}
			}
		}
	}
	
	public void ignore(int position){ 
		if(scanResultList!=null&&scanResultList.size()>0){
			ScanResult result = scanResultList.get(position);
			selected_ap = result;
			 WifiConfiguration conf =  netMgr.getSavedWifiConfig(selected_ap);
			   if(conf != null){
				   int netId = conf.networkId;
	 				netMgr.getWifiMgr().disableNetwork(netId);
	 				netMgr.getWifiMgr().removeNetwork(netId);   // ignore the network
	 				netMgr.getWifiMgr().saveConfiguration();
			   }
	 			selected_ap = null;	
	 			conecting_ap = null;
	 			if(scanResultList.size()>0){
	 				displayWifiList(NET_REFRESH_WIFI_LIST);
	 			}
	 			myHandler.sendEmptyMessage(NOCONNECT);
		}
 			
	}
	
	public void connectingWifi(String passwd){ //����������ɴ˴�����ȥ��������
		conecting_ap = selected_ap;
		doWifiConnect(conecting_ap,passwd,"");
	}
	private void processWifiState(Intent intent) {
		WifiInfo info = netMgr.getWifiMgr().getConnectionInfo();
		SupplicantState state = info.getSupplicantState();

   		String str = null;
		if (state == SupplicantState.ASSOCIATED) {
			str = "关联AP完成";
		} else if (state.toString().equals("AUTHENTICATING")) {
			str = "正在验证";
			String ssid = info.getSSID();
			ssid =ssid.substring(1,ssid.length() -1);
			conecting_ap =  netMgr.getScanResultBySSID(ssid);
			if(conecting_ap != null)
				   showWifiConnecting(conecting_ap.SSID);
		} else if (state == SupplicantState.ASSOCIATING) {
			str = "正在关联AP...";
		} else if (state == SupplicantState.COMPLETED) {
			str = "已连接";
		} else if (state == SupplicantState.DISCONNECTED) {
			str = "已断开";
		} else if (state == SupplicantState.DORMANT) {
			str = "暂停活动";
		} else if (state == SupplicantState.FOUR_WAY_HANDSHAKE) {
			str = "四路握手中...";
			String ssid = info.getSSID();
			ssid =ssid.substring(1,ssid.length() -1);
			conecting_ap =  netMgr.getScanResultBySSID(ssid);
			if(conecting_ap != null)
			   showWifiConnecting(conecting_ap.SSID);
		} else if (state == SupplicantState.GROUP_HANDSHAKE) {
			str = "GROUP_HANDSHAKE";
		} else if (state == SupplicantState.INACTIVE) {
			str = "休眠中...";
			if(isAuthFailed ){
				Log.d("ProcessWifiState", "++++++++++++++" +info.getSSID()+" ++++++++=" + str);
//				handler.sendEmptyMessage(HIDE_WIFI_CONNECTION_OBTAIN);//huang
				myHandler.removeMessages(WIFI_CONNECTION_FAIL);
				myHandler.sendEmptyMessage(WIFI_CONNECTION_FAIL);
				conecting_ap = null; //clear the data when wifi connect fail
			   //	handler.sendEmptyMessageDelayed(HIDE_WIFI_CONNECTION_FAIL, 2000);
				if(checkWifiThread != null) checkWifiThread.needStop = true;
				isAuthFailed = false;
				return ;
			}
		} else if (state == SupplicantState.INVALID) {
			str = "无效";
		} else if (state == SupplicantState.SCANNING) {
			str = "扫描中...";
		} else if (state == SupplicantState.UNINITIALIZED) {
			str = "未初始化";
		}
		Log.d("ProcessWifiState", "++++++++++++++" +info.getSSID()+" ++++++++=" + str);
		final int errorCode = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
		if (errorCode == WifiManager.ERROR_AUTHENTICATING) { // AUTHENTICATING
																// FAILED
			  Log.d("ProcessWifiState", "++++++++++?????++++++" +info.getSSID()+" ++++++=WIFI验证失败！");
			  isAuthFailed = true;
			  
			 WifiInfo info2 =   netMgr.getWifiMgr().getConnectionInfo();
			 WifiConfiguration config=  netMgr.getSavedWifiConfig(conecting_ap);
			 
			
			 if(config != null){
				 Log.d("ProcessWifiState", "++++++++++Remove config" + info2.getSSID());
				  int id = config.networkId;
				  netMgr.getWifiMgr().disableNetwork(id);
				  netMgr.getWifiMgr().removeNetwork(id);
				  netMgr.getWifiMgr().saveConfiguration();  
			 }
		}
	}
	
	private void showWifiConnecting(String ssid){
//		if(ssid != null) //huang
//			settingsPageThreeStatusContent.setText( "\"" + ssid +" \"" +getResources().getString(R.string.string_wifi_connecting));
//		 settingsPageThreeStatusContent.setVisibility(View.VISIBLE);//huang
		
		myHandler.removeMessages(SHOWWIFICONNECTING);
		myHandler.sendEmptyMessage(SHOWWIFICONNECTING);//��ʾ��������text
	}
	
	private void processWifiConnectChange(Intent intent)
	{
		  //WifiInfo info = netMgr.getWifiMgr().getConnectionInfo();
		  Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);    
          if (null != parcelableExtra) {    
              NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;    
		      Log.d("processWifiConnectChange", "++++++++++WIFI :"+ networkInfo.getState());
		      showWifiConnectionState(networkInfo);
          }
	}
	
	private void showWifiConnectionState(NetworkInfo networkInfo)
	{
		 WifiInfo info = netMgr.getWifiMgr().getConnectionInfo();
		 if(info != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI ){
			 // info.getSSID();
			 // Log.d("processWifiConnectChange", "++++++++++WIFI :"+ info.getState());
			 if(networkInfo.getState() == State.CONNECTED || networkInfo.getState() == State.CONNECTING){
//				 settingsPageThreeStatusContent.setText(   info.getSSID() +" " +getResources().getString(R.string.string_wifi_connecting));
//				 settingsPageThreeStatusContent.setVisibility(View.VISIBLE);//huang
//				 myHandler.sendEmptyMessage(SHOWWIFICONNECTING);//��ʾ��������text
			 }
		 }
	}
	
	class CheckWifiConnectionThread extends Thread {
        boolean needStop = false;
		@Override
		public void run() {

			int i = 0;
			WifiInfo	curInfo = null;
			for ( i = 0; i < 60 && needStop == false; i++) {
					curInfo = netMgr.getWifiMgr().getConnectionInfo();
					if(curInfo != null){
						  SupplicantState state  = curInfo.getSupplicantState();
						  if(SupplicantState.COMPLETED == state  ){
							      break;
						  } else if (SupplicantState.INACTIVE == state){
							      
						  }
					}
					try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
			}
			
			if(needStop) return ;
			if(i>=60){// send connection time out msg
				 Log.d("CheckWifiConnectionThread", "   ++++++++++++ wifi connection time out  :"+ curInfo.getSSID());
				 myHandler.removeMessages(WIFI_CONNECTION_FAIL);
				 myHandler.sendEmptyMessage(WIFI_CONNECTION_FAIL) ;
				 WifiInfo info2 =   netMgr.getWifiMgr().getConnectionInfo();
				 WifiConfiguration config=  netMgr.getSavedWifiConfig(conecting_ap);
				 if(config != null){
					 Log.d("CheckWifiConnectionThread", "++++++++++Remove config" + info2.getSSID());
					  int id = config.networkId;
					  netMgr.getWifiMgr().disableNetwork(id);
					  netMgr.getWifiMgr().removeNetwork(id);
					  netMgr.getWifiMgr().saveConfiguration();  
				 }
				 
			}
			
			super.run();
		}
	}
	
	public void doWifiConnect(ScanResult ap,String password, String username){
		conecting_ap = ap;
		netMgr.connect2AccessPoint(ap, password, "");
		showWifiConnecting(ap.SSID);
		if(checkWifiThread != null) checkWifiThread.needStop = true;
		checkWifiThread = new CheckWifiConnectionThread();
		checkWifiThread.start();
		  
	}
	
	public void unreigest(){
		if(myReceiver!=null)
			mContext.unregisterReceiver(myReceiver);
	}
	
}
