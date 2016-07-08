package com.txbox.settings.data;

import android.content.Context;

public class PivosSharedPreferencesFactory extends SharedPreferencesFactory {
	private final static String FILE_NAME = "pivos_setting_config";
	
	private String KEY_XMPP_NAME = "xmpp_name";
	private String KEY_MAC_ADDRESS = "mac_address";
	
	protected PivosSharedPreferencesFactory(Context curContext, String key) {
		super(curContext, key);
	}
	
	public PivosSharedPreferencesFactory(Context context){
		this(context, FILE_NAME);
	}
	
	public String getXmppName() {
		return getString(KEY_XMPP_NAME, null);
	}
	
	public void setXmppName(String xmppName){
		setValue(KEY_XMPP_NAME, xmppName);
	}

	public String getMacAddress(){
		return getString(KEY_MAC_ADDRESS, null);
	}
	
	public boolean setMacAddress(String macAddress){
		return setValue(KEY_MAC_ADDRESS, macAddress);
	}
}
