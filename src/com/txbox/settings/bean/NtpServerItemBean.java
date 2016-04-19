package com.txbox.settings.bean;

import java.util.List;
/**
 * 
 * @类描述：获取NTP服务器返回值中服务器列表实体类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.bean
 * @类名称：NtpServerItemBean	
 * @创建人：Administrator
 * @创建时间：2014-9-28上午11:06:16	
 * @修改人：Administrator
 * @修改时间：2014-9-28上午11:06:16	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class NtpServerItemBean {
	List<String> time_server;//NTP 服务器列表

	public List<String> getTime_server() {
		return time_server;
	}

	public void setTime_server(List<String> time_server) {
		this.time_server = time_server;
	}
}
