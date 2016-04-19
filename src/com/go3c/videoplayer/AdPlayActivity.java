package com.go3c.videoplayer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.tencent.dmproxy.api.TVK_IDMProxy;
import com.tencent.dmproxy.api.TVK_IDMProxy.OnFullScreenClickListener;
import com.tencent.dmproxy.api.TVK_IDMProxy.OnGetRecieveUrlListener;
import com.tencent.dmproxy.api.TVK_IDMProxy.OnReportPlayPositionListener;
import com.tencent.dmproxy.api.TVK_BuildConfig;
import com.tencent.dmproxy.api.TVK_UserInfo;
import com.tencent.dmproxy.api.TVK_VideoUrlInfo;
import com.tencent.dmproxy.logic.DmproxyManager;
import com.tencent.dmproxy.logic.SdkUtil;
import com.tencent.qqlive.sdk.TVKSdkManager;
import com.tencent.videoad.adInfo;
import com.txbox.settings.impl.GetAddressImpl;
import com.txbox.txsdk.R;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.MediaController.MediaPlayerControl;

public class AdPlayActivity extends Activity{
	public static final int CGI_MODULE_GETVINFO = 1;

	private String defn = "";
	private VideoView surfaceView;


	private String videourl;
	private String videurlString;
	
	private Boolean isAdBoolean;
	
	private TVK_IDMProxy dmProxy = null;

	private int curDateId;
	private int adIndex = 0;
	private int videoIndex = 0;
	private int videoCount = 0;
	private ArrayList<adInfo> adlistStrings;
	private View adTitle;
	private TVK_VideoUrlInfo curUrl;
	private int position=0;
	private int mDuration = 0;
	private int PlayingadIndex = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adplay);
		
		dmProxy = new DmproxyManager();
		TVK_UserInfo userInfo = new TVK_UserInfo();

		dmProxy.setOnGetRecieveUrl(onGetUrlListener);
		dmProxy.setOnFullScreenClick(onFullScreenClickListener);
		dmProxy.setOnReportPlayPositionListener(onReportPlayPositionListener);
		surfaceView = (VideoView)findViewById(R.id.surfaceView1);
		
		
		Uri uri = getIntent().getData();
		Log.i("debug","uri = " + uri.toString());
		if(uri!=null){
			String vid= uri.toString().substring(7);
			Log.i("debug","vid = " + vid);
			//dmProxy.buildPlayUrlReqCGI(AdPlayActivity.this,userInfo,vid,"",defn);
			//dmProxy.buildPlayUrlReqCGI(AdPlayActivity.this,userInfo,vid4,cid4,defn);
			dmProxy.getAdList(AdPlayActivity.this,vid,"",userInfo);
		}else AdPlayActivity.this.finish();
		
	}

	OnGetRecieveUrlListener onGetUrlListener = new OnGetRecieveUrlListener() {

		@Override
		public void onGetRecieveUrl(TVK_VideoUrlInfo url) {
			// TODO Auto-generated method stub
			//url 包含 视频和广告列表的本地代理URL
			Log.i("DMProxy_Sdk","haitend123");
			curUrl = url;
			if(url == null || url.isUseAd()==false){
				 AdPlayActivity.this.finish();
			}
			
			dmProxy.adViewAttachTo(surfaceView);
			dmProxy.informAdPrepared();
			String realurl = null;
			adlistStrings = url.getAdList();
			Log.i("DMProxy_Sdk","haitend123");
			adIndex = url.getAdCount();
			
			Log.i("DMProxy_Sdk","haitend adnum = " + adIndex);
			for(int i = 0; i < adIndex; i++){
				Log.i("debug",String.valueOf(i) +  "haitend"+ ((adInfo)(adlistStrings.get(i))).getLocalUrl());
			}
  		
			if (url.isUseAd() && adIndex > 0) {	
				realurl = ((adInfo)(adlistStrings.get(0))).getLocalUrl();	
				curDateId = Integer.valueOf(((adInfo)(adlistStrings.get(0))).getDataID());
				adIndex--;
			}else{
				 AdPlayActivity.this.finish();
			}
			

			Log.i("DMProxy_Sdk", "realUrl" + realurl + "dataId" + String.valueOf(curDateId));
		

			surfaceView.setOnCompletionListener(onCompletionListener);
			surfaceView.setOnErrorListener(mediaErrorListener);
			surfaceView.setOnPreparedListener(onPreparedListener);
			
			surfaceView.setVideoPath(realurl);
		}
		@Override
		public void onGetRecieveUrlError(int errCode,String msg){
			// TODO Auto-generated method stub
			Log.i("debug","errorCode = " + errCode + "errMessage = " + msg);
			
			AdPlayActivity.this.finish();
		};
	};
	
    MediaPlayer.OnPreparedListener onPreparedListener = new OnPreparedListener() {
        
        @Override
        public void onPrepared(MediaPlayer mp) {          
        	surfaceView.start();    
            
            if(PlayingadIndex>0){
				adInfo info = adlistStrings.get(adIndex+1);
				mDuration += info.getDurtion();
			}
        }
    };
	
	
	MediaPlayer.OnErrorListener mediaErrorListener = new OnErrorListener() {
		
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			// TODO Auto-generated method stub
			Log.i("debug","haitend error" + String.valueOf(what));
			AdPlayActivity.this.finish();
			return false;
		}
	};
	
	MediaPlayer.OnCompletionListener onCompletionListener = new OnCompletionListener() {
		
		@Override
		public void onCompletion(MediaPlayer mp) {
			Log.i("debug","onCompletion adIndex = !" + adIndex);
			if (curUrl!=null && curUrl.isUseAd() && adIndex >0) {	
				String realurl = ((adInfo)(adlistStrings.get(adIndex - 1))).getLocalUrl();	
				curDateId = Integer.valueOf(((adInfo)(adlistStrings.get(adIndex - 1))).getDataID());
				adIndex--;
				PlayingadIndex++;
				
				surfaceView.stopMedia();
				surfaceView.setVideoPath(realurl);
				
			}else{
				
				AdPlayActivity.this.finish();
			}
		}
	};
	TVK_IDMProxy.OnFullScreenClickListener onFullScreenClickListener = new OnFullScreenClickListener() {
		
		@Override
		public void onFullScreenClick() {
			// TODO Auto-generated method stub
			Log.i("debug","fullscreenclick!");
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);	
		}
	};
	TVK_IDMProxy.OnReportPlayPositionListener onReportPlayPositionListener = new OnReportPlayPositionListener() {
		@Override
		public int onReportPlayPosition() {
			// TODO Auto-generated method stub
			//Log.i("debug","onReportPlayPosition!");
			if(surfaceView != null) {
				int pos =surfaceView.getCurrentPosition();
				if(PlayingadIndex>0 && pos>0){
					position = mDuration+pos;
				}else{
					position= pos;
				}
				return position;
				//return mPlayer.getCurrentPosition();
			}
			return 0;
		}
	};
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (curUrl!=null && curUrl.isUseAd() && adIndex > 0) {	
			dmProxy.informAdFinished();
			surfaceView.stopPlayback();
			if(curUrl != null){
				for(int i = 0; i < curUrl.getAdCount(); i++){
					if(Integer.parseInt(((adInfo)(adlistStrings.get(i))).getDataID()) != 0){
						dmProxy.buildStopPlayReqCGI(Integer.parseInt(((adInfo)(adlistStrings.get(i))).getDataID()));
					}
					Log.i("debug","stopPlay" + Integer.parseInt(((adInfo)(adlistStrings.get(i))).getDataID()));
				}
				dmProxy.buildStopPlayReqCGI(curUrl.getDataId());
				Log.i("debug","stopPlay" + curUrl.getDataId());
			}
			
		}
	}
	
}
