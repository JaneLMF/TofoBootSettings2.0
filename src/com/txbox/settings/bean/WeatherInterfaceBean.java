package com.txbox.settings.bean;
/**
 * 
 * @类描述：获取天气接口地址返回值实体类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.bean
 * @类名称：WeaterInterfaceBean	
 * @创建人：Administrator
 * @创建时间：2014-9-28上午11:00:36	
 * @修改人：Administrator
 * @修改时间：2014-9-28上午11:00:36	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class WeatherInterfaceBean {
	ResultBean result;
	WeatherInterfaceDataBean data;
	public ResultBean getResult() {
		return result;
	}
	public void setResult(ResultBean result) {
		this.result = result;
	}
	public WeatherInterfaceDataBean getData() {
		return data;
	}
	public void setData(WeatherInterfaceDataBean data) {
		this.data = data;
	}
}
