package com.txbox.settings.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.util.Log;

/**
 * 
 * @类描述：复制assets目录下文件到指定路径
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.utils
 * @类名称：AssetsUtils	
 * @创建人：Administrator
 * @创建时间：2014-9-17下午12:20:19	
 * @修改人：Administrator
 * @修改时间：2014-9-17下午12:20:19	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class AssetsUtils {
	final String TAG = "AssetsUtils";
	Context lContext;
	AssetManager assetManager;

	public AssetsUtils(Context mContext) {
		lContext = mContext;
		assetManager = mContext.getResources().getAssets();
	}
	  
	public boolean openAssets(String path, String dirName, String targetPath,boolean isExcute){
		if(!dirName.equals("")){
			path=path+"/"+dirName;
		}  
		targetPath = targetPath.concat("/").concat(dirName);
		String[] assets = null;
		try {
			assets = assetManager.list(path);
		} catch (IOException e1) {	}
		
		for (int i = 0; i < assets.length; i++) {
			
			if(assets[i].equals("spec_keys.txt") || assets[i].equals("fast_keys.txt") || assets[i].equals("notify.txt")){
				File f = new File(targetPath+"/"+assets[i]);
				if (f.exists())
					f.delete();
			}
			File f = new File(targetPath+"/"+assets[i]);
			if (f.exists())
				f.delete();
			
			try {
					InputStream inputStream = assetManager.open(path.concat("/").concat(assets[i]));
					int r;
					for (r=0; r<20;r++)
					{
						if (unpack(path, assets[i], inputStream, targetPath)){
							if(isExcute)
								chmod(assets[i],targetPath,"777");
							else
								chmod(assets[i],targetPath,"666");
								
							break;
						} 
	   					try{
	   						Thread.sleep(500);
	   					}catch(InterruptedException e){ };
					}  
					if (r==20){
						Log.d("Webkey_java", "unpack errpr: - "+assets[i]);
						return false;
					}

				} catch (IOException e) {					
					if (!openAssets(path, assets[i], targetPath ,isExcute))
						return false;					
				}

		}
		return true;
	}
	
	public boolean unpackSingleFile(String path, String file, String targetPath){
		try{
			InputStream inputStream = assetManager.open(path+"/"+file);
			int r;
			for (r=0; r<20;r++)
			{
				if (unpack(path, file, inputStream, targetPath))
					break;
				try{
					Thread.sleep(500);
				}catch(InterruptedException e){ };
			}
			if (r==20)
			{
				Log.d(TAG,"unable to unpack "+file);
				return false;
			}
			return true;
		}catch(Exception e){
			Log.d(TAG,"unable to unpack "+file);
			return false;
		}
	}
	
    private boolean unpack(String path, String filename, InputStream is, String targetPath){
        (new File(targetPath)).mkdirs();
        
		try {
			FileOutputStream fOut;
			fOut = new FileOutputStream(new File(targetPath.concat("/").concat(filename)));
	        int read = 0;
	        byte[] bytes = new byte[16384];
	        while ((read = is.read(bytes)) != -1)
	                fOut.write(bytes, 0, read);	        
	        fOut.flush();
	        fOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("unpack file not found "+targetPath.concat("/").concat(filename));
			if (path == "")
			{ 
				try {
					InputStream inputStream = null;
					inputStream = assetManager.open(filename);
					FileOutputStream fOut;
					fOut = lContext.openFileOutput(filename, 0);
					DataOutputStream osw = new DataOutputStream(fOut);

					int c;
			        byte[] bytes = new byte[16384];
					while (true) {
						c = inputStream.read(bytes);
						if (c <= 0)
							break;
						osw.write(bytes,0,c);

					}
					osw.flush();
					osw.close();
				} catch (IOException ee) {
//					e.printStackTrace();
					Log.d(TAG,"unable to unpack "+filename);
					return false;
				}
				return true;
			}
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("unpack file IOException:" );
			e.printStackTrace();
			return false;
		}
		return true;
    }
    
	public boolean delDir(File path) {
	    if(path.exists()){
	        File[] files = path.listFiles();
		if (files != null)
		{
			for(int i=0; i<files.length; i++) {
				if(files[i].isDirectory())
					delDir(files[i]);	           
				else
					files[i].delete();
			}
		}
	    }
	    return(path.delete());
	}		

	public int getCurrentVersionCode() {
	
		try {
			PackageManager manager = lContext.getPackageManager();
			PackageInfo info = manager.getPackageInfo(lContext.getPackageName(),0);
			return info.versionCode;
		} catch (Exception e) {
			return -1;
		}
	}

	public String getCurrentVersionName() {
		
		try {
			PackageManager manager = lContext.getPackageManager();
			PackageInfo info = manager.getPackageInfo(lContext.getPackageName(),0);
			return info.versionName;
		} catch (Exception e) {
			return "";
		}
	}
	
	public boolean chmod(String file, String dir,String mod) {
		if(file.length() >0 && !dir.endsWith("/"))
			dir = dir+"/";
		try {
			String cmd = "chmod " +mod +" " + dir + file;
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
			Log.d(TAG,"success");
			return true;

		} catch (IOException ioe) {
			Log.d(TAG,"failed");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Log.d(TAG,"failed");
		}
		
		Log.d(TAG,"trying /system/bin/chmod");
		try {
			String[] cmd = { "/system/bin/chmod", mod,
					dir + file };
			File location = new File("/system/bin/");
			Process p = Runtime.getRuntime().exec(cmd, null, location);
			p.waitFor();
			Log.d(TAG,"success");
			return true;

		} catch (IOException ioe) {
			Log.d(TAG,"failed");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Log.d(TAG,"failed");
		}

		
		Log.d(TAG,"trying /system/xbin/chmod");		
		try {
			String[] cmd = { "/system/xbin/chmod", mod,
					dir + file };
			File location = new File("/system/xbin/");
			Process p = Runtime.getRuntime().exec(cmd, null, location);
			p.waitFor();
			Log.d(TAG,"success");
			return true;

		} catch (IOException ioe) {
			Log.d(TAG,"failed");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Log.d(TAG,"failed");
		}
		

		Log.d(TAG,"trying /system/xbin/bb/chmod");		
		try {
			String[] cmd = { "/system/xbin/bb/chmod", "775",
					dir + file };
			File location = new File("/system/xbin/bb");
			Process p = Runtime.getRuntime().exec(cmd, null, location);
			p.waitFor();
			Log.d(TAG,"success");
			return true;
			
		} catch (IOException ioe) {
			Log.d(TAG,"failed");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Log.d(TAG,"failed");
		}

		Log.d(TAG,"trying /system/xbin/busybox chmod");		
		try {
			String[] cmd = { "/system/xbin/busybox", "chmod", "775",
					dir + file };
			File location = new File("/system/xbin");
			Process p = Runtime.getRuntime().exec(cmd, null, location);
			p.waitFor();
			Log.d(TAG,"success");
			return true;

		} catch (IOException ioe) {
			Log.d(TAG,"failed");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Log.d(TAG,"failed");
		}

		Log.d(TAG,"trying /system/sbin/chmod");		
		try {
			String[] cmd = { "/system/sbin/chmod", "775",
					dir + file };
			File location = new File("/system/sbin");
			Process p = Runtime.getRuntime().exec(cmd, null, location);
			p.waitFor();
			Log.d(TAG,"success");
			return true;
			
		} catch (IOException ioe) {
			Log.d(TAG,"failed");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Log.d(TAG,"failed");
		}

		
		return false;
	}

	public String readAsset(Context ctx, String filename) {
		InputStream inStream = null;
		String result = "";
		try {
			// inStream = new FileInputStream(filename);
			inStream = assetManager.open(filename);
			if (inStream != null) {
				ByteArrayOutputStream content = new ByteArrayOutputStream();

				// Read response into a buffered stream
				int readBytes = 0;
				byte[] sBuffer = new byte[512];
				while ((readBytes = inStream.read(sBuffer)) != -1) {
					content.write(sBuffer, 0, readBytes);
				}
				inStream.close();
				// Return result from buffered stream
				result = new String(content.toByteArray());
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return "";
	}
	
}
