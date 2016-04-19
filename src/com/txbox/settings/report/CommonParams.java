package com.txbox.settings.report;

import java.util.List;
import java.util.Properties;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

/**
 * 设备参数 用于数据上报的�?�用参数
 * @author willyli
 * @author hardyxin
 */
public class CommonParams {
	
	
	/**
	 * 自定义参数，带commonprops
	 * @param propKVs
	 * @return
	 */
	public static Properties getCustomProps(Context context,String pr,List<KV> propKVs){
		Properties customPros = new Properties();
		customPros.put(ExParamKeys.common.COMMON_GUID, getNotNullString(GlobalInfo.getGUID()));
		customPros.put(ExParamKeys.common.COMMON_OPENID_TYPE, getNotNullString(GlobalInfo.getOpenIDType()));
		customPros.put(ExParamKeys.common.COMMON_OPENID, getNotNullString(GlobalInfo.getOpenID()));
		customPros.put(ExParamKeys.common.COMMON_PACKAGE, getNotNullString(GlobalInfo.getPackageName()));
		customPros.put(ExParamKeys.common.COMMON_QUA, getQUA(GlobalInfo.getQua(pr)));
		if(propKVs!=null && propKVs.size() > 0) {
			for(KV kv : propKVs){
				customPros.put(kv.getKey(), kv.getValue() != null ? kv.getValue() : "");
			}
		}
		
		return customPros;
	}
	
	public static Properties getPlayerCustomProps(Context context,String pr,List<KV> propKVs){
		Properties customPros = new Properties();
		customPros.put(ExParamKeys.common.COMMON_BIZTYPE, getNotNullString(GlobalInfo.getBizType()));
		customPros.put(ExParamKeys.common.COMMON_EXTRAINFO, GlobalInfo.getExtraInfo());
		customPros.put(ExParamKeys.common.COMMON_DEVMODEL, getNotNullString(GlobalInfo.getDeviceName()));
		customPros.put(ExParamKeys.common.COMMON_STAGUID, getNotNullString(GlobalInfo.getGUID()));
		customPros.put(ExParamKeys.common.COMMON_APPVERSION, getNotNullString(GlobalInfo.getAppVersion()));
		customPros.put(ExParamKeys.common.COMMON_GUID, getNotNullString(GlobalInfo.getGUID()));
		customPros.put(ExParamKeys.common.COMMON_OSVERSION, getNotNullString(GlobalInfo.getSysVesion()));
		customPros.put(ExParamKeys.common.COMMON_CURRENT_TIME, GlobalInfo.getFLowForMTA());
		customPros.put(ExParamKeys.common.COMMON_MAC, GlobalInfo.getMACAdress());
		if(propKVs!=null && propKVs.size() > 0) {	
			for(KV kv : propKVs){
				customPros.put(kv.getKey(), kv.getValue() != null ? kv.getValue() : "");
			}
		}
		return customPros;
	} 
	
	public static Properties getCGIConnectProps(){
		Properties cgiPros = new Properties();
		return cgiPros;
	}
	
	private static String getNotNullString(String originalString){
		if(TextUtils.isEmpty(originalString)){
			return "";
		}
		return originalString;
	}
	private static String getQUA(String originalString){
        if(TextUtils.isEmpty(originalString)){
            return "";
        }
        return originalString;
    }
}
