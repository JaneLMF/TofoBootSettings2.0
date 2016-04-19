package com.txbox.settings.bean;
/**
 * 
 * @类描述：获取家庭相册成员接口返回值members实体类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.bean
 * @类名称：AlbumMembers	
 * @创建人：Administrator
 * @创建时间：2014-9-18下午9:02:52	
 * @修改人：Administrator
 * @修改时间：2014-9-18下午9:02:52	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class AlbumMembers {
	String uid ;// 用户ID
	String wx_nick ;//“微信昵称”,
	String wx_headerurl; //“微信用户头像”,
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getWx_nick() {
		return wx_nick;
	}
	public void setWx_nick(String wx_nick) {
		this.wx_nick = wx_nick;
	}
	public String getWx_headerurl() {
		return wx_headerurl;
	}
	public void setWx_headerurl(String wx_headerurl) {
		this.wx_headerurl = wx_headerurl;
	}
}
