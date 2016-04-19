package com.txbox.settings.launcher.bootsettings;

import com.txbox.settings.bean.GuidBean;
import com.txbox.settings.bean.GuidInfoBean;
import com.txbox.settings.bean.TvsKeyBean;
import com.txbox.settings.bean.UpgradeBean;
import com.txbox.settings.common.TXbootApp;
import com.txbox.settings.impl.AuthImpl;
import com.txbox.settings.impl.RomUpgradeImpl;
import com.txbox.settings.interfaces.IAuthImpl;
import com.txbox.settings.interfaces.IRomUpgradeImpl;
import com.txbox.settings.launcher.systemsettings.SystemUpdateSettings;
import com.txbox.settings.service.DownLoadService;
import com.txbox.settings.utils.ConfigManager;
import com.txbox.settings.utils.DecodeImgUtil;
import com.txbox.settings.utils.DeviceUtils;
import com.txbox.settings.utils.ImageDownloader2;
import com.txbox.settings.utils.ServerManager;
import com.txbox.settings.utils.Utils;
import com.txbox.txsdk.R;

import android.R.integer;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

public class EnterBoxActivity extends Activity{

	private Bitmap b;
	private LinearLayout main_ll;
	private int menupressnum = 0;
	private Context mContext;
	private final int checkmenupressnum =1;
	private final int finishActivity = 2;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case checkmenupressnum:
				menupressnum = 0;
				break;
			case finishActivity:
				FinishSettings();
				EnterBoxActivity.this.finish();
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
		
		setContentView(R.layout.enter_box_layout);
		mContext = EnterBoxActivity.this;
		main_ll = (LinearLayout) findViewById(R.id.main_ll);
		b = new DecodeImgUtil(this, R.raw.enterbox).getBitmap();
		BitmapDrawable bd= new BitmapDrawable(b);
		main_ll.setBackground(bd);
		//GetRomUpgradeInfo();
		handler.sendEmptyMessageDelayed(finishActivity, 1000);
	}
	
	@Override
	public void onDestroy() {
		handler.removeMessages(finishActivity);
		if(b!=null) b.recycle();
		super.onDestroy();
	}
	@Override
	public boolean onKeyDown(int arg0, KeyEvent arg1) {
		// TODO Auto-generated method stub
		
		if(arg0 == KeyEvent.KEYCODE_DPAD_CENTER&& arg1.getAction() == KeyEvent.ACTION_DOWN){
			FinishSettings();
			
			EnterBoxActivity.this.finish();
		}else if(arg0 == KeyEvent.KEYCODE_ENTER&& arg1.getAction() == KeyEvent.ACTION_DOWN){
			FinishSettings();
			
			EnterBoxActivity.this.finish();
		}else if(arg0 == KeyEvent.KEYCODE_BACK&& arg1.getAction() == KeyEvent.ACTION_DOWN){
			startDetecting();
		}else if(arg0==KeyEvent.KEYCODE_MENU){
			if(menupressnum==0){
				handler.removeMessages(checkmenupressnum);
				handler.sendEmptyMessageDelayed(checkmenupressnum, 1000);
			}
			menupressnum++;
			if(menupressnum>=3){
				Utils.openAPP(EnterBoxActivity.this,"com.skyworthdigital.autotest");
			}
			
		}
		
		return super.onKeyDown(arg0, arg1);
	}
	
	private void FinishSettings(){
		/*设置完成，禁止下次开机时自动启动*/
	
		PackageManager pm = getPackageManager();  
        	ComponentName name = new ComponentName(EnterBoxActivity.this, BootActivity.class);
        	pm.setComponentEnabledSetting(name, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
        
        	TXbootApp app = (TXbootApp) getApplicationContext();
        	app.SetExit(true);
        
	}
	
	private void startDetecting(){
		Intent i = new Intent();
		i.setClass(this, DetectNetworkActivity.class);
		startActivity(i);
//		overridePendingTransition(R.anim.zoout, R.anim.zoin);
		this.finish();
	}
	private void startDownLoadService(){
		Intent i = new Intent(mContext,DownLoadService.class);
		startService(i);
	}
	/**
	 * 
	 * @描述:获取rom升级信息
	 * @方法名: GetRomUpgradeInfo
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-10下午5:21:54	
	 * @修改人 Administrator
	 * @修改时间 2014-9-10下午5:21:54	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	private void GetRomUpgradeInfo(){
		final RomUpgradeImpl upgradeImpl = new RomUpgradeImpl(mContext);
		upgradeImpl.SetRomUpgradeImplListener(new RomUpgradeImpl.IRomUpgradeImpl(){
			@Override
			public void onGetRomUpgradeFinished(UpgradeBean upgradetbean) {
				// TODO Auto-generated method stub
				boolean needupgrade = false;
				try {
					if(upgradetbean!=null){
						if(upgradetbean.getResult()!=null){
							System.out.println("result = " + upgradetbean.getResult().getRet() + upgradetbean.getData().getDownload_link());
							if(upgradetbean.getResult().getRet()==0 && upgradetbean.getData().getDownload_link()!=null && upgradetbean.getData().getDownload_link().length()>0){	
								startDownLoadService();
								boolean isNewVersion = true;
								String serverVersionContent = upgradetbean.getData().getDesc();
								String serverVersion = upgradetbean.getData().getRom_version();
								Intent i = new Intent();
								i.setClass(mContext, SystemUpdateSettings.class);
								i.putExtra("isNewVersion", isNewVersion);
								i.putExtra("serverVersion", serverVersion);
								i.putExtra("serverVersionContent", serverVersionContent);
								startActivity(i);
								needupgrade = true;
							}
						
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
				if(needupgrade == false) {
					handler.sendEmptyMessageDelayed(finishActivity, 5000);
				
				}
			}
			
		});

		
		String tvid = DeviceUtils.getTvID(this);
		final String wifimac = DeviceUtils.getWifiMac(this);
		final String devmd5 = DeviceUtils.getFirmwareMd5();
		String gitvsn = DeviceUtils.getGITVSN();
		String rom_version = DeviceUtils.getVersionInfo();
		
		final AuthImpl authImpl = new AuthImpl(mContext,new IAuthImpl() {
			
			@Override
			public void onTvsKeyFinished(TvsKeyBean tvskeybean) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGuidFinished(GuidBean guidbean) {
				// TODO Auto-generated method stub
	
			}
		});
		
		GuidInfoBean guid = authImpl.GetGudiInfo(ConfigManager.GUID_INII_PATH);
		String strguid;
		if (guid!=null && guid.getGuid().length()>0) {
			strguid = guid.getGuid();
		} else {
			strguid = ConfigManager.DEFAULT_GUID;
		}
		String rmac = DeviceUtils.getEthernetMac();
		upgradeImpl.GetRomUpgradeInfo(ServerManager.ROM_UPGRADE_SERVER, "2",  ConfigManager.TV_TYPE, rom_version, strguid, wifimac);
		//upgradeImpl.GetUpgradeInfo(ServerManager.ROM_UPGRADE_SERVER, rom_version,strguid,rmac);

	}
	
}
