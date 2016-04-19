package com.txbox.settings.popupwindow;

import com.txbox.txsdk.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public class LogoutSureOrNotPop {
	
	private Context mContext;
	private PopupWindow SureOrNotPop;
	private View Container = null;
	private Handler myHander;
	private Button sureBtn;
	private Button cancelBtn;
	private static final int LOGOUT = 1000;
	public LogoutSureOrNotPop(Context mContext,Handler myHander){
		this.mContext = mContext;
		this.myHander = myHander;
	}
	
	public void showListPop(String message){
		if(SureOrNotPop==null){
			SureOrNotPop = initSureOrNotPop(message);
		}
		if(SureOrNotPop!=null&&SureOrNotPop.isShowing()){
			SureOrNotPop.dismiss();
		}else{
			SureOrNotPop.showAtLocation(Container, Gravity.BOTTOM, 0, 30);
		}
	}
	
	private PopupWindow initSureOrNotPop(String message){
		PopupWindow Pop = null;
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		Container = layoutInflater.inflate(R.layout.logout_pop_sure_cancel, null);
		TextView titleView = (TextView)Container.findViewById(R.id.message_title);
		titleView.setText(message);
		sureBtn = (Button) Container.findViewById(R.id.sure);
	    cancelBtn = (Button) Container.findViewById(R.id.cancel);
		initOnClick();
		initFoucs();
		Pop = new PopupWindow(Container, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT, false);
		Pop.setBackgroundDrawable(new BitmapDrawable());//
		Pop.setOutsideTouchable(true);
		Pop.setFocusable(true); 
		return Pop;
	}
	
	private void initOnClick(){
		sureBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				myHander.removeMessages(LOGOUT);
				myHander.sendEmptyMessage(LOGOUT);
				SureOrNotPop.dismiss();
			}
		});
		
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SureOrNotPop.dismiss();
			}
		});
	}
	private void initFoucs(){
		sureBtn.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
					sureBtn.setBackgroundResource(R.drawable.btn_focus_bg);
					sureBtn.setTextColor(Color.parseColor("#FFFFFF"));
				}else{
					sureBtn.setBackgroundResource(0);
					sureBtn.setTextColor(Color.parseColor("#4c5c6a"));
				}
			}
		});
		cancelBtn.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
					cancelBtn.setBackgroundResource(R.drawable.btn_focus_bg);
					cancelBtn.setTextColor(Color.parseColor("#FFFFFF"));
				}else{
					cancelBtn.setBackgroundResource(0);
					cancelBtn.setTextColor(Color.parseColor("#4c5c6a"));
				}
			}
		});
	}
	
	public boolean isShowing(){
		if(SureOrNotPop!=null) return SureOrNotPop.isShowing();
		return false;
	}
}
