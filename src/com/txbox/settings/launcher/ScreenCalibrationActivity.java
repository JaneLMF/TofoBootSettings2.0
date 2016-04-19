package com.txbox.settings.launcher;

import com.txbox.txsdk.R;
import com.txbox.settings.launcher.bootsettings.DetectNetworkActivity;
import com.txbox.settings.mbx.api.ScreenPositionManager;
import com.txbox.settings.utils.DecodeImgUtil;
import com.txbox.settings.utils.ScaleAnimEffect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ScreenCalibrationActivity extends Activity {
	
	ScreenPositionManager mScreenPositionManager = null;
	private ImageView pmjz;
	private Bitmap b;
	private RelativeLayout pmjz_rl;
	private int rate;
	private ScaleAnimEffect mAnimEffect;
	private float preX;
	private float preY;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pageone_pmjz);
		pmjz = (ImageView) findViewById(R.id.pmjz);
		pmjz_rl = (RelativeLayout) findViewById(R.id.pmjz_rl);
		b = new DecodeImgUtil(this, R.raw.pmjz_bg).getBitmap();
		BitmapDrawable bd= new BitmapDrawable(b);
		pmjz_rl.setBackground(bd);
		b = new DecodeImgUtil(this, R.raw.pmjz_content).getBitmap();
		BitmapDrawable bd1= new BitmapDrawable(b);
		pmjz.setImageDrawable(bd1);
		mScreenPositionManager = new ScreenPositionManager(ScreenCalibrationActivity.this);
		mScreenPositionManager.initPostion();	
		
		rate = mScreenPositionManager.getRateValue();
		mAnimEffect = new ScaleAnimEffect();
		preX = rate/100.0f;
		preY = rate/100.0f;
		initImageViewRate(preX);
		mScreenPositionManager.zoomByPercent(100);
	}
	
	
	@Override
   public boolean onKeyDown(int keyCode, KeyEvent event){
		  if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
//			  mScreenPositionManager.zoomIn();
			  ScaleRight();
		  } else  if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
//			  mScreenPositionManager.zoomOut();
			  ScaleLeft();
		  }else if(keyCode == KeyEvent.KEYCODE_ENTER||keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
			  mScreenPositionManager.zoomByPercent(rate);
			  mScreenPositionManager.savePostion();  
			  this.finish();
		  }else if(keyCode == KeyEvent.KEYCODE_BACK){
			  mScreenPositionManager.zoomByPercent(rate); 
			  mScreenPositionManager.savePostion();
		  }
			  
	      return super.onKeyDown(keyCode, event);
	   
   }
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(b!=null) b.recycle();
		super.onDestroy();
	}
	
	private void ScaleRight(){
		if(rate<=80)return;
		rate--;
			mAnimEffect.setAttributs(preX, rate/100.0f, preY, rate/100.0f, 100);
			Animation animation = mAnimEffect.createAnimation();
			pmjz.startAnimation(animation);
			preX = rate/100.0f;
			preY = rate/100.0f;
	}
	private void ScaleLeft(){
		if(rate >= 100)return;
			rate++;
			mAnimEffect.setAttributs(preX, rate/100.0f, preY, rate/100.0f, 100);
			Animation animation = mAnimEffect.createAnimation();
			pmjz.startAnimation(animation);
			preX = rate/100.0f;
			preY = rate/100.0f;
	}
	private void initImageViewRate(float rate1){
	
		mAnimEffect.setAttributs(preY, preY, preY, preY, 0);
		Animation animation = mAnimEffect.createAnimation();
		pmjz.startAnimation(animation);
	
	}
}
