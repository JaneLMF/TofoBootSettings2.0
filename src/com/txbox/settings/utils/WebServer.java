package com.txbox.settings.utils;

import java.io.File;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.go3c.lighttpd.HttpService;
import com.go3c.lighttpd.IHttpService;

/**
 * 
 * @类描述：HTTP服务管理类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.utils
 * @类名称：WebServer	
 * @创建人：Administrator
 * @创建时间：2014-9-16上午11:57:10	
 * @修改人：Administrator
 * @修改时间：2014-9-16上午11:57:10	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class WebServer {
	private IHttpService httpService = null;
	private ServiceConnection serviceConnection = null;
	
	public void Start(final Context context){
		initWebConfig(context);
		serviceConnection = new ServiceConnection() {
			@Override
			public void onServiceDisconnected(ComponentName name) {
				
			}
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				// httpService = ((HttpService.MyBinder) service).getService();
				httpService = IHttpService.Stub.asInterface(service);
				System.out.println("Lighttpd service Connected !");

				String path = "";
				path = context.getApplicationContext().getFilesDir().getAbsolutePath();

				try {
					httpService.setConfigPath(path + "/config/lighttpd.conf");
					httpService.setEnvValue("LD_LIBRARY_PATH", path+"/www/txbox");
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					httpService.startServer();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		
		Intent serviceIntent = new Intent(context, HttpService.class);
		context.startService(serviceIntent);
		//context.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
	}
	public void Stop(){
		try {
			httpService.stopServer();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void SetWebConfig(){
		
	}
	/**
	 * 
	 * @描述: 从assets目录复制配置文件到应用程序目录下
	 * @方法名: initWebConfig
	 * @param context
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-17下午12:25:51	
	 * @修改人 Administrator
	 * @修改时间 2014-9-17下午12:25:51	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	private void initWebConfig(Context context){
		String path = context.getApplicationContext().getFilesDir().getAbsolutePath();
		AssetsUtils assetsUtils = new AssetsUtils(context);

		assetsUtils.openAssets("config", "", path + "/config", true);
		
		assetsUtils.openAssets("www", "html", path + "/www", false);
		assetsUtils.openAssets("www", "log", path + "/www", false);
		assetsUtils.openAssets("www", "txbox", path + "/www", true);
		assetsUtils.chmod("txbox", path + "/www", "775");

		assetsUtils.chmod("log", path + "/www", "775");

		assetsUtils.chmod("", path + "/www", "775");
		
		String appspath = ConfigManager.INSTALLED_APPS_PATH;
		String gamepath = ConfigManager.INSTALLED_GAME_PATH;
		String installingpath = ConfigManager.APP_INSTALLING_PATH;
		String waitingpath = ConfigManager.APP_INSTALLING_PATH;
		String iconpath = ConfigManager.APP_ICON_PATH;
		String allpath = ConfigManager.ALL_APP_PATH;
		
		String strpath = path +"/www/installedapps.js";

		Utils.runCommand("rm -rf " + strpath);
	
		String cmd = "ln -s " + appspath + " " + strpath;
		Utils.runCommand(cmd);
		
		strpath = path +"/www/installedgame.js";
		Utils.runCommand("rm -rf " + strpath);
		cmd = "ln -s " + gamepath + " " + strpath;
		Utils.runCommand(cmd);
		
		strpath = path +"/www/packageinstall.js";
		Utils.runCommand("rm -rf " + strpath);
		cmd = "ln -s " + installingpath + " " + strpath;
		Utils.runCommand(cmd);
		
		strpath = path +"/www/packagewaiting.js";
		Utils.runCommand("rm -rf " + strpath);
		cmd = "ln -s " + waitingpath + " " + strpath;
		Utils.runCommand(cmd);
		
		strpath = path +"/www/img";
		Utils.runCommand("rm -rf " + strpath);
		cmd = "ln -s " + iconpath + " " + strpath;
		Utils.runCommand(cmd);
		
		strpath = path +"/www/installedall.js";
		Utils.runCommand("rm -rf " + strpath);
		cmd = "ln -s " + allpath + " " + strpath;
		Utils.runCommand(cmd);
		
	}
}
