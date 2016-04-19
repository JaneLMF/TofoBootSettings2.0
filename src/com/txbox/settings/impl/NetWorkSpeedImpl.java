package com.txbox.settings.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.txbox.settings.bean.NetWorkSpeedInfo;
import com.txbox.settings.interfaces.INetworkSpeedImpl;




/**
 * 
 * @类描述：网络测速实现类
 * @项目名称：TXBootSettings
 * @包名： com.example.txbootsettings.utils
 * @类名称：NetWorkSpeedTest	
 * @创建人：Administrator
 * @创建时间：2014-9-10下午9:35:19	
 * @修改人：Administrator
 * @修改时间：2014-9-10下午9:35:19	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class NetWorkSpeedImpl {
	private Context context;
	private INetworkSpeedImpl mlisterner;
	private String murl="";
	private NetWorkSpeedInfo netWorkSpeedInfo = null;
	private SpeedTestTask speedTestTask = null;
	public NetWorkSpeedImpl(Context context, String url) {
		this.context = context;
		this.murl = url;
	}
	/**
	 * 
	 * @描述: 设置测速回调方法
	 * @方法名: setOnChangeListener
	 * @param listener
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-10下午9:46:25	
	 * @修改人 Administrator
	 * @修改时间 2014-9-10下午9:46:25	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public void setOnChangeListener(INetworkSpeedImpl listener){
		this.mlisterner = listener;
	}
	/**
	 * 
	 * @描述:开始网络测速
	 * @方法名: StartSpeedTest
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-10下午9:46:56	
	 * @修改人 Administrator
	 * @修改时间 2014-9-10下午9:46:56	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public void StartSpeedTest(){
		netWorkSpeedInfo = new NetWorkSpeedInfo();
		speedTestTask = new SpeedTestTask();
		speedTestTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, murl);
	}
	/**
	 * 
	 * @描述:停止网络测速
	 * @方法名: StopSpeedTest
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-10下午9:47:26	
	 * @修改人 Administrator
	 * @修改时间 2014-9-10下午9:47:26	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public void StopSpeedTest(){
		if(speedTestTask!=null){
			speedTestTask.cancel(true);
		}
	}
	
	/**
	 * 
	 * @类描述：网络测速异步类
	 * @项目名称：TXBootSettings
	 * @包名： com.example.txbootsettings.impl
	 * @类名称：SpeedTestTask	
	 * @创建人：Administrator
	 * @创建时间：2014-9-10下午9:59:32	
	 * @修改人：Administrator
	 * @修改时间：2014-9-10下午9:59:32	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright pivos
	 * @mail 939757170@qq.com
	 */
	public class SpeedTestTask extends AsyncTask<Object, Integer, File> {
		private int UNIT = 1;
		private long startTime = 0;
		private long intervalTime = 0;
		private long lastTime = 0;
		private int readlen = 0;
		@Override
		protected File doInBackground(Object... params) {
			try {
				String path = (String) params[0];
				System.out.println("speed test url="+path);
				startTime = System.currentTimeMillis();
				lastTime = startTime;
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(5000);
				conn.setReadTimeout(5000);
				// 文件大小
				int length = conn.getContentLength();
				netWorkSpeedInfo.totalBytes = length;
				UNIT = length / 100;
				InputStream in = conn.getInputStream();
				byte[] buffer = new byte[10240];
				int len = 0;
				while ((len = in.read(buffer)) >0&&isCancelled()==false) {
			
					onProgressUpdate(len);
				}
				in.close();
				return null;
			} catch (IOException e) {
				Log.e("test", e.getMessage(), e);
				cancel(true);
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
		}

		@Override
		protected void onPostExecute(File result) {
			super.onPostExecute(result);

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			int len = values[0];
			readlen+=len;
			netWorkSpeedInfo.hadFinishedBytes+=len;
			netWorkSpeedInfo.downloadPercent = (int) ((netWorkSpeedInfo.hadFinishedBytes)/UNIT);
			long curTime = System.currentTimeMillis();
			intervalTime = curTime - startTime;
			//System.out.println("intervalTime = " + intervalTime);
			if (intervalTime == 0) {
				netWorkSpeedInfo.avgspeed = 1000;
			} else {
				netWorkSpeedInfo.avgspeed = (netWorkSpeedInfo.hadFinishedBytes / intervalTime) * 1000;
			}
			
			if(mlisterner!=null){
				if(netWorkSpeedInfo.downloadPercent>=100 || (curTime-lastTime)>1000){
					netWorkSpeedInfo.curspeed = (readlen/(curTime-lastTime))*1000;
					lastTime = curTime;
					mlisterner.onChange(netWorkSpeedInfo);
					readlen = 0;
				}
			}
		}
		@Override
		protected void onCancelled() {
		    super.onCancelled();
			// 删除文件
			
		}

	}
}
