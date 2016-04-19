package com.txbox.settings.launcher;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.txbox.txsdk.R;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {
	private WebView mWebView;
	private String mUrl;
	private	boolean mIsTest = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.initData();
		this.setContentView(R.layout.activity_webview);
		this.initWebView(mUrl);
	}
	
	private void initData() {
        Uri uri = getIntent().getData();
        if (uri != null) {
            String dataurl = uri.toString();
            Log.d("WebViewActivity", "dataurl " + dataurl);
            mUrl = getKeyValue(dataurl, "url");
            Log.d("WebViewActivity","mUrl:"+mUrl);
            try {
				mUrl = URLDecoder.decode(mUrl, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				Log.e("WebViewActivity", "error", e);
			}
            Log.d("WebViewActivity","mUrl:"+mUrl);
            if(mUrl != null){
                return;
            }
        }else{
        	Log.d("WebViewActivity","uri is null ...");
        }
        
		if(mIsTest){
        	mUrl = "http://tv.t002.ottcn.com//nba_web//Home//Top//index";
            return;
        }
        finish();
    }
	
	/**
     * data://vid=s00190fcjfl&cid=xfxd9mej2luhfoz&isvip=0&title=芈月传&type=2
     * @param dataurl
     * @param key
     * @return
     */
    private String getKeyValue(String dataurl,String key){
        String cid = "";
        int index = dataurl.indexOf(key);
        if(index>=0){
            cid = dataurl.substring(index+key.length()+1);
            index = cid.indexOf("&");
            if(index>=0){
                cid = cid.substring(0, index);
            }
        }
        return cid;
    }
	
	private void initWebView(String url){
		mWebView = (WebView) findViewById(R.id.webView);
		WebSettings webSettings = mWebView .getSettings();       
		webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setJavaScriptEnabled(true);  
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);
		//覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
				return super.shouldOverrideKeyEvent(view, event);
			}
		});
		mWebView.loadUrl(url);
	}
	
	@Override
	protected void onDestroy() {
		if(mWebView != null){
			mWebView.destroy();	
		}
		super.onDestroy();
	}
}
