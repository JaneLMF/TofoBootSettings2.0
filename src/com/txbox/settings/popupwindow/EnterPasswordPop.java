package com.txbox.settings.popupwindow;

import java.util.Timer;
import java.util.TimerTask;

import com.txbox.txsdk.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

public class EnterPasswordPop {
	
	private Context mContext;
	private int curItem;
	private PopupWindow EnterPassPop;
	private View Container = null;
	private EditText PasswordEdit;
	private Handler myHander;
	public EnterPasswordPop(Context mContext,int position,Handler myHander){
		this.mContext = mContext;
		this.curItem = position;
		this.myHander = myHander;
	}
	
	
	public void showListPop(){
		if(EnterPassPop==null){
			EnterPassPop = initEnterPassPop();
		}
		if(EnterPassPop!=null&&EnterPassPop.isShowing()){
			EnterPassPop.dismiss();
		}else{
			EnterPassPop.showAtLocation(Container, Gravity.LEFT, 0, 30);
		}
	}
	
	private PopupWindow initEnterPassPop(){
		PopupWindow Pop = null;
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		Container = layoutInflater.inflate(R.layout.enter_wifi_password, null);
		PasswordEdit = (EditText) Container.findViewById(R.id.EditPsd);
		
		Pop = new PopupWindow(Container, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT, true);// ����PopupWindow����
		Pop.setInputMethodMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		Pop.setBackgroundDrawable(new BitmapDrawable());//
		Pop.setOutsideTouchable(true);//
		Pop.setFocusable(true);// 
//		PasswordEdit.requestFocus();
//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		
		return Pop;
	}
}
