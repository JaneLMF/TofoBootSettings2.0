package com.txbox.settings.bean;

/**
 * 
 * @类描述：获取音乐背景图片接口返回实体类
 * @项目名称：TXBootSettings
 * @包名： com.example.txbootsettings.bean
 * @类名称：MusicBackPicBean	
 * @创建人：Administrator
 * @创建时间：2014-9-9下午5:03:21	
 * @修改人：Administrator
 * @修改时间：2014-9-9下午5:03:21	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright go3c
 * @mail 939757170@qq.com
 */
public class MusicBackPicBean {
	ResultBean result;
	MusciBackPicDataBean data;
	public ResultBean getResult() {
		return result;
	}
	public void setResult(ResultBean result) {
		this.result = result;
	}
	public MusciBackPicDataBean getData() {
		return data;
	}
	public void setData(MusciBackPicDataBean data) {
		this.data = data;
	}
}
