package com.txbox.settings.bean;
/**
 * 
 * @类描述：网络测速接口返回实体类
 * @项目名称：TXBootSettings
 * @包名： com.example.txbootsettings.bean
 * @类名称：SpeedTestBean	
 * @创建人：Administrator
 * @创建时间：2014-9-9下午5:00:51	
 * @修改人：Administrator
 * @修改时间：2014-9-9下午5:00:51	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class SpeedTestBean {
	ResultBean result;
	SpeedTestDataBean data;
	public ResultBean getResult() {
		return result;
	}
	public void setResult(ResultBean result) {
		this.result = result;
	}
	public SpeedTestDataBean getData() {
		return data;
	}
	public void setData(SpeedTestDataBean data) {
		this.data = data;
	}
}
