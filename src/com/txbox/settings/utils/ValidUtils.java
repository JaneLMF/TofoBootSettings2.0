package com.txbox.settings.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;


/**
 * 
 * @类描述：校验工具�?
 * @项目名称：go3cLauncher
 * @包名�?com.go3c.utils
 * @类名称：ValidUtils
 * @创建人：gao
 * @创建时间�?014�?�?2日上�?1:34:54
 * @修改人：gao
 * @修改时间�?014�?�?2日上�?1:34:54
 * @修改备注�?
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright go3c
 * @mail 25550836@qq.com
 */



public class ValidUtils {
	static boolean temp = false;
	
	/**
	 * 
	 * @描述:判断应用是否正在运行
	 * @方法�? validIsRunning
	 * @param packageName
	 * @return
	 * @返回类型 boolean
	 * @创建�?gao
	 * @创建时间 2014�?�?1日下�?:57:21
	 * @修改�?gao
	 * @修改时间 2014�?�?1日下�?:57:21
	 * @修改备注
	 * @since
	 * @throws
	 */
	public static boolean validIsRunning(Context context, String packageName) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> l = am.getRunningAppProcesses();
		boolean isAppRunning = false;
		for (RunningAppProcessInfo runningAppProcessInfo : l) {
			if (runningAppProcessInfo.processName.equals(packageName)) {
				isAppRunning = true;
				break;
			}
		}

		return isAppRunning;
	}

	/**
	 * 
	 * @描述:判断有线是否连接
	 * @方法�? checkEthernet
	 * @param context
	 * @return
	 * @返回类型 boolean
	 * @创建�?gao
	 * @创建时间 2014�?�?4日下�?:45:50
	 * @修改�?gao
	 * @修改时间 2014�?�?4日下�?:45:50
	 * @修改备注
	 * @since
	 * @throws
	 */
	public static boolean checkEthernet(Context context) {
		if (context != null) {
			ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = conn.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
			return networkInfo.isConnected();
		}
		return false;
	}

	/**
	 * 
	 * @描述:判断WIFI是否连接
	 * @方法�? checkWifi
	 * @param context
	 * @return
	 * @返回类型 boolean
	 * @创建�?gao
	 * @创建时间 2014�?�?4日下�?:46:04
	 * @修改�?gao
	 * @修改时间 2014�?�?4日下�?:46:04
	 * @修改备注
	 * @since
	 * @throws
	 */
	public static boolean checkWifi(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 
	 * @描述:判断当前是否连接网络
	 * @方法�? isInternetConnected
	 * @param context
	 * @return
	 * @返回类型 boolean
	 * @创建�?gao
	 * @创建时间 2014�?�?日下�?2:14:58	
	 * @修改�?gao
	 * @修改时间 2014�?�?日下�?2:14:58	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public static boolean isInternetConnected1(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				String result = ping("www.baidu.com");
				if ("success".equals(result)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @描述:判断网线/无线是否连接（这里不确定是否有外网）
	 * @方法�? isNetworkExist
	 * @param context
	 * @return
	 * @返回类型 boolean
	 * @创建�?gao
	 * @创建时间 2014�?�?日下�?2:11:03
	 * @修改�?gao
	 * @修改时间 2014�?�?日下�?2:11:03
	 * @修改备注
	 * @since
	 * @throws
	 */
	public static boolean isNetworkExist(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	private static String ping(String str) {
		String result = "";
		Process p;
		try {
			// ping -c 3 -w 100 �?�?c 是指ping的次�?3是指ping 3�?�?w 100
			// 以秒为单位指定超时间隔，是指超时时间�?00�?
			p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + str);
			int status = p.waitFor();
			InputStream input = p.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			if (status == 0) {
				result = "success";
			} else {
				result = "faild";
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 
	 * @描述:判断类似ip，掩码的格式
	 * @方法名: isIllegalIP
	 * @return
	 * @返回类型 boolean
	 * @创建人 huang
	 * @创建时间 Aug 22, 201411:02:10 AM	
	 * @修改人 huang
	 * @修改时间 Aug 22, 201411:02:10 AM	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public static boolean isIllegalIP(String str){ 
		boolean temp = true;
		
		Pattern pattern = Pattern.compile("(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})");

		Matcher matcher = pattern.matcher(str); //以验证127.400.600.2为例

		if(!matcher.matches()){
			temp = false;
		}
		return temp;
	}
	public static boolean isInternetConnected(){
		 final String ip = "http://113.108.11.73/index.html";
		 final String ip2 = "http://115.239.210.27";
		 temp = false;
		new Thread(){
			public void run() {
				if(isInternetConnected(ip2)){
				   temp = true;
				}
			};
		}.start();
		
		
		return temp;
	}
	
	/**
	 * 
	 * @描述:判断当前是否连接网络（这里的网络指的是外网）
	 * @方法名: isInternetConnected
	 * @return
	 * @返回类型 boolean
	 * @创建人 gao
	 * @创建时间 2014年9月15日上午11:50:28
	 * @修改人 gao
	 * @修改时间 2014年9月15日上午11:50:28
	 * @修改备注
	 * @since
	 * @throws
	 */
	public static boolean isInternetConnected(String urlStr) {
		boolean result = false;
		try {
			// froyo之前的系统使用httpurlconnection存在bug
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
				System.setProperty("http.keepAlive", "false");
			}

			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);// 设置为允许输出
			conn.setConnectTimeout(3 * 1000);// 3S超时

			Map<String, List<String>> headerMap = conn.getHeaderFields();
			Iterator<String> iterator = headerMap.keySet().iterator();
			if(iterator==null){
				System.err.println("iterator==null");
				System.out.println("iterator==null");
			}
			while (iterator.hasNext()) {
				result = true;
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
}