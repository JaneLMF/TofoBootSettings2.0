package com.txbox.settings.bean;
/**
 * 
 * @类描述：当天天气接口返回值实体类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.bean
 * @类名称：WeatherItemBean	
 * @创建人：Administrator
 * @创建时间：2014-9-28下午3:15:17	
 * @修改人：Administrator
 * @修改时间：2014-9-28下午3:15:17	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class WeatherItemBean {
	String bi_name;//城市名称
	String tmax;//最近一次预报的最高气温
	String wt;//天气现象编码（编码值见附表一）
	String tmin;//最近一次预报的最低气温
	String tp;//即时气温
	public String getBi_name() {
		return bi_name;
	}
	public void setBi_name(String bi_name) {
		this.bi_name = bi_name;
	}
	public String getTmax() {
		return tmax;
	}
	public void setTmax(String tmax) {
		this.tmax = tmax;
	}
	public String getWt() {
		return wt;
	}
	public void setWt(String wt) {
		this.wt = wt;
	}
	public String getTmin() {
		return tmin;
	}
	public void setTmin(String tmin) {
		this.tmin = tmin;
	}
	public String getTp() {
		return tp;
	}
	public void setTp(String tp) {
		this.tp = tp;
	}
}
