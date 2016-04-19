package com.txbox.settings.bean;
/**
 * 
 * @类描述：获取未来3天天气接口返回实体类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.bean
 * @类名称：Weather3DaysBean	
 * @创建人：Administrator
 * @创建时间：2014-9-28下午3:25:08	
 * @修改人：Administrator
 * @修改时间：2014-9-28下午3:25:08	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class Weather3DaysBean {
	ResultBean result;
	Weather3DaysDataBean data;
	public ResultBean getResult() {
		return result;
	}
	public void setResult(ResultBean result) {
		this.result = result;
	}
	public Weather3DaysDataBean getData() {
		return data;
	}
	public void setData(Weather3DaysDataBean data) {
		this.data = data;
	}
	
}
