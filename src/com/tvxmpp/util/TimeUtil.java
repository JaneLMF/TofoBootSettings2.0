package com.tvxmpp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

/**
 * 鏃堕棿宸ュ叿绫�
 * 
 * @author way
 * 
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {

	public static String getTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
		return format.format(new Date(time));
	}

	public static String getHourAndMin(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(new Date(time));
	}

	public static String getChatTime(long timesamp) {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = new Date(timesamp);
		int temp = Integer.parseInt(sdf.format(today))
				- Integer.parseInt(sdf.format(otherDay));

		switch (temp) {
		case 0:
			result = "浠婂ぉ " + getHourAndMin(timesamp);
			break;
		case 1:
			result = "鏄ㄥぉ " + getHourAndMin(timesamp);
			break;
		case 2:
			result = "鍓嶅ぉ " + getHourAndMin(timesamp);
			break;

		default:
			// result = temp + "澶╁墠 ";
			result = getTime(timesamp);
			break;
		}

		return result;
	}
}
