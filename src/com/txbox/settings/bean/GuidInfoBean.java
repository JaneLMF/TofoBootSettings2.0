package com.txbox.settings.bean;

/**
 * 
 * @类描述：用于保存guid信息实体类
 * @项目名称：TXBootSettings
 * @包名： com.example.txbootsettings.bean
 * @类名称：GuidInfoBean	
 * @创建人：Administrator
 * @创建时间：2014-9-12下午12:14:55	
 * @修改人：Administrator
 * @修改时间：2014-9-12下午12:14:55	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright go3c
 * @mail 939757170@qq.com
 */
public class GuidInfoBean {
	String guid;//设备唯一ID, 目前为32位字符串
	String guid_secret;//不定长字符串，用于获取登录态接口
	String tvskey;
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
	public String getTvskey() {
		return tvskey;
	}
	public void setTvskey(String tvskey) {
		this.tvskey = tvskey;
	}
}
