package com.txbox.settings.bean;

import java.util.ArrayList;

public class CountyBean {

	private String id;
	private String en;
	private String ch;
	private ArrayList<ProvinceBean> provinces;
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
	public ArrayList<ProvinceBean> getProvinces() {
		return provinces;
	}
	public void setProvinces(ArrayList<ProvinceBean> provinces) {
		this.provinces = provinces;
	}
	
	
	
}
