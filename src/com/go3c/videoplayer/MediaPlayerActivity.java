package com.go3c.videoplayer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import com.txbox.settings.impl.GetAddressImpl;
import com.txbox.settings.popupwindow.LogoutSureOrNotPop;
import com.txbox.txsdk.R;



public class MediaPlayerActivity extends Activity {
	VideoView mVideoView;
	String stopurl = null;
	private static final int LOGOUT = 1000;
	private static final int SHOW_POP_MESG= 1001;
	private static boolean isPlaying = false;
	private LogoutSureOrNotPop mLogoutPop;
	Handler h = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case LOGOUT:
				MediaPlayerActivity.this.finish();
			case SHOW_POP_MESG:
				int duration = mVideoView.getDuration();
				int width = mVideoView.getWidth();
				int height = mVideoView.getHeight();
				System.out.println("video height = " + height + "width=" + width + "duration=" + duration);
				if(duration>0 && width >0 && height >0)
					isPlaying = true;
				else{
					String message = getResources().getString(R.string.try_not_support);
					mLogoutPop  = new LogoutSureOrNotPop(MediaPlayerActivity.this,h);
					mLogoutPop.showListPop(message);
				}
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.video);
		Intent i = getIntent();
		Uri uri= i.getData();
		stopurl= i.getStringExtra("stopurl");
		mVideoView = (VideoView) findViewById(R.id.vv);
		mVideoView.setOnCompletionListener(new OnCompletionListener(){
			@Override
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				
				MediaPlayerActivity.this.finish();
			}
			
		});
		mVideoView.setOnPreparedListener(new OnPreparedListener(){
			@Override
			public void onPrepared(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				mVideoView.start();
				int duration = mVideoView.getDuration();
				int width = mVideoView.getWidth();
				int height = mVideoView.getHeight();
				System.out.println("video height = " + height + "width=" + width + "duration=" + duration);
				if(duration>0 && width >0 && height >0)
					isPlaying = true;
				else{
					String message = getResources().getString(R.string.try_not_support);
					mLogoutPop  = new LogoutSureOrNotPop(MediaPlayerActivity.this,h);
					mLogoutPop.showListPop(message);
				}
			}
			
		});
		mVideoView.setOnErrorListener(new OnErrorListener(){
			@Override
			public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				String message = getResources().getString(R.string.try_not_support);
				mLogoutPop  = new LogoutSureOrNotPop(MediaPlayerActivity.this,h);
				mLogoutPop.showListPop(message);
				//MediaPlayerActivity.this.finish();
				return false;
			}
			
		});
		if(uri!=null){
			mVideoView.setVideoPath(uri.toString());
		}
		
		

	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("MediaPlayerActivity onDestroy");
		if(mLogoutPop!=null && mLogoutPop.isShowing())
		if(stopurl!=null && isPlaying ==true){
			GetAddressImpl addressImpl = new GetAddressImpl();
			addressImpl.stopTXPlay(stopurl);
		}
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		System.out.println("MediaPlayerActivity onStart");
		
		//h.sendEmptyMessageDelayed(SHOW_POP_MESG, 2000);
	}
}
