package com.txbox.settings.bean;

import java.util.List;
/**
 * 
 * @类描述：未来3天天气data实体类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.bean
 * @类名称：Weather3DaysDataBean	
 * @创建人：Administrator
 * @创建时间：2014-10-11下午3:32:42	
 * @修改人：Administrator
 * @修改时间：2014-10-11下午3:32:42	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class Weather3DaysDataBean {
	String bi_name;
	List<Weather3DaysItemBean> d3;
	public String getBi_name() {
		return bi_name;
	}
	public void setBi_name(String bi_name) {
		this.bi_name = bi_name;
	}
	public List<Weather3DaysItemBean> getD3() {
		return d3;
	}
	public void setD3(List<Weather3DaysItemBean> d3) {
		this.d3 = d3;
	}
}


