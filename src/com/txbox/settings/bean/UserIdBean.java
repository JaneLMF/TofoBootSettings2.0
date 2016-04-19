package com.txbox.settings.bean;
/**
 * 
 * @类描述：天威登录接口Data部分userid 实体类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.bean
 * @类名称：UserIdBean	
 * @创建人：ylt
 * @创建时间：2015-8-31下午11:00:27	
 * @修改人：ylt
 * @修改时间：2015-8-31下午11:00:27	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170*@qq.com
 */
public class UserIdBean {
	private String userid;
	private String installaddress;
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getInstalladdress() {
		return installaddress;
	}
	public void setInstalladdress(String installaddress) {
		this.installaddress = installaddress;
	}
}
