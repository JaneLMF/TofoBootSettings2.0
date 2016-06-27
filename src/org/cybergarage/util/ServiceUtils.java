package org.cybergarage.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.baustem.service.dvbnavigation.IDVBNavigation;
import com.baustem.service.epgnavigation.IEPGNavigation;
import com.baustem.service.vodnavigation.IVODNavigation;
import com.baustem.service.vodstream.IVODStream;
import com.pivos.utils.WebServer;
import com.tvxmpp.XMPPService;
import com.tvxmpp.util.L;
import com.txbox.settings.common.NetworkManager;
import com.txbox.settings.common.TXbootApp;
import com.txbox.settings.utils.ConfigManager;
import com.txbox.settings.utils.PathUtils;
import com.txbox.settings.utils.Utils;
import com.txbox.settings.utils.XmlConfTools;
import com.txbox.txsdk.R;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class ServiceUtils {
	private static final String TAG = ServiceUtils.class.getSimpleName();
	
	private Context mContext = null;
	private TXbootApp app=null;
	
	private static IDVBNavigation m_idvbService = null;
    private static IEPGNavigation m_iepgService = null;
    private static IVODNavigation m_ivodService = null;
    private static IVODStream m_ivodStreamService = null;
	
	private ServiceConnection m_serviceCon_dvbNavigation = null;
    private ServiceConnection m_serviceCon_epgNavigation = null;
    private ServiceConnection m_serviceCon_vodNavigation = null;
    private ServiceConnection m_serviceCon_vodStreamNavigation = null;

    public ServiceUtils(Context context){
    	this.mContext = context;
    	app = (TXbootApp) mContext.getApplicationContext();
    }
    
	public void start(){
		Log.d(TAG, "BootSettings BOOT_COMPLETED");
		CheckEth();
		
		startXMPPService();
		
		Utils.initInstalledData(ConfigManager.GUID_INII_PATH_OLD,ConfigManager.GUID_INII_PATH);		
		String cmd = "chmod 666 " + ConfigManager.GUID_INII_PATH;
		Utils.runCommand(cmd);
		
		app.startWebServer(mContext);
		app.initPlaySdk(mContext);
		app.initMTAConfig(true);
		app.startMat(mContext);
		//app.initGlobalConfig(context);
		SaveScreensaverPhotoLocal();
		
		SavePriorityApk();
		initDefaultInputMethod(mContext);
		app.startCyberHTTP(mContext);
		startGateWayTask();
	}
	
	private void CheckEth() {
		NetworkManager networkMgr = new NetworkManager(mContext, new Handler(mContext.getMainLooper()));
		networkMgr.setEthEnable();
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
		Log.i(TAG, "Enter the startGateWayService()");
        //start DVBNavigation
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
            Log.i(TAG, "start DVBNavigation fail");
            return;
        }
        Log.i(TAG, "start DVBNavigation success");

        //start EPGNavigation
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
            Log.i(TAG, "start EPGNavigation fail");
            return;
        }
        Log.i(TAG, "start EPGNavigation success");

        //start VODNavigation
        m_serviceCon_vodNavigation = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                m_ivodService = IVODNavigation.Stub.asInterface(service);
                WebServer.setVodService(m_ivodService);
            }
        };

        result = app.bindService(new Intent("com.baustem.service.vodnavigation.VODNavigation"),
                m_serviceCon_vodNavigation, Context.BIND_AUTO_CREATE);
        if (!result) {
            Log.i(TAG, "start VODNavigation fail");
            return;
        }
        Log.i(TAG, "start VODNavigation success");

        //start VODStreamNavigation
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
            Log.i(TAG, "start VODStreamNavigation fail");
            return;
        }
        Log.i(TAG, "start VODStreamNavigation success");
    }
	
	private void startXMPPService() {
		L.d("BootReceiver startXMPPService");
		Intent i = new Intent(mContext, XMPPService.class);
		mContext.startService(i);
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
	
	private void SavePriorityApk(){
		setParams(ConfigManager.SCREENSAVER_PATH,"screensaverpriority", "1");
		setParams(ConfigManager.XMLCONF_PATH,"allowlocalapk", "1");
	}
	
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
}
