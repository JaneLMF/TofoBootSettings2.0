package com.txbox.settings.bean;
/**
 * 
 * @类描述：获取天气接口返回值接口地址实体类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.bean
 * @类名称：WeatherInterfaceItemBean	
 * @创建人：Administrator
 * @创建时间：2014-9-28上午10:58:38	
 * @修改人：Administrator
 * @修改时间：2014-9-28上午10:58:38	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class WeatherInterfaceItemBean {
	String weather_today;//获取当天天气接口地址
	String weather_3days;//获取未来3天天气接口地址
	String citylist_xml;//地区,ID 对应xml文件
	public String getWeather_today() {
		return weather_today;
	}
	public void setWeather_today(String weather_today) {
		this.weather_today = weather_today;
	}
	public String getWeather_3days() {
		return weather_3days;
	}
	public void setWeather_3days(String weather_3days) {
		this.weather_3days = weather_3days;
	}
	public String getCitylist_xml() {
		return citylist_xml;
	}
	public void setCitylist_xml(String citylist_xml) {
		this.citylist_xml = citylist_xml;
	}

}
