package com.txbox.settings.popupwindow;

import com.txbox.settings.utils.DensityUtil;
import com.txbox.txsdk.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public class JudgeResolutionPop {
	
	private Context mContext;
	private PopupWindow JudgeResolutionPop;
	private View Container = null;
	private String Resolution = "";
	private long avgSpeed;
	private TextView avgSpeedView;
	private TextView ResolutionView;
	private Button rebutton;
	public IButtonClick mIButtonClick;
	public JudgeResolutionPop(Context mContext,long avgSpeed,String Resolution){
		this.mContext = mContext;
		this.Resolution = Resolution;
		this.avgSpeed = avgSpeed;
	}
	public void showListPop(){
		if(JudgeResolutionPop==null){
			JudgeResolutionPop = initTwoDimensionalCodePop();
		}
		if(JudgeResolutionPop!=null &&JudgeResolutionPop.isShowing()){
			JudgeResolutionPop.dismiss();
		}else{
			JudgeResolutionPop.showAtLocation(Container, Gravity.CENTER, 0, 30);
		}
	}
	public interface IButtonClick{
		void OnJudgeButtonClick();
	}
	
	public void setReJudgeListener(IButtonClick mIButtonClick){
		this.mIButtonClick = mIButtonClick;
	}
	private PopupWindow initTwoDimensionalCodePop(){
		PopupWindow Pop = null;
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		Container = layoutInflater.inflate(R.layout.judge_resolution_pop, null);
		avgSpeedView = (TextView) Container.findViewById(R.id.speed);
		ResolutionView = (TextView) Container.findViewById(R.id.Resolution);
		rebutton = (Button) Container.findViewById(R.id.rebutton);
		initParams();
		Pop = new PopupWindow(Container, DensityUtil.dip2px(mContext, 400),DensityUtil.dip2px(mContext, 400), false);
		Pop.setBackgroundDrawable(new BitmapDrawable());//
		Pop.setOutsideTouchable(true);
		Pop.setFocusable(true); 
		return Pop;
	}
	private void initParams(){
		avgSpeedView.setText(mContext.getString(R.string.detecting_speed_present_speed)+avgSpeed+"KB/S");
		ResolutionView.setText(Resolution);
		rebutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mIButtonClick.OnJudgeButtonClick(); 
				JudgeResolutionPop.dismiss();
			}
		});
	}
}
