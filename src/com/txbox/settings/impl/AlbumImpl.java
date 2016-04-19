package com.txbox.settings.impl;

import android.content.Context;
import android.os.AsyncTask;

import com.txbox.settings.bean.AlbumMembersBean;
import com.txbox.settings.bean.QrcodeBean;
import com.txbox.settings.bean.UserBean;
import com.txbox.settings.interfaces.IAlbumImpl;
import com.txbox.settings.report.GlobalInfo;
import com.txbox.settings.utils.ConfigManager;

/**
 * 
 * @类描述：相册接口实现类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.impl
 * @类名称：AlbumImpl	
 * @创建人：Administrator
 * @创建时间：2014-9-18下午8:27:57	
 * @修改人：Administrator
 * @修改时间：2014-9-18下午8:27:57	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class AlbumImpl extends GsonApi{
	private Context context;
	private IAlbumImpl mlisterner;
	public AlbumImpl(Context context, IAlbumImpl listerner) {
		this.context = context;
		this.mlisterner = listerner;
	}
	
	public void getAlbumQrcode(String server,String guid,String guid_tvskey){
		String qua = GlobalInfo.getQua(ConfigManager.DEFAULT_PR);
		String params = "/get_qrcode?version=1&format=json&guid="+guid+"&Q-UA=" + qua;
		String cookis = "guid_tvskey="+guid_tvskey+";guid="+guid;
		GetAlbumQrcodeAsync qrcodeTask = new GetAlbumQrcodeAsync();
		qrcodeTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,server,params,cookis);
	}
	public void getAlbumMembers(String server,String guid,String guid_tvskey){
		String qua = GlobalInfo.getQua(ConfigManager.DEFAULT_PR);
		String params = "/get_members?version=1&format=json&guid="+guid+"&Q-UA=" + qua;;
		String cookis = "guid_tvskey="+guid_tvskey+";guid="+guid;
		GetAlbumMembersAsync membersTask = new GetAlbumMembersAsync();
		membersTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,server,params,cookis);
	}
	
	/**
	 * 
	 * @类描述：异步获取相册绑定二维码地址
	 * @项目名称：TXBootSettings
	 * @包名： com.txbox.settings.impl
	 * @类名称：GetAlbumQrcodeAsync	
	 * @创建人：Administrator
	 * @创建时间：2014-9-18下午9:15:17	
	 * @修改人：Administrator
	 * @修改时间：2014-9-18下午9:15:17	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright pivos
	 * @mail 939757170@qq.com
	 */
	class GetAlbumQrcodeAsync extends AsyncTask<String, Integer, QrcodeBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected QrcodeBean doInBackground(String... params) {
			String server = params[0];
			String url = params[1];
			String cookies = params[2];
			String json = getJsonByGet(server+url,cookies);
			if (json.equals(ERROR))
				return null;
			try{
				QrcodeBean commonReturn = getGson().fromJson(json, QrcodeBean.class);
				return commonReturn;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(QrcodeBean result) {
			if (mlisterner != null)
				mlisterner.onGetQrcodeFinished(result);
		}
	}
	
	/**
	 * 
	 * @类描述：异步读取家庭相册成员
	 * @项目名称：TXBootSettings
	 * @包名： com.txbox.settings.impl
	 * @类名称：GetAlbumMembersAsync	
	 * @创建人：Administrator
	 * @创建时间：2014-9-18下午9:23:48	
	 * @修改人：Administrator
	 * @修改时间：2014-9-18下午9:23:48	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright pivos
	 * @mail 939757170@qq.com
	 */
	class GetAlbumMembersAsync extends AsyncTask<String, Integer, AlbumMembersBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected AlbumMembersBean doInBackground(String... params) {
			String server = params[0];
			String url = params[1];
			String cookies = params[2];
			String json = getJsonByGet(server+url,cookies);
			if (json.equals(ERROR))
				return null;
			try{
				AlbumMembersBean commonReturn = getGson().fromJson(json, AlbumMembersBean.class);
				return commonReturn;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(AlbumMembersBean result) {
			if (mlisterner != null)
				mlisterner.onGetMembersFinished(result);
		}
	}
}
