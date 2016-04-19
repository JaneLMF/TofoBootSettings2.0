package com.txbox.settings.bean;
/**
 * 
 * @类描述：获取guid返回结果 data部分实体类
 * @项目名称：TXBootSettings
 * @包名： com.example.txbootsettings.bean
 * @类名称：GuidDataBean	
 * @创建人：Administrator
 * @创建时间：2014-9-4下午8:14:32	
 * @修改人：Administrator
 * @修改时间：2014-9-4下午8:14:32	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright go3c
 * @mail 939757170@qq.com
 */
public class GuidDataBean {
	String guid;//设备唯一ID, 目前为32位字符串
	String guid_secret;//不定长字符串，用于获取登录态接口
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getGuid_secret() {
		return guid_secret;
	}
	public void setGuid_secret(String guid_secret) {
		this.guid_secret = guid_secret;
	}
}
