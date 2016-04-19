package com.txbox.settings.impl;

import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import com.txbox.settings.bean.GameListBean;
import com.txbox.settings.bean.LoginBean;
import com.txbox.settings.bean.MusicBackPicBean;
import com.txbox.settings.bean.MusicBackPicItemBean;
import com.txbox.settings.bean.NetTestBean;
import com.txbox.settings.bean.NetTestItemBean;
import com.txbox.settings.bean.NtpServerBean;
import com.txbox.settings.bean.NtpServerItemBean;
import com.txbox.settings.bean.SetCfgResultBean;
import com.txbox.settings.bean.SpeedTestBean;
import com.txbox.settings.bean.SpeedTestItemBean;
import com.txbox.settings.bean.WeatherInterfaceBean;
import com.txbox.settings.bean.WeatherInterfaceItemBean;
import com.txbox.settings.report.GlobalInfo;
import com.txbox.settings.utils.ConfigManager;
import com.txbox.settings.utils.Utils;

import android.content.Context;
import android.os.AsyncTask;



/**
 * 
 * @类描述：配置系统接口实现类
 * @项目名称：TXBootSettings
 * @包名： com.example.txbootsettings.impl
 * @类名称：SyscfgImpl	
 * @创建人：Administrator
 * @创建时间：2014-9-9下午2:05:18	
 * @修改人：Administrator
 * @修改时间：2014-9-9下午2:05:18	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class SyscfgImpl extends GsonApi{
	private Context context;
	private SetCfgListener mSetCfgListerner;
	private GetSpeedTestListener mGetSpeedTestListener;
	private GetMusicBackListener mGetMusicBackListener;
	private GetNetTestListener mGetNetTestListener;
	private GetNtpServerListener mGetNtpServerListener;
	private GetWeatherInterfaceListener mGetWeatherInterfaceListener;
	private GetGameListInterfaceListener mGameListInterfaceListener;
	
	/**
	 * 
	 * @类描述：设置配置接口回调接口
	 * @项目名称：TXBootSettings
	 * @包名： com.txbox.settings.impl
	 * @类名称：SetCfgListener	
	 * @创建人：Administrator
	 * @创建时间：2014-9-28上午11:46:28	
	 * @修改人：Administrator
	 * @修改时间：2014-9-28上午11:46:28	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright go3c
	 * @mail 939757170@qq.com
	 */
	public static interface SetCfgListener {
		public void onSetCfgFinished(SetCfgResultBean resultbean);
		
	}
	/**
	 * 
	 * @类描述：获取网速测试接口回调接口
	 * @项目名称：TXBootSettings
	 * @包名： com.txbox.settings.impl
	 * @类名称：GetSpeedTestListener	
	 * @创建人：Administrator
	 * @创建时间：2014-9-28上午11:45:44	
	 * @修改人：Administrator
	 * @修改时间：2014-9-28上午11:45:44	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright go3c
	 * @mail 939757170@qq.com
	 */
	public static interface GetSpeedTestListener {
		public void onGetSpeedTestFinished(SpeedTestItemBean speedtestbean);
	}
	
	/**
	 * 
	 * @类描述：获取音乐背景接口回调方法
	 * @项目名称：TXBootSettings
	 * @包名： com.txbox.settings.impl
	 * @类名称：GetMusicBackListener	
	 * @创建人：Administrator
	 * @创建时间：2014-9-28上午11:44:58	
	 * @修改人：Administrator
	 * @修改时间：2014-9-28上午11:44:58	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright go3c
	 * @mail 939757170@qq.com
	 */
	public static interface GetMusicBackListener {
		public void onGetMusicBackFinished(MusicBackPicItemBean muaicbackpicbean);
	}
	/**
	 * 
	 * @类描述：获取网速测试接口回调方法
	 * @项目名称：TXBootSettings
	 * @包名： com.txbox.settings.impl
	 * @类名称：GetNetTestListener	
	 * @创建人：Administrator
	 * @创建时间：2014-9-28上午11:42:28	
	 * @修改人：Administrator
	 * @修改时间：2014-9-28上午11:42:28	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright pivos
	 * @mail 939757170@qq.com
	 */
	public  interface GetNetTestListener {
		public void onGetNetTestFinished(NetTestItemBean itembean);
	}
	
	/**
	 * 
	 * @类描述：获取NTP服务器接口回调方法
	 * @项目名称：TXBootSettings
	 * @包名： com.txbox.settings.impl
	 * @类名称：GetNtpServerListener	
	 * @创建人：Administrator
	 * @创建时间：2014-9-28上午11:41:59	
	 * @修改人：Administrator
	 * @修改时间：2014-9-28上午11:41:59	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright pivos
	 * @mail 939757170@qq.com
	 */
	public  interface GetNtpServerListener {
		public void onGetNtpServerFinished(NtpServerItemBean itembean);
	}
	
	/**
	 * 
	 * @类描述：获取天气接口回调方法
	 * @项目名称：TXBootSettings
	 * @包名： com.txbox.settings.impl
	 * @类名称：GetWeatherInterfaceListener	
	 * @创建人：Administrator
	 * @创建时间：2014-9-28上午11:41:10	
	 * @修改人：Administrator
	 * @修改时间：2014-9-28上午11:41:10	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright pivos
	 * @mail 939757170@qq.com
	 */
	public  interface GetWeatherInterfaceListener {
		public void onGetWeatherInterfaceFinished(WeatherInterfaceItemBean itembean);
	}

	public interface GetGameListInterfaceListener{
		public void onGetGameListInterfaceFinished(GameListBean gameList);
	}
	public SyscfgImpl(Context context) {
		this.context = context;
	}
	
	public void SetCfgListener(SetCfgListener listener){
		mSetCfgListerner = listener;
	}
	public void SetSpeedTestListener(GetSpeedTestListener listener){
		mGetSpeedTestListener = listener;
	}
	public void SetMusicBackListener(GetMusicBackListener listener){
		mGetMusicBackListener = listener;
	}
	public void SetNetTestListener(GetNetTestListener listener){
		mGetNetTestListener = listener;
	}
	public void SetNtpServerListener(GetNtpServerListener listener){
		mGetNtpServerListener = listener;
	}
	public void SetWeatherInterfaceListener(GetWeatherInterfaceListener listener){
		mGetWeatherInterfaceListener = listener;
	}
	public void setGameListInterfaceListener(GetGameListInterfaceListener liseter){
		mGameListInterfaceListener = liseter;
	}
	/**
	 * 
	 * @描述: 设置升级配置信息
	 * @方法名: SetUpdateCfg
	 * @param server
	 * @param tv_type
	 * @param cfg_name
	 * @param guid
	 * @param logininfo
	 * @param value
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-9下午3:02:07	
	 * @修改人 Administrator
	 * @修改时间 2014-9-9下午3:02:07	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public void SetUpdateCfg(String server,String tv_type,String cfg_name,String guid,LoginBean logininfo,String value,String guid_tvskey,String token){
		//String url = "set_cfg?tv_type="+tv_type+"&cfg_name="+cfg_name+"&guid="+guid+"&value="+value+"&format=json";
		String qua = GlobalInfo.getQua(ConfigManager.DEFAULT_PR);
		String url = server+"/set_cfg"+"?Q-UA=" + qua;
		//user_info暂时都设置为空
		String cookis = "guid_tvskey="+guid_tvskey+";guid="+guid;
		String[] params = { url.toString(),cookis,tv_type, cfg_name, guid,"{}","json",value,token};
		
		SetUpdateCfgAsync setAsync = new SetUpdateCfgAsync();
		setAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
	}
	/**
	 * 
	 * @描述:获取测速链接
	 * @方法名: GetSpeetTestInfo
	 * @param server
	 * @param tv_type
	 * @param guid
	 * @param logininfo
	 * @param cfg_name
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-9下午5:21:58	
	 * @修改人 Administrator
	 * @修改时间 2014-9-9下午5:21:58	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public void GetSpeetTestInfo(String server,String tv_type,String guid,LoginBean logininfo,String cfg_name,String version){
		//String url = "/get_cfg?tv_type="+tv_type+"&guid="+guid+"&user_info="+"test"+"&cfg_names="+cfg_name+"&format=json";
		String qua = GlobalInfo.getQua(ConfigManager.DEFAULT_PR);
		String url = server+"/get_cfg"+"?Q-UA=" + qua;
		//user_info暂时都设置为"{}"
		String[] params = { url.toString(),tv_type, cfg_name, guid,"{}","json",version};
		GetSpeedTestAsync getspeedtestAsync = new GetSpeedTestAsync();
		getspeedtestAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);
	}
	/**
	 * 
	 * @描述:获取音乐背景图片
	 * @方法名: GetMusciBackInfo
	 * @param server
	 * @param tv_type
	 * @param guid
	 * @param logininfo
	 * @param cfg_name
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-9下午5:22:19	
	 * @修改人 Administrator
	 * @修改时间 2014-9-9下午5:22:19	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public void GetMusciBackInfo(String server,String tv_type,String guid,LoginBean logininfo,String cfg_name,String version){
		//String url = "/get_cfg?tv_type="+tv_type+"&guid="+guid+"&user_info="+"test"+"&cfg_names="+cfg_name+"&format=json";
		String qua = GlobalInfo.getQua(ConfigManager.DEFAULT_PR);
		String url = server+"/get_cfg"+"?Q-UA=" + qua;
		//user_info暂时都设置为"{}"
		String[] params = { url.toString(),tv_type, cfg_name, guid,"{}","json",version};
		GetMusicBackPicAsync getmusicbackAsync = new GetMusicBackPicAsync();
		getmusicbackAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);
	}
	
	/**
	 * 
	 * @描述: 获取网络联通性测试连接
	 * @方法名: GetNetTestInfo
	 * @param server
	 * @param tv_type
	 * @param guid
	 * @param logininfo
	 * @param cfg_name
	 * @param version
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-24上午10:56:12	
	 * @修改人 Administrator
	 * @修改时间 2014-9-24上午10:56:12	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public void GetNetTestInfo(String server,String tv_type,String guid,LoginBean logininfo,String cfg_name,String version){
		String qua = GlobalInfo.getQua(ConfigManager.DEFAULT_PR);
		GetNetTestAsync netTestAsync = new GetNetTestAsync();
		String url = server+"/get_cfg"+"?Q-UA=" + qua;
		String[] params = { url.toString(),tv_type, cfg_name, guid,"{}","json",version};
		netTestAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);
	}
	
	/**
	 * 
	 * @描述:获取NTP服务器列表
	 * @方法名: GetNtpServerInfo
	 * @param server
	 * @param tv_type
	 * @param guid
	 * @param logininfo
	 * @param cfg_name
	 * @param version
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-24上午10:57:29	
	 * @修改人 Administrator
	 * @修改时间 2014-9-24上午10:57:29	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public void GetNtpServerInfo(String server,String tv_type,String guid,LoginBean logininfo,String cfg_name,String version)
	{
		String qua = GlobalInfo.getQua(ConfigManager.DEFAULT_PR);
		GetNtpServerAsync netServerAsync = new GetNtpServerAsync();
		String url = server+"/get_cfg"+"?Q-UA=" +qua;
		String[] params = { url.toString(),tv_type, cfg_name, guid,"{}","json",version};
		netServerAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);
	}
	
	/**
	 * 
	 * @描述:获取天气相关接口地址
	 * @方法名: GetWeatherInfo
	 * @param server
	 * @param tv_type
	 * @param guid
	 * @param logininfo
	 * @param cfg_name
	 * @param version
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-24上午10:58:14	
	 * @修改人 Administrator
	 * @修改时间 2014-9-24上午10:58:14	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public void GetWeatherInfo(String server,String tv_type,String guid,LoginBean logininfo,String cfg_name,String version)
	{
		String qua = GlobalInfo.getQua(ConfigManager.DEFAULT_PR);
		GetWeatherInterfaceAsync WeatherAsync = new GetWeatherInterfaceAsync();
		String url = server+"/get_cfg"+"?Q-UA=" + qua;
		String[] params = { url.toString(),tv_type, cfg_name, guid,"{}","json",version};
		WeatherAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);
	}
	
	public void GetGameListInfo(String server){
		String qua = GlobalInfo.getQua(ConfigManager.DEFAULT_PR);
		server+="&Q-UA="+qua;
		GetGameListAsync gameAsync = new GetGameListAsync();
		gameAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, server);
	}
	/**
	 * 
	 * @描述:保存播放SDK代理地址，端口到文件
	 * @方法名: SavePlayProxyAddress
	 * @param address
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-24下午6:42:58	
	 * @修改人 Administrator
	 * @修改时间 2014-9-24下午6:42:58	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public static void SavePlayProxyAddress(String path,String address,String platform){
		JSONObject object = new JSONObject();// 一个user对象，使用一个JSONObject对象来装
		try {
			object.put("LocalHttpSvrAddr", address);
			object.put("platform", platform);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String jsonString = null;
		jsonString = object.toString(); // 把JSONObject转换成json格式的字符串
		Utils.saveStringToFile(path, jsonString);
	}
	
	
	/**
	 * 
	 * @类描述：异步设置升级配置信息（POST 方式）
	 * @项目名称：TXBootSettings
	 * @包名： com.example.txbootsettings.impl
	 * @类名称：SetUpdateCfgAsync	
	 * @创建人：Administrator
	 * @创建时间：2014-9-9下午3:01:52	
	 * @修改人：Administrator
	 * @修改时间：2014-9-9下午3:01:52	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright pivos
	 * @mail 939757170@qq.com
	 */
	class SetUpdateCfgAsync extends AsyncTask<String, Integer, SetCfgResultBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected SetCfgResultBean doInBackground(String... params) {
			String url = params[0];
			String cookies = params[1];
			String[] objName = {"tv_type", "cfg_name", "guid", "user_info", "format", "value","g_token"};
			Object[] obj = Arrays.copyOfRange(params, 2, 9);
			if(obj!=null){
				System.out.println("objName.length=" + objName.length + "obj.length=" + obj.length);
				String json = getJsonByPost(url, objName, obj,cookies);
				if (json.equals(ERROR))
					return null;
				try {
					SetCfgResultBean commonReturn = getGson().fromJson(json, SetCfgResultBean.class);
					return commonReturn;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(SetCfgResultBean result) {
			if (mSetCfgListerner != null)
				mSetCfgListerner.onSetCfgFinished(result);
		}
	}
	
	/**
	 * 
	 * @类描述：异步获取测速链接
	 * @项目名称：TXBootSettings
	 * @包名： com.example.txbootsettings.impl
	 * @类名称：GetSpeedTestAsync	
	 * @创建人：Administrator
	 * @创建时间：2014-9-9下午5:16:25	
	 * @修改人：Administrator
	 * @修改时间：2014-9-9下午5:16:25	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright pivos
	 * @mail 939757170@qq.com
	 */
	class GetSpeedTestAsync extends AsyncTask<String, Integer, SpeedTestItemBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected SpeedTestItemBean doInBackground(String... params) {
			String url = params[0];
			String[] objName = {"tv_type", "cfg_names", "guid", "user_info", "format","version"};
			Object[] obj = Arrays.copyOfRange(params, 1, 7);
			
			String json = getJsonByPost(url, objName, obj,null);
			if (json.equals(ERROR))
				return null;
			try{
				SpeedTestBean commonReturn = getGson().fromJson(json, SpeedTestBean.class);
				if(commonReturn!=null && commonReturn.getResult().getCode()==0){
					SpeedTestItemBean testItemBean = getGson().fromJson(commonReturn.getData().getSpeed_test_url(), SpeedTestItemBean.class);
					return testItemBean;
				}
				return null;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(SpeedTestItemBean result) {
			if (mGetSpeedTestListener != null)
				mGetSpeedTestListener.onGetSpeedTestFinished(result);
		}
	}
	
	/**
	 * 
	 * @类描述：异步获取音乐背景图片
	 * @项目名称：TXBootSettings
	 * @包名： com.example.txbootsettings.impl
	 * @类名称：GetMusicBackPicAsync	
	 * @创建人：Administrator
	 * @创建时间：2014-9-9下午5:17:06	
	 * @修改人：Administrator
	 * @修改时间：2014-9-9下午5:17:06	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright pivos
	 * @mail 939757170@qq.com
	 */
	class GetMusicBackPicAsync extends AsyncTask<String, Integer, MusicBackPicItemBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected MusicBackPicItemBean doInBackground(String... params) {
			String url = params[0];
			String[] objName = {"tv_type", "cfg_names", "guid", "user_info", "format","version"};
			Object[] obj = Arrays.copyOfRange(params, 1, 7);
			
			String json = getJsonByPost(url, objName, obj,null);
			if (json.equals(ERROR))
				return null;
			try{
				MusicBackPicBean commonReturn = getGson().fromJson(json, MusicBackPicBean.class);
				if(commonReturn!=null && commonReturn.getResult().getCode()==0){
					MusicBackPicItemBean itemBean = getGson().fromJson(commonReturn.getData().getMusic_back_pic(), MusicBackPicItemBean.class);
					return itemBean;
				}
				return null;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(MusicBackPicItemBean result) {
			if (mGetMusicBackListener != null)
				mGetMusicBackListener.onGetMusicBackFinished(result);
		}
	}
	
	/**
	 * 
	 * @类描述：异步获取网络联通测试连接
	 * @项目名称：TXBootSettings
	 * @包名： com.txbox.settings.impl
	 * @类名称：GetConnectTestAsync	
	 * @创建人：Administrator
	 * @创建时间：2014-9-17下午3:16:47	
	 * @修改人：Administrator
	 * @修改时间：2014-9-17下午3:16:47	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright pivos
	 * @mail 939757170@qq.com
	 */
	class GetNetTestAsync extends AsyncTask<String, Integer, NetTestItemBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected NetTestItemBean doInBackground(String... params) {
			String url = params[0];
			String[] objName = {"tv_type", "cfg_names", "guid", "user_info", "format","version"};
			Object[] obj = Arrays.copyOfRange(params, 1, 7);
			
			String json = getJsonByPost(url, objName, obj,null);
			if (json.equals(ERROR))
				return null;
			try{
				NetTestBean commonReturn = getGson().fromJson(json, NetTestBean.class);
				if(commonReturn!=null && commonReturn.getResult().getCode()==0){
					NetTestItemBean itemBean = getGson().fromJson(commonReturn.getData().getTest_net(), NetTestItemBean.class);
					return itemBean;
				}
				return null;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(NetTestItemBean result) {
			if (mGetNetTestListener != null)
				mGetNetTestListener.onGetNetTestFinished(result);
		}
	}
	
	/**
	 * 
	 * @类描述：异步获取天气接口地址
	 * @项目名称：TXBootSettings
	 * @包名： com.txbox.settings.impl
	 * @类名称：GetWeatherInterfaceAsync	
	 * @创建人：Administrator
	 * @创建时间：2014-9-25下午3:17:56	
	 * @修改人：Administrator
	 * @修改时间：2014-9-25下午3:17:56	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright go3c
	 * @mail 939757170@qq.com
	 */
	class GetWeatherInterfaceAsync extends AsyncTask<String, Integer, WeatherInterfaceItemBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected WeatherInterfaceItemBean doInBackground(String... params) {
			String url = params[0];
			String[] objName = {"tv_type", "cfg_names", "guid", "user_info", "format","version"};
			Object[] obj = Arrays.copyOfRange(params, 1, 7);
			
			String json = getJsonByPost(url, objName, obj,null);
			if (json.equals(ERROR))
				return null;
			try{
				WeatherInterfaceBean commonReturn = getGson().fromJson(json, WeatherInterfaceBean.class);
				if(commonReturn!=null && commonReturn.getResult().getCode()==0){
					WeatherInterfaceItemBean itemBean = getGson().fromJson(commonReturn.getData().getWeather(), WeatherInterfaceItemBean.class);
					return itemBean;
				}
				return null;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(WeatherInterfaceItemBean result) {
			if (mGetWeatherInterfaceListener != null)
				mGetWeatherInterfaceListener.onGetWeatherInterfaceFinished(result);
		}
	}
	
	/**
	 * 
	 * @类描述：异步获NTP 服务器列表
	 * @项目名称：TXBootSettings
	 * @包名： com.txbox.settings.impl
	 * @类名称：GetNtpServerAsync	
	 * @创建人：Administrator
	 * @创建时间：2014-9-25下午3:19:15	
	 * @修改人：Administrator
	 * @修改时间：2014-9-25下午3:19:15	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright go3c
	 * @mail 939757170@qq.com
	 */
	class GetNtpServerAsync extends AsyncTask<String, Integer, NtpServerItemBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected NtpServerItemBean doInBackground(String... params) {
			String url = params[0];
			String[] objName = {"tv_type", "cfg_names", "guid", "user_info", "format","version"};
			Object[] obj = Arrays.copyOfRange(params, 1, 7);
			
			String json = getJsonByPost(url, objName, obj,null);
			if (json.equals(ERROR))
				return null;
			try{
				NtpServerBean commonReturn = getGson().fromJson(json, NtpServerBean.class);
				if(commonReturn!=null && commonReturn.getResult().getCode()==0){
					NtpServerItemBean itemBean = getGson().fromJson(commonReturn.getData().getNtp(), NtpServerItemBean.class);
					return itemBean;
				}
				return null;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(NtpServerItemBean result) {
			if (mGetNtpServerListener != null)
				mGetNtpServerListener.onGetNtpServerFinished(result);
		}
	}
	class GetGameListAsync extends AsyncTask<String, Integer, GameListBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected GameListBean doInBackground(String... params) {
			String url = params[0];
			
			String json = getJsonByGet(url, null);
			if (json.equals(ERROR))
				return null;
			try{
				GameListBean gameListReturn = getGson().fromJson(json, GameListBean.class);
				if(gameListReturn!=null && gameListReturn.getData()!=null){
					return gameListReturn;
				}
				return null;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(GameListBean result) {
			if (mGameListInterfaceListener!= null)
				mGameListInterfaceListener.onGetGameListInterfaceFinished(result);
		}
	}
}
