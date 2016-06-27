package com.tvxmpp.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class DialogShowView extends Activity {

	String showMessage = "";
	String showTitle = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		//if(intent != null){
		Intent intent = this.getIntent();
		showMessage = intent.getStringExtra("showstr");
		showTitle = intent.getStringExtra("titlestr");
		
		showConfirmdialog(showTitle, showMessage);
		//}
	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//showConfirmdialog("Ÿ¯žæ", "È·ÈÏÍË³öÂð?");
		

	}

	private void finishActivity(){
		this.finish();
	}
	
	/*private void showConfirmdialog(String strTitle, String strMessage) {

		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage(strMessage);
		builder.setTitle(strTitle);

		builder.setPositiveButton("确认", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				finishActivity();
			}

		});

		builder.create().show();
	}*/
	
	public void showConfirmdialog(String strTitle, String strMessage) {

		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setMessage(strMessage);
		builder.setTitle(strTitle);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finishActivity();
			}
		});

		builder.create().show();

	}
}
