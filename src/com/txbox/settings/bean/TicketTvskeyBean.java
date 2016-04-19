package com.txbox.settings.bean;
/**
 * 
 * @类描述：本地获取tvskey返回值实体类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.bean
 * @类名称：TicketTvskeyBean	
 * @创建人：Administrator
 * @创建时间：2014-9-17下午10:53:09	
 * @修改人：Administrator
 * @修改时间：2014-9-17下午10:53:09	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright go3c
 * @mail 939757170@qq.com
 */
public class TicketTvskeyBean {
	String code;
	String description;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTvskey() {
		return tvskey;
	}
	public void setTvskey(String tvskey) {
		this.tvskey = tvskey;
	}
	String tvskey;
}
