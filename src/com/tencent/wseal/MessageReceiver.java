package com.tencent.wseal;

import com.tencent.oma.push.PushConstants;
import com.txbox.settings.utils.MessageUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MessageReceiver extends BroadcastReceiver {
	 @Override
	 public void onReceive(Context ctx, Intent intent) {
	  if (intent != null) {
		String action = intent.getAction();
		System.out.println("message on receive action = " + action);
		if (action.equalsIgnoreCase
			(PushConstants.ACTION_PUSH_MSG_RECEIVE))
			{
				String msg = intent.getStringExtra(PushConstants.ACTION_PUSH_MSG_DATA);
				System.out.println("message on receive msg:" + msg);
				if(msg!=null && msg.length()>0){
					MessageUtils.PushMessage(ctx,msg);
				}
			}
		}
	}
}