/*
 * Copyright (C) 2010-2012 TENCENT Inc.All Rights Reserved.
 *
 * FileName: HttpApi
 *
 * Description:  Android中的几种http访问方式的封装，比如AndroidHttpClient,HttpClient,UrlConnection
 *
 * History:
 *  1.0   kodywu (kodytx@gmail.com) 2010-11-30   Create
 */

package com.txbox.settings.impl;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.text.TextUtils;
import android.util.Log;


public class HttpApi {

	private static final String TAG = "HttpApi";
	private static final boolean isDebug = true;

	public static final int TIME_OUT_5S = 5000;// 5秒
	public static final int TIME_OUT_10S = 10000;// 10秒
	public static final int TIME_OUT_20S = 20000;// 20秒
	public static final int TIME_OUT_30S = 20000;// 20秒

	public static final int READ_TIME_OUT = TIME_OUT_20S;

	private static int mConnectionTimeout = TIME_OUT_10S;

	public static void setConnectionTimeout(int timeout) {
		mConnectionTimeout = timeout;
	}

	public static int getConnectionTimeout() {
		return mConnectionTimeout;
	}

	public static class TimeOutException extends Exception {
		private static final long serialVersionUID = 42438942451326636L;
		private int mErrCode;

		public TimeOutException(int errCode) {
			super("Connection Time out");
			mErrCode = errCode;
		}

		public int getErrCode() {
			return mErrCode;
		}
	}

	public static class UnAuthorizedException extends Exception {
		private static final long serialVersionUID = 42438942451326637L;
		private int mErrCode;

		public UnAuthorizedException(int errCode) {
			super("unauthorized excepton");
			mErrCode = errCode;
		}

		public int getErrCode() {
			return mErrCode;
		}
	}

	public static class NotFoundException extends Exception {
		private static final long serialVersionUID = 42438942451326638L;
		private int mErrCode;

		public NotFoundException(int errCode) {
			super("Request file not found");
			mErrCode = errCode;
		}

		public int getErrCode() {
			return mErrCode;
		}
	}

	public static byte[] httpEntityToByteArray(final HttpEntity entity)
			throws IOException {
		if (entity == null) {
			throw new IllegalArgumentException("HTTP entity may not be null");
		}
		InputStream instream = entity.getContent();
		if (instream == null) {
			return new byte[] {};
		}
		if (entity.getContentLength() > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(
					"HTTP entity too large to be buffered in memory");
		}
		int i = (int) entity.getContentLength();
		if (i < 0) {
			i = 4096;
		}
		ByteArrayBuffer buffer = new ByteArrayBuffer(i);
		try {
			byte[] tmp = new byte[4096];
			int l;
			while ((l = instream.read(tmp)) != -1) {
				if (Thread.interrupted())
					throw new InterruptedIOException(
							"File download process was canceled");
				buffer.append(tmp, 0, l);
			}
		} finally {
			instream.close();
		}
		return buffer.toByteArray();
	}

	public byte[] getFileFromUrl(AbstractHttpClient httpClient, String url)
			throws IOException {
		if (url == null)
			return new byte[] {};
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = httpClient.execute(httpGet);
		HttpEntity httpEntity = response.getEntity();
		byte[] result = null;
		if (httpEntity != null) {
			try {
				result = httpEntityToByteArray(httpEntity);
			} catch (InterruptedIOException e) {
				httpGet.abort();
				throw e;
			} finally {
				httpEntity.consumeContent();
			}
		}
		return result;
	}

	@SuppressWarnings("unused")
	private static boolean isValidJsonData(String result) {
		boolean ret = true;
		try {
			if (result.startsWith("QZOutputJson=")) {
				result = result.replace("QZOutputJson=", "");
			}
			new JSONObject(result);
		} catch (Exception e) {
			ret = false;
			Log.i("cache error", "invalid cache data");
		}
		return ret;
	}



	private final static int retryTimes[]={3,2};
	private final static int connectTimeOut[]={3000,3000,6000};
	public static void setConnectTimeOut(int firstTimeOut,int secondTimeOut,int thirdTimeOut){
		if(firstTimeOut>1000&&firstTimeOut<50000){
			connectTimeOut[0]=firstTimeOut;
		}
		if(secondTimeOut>1000&&secondTimeOut<50000){
			connectTimeOut[1]=secondTimeOut;		
		}
		if(thirdTimeOut>1000&&thirdTimeOut<50000){
			connectTimeOut[2]=thirdTimeOut;
		}
	}
	

	
	public static String fetchTextFromUrl(int moduleId, String remoteUrl) throws IOException {

		long start = 0;
		long readDataEnd = 0;
		int code = 0;
		long connectEnd = 0;
		boolean suc=false;
		String result = null;
		
		start = System.currentTimeMillis();
		for (int domain = 0; domain < 2; domain++) {
			String connectUrl = remoteUrl;
			if (domain == 1) {
				connectUrl = getBkDomain(remoteUrl);
			}
			if (isDebug) {
				Log.i(TAG, "[fetchTextFromUrl]request url=" + connectUrl);
			}
			InputStream is = null;
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				HttpURLConnection connection = null;
				int connectTimes=0;
				int i = 0;
				for (i = 0; i < retryTimes[domain]; i++) {
					try {
						connectTimes++;
						int conTimeout = connectTimeOut[i];
						URL url = new URL(connectUrl);
						connection = (HttpURLConnection) url.openConnection();
						connection.setConnectTimeout(conTimeout);
						connection.setReadTimeout(READ_TIME_OUT);
						if (isDebug) {
							Log.d(TAG, "第" + i + "次，超时时间=" + conTimeout
									+ " url=" + connectUrl);
						} else {
							Log.d(TAG, "第" + i + "次，超时时间=" + conTimeout
									+ " url=" + getRemoteUrl(connectUrl));
						}

						connection.setUseCaches(false);
						connection.connect();
						break;

					} catch (IOException e) {
						if (isDebug) {
							Log.d(TAG,
									"重试" + i + "次后出现的错误+Connection exception ="
											+ e.toString() + " ,url="
											+ connectUrl);
						} else {
							Log.d(TAG,
									"重试" + i + "次后出现的错误+Connection exception ="
											+ e.toString() + " ,url="
											+ getRemoteUrl(connectUrl));
						}

						if (i >= 2) {
							if (isDebug) {
								Log.d(TAG, "重试" + i
										+ "次后抛出异常+ exception =" + e.toString()
										+ ",url=" + connectUrl);
							} else {
								Log.d(TAG, "重试" + i
										+ "次后抛出异常+ exception =" + e.toString()
										+ ",url=" + getRemoteUrl(connectUrl));
							}
							
						}
					} catch (AssertionError e) {
						Log.e(TAG, "connect error," + e.toString());
					}
				}
				
				connectEnd = System.currentTimeMillis();

				/*
				 * URL url = new URL(remoteUrl); HttpURLConnection connection =
				 * (HttpURLConnection)url.openConnection();
				 * connection.setConnectTimeout(mConnectionTimeout);
				 * connection.setReadTimeout(READ_TIME_OUT);
				 * connection.connect();
				 */
				if (connection == null) {
					throw new IOException();
				}
				code = connection.getResponseCode();
				if (code >= 400) {
					/*if(code == 404){
						throw new FileNotFoundException();
					}*/
				}

				is = connection.getInputStream();
				int bytesRead;
				byte[] buffer = new byte[2048];
				while ((bytesRead = is.read(buffer)) != -1) {
					os.write(buffer, 0, bytesRead);
				}

				os.flush();
				result = new String(os.toByteArray());
				
				readDataEnd = System.currentTimeMillis();
//				reportDuration(moduleId, connectUrl,
//						(int) (connectEnd - start),
//						(int) (readDataEnd - connectEnd),connectTimes);
//				
//				if(httpReqReportListener!=null){
//					httpReqReportListener.reportDuration(moduleId, connectUrl, (int) (connectEnd - start),
//						(int) (readDataEnd - connectEnd),connectTimes);
//				}
				if (isDebug) {

					Log.d(TAG, "[fetchTextFromUrl]网络请求连接耗时 "
							+ (connectEnd - start) + " 毫秒" + "读取数据耗时"
							+ (readDataEnd - connectEnd) + " 毫秒" + " moduleID="
							+ moduleId + " 请求url=" + connectUrl);
				} else {

					Log.d(TAG, "[fetchTextFromUrl]网络请求连接耗时 "
							+ (connectEnd - start) + " 毫秒" + "读取数据耗时"
							+ (readDataEnd - connectEnd) + " 毫秒" + " moduleID="
							+ moduleId + " 请求url=" + getRemoteUrl(connectUrl));
				}
				suc=true;
			} catch (IOException e) {
				if (isDebug) {
					Log.d(TAG, "[fetchTextFromUrl]抛出网络异常：" + e.toString()
							+ " result=" + result + ",url=" + connectUrl);
				} else {
					Log.d(TAG, "[fetchTextFromUrl]抛出网络异常：" + e.toString()
							+ " result=" + result + ",url="
							+ getRemoteUrl(connectUrl));
				}
				throw e;

				 //reportException(moduleId,end-start,remoteUrl,desc);

			} finally {

				try {
					if (os != null) {
						os.close();
					}
					if (is != null) {
						is.close();
					}
				} catch (Exception e) {
					Log.i(TAG, "释放streaming" + e.toString());
				}
			}
			if(suc==true) break;
		}
		return result;
	}

	private static String getRemoteUrl(String remoteUrl) {
		if (!TextUtils.isEmpty(remoteUrl)) {
			int index = remoteUrl.indexOf("?");
			int urlLength = remoteUrl.length();
			remoteUrl = remoteUrl.substring(index + 1, urlLength);
		}
		return remoteUrl;
	}

	public static String fetchTextFromUrl(int moduleId, String remoteUrl,
			String charSet, String cookie, int retryTimes) throws IOException {
		return fetchTextFromUrl(moduleId, remoteUrl, charSet, cookie,false);
	}

	public static String fetchTextFromUrl(int moduleId, String remoteUrl,
			String charSet, String cookie, boolean isPostMethod)
			throws IOException {

		long start = 0;
		long connectEnd = 0;
		long readDataEnd = 0;
        boolean suc=false;
		String result = null;
		start = System.currentTimeMillis();
		for (int domain = 0; domain < 2; domain++) {
			String connectUrl = remoteUrl;
			if (domain == 1) {
				connectUrl = getBkDomain(remoteUrl);
			}
			if (isDebug) {
				Log.i(TAG, "[fetchTextFromUrl]request url=" + connectUrl);
			}
			InputStream is = null;
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				HttpURLConnection connection = null;
				int i = 0;
				int connectTimes=0;
				for (i = 0; i < retryTimes[domain]; i++) {
					try {
						connectTimes++;
						int conTimeout = connectTimeOut[i];
						URL url = new URL(connectUrl);
						connection = (HttpURLConnection) url.openConnection();
						if (!TextUtils.isEmpty(cookie)) {
							connection.setRequestProperty("Cookie", cookie);
						}
						connection.setConnectTimeout(conTimeout);
						connection.setReadTimeout(READ_TIME_OUT);
						if (isDebug) {
							Log.d(TAG, "第" + i + "次，超时时间=" + conTimeout
									+ " url=" + connectUrl);
						} else {
							Log.d(TAG, "第" + i + "次，超时时间=" + conTimeout
									+ " url=" + getRemoteUrl(connectUrl));
						}

						connection.setUseCaches(false);
						if (isPostMethod) {
							connection.setRequestMethod("POST");
						}
						connection.connect();
						break;
					} catch (IOException e) {
						if (isDebug) {
							Log.d(TAG,
									"重试" + i + "次后出现的错误+Connection exception ="
											+ e.toString() + " ,url="
											+ connectUrl);
						} else {
							Log.d(TAG,
									"重试" + i + "次后出现的错误+Connection exception ="
											+ e.toString() + " ,url="
											+ getRemoteUrl(connectUrl));
						}

						if (i >= 2) {
							if (isDebug) {
								Log.d(TAG, "重试" + i
										+ "次后抛出异常+ exception =" + e.toString()
										+ ",url=" + connectUrl);
							} else {
								Log.d(TAG, "重试" + i
										+ "次后抛出异常+ exception =" + e.toString()
										+ ",url=" + getRemoteUrl(connectUrl));
							}
						}
					} catch (AssertionError e) {
						Log.e(TAG, "connect error," + e.toString());
					}
				}

				connectEnd = System.currentTimeMillis();

				// tangwh add 20130425
				if (connection == null) {
					throw new IOException();
				}

				is = connection.getInputStream();
				int bytesRead;
				byte[] buffer = new byte[2048];
				while ((bytesRead = is.read(buffer)) != -1) {
					os.write(buffer, 0, bytesRead);
				}

				os.flush();
				if (charSet == null) {
					result = new String(os.toByteArray());
				} else {
					result = new String(os.toByteArray(), charSet);
				}
				/*
				 * if(isValidJsonData(result)){
				 * FsCache.getInstance().put(remoteUrl, result); }
				 */
				readDataEnd = System.currentTimeMillis();
//				reportDuration(moduleId, connectUrl,
//						(int) (connectEnd - start),
//						(int) (readDataEnd - connectEnd),connectTimes);
//				if(httpReqReportListener!=null){
//					httpReqReportListener.reportDuration(moduleId, connectUrl, (int) (connectEnd - start),
//						(int) (readDataEnd - connectEnd),connectTimes);
//				}
				if (isDebug) {

					Log.i(TAG, "[fetchTextFromUrl]网络请求耗时 "
							+ (readDataEnd - start) + " 毫秒" + " moduleID="
							+ moduleId);
				}
				suc=true;
			} catch (IOException e) {
				throw e;
			} finally {

				try {
					if (os != null) {
						os.close();
					}
					if (is != null) {
						is.close();
					}
				} catch (Exception e) {
					Log.i(TAG, "释放streaming" + e.toString());
				}
			}
			if(suc==true) break;
		}
		return result;
	}

	// private HttpParams mHttpParameters;

	private static HttpApi sConnection;

	public HttpApi() {
		/*
		 * mHttpParameters = new BasicHttpParams();
		 * HttpConnectionParams.setConnectionTimeout(mHttpParameters,
		 * mTimeoutConnection);
		 * HttpConnectionParams.setSoTimeout(mHttpParameters, mTimeoutSocket);
		 * mHttpClient = new DefaultHttpClient(mHttpParameters);
		 */

	}

	public AndroidHttpClient createHttpClient() {
		/*
		 * if(mHttpParameters == null){ mHttpParameters = new BasicHttpParams();
		 * HttpConnectionParams.setConnectionTimeout(mHttpParameters,
		 * mTimeoutConnection);
		 * HttpConnectionParams.setSoTimeout(mHttpParameters, mTimeoutSocket); }
		 */
		// return new DefaultHttpClient(mHttpParameters);
		return AndroidHttpClient.newInstance("Android");
		// return new DefaultHttpClient();
	}

	public static Bitmap downloadBitmap(String url) {

		// final AndroidHttpClient client = AndroidHttpClient("Android"); //
		// Android API 8 only
		// final DefaultHttpClient client = new DefaultHttpClient();
		AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Android");
		final HttpGet getRequest = new HttpGet(url);
		try {
			HttpResponse response = httpClient.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode
						+ " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					final Bitmap bitmap = BitmapFactory
							.decodeStream(inputStream);
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			// Could provide a more explicit error message for IOException or
			// IllegalStateException
			getRequest.abort();
			// Log.w("ImageDownloader", "Error while retrieving bitmap from " +
			// url, e.toString());
		} finally {
			if (httpClient != null) {
				httpClient.close(); // Android API 8 only
			}
		}
		return null;
	}

	public static HttpApi getInstance() {
		if (sConnection == null) {
			sConnection = new HttpApi();
		}
		return sConnection;
	}

	/************************************* do Http GET **********************************/

	/**
	 * Sets the timeout until a connection is etablished. A value of zero means
	 * the timeout is not used. The default value is zero.
	 */
	int CONNECTION_TIMEOUT = 10 * 1000;

	/**
	 * Sets the default socket timeout (SO_TIMEOUT) in milliseconds which is the
	 * timeout for waiting for data. A timeout value of zero is interpreted as
	 * an infinite timeout. This value is used when no socket timeout is set in
	 * the method parameters.
	 */
	int SO_TIMEOUT = 10 * 1000;

	int SOCKET_BUFFER_SIZE = 8 * 1024;

	/**
	 * 重试次数
	 */
	int RETRY_COUNT = 2;

	private HttpParams getHttpParams() {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams,
				CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		HttpConnectionParams
				.setSocketBufferSize(httpParams, SOCKET_BUFFER_SIZE);
		HttpClientParams.setRedirecting(httpParams, true);
		return httpParams;
	}

	/*
	 * public String doHttpPost(int moduleId,String url,String cookie) throws
	 * IOException {
	 * 
	 * 
	 * boolean isConnected = false; int times = 0; String result = "";
	 * 
	 * long start=0; long end=0;
	 * 
	 * start=System.currentTimeMillis();
	 * 
	 * DefaultHttpClient httpClient = null ; HttpPost httpPost = null;
	 * HttpParams httpParams = getHttpParams(); while(!isConnected &&
	 * times<RETRY_COUNT){ try { if(times >0 ){
	 * Log.i(TAG,"[doHttpGet]重试第"+times+"次,moduleId="+moduleId); }
	 * httpClient = new DefaultHttpClient(httpParams); httpPost = new
	 * HttpPost(url);
	 * 
	 * // httpGet.setHeader("Content-Type",
	 * "application/x-www-form-urlencoded"); // httpGet.setHeader("Accept",
	 * "application/json");
	 * 
	 * 
	 * HttpResponse response = httpClient.execute(httpPost); if(
	 * response.getStatusLine().getStatusCode() != HttpStatus.SC_OK ){ // throw
	 * new
	 * HttpStatusCodeException("http status:"+response.getStatusLine().getStatusCode
	 * ()); Log.i(TAG,
	 * "[doHttpGet] Http错误："+response.getStatusLine().getStatusCode
	 * ()+"moduleId="+moduleId); throw new
	 * IOException("http status code:"+(response
	 * !=null&&response.getStatusLine()!=
	 * null?response.getStatusLine().getStatusCode():"unknown!")); } HttpEntity
	 * entity = response.getEntity(); result = EntityUtils.toString( entity
	 * ,HTTP.UTF_8); isConnected = true; break; }catch (IOException e) { if (e
	 * instanceof ConnectTimeoutException || e instanceof
	 * SocketTimeoutException){ result=FsCache.getInstance().get(url);
	 * if(FsCache.isValidString(result)){ if(isDebug){
	 * end=System.currentTimeMillis(); Log.i(TAG,
	 * "[doHttpGet]网络超时：读取缓存的内容耗时 "
	 * +(end-start)+" 毫秒,url="+url+" ,moduleId="+moduleId); } return result; } }
	 * 
	 * Log.i(TAG,"[doHttpGet]请求异常："+e.toString()+" times="+times+",url="+url
	 * ); ++times ; if(times>=RETRY_COUNT){ Log.i(TAG,
	 * "[doHttpGet]抛出异常:"+e.toString()+" times ="+times);
	 * 
	 * throw e; }else{ continue; } // e.printStackTrace();
	 * 
	 * } } Log.i(TAG, "[doHttpGet]isConnected="+
	 * isConnected+"moduleID="+moduleId); if(!isConnected){ Log.i(TAG,
	 * "[doHttpGet]网络请求异常， moduleID="+moduleId); throw new IOException(); }
	 * Log.i(TAG, "[doHttpGet] 数据写入缓存，moduleID="+moduleId);
	 * if(isValidJsonData(result)){ FsCache.getInstance().put(url, result); }
	 * end=System.currentTimeMillis();
	 * 
	 * if(isDebug){ end=System.currentTimeMillis(); Log.i(TAG,
	 * "[doHttpGet]网络请求成功耗时 "+(end-start)+" 毫秒"+" moduleID="+moduleId); }
	 * 
	 * return result ; }
	 */
	
	public static final int DEFAULT_OZ_REPORT_TIMEOUT = 9000;

	public static String postHttpText(int moduleId, String host,
			HashMap<String, String> params, String cookie) throws IOException {
		String ret = null;

		URL url = null;
		HttpURLConnection httpurlconnection = null;
		BufferedReader rd = null;

		try {
			url = new URL(host);
			httpurlconnection = (HttpURLConnection) url.openConnection();
			httpurlconnection.setDoOutput(true);
			httpurlconnection.setRequestMethod("POST");
			httpurlconnection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
//			httpurlconnection.setConnectTimeout(TencentVideo.getOzTimeout());
//			httpurlconnection.setReadTimeout(TencentVideo.getOzTimeout());
			httpurlconnection.setConnectTimeout(DEFAULT_OZ_REPORT_TIMEOUT);
			httpurlconnection.setReadTimeout(DEFAULT_OZ_REPORT_TIMEOUT);
			if (!TextUtils.isEmpty(cookie)) {
				httpurlconnection.setRequestProperty("Cookie", cookie);
			}

			StringBuilder dataBfr = new StringBuilder();
			for (Object key : params.keySet()) {
				if (dataBfr.length() != 0) {
					dataBfr.append('&');
				}
				Object value = params.get(key);
				if (value == null) {
					value = "";
				}
				dataBfr.append(URLEncoder.encode(key.toString(), "UTF-8"))
						.append('=')
						.append(URLEncoder.encode(value.toString(), "UTF-8"));
			}

			httpurlconnection.getOutputStream().write(
					dataBfr.toString().getBytes("UTF-8"));
			httpurlconnection.getOutputStream().flush();
			httpurlconnection.getOutputStream().close();

			rd = new BufferedReader(new InputStreamReader(
					httpurlconnection.getInputStream()));
			String line;

			StringBuilder retString = new StringBuilder();
			while ((line = rd.readLine()) != null) {
				retString.append(line);
			}
			ret = retString.toString();

			rd.close();

		} catch (Exception e) {

			if (rd != null) {
				try {
					rd.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					// e1.printStackTrace();
					Log.e(TAG, ExceptionHelper.PrintStack(e1));
				}
			}

		} catch (AssertionError e) {
			// e.printStackTrace();
			Log.e(TAG, ExceptionHelper.PrintStack(e));
		} finally {
			if (httpurlconnection != null) {
				try {
					httpurlconnection.disconnect();
				} catch (AssertionError e) {
					Log.e(TAG, "disconnect error," + e.toString());
				}
			}

		}
		return ret;

		/*
		 * String result = null; HttpClient httpclient = new
		 * DefaultHttpClient();
		 * 
		 * 
		 * HttpPost httppost = new HttpPost(host);
		 * if(!TextUtils.isEmpty(cookie)){ httppost.setHeader("Cookie", cookie);
		 * } List<NameValuePair> nameValuePairs = new
		 * ArrayList<NameValuePair>(); for (String key : params.keySet()) {
		 * String utfKey = URLEncoder.encode(key, "UTF-8"); String utfValue
		 * =URLEncoder.encode(params.get(key), "UTF-8");//
		 * URLEncoder.encode(params.get(key), "UTF-8"); nameValuePairs.add(new
		 * BasicNameValuePair(utfKey, utfValue)); } httppost.setEntity(new
		 * UrlEncodedFormEntity(nameValuePairs)); HttpResponse response =
		 * httpclient.execute(httppost); HttpEntity entity =
		 * response.getEntity(); result = EntityUtils.toString( entity
		 * ,HTTP.UTF_8); return result;
		 */

	}
//	private static HttpReqReportListener httpReqReportListener=null;
//	public  static void setHttpReqReportListener(HttpReqReportListener listener){
//		HttpApi.httpReqReportListener=listener;
//	}

	private final static String SPECIAL_DOMAIN="http://zb.v.qq.com";//这个域名的备份域名比较特殊
	private static String getBkDomain(String url) {
		int pos=url.indexOf("http://");
		if(pos==0){
			pos+=7;
			if(url.indexOf(SPECIAL_DOMAIN)==0){
				url=url.substring(0, pos)+"bk."+url.substring(pos, url.length());
			}else{
				url=url.substring(0, pos)+"bk"+url.substring(pos, url.length());
			}
		}
		return url;
	}
}
