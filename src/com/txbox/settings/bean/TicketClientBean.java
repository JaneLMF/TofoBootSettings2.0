package com.txbox.settings.bean;
/**
 * 
 * @类描述：获取ticket_client 接口返回实体类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.bean
 * @类名称：TicketClientBean	
 * @创建人：Administrator
 * @创建时间：2014-9-17下午10:50:27	
 * @修改人：Administrator
 * @修改时间：2014-9-17下午10:50:27	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class TicketClientBean {
	String code;
	String description;
	String ticket_client;
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
	public String getTicket_client() {
		return ticket_client;
	}
	public void setTicket_client(String ticket_client) {
		this.ticket_client = ticket_client;
	}
}
