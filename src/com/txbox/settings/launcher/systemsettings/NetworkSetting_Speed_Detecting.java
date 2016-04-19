package com.txbox.settings.launcher.systemsettings;

import com.txbox.settings.bean.GuidInfoBean;
import com.txbox.settings.bean.LoginBean;
import com.txbox.settings.bean.MusicBackPicBean;
import com.txbox.settings.bean.MusicBackPicItemBean;
import com.txbox.settings.bean.NetTestItemBean;
import com.txbox.settings.bean.NetWorkSpeedInfo;
import com.txbox.settings.bean.SetCfgResultBean;
import com.txbox.settings.bean.SpeedTestBean;
import com.txbox.settings.bean.SpeedTestItemBean;
import com.txbox.settings.common.LocalInfor;
import com.txbox.settings.impl.AuthImpl;
import com.txbox.settings.impl.NetWorkSpeedImpl;
import com.txbox.settings.impl.SyscfgImpl;
import com.txbox.settings.impl.SyscfgImpl.GetSpeedTestListener;
import com.txbox.settings.interfaces.INetworkSpeedImpl;
import com.txbox.settings.interfaces.ISyscfgImpl;
import com.txbox.settings.popupwindow.JudgeResolutionPop;
import com.txbox.settings.popupwindow.JudgeResolutionPop.IButtonClick;
import com.txbox.settings.utils.ConfigManager;
import com.txbox.settings.utils.ServerManager;
import com.txbox.txsdk.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class NetworkSetting_Speed_Detecting extends Activity{
	
	private Button b;
	private String murl = "";
	private TextView tencent,highestspeed,Avgspeed;
	private NetWorkSpeedInfo netWorkSpeedInfo = null;
	private Context mContext;
	private static final int UPDATEVIEW = 2;
	private JudgeResolutionPop JRPop;
	private  NetWorkSpeedImpl speedImpl;
	
	private TextView localTime;
	 private LocalInfor mLocalInfor;
	 private static final int UPDATETIME = 1024;
	Handler h = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				int y = (int) b.getY();
				if(y>200){
					y-=20;
				}else{
					y+=20;
				}
				
				b.setY(y);
				h.sendEmptyMessageDelayed(1, 2000);
				break;
			case UPDATEVIEW:
				initParams(netWorkSpeedInfo.downloadPercent,netWorkSpeedInfo.curspeed,netWorkSpeedInfo.avgspeed);// UI更新
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
		setContentView(R.layout.network_setting_speed_detecting);
		mContext = this;
		mLocalInfor = new LocalInfor(mContext);
		initView();
		initData();
		b = (Button) findViewById(R.id.test);
		h.sendEmptyMessageDelayed(1, 2000);
	}
	private void initView(){
		tencent = (TextView) findViewById(R.id.tencent);
		highestspeed = (TextView) findViewById(R.id.highestspeed);
		Avgspeed = (TextView) findViewById(R.id.Avgspeed);
		localTime = (TextView) findViewById(R.id.localTime);
		h.sendEmptyMessage(UPDATETIME);
		
	}
	
	private void initData(){
		GetSpeedTestUrl();
		
		//murl 应该从GetSpeedTestUrl获取，目前测试使用固定url
//		murl = "http://218.108.171.41:8360/Downloads/uploadAndroid/2013-06-13/51b95c332ac94.apk";
		
//		  speedImpl = new NetWorkSpeedImpl(this,murl);
//		speedImpl.setOnChangeListener(new INetworkSpeedImpl(){
//			@Override
//			public void onChange(NetWorkSpeedInfo speedinfo) {
//				// TODO Auto-generated method stub
//				netWorkSpeedInfo = speedinfo;
//				h.sendEmptyMessage(UPDATEVIEW);
//			}
//			
//		});
//		speedImpl.StartSpeedTest();
	}
	/**
	 * 
	 * @描述: update view
	 * @方法名: initParams
	 * @param Percent
	 * @param curspeed
	 * @param avgspeed
	 * @返回类型 void
	 * @创建人 huang
	 * @创建时间 Sep 18, 201410:53:22 AM	
	 * @修改人 huang
	 * @修改时间 Sep 18, 201410:53:22 AM	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	private void initParams(int Percent,long curspeed,long avgspeed){
		tencent.setText(String.valueOf(Percent));
		highestspeed.setText(String.valueOf(curspeed/1024));
		Avgspeed.setText(String.valueOf(avgspeed/1024));
		if(Percent >= 100){
			long curSpeed = curspeed/1024;
			JudgeResolution(curSpeed);
		}
	}
	/**
	 * 
	 * @描述: 获取网络测速url
	 * @方法名: GetSpeedTestUrl
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-10下午1:34:09	
	 * @修改人 Administrator
	 * @修改时间 2014-9-10下午1:34:09	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	private void GetSpeedTestUrl(){
		SyscfgImpl cfgImpl = new SyscfgImpl(mContext);
		cfgImpl.SetSpeedTestListener(new GetSpeedTestListener(){

			@Override
			public void onGetSpeedTestFinished(SpeedTestItemBean speedtestbean) {
				// TODO Auto-generated method stub
				try {
					if(speedtestbean!=null){
						murl = speedtestbean.getUrl();
						System.out.println("speed_test_url = " + murl);
						  speedImpl = new NetWorkSpeedImpl(NetworkSetting_Speed_Detecting.this,murl);
							speedImpl.setOnChangeListener(new INetworkSpeedImpl(){
								@Override
								public void onChange(NetWorkSpeedInfo speedinfo) {
									// TODO Auto-generated method stub
									netWorkSpeedInfo = speedinfo;
									h.sendEmptyMessage(UPDATEVIEW);
								}
								
							});
							speedImpl.StartSpeedTest();
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		LoginBean logininfo = new LoginBean(); 
		AuthImpl authImpl = new AuthImpl(mContext,null);
		GuidInfoBean guid = authImpl.GetGudiInfo(ConfigManager.GUID_INII_PATH);
		cfgImpl.GetSpeetTestInfo(ServerManager.SYSCFG_SERVER, ConfigManager.TV_TYPE, guid.getGuid(), logininfo, "speed_test_url","0");
	}
	
	private void JudgeResolution(long avgSpeed){
		
		String  Resolution = getResolution(avgSpeed);
		//传平均速度和超清 
		JRPop = new JudgeResolutionPop(mContext,avgSpeed,Resolution);
		JRPop.showListPop();
		JRPop.setReJudgeListener(new IButtonClick() {
			
			@Override
			public void OnJudgeButtonClick() {
				// TODO Auto-generated method stub
				initParams(0,0,0);
				speedImpl.StartSpeedTest();
			}
		});
	}
	private String getResolution(long avgSpeed){
		if(0 <= avgSpeed && avgSpeed <= 128){
			return mContext.getString(R.string.play_settings_SD);
		}else if(129 <= avgSpeed && avgSpeed <= 256){
			return mContext.getString(R.string.play_settings_HD);
		}else if(257 <= avgSpeed && avgSpeed <= 512){
			return mContext.getString(R.string.play_settings_SC);
		}else if(513 <= avgSpeed ){
			return mContext.getString(R.string.play_settings_BR);
		}
		return mContext.getString(R.string.play_settings_SD);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		speedImpl.StopSpeedTest();
		super.onDestroy();
	}
}
