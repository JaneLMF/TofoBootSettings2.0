package com.txbox.settings.launcher.systemsettings;

import java.util.ArrayList;
import java.util.Map;

import com.txbox.settings.common.LocalInfor;
import com.txbox.settings.common.NetworkManager;
import com.txbox.settings.common.TXbootApp;
import com.txbox.settings.launcher.EthernetActivity;
import com.txbox.settings.launcher.WirelessActivity;
import com.txbox.settings.mbx.api.NetManager;
import com.txbox.settings.mbx.api.NetManager.NetworkDetailInfo;
import com.txbox.txsdk.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NetworkSetting extends Activity{

	private ListView mListView;
	private ArrayList<String> dataList = new ArrayList<String>();
	private int[] dataStr = {R.string.network_detect_BigTitle,R.string.network_menu_speed_detect};
	//无线网络，有线网络，网络检测，网速检测，蓝牙
	private int curPosition = 0;
	MyAdapter mAdapter = null;
	private Context mContext;
	private static final int HASCONNECTED = 15;
	private static final int NOCONNECT = 9;
	private static final int DETECTENTHERNET_SUCCESS = 5;
	private static final int DETECTENTHERNET_FIAL = 6;
	private static final int DETECTWIRELESS = 7;
	private NetworkDetailInfo netInfor = null;
	private NetworkManager networkMgr;
	private NetManager netMgr;
	private TXbootApp app;
	private ArrayList<Map<String, String>> networkInfo = null;
	 private TextView localTime;
	 private LocalInfor mLocalInfor;
	 private static final int UPDATETIME = 1024;
	
	Handler h = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) { 
			case DETECTENTHERNET_SUCCESS: 
				String title = dataList.get(0);
				if(title.equals(mContext.getString(R.string.wireless_bigTitle))){
					dataList.remove(0);
					dataList.add(0,mContext.getString(R.string.ethernet_bigTitle));
				}
				mAdapter.setConnected(true, getResources().getString(R.string.detect_network_wirelessTip_connected));
				 
				break;
			case DETECTENTHERNET_FIAL:
				mAdapter.setConnected(false, "");
				netInfor = null;
				break;
			case DETECTWIRELESS:
				String title1 = dataList.get(0);
				if(title1.equals(mContext.getString(R.string.ethernet_bigTitle))){
					dataList.remove(0);
					dataList.add(0,mContext.getString(R.string.wireless_bigTitle));
				}
				if(netMgr!=null){
					netInfor = netMgr.getNetworkDetailInfo();
				}
				mAdapter.setConnected(true,netInfor.ssid );
				 
				break;
			case NOCONNECT:
				String title2 = dataList.get(0);
				if(title2.equals(mContext.getString(R.string.ethernet_bigTitle))){
					dataList.remove(0);
					dataList.add(0,mContext.getString(R.string.wireless_bigTitle));
				}
				 mAdapter.setConnected(false, "");
				break;
			case HASCONNECTED:

				break;
			case UPDATETIME:
				localTime.setText(mLocalInfor.getLocalTime());
				h.sendEmptyMessageDelayed(UPDATETIME, 5*1000);
				break;
			default:
				break;
			}
			
		};
	};
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(arg0);
		setContentView(R.layout.network_settings);
		mListView = (ListView) findViewById(R.id.menuList);
		localTime = (TextView) findViewById(R.id.localTime);
		h.sendEmptyMessage(UPDATETIME);
		mContext = this;
		mLocalInfor = new LocalInfor(mContext);
		networkMgr = new NetworkManager(mContext, h);
		networkMgr.regiestbroadcast();
		app = (TXbootApp) getApplicationContext();
		netMgr = app.getNetManager();
		
		initData();
		mAdapter = new MyAdapter(this, dataList);
		mListView.setAdapter(mAdapter);
		initParam();
		mListView.setOnItemSelectedListener(new ItemSelected());
		mListView.setOnItemClickListener(new ItemClicked());
		mListView.requestFocus();
	}
	
	private void initData(){
		dataList.clear();
		for (int i = 0; i < dataStr.length; i++) {
			dataList.add(getResources().getString(dataStr[i]));
		}
		
			networkInfo = app.getNetworkInfor();
			if(networkInfo!=null){
			Map<String, String> map = networkInfo.get(0);
			String title = map.get("netName");
			if(title.equals(mContext.getString(R.string.detect_network_detectedNetworkTip_wireless))){
				title = mContext.getString(R.string.wireless_bigTitle);
			}else if(title.equals(mContext.getString(R.string.detect_network_detectedNetworkTip_ethernet))){
				title = mContext.getString(R.string.ethernet_bigTitle);
			}
			dataList.add(0, title);
		    }
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		initParam();
		super.onResume();
	}
	
	private void initParam(){
		networkInfo = app.getNetworkInfor();
		if(networkInfo!=null){
			Map<String, String> map = networkInfo.get(0);
			String state = map.get("netState");
			String str = map.get("hasConnected");
			if(str.equals("true")){
				mAdapter.setConnected(true, state);
			}else{
				mAdapter.setConnected(false, state);
			}
		}
	}
	
	class MyAdapter extends BaseAdapter{

		private Context c;
		private ArrayList<String> data;
		private int curP;
		
		private TextView commtext;
		private TextView commState;
		private ImageView commIcon;
		private boolean  isConnected = false;
		private String State = "";
		ViewHolder  holder;
		public MyAdapter(Context c,ArrayList<String> data){
			this.c = c;
			this.data = data;
		}
		
		public void setConnected(boolean IsConnected,String State){
			this.isConnected = IsConnected;
			this.State = State;
			notifyDataSetChanged();
		}
		private void setSelectesPosition(int p){
			this.curP = p;
			notifyDataSetChanged();
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			if(data!=null){
				return data.get(arg0);
			}
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		class ViewHolder
		{
			RelativeLayout network_default;
			TextView tv;
			ImageView rightIcon;
			ImageView netIcon ;
			TextView netState;
			
			
			RelativeLayout network_selected;
			TextView tvSelected;
			ImageView netIconSelected;
			TextView netStateSelected;
			
		}
		
		
		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			
			
			if(arg1==null){
			    holder = new ViewHolder();
				LayoutInflater lay = LayoutInflater.from(c);
				arg1 = lay.inflate(R.layout.network_setting_menu_listitem, null);
				holder.network_default = (RelativeLayout)arg1.findViewById(R.id.network_default);
				holder.tv = (TextView) arg1.findViewById(R.id.text);
				holder.tvSelected = (TextView) arg1.findViewById(R.id.textSelected);
				holder.network_selected = (RelativeLayout) arg1.findViewById(R.id.network_selected);
				holder.netIconSelected = (ImageView) arg1.findViewById(R.id.netIconSelected) ;
				holder.netStateSelected = (TextView) arg1.findViewById(R.id.netStateSelected);
				holder.netIcon = (ImageView) arg1.findViewById(R.id.netIcon) ;
				holder.netState = (TextView) arg1.findViewById(R.id.netState);
				arg1.setTag(holder);
			}else{
				 holder = (ViewHolder) arg1.getTag();
			}
			
			if(curP == position){
				commtext  = holder.tvSelected;
				commIcon = holder.netIconSelected;
				commState = holder.netStateSelected;
				holder.network_default.setVisibility(View.GONE);
				holder.network_selected.setVisibility(View.VISIBLE);
			}else{
				commtext  = holder.tv;
				commIcon = holder.netIcon;
				commState = holder.netState;
				holder.network_selected.setVisibility(View.GONE);
				holder.network_default.setVisibility(View.VISIBLE);
			}
			
			
			String title = data.get(position);
			if(title.equals(mContext.getString(R.string.wireless_bigTitle))||title.equals(mContext.getString(R.string.ethernet_bigTitle))){
				if(isConnected){
					if(title.equals(mContext.getString(R.string.wireless_bigTitle))){
						String ssid = netMgr.getNetworkDetailInfo().ssid;
						if(ssid!=null){
							ScanResult mScanResult = netMgr.getScanResultBySSID(ssid.split("\"")[1]);
							if(mScanResult!=null){
								int wifi_de = netMgr.getWifiMgr().calculateSignalLevel(mScanResult.level, 4);
								if (wifi_de == 1) {
									commIcon.setImageResource(R.drawable.icon_wifi_1);
								} else if (wifi_de == 2) {
									commIcon.setImageResource(R.drawable.icon_wifi_2);
								} else if (wifi_de == 3) {
									commIcon.setImageResource(R.drawable.icon_wifi_3);
								} else {
									commIcon.setImageResource(R.drawable.icon_wifi);
								}
							}
						}
//						System.err.println("wifisigner"+mWifiInfo.getLinkSpeed());
//						commIcon.setImageResource(R.drawable.icon_wifi_3);
					}else{
					commIcon.setImageResource(R.drawable.green_right_icon);
					}
					commIcon.setVisibility(View.VISIBLE);
					commState.setText(State);
					commState.setVisibility(View.VISIBLE);
				}else{
					commIcon.setVisibility(View.GONE);
					commState.setVisibility(View.GONE);
				}
			}
			commtext.setText(title);
			return arg1;
		}
		
	}
	
	class ItemSelected implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			curPosition = position;
			mAdapter.setSelectesPosition(curPosition);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class ItemClicked implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			switch (position) {
			case 0:
				String title = dataList.get(0);
				if(title.equals(mContext.getString(R.string.ethernet_bigTitle))){
					startEthernet();
				}else{
					startWireless();
				}
				break;
			
			case 1:
				startNetworkSettingDetecting();
				break;
			case 2:
				startNetworkSettingSpeedDetecting();
				break;
	

			default:
				break;
			}
		}
		
	}
	
	private void startNetworkSettingDetecting(){
		Intent i = new Intent();
		i.setClass(this, NetworkSetting_Detecting.class);
		startActivity(i);
	} 	
	
	private void startNetworkSettingSpeedDetecting(){
		Intent i = new Intent();
		i.setClass(this, NetworkSetting_Speed_Detecting.class);
		startActivity(i);
	} 	
	
	private void startWireless(){
		Intent i  = new Intent();
		i.setClass(mContext, WirelessActivity.class);
		i.putExtra("type", "sys");
		startActivity(i);
		overridePendingTransition(R.anim.zoout, R.anim.zoin);
	}
	private void startEthernet(){
		Intent i  = new Intent();
		i.setClass(mContext, EthernetActivity.class);
		i.putExtra("type", "sys");
		startActivity(i);
		overridePendingTransition(R.anim.zoout, R.anim.zoin);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(networkMgr!=null){
			networkMgr.unreigest();
		}
		System.gc();
	
		super.onDestroy();
	}
 	
}
