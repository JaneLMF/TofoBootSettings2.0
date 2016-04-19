package com.txbox.settings.bean;
/**
 * 
 * @类描述：天威登录接口返回值实体类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.bean
 * @类名称：PivosLoginBean	
 * @创建人：ylt
 * @创建时间：2015-8-31下午10:56:49	
 * @修改人：ylt
 * @修改时间：2015-8-31下午10:56:49	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170*@qq.com
 */
public class PivosLoginBean {
	private ResultBean result;
	private PivosLoginDataBean data;
	public ResultBean getResult() {
		return result;
	}
	public void setResult(ResultBean result) {
		this.result = result;
	}
	public PivosLoginDataBean getData() {
		return data;
	}
	public void setData(PivosLoginDataBean data) {
		this.data = data;
	}
}
