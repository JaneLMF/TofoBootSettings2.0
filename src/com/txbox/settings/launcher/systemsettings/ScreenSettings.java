package com.txbox.settings.launcher.systemsettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.txbox.txsdk.R;
import com.txbox.settings.adapter.ScreenPMFBAdapter;
import com.txbox.settings.common.LocalInfor;
import com.txbox.settings.common.OutPutModeManager;
import com.txbox.settings.launcher.ScreenCalibrationActivity;
import com.txbox.settings.popupwindow.UpdateSureOrNotPop;
import com.txbox.settings.popupwindow.UpdateSureOrNotPop.IButtonClick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScreenSettings extends Activity{
	
	private RelativeLayout pmjz_rl;
	private LinearLayout pmfb_ll;
	private ListView pmfbList;
	private TextView pmfb_big,pmfb_little;
	private ArrayList<Map<String, String>> dataList = new ArrayList<Map<String,String>>();
	private ScreenPMFBAdapter mAdapter = null;
	private Context mContext;
	private int curPosition;
	ArrayList<Map<String, String>> mData  = new ArrayList<Map<String,String>>();
	private UpdateSureOrNotPop mUpdateSureOrNotPop = null;
	private OutPutModeManager outputModMgr = null;
	public ArrayList<String> mOutputModList = null;
	String outPort = "hdmi";
	
	private TextView localTime;
	 private LocalInfor mLocalInfor;
	 private static final int UPDATETIME = 1024;
	    
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
		setContentView(R.layout.screen_settings);
		mContext = this;
		mLocalInfor = new LocalInfor(mContext);
		initView();
		initData();
		initParam();
		initOnClick();
		initOnKey();
		pmfbList.setSelection(mAdapter.getCount()-1);
		pmjz_rl.requestFocus();
	}

	private void initView(){
		pmjz_rl = (RelativeLayout) findViewById(R.id.pmjz_rl);
		pmfb_ll = (LinearLayout) findViewById(R.id.pmfb_ll);
		pmfbList = (ListView) findViewById(R.id.pmfbList);
		pmfb_big = (TextView) findViewById(R.id.pmfb_big);
		pmfb_little = (TextView) findViewById(R.id.pmfb_little);
		localTime = (TextView) findViewById(R.id.localTime);
		h.sendEmptyMessage(UPDATETIME);
	}
	
	private void initOnKey(){
		pmfb_ll.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if(keyCode==KeyEvent.KEYCODE_DPAD_UP&&event.getAction()==KeyEvent.ACTION_DOWN){
					if (curPosition > 0) {
						curPosition--;
					}else if (curPosition == 0) {
						curPosition = dataList.size() - 1;
					}
					changeAdapterData(curPosition);
					return true;
				}else if(keyCode==KeyEvent.KEYCODE_DPAD_DOWN&&event.getAction()==KeyEvent.ACTION_DOWN){
					if (curPosition < dataList.size() - 1) {
						curPosition++;
					}else if (curPosition == dataList.size() - 1){
						curPosition = 0;
					}
					changeAdapterData(curPosition);
					return true;
				}else if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
					startDialog();
					return true;
				}
				
				return false;
			}
		});
	}
	private void initOnClick(){
		pmjz_rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startPMJZ();
			}
		});
	}
	private void initParam(){
		pmfbList.setFocusable(false);// 程序中设置，xml中设置不起作用
//		mAdapter = new ScreenPMFBAdapter(mContext, dataList);
//		pmfbList.setAdapter(mAdapter);
		int pageOneCurItem = outputModMgr.getCurrentSelectItemID();
		changeAdapterData(pageOneCurItem);
		curPosition = pageOneCurItem;
	}
	private void initData(){
		
			outputModMgr = new OutPutModeManager(this, outPort);
			mOutputModList = outputModMgr.getOutPutModeList();
		
		dataList.clear();
		for (int i = 0; i < mOutputModList.size(); i++) {
			String[] str = new String[2];
			str = getStrings(mOutputModList.get(i));
			Map<String, String> map = new HashMap<String, String>();
			 map.put("Big", str[0]);
			if(str.length > 1){
			 map.put("Little", str[1]);
			}else{
				map.put("Little", "");
			}
			dataList.add(map);
		}
	}
	
	private String[] getStrings(String item){
		String[] str = item.split(" ");
		return str;
	}
	private void changeAdapterData(int index){
		int size = dataList.size();
		mData.clear();
		for (int i = index+1; i < size; i++) {
			mData.add(dataList.get(i));
		}
		for (int i = 0; i < index; i++) {
			mData.add(dataList.get(i));
		}
		if (mAdapter == null) {
			 mAdapter = new ScreenPMFBAdapter(mContext, mData);
			 pmfbList.setAdapter(mAdapter);
			
		} else {
			mAdapter.notifyDataSetChanged();
		}
		pmfb_big.setText(dataList.get(index).get("Big"));
		pmfb_little.setText(dataList.get(index).get("Little"));
		pmfbList.setSelection(mAdapter.getCount()-1);
	}
	
	private void startPMJZ(){
		Intent i = new Intent();
		i.setClass(mContext, ScreenCalibrationActivity.class);
		startActivity(i);
		overridePendingTransition(R.anim.zoout, R.anim.zoin);
	}
	
	private void startDialog(){
		mUpdateSureOrNotPop = new UpdateSureOrNotPop(mContext,"screen",mContext.getString(R.string.screen_pop_content),
				mContext.getString(R.string.screen_pop_save_text),mContext.getString(R.string.system_update_pop_cancel_text));
		mUpdateSureOrNotPop.showListPop();
		mUpdateSureOrNotPop.setOnButtonClick(new IButtonClick() {
			
			@Override
			public void onSureClick() {
				// TODO rest factory
				outputModMgr.selectItem(curPosition);
				ScreenSettings.this.finish();
			}
			
			@Override
			public void onCancelClick() {
				// TODO cancel
				ScreenSettings.this.finish();
			}
		});
	}
	
	
}
