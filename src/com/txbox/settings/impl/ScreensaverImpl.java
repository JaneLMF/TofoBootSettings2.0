package com.txbox.settings.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;

import com.txbox.settings.interfaces.IScreensaverImpl;
import com.txbox.settings.utils.PathUtils;
import com.txbox.settings.utils.ZipUtils;

public class ScreensaverImpl {
	
	private IScreensaverImpl mListerner;
	private Context mContext;
	private ScreensaverAsync mScreensaverAsync;
	public ScreensaverImpl(Context mContext,IScreensaverImpl mListerner){
		this.mContext = mContext;
		this.mListerner = mListerner;
	}
	
	public void getScreensaver(String url){
		mScreensaverAsync = new ScreensaverAsync();
		mScreensaverAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
	}
	
	class ScreensaverAsync extends AsyncTask<String, Integer, Boolean>{

		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String path = arg0[0];
			File file = new File(PathUtils.SCREENSAVERINTERNETDIR);
			if(!file.exists()||!file.isDirectory()){
				file.mkdirs();
			}
			File f = new File(file, "defaultZIP.zip");
			URL url;
			try {
				url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(5000);
				conn.setReadTimeout(5000);
				// 文件大小
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				byte[] buffer = new byte[10240];
				int len;
				
				OutputStream os = new FileOutputStream(f);
				while ((len = is.read(buffer)) != -1) {
					os.write(buffer, 0, len);
				}
				os.close();
				is.close();
				
				//解压zip包
				ZipUtils.upZipFile(f, PathUtils.SCREENSAVERINTERNETDIR);
				return true;
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			mListerner.OnSaveResoult(result);
			super.onPostExecute(result);
		}
	}
	
	
}
