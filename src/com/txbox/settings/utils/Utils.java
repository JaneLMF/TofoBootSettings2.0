package com.txbox.settings.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

//import android.app.SystemWriteManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class Utils {
	private static final String TAG = "Utils";

	/*public static String readSysFile(SystemWriteManager sw, String path) {
		if (sw == null) {
			Log.d(TAG, "readSysFile(), sw is null !!");
			return null;
		}

		if (path == null) {
			Log.d(TAG, "readSysFile(), path is null !!");
			return null;
		}

		return sw.readSysfs(path);

	}

	public static void writeSysFile(SystemWriteManager sw, String path,String value) {

		if (sw == null) {
			Log.d(TAG, "writeSysFile(), sw is null !!");
			return;
		}

		if (path == null) {
			Log.d(TAG, "writeSysFile(), path is null !!");
			return;
		}

		if (value == null) {
			Log.d(TAG, "writeSysFile(), value is null !!");
			return;
		}

		sw.writeSysfs(path, value);
	}

	public static boolean getPropertyBoolean(SystemWriteManager sw,String prop, boolean defaultValue) {
		if (sw == null) {
			Log.d(TAG, "getPropertyBoolean(), sw is null !!");
			return defaultValue;
		}

		if (prop == null) {
			Log.d(TAG, "getPropertyBoolean(), path is null !!");
			return defaultValue;
		}

		return sw.getPropertyBoolean(prop, defaultValue);

	}

	public static String getPropertyString(SystemWriteManager sw, String prop,String defaultValue) {
		if (sw == null) {
			Log.d(TAG, "getPropertyString(), sw is null !!");
			return defaultValue;
		}

		if (prop == null) {
			Log.d(TAG, "getPropertyString(), path is null !!");
			return defaultValue;
		}

		return sw.getPropertyString(prop, defaultValue);

	}

	public static int getPropertyInt(SystemWriteManager sw, String prop,int defaultValue) {
		if (sw == null) {
			Log.d(TAG, "getPropertyInt(), sw is null !!");
			return defaultValue;
		}

		if (prop == null) {
			Log.d(TAG, "getPropertyInt(), path is null !!");
			return defaultValue;
		}
		return sw.getPropertyInt(prop, defaultValue);

	}
	*/
	public static String getBinaryString(String config) {
		String indexString = "0123456789abcdef";
		String configString = config.substring(config.length() - 1,
				config.length());
		int indexOfConfigNum = indexString.indexOf(configString);
		String ConfigBinary = Integer.toBinaryString(indexOfConfigNum);
		if (ConfigBinary.length() < 4) {
			for (int i = ConfigBinary.length(); i < 4; i++) {
				ConfigBinary = "0" + ConfigBinary;
			}
		}
		return ConfigBinary;
	}
	
	public static int[] getBinaryArray(String binaryString) {
		int[] tmp = new int[4];
		for (int i = 0; i < binaryString.length(); i++) {
			String tmpString = String.valueOf(binaryString.charAt(i));
			tmp[i] = Integer.parseInt(tmpString);
		}
		return tmp;
	}
	
	public static String arrayToString(int[] array) {
		String getIndexString = "0123456789abcdef";
		int total = 0;
		System.out.println();
		for (int i = 0; i < array.length; i++) {
			total = total
					+ (int) (array[i] * Math.pow(2, array.length - i - 1));
		}
		Log.d(TAG, "in arrayToString cecConfig is:" + total);
		String cecConfig = "cec" + getIndexString.charAt(total);
		Log.d(TAG, "in arrayToString cecConfig is:" + cecConfig);
		return cecConfig;
	}
	
	public static void saveStringToFile(String fileName, String value) {
		File file = new File(fileName);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, false);
			fos.write(value.getBytes());
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	public static StringBuilder getStringFromFile(String path) throws UnsupportedEncodingException {

		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		BufferedInputStream in = new BufferedInputStream(inputStream);
		in.mark(4);
		byte[] first3bytes = new byte[3];
		try {
			in.read(first3bytes);
			in.reset();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		BufferedReader br = null;

		try {
			br = new BufferedReader(new InputStreamReader(in, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String line = null;
		StringBuilder sb = new StringBuilder();

		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb;
	}
	
	public static boolean fileIsExists(Context context , String name){
        String pathString = "/data/data/"+ context.getPackageName() +"/lib/" +name; 
		try{
             File f=new File(pathString);
             if(!f.exists()){
                     return false;
             }
             
	     }catch (Exception e) {
	             // TODO: handle exception
	             return false;
	     }
		return true;
	}
	
	public static String getLibPath(Context context , String name){
        String pathString = "/data/data/"+ context.getPackageName() +"/lib/" +name; 
         return pathString;
	}
	
	public static boolean runCommand(String command) {
		Process process = null;
		String s = "\n";
		try {
			process = Runtime.getRuntime().exec(command);
			Log.i("command", "The Command is : " + command);
			/*
			 * BufferedReader in = new BufferedReader( new
			 * InputStreamReader(process.getInputStream())); String line = null;
			 * while ((line = in.readLine()) != null) { s += line + "\n"; }
			 * System.out.println("run commond res=" + s);
			 */
			int ret = process.waitFor();
			Log.i("command", "The Command result is : " + ret);
		} catch (Exception e) {
			Log.w("Exception ", "Unexpected error - " + e.getMessage());
			return false;
		} finally {
			try {
				process.destroy();
			} catch (Exception e) {
				Log.w("Exception ", "Unexpected error - " + e.getMessage());
			}
		}
		return true;
	}

	
	/* 
	 * 返回长度为【strLength】的随机数，在前面补0 
	 */  
	public static String getFixLenthString(int strLength) {  
	      
	    Random rm = new Random();  
	      
	    // 获得随机数  
	    double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);  
	  
	    // 将获得的获得随机数转化为字符串  
	    String fixLenthString = String.valueOf(pross);  
	  
	    // 返回固定的长度的随机数  
	    return fixLenthString.substring(2, strLength + 2);
	}
	
	public static boolean openAPP(Context mcontext, String packageName) {
		PackageInfo pi;
		try {
			pi = mcontext.getPackageManager().getPackageInfo(packageName, 0);
			Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
			resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			resolveIntent.setPackage(pi.packageName);
			PackageManager pm = mcontext.getPackageManager();
			List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);
			if (apps != null && apps.size() > 0) {
				ResolveInfo ri = apps.iterator().next();
				if (ri != null) {
					// String packageName = ri.activityInfo.packageName;
					String className = ri.activityInfo.name;
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_LAUNCHER);
					ComponentName cn = new ComponentName(packageName, className);
					intent.setComponent(cn);
					// send_pause_cmd(mcontext);
					mcontext.startActivity(intent);
					return true;
				}
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// String str =
			// mcontext.getResources().getString(R.string.string_app_start_error);
			// UIHelper.sendToast(mcontext,str);
			return false;
		}
		return false;
	}
	
	public static boolean IsAppInstalled(Context mcontext, String packageName) {
		PackageInfo pi;
		try {
			pi = mcontext.getPackageManager().getPackageInfo(packageName, 0);
			Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
			resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			resolveIntent.setPackage(pi.packageName);
			PackageManager pm = mcontext.getPackageManager();
			List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);
			if (apps != null && apps.size() > 0) {
				ResolveInfo ri = apps.iterator().next();
				if (ri != null) {
					return true;
				}
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public static void initInstalledData(String oldPath,String nowPath){
		int bytesum = 0; 
        int byteread = 0; 
		File old = new File(oldPath);
		if(old.exists()){
			try {
			 InputStream inStream = new FileInputStream(oldPath);
			 //读入原文件 
             FileOutputStream fos = new FileOutputStream(nowPath); 
			byte[] buffer = new byte[1444]; 
            int length; 
            while ( (byteread = inStream.read(buffer)) != -1) { 
                bytesum += byteread; //字节数 文件大小 
                fos.write(buffer, 0, byteread); 
            } 
            fos.flush();
            fos.close();
            inStream.close(); 
            old.delete();//复制完了将文件删除
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
