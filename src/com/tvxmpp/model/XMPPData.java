package com.tvxmpp.model;


public class XMPPData {  
	
	private String type;
    private String subtype;
    private String SessionId;
    private String level;    
    private String privateData;   
    
    public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSubtype() {
		return subtype;
	}
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}
	public String getSessionId() {
		return SessionId;
	}
	public void setSessionId(String sessionId) {
		SessionId = sessionId;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getPrivateData() {
		return privateData;
	}
	public void setPrivateData(String privateData) {
		this.privateData = privateData;
	}
}  