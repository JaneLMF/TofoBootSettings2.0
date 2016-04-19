package com.txbox.settings.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

public class GsonApi {
	public static final String TAG="GsonApi";
	public static final String ERROR = "error";
	
	public static final int CONNECTION_TIMEOUT=20000;
	public static final int SO_TIMEOUT=20000;

	private Gson gson;
	
	/**
	 * Gson API 
	 * json To bean
	 * @return Gson
	 */
	public Gson getGson(){
		return gson==null?new Gson():gson;
	}
	
	/**
	 * HttpRequest Get
	 * @param url
	 * @return json
	 */
	public static String getJsonByGet(String url,String cookies) {
		
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		//url=SearchUtil.getServerURL()+url;
		String json = "";
		HttpClient client = new DefaultHttpClient(httpParams);
		//Log.e("Boot Setting GsonApi", "请求10地址为："+url);
		HttpGet get = new HttpGet(url);
		if(cookies!=null && cookies.length()>0){
			get.addHeader("Cookie", cookies);
		}
		HttpResponse response;
		try {
			response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//				json = EntityUtils.toString(response.getEntity());
				json = EntityUtils.toString(response.getEntity(),"UTF-8");
			} else {
				json = ERROR;
				Log.e(ERROR, "NullPointerException");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			json=ERROR;
		} catch (IOException e) {
			e.printStackTrace();
			json=ERROR;
		}
		//Log.e("Boot Setting GsonApi", "Return JSON:"+json);
		return json;
	}
	
	/**
	 * HttpRequest Get
	 * @param url
	 * @return json
	 */
	public static String getGo3cJsonByGet(String url) {
		String json = "";
		HttpClient client = new DefaultHttpClient();
		//Log.e("GsonApi", "请求URL地址为："+url);
		HttpGet get = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				json = EntityUtils.toString(response.getEntity());
			} else {
				json = ERROR;
				Log.e(ERROR, "NullPointerException");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * HttpRequest Post
	 * @param url
	 * @param objName
	 * @param obj
	 * @return json
	 */
	public static String getJsonByPost(String url, Object[] objName,
			Object[] obj,String cookies) {
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		if(cookies!=null && cookies.length()>0){
			httpPost.addHeader("Cookie", cookies);
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (int i = 0; i < objName.length; i++) {
			//System.out.println("objName="+objName[i] + "obj=" + obj[i]);
			if(objName[i]!=null && obj[i]!=null){
				params.add(new BasicNameValuePair(objName[i].toString(), obj[i]
						.toString()));
			}
		}
		String json = "";
		try {
			HttpEntity he = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			httpPost.setEntity(he);
			HttpResponse response = client.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				json = EntityUtils.toString(response.getEntity());
			} else
				json = ERROR;
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("return json = " + json);
		return json;
	}
	/**
	 * 
	 * @描述:获取302重定向url
	 * @方法名: getLocation
	 * @param url
	 * @return
	 * @返回类型 String
	 * @创建人 "liting yan"
	 * @创建时间 2014-6-5下午3:10:31	
	 * @修改人 "liting yan"
	 * @修改时间 2014-6-5下午3:10:31	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public String getLocation(String url){  
			HttpParams httpParams = new BasicHttpParams();
			httpParams.setParameter(ClientPNames.HANDLE_REDIRECTS, false);
			HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
			//url=SearchUtil.getServerURL()+url;
			String json = "";
			HttpClient client = new DefaultHttpClient(httpParams);
			//Log.e("GsonApi", "请求10地址为："+url);
			HttpGet get = new HttpGet(url);
			HttpResponse response;
			try {
				response = client.execute(get);
				int status = response.getStatusLine().getStatusCode();
				System.out.println("response status=" + status);
				Header[] header  = response.getHeaders("Location");
				//String redirecturl = header[0].getValue();
				//System.out.println("response location=" + redirecturl);
				if (status == HttpStatus.SC_MOVED_TEMPORARILY) {
					json = header[0].getValue();
					//json = json.replace("\\", "//");
					json = json.replace("\\","/");
					
				} else {
					json = ERROR;
					Log.e(ERROR, "NullPointerException");
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				json=ERROR;
			} catch (IOException e) {
				e.printStackTrace();
				json=ERROR;
			}
			//System.out.println("Return JSON:"+json);
			return json;
	}
}
