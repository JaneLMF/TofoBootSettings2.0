package com.txbox.settings.launcher.systemsettings;

import com.txbox.txsdk.R;
import com.txbox.settings.bean.GuidInfoBean;
import com.txbox.settings.bean.LoginBean;
import com.txbox.settings.bean.MusicBackPicBean;
import com.txbox.settings.bean.MusicBackPicItemBean;
import com.txbox.settings.bean.NetTestItemBean;
import com.txbox.settings.bean.NetWorkSpeedInfo;
import com.txbox.settings.bean.SetCfgResultBean;
import com.txbox.settings.bean.SpeedTestBean;
import com.txbox.settings.bean.SpeedTestItemBean;
import com.txbox.settings.impl.AuthImpl;
import com.txbox.settings.impl.NetWorkSpeedImpl;
import com.txbox.settings.impl.SyscfgImpl;
import com.txbox.settings.impl.SyscfgImpl.GetSpeedTestListener;
import com.txbox.settings.interfaces.INetworkSpeedImpl;
import com.txbox.settings.interfaces.ISyscfgImpl;
import com.txbox.settings.utils.ConfigManager;
import com.txbox.settings.utils.ServerManager;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * @类描述：用于测试网络测速的简单Activity
 * @项目名称：TXBootSettings
 * @包名： com.example.txbootsettings.launcher.system
 * @类名称：SpeedTestActivity	
 * @创建人：Administrator
 * @创建时间：2014-9-11上午11:18:21	
 * @修改人：Administrator
 * @修改时间：2014-9-11上午11:18:21	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class SpeedTestActivity extends Activity {
	private TextView fileLength = null;
	private TextView speed = null;
	private TextView hasDown = null;
	private TextView percent = null;
	private TextView avgspeed = null;
	private String murl = "";

	private NetWorkSpeedInfo netWorkSpeedInfo = null;
	private final int UPDATE_VIEW = 1;
	
	private Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_speedtest);
		mContext = this;
		hasDown = (TextView) findViewById(R.id.hasDown);
		fileLength = (TextView) findViewById(R.id.fileLength);
		speed = (TextView) findViewById(R.id.speed);
		percent = (TextView) findViewById(R.id.percent);
		avgspeed = (TextView) findViewById(R.id.avgspeed);
		Button b = (Button) findViewById(R.id.Button01);
		Button b2 = (Button) findViewById(R.id.Button02);
		GetSpeedTestUrl();
		
		//murl 应该从GetSpeedTestUrl获取，目前测试使用固定url
		murl = "http://218.108.171.41:8360/Downloads/uploadAndroid/2013-06-13/51b95c332ac94.apk";
		
		final NetWorkSpeedImpl speedImpl = new NetWorkSpeedImpl(this,murl);
		speedImpl.setOnChangeListener(new INetworkSpeedImpl(){
			@Override
			public void onChange(NetWorkSpeedInfo speedinfo) {
				// TODO Auto-generated method stub
				netWorkSpeedInfo = speedinfo;
				handler.sendEmptyMessage(UPDATE_VIEW);// UI更新
			}
			
		});
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				speedImpl.StartSpeedTest();
			}
		});
		b2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				speedImpl.StopSpeedTest();
			}
		});
	}


	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int value = msg.what;
			switch (value) {
			case UPDATE_VIEW:
				updateView(false);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 
	 * @描述:UI更新
	 * @方法名: updateView
	 * @返回类型 void
	 * @创建人 gao
	 * @创建时间 2014年8月6日下午12:03:32
	 * @修改人 gao
	 * @修改时间 2014年8月6日下午12:03:32
	 * @修改备注
	 * @since
	 * @throws
	 */
	private void updateView(boolean isComplete) {
		speed.setText(getString(R.string.current_speed) + netWorkSpeedInfo.curspeed / 1024 + "KB/S");
		hasDown.setText(getString(R.string.had_download) + netWorkSpeedInfo.hadFinishedBytes / 1024 + "KB");
		fileLength.setText(getString(R.string.file_size)  + netWorkSpeedInfo.totalBytes / 1024 + "KB");
	
		avgspeed.setText(getString(R.string.avg_speed)  + netWorkSpeedInfo.avgspeed / 1024 + "KB/S");
	
		percent.setText(getString(R.string.download_progress)  + netWorkSpeedInfo.downloadPercent + "%");
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
}
