package com.txbox.settings.mbx.api;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import com.txbox.settings.utils.Utils;
import com.txbox.settings.utils.WifiUtils;

//import android.app.SystemWriteManager;
import android.content.Context;
import android.util.Log;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
//import android.net.ethernet.EthernetDevInfo;
//import android.net.ethernet.EthernetManager;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.SystemProperties;

public class NetManager {

	private static String TAG = "NetManager";

	public static int DEV_TYPE_ETH = 1;
	public static int DEV_TYPE_WIFI = 2;

	public static final int SECURITY_NONE = 0;
	public static final int SECURITY_WEP = 1;
	public static final int SECURITY_WPA_WPA2 = 2;
	public static final int SECURITY_WPA2 = 3;
	public static final int SECURITY_WPA = 4;
	public static final int SECURITY_EAP = 5;

	private static final String eth_device_sysfs = "/sys/class/ethernet/linkspeed";
	//public EthernetManager mEthernetManager;
	public WifiManager mWifiManager;
	Context mContext = null;
	//private static SystemWriteManager sw = null;

	public NetManager(Context context) {
		mContext = context;
		//sw = (SystemWriteManager) mContext.getSystemService("system_write");
		//mEthernetManager = (EthernetManager) mContext.getSystemService("ethernet");
		mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
	}

	public WifiManager getWifiMgr() {
		return mWifiManager;
	}

	public static int getSecurityType(ScanResult result) { // CISCO MODIFY, from
		// private to public
		//System.out.println("capabilities = " + result.capabilities);
		if (result.capabilities.contains("WEP")) {
			return SECURITY_WEP;
		} else if (result.capabilities.contains("WPA2-PSK") && result.capabilities.contains("WPA-PSK")) {
			return SECURITY_WPA_WPA2;
		} else if (result.capabilities.contains("WPA2-PSK")) {
			return SECURITY_WPA2;
		} else if (result.capabilities.contains("WPA-PSK")) {
			return SECURITY_WPA;
		} else if (result.capabilities.contains("EAP")) {
			return SECURITY_EAP;
		}
		return SECURITY_NONE;
	}


	
	public boolean isWifiSaved(ScanResult scanResult) {
		WifiConfiguration config = getSavedWifiConfig(scanResult);
	
		if (config == null) {
			  System.out.println("WifiConfiguration:    NO!!!!!!!!");
			  return false;
		}
		  System.out.println("WifiConfiguration:    "+ config.SSID + "  has config");
		//if (config.status == WifiConfiguration.Status.DISABLED )
		//	 return false;
		return true;
	}

	public boolean isWifiConfDisable(ScanResult scanResult) {
		WifiConfiguration config = getSavedWifiConfig(scanResult);
		WifiInfo info = mWifiManager.getConnectionInfo();
		
		SupplicantState state =  mWifiManager.getConnectionInfo().getSupplicantState();
		if (config == null)
			  return true;
		if (config.status == WifiConfiguration.Status.DISABLED )
			  return true;
		else if(state == SupplicantState.INACTIVE )
			return true;
			
		return false;
	}

  public ScanResult getScanResultBySSID(String ssid){
	   List<ScanResult> results = getWifiMgr().getScanResults();
		if (results != null) {
			for (ScanResult result : results) {
				System.out.println("ssid" + ssid + "  ssid2:" +result.SSID);
				if(ssid.equals(result.SSID)){
					return result;
				}
			}
		}
		return null;
  }
 	
	
	public void removWifiConf(ScanResult scanResult) {
		WifiConfiguration config = getSavedWifiConfig(scanResult);
		if (config != null) {
			mWifiManager.removeNetwork(config.networkId);
		}
	}

	public WifiConfiguration createWifiConfig(ScanResult scanResult, String Password, String username) {

		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + scanResult.SSID + "\"";

		if (isWifiSaved(scanResult)) {
			removWifiConf(scanResult);
		}
		int Type = getSecurityType(scanResult);

		if (Type == SECURITY_NONE) // WIFICIPHER_NOPASS
		{
			//config.wepKeys[0] = "";
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			//config.wepTxKeyIndex = 0;
		}
		if (Type == SECURITY_WEP) // WIFICIPHER_WEP
		{
			config.hiddenSSID = true;
			config.wepKeys[0] = "\"" + Password + "\"";
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == SECURITY_WPA_WPA2 || Type == SECURITY_WPA2 || Type == SECURITY_WPA) // WIFICIPHER_WPA
		{
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
		}
		if (Type == SECURITY_EAP) {
			config.allowedKeyManagement.set(KeyMgmt.WPA_EAP);
			config.allowedKeyManagement.set(KeyMgmt.IEEE8021X);
			/*
			 * config.eap.setValue((String) mEapMethod.getSelectedItem());
			 * 
			 * config.phase2.setValue((mPhase2.getSelectedItemPosition() == 0) ?
			 * "" : "auth=" + mPhase2.getSelectedItem());
			 * config.ca_cert.setValue((mEapCaCert.getSelectedItemPosition() ==
			 * 0) ? "" : KEYSTORE_SPACE + Credentials.CA_CERTIFICATE + (String)
			 * mEapCaCert.getSelectedItem());
			 * config.client_cert.setValue((mEapUserCert
			 * .getSelectedItemPosition() == 0) ? "" : KEYSTORE_SPACE +
			 * Credentials.USER_CERTIFICATE + (String)
			 * mEapUserCert.getSelectedItem());
			 * config.private_key.setValue((mEapUserCert
			 * .getSelectedItemPosition() == 0) ? "" : KEYSTORE_SPACE +
			 * Credentials.USER_PRIVATE_KEY + (String)
			 * mEapUserCert.getSelectedItem());
			 * config.identity.setValue((mEapIdentity.length() == 0) ? "" :
			 * mEapIdentity.getText().toString());
			 */
			/* 设置用户名，密码 */
			/*
			 * config.anonymous_identity.setValue(username); if
			 * (Password.length() != 0) { config.password.setValue(Password); }
			 */
		}

		return config;
	}

	public void connect2AccessPoint(ScanResult scanResult, String password, String usrname) {

		int securityType = getSecurityType(scanResult);

		WifiConfiguration config = getSavedWifiConfig(scanResult);

		mWifiManager.disconnect();
		if (config == null /*|| isWifiConfDisable(scanResult)*/) {
			Log.d(TAG, "===== It's a new AccessPoint!!! ");
			config = createWifiConfig(scanResult, password, usrname);
			// config.priority = 1;
			config.status = WifiConfiguration.Status.ENABLED;
			int netId = mWifiManager.addNetwork(config);
			mWifiManager.enableNetwork(netId, true);
			mWifiManager.saveConfiguration();
		} else {
			Log.d(TAG, "===== It's a saved AccessPoint!!! ");
			int netId  = config.networkId;
			if (password != null && password.length() > 0) {
				Log.d(TAG, "===== It's a saved AccessPoint, Need Update password ! ");
				mWifiManager.removeNetwork(config.networkId);
				config = createWifiConfig(scanResult, password, usrname);
				netId = mWifiManager.addNetwork(config);
			}else {
				Log.d(TAG, "===== It's a saved AccessPoint,  Enable the config ! ");
			}

			config.status = WifiConfiguration.Status.ENABLED;
			mWifiManager.enableNetwork(netId, true);
			mWifiManager.saveConfiguration();
			// mWifiManager.updateNetwork(config);
		}

	}
  
	public WifiConfiguration getSavedWifiConfig(ScanResult scanResult) {
		List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
		if(existingConfigs!=null){
			for (WifiConfiguration existingConfig : existingConfigs) {
	
				if (existingConfig != null &&  existingConfig.SSID != null && existingConfig.SSID.equals("\"" + scanResult.SSID + "\"")) {
					return existingConfig;
				}
			}
		}
		return null;
	}

	public boolean isEthDeviceAdded() {
		/*String str = Utils.readSysFile(sw, eth_device_sysfs);
		if (str == null)
			return false;
		 if(str.contains("unlink")){
			 return false;
		 }else {
			 return true;
		 }*/
		return false;
	}

	public void enableEthernet(boolean enable) {
		/*if(!WifiUtils.isEthConnected(mContext)){
			EthernetDevInfo info = new EthernetDevInfo();
			info.setIfName("eth0");
			info.setConnectMode(EthernetDevInfo.ETH_CONN_MODE_DHCP);
	        	info.setIpAddress(null);
	        	info.setRouteAddr(null);
	        	info.setDnsAddr(null);
	        	info.setNetMask(null);
			info.setProxy(null, 0, null);
	        	mEthernetManager.updateEthDevInfo(info);
			mEthernetManager.setEthEnabled(enable);
		}*/
	}

	public void enableWIFI(boolean enable) {
		mWifiManager.setWifiEnabled(enable);
	}

	public void startWifiScan() {
		mWifiManager.startScan();
	}

	public String int2ip(long ipInt) {
		StringBuilder sb = new StringBuilder();
		sb.append(ipInt & 0xFF).append(".");
		sb.append((ipInt >> 8) & 0xFF).append(".");
		sb.append((ipInt >> 16) & 0xFF).append(".");
		sb.append((ipInt >> 24) & 0xFF);
		return sb.toString();
	}

	public String getDeviceIpAddress(int type) {
		boolean isEthConnected = WifiUtils.isEthConnected(mContext);
		boolean isWifiConnected = WifiUtils.isWifiConnected(mContext);
		/*if (isWifiConnected && type == DEV_TYPE_WIFI) {
			WifiInfo mWifiinfo = mWifiManager.getConnectionInfo();
			String ipAddress = null;
			if (mWifiinfo != null) {
				ipAddress = int2ip(mWifiinfo.getIpAddress());
				Log.d(TAG, "==== wifi ipAddress : " + ipAddress);

			}
			return ipAddress;
		} else if (isEthConnected && type == DEV_TYPE_ETH) {
			DhcpInfo mDhcpInfo = mEthernetManager.getDhcpInfo();
			if (mDhcpInfo != null) {
				int ip = mDhcpInfo.ipAddress;
				String ipAddress = int2ip(ip);
				Log.d(TAG, "==== Wired ipAddress : " + ipAddress);
				return ipAddress;

			}
		}*/
		return null;
	}

	public String getDeviceIpAddress() {
		boolean isEthConnected = WifiUtils.isEthConnected(mContext);
		boolean isWifiConnected = WifiUtils.isWifiConnected(mContext);
		/*if (isWifiConnected ) {
			WifiInfo mWifiinfo = mWifiManager.getConnectionInfo();
			String ipAddress = null;
			if (mWifiinfo != null) {
				ipAddress = int2ip(mWifiinfo.getIpAddress());
				Log.d(TAG, "==== wifi ipAddress : " + ipAddress);
			}
			return ipAddress;
		} else if (isEthConnected ) {
			DhcpInfo mDhcpInfo = mEthernetManager.getDhcpInfo();
			if (mDhcpInfo != null) {
				int ip = mDhcpInfo.ipAddress;
				String ipAddress = int2ip(ip);
				Log.d(TAG, "==== Wired ipAddress : " + ipAddress);
				return ipAddress;

			}
		}*/
		return null;
	}
	
	
	public boolean isEthConnected() {
		return WifiUtils.isEthConnected(mContext) && this.isEthEnable();
	}

	public boolean isWifiConnected() {
		return WifiUtils.isWifiConnected(mContext) && this.isWIFIEnable();
	}
	
	public State getWifiConnectState() {
		return WifiUtils.getWifiConnectState(mContext);
	}

	public State getEtherConnected() {
		 return WifiUtils.getEthConnectState(mContext);
	}

	  
	public void disableNotActiveWifiConfig()
	{
		String activeSSID= null;
		if(isWifiConnected()){
			 WifiInfo info =mWifiManager.getConnectionInfo();
             activeSSID = info.getSSID();
		}
		List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs) {
	  		if( activeSSID != null && activeSSID.equals(existingConfig.SSID))
				 continue;
	  		
			getWifiMgr().disableNetwork(existingConfig.networkId);
			getWifiMgr().disconnect();

			
		}
		getWifiMgr().saveConfiguration();
	}
	
	
	
	public int getEthSpeed() {
		/*String str = Utils.readSysFile(sw, eth_device_sysfs);
		if (str == null)
			return 0;
		int start = str.indexOf("speed :");
		String value = null;
		if (str.length() > start + 7) {
			value = str.substring(start + 7).trim();
		}
		if (value != null)
			return Integer.parseInt(value);
		else*/
			return 0;
	}

	public boolean isEthEnable() {
		/*int state = mEthernetManager.getEthState();
		Log.d(TAG, "===== getEthCheckBoxState() , state : " + state);
		if (state == EthernetManager.ETH_STATE_ENABLED) {
			return true;
		} else {
			return false;
		}*/
		return false;
	}

	public boolean isWIFIEnable() {
		int state = mWifiManager.getWifiState();
		if (state == WifiManager.WIFI_STATE_ENABLED) {
			return true;
		} else {
			return false;
		}
	}

	public  NetworkInfo getActiveNetwork() {
		final ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info  = connMgr.getActiveNetworkInfo();
		return info;

	}
	
	protected InetAddress getLocalInetAddress() {  
	    InetAddress ip = null;  
	    try {  
		        Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();  
		        while (en_netInterface.hasMoreElements()) {  
		            NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();  
		            Enumeration<InetAddress> en_ip = ni.getInetAddresses();  
		            while (en_ip.hasMoreElements()) {  
		                ip = en_ip.nextElement();  
		                if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)  
		                    break;  
		                else  
	                    ip = null;  
		            }  
		  
		            if (ip != null) {  
		                break;  
		            }  
		        }  
		    } catch (SocketException e) {  
		        // TODO Auto-generated catch block  
		        e.printStackTrace();  
		    }  
		    return ip;  
	}  
	  

	
	public String getLocalEthMacAddress() {
		String mac_s = "";
		String ipAddress = null;
		/*DhcpInfo mDhcpInfo = mEthernetManager.getDhcpInfo();
		if (mDhcpInfo != null) {
			int ip = mDhcpInfo.ipAddress;
			ipAddress = int2ip(ip);
			Log.d(TAG, "==== Wired ipAddress : " + ipAddress);
		} else {
			return null;
		}
		try {
			byte[] mac;
			InetAddress inet =getLocalInetAddress();	//InetAddress.getByName(ipAddress);
			NetworkInterface ne = NetworkInterface.getByInetAddress(inet);
			mac = ne.getHardwareAddress();
			mac_s = byte2hex(mac);
		} catch (Exception e) {
			e.printStackTrace();
		}*/

		return mac_s;
	}

	public String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer(b.length);
		String stmp = "";
		int len = b.length;
		for (int n = 0; n < len; n++) {
			
			stmp = Integer.toHexString(b[n] & 0xFF);
			if (n == 0) {
				if (stmp.length() == 1)
					hs = hs.append("0").append(stmp);
				else {
					hs = hs.append(stmp);
				}
			} else {
				if (stmp.length() == 1)
					hs = hs.append(":").append("0" + stmp);
				else {
					hs = hs.append(":" + stmp);
				}
			}
		}
		return String.valueOf(hs);
	}
	
	public class NetworkDetailInfo
	{
		 public  String ip = null;
		 public String mac = null;
		 public String gateway = null;
		 public String netmask = null;
		 public  String dns1 = null;
		 public  String dns2 = null;
		 public  int speed = 0;
		 public String ssid =null;
	}
	
	public NetworkDetailInfo getNetworkDetailInfo(){
		NetworkDetailInfo info = new  NetworkDetailInfo();
		 DhcpInfo mDhcpInfo = null;
        int speed = 0;
        String mac = null;
		if(isWifiConnected()){
			mDhcpInfo = mWifiManager.getDhcpInfo();
			speed =mWifiManager.getConnectionInfo().getLinkSpeed();
			mac = mWifiManager.getConnectionInfo().getMacAddress();
			if(mDhcpInfo != null){
				 info.ip = int2ip(mDhcpInfo.ipAddress);
				 info.gateway = int2ip(mDhcpInfo.gateway);
				 info.netmask = int2ip( mDhcpInfo.netmask);
				 info.dns1 = int2ip(mDhcpInfo.dns1);
				 info.dns2 = int2ip(mDhcpInfo.dns2);
				 info.mac = mac;
				 info.ssid = mWifiManager.getConnectionInfo().getSSID();
			}
		}else if(isEthConnected()){
			info.ip = SystemProperties.get("dhcp.eth0.ipaddress","0.0.0.0");
			info.gateway = SystemProperties.get("dhcp.eth0.gateway","0.0.0.0");
			info.netmask = SystemProperties.get("dhcp.eth0.mask","0.0.0.0");
			info.dns1 = SystemProperties.get("dhcp.eth0.dns1","0.0.0.0");
			info.dns2 = SystemProperties.get("dhcp.eth0.dns2","0.0.0.0");
			info.mac = getLocalEthMacAddress();
		} 
		return info;
	}

}
