package com.txbox.settings.impl;

import com.txbox.settings.bean.LbsBean;
import com.txbox.settings.bean.NtpServerItemBean;
import com.txbox.settings.bean.QrcodeBean;
import com.txbox.settings.bean.SetCfgResultBean;
import com.txbox.settings.bean.Weather3DaysBean;
import com.txbox.settings.bean.WeatherItemBean;
import com.txbox.settings.bean.WeatherTodayBean;
import com.txbox.settings.impl.SyscfgImpl.GetMusicBackListener;
import com.txbox.settings.impl.SyscfgImpl.GetSpeedTestListener;
import com.txbox.settings.impl.SyscfgImpl.SetCfgListener;
import com.txbox.settings.report.GlobalInfo;
import com.txbox.settings.utils.ConfigManager;

import android.content.Context;
import android.os.AsyncTask;

public class AreaWeatherImpl extends GsonApi{
	private Context context;
	private GetLbsListener mGetLbsListener;
	private GetWeatherTodayListener mGetWeatherTodayListener;
	private GetWeather3DaysListener mGetWeather3DaysListener;
	
	/**
	 * 
	 * @类描述：获取地理位置回调接口
	 * @项目名称：TXBootSettings
	 * @包名： com.txbox.settings.impl
	 * @类名称：GetLbsListener	
	 * @创建人：Administrator
	 * @创建时间：2014-9-28下午2:50:04	
	 * @修改人：Administrator
	 * @修改时间：2014-9-28下午2:50:04	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright go3c
	 * @mail 939757170@qq.com
	 */
	public static interface GetLbsListener {
		public void onGetLbsFinished(LbsBean lbsbean);
		
	}
	/**
	 * 
	 * @类描述：获取当日天气回调接口
	 * @项目名称：TXBootSettings
	 * @包名： com.txbox.settings.impl
	 * @类名称：GetWeatherTodayListener	
	 * @创建人：Administrator
	 * @创建时间：2014-9-28下午3:10:45	
	 * @修改人：Administrator
	 * @修改时间：2014-9-28下午3:10:45	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright go3c
	 * @mail 939757170@qq.com
	 */
	public static interface GetWeatherTodayListener {
		public void onGetWeatherTodayFinished(WeatherTodayBean weathertodaybean);
		
	}
	/**
	 * 
	 * @类描述：获取未来3天天气回调接口
	 * @项目名称：TXBootSettings
	 * @包名： com.txbox.settings.impl
	 * @类名称：GetWeather3DaysListener	
	 * @创建人：Administrator
	 * @创建时间：2014-9-28下午3:11:14	
	 * @修改人：Administrator
	 * @修改时间：2014-9-28下午3:11:14	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright go3c
	 * @mail 939757170@qq.com
	 */
	public static interface GetWeather3DaysListener {
		public void onGetWeather3DaysFinished(Weather3DaysBean bean);
		
	}
	
	
	public void SetGetLbsListener(GetLbsListener listener){
		mGetLbsListener = listener;
	}
	public void SetWeatherTodayListener(GetWeatherTodayListener listener){
		mGetWeatherTodayListener = listener;
	}
	public void SetWeather3DaysListener(GetWeather3DaysListener listener){
		mGetWeather3DaysListener = listener;
	}
	
	
	public AreaWeatherImpl(Context context) {
		this.context = context;
	}
	
	/**
	 * 
	 * @描述:获取地理位置信息
	 * @方法名: GetLbsInfo
	 * @param server
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-10-11下午3:20:09	
	 * @修改人 Administrator
	 * @修改时间 2014-10-11下午3:20:09	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public void GetLbsInfo(String server,String guid,String guid_tvskey){
		String qua = GlobalInfo.getQua(ConfigManager.DEFAULT_PR);
		server += server+"?Q-UA=" + qua;
		String cookis = "guid_tvskey="+guid_tvskey+";guid="+guid;
		GetAreaAsync areaTask = new GetAreaAsync();
		areaTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,server,cookis);
	}
	/**
	 * 
	 * @描述:获取当天天气信息
	 * @方法名: GetWeatherToday
	 * @param server
	 * @param ID
	 * @param guid
	 * @param guid_tvskey
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-10-11下午3:20:27	
	 * @修改人 Administrator
	 * @修改时间 2014-10-11下午3:20:27	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public void GetWeatherToday(String server,String ID,String guid,String guid_tvskey)
	{
		String qua = GlobalInfo.getQua(ConfigManager.DEFAULT_PR);
		String params = "/today_weather?cityid="+ID + "&Q-UA=" + qua;
		String cookis = "guid_tvskey="+guid_tvskey+";guid="+guid;
		GetWeatherTodayAsync weatherTodayTask = new GetWeatherTodayAsync();
		weatherTodayTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,server,params,cookis);
		
	}
	/**
	 * 
	 * @描述:获取未来3天天气信息
	 * @方法名: GetWeather3Days
	 * @param server
	 * @param ID
	 * @param guid
	 * @param guid_tvskey
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-10-11下午3:20:48	
	 * @修改人 Administrator
	 * @修改时间 2014-10-11下午3:20:48	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public void GetWeather3Days(String server,String ID,String guid,String guid_tvskey){	
		String qua = GlobalInfo.getQua(ConfigManager.DEFAULT_PR);
		String params = "/next_3-days_weather?cityid="+ID + "&Q-UA=" + qua;
		String cookis = "guid_tvskey="+guid_tvskey+";guid="+guid;
		GetWeather3DaysAsync weather3DaysTask = new GetWeather3DaysAsync();
		weather3DaysTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,server,params,cookis);
	}
	
	/**
	 * 
	 * @类描述：异步获取地理位置信息
	 * @项目名称：TXBootSettings
	 * @包名： com.txbox.settings.impl
	 * @类名称：GetAreaAsync	
	 * @创建人：Administrator
	 * @创建时间：2014-9-28下午2:48:15	
	 * @修改人：Administrator
	 * @修改时间：2014-9-28下午2:48:15	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright pivos
	 * @mail 939757170@qq.com
	 */
	class GetAreaAsync extends AsyncTask<String, Integer, LbsBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected LbsBean doInBackground(String... params) {
			String server = params[0];
			//String url = params[1];
			String cookies = params[1];
			String json = getJsonByGet(server,cookies);
			if (json.equals(ERROR))
				return null;
			try{
				LbsBean commonReturn = getGson().fromJson(json, LbsBean.class);
				return commonReturn;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(LbsBean result) {
			if (mGetLbsListener != null)
				mGetLbsListener.onGetLbsFinished(result);
		}
	}
	
	/**
	 * 
	 * @类描述：异步获取当天天气信息
	 * @项目名称：TXBootSettings
	 * @包名： com.txbox.settings.impl
	 * @类名称：GetWeatherTodayAsync	
	 * @创建人：Administrator
	 * @创建时间：2014-10-11下午2:59:56	
	 * @修改人：Administrator
	 * @修改时间：2014-10-11下午2:59:56	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright pivos
	 * @mail 939757170@qq.com
	 */
	class GetWeatherTodayAsync extends AsyncTask<String, Integer, WeatherTodayBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected WeatherTodayBean doInBackground(String... params) {
			String server = params[0];
			String url = params[1];
			String cookies = params[2];
			String json = getJsonByGet(server+url,cookies);
			if (json.equals(ERROR))
				return null;
			try{
				WeatherTodayBean commonReturn = getGson().fromJson(json, WeatherTodayBean.class);
				return commonReturn;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(WeatherTodayBean result) {
			if (mGetWeatherTodayListener != null)
				mGetWeatherTodayListener.onGetWeatherTodayFinished(result);
		}
	}
	
	/**
	 * 
	 * @类描述：异步获取未来3天天气信息
	 * @项目名称：TXBootSettings
	 * @包名： com.txbox.settings.impl
	 * @类名称：GetWeather3DaysAsync	
	 * @创建人：Administrator
	 * @创建时间：2014-10-11下午3:00:23	
	 * @修改人：Administrator
	 * @修改时间：2014-10-11下午3:00:23	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright go3c
	 * @mail 939757170@qq.com
	 */
	class GetWeather3DaysAsync extends AsyncTask<String, Integer, Weather3DaysBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Weather3DaysBean doInBackground(String... params) {
			String server = params[0];
			String url = params[1];
			String cookies = params[2];
			String json = getJsonByGet(server+url,cookies);
			if (json.equals(ERROR))
				return null;
			try{
				Weather3DaysBean commonReturn = getGson().fromJson(json, Weather3DaysBean.class);
				return commonReturn;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Weather3DaysBean result) {
			if (mGetWeather3DaysListener != null)
				mGetWeather3DaysListener.onGetWeather3DaysFinished(result);
		}
	}
}
