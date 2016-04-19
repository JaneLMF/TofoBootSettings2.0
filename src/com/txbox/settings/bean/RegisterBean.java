package com.txbox.settings.bean;

/**
 * 
 * @类描述： 设备注册返回值实体类
 * @项目名称：TXBootSettings
 * @包名： com.example.txbootsettings.bean
 * @类名称：RegisterBean
 * @创建人：Administrator
 * @创建时间：2015-8-22下午15:42:19	
 * @修改人：Administrator
 * @修改时间：2015-8-22下午15:42:19		
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class RegisterBean {
	private ResultBean result;
	private DataBean data;
	public ResultBean getResult() {
		return result;
	}
	public void setResult(ResultBean result) {
		this.result = result;
	}
	public DataBean getData() {
		return data;
	}
	public void setData(DataBean data) {
		this.data = data;
	}
}
