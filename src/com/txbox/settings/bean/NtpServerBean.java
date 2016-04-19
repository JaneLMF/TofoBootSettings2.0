package com.txbox.settings.bean;
/**
 * 
 * @类描述：获取NTP服务器接口返回值实体类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.bean
 * @类名称：NtpServerBean	
 * @创建人：Administrator
 * @创建时间：2014-9-28上午11:07:45	
 * @修改人：Administrator
 * @修改时间：2014-9-28上午11:07:45	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class NtpServerBean {
	ResultBean result;
	NtpServerDataBean data;
	public ResultBean getResult() {
		return result;
	}
	public void setResult(ResultBean result) {
		this.result = result;
	}
	public NtpServerDataBean getData() {
		return data;
	}
	public void setData(NtpServerDataBean data) {
		this.data = data;
	}
}
