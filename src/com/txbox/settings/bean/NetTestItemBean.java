package com.txbox.settings.bean;
/**
 * 
 * @类描述：网络联通接口返回test_net 实体类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.bean
 * @类名称：NetTestItemBean	
 * @创建人：Administrator
 * @创建时间：2014-9-17下午3:25:21	
 * @修改人：Administrator
 * @修改时间：2014-9-17下午3:25:21	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class NetTestItemBean {
	String dns;
	public String getDns() {
		return dns;
	}
	public void setDns(String dns) {
		this.dns = dns;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	String ip;
}
