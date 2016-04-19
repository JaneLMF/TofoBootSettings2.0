package com.txbox.settings.service;

import java.io.File;

import com.txbox.settings.bean.ResultBean;
import com.txbox.settings.bean.UpgradeBean;
import com.txbox.settings.dialog.DialogCollection;
import com.txbox.settings.dialog.DialogCollection.IButtonClick;
import com.txbox.settings.impl.RomUpgradeImpl;
import com.txbox.settings.impl.ScreensaverImpl;
import com.txbox.settings.interfaces.IRomUpgradeImpl;
import com.txbox.settings.interfaces.IScreensaverImpl;
import com.txbox.settings.utils.ConfigManager;
import com.txbox.settings.utils.DeviceUtils;
import com.txbox.settings.utils.PathUtils;
import com.txbox.settings.utils.XmlConfTools;
import com.txbox.txsdk.R;

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
import android.view.WindowManager;
import android.widget.Toast;

public class UpdateService extends Service{

	
	private String ServerMD5;
	private String ServerDownLoadUrl;
	private static final int GETSOURCE_TIMEOUT = 10;
	private Context mContext;
	private DialogCollection mdDialogCollection;
	private Dialog dialog;
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GETSOURCE_TIMEOUT: //连接超时
				
				break;
			default:
				break;
			}
		};
	};
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		mContext = getApplicationContext();
		
		BootSettingsUpdate();
		//SaveScreensaverPhotoInternet();
		return super.onStartCommand(intent, flags, startId);
	}
	private void BootSettingsUpdate(){
		int ver = DeviceUtils.GetAppVersion(mContext);

	
		//upgradeImpl.GetUpgradeInfo("http://www.go3c.tv:8060/go3cauth/api.php","apk",String.valueOf(ver));
	}
	
	
	private void showUpdateDialog(final String url){
		if(mdDialogCollection==null){
			mdDialogCollection = new DialogCollection(mContext);
		}
		String title = getResources().getString(R.string.midware_update_tiele);
		String update_now = getResources().getString(R.string.is_update_now);
		String update_confirm = getResources().getString(R.string.update_confirm);
		String update_cancel = getResources().getString(R.string.update_cancel);
		
//		mdDialogCollection.showWarningDialog(title, update_now, update_confirm,update_cancel);
//		mdDialogCollection.setOnButtonClick(new IButtonClick() {
//			@Override
//			public void OnOkClick() {
//				// TODO Auto-generated method stub
//				 Intent intent = new Intent();
//			     intent.setAction("boot.broadcast");
//			     String midware = getResources().getString(R.string.midware);
//			     String uri = "file://title="+midware+"&file_path="+url + "&packagename=com.txbox.txsdk"+"&installtype=0&apptype=app&isupdate=1";
//			     intent.setDataAndType(Uri.parse(uri), "text/html");   
//			     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			     mContext.startActivity(intent);
//			}
//			
//			@Override
//			public void OnCancelClick() {
//				// TODO Auto-generated method stub
//			}
//		});
		
		dialog = new AlertDialog.Builder(mContext).setTitle(title).
				setMessage(update_now).setPositiveButton
				(update_cancel,new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
					}
				}
				).setNegativeButton(update_confirm, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								 Intent intent = new Intent();
							     intent.setAction("boot.broadcast");
							     String midware = getResources().getString(R.string.midware);
							     String uri = "file://title="+midware+"&file_path="+url + "&packagename=com.txbox.txsdk"+"&installtype=0&apptype=app&isupdate=1";
							     intent.setDataAndType(Uri.parse(uri), "text/html");   
							     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							     mContext.startActivity(intent);
							}
						}).create();
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
	}
	/**
	 * 检测服务器上的图片资源
	 */
	private void SaveScreensaverPhotoInternet(){
		ScreensaverImpl mScreensaverImpl = new ScreensaverImpl(mContext, new IScreensaverImpl() {
			
			@Override
			public void OnSaveResoult(boolean isSuccess) {
				// TODO Auto-generated method stub
				if(isSuccess){
					setParams(ConfigManager.SCREENSAVER_PATH,"screensaver_path_extra",PathUtils.SCREENSAVERINTERNETDIR);
					File f = new File(PathUtils.SCREENSAVERINTERNETDIR+"/"+"defaultZIP.zip");
					if(f.exists()){
						f.delete();
					}
				}
			}
		});
		mScreensaverImpl.getScreensaver("http://www.go3c.tv:8060/download/tencent/screensaver.zip");
	}
	
	private void setParams(String path,String name,String value){
		try {
			XmlConfTools.setValue(path, name, value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
