package com.txbox.settings.report;

import java.io.UnsupportedEncodingException;
import java.lang.ref.FinalizerReference;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.tencent.dmproxy.logic.SdkConfig;
import com.txbox.settings.bean.GuidInfoBean;
import com.txbox.settings.impl.AuthImpl;
import com.txbox.settings.utils.ConfigManager;
import com.txbox.settings.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemProperties;

public class GlobalInfo {
    private static String   mAppVer               = "";
    private static int      mVerCode              = 0;
    private static long     mAppInstallTime;
    private static int      mSdkVersion;
    private static int      mPlatform             = 42;
    private static String   mGUID;
    private static String   mQQ;
    private static String   mOpenid;
    private static String   mOpenid_Type;
    private static String   mLicence_Id;
    private static String   mMACAdress;
    private static String   mIPinfo;
    private static String   mOSVersion;
    private static String   mDeviceID;
    private static String   mUserAgent;
    private static String   mPackage;
    private static String   mResource;
    private static String   mQua;
    private static String   mMD5;   //目前用版本
    private static String   mBizType = null;//播放渠道号

    public static final int CHANNEL_ID_DEFAULT    = 10009;

    public static void setAppVersion(String ver) {
        mAppVer = ver;
    }

    public static String getAppVersion() {
        return mAppVer;
    }
    
    public static void setVersionCode(int code) {
        mVerCode = code;
    }
    
    public static int getVersionCode() {
        return mVerCode;
    }
    
    public static String getSysVesion() {
        return mOSVersion;
    }
    
    public static void setSysVesion(String version) {
        mOSVersion = version;
    }
    
    public static String getQQ() {
        return mQQ;
    }
    
    public static void setQQ(String qq) {
        mQQ = qq;
    }
    
    public static String getOpenID() {
        return mOpenid;
    }
    
    public static void setOpenID(String openid) {
        mOpenid = openid;
    }
    
    public static String getOpenIDType() {
        return mOpenid_Type;
    }
    
    public static void setOpenIDType(String openidType) {
        mOpenid_Type = openidType;
    }
    
    public static String getLicenseID() {
        return mLicence_Id;
    }
    
    public static void setLicenseID(String licenseID) {
        mLicence_Id = licenseID;
    }
    
    public static String getMD5() {
        return mMD5;
    }

    public static void setMD5(String md5) {
        mMD5 = md5;
    }
    
    public static long getAppInstallTime() {
        return mAppInstallTime;
    }

    public static void setAppInstallTime(long time) {
        mAppInstallTime = time;
    }

    public static int getSdkNum() {
        return mSdkVersion;
    }

    public static void setSdkNum(int sdkNum) {
        mSdkVersion = sdkNum;
    }

    public static int getPlatform() {
        return mPlatform;
    }

    public static String getGUID() {
    	return mGUID;
    }

    public static void setGUID(String GUID) {
        mGUID = GUID;
    }

    public static String getMACAdress() {
        return mMACAdress;
    }

    public static void setMACAdress(String mac) {
        mMACAdress = mac;
    }
    
    public static String getIPInfo() {
        return mIPinfo;
    }

    public static void setIPInfo(String ip) {
        mIPinfo = ip;
    }
    
    public static String getDeviceID() {
        return mDeviceID;
    }

    public static void setDeviceID(String deviceID) {
        mDeviceID = deviceID;
    }

    public static String getUserAgent() {
        return mUserAgent;
    }

    public static void setUserAgent(String userAgent) {
        mUserAgent = userAgent;
    }

    public static int getFLowForMTA() {
        return (int) (System.currentTimeMillis() / 1000 + (int) (Math.random() * 10));
    }

    public static String getDeviceName() {
      return urlToFileName(android.os.Build.MODEL);
    }
    public static String getResource() {
        return mResource;
    }
    
    public static void setResource(final Context ctx) {
        int iWidth = ctx.getResources().getDisplayMetrics().widthPixels;
        int iHeight = ctx.getResources().getDisplayMetrics().heightPixels;
        mResource = iWidth+"x"+iHeight;
    }
    public static String getPackageName() {
        return mPackage;
    }
    
    public static void setPackageName(String name) {
        mPackage = name;
    }
    public static String getQua(String pr) {
    	String qua = mQua+"&PR="+pr;
    	try {
            qua = URLEncoder.encode(qua, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            qua = qua.toString();
        }
        return qua;
    }
    public static String getBizType () {
    	if(mBizType!=null && !mBizType.isEmpty()) return mBizType;
    	mBizType = SdkConfig.getPlayerChannelId();
    	return mBizType ;
    }
    public static void setBizType (String biztype) {
    	mBizType = biztype;
    }
    public static String getExtraInfo() {
    	
    	
    	JSONObject data = new JSONObject();
		try{
			data.put("guid", getGUID());
			data.put("appid", ConfigManager.APPID);
			data.put("qua", getQua("video"));
			data.put("person_status", 0);
			data.put("openid", getOpenID());
			data.put("openid_type", "qq");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String extraInfo = data.toString();
    	return extraInfo;
    }
    public static String getHardInfo() {
        String hard_info = "{\"hard_platform\":\"default\",\"hard_configure\":\"default\"}";
        try {
    		hard_info = URLEncoder.encode(hard_info, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            hard_info = hard_info.toString();
        }
        return hard_info;
    }
    
    public static void setQua() {
        StringBuilder sb = new StringBuilder();
        sb.append("QV=1&");
        //sb.append("PR=LAUNCHER&");
        sb.append("VN="+mAppVer+"&");
        sb.append("PT="+SystemProperties.get("ro.pivos.pt","")+"&");
        sb.append("RL=" + mResource + "&");
        sb.append("IT=" + mAppInstallTime + "&");
        sb.append("OS=" + mOSVersion + "&");
        sb.append("SV=" + mAppVer+ "&");
        sb.append("CHID="+SystemProperties.get("ro.pivos.chid","")+"&");
        sb.append("DV=" + mDeviceID);
        mQua = sb.toString();
        
		JSONObject object = new JSONObject();// 一个user对象，使用一个JSONObject对象来装
		try {
			object.put("qua", mQua);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String jsonString = null;
		jsonString = object.toString(); // 把JSONObject转换成json格式的字符串
		Utils.saveStringToFile(ConfigManager.QUA_PATH, jsonString);
		String cmd = "chmod 666 " + ConfigManager.QUA_PATH;
		Utils.runCommand(cmd);
    }
    

    /**
     * get IPV4 address
     */
    public static String getLocalIPAdresss() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return "";
    }

    /**
     * 去掉文件名非法字�?
     * 
     * @param fileName
     * @return
     */
    public static String urlToFileName(String fileName) {
        String str = fileName;
        str = str.replace("\\", "");
        str = str.replace("/", "");
        str = str.replace(":", "");
        str = str.replace("*", "");
        str = str.replace("?", "");
        str = str.replace("\"", "");
        str = str.replace("<", "");
        str = str.replace(">", "");
        str = str.replace("|", "");
        str = str.replace("&", "_");
        str = str.replace(" ", "_"); // 前面的替换会产生空格,�?后将其一并替换掉
        return str;
    }

    /**
     * 获取网络类型
     * 
     * @param context
     * @return
     */
    public static String getNetworkTypeName() {
        return "";
    }
    
    public static void reportMta(Context context,String params) {
    	Intent intent = new Intent();  
		intent.setAction("com.tencent.mtareport");
		intent.putExtra("data",params);
		context.sendBroadcast(intent);
    }

}
