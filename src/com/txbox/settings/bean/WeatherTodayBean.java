package com.txbox.settings.bean;
/**
 * 
 * @类描述：获取当天天气接口返回值实体类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.bean
 * @类名称：WeatherTodayBean	
 * @创建人：Administrator
 * @创建时间：2014-10-11下午3:24:58	
 * @修改人：Administrator
 * @修改时间：2014-10-11下午3:24:58	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class WeatherTodayBean {
	ResultBean result;
	WeatherItemBean data;
	public ResultBean getResult() {
		return result;
	}
	public void setResult(ResultBean result) {
		this.result = result;
	}
	public WeatherItemBean getData() {
		return data;
	}
	public void setData(WeatherItemBean data) {
		this.data = data;
	}
}
