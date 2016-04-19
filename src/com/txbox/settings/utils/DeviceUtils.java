package com.txbox.settings.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemProperties;
import android.telephony.TelephonyManager;
import android.util.Log;
/**
 * 
 * @类描述：设备工具类
 * @项目名称：go3cfactory
 * @包名： com.go3c.utils
 * @类名称：DeviceUtils
 * @创建人：gao
 * @创建时间：2014年6月23日下午12:23:15
 * @修改人：gao
 * @修改时间：2014年6月23日下午12:23:15
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright go3c
 * @mail 25550836@qq.com
 */
public class DeviceUtils {

	private static String macAddress = "";

	/**
	 * 
	 * @描述:获取wifimac地址
	 * @方法名: getMac
	 * @return
	 * @返回类型 String
	 * @创建人 gao
	 * @创建时间 2014年6月23日下午12:23:25
	 * @修改人 gao
	 * @修改时间 2014年6月23日下午12:23:25
	 * @修改备注
	 * @since
	 * @throws
	 */
	public static String getMac11() {
		String macSerial = null;
		String str = "";
		try {
			Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			for (; null != str;) {
				str = input.readLine();
				if (str != null) {
					macSerial = str.trim();// 去空格
					break;
				}
			}
		} catch (IOException ex) {
			// 赋予默认值
			ex.printStackTrace();
		}
		return macSerial;
	}

	/**
	 * 
	 * @描述:获取wifi的mac地址
	 * @方法名: getWifiMac
	 * @param context
	 * @return
	 * @返回类型 String
	 * @创建人 gao
	 * @创建时间 2014年6月24日下午8:43:01
	 * @修改人 gao
	 * @修改时间 2014年6月24日下午8:43:01
	 * @修改备注
	 * @since
	 * @throws
	 */
	public static String getWifiMac(Context context) {
		macAddress = "";
		final WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifi == null) {
			return "";
		}
		WifiInfo info = wifi.getConnectionInfo();
		macAddress = info.getMacAddress();
		if (macAddress == null && !wifi.isWifiEnabled()) {
			new Thread() {
				@Override
				public void run() {
					wifi.setWifiEnabled(true);
					for (int i = 0; i < 10; i++) {
						WifiInfo info = wifi.getConnectionInfo();
						macAddress = info.getMacAddress();
						if (macAddress != null) {
							break;
						}
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					wifi.setWifiEnabled(false);
				}
			}.start();

		}

		return macAddress;
	}

	/**
	 * 
	 * @描述:从配置中获取mac地址
	 * @方法名: getMacAddres
	 * @return
	 * @返回类型 String
	 * @创建人 gao
	 * @创建时间 2014年6月24日下午8:47:28
	 * @修改人 gao
	 * @修改时间 2014年6月24日下午8:47:28
	 * @修改备注
	 * @since
	 * @throws
	 */
	public static String getMacAddres() {
		String mac = null;
		mac = SystemProperties.get("persist.sys.macaddr", "");
		return mac;
	}
	
	public static String getEthernetMac() {
		String readMac = readStringFromFile("/sys/class/efuse/mac",null);
		if(readMac!=null) return readMac;
		String macSerial = null;
		String str = "";
		try {
			Process pp = Runtime.getRuntime().exec("cat /sys/class/net/eth0/address ");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			for (; null != str;) {
				str = input.readLine();
				if (str != null) {
					macSerial = str.trim();// 去空格
					break;
				}
			}
		} catch (IOException ex) {
			// 赋予默认值
			ex.printStackTrace();
		}
		return macSerial;
	}

	/**
	 * 
	 * @描述: 获取firmware版本
	 * @方法名: getFirmwareVersion
	 * @return
	 * @返回类型 String
	 * @创建人 Administrator
	 * @创建时间 2014-9-10上午11:59:10	
	 * @修改人 Administrator
	 * @修改时间 2014-9-10上午11:59:10	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public static String getFirmwareVersion(){
		String version="";
		version = Build.VERSION.INCREMENTAL;
		version = version.replace(" ", "_");
		return version;
	}
	
    public static String getVersionInfo(){
        String version="";
        version = SystemProperties.get("ro.build.version.versioncode","");
        version = version.replace(" ", "_");
        return version;
    }
	/**
	 * 
	 * @描述:  获取firmware md5 (目前暂时返回一指定值)
	 * @方法名: getFirmwareMd5
	 * @return
	 * @返回类型 String
	 * @创建人 Administrator
	 * @创建时间 2014-9-10上午11:58:34	
	 * @修改人 Administrator
	 * @修改时间 2014-9-10上午11:58:34	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public static String getFirmwareMd5(){
		
		String md5="01234567890123456789012345678901";
		return md5;
	}
    /**
     * 
     * @描述:获取设备唯一标识（临时使用，实际使用时换成guid）
     * @方法名: getTvID
     * @param c
     * @return
     * @返回类型 String
     * @创建人 Administrator
     * @创建时间 2014-9-1上午11:10:38	
     * @修改人 Administrator
     * @修改时间 2014-9-1上午11:10:38	
     * @修改备注 
     * @since
     * @throws
     */
	public static String getTvID(Context context) {
		
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, androidId;

		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String uniqueId = deviceUuid.toString();
	
				
		return uniqueId;
	}
	public static String getGITVSN(){
		String gitvsn = "AA0801021C030B7268";
		return gitvsn;
	}
	/**
	 * 
	 * @描述: 获取APK版本号
	 * @方法名: GetAppVersion
	 * @param context
	 * @return
	 * @返回类型 int
	 * @创建人 Administrator
	 * @创建时间 2014-11-21下午6:25:18	
	 * @修改人 Administrator
	 * @修改时间 2014-11-21下午6:25:18	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public static int GetAppVersion(Context context) {
		PackageManager pm = context.getPackageManager();
		PackageInfo pi = null;
		try {
			pi = pm.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pi.versionCode;
	}
    /* **
     * 获取前台进程的包名
     * *
     */
    
    public static String getForgroundApp(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(100);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            return topActivity.getPackageName();
        }
        return null;
    }
    
    public static long getAppInstallTime(Context ctx){
        PackageManager pm = ctx.getPackageManager();
        ApplicationInfo appInfo;
        try {
            appInfo = pm.getApplicationInfo(ctx.getPackageName(), 0);
            String appFile = appInfo.sourceDir;
            long installed = new File(appFile).lastModified()/1000; 
            return installed;
        } catch (NameNotFoundException e) {
            return 0;
        }catch(Exception e){
            return 0;
        }
    }
	public static String getPT() {
		String value = null;
		value = SystemProperties.get("ro.pivos.pt", "");
		return value;
	}
	public static String getChid() {
		String value = null;
		value = SystemProperties.get("ro.pivos.chid", "");
		return value;
	}
	public static String getHwVer() {
		String value = null;
		value = SystemProperties.get("persist.sys.hwconfig.hw_ver", "");
		return value;
	}
	public static String readStringFromFile(String path, String def) {
		BufferedReader reader = null;
		try {
			StringBuffer fileData = new StringBuffer(100);
			reader = new BufferedReader(new FileReader(path));
			char[] buf = new char[100];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
			}
			reader.close();

			return fileData.toString().trim();

		} catch (Throwable e) {
			Log.e("DeviceUtils","Exception", e);
		} finally {
			if (null != reader)
				try {
					reader.close();
					reader = null;
				} catch (Throwable t) {
					;
				}
		}

		return def;
	}
}
