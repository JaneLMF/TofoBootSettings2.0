package com.txbox.settings.bean;

import java.util.ArrayList;

public class ProvinceBean {

	private String id;
	private String en;
	private String ch;
	private ArrayList<CityBean> citys;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEn() {
		return en;
	}
	public void setEn(String en) {
		this.en = en;
	}
	public String getCh() {
		return ch;
	}
	public void setCh(String ch) {
		this.ch = ch;
	}
	public ArrayList<CityBean> getCitys() {
		return citys;
	}
	public void setCitys(ArrayList<CityBean> citys) {
		this.citys = citys;
	}
	
	
}
