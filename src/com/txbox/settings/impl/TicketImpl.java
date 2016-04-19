package com.txbox.settings.impl;

import android.content.Context;
import android.os.AsyncTask;

import com.txbox.settings.bean.TicketClientBean;
import com.txbox.settings.bean.TicketTvskeyBean;
import com.txbox.settings.bean.UpgradeBean;
import com.txbox.settings.interfaces.ITicketImpl;

/**
 * 
 * @类描述：通过本地http加密、解密票据数据测试类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.impl
 * @类名称：TicketImpl	
 * @创建人：Administrator
 * @创建时间：2014-9-17下午10:46:27	
 * @修改人：Administrator
 * @修改时间：2014-9-17下午10:46:27	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class TicketImpl extends GsonApi{
	private Context context;
	private ITicketImpl mlisterner;
	public TicketImpl(Context context, ITicketImpl listerner) {
		this.context = context;
		this.mlisterner = listerner;
	}
	
	public void getTicketClient(String guid,String guid_secret,String random){
		String server = "http://127.0.0.1:8090/txbox/ticket.api?m=genticket_client";
		String params="&guid="+guid+"&guid_secret="+guid_secret+"&random="+random;
		GetTicketClientInfoAsync clientTask = new GetTicketClientInfoAsync();
		clientTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,server,params);
	}
	public void getTvskey(String guid,String ticket_server,String random){
		String server = "http://127.0.0.1:8090/txbox/ticket.api?m=gettvskey";
		String params="&guid="+guid+"&ticket_server="+ticket_server+"&random="+random;
		GetTvskeyInfoAsync tvskeyTask = new GetTvskeyInfoAsync();
		tvskeyTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,server,params);
	}
	
	class GetTicketClientInfoAsync extends AsyncTask<String, Integer,TicketClientBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected TicketClientBean doInBackground(String... params) {
			String server = params[0];
			String url = params[1];
			String json = getJsonByGet(server+url,null);
			if (json.equals(ERROR))
				return null;
			try{
				TicketClientBean commonReturn = getGson().fromJson(json, TicketClientBean.class);
				return commonReturn;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(TicketClientBean result) {
			if (mlisterner != null)
				mlisterner.onTicketClientFinished(result);
		}
	}
	
	class GetTvskeyInfoAsync extends AsyncTask<String, Integer, TicketTvskeyBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected TicketTvskeyBean doInBackground(String... params) {
			String server = params[0];
			String url = params[1];
			String json = getJsonByGet(server+url,null);
			if (json.equals(ERROR))
				return null;
			try{
				TicketTvskeyBean commonReturn = getGson().fromJson(json, TicketTvskeyBean.class);
				return commonReturn;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(TicketTvskeyBean result) {
			if (mlisterner != null)
				mlisterner.onTvSkeyFinished(result);
		}
	}
}
