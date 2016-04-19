package com.txbox.settings.launcher.systemsettings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.txbox.settings.common.TXbootApp;
import com.txbox.settings.report.EventId;
import com.txbox.settings.report.GlobalInfo;
import com.txbox.settings.service.DownLoadService;
import com.txbox.settings.utils.ConfigManager;
import com.txbox.settings.utils.DecodeImgUtil;
import com.txbox.settings.utils.DeviceUtils;
import com.txbox.txsdk.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.KeyEvent;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.txbox.settings.utils.Contacts;
import com.txbox.settings.utils.Recovery;
import android.os.PowerManager;
import android.content.pm.PackageManager;
import android.content.ComponentName;
import com.txbox.settings.launcher.bootsettings.BootActivity;

public class SystemUpdateSettings extends Activity {
	
	
	 private LinearLayout before_update_ll,system_update_updating,system_update_updated;
	 private TextView before_update_newVersion_text,before_update_newVersion_content;
	 private Button versionInforBtn,updateNowBtn,updateCancel;
	 private ProgressBar updatingProgressBar;
	 private Context mContext;
	 private static final int SURETOUPDATE = 0;
	 private static final int CANCELUPDATE = 1;
	 private static final int UPDATEPROGRESS = 2;
	private float progress = 0;
	private LinearLayout updateButton_ll;
	private boolean isNewVersion = false;
	 private Bitmap b;
	 private RelativeLayout main_rl;
	 private TXbootApp app;
	 public static boolean isUpdating = false;
	 private BroadcastReceiver mReceiver = null;
	 private String serverVersion;
	 private String serverVersionContent;
	 Handler h = new Handler(){
		 public void handleMessage(android.os.Message msg) {
			 
			 switch (msg.what) {
			case SURETOUPDATE: //sure update
				updateButton_ll.setVisibility(View.GONE);
				system_update_updating.setVisibility(View.VISIBLE);
//				h.sendEmptyMessageDelayed(UPDATEPROGRESS, 1000);// update the progress by 3 seconds;
				startDownLoadService();
				break;
			case CANCELUPDATE: //cancel update
				
				break;
			case UPDATEPROGRESS://update progress 
				int i = (Integer) msg.obj;
				updatingProgressBar.setProgress(i);
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
			setContentView(R.layout.system_update_settings);
			mContext = this;
			main_rl = (RelativeLayout) findViewById(R.id.main_rl);
			b = new DecodeImgUtil(this, R.raw.settings_bg).getBitmap();
			BitmapDrawable bd= new BitmapDrawable(b);
			main_rl.setBackground(bd);
			initView();
			isNewVersion = getIntent().getBooleanExtra("isNewVersion",false);
			serverVersion = getIntent().getStringExtra("serverVersion");
			serverVersionContent = getIntent().getStringExtra("serverVersionContent");
			System.err.println("update-isNewVersion" + isNewVersion);
			app = (TXbootApp) mContext.getApplicationContext();
			initParam();
			register();
			initOnClick();
			initOnFocus();
			updateNowBtn.requestFocus();
		}
	
		
		private void initView(){
			before_update_ll = (LinearLayout) findViewById(R.id.before_update_ll);
			system_update_updating = (LinearLayout) findViewById(R.id.system_update_updating);
			system_update_updated = (LinearLayout) findViewById(R.id.system_update_updated);
			before_update_newVersion_text = (TextView) findViewById(R.id.before_update_newVersion_text);
			before_update_newVersion_content = (TextView) findViewById(R.id.before_update_newVersion_content);
			versionInforBtn = (Button) findViewById(R.id.versionInforBtn);
			updateNowBtn = (Button) findViewById(R.id.updateNowBtn);
			updatingProgressBar = (ProgressBar) findViewById(R.id.updatingProgressBar);
			updateButton_ll = (LinearLayout) findViewById(R.id.updateButton_ll);
			updateCancel = (Button) findViewById(R.id.updateCancel);
		}
		private void initParam(){
			// TODO: init the 版本号"newVersion_text",版本更新内容"newVersion_content"
			if(isNewVersion){
				if(serverVersion!=null){
					before_update_newVersion_text.setText(mContext.getString(R.string.system_update_newVersion)+serverVersion);
				}
				updateButton_ll.setVisibility(View.VISIBLE);
				if(serverVersionContent!=null){
					before_update_newVersion_content.setText(serverVersionContent);
				}
			}else{
				before_update_newVersion_text.setText(mContext.getString(R.string.system_update_oldVersion)+DeviceUtils.getVersionInfo());
				updateButton_ll.setVisibility(View.GONE);
				String content = getCurrentVersionInfo();
				if(content != null){
					before_update_newVersion_content.setText(content);
				}
			}
			
			if(isNewVersion){
				updateButton_ll.setVisibility(View.GONE);
				system_update_updating.setVisibility(View.VISIBLE);
			}
			
		}
		private void initOnClick(){
			updateNowBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO 
					String params = "eventid=" + EventId.systemsetting.SYSTEMSETTING_ACTION_UPGRADE_CLICK+"&pr=" + ConfigManager.DEFAULT_PR;
					GlobalInfo.reportMta(mContext,params);
					//app.setDownLoadHandler(h);
					h.removeMessages(SURETOUPDATE);
					h.sendEmptyMessage(SURETOUPDATE);
					
				}
			});
			updateCancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			versionInforBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO 显示新版本的特性
					
				}
			});
		}
		
		private void initOnFocus(){
			updateNowBtn.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View arg0, boolean hasFocus) {
					// TODO Auto-generated method stub
					if(hasFocus){
						updateNowBtn.setBackgroundResource(R.drawable.btn_focus_bg);
						updateNowBtn.setTextColor(Color.parseColor("#FFFFFF"));
					}else{
						updateNowBtn.setBackgroundResource(0);
						updateNowBtn.setTextColor(Color.parseColor("#4c5c6a"));
					}
				}
			});
			updateCancel.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View arg0, boolean hasFocus) {
					// TODO Auto-generated method stub
					if(hasFocus){
						updateCancel.setBackgroundResource(R.drawable.btn_focus_bg);
						updateCancel.setTextColor(Color.parseColor("#FFFFFF"));
					}else{
						updateCancel.setBackgroundResource(0);
						updateCancel.setTextColor(Color.parseColor("#4c5c6a"));
					}
				}
			});
		}
		
		private void startDownLoadService(){
			Intent i = new Intent(mContext,DownLoadService.class);
			startService(i);
		}
	private void register(){
		if(mReceiver == null){
			mReceiver = new BroadcastReceiver(){
				@Override
				public void onReceive(Context arg0, Intent intent) {
					// TODO Auto-generated method stub
					String action = intent.getAction();
					if(action.equals("com.txbox.updateProgress")){
						int newProgress = intent.getIntExtra("progress", 0);
						Message msg = new Message();
						msg.what= UPDATEPROGRESS ;
						msg.obj = newProgress;
						h.sendMessage(msg);
					}else if(action.equals("com.txbox.downloadFinished")) {
						DisableBootSettings();
						Recovery.saveCommand(mContext, Contacts.DOWNLOAD_FILE_PATH_CACHE + Contacts.DEFAULT_DOWNLOAD_FILENAME);
						PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
						pm.reboot("recovery");
					}
				}
				
			};
		}
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction("com.txbox.updateProgress");
		iFilter.addAction("com.txbox.downloadFinished");
		mContext.registerReceiver(mReceiver, iFilter);
	}	
	
	private void unregiest(){
		if(mReceiver!=null){
			unregisterReceiver(mReceiver);
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregiest();
		super.onDestroy();
	}
	
	private String getCurrentVersionInfo(){
		File f = new File("/system/releaseNotes.txt");
		StringBuffer line = null;
		InputStreamReader isr = null;
		try {
			FileInputStream fis = new FileInputStream(f);
			isr=new InputStreamReader(fis,"GBK");
			BufferedReader br = new BufferedReader(isr);
			
			String str=null;
			line = new StringBuffer();
			while((str = br.readLine())!=null){
				line.append(str);
				line.append("\n");
			}
			br.close();
			isr.close();
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(line!=null){
			return line.toString();
		}
		return null;
	}
	private void initOnkey(){
		before_update_newVersion_content.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if(arg1 == KeyEvent.KEYCODE_DPAD_UP&&arg2.getAction()==KeyEvent.ACTION_DOWN){
				}else if(arg1 == KeyEvent.KEYCODE_DPAD_DOWN&&arg2.getAction()==KeyEvent.ACTION_DOWN){
					
				}
				return false;
			}
		});
	}
	@Override
	public boolean onKeyDown(int arg0, KeyEvent arg1) {
		// TODO Auto-generated method stub
		if(arg0 == KeyEvent.KEYCODE_BACK&& arg1.getAction() == KeyEvent.ACTION_DOWN){
			return true;
		}
		return super.onKeyDown(arg0, arg1);
	}
	private void DisableBootSettings(){
		/*设置完成，禁止下次开机时自动启动*/
	
		PackageManager pm = getPackageManager();  
		ComponentName name = new ComponentName(SystemUpdateSettings.this, BootActivity.class);
		pm.setComponentEnabledSetting(name, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);        
        
	}
}
