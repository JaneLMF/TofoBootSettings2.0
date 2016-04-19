package com.txbox.settings.dialog;

import com.txbox.txsdk.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;


public class EnterPassDialog {

	private Context mContext;
	private Dialog d;
	public EnterPassDialog(Context mContext){
		this.mContext = mContext;
		d = new Dialog(mContext, R.style.userDialog);
	}
	
public Dialog  showDialog(){
		
//		Window dialogWindow = d.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.CENTER);
//        d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//       
        
		d.setContentView(R.layout.enter_wifi_password);
		d.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
		
		d.show();
		return d;
	}
}
