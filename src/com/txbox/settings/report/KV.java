package com.txbox.settings.report;

import android.text.TextUtils;

public class KV {

	public KV(String key, Object value) {
		super();
		this.key = key;
		this.value = value;
	}
	
	private String key;
	private Object value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean isValid() {
		return !TextUtils.isEmpty(key) && value != null && !TextUtils.isEmpty(value.toString());
	}

}
