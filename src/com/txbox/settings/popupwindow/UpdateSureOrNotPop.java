package com.txbox.settings.popupwindow;

import com.txbox.txsdk.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public class UpdateSureOrNotPop {
	
	private Context mContext;
	private PopupWindow SureOrNotPop;
	private View Container = null;
	private Button sureBtn;
	private Button cancelBtn;
	private IButtonClick mIButtonClick ;
	private TextView content;
	
	private String contentText = "";
	private String sureText = "";
	private String cancelText = "";
	private String type= "";
	public UpdateSureOrNotPop(Context mContext,String type,String contentText,String sureText,String cancelText){
		this.mContext = mContext;
		this.contentText = contentText;
		this.sureText = sureText;
		this.cancelText = cancelText;
		this.type = type;
	}
	
	public void showListPop(){
		if(SureOrNotPop==null){
			SureOrNotPop = initSureOrNotPop();
		}
		if(SureOrNotPop!=null&&SureOrNotPop.isShowing()){
			SureOrNotPop.dismiss();
		}else{
			SureOrNotPop.showAtLocation(Container, Gravity.BOTTOM, 0, 30);
		}
	}
	
	private PopupWindow initSureOrNotPop(){
		PopupWindow Pop = null;
		if(type.equals("screen")){
			LayoutInflater layoutInflater = LayoutInflater.from(mContext);
			Container = layoutInflater.inflate(R.layout.system_update_pop_sure_cancel1, null);
		}else{
			LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		    Container = layoutInflater.inflate(R.layout.system_update_pop_sure_cancel, null);
		}
		sureBtn = (Button) Container.findViewById(R.id.sure);
	    cancelBtn = (Button) Container.findViewById(R.id.cancel);
	    content = (TextView) Container.findViewById(R.id.content);
	    initView();
		initOnClick();
		
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
				mIButtonClick.onSureClick();
				SureOrNotPop.dismiss();
			}
		});
		
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mIButtonClick.onCancelClick();
				SureOrNotPop.dismiss();
			}
		});
	}
	
	public interface IButtonClick{
		void onSureClick();
		void onCancelClick();
	}
	
	public void setOnButtonClick(IButtonClick mIButtonClick){
		this.mIButtonClick = mIButtonClick;
	}
	private void initView(){
		content.setText(contentText);
		sureBtn.setText(sureText);
		cancelBtn.setText(cancelText);
	}
	public PopupWindow getPop(){
		return SureOrNotPop;
	}
}
