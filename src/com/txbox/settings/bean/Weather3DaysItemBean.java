package com.txbox.settings.bean;
/**
 * 
 * @类描述：未来3天每天天气状况实体类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.bean
 * @类名称：Weather3DaysItemBean	
 * @创建人：Administrator
 * @创建时间：2014-10-11下午3:31:36	
 * @修改人：Administrator
 * @修改时间：2014-10-11下午3:31:36	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class Weather3DaysItemBean {
	String date;//日期
	String tmax;//最高气温
	String tmin;//最低气温
	String wt;//天气现象
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTmax() {
		return tmax;
	}
	public void setTmax(String tmax) {
		this.tmax = tmax;
	}
	public String getTmin() {
		return tmin;
	}
	public void setTmin(String tmin) {
		this.tmin = tmin;
	}
	public String getWt() {
		return wt;
	}
	public void setWt(String wt) {
		this.wt = wt;
	}
}
