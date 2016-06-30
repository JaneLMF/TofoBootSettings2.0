package com.tvxmpp.ui;

import java.util.ArrayList;
import java.util.List;

import com.txbox.txsdk.R;
import com.tencent.oma.log.util.Log;
import com.tvxmpp.WindowService;
import com.tvxmpp.util.DensityUtil;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TableShowView extends View {

	protected static final String TAG = TableShowView.class.getSimpleName();
	private Context c;
	private WindowManager mWM; // WindowManager

	private WindowManager.LayoutParams mWMParams; // WindowManager����
	private View win;
	int tag = 0;
	int oldOffsetX;
	int oldOffsetY;
	
	private TextView text;
	
	//private TextView appnumtips;
	//private ImageView appline;
	//private ListView installapplist;
	//private LinearLayout main_ll;
	
	private boolean isVisible = false;

	
	private Typeface face;
	
	private final int MSG_HIDE_WIN = 1;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			Log.d(TAG, "handleMessage : " + msg.what);
			
			switch (msg.what) {
			case MSG_HIDE_WIN:
				hidewin();
				break;
			}
		};
	};
	public TableShowView(Context context) {
		super(context);
		this.c = context;
		face = Typeface.createFromAsset (c.getAssets() , "LTXHGBK.TTF" );
	}

	public String getText() {
		try {
			if (text != null) {
				return text.getText().toString();
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	public boolean isVisible() {
		return isVisible;
	}

	public void setText(String str) {
		try {
			text.setText(str);
			mWMParams.flags = 40;
			mWM.updateViewLayout(win, mWMParams);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateLocation(int y) {
		mWMParams.y = y;
		mWM.updateViewLayout(win, mWMParams);
	}

	public void cancle(){
		mHandler.removeMessages(MSG_HIDE_WIN);
		this.removeView();
	}
	
	public void removeView() {
		try {
			mWM.removeView(win);
			isVisible = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fun(String strText) {
		hidewin();
		
		isVisible = true;
		mWM = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		win = LayoutInflater.from(c).inflate(R.layout.installation_tips, null);
		win.setBackgroundColor(Color.TRANSPARENT);
		WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
		mWMParams = wmParams;
		text = (TextView) win.findViewById(R.id.text);
		//appnumtips = (TextView) win.findViewById(R.id.appnumtips);
		//installapplist = (ListView) win.findViewById(R.id.installapplist);
		//appline = (ImageView) win.findViewById(R.id.appline);
		//main_ll = (LinearLayout) findViewById(R.id.main_ll);
		setFont(text);

		WindowManager wm = mWM;
		
		wmParams.type = 2002;
		wmParams.flags = 40;

		wmParams.width = 615;

		wmParams.height = 96;

		wmParams.format = -3; 
		wmParams.x =  DensityUtil.dip2px(c, 0);
		wmParams.y =  DensityUtil.dip2px(c, -350);
		if (strText != null){
			setText(strText);
		}
		wm.addView(win, wmParams);
		mHandler.sendEmptyMessageDelayed(MSG_HIDE_WIN, 4000);
	}
	public void hidewin(){
		removeView();
	}
	
	private void setFont(TextView v){
		v.setTypeface(face);
		v.setTextSize(DensityUtil.px2sp(c, 30));
		v.getPaint().setFakeBoldText(true);
	}
}
