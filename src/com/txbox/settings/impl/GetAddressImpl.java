package com.txbox.settings.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.os.AsyncTask;
import com.txbox.settings.bean.AddressBean;
import com.txbox.settings.utils.ConfigManager;



public class GetAddressImpl extends GsonApi {
	private IPlayInfoImpl mGetPlayInfoListener;
	
	public interface IPlayInfoImpl {
		public void onPlayInfoFinished(String playinfo);

	}
	
	public void SetGetPlayInfoListener(IPlayInfoImpl listener){
		mGetPlayInfoListener = listener;
	}
	
	public String getAddress(){
		StringBuilder json = new StringBuilder();
		try {
			json = getStringFromFile(ConfigManager.PLAY_PROXY_ADDRESS_PATH);
			if(json!=null && json.length()>0){
				AddressBean logininfo = getGson().fromJson(json.toString(), AddressBean.class);
				return logininfo.getLocalHttpSvrAddr();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public  StringBuilder getStringFromFile(String path) throws UnsupportedEncodingException {

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
	public void getPlayInfo(String server,String vid,int ischarge){
		String params = "/startplay?vid="+vid+"&ischarge="+ischarge;
		GetPlayInfoAsync playInfoTask = new GetPlayInfoAsync();
		playInfoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,server,params);
	}
	
	public void stopTXPlay(String url){
		GetPlayInfoAsync playInfoTask = new GetPlayInfoAsync();
		playInfoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url,"");
	}
	
	class GetPlayInfoAsync extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			String server = params[0];
			String url = params[1];
			String json = null;
			try {
				json = HttpApi.fetchTextFromUrl(1,server+url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
			if (json!=null && json.equals(ERROR))
				return null;
			
		    return json;
		}

		@Override
		protected void onPostExecute(String result) {
			if (mGetPlayInfoListener != null)
				mGetPlayInfoListener.onPlayInfoFinished(result);
		}
	}
	
}
