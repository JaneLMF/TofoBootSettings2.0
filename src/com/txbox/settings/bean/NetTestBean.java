package com.txbox.settings.bean;

/**
 * 
 * @类描述：网络联通测试接口返回值实体类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.bean
 * @类名称：NetTestBean	
 * @创建人：Administrator
 * @创建时间：2014-9-17下午3:20:52	
 * @修改人：Administrator
 * @修改时间：2014-9-17下午3:20:52	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class NetTestBean {
	ResultBean result;
	NetTestDataBean data;
	public ResultBean getResult() {
		return result;
	}
	public void setResult(ResultBean result) {
		this.result = result;
	}
	public NetTestDataBean getData() {
		return data;
	}
	public void setData(NetTestDataBean data) {
		this.data = data;
	}
}
