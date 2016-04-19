package com.txbox.settings.launcher.bootsettings;

import com.txbox.settings.utils.ClsUtils;
import com.txbox.settings.utils.DecodeImgUtil;
import com.txbox.txsdk.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class RemoteGuide2 extends Activity{
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
		main_ll = (LinearLayout) findViewById(R.id.main_ll);
		b = new DecodeImgUtil(this, R.raw.userguide3).getBitmap();
		BitmapDrawable bd= new BitmapDrawable(b);
		main_ll.setBackground(bd);
		registerbt(this);
	}
	
	private void startRemoteAutoPair(){
		Intent i = new Intent();
		i.setClass(this, RemoteAutoPair.class);
		startActivity(i);
		overridePendingTransition(R.anim.zoout, R.anim.zoin);
		RemoteGuide2.this.finish();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(myReceiver!=null)
			unregisterReceiver(myReceiver);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch (keyCode) {
		case KeyEvent.KEYCODE_ESCAPE:
		case KeyEvent.KEYCODE_BACK:			
				 return true;
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
			startRemoteAutoPair();
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
