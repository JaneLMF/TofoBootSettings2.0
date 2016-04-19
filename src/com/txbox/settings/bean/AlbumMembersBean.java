package com.txbox.settings.bean;
/**
 * 
 * @类描述：获取家庭相册成员接口返回值实体类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.bean
 * @类名称：AlbumMembersBean	
 * @创建人：Administrator
 * @创建时间：2014-9-18下午8:59:44	
 * @修改人：Administrator
 * @修改时间：2014-9-18下午8:59:44	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class AlbumMembersBean {
	ResultBean result;
	AlbumMembersDataBean data;
	public ResultBean getResult() {
		return result;
	}
	public void setResult(ResultBean result) {
		this.result = result;
	}
	public AlbumMembersDataBean getData() {
		return data;
	}
	public void setData(AlbumMembersDataBean data) {
		this.data = data;
	}
}
