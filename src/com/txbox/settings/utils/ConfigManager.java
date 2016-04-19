package com.txbox.settings.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemProperties;
import android.util.Log;
/**
 * 
 * @类描述：配置文件读写类
 * @项目名称：go3cLauncher
 * @包名： com.go3c.utils
 * @类名称：ConfigManager	
 * @创建人："liting yan"
 * @创建时间：2014-6-15下午7:22:35	
 * @修改人："liting yan"
 * @修改时间：2014-6-15下午7:22:35	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright go3c
 * @mail 939757170@qq.com
 */
public class ConfigManager{
	private String CONFIG_FILE="";
	//private static final String CONFIG_FILE="/mnt/sdcard/customer.properties";
	private Properties prop=null;
	private String TAG = "config";
	
	/*APPID */
	//private String APPID="101151335";
	public static String APPID="101161688";
	
	/*用于保存用户登录信息文件路径*/
	public static String USERINFO_INII_PATH_OLD ="/sdcard/Android/userinfo"; 
	public static String USERINFO_INII_PATH ="/sdcard/Android/userinfo"; 
	
	/*用于保存guid信息文件路径*/
	public static String GUID_INII_PATH_OLD ="/sdcard/Android/guidinfo";
	public static String GUID_INII_PATH ="/sdcard/Android/guidinfo";
	
	/*腾讯播放SDK代理地址，端口存储路径*/
	public static String PLAY_PROXY_ADDRESS_PATH_OLD ="/sdcard/Android/proxyaddress";
	public static String PLAY_PROXY_ADDRESS_PATH ="/sdcard/Android/proxyaddress";
	
	/*tv type,取值由TX定义*/
	public static String TV_TYPE ="41";
    	/*pivos认证结果*/
	public static String PIVOS_AUTH_RESULT = "/data/data/pivos_auth";
	/*未来电视认证结果*/
	public static String ICNTV_AUTH_RESULT = "/data/data/icntv_auth";
	
	/*已安装应用json文件保存路径*/
	public static String INSTALLED_APPS_PATH = "/data/data/installedapps.js";
	public static String INSTALLED_GAME_PATH = "/data/data/installedgame.js";
	public static String APP_INSTALLING_PATH = "/data/data/packageinstall.js";
	public static String APP_WAITING_PATH = "/data/data/packagewaiting.js";
	public static String APP_ICON_PATH = "/data/data/img";
	public static String ALL_APP_PATH = "/data/data/installedall.js";
	/*保存屏保参数路径*/
	public static String SCREENSAVER_PATH = "/sdcard/Android/data/com.pivos.tofu/files/.tofu/userdata/addon_data/screensaver.tencent.slideshow";
	/*保存一般参数路径*/
	public static String XMLCONF_PATH = "/sdcard/Android";
	
	public static String PLAYING10C_FILE_PATH = "/sdcard/Android/data/com.pivos.tofu/files/.tofu/temp/temp";
	public static String DEFAULT_GUID="95c60a8d505a0d308b59facbe05d7bfe";
	public static String DEFAULT_PR = "SETUPWIZARD";
	public static String QUA_PATH = "/sdcard/Android/qua";
	public static String GAME_PATH = "/data/data";
	/*保存游戏文件名*/
	public static String GAMEFILE = "gamePackage.js";
	
	public static String CID = "pivos";
	public static String TERMID = "STB";
	public ConfigManager(String config_file) {
		CONFIG_FILE = config_file;
		init();
	}
	/**
	 * 
	 * @描述:初始化配置文件
	 * @方法名: init
	 * @返回类型 void
	 * @创建人 "liting yan"
	 * @创建时间 2014-6-15下午7:28:01	
	 * @修改人 "liting yan"
	 * @修改时间 2014-6-15下午7:28:01	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	private void init(){
		prop = loadConfig(CONFIG_FILE);
		if(prop==null){
			//配置文件不存在的时候创建配置文件 初始化配置信息
			prop=new Properties();	
		}
	}
	/**
	 * 
	 * @描述: 获取字段相应值
	 * @方法名: getValue
	 * @param name
	 * @return
	 * @返回类型 String
	 * @创建人 "liting yan"
	 * @创建时间 2014-6-15下午7:28:24	
	 * @修改人 "liting yan"
	 * @修改时间 2014-6-15下午7:28:24	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public String getValue(String name){
		String value;
		value = (String)prop.getProperty(name);
		Log.d(TAG,"get name = " + name + "  value = " + value);
		return value;
	}
	/**
	 * 
	 * @描述:设置字段值
	 * @方法名: setValue
	 * @param name
	 * @param value
	 * @返回类型 void
	 * @创建人 "liting yan"
	 * @创建时间 2014-6-15下午7:28:51	
	 * @修改人 "liting yan"
	 * @修改时间 2014-6-15下午7:28:51	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public boolean setValue(String name ,String value){
		
		
		if(value!=null){
			prop.put(name,value);
			Log.d(TAG,"set name = " + name + "  value = " + value);
			return saveConfig(CONFIG_FILE,prop);
		}else{
			return false;
		}
		
	}
		
	//读取配置文件 
	public Properties loadConfig(String file) {
		Properties properties = new Properties();
		try {
			FileInputStream s = new FileInputStream(file);
			properties.load(s);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return properties;
	}
	//保存配置文件
	public boolean saveConfig(String file, Properties properties) {
		try {
			File fil=new File(file);
			if(!fil.exists())
				fil.createNewFile();
			FileOutputStream s = new FileOutputStream(fil);
			properties.store(s, "");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
