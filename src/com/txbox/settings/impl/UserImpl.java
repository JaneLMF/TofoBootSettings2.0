package com.txbox.settings.impl;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.txbox.settings.bean.LoginBean;
import com.txbox.settings.bean.PayInfoBean;
import com.txbox.settings.bean.ResultBean;
import com.txbox.settings.bean.UserBean;
import com.txbox.settings.impl.AreaWeatherImpl.GetLbsListener;
import com.txbox.settings.report.GlobalInfo;
import com.txbox.settings.utils.ConfigManager;
import com.txbox.settings.utils.Utils;

import android.content.Context;
import android.os.AsyncTask;

/**
 * 
 * @类描述：用户登录相关实现类
 * @项目名称：TXBootSettings
 * @包名： com.example.txbootsettings.impl
 * @类名称：UserImpl	
 * @创建人：Administrator
 * @创建时间：2014-9-4下午7:50:45	
 * @修改人：Administrator
 * @修改时间：2014-9-4下午7:50:45	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class UserImpl extends GsonApi {
	private Context context;
	private IVipInfoImpl mGetVipInfoListener;
	private IPayInfoImpl mGetPayInfoListener;
	private IDelBindInfoImpl mDelBindInfoListener;

	public UserImpl(Context context) {
		this.context = context;
	}
	/**
	 * 
	 * @类描述：用户相关回调接口
	 * @项目名称：TXBootSettings
	 * @包名： com.example.txbootsettings.interfaces
	 * @类名称：IUserImpl	
	 * @创建人：Administrator
	 * @创建时间：2014-9-4下午12:30:13	
	 * @修改人：Administrator
	 * @修改时间：2014-9-4下午12:30:13	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright pivos
	 * @mail 939757170@qq.com
	 */
	public interface IVipInfoImpl {
		public void onVipInfoFinished(UserBean userbean);

	}
	
	public interface IPayInfoImpl {
		public void onPayInfoFinished(PayInfoBean payinfobean);

	}
	public interface IDelBindInfoImpl{
		public void onDelBindInfoFinished(ResultBean bean);
	}
	
	public void SetGetVipInfoListener(IVipInfoImpl listener){
		mGetVipInfoListener = listener;
	}
	public void SetGetPayInfoListener(IPayInfoImpl listener){
		mGetPayInfoListener = listener;
	}
	public void SetDelBindInfoListener(IDelBindInfoImpl listener){
		mDelBindInfoListener = listener;
	}
	/**
	 * 
	 * @描述:从本地获取登录信息
	 * @方法名: getLoginInfo
	 * @param path
	 * @param logininfo
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-4下午2:38:39	
	 * @修改人 Administrator
	 * @修改时间 2014-9-4下午2:38:39	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public LoginBean getLoginInfo(String path){
		StringBuilder json = new StringBuilder();
		try {
			json = Utils.getStringFromFile(path);
			if(json!=null && json.length()>0){
				LoginBean logininfo = getGson().fromJson(json.toString(), LoginBean.class);
				return logininfo;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 
	 * @描述:保存登录信息到文件，供其它程序使用
	 * @方法名: SaveLoginInfo
	 * @param nick
	 * @param face
	 * @param openid
	 * @param accesstoken
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-4上午9:30:13	
	 * @修改人 Administrator
	 * @修改时间 2014-9-4上午9:30:13	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public void SaveLoginInfo(String path,LoginBean logininfo){
		JSONObject object = new JSONObject();// 一个user对象，使用一个JSONObject对象来装
		try {
			object.put("nick", logininfo.getNick());
			object.put("face", logininfo.getFace());
			object.put("openid", logininfo.getOpenid());
			object.put("accesstoken", logininfo.getAccesstoken());
			object.put("appid", logininfo.getAppid());
			object.put("bid", logininfo.getBid());
			object.put("islogin", logininfo.getIslogin());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String jsonString = null;
		jsonString = object.toString(); // 把JSONObject转换成json格式的字符串
		Utils.saveStringToFile(path, jsonString);
	}
	
	/**
	 * 
	 * @描述:
	 * @方法名: getVipInfo
	 * @param server
	 * @param userid
	 * @param psd
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-4下午12:17:07	
	 * @修改人 Administrator
	 * @修改时间 2014-9-4下午12:17:07	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public void getVipInfo(String server,String appid,String openid,String accesstoken) {
		String qua = GlobalInfo.getQua(ConfigManager.DEFAULT_PR);
		String params = "/get_vip_info?access_token="+accesstoken+"&oauth_consumer_key="+appid + "&openid=" + openid + "&pf=qzone"+"&Q-UA=" + qua;
		GetVipInfoAsync vipinfoAsync = new GetVipInfoAsync();
		vipinfoAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,server,params);
	}
	
	public void delBindInfo(String server,String appid,String openid,String accesstoken,String guid,String bid){
		String qua = GlobalInfo.getQua(ConfigManager.DEFAULT_PR);
		String params = "/del_bind_info?access_token="+accesstoken+"&oauth_consumer_key="+appid + "&openid=" + openid + "&pf=qzone"+"&tvid="+guid + "&bid="+bid+"&Q-UA="+qua;
		DelBindInfoAsync delTask = new DelBindInfoAsync();
		delTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,server,params);
	}
	
	public void getPayInfo(String server,String appid,String openid,String accesstoken,String cid){
		String qua = GlobalInfo.getQua(ConfigManager.DEFAULT_PR);
		String params = "/get_pay_info?access_token="+accesstoken+"&oauth_consumer_key="+appid + "&openid=" + openid + "&pf=qzone"+"&cid="+cid+"&Q-UA="+qua;
		GetPayInfoInfoAsync payinfoTask = new GetPayInfoInfoAsync();
		payinfoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,server,params);
	}
	
	/**
	 * 
	 * @类描述：异步获取会员信息类
	 * @项目名称：TXBootSettings
	 * @包名： com.example.txbootsettings.impl
	 * @类名称：GetVipInfoAsync	
	 * @创建人：Administrator
	 * @创建时间：2014-9-4下午12:15:15	
	 * @修改人：Administrator
	 * @修改时间：2014-9-4下午12:15:15	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright pivos
	 * @mail 939757170@qq.com
	 */
	class GetVipInfoAsync extends AsyncTask<String, Integer, UserBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected UserBean doInBackground(String... params) {
			String server = params[0];
			String url = params[1];
			String json = getJsonByGet(server+url,null);
			if (json.equals(ERROR))
				return null;
			try{
				UserBean commonReturn = getGson().fromJson(json, UserBean.class);
				return commonReturn;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(UserBean result) {
			if (mGetVipInfoListener != null)
				mGetVipInfoListener.onVipInfoFinished(result);
		}
	}
	/**
	 * 
	 * @类描述：解除QQ和tvid的绑定关系
	 * @项目名称：TXBootSettings
	 * @包名： com.txbox.settings.impl
	 * @类名称：DelBindInfoAsync	
	 * @创建人：Administrator
	 * @创建时间：2014-10-11下午5:02:40	
	 * @修改人：Administrator
	 * @修改时间：2014-10-11下午5:02:40	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright pivos
	 * @mail 939757170@qq.com
	 */
	class DelBindInfoAsync extends AsyncTask<String, Integer, ResultBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected ResultBean doInBackground(String... params) {
			String server = params[0];
			String url = params[1];
			String json = getJsonByGet(server+url,null);
			if (json.equals(ERROR))
				return null;
			try{
				ResultBean commonReturn = getGson().fromJson(json, ResultBean.class);
				return commonReturn;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ResultBean result) {
			if (mDelBindInfoListener != null)
				mDelBindInfoListener.onDelBindInfoFinished(result);
		}
	}
	/**
	 * 
	 * @类描述：异步获取单片付费情况
	 * @项目名称：TXBootSettings
	 * @包名： com.txbox.settings.impl
	 * @类名称：GetPayInfoInfoAsync	
	 * @创建人：Administrator
	 * @创建时间：2014-10-11下午5:03:51	
	 * @修改人：Administrator
	 * @修改时间：2014-10-11下午5:03:51	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright pivos
	 * @mail 939757170@qq.com
	 */
	class GetPayInfoInfoAsync extends AsyncTask<String, Integer, PayInfoBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected PayInfoBean doInBackground(String... params) {
			String server = params[0];
			String url = params[1];
			String json = getJsonByGet(server+url,null);
			if (json.equals(ERROR))
				return null;
			try{
				PayInfoBean commonReturn = getGson().fromJson(json, PayInfoBean.class);
				return commonReturn;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(PayInfoBean result) {
			if (mGetPayInfoListener != null)
				mGetPayInfoListener.onPayInfoFinished(result);
		}
	}


}
