package com.txbox.settings.common;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xbmc.android.remote.business.Command;
import org.xbmc.android.remote.business.NowPlayingPollerThread;
import org.xbmc.android.util.ClientFactory;
import org.xbmc.android.util.ConnectionFactory;
import org.xbmc.api.business.DataResponse;
import org.xbmc.api.business.INotifiableManager;
import org.xbmc.api.data.IControlClient;
import org.xbmc.api.data.IControlClient.ICurrentlyPlaying;
import org.xbmc.api.info.PlayStatus;
import org.xbmc.api.object.Host;
import org.xbmc.httpapi.WifiStateException;
import org.xml.sax.InputSource;

import com.pivos.cqdvbserver.CqDvbService;
import com.tencent.dmproxy.api.TVK_DMProxyConfig;
import com.tencent.dmproxy.api.TVK_BuildConfig;
import com.tencent.dmproxy.api.TVK_IDMProxy;
import com.tencent.dmproxy.api.TVK_VideoUrlInfo;
import com.tencent.dmproxy.logic.DmproxyManager;
import com.tencent.dmproxy.logic.SdkUtil;
import com.tencent.httpproxy.IDownloadFacade;
import com.tencent.oma.push.PushConfig;
import com.tencent.oma.push.PushManager;
import com.tencent.qqlive.sdk.TVKSdkManager;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatReportStrategy;
import com.tencent.stat.StatService;
import com.txbox.settings.bean.ClipBean;
import com.txbox.settings.bean.GuidInfoBean;
import com.txbox.settings.impl.AuthImpl;
import com.txbox.settings.impl.GetAddressImpl;
import com.txbox.settings.impl.SyscfgImpl;
import com.txbox.settings.impl.GetAddressImpl.IPlayInfoImpl;
import com.txbox.settings.mbx.api.NetManager;
import com.txbox.settings.report.GlobalInfo;
import com.txbox.settings.report.MtaOptions;
import com.txbox.settings.utils.CrashHandler;
import com.txbox.settings.utils.ConfigManager;
import com.txbox.settings.utils.DeviceUtils;
import com.txbox.settings.utils.MessageUtils;
import com.txbox.settings.utils.ServerManager;
import com.txbox.settings.utils.Utils;
import com.txbox.settings.utils.WebServer;
import com.txbox.settings.utils.XmlConfTools;

import android.R.integer;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import com.txbox.settings.utils.AssetsUtils;
import android.os.UserHandle;

public class TXbootApp extends Application{
	
	private NetManager netMgr;
	private ArrayList<Map<String, String>> networkInfo = null;
	private List<ScanResult> scanResult = null;
	private String local = "";
	private String Country = "";
	private String Province = "";
	private String City = "";
	private String city_id;
	private boolean mExit = false;
	private boolean mIsStart = false;
	private boolean mPushServiceStart = false;
	public void setNetMgr(NetManager netMgr){
		this.netMgr = netMgr;
	}
	public NetManager getNetManager(){
		return this.netMgr;
	}
	public void setNetworkInfo(ArrayList<Map<String, String>> networkInfo){
		this.networkInfo = networkInfo;
	}
	public ArrayList<Map<String, String>> getNetworkInfor(){
		return this.networkInfo ;
	}
	public void setScanResult(List<ScanResult> scanResult){
		this.scanResult = scanResult;
	}
	public List<ScanResult> getScanResult(){
		return this.scanResult ;
	}
	public void setLocaltion(String local){
		this.local = local;
	}
	public String getLocaltion(){
		return this.local;
	}
	public void setCountry(String Country){
		this.Country = Country;
	}
	public void setProvince(String Province){
		this.Province = Province;
	}
	public void setCity(String City){
		this.City = City;
	}
	public String getCountry(){
		return this.Country;
	}
	public String getProvince(){
		return this.Province;
	}
	public String getCity(){
		return this.City;
	}
	public void setCity_id(String city_id){
		this.city_id = city_id;
	}
	public String getCity_id(){
		return this.city_id;
	}
	public void startCyberHTTP(Context context) {
		//鍚姩鏈嶅姟
        	Intent intent_webServe = new Intent(context, CqDvbService.class);
		System.out.println("startCyberHTTP!!!!");
        	if(intent_webServe != null){
			System.out.println("startCyberHTTP 2222222");
        		//context.startServiceAsUser(intent_webServe,UserHandle.CURRENT);
			    context.startService(intent_webServe);
        	} else {
			System.out.println("startCyberHTTP intent_webServe is NULL !!!!");
		}
		
	}	

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
		System.out.println("Appliction startCyberHTTP ");
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		initGlobalConfig(getApplicationContext());
		super.onCreate();
	}
	public boolean GetExit(){
		return mExit;
	}
	public void SetExit(boolean value){
		mExit = value;
	}
	public boolean GetIsStart() {
		return mIsStart;
	}
	public void SetIsStart(boolean value) {
		mIsStart = value;
	}
	public void StartPushService(Context context)
	{
		AuthImpl authImpl = new AuthImpl(context,null);
		GuidInfoBean guid = authImpl.GetGudiInfo(ConfigManager.GUID_INII_PATH);
		if(guid!=null && guid.getGuid()!=null){
			PushConfig.setHost(ServerManager.PUSH_SERVER_ADDRESS);
			PushConfig.setPort(ServerManager.PUSH_SERVER_PORT);
			PushConfig.setDebug(true);
			PushConfig.setDeviceId(guid.getGuid());
			PushConfig.setBid(ServerManager.PUSH_SERVICE_BiD);
			PushManager.startPushService(context.getApplicationContext());
			mPushServiceStart = true;
		}
	}
	public boolean isPushServiceStarted(){
		return mPushServiceStart;
	}

	
	public void startWebServer(Context context){
		WebServer webServer = new WebServer();
		webServer.Start(context);
	}
	
	public void initPlaySdk(Context context){
		
		//String appkey = "eZHV3+s/Wnbw9WA1mI9WjwzxB9TNoKCJzp0wtvbDC5ruQfxnTBccWRNmO2YTNtx6IOR9bHSjR9ITtO7Da17KXGRR2IvC9jVHchFR4+OprzY2S57cvs8LBxjVQTyd6dzmH1DBw2piO4VAbJ/Wd6i4wPWktLN0shuDp3/5KojwNFe54lKc5X+HIqk6IoLcMcj8rxGFujnoX0jRw+lRJVeTBW9Yqxv7j0t5QSd5478KUctZcN+5QzxiilwQb9W9aNvZdoHWt+Ojxr5PQmBI4VJpl2HQqxPkTSCc4RijtjDQGzn098gmY4rV4S6uVXj8Wi8Ae1MN+M95XWrq1DobSBQtag==";
		
		String appkey = "MrxgWLFcJJOdSmE6XXg2k+EWtgtHv3lBPiL4ltF6Z3Es3QTLkNn5pY8P6RBZznKtgU/FpKQWr5UwC88 Qb1JVafL0P9Ok66Lky8wI3he+R8qnO1PqVKh19eS1goXg7ncw48FJuD1BXr9rI5ByYL+OrSzuphacti wb4d26q0ZchQ9CZ68KseJILUkDMbkARfKU5lRS68jGumSOW4uSTkJB5vpDKyql2jZsSoiJ3qigIEZYk ndmYVq6BLp5zyTZG0ZrDQnD5wlmP+/5+Y9otTgsyKV+x8LG1u2cR03ZUOJdCW+pQHXgF/FGQwtZOCk6 quKSkbmeOcHTNOG/gyN1purzvw==";
		TVK_BuildConfig.setDebugEnable(false);
		TVKSdkManager.initSdk(context, appkey);
		
		SdkUtil.init(context, "");
		/*
		AssetsUtils assetsUtils = new AssetsUtils(context);
                String skdServer = assetsUtils.readAsset(context,"sdkserver.json");
                System.out.println("sdk server = " + skdServer);
                TVK_DMProxyConfig.setDMProxyConfig(skdServer);	
		*/
	
		TVK_IDMProxy dmProxy = new DmproxyManager();
		
		int platform = TVK_DMProxyConfig.getPlatform();
		//int platform = 940603;
		String svraddr = dmProxy.getLocalHttpSvrAddr(null);
		
		SyscfgImpl.SavePlayProxyAddress(ConfigManager.PLAY_PROXY_ADDRESS_PATH_OLD,svraddr,String.valueOf(platform));
		SyscfgImpl.SavePlayProxyAddress(ConfigManager.PLAY_PROXY_ADDRESS_PATH,svraddr,String.valueOf(platform));
		String cmd = "chmod 666 " + ConfigManager.PLAY_PROXY_ADDRESS_PATH;
		Utils.runCommand(cmd);
		/*
		TVK_BuildConfig.setDebugEnable(false);
		String appkey_hls = "M4z8V/LT+rZXa5Hc0jLGTRjLI9fwiVwbRvILaldV8azGk6VM1UQkdCaCH9sEyByFy6vdgOm0L0R3Wd+f7ZDL9DbMnhwcu2fQW0Cdu1SacOByguW+6BT3fjAtIkTnIySTT4hmU9dYzmQ1HyHHwglTjZjcGi/K6bxVM4OhAQ595kdAJNA+PwsdoI3Kk+SkJNa4/gr4UGCvnjdVq51UQF+YDUBxA9eiMXLAqmc5OOJmR4iWj/CJtYN67Zy/mh3Ve2F8V5cp62CGJS6VbpB2x9nDTmw0e7b3eOKcSCQ9uOtJ36R3ffYBY+VC9LLWzlf4u9danCaKUzLqL5wgFdMt5JT2Hw==";
		TVK_SDKMgr.initSdk(context, appkey_hls, "814160592");
		*/
	}
    public static void initGlobalConfig(final Context ctx) {
        //GlobalInfo.setGUID(AppUtils.getGUID());
        GlobalInfo.setQQ("");
        GlobalInfo.setOpenID("");
        GlobalInfo.setOpenIDType("");
        GlobalInfo.setLicenseID("");
        //GlobalInfo.setMACAdress(AppUtils.getLocalMacAddress(ctx));
        //GlobalInfo.setIPInfo(AppUtils.getLocalIpAddress());
        GlobalInfo.setDeviceID(android.os.Build.MODEL);
        GlobalInfo.setAppVersion(SystemProperties.get("ro.build.version.versioncode",""));
        //GlobalInfo.setVersionCode(SystemProperties.get("ro.build.version.versioncode",""));
        GlobalInfo.setSysVesion(SystemProperties.get("ro.build.version.versioncode",""));
        GlobalInfo.setAppInstallTime(DeviceUtils.getAppInstallTime(ctx));
        // GlobalInfo.setSdkNum(AppUtils.getSdkVersion());
        //GlobalInfo.setUserAgent(getUserAgent());
        //GlobalInfo.setMD5(AppUtils.getMD5());
        GlobalInfo.setResource(ctx);
        GlobalInfo.setPackageName("com.pivos.tofu");
        GlobalInfo.setQua();
    }
	
    public static void initMTAConfig(boolean isDebugMode) {
        if (isDebugMode) { // 璋冭瘯鏃跺缓璁缃殑寮�鍏崇姸鎬�
            // 鏌ョ湅MTA鏃ュ織鍙婁笂鎶ユ暟鎹唴瀹�
            StatConfig.setDebugEnable(true);
            // 绂佺敤MTA瀵筧pp鏈鐞嗗紓甯哥殑鎹曡幏锛屾柟渚垮紑鍙戣�呰皟璇曟椂锛屽強鏃惰幏鐭ヨ缁嗛敊璇俊鎭��
            // StatConfig.setAutoExceptionCaught(false);
            // StatConfig.setEnableSmartReporting(false);
            // Thread.setDefaultUncaughtExceptionHandler(new
            // UncaughtExceptionHandler() {
            //
            // @Override
            // public void uncaughtException(Thread thread, Throwable ex) {
            // logger.error("setDefaultUncaughtExceptionHandler");
            // }
            // });
            // 璋冭瘯鏃讹紝浣跨敤瀹炴椂鍙戦��
//          StatConfig.setStatSendStrategy(StatReportStrategy.BATCH);
//          // 鏄惁鎸夐『搴忎笂鎶�
//          StatConfig.setReportEventsByOrder(false);
//          // 缂撳瓨鍦ㄥ唴瀛樼殑buffer鏃ュ織鏁伴噺,杈惧埌杩欎釜鏁伴噺鏃朵細琚啓鍏b
//          StatConfig.setNumEventsCachedInMemory(30);
//          // 缂撳瓨鍦ㄥ唴瀛樼殑buffer瀹氭湡鍐欏叆鐨勫懆鏈�
//          StatConfig.setFlushDBSpaceMS(10 * 1000);
//          // 濡傛灉鐢ㄦ埛閫�鍑哄悗鍙帮紝璁板緱璋冪敤浠ヤ笅鎺ュ彛锛屽皢buffer鍐欏叆db
//          StatService.flushDataToDB(getApplicationContext());

//           StatConfig.setEnableSmartReporting(false);
//           StatConfig.setSendPeriodMinutes(1);
//           StatConfig.setStatSendStrategy(StatReportStrategy.PERIOD);
        } else { // 鍙戝竷鏃讹紝寤鸿璁剧疆鐨勫紑鍏崇姸鎬侊紝璇风‘淇濅互涓嬪紑鍏虫槸鍚﹁缃悎鐞�
            // 绂佹MTA鎵撳嵃鏃ュ織
            StatConfig.setDebugEnable(false);
            // 鏍规嵁鎯呭喌锛屽喅瀹氭槸鍚﹀紑鍚疢TA瀵筧pp鏈鐞嗗紓甯哥殑鎹曡幏
            StatConfig.setAutoExceptionCaught(true);
            // 閫夋嫨榛樿鐨勪笂鎶ョ瓥鐣�
            StatConfig.setStatSendStrategy(StatReportStrategy.APP_LAUNCH);
        }
        
    }
    public static void startMat(Context context){
    	String appkey = "A3VA23DKRR4P";
    	// 鍒濆鍖栧苟鍚姩MTA
    	// 绗笁鏂筍DK蹇呴』鎸変互涓嬩唬鐮佸垵濮嬪寲MTA锛� 鍏朵腑appkey涓鸿瀹氱殑鏍煎紡鎴朚TA鍒嗛厤鐨勪唬鐮併��
    	// 鍏跺畠鏅�氱殑app鍙嚜琛岄�夋嫨鏄惁璋冪敤
    	try {
    		// 绗笁涓弬鏁板繀椤讳负锛� com.tencent.stat.common.StatConstants.VERSION
    		StatService.startStatService(context, appkey,
    				com.tencent.stat.common.StatConstants.VERSION);
    	} catch (MtaSDkException e) {
    		// MTA鍒濆鍖栧け璐�
    		Log.e("startMat","MTA start failed.");
  
    	}
    }
}
