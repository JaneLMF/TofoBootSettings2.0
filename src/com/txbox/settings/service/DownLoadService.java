package com.txbox.settings.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import com.txbox.settings.bean.FirmwareMd5Bean;
import com.txbox.settings.bean.GuidBean;
import com.txbox.settings.bean.GuidDataBean;
import com.txbox.settings.bean.GuidInfoBean;
import com.txbox.settings.bean.ResultBean;
import com.txbox.settings.bean.TvsKeyBean;
import com.txbox.settings.bean.UpgradeBean;
import com.txbox.settings.dialog.DialogCollection;
import com.txbox.settings.dialog.DialogCollection.IButtonClick;
import com.txbox.settings.impl.AuthImpl;
import com.txbox.settings.impl.RomUpgradeImpl;
import com.txbox.settings.impl.RomUpgradeImpl.IGetMd5Impl;
import com.txbox.settings.impl.RomUpgradeImpl.IRomUpgradeImpl;
import com.txbox.settings.interfaces.IAuthImpl;
import com.txbox.settings.launcher.systemsettings.SystemUpdateSettings;
import com.txbox.settings.report.EventId;
import com.txbox.settings.report.GlobalInfo;
import com.txbox.settings.utils.ConfigManager;
import com.txbox.settings.utils.Contacts;
import com.txbox.settings.utils.DeviceUtils;
import com.txbox.settings.utils.MD5;
import com.txbox.settings.utils.Recovery;
import com.txbox.settings.utils.ServerManager;
import com.txbox.txsdk.R;
import android.app.ActivityManager;
import android.content.ComponentName;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.UserHandle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class DownLoadService extends Service{

	private static final int DOWNLOAD_FAILED = 10;
	private static final int DOUPGRADE = 11;
	private Context mContext;
	private String ServerMD5;
	private String ServerDownLoadUrl;
	private DialogCollection mDialogCollection;
	private Dialog d = null;
	private int forceUpdate = 0;
	AsyncTask<String, Integer, File> mDownLoad = null;
	private TimerTask updatechecktask;
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DOWNLOAD_FAILED: //连接超时
				//SystemUpdateSettings.isUpdating = false;
				if(mDownLoad == null){
					mDownLoad = new DownLoadFirmware();
					if(mDownLoad.getStatus()==AsyncTask.Status.PENDING){
						mDownLoad.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ServerDownLoadUrl);
					}
				}
				break;
			case DOUPGRADE:
				doUpgrade();
				break;
			default:
				break;
			}
		};
	};
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.d("DownloadService", "onCreate");
		CheckUpgrade(30*60);	
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d("DownloadService", "onStartCommand");
		mContext = getApplicationContext();
		mDialogCollection = new DialogCollection(mContext);
		GetRomUpgradeInfo();

		return super.onStartCommand(intent, flags, startId);
	}

	private class DownLoadFirmware extends AsyncTask<String, Integer, File>{

		
		private int Total = 1;
		@Override
		protected File doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				SystemUpdateSettings.isUpdating = true;
				String firmwareUrl = arg0[0];
				long startPosition = 0;
				File file = new File(Contacts.DOWNLOAD_FILE_PATH_CACHE, Contacts.DEFAULT_DOWNLOAD_FILENAME);
				RandomAccessFile rFile = null;
				URL url = new URL(firmwareUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5000);
				conn.setReadTimeout(5000); 
				conn.setRequestMethod("GET");  
				conn.setRequestProperty("Accept-Encoding", "identity");
				if(file.exists()){
					FileInputStream fis = new FileInputStream(file);
					startPosition = fis.available();
					Log.d("DownloadService", "file is exits startPosition = " + startPosition);
					//文件下载位置   规定的格式 “byte=xxxx-”  
					String start = "bytes="+startPosition + "-";  
					//设置文件开始的下载位置  使用 Range字段设置断点续传
					conn.setRequestProperty("Range", start);  
					rFile=new RandomAccessFile(file,"rw"); 
					rFile.seek(startPosition);
				}else{
					rFile=new RandomAccessFile(file,"rw");
					rFile.seek(0);
				}
			
				int length = conn.getContentLength();// 主要这里耗时间
				Log.e("DownloadService", "getContentLength length = " + length);
				if(length <=0){
					Log.e("DownloadService", "getContentLength error length = " + length);
					if(length == 0) {
						deleteUpdateFile();
					}
					return null;
				}
				
				Total = (length+(int)startPosition) / 100;
				InputStream in = conn.getInputStream();

				//FileOutputStream fos = new FileOutputStream(rFile);
				byte[] buffer = new byte[1024];
				int len = 0;
				int completed = (int) startPosition;
				while ((len = in.read(buffer)) > 0 && getStatus() == AsyncTask.Status.RUNNING) {
					completed += len;
					rFile.write(buffer, 0, len);
					onProgressUpdate(completed / Total);
				}
				in.close();
				rFile.close();
				return file;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//cancel(true);
				mDownLoad = null;
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(File result) {
			// TODO Auto-generated method stub
			
			//下载完成
			if (getStatus() == AsyncTask.Status.RUNNING && result != null) {
				String params = "eventid=" + EventId.upgrade.UPGRADE_ACTION_DOWNLOADED+"&pr=" + ConfigManager.DEFAULT_PR + "&curVsnName="+ DeviceUtils.getVersionInfo();
				GlobalInfo.reportMta(mContext,params);
				if(isLocalExist()){
					showUpdateDialog();
				}else{
					Toast.makeText(mContext, "md5错误或文件不存在", 0).show();
					//result.deleteOnExit();
					deleteUpdateFile();
					mHandler.sendEmptyMessageDelayed(DOWNLOAD_FAILED, 5 * 1000);
					mDownLoad = null;
				}
			}else {
				mDownLoad = null;
				cancel(true);
				mHandler.sendEmptyMessageDelayed(DOWNLOAD_FAILED, 5 * 1000);
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			int newProgress = values[0];
			if(newProgress <= 100){
				
				sendBroadCast(newProgress);
			}else if(newProgress > 100){
				//下载完毕
			}
			
			super.onProgressUpdate(values);
		}
		
	}
	private void GetRomUpgradeInfo1(){
		String ver = DeviceUtils.getFirmwareVersion();
		RomUpgradeImpl upgradeImpl = new RomUpgradeImpl(mContext);
		upgradeImpl.SetRomUpgradeImplListener(new IRomUpgradeImpl(){

			@Override
			public void onGetRomUpgradeFinished(UpgradeBean upgradetbean) {
				// TODO Auto-generated method stub
				try{
					if(upgradetbean!=null){
						ResultBean resultBean = upgradetbean.getResult();
						if(resultBean!=null){
							if(upgradetbean.getResult().getRet()==0 && upgradetbean.getData().getDownload_link()!=null && upgradetbean.getData().getDownload_link().length()>0){
								ServerMD5 = upgradetbean.getData().getMd5();
								ServerDownLoadUrl = upgradetbean.getData().getDownload_link();
								if(isLocalExist()){ //已存在，直接提示升级
									sendBroadCast(100);
									showUpdateDialog();
								}else{//需静默安装
									if(mDownLoad == null){
										String params = "eventid=" + EventId.upgrade.UPGRADE_ACTION_NEED_UPGRADE+"&pr=" + ConfigManager.DEFAULT_PR + "&curVsnName="+ DeviceUtils.getVersionInfo();
										GlobalInfo.reportMta(mContext,params);
										mDownLoad = new DownLoadFirmware();
										if(mDownLoad.getStatus()==AsyncTask.Status.PENDING){
											mDownLoad.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ServerDownLoadUrl);
										}
									}
								}
							}else{
								if(isUpgradeFileExist()){ //已存在升级文件，则删除
									deleteUpdateFile();
								}
							}
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
		});
		//upgradeImpl.GetUpgradeInfo("http://www.go3c.tv:8060/go3cauth/api.php","firmware",ver);
	}
	
	private void GetRomUpgradeInfo(){
		final RomUpgradeImpl upgradeImpl = new RomUpgradeImpl(mContext);
		upgradeImpl.SetRomUpgradeImplListener(new IRomUpgradeImpl(){

			@Override
			public void onGetRomUpgradeFinished(UpgradeBean upgradetbean) {
				// TODO Auto-generated method stub
				try {
					if(upgradetbean!=null){
						System.out.println("result = " + upgradetbean.getResult().getRet() + upgradetbean.getData().getDownload_link());
						if(upgradetbean.getResult().getRet()==0 && upgradetbean.getData().getDownload_link()!=null && upgradetbean.getData().getDownload_link().length()>0){
							ServerMD5 = upgradetbean.getData().getMd5();
							ServerDownLoadUrl = upgradetbean.getData().getDownload_link();
							forceUpdate = upgradetbean.getData().getForce();
							if(isLocalExist()){ //已存在，直接提示升级
								sendBroadCast(100);
								showUpdateDialog();
							}else{//需静默安装
								if(mDownLoad == null){
									String params = "eventid=" + EventId.upgrade.UPGRADE_ACTION_NEED_UPGRADE+"&pr=" + ConfigManager.DEFAULT_PR + "&curVsnName="+ DeviceUtils.getVersionInfo();
									GlobalInfo.reportMta(mContext,params);
									
									mDownLoad = new DownLoadFirmware();
									if(mDownLoad.getStatus()==AsyncTask.Status.PENDING){
										mDownLoad.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ServerDownLoadUrl);
									}
								}
							}
						}else{
							//GetRomUpgradeInfo1();
							if(isUpgradeFileExist()){ //已存在升级文件，则删除
								deleteUpdateFile();
							}
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
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
		forceUpdate = 0;
		String rmac = DeviceUtils.getEthernetMac();
		upgradeImpl.GetRomUpgradeInfo(ServerManager.ROM_UPGRADE_SERVER, "2",  ConfigManager.TV_TYPE, rom_version, strguid, wifimac);
		//upgradeImpl.GetUpgradeInfo(ServerManager.ROM_UPGRADE_SERVER, rom_version,strguid,rmac);
				
	}

	private boolean isUpgradeFileExist(){
		File f =new File(Contacts.DOWNLOAD_FILE_PATH_CACHE + Contacts.DEFAULT_DOWNLOAD_FILENAME);
		if(f.exists()){
			return true;
		}
		return false;
	}

	private boolean isLocalExist(){
		File f =new File(Contacts.DOWNLOAD_FILE_PATH_CACHE + Contacts.DEFAULT_DOWNLOAD_FILENAME);
		if(f.exists()){
			MD5 mMD5 = new MD5();	
			String localMD5 = mMD5.md5sum(Contacts.DOWNLOAD_FILE_PATH_CACHE + Contacts.DEFAULT_DOWNLOAD_FILENAME);
			if(localMD5!=null && localMD5.equals(ServerMD5)){//最新版本已下载，返回true，提示升级
				return true;
			}
		}
		return false;
	}
	private void deleteUpdateFile() {

		File f =new File(Contacts.DOWNLOAD_FILE_PATH_CACHE + Contacts.DEFAULT_DOWNLOAD_FILENAME);
		if(f.exists()){
			f.delete();
		}
	}
	
	private void showUpdateDialog(){
		ActivityManager am = (ActivityManager) mContext.getSystemService(mContext.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String packageName = cn.getPackageName();
		if(packageName.equals("com.txbox.txsdk")) {
			Intent i = new Intent();
			i.setAction("com.txbox.downloadFinished");
			i.putExtra("progress", 100);
			mContext.sendBroadcast(i);
        }else if(forceUpdate !=0){
        	mHandler.sendEmptyMessageDelayed(DOUPGRADE, 10*1000);
        	d = new AlertDialog.Builder(mContext).setTitle(mContext.getString(R.string.update_dialog_title)).
        	setMessage( mContext.getString(R.string.update_dialog_forceupdate)).
            setNegativeButton( mContext.getString(R.string.update_dialog_update_text), new DialogInterface.OnClickListener() {
            	@Override
            	public void onClick(DialogInterface arg0, int arg1) {
            		// TODO Auto-generated method stub
            		String params = "eventid=" + EventId.upgrade.UPGRADE_ACTION_START_TRIGGER_UPGRADE+"&pr=" + ConfigManager.DEFAULT_PR + "&curVsnName="+ DeviceUtils.getVersionInfo();
            		GlobalInfo.reportMta(mContext,params);
            		doUpgrade();
            	}
            }).create();
        	d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        	d.show();
        }else {
        	String params = "eventid=" + EventId.upgrade.UPGRADE_ACTION_SHOW_UPGRADE_TIP+"&pr=" + ConfigManager.DEFAULT_PR + "&curVsnName="+ DeviceUtils.getVersionInfo();
        	GlobalInfo.reportMta(mContext,params);
        	d = new AlertDialog.Builder(mContext).setTitle(mContext.getString(R.string.update_dialog_title)).
            setMessage( mContext.getString(R.string.update_dialog_content)).setPositiveButton
            (mContext.getString(R.string.update_dialog_noupdate_text),new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
			if(updatechecktask!=null) {
				updatechecktask.cancel();
                    		updatechecktask = null;
			}
                    }
            }
            ).setNegativeButton( mContext.getString(R.string.update_dialog_update_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub
                            String params = "eventid=" + EventId.upgrade.UPGRADE_ACTION_START_TRIGGER_UPGRADE+"&pr=" + ConfigManager.DEFAULT_PR + "&curVsnName="+ DeviceUtils.getVersionInfo();
                            GlobalInfo.reportMta(mContext,params);
                            doUpgrade();
                    }
            }).create();
        	d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        	d.show();
        }
	}
	
	@SuppressLint("NewApi") 
	private void sendBroadCast(int progress){
		Intent i = new Intent();
		i.setAction("com.txbox.updateProgress");
		i.putExtra("progress", progress);
		mContext.sendBroadcastAsUser(i, UserHandle.CURRENT);
	}
	private void doUpgrade(){
    	Recovery.saveCommand(mContext, Contacts.DOWNLOAD_FILE_PATH_CACHE + Contacts.DEFAULT_DOWNLOAD_FILENAME);
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        pm.reboot("recovery");
	}
	private void CheckUpgrade(final int second){
		if(updatechecktask == null){
			updatechecktask = new TimerTask() {
				public void run() {
				 	Log.d("DownloadService", "CheckUpgrade");	
					GetRomUpgradeInfo();
				}
			};
			Timer timer = new Timer();
			timer.schedule(updatechecktask, 60*1000, second* 1000);
		}
	}
}
