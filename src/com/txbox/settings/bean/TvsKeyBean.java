package com.txbox.settings.bean;
/**
 * 
 * @类描述：获取临时登录态返回结果实体类
 * @项目名称：TXBootSettings
 * @包名： com.example.txbootsettings.bean
 * @类名称：TvsKeyBean	
 * @创建人：Administrator
 * @创建时间：2014-9-4下午8:21:08	
 * @修改人：Administrator
 * @修改时间：2014-9-4下午8:21:08	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class TvsKeyBean {
	ResultBean result;
	TvsKeyDataBean data;
	public ResultBean getResult() {
		return result;
	}
	public void setResult(ResultBean result) {
		this.result = result;
	}
	public TvsKeyDataBean getData() {
		return data;
	}
	public void setData(TvsKeyDataBean data) {
		this.data = data;
	}
}
