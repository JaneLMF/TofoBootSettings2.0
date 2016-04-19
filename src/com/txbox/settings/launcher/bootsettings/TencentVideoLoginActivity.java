package com.txbox.settings.launcher.bootsettings;

import java.io.StringReader;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.go3c.videoplayer.MediaPlayerActivity;
import com.tencent.dmproxy.api.TVK_VideoUrlInfo;
import com.tencent.httpproxy.api.DownloadFacadeEnum;
import com.txbox.txsdk.R;
import com.txbox.settings.bean.GuidBean;
import com.txbox.settings.bean.GuidDataBean;
import com.txbox.settings.bean.GuidInfoBean;
import com.txbox.settings.bean.LoginBean;
import com.txbox.settings.bean.ResultBean;
import com.txbox.settings.bean.TvsKeyBean;
import com.txbox.settings.common.SystemInfoManager;
import com.txbox.settings.common.TXbootApp;
import com.txbox.settings.dialog.CustomProgressDialog;
import com.txbox.settings.impl.AuthImpl;
import com.txbox.settings.impl.GetAddressImpl;
import com.txbox.settings.impl.GetAddressImpl.IPlayInfoImpl;
import com.txbox.settings.impl.UserImpl;
import com.txbox.settings.interfaces.IAuthImpl;
import com.txbox.settings.report.EventId;
import com.txbox.settings.report.GlobalInfo;
import com.txbox.settings.utils.ConfigManager;
import com.txbox.settings.utils.DeviceUtils;
import com.txbox.settings.utils.MessageUtils;
import com.txbox.settings.utils.ServerManager;
import com.txbox.settings.utils.Utils;
import com.txbox.settings.utils.WebSocketFactory;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import android.widget.LinearLayout;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 
 * @类描述：调用腾讯视频付费统一登录页面类
 * @项目名称：TencentVideoSdkDemo
 * @包名： com.tencent.qqlive.demo
 * @类名称：TencentVideoLoginActivity	
 * @创建人：Administrator
 * @创建时间：2014-8-30下午3:03:26	
 * @修改人：Administrator
 * @修改时间：2014-8-30下午3:03:26	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class TencentVideoLoginActivity extends Activity {
	private String TAG="TencentVideoLoginActivity";
	private WebView mWebView;
	private String url = null;
	
	public CustomProgressDialog progressDialog = null;
	private JSAction jsAction;
	private WebSocketFactory mSocketFactory = null;
	

	/*盒子唯一ID*/
	private String TVID;
	/*盒子版本号*/
	private String APPVER;
	
	private String APPID = "";

	/*业务ID，需要向腾讯视频侧申请业务id*/
	private String BID="31001";
	

	private String mVID = null;
	private String mCID = null;
	private String mTitle = null;//视频名称
	private String address=null;//腾讯播放SDK接口地址

	private Context mContext;
	private String mEventIdString = null;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.flashwebview);
		System.out.println("WebViewActivity  onCreate");
		mContext = TencentVideoLoginActivity.this;
		WebView mWebView = new WebView(getApplicationContext());
		DisplayMetrics dm = new DisplayMetrics();//获取当前显示的界面大小
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width=dm.widthPixels;
		int height=dm.heightPixels;//获取当前界面的高度
		LinearLayout mll = (LinearLayout) findViewById(R.id.webview);
		mll.addView(mWebView, new LinearLayout.LayoutParams(width, height));
		progressDialog = CustomProgressDialog.create(this, true);
		
		APPID = ConfigManager.APPID;
	
	
		//mWebView = (WebView) findViewById(R.id.flashplaywebview);
		mWebView.setBackgroundColor(0);
		mWebView.getSettings().setJavaScriptEnabled(true);
		
		jsAction = new JSAction();
    	
		mWebView.addJavascriptInterface(jsAction,"OpenTV");
		
		mSocketFactory = new WebSocketFactory(mWebView);
		mWebView.addJavascriptInterface(mSocketFactory, "WebSocketFactory");//JS 中定义的 WebSocketFactory, 一般不要改动
		

		//TVID = DeviceUtils.getTvID(this);
		AuthImpl authImpl = new AuthImpl(mContext,null);
		GuidInfoBean guid = authImpl.GetGudiInfo(ConfigManager.GUID_INII_PATH);
		if(guid!=null){
			TVID = guid.getGuid();
		}
		APPVER = DeviceUtils.getFirmwareVersion();
		
		Uri uri = getIntent().getData();
		
		
		if(uri!=null){
			String dataurl = uri.toString();
			System.out.println("WebViewActivity  dataurl = " + dataurl);
			if(dataurl.indexOf("action=login")>=0){//登录页面
				mEventIdString = EventId.login.LOGIN_ACTION_LOGIN_PAGE_LOAD_FINISHED;
				url = ServerManager.LoginServer+"&tvid="+TVID+"&appver="+APPVER+"&bid="+BID+"&appid="+APPID+"&from=100"+"&ftime="+System.currentTimeMillis();
			}else if(dataurl.indexOf("action=vip")>=0){//开通会员页面一
				mEventIdString = EventId.login.LOGIN_ACTION_CHARGEVIP_PAGE_LOAD_FINISHED;
				url = ServerManager.VipServer+"&tvid="+TVID+"&appver="+APPVER+"&bid="+BID+"&appid="+APPID;
				//+"&openid="+logininfo.getOpenid()+"&access_token="+logininfo.getAccesstoken();
			}else if (dataurl.indexOf("action=action")>=0){
				url = getKeyValue(dataurl,"url");
				try {
                                        url = URLDecoder.decode(url,"utf-8");
                                } catch (UnsupportedEncodingException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                }
			}
			else if(dataurl.indexOf("pay")>=0){//开通会员页面二
				url = ServerManager.PayServer+"&tvid="+TVID+"&appver="+APPVER+"&bid="+BID+"&appid="+APPID+"&ftime="+System.currentTimeMillis();
				String from = getKeyValue(dataurl,"from");
				if(from!=null && from.length()>0) {
					url+="&from=" + from;
				}else {
					url+="&from=200";
				}
				String vipbid = getKeyValue(dataurl,"vipbid");
				if(vipbid!=null && vipbid.length()>0){
					url+="&vipbid=" + vipbid;
				}
				String cid = getKeyValue(dataurl,"cid");
				if(cid!=null && cid.length()>0) {
					mVID = getKeyValue(dataurl,"vid");
					mCID = cid;
					url+="&cid="+cid + "&vid="+mVID;
				}
				mTitle = getKeyValue(dataurl,"title");
				mEventIdString = EventId.login.LOGIN_ACTION_CHARGEVIP_PAGE_LOAD_FINISHED;
				UserImpl userimpl = new UserImpl(mContext);
				LoginBean logininfo = userimpl.getLoginInfo(ConfigManager.USERINFO_INII_PATH);
				if(logininfo!=null && logininfo.getIslogin()==1){
					url+="&openid="+logininfo.getOpenid()+"&access_token="+logininfo.getAccesstoken();
				}

			}else if(dataurl.indexOf("ticket")>=0){//观影券支付页面
				String cid = getKeyValue(dataurl,"cid");
				mVID = getKeyValue(dataurl,"vid");
				mCID = cid;
				mTitle = getKeyValue(dataurl,"title");
				mEventIdString = EventId.login.LOGIN_ACTION_CHARGETICKET_PAGE_LOAD_FINISHED;
				url = ServerManager.TicketServer+"&tvid="+TVID+"&appver="+APPVER+"&bid="+BID+"&appid="+APPID+"&cid="+cid;
				//+"&openid="+openid+"&access_token="+access_token;
			}else if(dataurl.indexOf("buy")>=0){//单片支付页面
				String cid = getKeyValue(dataurl,"cid");
				mVID = getKeyValue(dataurl,"vid");
				mCID = cid;
				mTitle = getKeyValue(dataurl,"title");
				mEventIdString = EventId.login.LOGIN_ACTION_CHARGECOVER_PAGE_LOAD_FINISHED;
				url = ServerManager.BuyServer+"?tvid="+TVID+"&appver="+APPVER+"&bid="+BID+"&appid="+APPID+"&cid="+cid;
				//"&openid="+openid+"&access_token="+access_token;
			}else if (dataurl.indexOf("pivosauth")>=0) {
				url = ServerManager.PIVOS_AUTH_QRCODE_SERVER;
			}
			else{//默认访问登录页面
				mEventIdString = EventId.login.LOGIN_ACTION_LOGIN_PAGE_LOAD_FINISHED;
				url = ServerManager.LoginServer+"&tvid="+TVID+"&appver="+APPVER+"&bid="+BID+"&appid="+APPID+"&from=100"+"&ftime="+System.currentTimeMillis();
			}
			String qua = GlobalInfo.getQua(ConfigManager.DEFAULT_PR);
			url+="&Q-UA="+qua;
			System.out.println("WebViewActivity load url= " + this.url);
			mWebView.loadUrl(this.url);
		}else{
			url = ServerManager.PIVOS_AUTH_QRCODE_SERVER;
			PivosRegDev();
			PivosGetGuid();
		}
		
		mWebView.addJavascriptInterface(mSocketFactory, "WebSocketFactory");//JS 中定义的 WebSocketFactory, 一般不要改动
		
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);// 使用当前WebView处理跳转
				return true;// true表示此事件在此处被处理，不需要再广播
			}
			
			@Override
			public void onPageFinished(WebView view, String url) { 
			   // 加载完成的处理
				super.onPageFinished(view, url);
				hideLoadingProgress();
				String params = "eventid=" + mEventIdString +"&pr=" + ConfigManager.DEFAULT_PR;
				GlobalInfo.reportMta(mContext,params);
			}

			@Override 
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				 // 转向错误时的处理
				 //登录页面加载失败在此处理
			}
			
			@Override  
			public void onReceivedSslError(WebView view, SslErrorHandler handler,  
			        SslError error)  
			{  
			    // TODO Auto-generated method stub  
			    //super.onReceivedSslError(view, handler, error); 
			    handler.proceed();
			}  
		});
//
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				view.requestFocus();
				if(newProgress>=100)
				{
					hideLoadingProgress();
				}else{
					updateLoaingProgress(newProgress);
				}
			}
			
			
		});
		
		showLoadingProgress();	
	
	}
	private String getKeyValue(String dataurl,String key){
		String value = "";
		int index = dataurl.indexOf(key);
		if(index>=0){
			value = dataurl.substring(index+key.length()+1);
			index = value.indexOf("&");
			if(index>=0){
				value = value.substring(0, index);
			}
		}
		return value;
	}
	
	public void hideLoadingProgress() {
		 //hide processbar
		progressDialog.dismiss();
		progressDialog.setMessage("");
	}
	public void showLoadingProgress(){
		progressDialog.show();
	}
	public void updateLoaingProgress(int progress){
		progressDialog.setMessage(progress + "%");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mSocketFactory!=null)
		{
			mSocketFactory.closeSocket();
			mSocketFactory = null;
			progressDialog = null;
		}
		if(mWebView!=null)
		{
			mWebView.removeAllViews();
			mWebView.clearCache(true);
			mWebView.destroy();
			mWebView = null;
		}
		
	}

    public void onStart() {
    	super.onStart();
    }
    
    public void onStop() {
    	super.onStop();

    }



    
    class MyWebChromeClient extends WebChromeClient {
    	
		public void onProgressChanged(WebView view, int progress)
		{
			Log.v("zhaojm", "progress is "+progress);
			TencentVideoLoginActivity.this.progressDialog.setMessage(progress + "%");
		}
    	
    	public boolean onJsAlert(WebView view, String url, String message,final JsResult result) {
    		return false;
    	}
    	
    	public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
    		return true;
    	}
    	
    	public boolean onJsPrompt(WebView view, String url, String message,  
    			String defaultValue, JsPromptResult result) {
    		return true;
    	}
    }
    
    /**
     * 
     * @类描述：调用网页登录成功后回调类
     * @项目名称：TencentVideoSdkDemo
     * @包名： com.tencent.qqlive.demo
     * @类名称：JSAction	
     * @创建人：Administrator
     * @创建时间：2014-8-30下午3:42:19	
     * @修改人：Administrator
     * @修改时间：2014-8-30下午3:42:19	
     * @修改备注：
     * @version v1.0
     * @see [nothing]
     * @bug [nothing]
     * @Copyright pivos
     * @mail 939757170@qq.com
     */
    public class JSAction {
    	/**
    	 * 
    	 * @描述:登录成功后回调方法，登录成功后在此处理
    	 * @方法名: onlogin
    	 * @param nick 昵称
    	 * @param face 头像
    	 * @param openid 用户openid
    	 * @param accesstoken 访问凭证
    	 * @返回类型 void
    	 * @创建人 Administrator
    	 * @创建时间 2014-8-30下午3:42:59	
    	 * @修改人 Administrator
    	 * @修改时间 2014-8-30下午3:42:59	
    	 * @修改备注 
    	 * @since
    	 * @throws
    	 */
    	@JavascriptInterface
    	public void onlogin(String nick,String face,String openid,String accesstoken,int mode) {
    		Log.d(TAG,"onlogin nick = " + nick + "face =" + face + "openid = " + openid + "accesstoken =" + accesstoken + "mode=" + mode);
    		// set the parmas back to Intent
    		UserImpl userimpl = new UserImpl(mContext);
    		LoginBean logininfo = new LoginBean();
    		logininfo.setNick(nick);
    		logininfo.setFace(face);
    		logininfo.setOpenid(openid);
    		logininfo.setAccesstoken(accesstoken);
    		logininfo.setAppid(APPID);
    		logininfo.setBid(BID);
    		logininfo.setIslogin(1);
    		userimpl.SaveLoginInfo(ConfigManager.USERINFO_INII_PATH_OLD,logininfo);
    		userimpl.SaveLoginInfo(ConfigManager.USERINFO_INII_PATH,logininfo);
    		String cmd = "chmod 666 " + ConfigManager.USERINFO_INII_PATH;
    		Utils.runCommand(cmd);
    		
    		MessageUtils.onlogin(mContext,nick,face,openid,accesstoken);
    		
    		Intent i = new Intent();
    		i.putExtra("nick", nick);
    		i.putExtra("face", face);
    		i.putExtra("openid", openid);
    		i.putExtra("accesstoken", accesstoken);
    		setResult(RESULT_OK, i);
    		if(mode==0){
    			TencentVideoLoginActivity.this.finish();
    		}
    	}
    	/**
    	 * 
    	 * @描述: 新登录接口回调方法
    	 * @方法名: onlogin
    	 * @param nick
    	 * @param face
    	 * @param openid
    	 * @param accesstoken
    	 * @param mode
    	 * @返回类型 void
    	 * @创建人 Administrator
    	 * @创建时间 2014-12-24下午11:32:55	
    	 * @修改人 Administrator
    	 * @修改时间 2014-12-24下午11:32:55	
    	 * @修改备注 
    	 * @since
    	 * @throws
    	 */
       	@JavascriptInterface
    	public void onlogin(String nick,String face,String openid,String accesstoken,int mode,String license_account,
    			String license_owner) {
    		Log.d(TAG,"onlogin nick = " + nick + "face =" + face + "openid = " + openid + 
    				"accesstoken =" + accesstoken + "mode=" + mode + "license_account=" + license_account + "license_owner=" + license_owner);
    		// set the parmas back to Intent
    		UserImpl userimpl = new UserImpl(mContext);
    		LoginBean logininfo = new LoginBean();
    		logininfo.setNick(nick);
    		logininfo.setFace(face);
    		logininfo.setOpenid(openid);
    		logininfo.setAccesstoken(accesstoken);
    		logininfo.setAppid(APPID);
    		logininfo.setBid(BID);
    		logininfo.setIslogin(1);
    		userimpl.SaveLoginInfo(ConfigManager.USERINFO_INII_PATH_OLD,logininfo);
    		userimpl.SaveLoginInfo(ConfigManager.USERINFO_INII_PATH,logininfo);
    		String cmd = "chmod 666 " + ConfigManager.USERINFO_INII_PATH;
    		Utils.runCommand(cmd);
    		
    		MessageUtils.onlogin(mContext,nick,face,openid,accesstoken);
    		
    		Intent i = new Intent();
    		i.putExtra("nick", nick);
    		i.putExtra("face", face);
    		i.putExtra("openid", openid);
    		i.putExtra("accesstoken", accesstoken);
    		setResult(RESULT_OK, i);
    		if(mode==0){
    			TencentVideoLoginActivity.this.finish();
    		}
    		
    		mEventIdString = EventId.login.LOGIN_ACTION_LOGIN_SUCCEED;
    		String params = "eventid=" + mEventIdString +"&pr=" + ConfigManager.DEFAULT_PR;
			GlobalInfo.reportMta(mContext,params);
    	}
    	
    	/**
    	 * 
    	 * @描述:开通会员成功回调方法：
    	 * @方法名: onPay
    	 * @param openid
    	 * @param month
    	 * @返回类型 void
    	 * @创建人 Administrator
    	 * @创建时间 2014-9-28下午1:43:23	
    	 * @修改人 Administrator
    	 * @修改时间 2014-9-28下午1:43:23	
    	 * @修改备注 
    	 * @since
    	 * @throws
    	 */
    	@JavascriptInterface
    	public void  onPay(String openid,String month,String vipbid)
    	{
    		Log.d(TAG, "Callback :onPay " + "openid=" + openid + "month = " + month+"vipbid = " + vipbid);
    		MessageUtils.onPay(mContext,openid,month);
    		mEventIdString = EventId.login.LOGIN_ACTION_CHARGEVIP_SUCCEED;
    		String params = "eventid=" + mEventIdString +"&pr=" + ConfigManager.DEFAULT_PR;
			GlobalInfo.reportMta(mContext,params);
    		TencentVideoLoginActivity.this.finish();
    	}
    	/**
    	 * 
    	 * @描述:买成功回调方法：
    	 * @方法名: onBuy
    	 * @返回类型 void
    	 * @创建人 Administrator
    	 * @创建时间 2014-9-28下午1:43:47	
    	 * @修改人 Administrator
    	 * @修改时间 2014-9-28下午1:43:47	
    	 * @修改备注 
    	 * @since
    	 * @throws
    	 */
    	@JavascriptInterface
    	public void  onBuy(String openid,String cid)
    	{
    		Log.d(TAG, "Callback :onBuy ");
    		MessageUtils.onBuy(mContext,openid,cid);
    		mEventIdString = EventId.login.LOGIN_ACTION_CHARGECOVER_SUCCEED;
    		String params = "eventid=" + mEventIdString +"&pr=" + ConfigManager.DEFAULT_PR;
			GlobalInfo.reportMta(mContext,params);
    		TencentVideoLoginActivity.this.finish();
    	}
	@JavascriptInterface
	public void  onBuyWithTicket(String openid, String cid)
	{
		if(mVID!=null){
			MessageUtils.onTry(mContext,mCID,mVID);
		}
		TencentVideoLoginActivity.this.finish();
	}
	@JavascriptInterface
	public void  Play(String tips)
	{
		Log.d(TAG, "Callback :onPlay " + tips);
		if(mVID!=null){
			MessageUtils.onTry(mContext,mCID,mVID);
		}
		TencentVideoLoginActivity.this.finish();
	}
    	/**
    	 * 
    	 * @描述:拉起试看
    	 * @方法名: Try
    	 * @返回类型 void
    	 * @创建人 Administrator
    	 * @创建时间 2014-9-28下午1:43:59	
    	 * @修改人 Administrator
    	 * @修改时间 2014-9-28下午1:43:59	
    	 * @修改备注 
    	 * @since
    	 * @throws
    	 */
    	@JavascriptInterface
    	public void  Try(){
    		Log.d(TAG, "Callback :Try ");
    		MessageUtils.onTry(mContext,mCID,mVID);
		TencentVideoLoginActivity.this.finish();
    		/*
    		GetAddressImpl addressImpl = new GetAddressImpl();
    		addressImpl.SetGetPlayInfoListener(new IPlayInfoImpl(){

				@Override
				public void onPlayInfoFinished(String playinfo) {
					// TODO Auto-generated method stub
					if(playinfo!=null){
						StartPlayAsync playTask = new StartPlayAsync();
						playTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, playinfo);
					}
				}
    			
    		});
    		
    		if(mVID!=null){
	    		address = addressImpl.getAddress();
	    		addressImpl.getPlayInfo("http://"+address, mVID,1);
    		}		
			*/
    	}
    	/**
    	 * 
    	 * @描述:调用对话框
    	 * @方法名: MsgBox
    	 * @返回类型 void
    	 * @创建人 Administrator
    	 * @创建时间 2014-9-28下午1:44:20	
    	 * @修改人 Administrator
    	 * @修改时间 2014-9-28下午1:44:20	
    	 * @修改备注 
    	 * @since
    	 * @throws
    	 */
    	@JavascriptInterface
    	public void  MsgBox(String message,String button)
    	{
    		Log.d(TAG, "MsgBox :message = " + message + "button=" + button);
    	}
    	/**
    	 * 
    	 * @描述:
    	 * @方法名: onPay
    	 * @param openid
    	 * @param month
    	 * @返回类型 void
    	 * @创建人 Administrator
    	 * @创建时间 2014-11-24上午11:06:26	
    	 * @修改人 Administrator
    	 * @修改时间 2014-11-24上午11:06:26	
    	 * @修改备注 
    	 * @since
    	 * @throws
    	 */
    	@JavascriptInterface
    	public String  getUserInfo()
    	{
    		Log.d(TAG, "Callback :getUserInfo");
    		JSONObject object = new JSONObject();// 一个user对象，使用一个JSONObject对象来装
	    	UserImpl userimpl = new UserImpl(mContext);
			LoginBean logininfo = userimpl.getLoginInfo(ConfigManager.USERINFO_INII_PATH);
			if(logininfo!=null && logininfo.getIslogin()==1){
				try {
						object.put("nick", logininfo.getNick());
						object.put("face", logininfo.getFace());
						object.put("openid", logininfo.getOpenid());
						object.put("access_token", logininfo.getAccesstoken());
						object.put("state", 0);
				} catch (JSONException e) {
						e.printStackTrace();
				}
				
			}else{
				try {
					object.put("msg","未登录");
					object.put("state", 1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			String jsonString = null;
			jsonString = object.toString(); // 把JSONObject转换成json格式的字符串
			Log.d(TAG, "Callback :getUserInfo:" + jsonString);
			return jsonString;
    	}
    	
    	@JavascriptInterface
    	public void  onAuthed(int ret,String guid,String userid)
    	{
    		System.out.println("WebViewActivity onAuthed ret= " + ret + "guid=" + guid + "userid = " + userid);
    		if(ret==0){
    			AuthImpl authImpl = new AuthImpl(mContext,null);
    			authImpl.SavePivosAuthInfo(ConfigManager.PIVOS_AUTH_RESULT,String.valueOf(ret),guid,userid);
    			String cmd = "chmod 666 " + ConfigManager.PIVOS_AUTH_RESULT;
    			Utils.runCommand(cmd);
    			enterBox();
    		}
    		
    	}
	@JavascriptInterface
	public void log(String msg)
	{
		System.out.println(msg);
	}
	@JavascriptInterface
	public void onScan(String msg)
	{
		System.out.println(msg);
	}
	@JavascriptInterface
	public void closePage()
	{
		System.out.println("on callback closePage");
		TencentVideoLoginActivity.this.finish();
	}
	@JavascriptInterface
	public void writePayInfo(String openid,String month,String vipbid)
	{
		System.out.println("on callback writePayInfo openid = " + openid + "month = " + month + "vipbid = " + vipbid);
		TencentVideoLoginActivity.this.finish();
	}
	@JavascriptInterface
	public void jumpAppPage(String itype,String sparam)
	{
		System.out.println("on callback jumpAppPage");
		TencentVideoLoginActivity.this.finish();
	}
    	
    }
    
	
	class StartPlayAsync extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			String url = params[0];
			startxbmcPlay(url);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			
		}
	}
	
	private void startxbmcPlay(String playinfo){
		
		TVK_VideoUrlInfo mUrlinfo=null;
		try {
			mUrlinfo = parseGetUrlRet(playinfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(mUrlinfo==null ) return ;
		String realurl = null;
		if(mUrlinfo.getFormat()==DownloadFacadeEnum.MP4_5MIN || mUrlinfo.getFormat()==DownloadFacadeEnum.MP4_20MIN)
		{
			String xmlinfo = mUrlinfo.getVideoUrl();
			if (xmlinfo.contains("<?xml"))
			{
				DocumentBuilderFactory factory = DocumentBuilderFactory
				        .newInstance();
				DocumentBuilder builder;
				try {
					builder = factory.newDocumentBuilder();
					Document doc = builder.parse(new InputSource(new StringReader(
							xmlinfo)));
					NodeList localNodes = doc.getElementsByTagName("LOCALURL");
					realurl = localNodes.item(0).getTextContent();
					realurl = realurl+ "&clipid=1";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else
		{
			realurl = mUrlinfo.getVideoUrl();
		}
	
		Intent it = new  Intent(Intent.ACTION_VIEW);
		Uri uri = Uri.parse(realurl);
		ComponentName name=new ComponentName("com.pivos.tofu", "com.pivos.tofu.Main");
		//ComponentName name=new ComponentName("com.android.gallery3d", "com.android.gallery3d.app.MovieActivity"); 
		it.setComponent(name);
		it.setDataAndType(uri , "video/mp4");
		startActivity(it); 
		
	}
	
	private TVK_VideoUrlInfo parseGetUrlRet(String result) throws Exception
	{
		Log.i("playsdk", "geturl retrun = "+result);
		
		TVK_VideoUrlInfo urlInfo = null;
		
		// check return is xml or json
		if(result.contains("<?xml"))
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory
			        .newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(
			        result)));
			
			Element root = doc.getDocumentElement();  
            if (root == null) 
            	return null;
            
            // errcode
            int errcode = Integer.parseInt(
            		root.getElementsByTagName("errcode").item(0).getFirstChild().getTextContent()
            		); 
    		if(errcode!=0)
    		{
    			return null;
    		}
    		
            long dataid = Long.parseLong(
            		root.getElementsByTagName("dataid").item(0).getFirstChild().getTextContent()
            		);
            String vid = root.getElementsByTagName("vid").item(0).getFirstChild().getTextContent()
            		;  
            int format = Integer.parseInt(
            		root.getElementsByTagName("format").item(0).getFirstChild().getTextContent()
            		);
            String videourl = root.getElementsByTagName("videourl").item(0).getFirstChild().getTextContent()
            		;  
            String defncur = root.getElementsByTagName("defncur").item(0).getFirstChild().getTextContent();
            
            Log.i("playsdk","errcode=" + errcode + "dataid = " + dataid + "vid=" + vid + "format = " + format + "videourl=" + videourl);
    		urlInfo = new TVK_VideoUrlInfo();
    		urlInfo.setDataId(dataid);
    		urlInfo.setFormat(format);
    		urlInfo.setCurDefinition(defncur);
    		urlInfo.setVideoUrl(videourl);
    		
		}
		else
		{
			//TODO: parse json
		}
		return urlInfo;
	}
	private void PivosRegDev() {
		String tvid = DeviceUtils.getTvID(mContext);
		String wifimac = DeviceUtils.getWifiMac(mContext);
		String devmd5 = DeviceUtils.getFirmwareMd5();
		String gitvsn = DeviceUtils.getGITVSN();
		String mac_wire = DeviceUtils.getEthernetMac();
		String randomvalue1 = Utils.getFixLenthString(8);
		String randomvalue2 = Utils.getFixLenthString(8);
		String randomvalue3 = Utils.getFixLenthString(8);
		String randomvalue4 = Utils.getFixLenthString(8);
		final String random = randomvalue1 + randomvalue2 + randomvalue3 + randomvalue4;
		AuthImpl authImpl = new AuthImpl(mContext,null);
		String sign =ConfigManager.CID+"|"+ConfigManager.TERMID+"|"+mac_wire+"|"+wifimac+"|"+random ;
		authImpl.PivosRegister(ServerManager.PIVOS_AUTH_SERVER, ConfigManager.CID, ConfigManager.TERMID, sign);
	}
	private void PivosGetGuid() {
		String tvid = DeviceUtils.getTvID(mContext);
		String wifimac = DeviceUtils.getWifiMac(mContext);
		String devmd5 = DeviceUtils.getFirmwareMd5();
		String sn = SystemInfoManager.readFromNandkey("usid");
		if(sn==null ||sn.isEmpty()){
			sn = "1234567890";
		}
		String mac_wire = DeviceUtils.getEthernetMac();
		String ver = DeviceUtils.getVersionInfo();
		AuthImpl authImpl = new AuthImpl(mContext,new IAuthImpl(){

			@Override
			public void onGuidFinished(GuidBean guidbean) {
				// TODO Auto-generated method stub
				try {
					if(guidbean!=null){
						ResultBean result = guidbean.getResult();
						GuidDataBean data = guidbean.getData();
						System.out.println("guid = " + data.getGuid() + "Guid_secret=" + data.getGuid_secret());
						//GuidInfoBean info = new GuidInfoBean();
						//info.setGuid(data.getGuid());
						//info.setGuid_secret(data.getGuid_secret());
						//info.setTvskey("");
						//SaveGuidInfo(info);
						url+=data.getGuid();
						//url = "http://www.go3c.tv:8060/test/index.html";
						System.out.println("WebViewActivity load url= " + url);
						mWebView.loadUrl(url);
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onTvsKeyFinished(TvsKeyBean tvskeybean) {
				// TODO Auto-generated method stub
				
			}
			
		});
		authImpl.PivosGetGuid(ServerManager.PIVOS_AUTH_SERVER, tvid, ver,wifimac, devmd5,sn,mac_wire);
	}
	private void enterBox(){
		 Intent i = new Intent(mContext, EnterBoxActivity.class);
        startActivity(i);
        TencentVideoLoginActivity.this.finish();
	}
}
