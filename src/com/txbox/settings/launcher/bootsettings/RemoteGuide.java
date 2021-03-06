package com.txbox.settings.launcher.bootsettings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.txbox.txsdk.R;
import com.txbox.settings.common.LocalInfor;
import com.txbox.settings.common.NetworkManager;
import com.txbox.settings.common.TXbootApp;
import com.txbox.settings.impl.SyscfgImpl;
import com.txbox.settings.launcher.bootsettings.RemoteGuide2.BluetoothReceiver;
import com.txbox.settings.mbx.api.NetManager;
import com.txbox.settings.mbx.api.NetManager.NetworkDetailInfo;
import com.txbox.settings.utils.ConfigManager;
import com.txbox.settings.utils.DecodeImgUtil;
import com.txbox.settings.utils.WebServer;

import android.app.Activity;
import android.app.ActivityGroup;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class RemoteGuide extends ActivityGroup{
	private LinearLayout main_ll;
	private Bitmap b;
	private  BluetoothReceiver myReceiver;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.welcome_activity);
		initView();
		registerbt(this);
	}
	
	private void initView() {
		main_ll = (LinearLayout) findViewById(R.id.main_ll);
		b = new DecodeImgUtil(this, R.raw.userguide3).getBitmap();
		BitmapDrawable bd= new BitmapDrawable(b);
		main_ll.setBackground(bd);
	}
	
	private void startPairingView() {
		Intent it = new Intent();
		it.setClass(RemoteGuide.this, RemoteAutoPair.class);
		startActivity(it);
		RemoteGuide.this.finish();
	}
	
	private void startRemotePairFinish(){
		Intent i = new Intent();
		i.setClass(this, RemoteAutoPairFinished.class);
		startActivity(i);
		overridePendingTransition(R.anim.zoout, R.anim.zoin);
		RemoteGuide.this.finish();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(myReceiver!=null)
			unregisterReceiver(myReceiver);
		if(b!=null) b.recycle();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch (keyCode) {
		case KeyEvent.KEYCODE_ESCAPE:
		case KeyEvent.KEYCODE_BACK:			
				 return true;
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
			startPairingView();
			return true;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void registerbt(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND); 
        
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);   // 蓝牙设备配对状态发生改变，详细可查看官方文档

        
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);   // 蓝牙开始扫描
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);  // 蓝牙扫描结束
        
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);       // 蓝牙状态改变，即开启或者关闭，多种状态
        
        filter.addAction( BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);  // BT 链接状态改变
 
        myReceiver = new BluetoothReceiver();
        context.registerReceiver(myReceiver, filter);
    }
	
	class BluetoothReceiver extends BroadcastReceiver {
 		String TAG = "BluetoothReceiver";
 		@Override
 		public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();  
            System.out.println("++++++++ BT action = " +action);  
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {  
            	 
            } else if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
        	   
            }else if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)){
  		   			
            }else if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
        	   

            } else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){ 
            	BluetoothDevice	device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE); 
       		  	switch (device.getBondState()) { 
       		  		case BluetoothDevice.BOND_BONDING: 
       		  			startPairingView();
       		  			Log.d("BlueToothTestActivity", "正在配对......"); 
       		  			break; 
       		  		case BluetoothDevice.BOND_BONDED: 
       		  			Log.d("BlueToothTestActivity", "完成配对"); 
       		  			break; 
       		  		 case BluetoothDevice.BOND_NONE: 
           		   	    break;
       		  		 default: 
       		  				break; 
       		  } 
            }else if(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
        	   //Auto-generated method stub  
        	   int blueconState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, 0);  
        	   BluetoothDevice	device =  intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        	   switch (blueconState) {  
        	   		case BluetoothAdapter.STATE_CONNECTED:  
        	   			Log.i(TAG, "STATE_CONNECTED");  
        	   			startRemotePairFinish();
        		        break;  
        		    case BluetoothAdapter.STATE_CONNECTING:  
        		    	Log.i(TAG, "STATE_CONNECTING");            
        		        break;  
        		    case BluetoothAdapter.STATE_DISCONNECTED:  
        		    	Log.i(TAG, "STATE_DISCONNECTED");          
        		        break;  
        		    case BluetoothAdapter.STATE_DISCONNECTING:  
        		    	Log.i(TAG, "STATE_DISCONNECTING");  
        		    	break;  
        		    default:  
        		    	break;  
        		} 	
             }    
 		}
 	}
}
