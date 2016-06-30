package com.tvxmpp.model;
public class Messege{
		String type;
		String msgId;
		String msg;
		String leve;
		boolean isNotify;
		
		public Messege(String type, String msgId, String msg, String leve, boolean isNotify){
			this.type = type;
			this.msgId = msgId;
			this.msg = msg;
			this.leve = leve;
			this.isNotify = isNotify;
		}
		
		public Messege(String type, String msgId, String msg, String leve){
			this.type = type;
			this.msgId = msgId;
			this.msg = msg;
			this.leve = leve;
			this.isNotify = false;
		}

		
		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getMsgId() {
			return msgId;
		}

		public void setMsgId(String msgId) {
			this.msgId = msgId;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public String getLeve() {
			return leve;
		}

		public void setLeve(String leve) {
			this.leve = leve;
		}

		public void setNofity(boolean isNotify) {
			this.isNotify = isNotify;
		}

		public boolean isNotify() {
			return isNotify;
		}
	}