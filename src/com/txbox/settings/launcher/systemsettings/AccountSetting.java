package com.txbox.settings.launcher.systemsettings;

import com.txbox.txsdk.R;
import com.txbox.settings.common.LocalInfor;
import com.txbox.settings.popupwindow.UpdateSureOrNotPop;
import com.txbox.settings.popupwindow.UpdateSureOrNotPop.IButtonClick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.KeyEvent;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

public class AccountSetting extends Activity{
	
	private LinearLayout installUnknowApp_ll,TXAccount_ll,webchatAccount_ll;
	private TextView installUnknowApp_text,installUnknowApp_state,installUnknowApp_little,
	TXAccount_text,TXAccount_state,TXAccount_little,webchatAccount_text,webchatAccount_state,webchatAccount_little;
	private String[] state = {"关闭","允许"};
	private int keyPos = 0;
	private Context mContext;
	 private UpdateSureOrNotPop mUpdateSureOrNotPop = null;
	 private TextView localTime;
	 private LocalInfor mLocalInfor;
	 private static final int UPDATETIME = 1024;
	 private boolean isClicked = false;
	    Handler h = new Handler(){
	    	public void handleMessage(android.os.Message msg) {
	    		switch (msg.what) {
				case UPDATETIME:
					localTime.setText(mLocalInfor.getLocalTime());
					h.sendEmptyMessageDelayed(UPDATETIME, 5*1000);
					break;

				default:
					break;
				}
	    	};
	    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_settings);
		mLocalInfor = new LocalInfor(mContext);
		mContext = this;
		initView();
		initOnKey();
		installUnknowApp_ll.requestFocus();
	}
	private void initView(){
		installUnknowApp_ll = (LinearLayout) findViewById(R.id.installUnknowApp_ll);
		TXAccount_ll = (LinearLayout) findViewById(R.id.TXAccount_ll);
		webchatAccount_ll = (LinearLayout) findViewById(R.id.webchatAccount_ll);
		installUnknowApp_text = (TextView) findViewById(R.id.installUnknowApp_text);
		installUnknowApp_state = (TextView) findViewById(R.id.installUnknowApp_state);
		installUnknowApp_little = (TextView) findViewById(R.id.installUnknowApp_little);
		TXAccount_text = (TextView) findViewById(R.id.TXAccount_text);
		TXAccount_state = (TextView) findViewById(R.id.TXAccount_state);
		TXAccount_little = (TextView) findViewById(R.id.TXAccount_little);
		webchatAccount_text = (TextView) findViewById(R.id.webchatAccount_text);
		webchatAccount_state = (TextView) findViewById(R.id.webchatAccount_state);
		webchatAccount_little = (TextView) findViewById(R.id.webchatAccount_little);
		localTime = (TextView) findViewById(R.id.localTime);
		
		h.sendEmptyMessage(UPDATETIME);
	}
	
	
	
	private void initOnKey(){
		installUnknowApp_ll.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				String item = installUnknowApp_state.getText().toString();
				if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT && event.getAction()==KeyEvent.ACTION_DOWN){
					keyPos = getCurKeyPos(state,item);
					keyPos--;
					if(keyPos < 0){
						keyPos = state.length - 1;
					}
					installUnknowApp_state.setText(state[keyPos]);
					if(state[keyPos].equals(mContext.getString(R.string.safe_setting_state))){
						startDialog();
					}else {
						setDisableInstallUnknowApp();
					}
				}else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction()==KeyEvent.ACTION_DOWN){
					keyPos = getCurKeyPos(state,item);
					keyPos ++;
					if(keyPos > state.length - 1){
						keyPos = 0;
					}
					installUnknowApp_state.setText(state[keyPos]);
					if(state[keyPos].equals(mContext.getString(R.string.safe_setting_state))){
						startDialog();
					}else{
						setDisableInstallUnknowApp();
					}
				}
				return false;
			}
		});
	}
	
	private int getCurKeyPos(String[] str,String item){
		for (int i = 0; i < str.length; i++) {
			if(str[i].equals(item)){
				return i;
			}
		}
		return 0;
	}
	private void setDisableInstallUnknowApp(){
		Settings.Secure.putInt(getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS, 0);
	}
	private void startDialog(){
		mUpdateSureOrNotPop = new UpdateSureOrNotPop(mContext,"",mContext.getString(R.string.safe_setting_pop_content),
				mContext.getString(R.string.system_update_pop_sure_text),mContext.getString(R.string.system_update_pop_cancel_text));
		mUpdateSureOrNotPop.showListPop();
		isClicked = false;
		mUpdateSureOrNotPop.setOnButtonClick(new IButtonClick() {
			
			@Override
			public void onSureClick() {
				// TODO rest factory
				Settings.Secure.putInt(getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS, 1);
				isClicked = true;
			}
			
			@Override
			public void onCancelClick() {
				// TODO cancel
				setDisableInstallUnknowApp();
				installUnknowApp_state.setText(state[0]);
				isClicked = true;
			}
		});
		
		mUpdateSureOrNotPop.getPop().setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				if(installUnknowApp_state.getText().toString().equals(state[1])&&!isClicked){
					installUnknowApp_state.setText(state[0]);
					
				}
			}
		});
	}
	
}
