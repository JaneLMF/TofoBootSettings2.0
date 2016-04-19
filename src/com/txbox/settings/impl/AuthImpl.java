package com.txbox.settings.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.txbox.settings.bean.BindStatusBean;
import com.txbox.settings.bean.GuidBean;
import com.txbox.settings.bean.GuidDataBean;
import com.txbox.settings.bean.GuidInfoBean;
import com.txbox.settings.bean.LoginBean;
import com.txbox.settings.bean.OpenCGameBean;
import com.txbox.settings.bean.PivosAuthInfo;
import com.txbox.settings.bean.PivosLoginBean;
import com.txbox.settings.bean.RegisterBean;
import com.txbox.settings.bean.TvsKeyBean;
import com.txbox.settings.bean.UserBean;
import com.txbox.settings.impl.UserImpl.IVipInfoImpl;
import com.txbox.settings.interfaces.IAuthImpl;
import com.txbox.settings.report.GlobalInfo;
import com.txbox.settings.utils.ConfigManager;
import com.txbox.settings.utils.Utils;
import com.txbox.settings.bean.PivosAuthInfo;

import android.content.Context;
import android.os.AsyncTask;

/**
 * 
 * @类描述：鉴权票据相关实现类
 * @项目名称：TXBootSettings
 * @包名： com.example.txbootsettings.impl
 * @类名称：AuthImpl	
 * @创建人：Administrator
 * @创建时间：2014-9-4下午7:49:46	
 * @修改人：Administrator
 * @修改时间：2014-9-4下午7:49:46	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class AuthImpl extends GsonApi{
	private Context context;
	private IAuthImpl mlisterner;
	private IRegisterImpl mRegisterListener;
	private ILoginListenerImpl mLoginListener;
	private IOpenCGameListenerImpl mOpenCGameListener;
	private IBindStatusListenerImpl mBindStatusListener;
	public interface IRegisterImpl {
		public void onRegistrFinished(RegisterBean registrbean);

	}
	public interface ILoginListenerImpl {
		public void onLoginFinished(PivosLoginBean loginbean);
	}
	public interface IOpenCGameListenerImpl {
		public void onOpenFinished(OpenCGameBean openbean);
	}
	public interface IBindStatusListenerImpl {
		public void onGetBindStatusFinished(BindStatusBean bindStatusBean);
	}
	public void SetRegisterListener(IRegisterImpl listener){
		mRegisterListener = listener;
	}
	public void SetLoginListener(ILoginListenerImpl listener){
		mLoginListener = listener;
	}
	public void SetOpenCGameListener(IOpenCGameListenerImpl listener){
		mOpenCGameListener = listener;
	}
	public void SetGetBindStatusListener(IBindStatusListenerImpl listener){
		mBindStatusListener = listener;
	}
	public AuthImpl(Context context, IAuthImpl listerner) {
		this.context = context;
		this.mlisterner = listerner;
	}
	/**
	 * 
	 * @描述:获取唯一guid
	 * @方法名: GetGuid
	 * @param server
	 * @param tvid
	 * @param mac
	 * @param tvmd5
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-4下午8:26:01	
	 * @修改人 Administrator
	 * @修改时间 2014-9-4下午8:26:01	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public void GetGuid(String server,String devid,String mac,String devmd5,String gitvsn,String mac_wire){
		String qua = GlobalInfo.getQua(ConfigManager.DEFAULT_PR);
		String params = "/get_guid?version=1&format=json&device_id="+devid+"&mac_address="+mac+"&tofu_md5="+devmd5+"&sn="+gitvsn +"&mac_wire="+mac_wire+ "&Q-UA=" + qua;
		GetGuidAsync guidAsync = new GetGuidAsync();
		guidAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,server,params);
	}
	/**
	 * 
	 * @描述:获取临时登录态
	 * @方法名: GetTvsKey
	 * @param server
	 * @param guid
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-4下午8:26:45	
	 * @修改人 Administrator
	 * @修改时间 2014-9-4下午8:26:45	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public void GetTvsKey(String server,String guid,String ticket_client){
		String qua = GlobalInfo.getQua(ConfigManager.DEFAULT_PR);
		String params = "/get_tvskey?version=1&format=json&guid="+guid+"&ticket_client="+ticket_client + "&Q-UA=" + qua;
		GetTvsKeyAsync tvskeyAsync = new GetTvsKeyAsync();
		tvskeyAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,server,params);
	}
	
	/**
	 * 
	 * @描述: 保存guid到本地
	 * @方法名: SaveGuidInfo
	 * @param path
	 * @param data
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-10下午12:12:55	
	 * @修改人 Administrator
	 * @修改时间 2014-9-10下午12:12:55	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public void SaveGuidInfo(String path,GuidInfoBean data){
		JSONObject object = new JSONObject();// 一个user对象，使用一个JSONObject对象来装
		try {
			object.put("guid", data.getGuid());
			object.put("guid_secret", data.getGuid_secret());
			object.put("tvskey", data.getTvskey());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String jsonString = null;
		jsonString = object.toString(); // 把JSONObject转换成json格式的字符串
		Utils.saveStringToFile(path, jsonString);
	}
	
	/**
	 * 
	 * @描述: 从本地获取guid
	 * @方法名: GetGudiInfo
	 * @param path
	 * @return
	 * @返回类型 GuidDataBean
	 * @创建人 Administrator
	 * @创建时间 2014-9-10下午12:14:40	
	 * @修改人 Administrator
	 * @修改时间 2014-9-10下午12:14:40	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public GuidInfoBean GetGudiInfo(String path){
		StringBuilder json = new StringBuilder();
		try {
			json = Utils.getStringFromFile(path);
			if(json!=null && json.length()>0){
				GuidInfoBean data = getGson().fromJson(json.toString(), GuidInfoBean.class);
				return data;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public void SaveIcntvAuthInfo(String path,String ret, String deviceid){
		JSONObject object = new JSONObject();// 一个user对象，使用一个JSONObject对象来装
		try {
			object.put("ret", ret);
			object.put("deviceid", deviceid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String jsonString = null;
		jsonString = object.toString(); // 把JSONObject转换成json格式的字符串
		Utils.saveStringToFile(path, jsonString);
	}
	
	public void SavePivosAuthInfo(String path,String ret, String guid,String userid){
		JSONObject object = new JSONObject();// 一个user对象，使用一个JSONObject对象来装
		try {
			object.put("ret", ret);
			object.put("userid", userid);
			object.put("guid", guid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String jsonString = null;
		jsonString = object.toString(); // 把JSONObject转换成json格式的字符串
		Utils.saveStringToFile(path, jsonString);
	}
	public PivosAuthInfo GetPivosAuthInfo(String path){
		StringBuilder json = new StringBuilder();
		try {
			json = Utils.getStringFromFile(path);
			if(json!=null && json.length()>0){
				PivosAuthInfo data = getGson().fromJson(json.toString(), PivosAuthInfo.class);
				return data;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void PivosRegister(String server, String customerid, String clientid, String sign) {
		// sign = this.encrypt(sign);
		try {
			sign = URLEncoder.encode(sign, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringBuilder url = new StringBuilder(server);
		url.append("?m=Authentication.getrRegdev&Cid=");
		url.append(customerid);
		url.append("&Term=");
		url.append(clientid);
		url.append("&Sign=");
		url.append(sign);
		
		System.out.println("PivosRegister url = " + url);

		RegistereAsync registerAsync = new RegistereAsync();
		registerAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url.toString());
	}
	public void PivosGetGuid(String server, String devid,String ver,String mac,String devmd5,String gitvsn,String mac_wire){
		String params = "?m=Authorize.getGuidInfo"+"&device_id="+devid+"&appver="+ver+"&mac_address="+mac+"&tofu_md5="+devmd5+"&sn="+gitvsn +"&mac_wire="+mac_wire;
		System.out.println("PivosGetGuid url = " + params);
		GetGuidAsync guidAsync = new GetGuidAsync();
		guidAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,server,params);
	}
	public void PivosLogin(String server,String guid,String cardid) {
		String url = server+"?m=Login.getLoginInfo"+"&cardid="+cardid+"&guid="+guid;
		System.out.println("PivosLogin url = " + url);
		LoginAsync loginAsync = new LoginAsync();
		loginAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url.toString());
	}
	public void PivosBildAddress(String server,String cardid,String userid,String guid) {
		String url = server+"?m=Service.getBoxBild&userid="+userid+"&cardid="+cardid+"&guid="+guid;
		OpenCGameAsync openAsync = new OpenCGameAsync();
		openAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url.toString());
	}
	public void PivosGetBindStatus(String server,String mac) {
		String url = server+"?m=Service.getBoxBildStatus&mac_wire="+mac;
		GetBildStatusAsync openAsync = new GetBildStatusAsync();
		openAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url.toString());
	}
	public void PivosOpenGameSucess(String server,String userid,String guid,String cardid) {
		String url = server + "?m=Service.getOpenGame"+"&userid="+userid + "&guid=" + guid+"&cardid=" + cardid;
		OpenCGameAsync openAsync = new OpenCGameAsync();
		openAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url.toString());
	}
	
	/**
	 * 
	 * @类描述：异步获取guid类
	 * @项目名称：TXBootSettings
	 * @包名： com.example.txbootsettings.impl
	 * @类名称：GetGuidAsync	
	 * @创建人：Administrator
	 * @创建时间：2014-9-4下午8:29:57	
	 * @修改人：Administrator
	 * @修改时间：2014-9-4下午8:29:57	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright pivos
	 * @mail 939757170@qq.com
	 */
	class GetGuidAsync extends AsyncTask<String, Integer, GuidBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected GuidBean doInBackground(String... params) {
			String server = params[0];
			String url = params[1];
			String json = getJsonByGet(server+url,null);
			if (json.equals(ERROR))
				return null;
			try{
				GuidBean commonReturn = getGson().fromJson(json, GuidBean.class);
				return commonReturn;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(GuidBean result) {
			if (mlisterner != null)
				mlisterner.onGuidFinished(result);
		}
	}
	
	class GetTvsKeyAsync extends AsyncTask<String, Integer, TvsKeyBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected TvsKeyBean doInBackground(String... params) {
			String server = params[0];
			String url = params[1];
			String json = getJsonByGet(server+url,null);
			if (json.equals(ERROR))
				return null;
			try{
				TvsKeyBean commonReturn = getGson().fromJson(json, TvsKeyBean.class);
				return commonReturn;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(TvsKeyBean result) {
			if (mlisterner != null)
				mlisterner.onTvsKeyFinished(result);
		}
	}
	
	class RegistereAsync extends AsyncTask<String, Integer, RegisterBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected RegisterBean doInBackground(String... params) {
			String url = params[0];
			String json = getJsonByGet(url,null);
			if (json.equals(ERROR))
				return null;
			try{
				RegisterBean commonReturn = getGson().fromJson(json, RegisterBean.class);
				return commonReturn;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(RegisterBean result) {
			if (mRegisterListener != null)
				mRegisterListener.onRegistrFinished(result);
		}
	}
	
	class LoginAsync extends AsyncTask<String, Integer, PivosLoginBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected PivosLoginBean doInBackground(String... params) {
			String url = params[0];
			String json = getJsonByGet(url,null);
			if (json.equals(ERROR))
				return null;
			try{
				PivosLoginBean commonReturn = getGson().fromJson(json, PivosLoginBean.class);
				return commonReturn;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(PivosLoginBean result) {
			if (mLoginListener != null)
				mLoginListener.onLoginFinished(result);
		}
	}
	
	class OpenCGameAsync extends AsyncTask<String, Integer, OpenCGameBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected OpenCGameBean doInBackground(String... params) {
			String url = params[0];
			String json = getJsonByGet(url,null);
			if (json.equals(ERROR))
				return null;
			try{
				OpenCGameBean commonReturn = getGson().fromJson(json, OpenCGameBean.class);
				return commonReturn;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(OpenCGameBean result) {
			if (mOpenCGameListener != null)
				mOpenCGameListener.onOpenFinished(result);
		}
	}
	class GetBildStatusAsync extends AsyncTask<String, Integer, BindStatusBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected BindStatusBean doInBackground(String... params) {
			String url = params[0];
			String json = getJsonByGet(url,null);
			if (json.equals(ERROR))
				return null;
			try{
				BindStatusBean commonReturn = getGson().fromJson(json, BindStatusBean.class);
				return commonReturn;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(BindStatusBean result) {
			if (mBindStatusListener != null)
				mBindStatusListener.onGetBindStatusFinished(result);
		}
	}
	
}
