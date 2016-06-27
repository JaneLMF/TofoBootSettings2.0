package com.txbox.settings.launcher.bootsettings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.pivos.cqdvbserver.CqDvbService;
import com.pivos.utils.WebServer;
import com.txbox.settings.bean.CommonReturn;
import com.txbox.settings.bean.CrashBean;
import com.txbox.settings.bean.GameListBean;
import com.txbox.settings.bean.GuidBean;
import com.txbox.settings.bean.GuidDataBean;
import com.txbox.settings.bean.GuidInfoBean;
import com.txbox.settings.bean.LoginBean;
import com.txbox.settings.bean.ResultBean;
import com.txbox.settings.bean.TvsKeyBean;
import com.txbox.settings.bean.UserBean;
import com.txbox.settings.common.TXbootApp;
import com.txbox.settings.db.AppDao;
import com.txbox.settings.impl.AuthImpl;
import com.txbox.settings.impl.ReportImpl;
import com.txbox.settings.impl.SyscfgImpl;
import com.txbox.settings.impl.SyscfgImpl.GetGameListInterfaceListener;
import com.txbox.settings.impl.UserImpl;
import com.txbox.settings.impl.UserImpl.IVipInfoImpl;
import com.txbox.settings.interfaces.IAuthImpl;
import com.txbox.settings.interfaces.IReportImpl;
import com.txbox.settings.service.DownLoadService;
import com.txbox.settings.service.UpdateService;
import com.txbox.settings.service.NtpService;
import com.txbox.settings.utils.ConfigManager;
import com.txbox.settings.utils.DeviceUtils;
import com.txbox.settings.utils.FileUtils;
import com.txbox.settings.utils.PathUtils;
import com.txbox.settings.utils.ServerManager;
import com.txbox.settings.utils.Utils;
import com.txbox.settings.utils.XmlConfTools;
import com.txbox.txsdk.R;
import com.txbox.settings.common.SystemInfoManager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.SntpClient;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import tv.icntv.ottlogin.loginSDK;
import com.txbox.settings.utils.AssetsUtils;
import com.txbox.settings.bean.PivosAuthInfo;
import com.txbox.settings.common.NetworkManager;
import android.os.Handler;
import com.baustem.service.dvbnavigation.IDVBNavigation;
import com.baustem.service.epgnavigation.IEPGNavigation;
import com.baustem.service.vodnavigation.IVODNavigation;
import com.baustem.service.vodstream.IVODStream;

public class BootReceiver extends BroadcastReceiver {

	private static final String TAG = "BootReceiver";
	
	Context mContext = null;
	TXbootApp app=null;
	TimerTask task = null;
	
    private static IDVBNavigation m_idvbService = null;
    private static IEPGNavigation m_iepgService = null;
    private static IVODNavigation m_ivodService = null;
    private static IVODStream m_ivodStreamService = null;

    private ServiceConnection m_serviceCon_dvbNavigation = null;
    private ServiceConnection m_serviceCon_epgNavigation = null;
    private ServiceConnection m_serviceCon_vodNavigation = null;
    private ServiceConnection m_serviceCon_vodStreamNavigation = null;
    private static final int STARTGATEWAYSERVICE = 1;	
	Handler h = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case STARTGATEWAYSERVICE:
				startGateWayTask();
				break;
			default:
				break;
			}
			
		};
	};
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "==++++++++++== BootSettings BootReceiver , action : " + intent.getAction());
		mContext = context;
		app = (TXbootApp) mContext.getApplicationContext();
        String action = intent.getAction();
//        Toast.makeText(context, "开机启动完成，检查登录信息是否过期", Toast.LENGTH_SHORT).show();
		if ("android.intent.action.BOOT_COMPLETED".equals(action) || "com.txbox.txsdk.installed".equals(action)) {
			CheckEth();
			/*将guid从/sdcard/Android目录复制到/data/data 目录*/
			Utils.initInstalledData(ConfigManager.GUID_INII_PATH_OLD,ConfigManager.GUID_INII_PATH);		
			String cmd = "chmod 666 " + ConfigManager.GUID_INII_PATH;
			Utils.runCommand(cmd);
			
			app.startWebServer(context);
			app.initPlaySdk(context);
			app.initMTAConfig(true);
			app.startMat(context);
			//app.initGlobalConfig(context);
			SaveScreensaverPhotoLocal();
			
			SavePriorityApk();
			initDefaultInputMethod(context);
			app.startCyberHTTP(context);
			h.sendEmptyMessage(STARTGATEWAYSERVICE);
				
		}else if(action.equals("android.net.conn.CONNECTIVITY_CHANGE")){
			NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO); 
        	ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);   
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            if(activeNetInfo!=null && activeNetInfo.isConnected() /*&& activeNetInfo.getType() != networkInfo.getType()*/){
            		startDownLoadService();
            		CheckAccessTokenValid();
            		initBox(context);
            		//上传crash信息
            		//upLoadCrash();
            		//downLoadGameList();
            		SyncTime();
			//icntvAuth(context);
			PivosInitGuid();
            }
        }else if(action.equals("android.intent.action.START_UPGRADE")) {
        	startDownLoadService();
        }
	}

	private void startGateWayTask() {
		Runnable gatewayRunner = new Runnable() {
                        @Override
                        public void run() {
				startGateWayService();
                        }
                };
                new Thread(gatewayRunner).start();
		
	}
	
	private void startGateWayService() {
        //绑定DVBNavigation
        m_serviceCon_dvbNavigation = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                m_idvbService = IDVBNavigation.Stub.asInterface(service);
                WebServer.setDvbService(m_idvbService);
            }
        };

        boolean result = app.bindService(new Intent("com.baustem.service.dvbnavigation.DVBNavigation"),
                m_serviceCon_dvbNavigation, Context.BIND_AUTO_CREATE);
        if (!result) {
            Log.i("res", "绑定DVBNavigation fail");
            return;
        }
        Log.i("res", "绑定DVBNavigation success");

        //绑定EPGNavigation
        m_serviceCon_epgNavigation = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                m_iepgService = IEPGNavigation.Stub.asInterface(service);
                WebServer.setEpgService(m_iepgService);
            }
        };

        result = app.bindService(new Intent("com.baustem.service.epgnavigation.EPGNavigation"),
                m_serviceCon_epgNavigation, Context.BIND_AUTO_CREATE);
        if (!result) {
            Log.i("res", "绑定EPGNavigation fail");
            return;
        }
        Log.i("res", "绑定EPGNavigation success");

        //绑定VODNavigation
        m_serviceCon_vodNavigation = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                m_ivodService = IVODNavigation.Stub.asInterface(service);
                WebServer.setVodService(m_ivodService);
                Log.i("res", "m_serviceCon_vodNavigation.onServiceConnected");
            }
        };

        result = app.bindService(new Intent("com.baustem.service.vodnavigation.VODNavigation"),
                m_serviceCon_vodNavigation, Context.BIND_AUTO_CREATE);
        if (!result) {
            Log.i("res", "绑定VODNavigation fail");
            return;
        }
        Log.i("res", "绑定VODNavigation success");

        //绑定VODStreamNavigation
        m_serviceCon_vodStreamNavigation = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                m_ivodStreamService = IVODStream.Stub.asInterface(service);
                WebServer.setVodStreamService(m_ivodStreamService);
            }
        };

        result = app.bindService(new Intent("com.baustem.service.vodstream.VODStream"),
                m_serviceCon_vodStreamNavigation, Context.BIND_AUTO_CREATE);
        if (!result) {
            Log.i("res", "绑定VODStreamNavigation fail");
            return;
        }
        Log.i("res", "绑定VODStreamNavigation success");
    }

    public static IDVBNavigation getDvbService() { return m_idvbService; }
    public static IEPGNavigation getEpgService() { return m_iepgService; }
    public static IVODNavigation getVodService() { return m_ivodService; }
    public static IVODStream getVodStreamService() { return m_ivodStreamService; }

	private void CheckEth() {
		NetworkManager networkMgr = new NetworkManager(mContext, h);
		networkMgr.setEthEnable();
	}	
	
	private void startDownLoadService(){
		Intent i = new Intent(mContext,DownLoadService.class);
		mContext.startService(i);
	}
	private void startUpgradeService(){
		Intent i = new Intent(mContext,UpdateService.class);
		mContext.startService(i);
	}
	/**
	 * 
	 * @描述: 检查accesstoken是否过期
	 * @方法名: CheckAccessTokenValid
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-4下午12:26:27	
	 * @修改人 Administrator
	 * @修改时间 2014-9-4下午12:26:27	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	private void CheckAccessTokenValid(){
		UserImpl userimpl = new UserImpl(mContext);
		userimpl.SetGetVipInfoListener(new IVipInfoImpl() {
			@Override
			public void onVipInfoFinished(UserBean userbean) {
				// TODO Auto-generated method stub
				try {
					if(userbean!=null){
						if(userbean.getRet()==100014){//登录信息已过期，清除原来保存登录信息
							clearLoginInfo();
						}
					}
	
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		
		LoginBean logininfo = userimpl.getLoginInfo(ConfigManager.USERINFO_INII_PATH);
		if(logininfo!=null && logininfo.getIslogin()==1){
			userimpl.getVipInfo(ServerManager.OPENAPI_SERVER,logininfo.getAppid(), logininfo.getOpenid(), logininfo.getAccesstoken());
		}
		
		
	}
	/**
	 * 
	 * @描述:清空上次登录信息
	 * @方法名: clearLoginInfo
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-4下午3:28:57	
	 * @修改人 Administrator
	 * @修改时间 2014-9-4下午3:28:57	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	private void clearLoginInfo(){
		UserImpl userimpl = new UserImpl(mContext);
		LoginBean info = userimpl.getLoginInfo(ConfigManager.USERINFO_INII_PATH);
		info.setIslogin(0);
		info.setNick("");
		info.setFace("");
		info.setOpenid("");
		info.setAccesstoken("");
		userimpl.SaveLoginInfo(ConfigManager.USERINFO_INII_PATH,info);
		String cmd = "chmod 666 " + ConfigManager.USERINFO_INII_PATH;
		Utils.runCommand(cmd);
	}
	/**
	 * 保存屏保的默认图片
	 */
    
	private void SaveScreensaverPhotoLocal(){
		int[] photos ={R.raw.pb_img1,R.raw.pb_img2,R.raw.pb_img3,R.raw.pb_img4,R.raw.pb_img5,R.raw.pb_img6,R.raw.pb_img7,R.raw.pb_img8,R.raw.pb_img9,R.raw.pb_img10};
		InputStream is ;
		for (int i = 0; i < photos.length; i++) {
			is = mContext.getResources().openRawResource(photos[i]);
			savePhotos(is, PathUtils.SCREENSAVERLOCALDIR, "pb_img"+(i+1)+".jpg");
		}
		setParams(ConfigManager.SCREENSAVER_PATH, "screensaver_path_default",PathUtils.SCREENSAVERLOCALDIR);
	}
	private void savePhotos(InputStream is,String path,String fileName){
		File f = new File(path);
		File file = new File(path+"/"+fileName);
		
		if(!f.exists()||!f.isDirectory()){
			f.mkdirs();
		}
		if(file.exists()){
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int len;
		byte[] bs = new byte[10240];
		OutputStream os;
		try {
			os = new FileOutputStream(file);
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			os.close();
			is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void setParams(String path,String name,String value){
		try {
			XmlConfTools.setValue(path,name, value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void SavePriorityApk(){
		//优先级
		setParams(ConfigManager.SCREENSAVER_PATH,"screensaverpriority", "1");
		setParams(ConfigManager.XMLCONF_PATH,"allowlocalapk", "1");
	}
	
	
	private void upLoadCrash(){
		
		final AppDao dao = AppDao.getInstance(mContext);

		// 定时执行的任务
		if(task == null){
			 task = new TimerTask() {
				public void run() {
						List<CrashBean> list = dao.getCrashList();
						for (CrashBean crashBean : list) {
							ReportImpl reportImpl = new ReportImpl(mContext, new IReportImpl() {
								@Override
								public void onCommonResultFinished(CommonReturn commonReturn) {
									if (commonReturn != null) {
										if ("0".equals(commonReturn.getCode())) {// 如果上传成功，则把该记录从本地删除
											// 删除记录
											dao.delCrashById(commonReturn.getId());
											task = null;
										}
									}
								}
							});
	 
							reportImpl.addCrash(ServerManager.REPORT_SERVER, "tencent.uuid", crashBean.getTime_added(), crashBean.getId(), crashBean.getDescrption());
						}
					}
				
			};
	
			// 创建一个定时器
			Timer timer = new Timer();
			timer.schedule(task, 0, 60 * 60 * 1000);// 1小时定时更新一次
		}

		System.out.println("-->info：已将crash信息同步至服务器！\n");
	}

	private void initDefaultInputMethod(Context context){
		if(Utils.IsAppInstalled(context,"com.go3c.android.softkeyboard")){
			String InputMethodId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
			String defaultInputMethodId = "com.go3c.android.softkeyboard/.SoftKeyboard";
			if(!defaultInputMethodId.equals(InputMethodId)){
				  Settings.Secure.putString(context.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD, defaultInputMethodId);
			}
		}
	}
	private void downLoadGameList(){
		SyscfgImpl mImpl = new SyscfgImpl(mContext);
		mImpl.setGameListInterfaceListener(new GetGameListInterfaceListener() {
			
			@Override
			public void onGetGameListInterfaceFinished(GameListBean gameList) {
				// TODO Auto-generated method stub
//				System.err.println("gameList = " + gameList);
				if (gameList!=null) {
//					GameBeanItem[] gamelist = gameList.getData().getGame_list();
					try {
						File file = new File(ConfigManager.GAME_PATH, ConfigManager.GAMEFILE);
						if(!file.exists()){
							file.createNewFile();
						}
							Gson gson = new Gson();
							String data = gson.toJson(gameList);
						
							FileUtils.writeFile(file, data);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		mImpl.GetGameListInfo(ServerManager.GAMELIST_SERVER);
	}
	
	/**
	 * 
	 * @描述: 获取guid
	 * @方法名: initBox
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-10上午11:45:35	
	 * @修改人 Administrator
	 * @修改时间 2014-9-10上午11:45:35	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	private void initBox(final Context context){
		AuthImpl authImpl = new AuthImpl(mContext,new IAuthImpl(){

			@Override
			public void onGuidFinished(GuidBean guidbean) {
				// TODO Auto-generated method stub
				try {
					if(guidbean!=null){
						ResultBean result = guidbean.getResult();
						GuidDataBean data = guidbean.getData();
						System.out.println("guid = " + data.getGuid() + "Guid_secret=" + data.getGuid_secret());
						GuidInfoBean info = new GuidInfoBean();
						info.setGuid(data.getGuid());
						info.setGuid_secret(data.getGuid_secret());
						info.setTvskey("");
						SaveGuidInfo(info);
						
//						GetTvsKey(data.getGuid(),data.getGuid_secret());
						TXbootApp app = (TXbootApp) mContext.getApplicationContext();
						app.StartPushService(mContext);
						Intent i = new Intent("com.txbox.weather");
						context.sendBroadcast(i);
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onTvsKeyFinished(TvsKeyBean tvskeybean) {
				// TODO Auto-generated method stub
				
			}
			
		});
		String tvid = DeviceUtils.getTvID(context);
		String wifimac = DeviceUtils.getWifiMac(context);
		String devmd5 = DeviceUtils.getFirmwareMd5();
		String gitvsn = DeviceUtils.getGITVSN();
		String mac_wire = DeviceUtils.getEthernetMac();
		GuidInfoBean guid = authImpl.GetGudiInfo(ConfigManager.GUID_INII_PATH);
		if(guid==null){
			authImpl.GetGuid(ServerManager.AUTH_SERVER, tvid, wifimac, devmd5,gitvsn,mac_wire);
		}else{
			//app.initMultiScreen(context);
        		if(app.isPushServiceStarted()==false){
        			app.StartPushService(context);
        		}
		}

	}
	private void SaveGuidInfo(GuidInfoBean data){
		AuthImpl authImpl = new AuthImpl(mContext,null);
		authImpl.SaveGuidInfo(ConfigManager.GUID_INII_PATH_OLD, data);
		authImpl.SaveGuidInfo(ConfigManager.GUID_INII_PATH, data);
		String cmd = "chmod 666 " + ConfigManager.GUID_INII_PATH;
		Utils.runCommand(cmd);
	}
	
	public void SyncTime(){
		Intent i = new Intent(mContext,NtpService.class);
		mContext.startService(i);
	}  
	/**
	 * 
	 * @描述: 未来电视认证
	 * @方法名: icntvAuth
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2015-8-12上午11:45:35	
	 * @修改人 Administrator
	 * @修改时间 2015-8-12上午11:45:35	
	 * @修改备注 
	 * @since
	 * @throws
	*/
	private void icntvAuth(final Context context){
		Runnable icntvAuthRunner = new Runnable() {
                        @Override
                        public void run() {
				String path = context.getApplicationContext().getFilesDir().getAbsolutePath();
				AssetsUtils assetsUtils = new AssetsUtils(context);
				File file = new File(path+"/ini/DeviceInfo.ini");
				if(!file.exists()){
					assetsUtils.openAssets("ini", "", path + "/ini", true);
				}else{
					Log.i(TAG, "icntv auth init/DeviceInfo.ini is exist ");
				}
				Log.i(TAG, "icntv auth start ");
				AuthImpl authImpl = new AuthImpl(context,null);
                                authImpl.SaveIcntvAuthInfo(ConfigManager.ICNTV_AUTH_RESULT,"111","123456");
                                String cmd = "chmod 666 " + ConfigManager.ICNTV_AUTH_RESULT;
                                Utils.runCommand(cmd);
				/*
				loginSDK sdk = loginSDK.getInstance();
				sdk.sdkInit(path);
				String ret = sdk.deviceLogin();
				Log.i(TAG, "icntv auth deviceLogin ret =" + ret);
				if(ret.equals("111")){
					StringBuffer tf  = new StringBuffer();
					int id = sdk.getDeviceID(tf);
					if(id==0) {
						Log.i(TAG, "icntv auth getDeviceID id =" + id + "deviceid = " + tf.toString());
						String sn = SystemInfoManager.readFromNandkey("sn");
						if(null == sn || sn.equals(" ") || sn.length()<5){
							SystemInfoManager.writeNandkey("sn", tf.toString());
						}
						AuthImpl authImpl = new AuthImpl(context,null);
						authImpl.SaveIcntvAuthInfo(ConfigManager.ICNTV_AUTH_RESULT,ret,tf.toString());
						String cmd = "chmod 666 " + ConfigManager.ICNTV_AUTH_RESULT;
						Utils.runCommand(cmd);
					}
	    			}
				*/
			}
                };
                new Thread(icntvAuthRunner).start();
	}
	private void PivosInitGuid() {
		AuthImpl authImpl = new AuthImpl(mContext,null);
		PivosAuthInfo authInfo = authImpl.GetPivosAuthInfo(ConfigManager.PIVOS_AUTH_RESULT);
		if(authInfo!=null && authInfo.getGuid().length()>0) {
			return ;
		}else{
			PivosRegDev();
			PivosGetGuid();
		}
	}
	private void PivosRegDev() {
		DeviceUtils.getTvID(mContext);
		String wifimac = DeviceUtils.getWifiMac(mContext);
		DeviceUtils.getFirmwareMd5();
		DeviceUtils.getGITVSN();
		String mac_wire = DeviceUtils.getEthernetMac();
		String randomvalue1 = Utils.getFixLenthString(8);
		String randomvalue2 = Utils.getFixLenthString(8);
		String randomvalue3 = Utils.getFixLenthString(8);
		String randomvalue4 = Utils.getFixLenthString(8);
		final String random = randomvalue1 + randomvalue2 + randomvalue3 + randomvalue4;
		AuthImpl authImpl = new AuthImpl(mContext,null);
		String sign =ConfigManager.CID+"|"+ConfigManager.TERMID+"|"+mac_wire+"|"+wifimac+"|"+random ;
		authImpl.PivosRegister(ServerManager.PIVOS_AUTH_SERVER, ConfigManager.CID, ConfigManager.TERMID, sign);
	}

	private void PivosGetGuid() {
		String tvid = DeviceUtils.getTvID(mContext);
		String wifimac = DeviceUtils.getWifiMac(mContext);
		String devmd5 = DeviceUtils.getFirmwareMd5();
		String sn = SystemInfoManager.readFromNandkey("usid");
		if(sn==null ||sn.isEmpty()){
			sn = "1234567890";
		}
		String mac_wire = DeviceUtils.getEthernetMac();
		String ver = DeviceUtils.getVersionInfo();
		AuthImpl authImpl = new AuthImpl(mContext,new IAuthImpl(){
			@Override
			public void onGuidFinished(GuidBean guidbean) {
				// TODO Auto-generated method stub
				try {
					if(guidbean!=null){
						GuidDataBean data = guidbean.getData();
						System.out.println("guid = " + data.getGuid() + "Guid_secret=" + data.getGuid_secret());
						AuthImpl authImpl = new AuthImpl(mContext,null);
		    			authImpl.SavePivosAuthInfo(ConfigManager.PIVOS_AUTH_RESULT,String.valueOf(0),data.getGuid(),"");
		    			String cmd = "chmod 666 " + ConfigManager.PIVOS_AUTH_RESULT;
		    			Utils.runCommand(cmd);
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onTvsKeyFinished(TvsKeyBean tvskeybean) {
				// TODO Auto-generated method stub
				
			}
			
		});
		authImpl.PivosGetGuid(ServerManager.PIVOS_AUTH_SERVER, tvid, ver,wifimac, devmd5,sn,mac_wire);
	}	
}
