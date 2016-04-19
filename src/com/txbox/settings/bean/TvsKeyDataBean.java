package com.txbox.settings.bean;
/**
 * 
 * @类描述：获取临时登录态 返回结果data部分实体类
 * @项目名称：TXBootSettings
 * @包名： com.example.txbootsettings.bean
 * @类名称：TvsKeyDataBean	
 * @创建人：Administrator
 * @创建时间：2014-9-4下午8:16:16	
 * @修改人：Administrator
 * @修改时间：2014-9-4下午8:16:16	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class TvsKeyDataBean {
	public int getSys_time() {
		return sys_time;
	}
	public void setSys_time(int sys_time) {
		this.sys_time = sys_time;
	}
	public String getTicket_server() {
		return ticket_server;
	}
	public void setTicket_server(String ticket_server) {
		this.ticket_server = ticket_server;
	}
	public String getExpire() {
		return expires;
	}
	public void setExpire(String expire) {
		this.expires = expire;
	}
	String ticket_server;//含临时登录态的加密串
	String expires;//临时登录态失效时间（目前为7200秒）
	int sys_time;//失败重试时，请用这个时间重新计算ticket_client
}
