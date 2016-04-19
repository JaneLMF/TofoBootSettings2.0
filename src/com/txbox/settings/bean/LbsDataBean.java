package com.txbox.settings.bean;
/**
 * 
 * @类描述：获取地理位置信息接口返回data实体类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.bean
 * @类名称：LbsDataBean	
 * @创建人：Administrator
 * @创建时间：2014-9-28下午2:46:21	
 * @修改人：Administrator
 * @修改时间：2014-9-28下午2:46:21	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class LbsDataBean {
	String country;//国家
	String province;//省份
	String city; //城市
	String ip; //ip地址
	String weather_city_id;//城市ID
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getWeather_city_id() {
		return weather_city_id;
	}
	public void setWeather_city_id(String weather_city_id) {
		this.weather_city_id = weather_city_id;
	}
	
}
