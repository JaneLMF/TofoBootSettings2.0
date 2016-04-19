package com.txbox.settings.bean;

public class LoginBean {
	private String appid;//应用ID，在开放平台创建应用时分配
	private String bid;//业务ID，由腾讯分配
	private String nick;//昵称
	private String face;//头像
	private String openid;//QQ号对应的openID
	private String accesstoken;//访问凭证
	private int islogin;//是否已经登录
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getFace() {
		return face;
	}
	public void setFace(String face) {
		this.face = face;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getAccesstoken() {
		return accesstoken;
	}
	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}
	public int getIslogin() {
		return islogin;
	}
	public void setIslogin(int islogin) {
		this.islogin = islogin;
	}
}
