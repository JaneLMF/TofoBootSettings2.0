package com.txbox.settings.launcher.bootsettings;

import java.util.ArrayList;
import java.util.Map;

import com.txbox.txsdk.R;
import com.txbox.settings.bean.GuidBean;
import com.txbox.settings.bean.GuidDataBean;
import com.txbox.settings.bean.GuidInfoBean;
import com.txbox.settings.bean.ResultBean;
import com.txbox.settings.bean.TvsKeyBean;
import com.txbox.settings.common.NetworkManager;
import com.txbox.settings.common.TXbootApp;
import com.txbox.settings.impl.AuthImpl;
import com.txbox.settings.interfaces.IAuthImpl;
import com.txbox.settings.launcher.EthernetActivity;
import com.txbox.settings.launcher.WirelessActivity;
import com.txbox.settings.mbx.api.NetManager;
import com.txbox.settings.mbx.api.NetManager.NetworkDetailInfo;
import com.txbox.settings.utils.ConfigManager;
import com.txbox.settings.utils.DecodeImgUtil;
import com.txbox.settings.utils.DeviceUtils;
import com.txbox.settings.utils.ScaleAnimEffect;
import com.txbox.settings.utils.ServerManager;
import com.txbox.settings.utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DetectNetworkActivity extends Activity{
	
	
	private static final int DETECTENTHERNET_SUCCESS = 5;
	private static final int DETECTENTHERNET_FIAL = 6;
	private static final int DETECTWIRELESS = 7;
	private TextView detectingTip;
	private LinearLayout detectedNetworkTip;
	private TextView detectedNetworkTip_title;
	private TextView detectedNetworkTip_state;
	private Context mContext;
	private NetworkManager networkMgr;
	private static final int NOCONNECT = 9;
	private NetManager netMgr;
	private TXbootApp app;
	private NetworkDetailInfo netInfor = null;
	private LinearLayout next_ll;
	private static final int HASCONNECTED = 15;
	private static final int DISCONNECTETHERNET = 12;
	private static final int DISCONNECTWIFI = 8;
	private boolean hasConnected = false;
	private boolean iFirst = true;
	private ScaleAnimEffect animEffect;
	private float scaleX = 1.125f;
	private float scaleY = 1.125f;
	private ArrayList<Map<String, String>> networkInfo = null;
	private ImageView connect_icon;
	private TextView next_text;
	private Bitmap b;
	private RelativeLayout main_rl;
	Handler h = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) { 
			case DETECTENTHERNET_SUCCESS: 
				detectedNetworkTip_title.setText(mContext.getString(R.string.detect_network_detectedNetworkTip_ethernet));
				detectedNetworkTip_state.setText(mContext.getString(R.string.detect_network_wirelessTip_connected));
				connect_icon.setImageResource(R.drawable.ethernet_connected);
				 hasConnected = true;
				 h.removeMessages(HASCONNECTED);
				 h.sendEmptyMessage(HASCONNECTED);
				break;
			case DETECTENTHERNET_FIAL:
				detectedNetworkTip_title.setText(mContext.getString(R.string.detect_network_detectedNetworkTip_ethernet));
				detectedNetworkTip_state.setText(mContext.getString(R.string.detect_network_wirelessTip_disconnected));
				connect_icon.setImageResource(R.drawable.ethernet_connet_fail);
				netInfor = null;
				break;
			case DETECTWIRELESS:
				detectedNetworkTip_title.setText(mContext.getString(R.string.detect_network_detectedNetworkTip_wireless));
				if(netMgr!=null){
					netInfor = netMgr.getNetworkDetailInfo();
				}
				detectedNetworkTip_state.setText(mContext.getString(R.string.detect_network_wirelessTip_connected));
				connect_icon.setImageResource(R.drawable.wireless_connected_icon);
				hasConnected = true;
				 h.removeMessages(HASCONNECTED);
				 h.sendEmptyMessage(HASCONNECTED);
				break;
			case NOCONNECT:
				connect_icon.setImageResource(R.drawable.wireless_unconnect_icon);
				detectedNetworkTip_title.setText(mContext.getString(R.string.detect_network_detectedNetworkTip_wireless));
				detectedNetworkTip_state.setText(mContext.getString(R.string.detect_network_wirelessTip_disconnected));
				hasConnected = false;
				next_ll.setVisibility(View.GONE);
				break;
			case HASCONNECTED:
				detectingTip.setVisibility(View.GONE);	
				detectedNetworkTip.setVisibility(View.VISIBLE);
				detectedNetworkTip.requestFocus();
				next_ll.setVisibility(View.VISIBLE);
				isConnected(hasConnected);
				break;
			default:
				break;
			}
			
		};
	};
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.detect_network);
		mContext = this;
		main_rl = (RelativeLayout) findViewById(R.id.main_rl);
		b = new DecodeImgUtil(this, R.raw.settings_bg).getBitmap();
		BitmapDrawable bd= new BitmapDrawable(b);
		main_rl.setBackground(bd);
		networkMgr = new NetworkManager(mContext, h);
		networkMgr.regiestbroadcast();
		animEffect = new ScaleAnimEffect();
		app = (TXbootApp) getApplicationContext();
		netMgr = app.getNetManager();
		initView();
		initOnfocusChange();
		detectedNetworkTip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(detectedNetworkTip_title.getText().equals(mContext.getString(R.string.detect_network_detectedNetworkTip_ethernet))){
					
					Intent i  = new Intent();
					i.setClass(mContext, EthernetActivity.class);
					i.putExtra("type", "boot");
					startActivity(i);
//					overridePendingTransition(R.anim.zoout, R.anim.zoin);
					DetectNetworkActivity.this.finish();
					
				}else{
					Intent i  = new Intent();
					i.setClass(mContext, WirelessActivity.class);
					i.putExtra("type", "boot");
					startActivity(i);
//					overridePendingTransition(R.anim.zoout, R.anim.zoin);
					DetectNetworkActivity.this.finish();
				}
			}
		});
		next_ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub				
				enterBox();
			}
		});
		//networkMgr.regiestbroadcast();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent arg1) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && arg1.getAction()==KeyEvent.ACTION_DOWN){
			  startScreenSettings();
		}
		return super.onKeyDown(keyCode, arg1);
	}

	private void initView(){
		detectingTip = (TextView) findViewById(R.id.detectingTip);
		detectedNetworkTip = (LinearLayout) findViewById(R.id.detectedNetworkTip);
		detectedNetworkTip_title = (TextView) findViewById(R.id.detectedNetworkTip_title);
		detectedNetworkTip_state =  (TextView) findViewById(R.id.detectedNetworkTip_state);
		next_ll = (LinearLayout) findViewById(R.id.next_ll);
		connect_icon = (ImageView) findViewById(R.id.connect_icon);
		next_text = (TextView) findViewById(R.id.next_text);
		if(netMgr.isEthConnected()==true){
			detectedNetworkTip_title.setText(mContext.getString(R.string.detect_network_detectedNetworkTip_ethernet));
			detectedNetworkTip_state.setText(mContext.getString(R.string.detect_network_wirelessTip_connected));
			connect_icon.setImageResource(R.drawable.ethernet_connected);
			isConnected(true);
		}else if(netMgr.isWifiConnected()) {
			NetworkDetailInfo netInfo = netMgr.getNetworkDetailInfo();
			detectedNetworkTip_title.setText(mContext.getString(R.string.detect_network_detectedNetworkTip_wireless));
			detectedNetworkTip_state.setText(netInfo.ssid);
			connect_icon.setImageResource(R.drawable.wireless_connected_icon);
			isConnected(true);
		}else {
			detectedNetworkTip_title.setText(mContext.getString(R.string.detect_network_detectedNetworkTip_wireless));
			detectedNetworkTip_state.setText(mContext.getString(R.string.detect_network_wirelessTip_disconnected));
			connect_icon.setImageResource(R.drawable.wireless_unconnect_icon);
			isConnected(false);
		}
		
	}
	
	private void isConnected(boolean b){
		if(b){
			next_ll.setVisibility(View.VISIBLE);
			next_ll.setFocusable(true);
		}else{
			next_ll.setVisibility(View.GONE);
			next_ll.setFocusable(false);
		}
	}
	private void startScreenSettings(){
		Intent i = new Intent();
		i.setClass(mContext, ScreenSettingsActivity.class);
		startActivity(i);
		this.finish();
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(networkMgr!=null)
			networkMgr.unreigest();
		if(b!=null) b.recycle();
	}
	/**
	 * 
	 * @描述: hasFocus:startAnim,otherwise looseAnim
	 * @方法名: initOnfocusChange
	 * @返回类型 void
	 * @创建人 huang
	 * @创建时间 Sep 3, 201410:50:12 AM	
	 * @修改人 huang
	 * @修改时间 Sep 3, 201410:50:12 AM	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	private void initOnfocusChange(){
		detectedNetworkTip.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
					startAnimation();
				}else{
					looseAnimation();
				}
			}
		});
		next_ll.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
					next_ll.setBackgroundResource(R.drawable.btn_focus_bg);
					next_text.setTextColor(Color.parseColor("#FFFFFF"));
				}else{
					next_ll.setBackgroundResource(0);
					next_text.setTextColor(Color.parseColor("#4c5c6a"));
				}
			}
		});
	}
	private void startAnimation(){
		this.animEffect.setAttributs(1.0F, scaleX, 1.0F, scaleY, 200L);
		Animation localAnimation = this.animEffect.createAnimation();
		localAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				detectedNetworkTip.setBackgroundResource(R.drawable.login_focus_bg);
			}
		});
		detectedNetworkTip.startAnimation(localAnimation);
	}
	private void looseAnimation(){
		this.animEffect.setAttributs(scaleX, 1.0F,scaleY, 1.0F, 200L);
		Animation localAnimation = this.animEffect.createAnimation();
		localAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				detectedNetworkTip.setBackgroundResource(R.drawable.login_default_bg);
			}
		});
		detectedNetworkTip.startAnimation(localAnimation);
	}
		
	private void enterBox(){
		 Intent i = new Intent(mContext, EnterBoxActivity.class);
         startActivity(i);
//         overridePendingTransition(R.anim.zoout, R.anim.zoin);
         DetectNetworkActivity.this.finish();
	}
	
}
