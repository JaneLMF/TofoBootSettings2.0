package com.txbox.settings.common;

import java.util.Calendar;

import android.content.Context;

/**
 * 
 * @��������Ҫ�����ɵ����࣬���ص���Ϣ
 * @��Ŀ��ƣ�TXBootSettings
 * @���� com.example.txbootsettings.common
 * @����ƣ�LocalInfor	
 * @�����ˣ�huang
 * @����ʱ�䣺Aug 18, 20142:27:24 PM	
 * @�޸��ˣ�huang
 * @�޸�ʱ�䣺Aug 18, 20142:27:24 PM	
 * @�޸ı�ע��
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright go3c
 * @mail 774674395@qq.com
 */
public class LocalInfor {

	private Context mContext;
	public LocalInfor(Context mContext){
		this.mContext = mContext;
	}
	
	public String getLocalTime(){
		Calendar calendar = Calendar.getInstance(); 
		int minute = calendar.get(Calendar.MINUTE);
		String MINU = minute>=0&&minute<=9?"0"+minute:minute+"";
		return calendar.get(Calendar.HOUR_OF_DAY)+" : "+ MINU;
	}
	
}
