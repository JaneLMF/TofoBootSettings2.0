package com.txbox.settings.bean;

import java.util.ArrayList;

/**
 * 
 * @类描述：天威登录接口返回值Data部分实体类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.bean
 * @类名称：PivosLoginDataBean	
 * @创建人：ylt
 * @创建时间：2015-8-31下午10:58:06	
 * @修改人：ylt
 * @修改时间：2015-8-31下午10:58:06	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170*@qq.com
 */
public class PivosLoginDataBean {
	private String su;
	private ArrayList<UserIdBean> userid;
	public String getSu() {
		return su;
	}
	public void setSu(String su) {
		this.su = su;
	}
	public ArrayList<UserIdBean> getUserid() {
		return userid;
	}
	public void setUserid(ArrayList<UserIdBean> userid) {
		this.userid = userid;
	}
}
