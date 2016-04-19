package com.txbox.settings.bean;
/**
 * 
 * @类描述：获取地理位置信息接口返回实体类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.bean
 * @类名称：LbsBean	
 * @创建人：Administrator
 * @创建时间：2014-9-28下午2:41:32	
 * @修改人：Administrator
 * @修改时间：2014-9-28下午2:41:32	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class LbsBean {
	private ResultBean result;
	private LbsDataBean data;
	public ResultBean getResult() {
		return result;
	}
	public void setResult(ResultBean result) {
		this.result = result;
	}
	public LbsDataBean getData() {
		return data;
	}
	public void setData(LbsDataBean data) {
		this.data = data;
	}
}
