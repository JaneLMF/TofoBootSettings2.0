package com.tencent.wseal;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class CPushMessage implements Serializable ,Parcelable{
	private static final long serialVersionUID = -8816592806835215573L;

	public String body;
	
	public String stockID;
	public String type;
	public int opt_info_type;
	public String opt_id;
	public String msg_id;

	public int badge;
	public String sound;

	@Override
	public int hashCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(body);
		sb.append(stockID);
		sb.append(type);
		sb.append(opt_info_type);
		sb.append(opt_id);
		sb.append(msg_id);
		sb.append(badge);
		sb.append(sound);
		return sb.toString().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o==null)
			return false;
		CPushMessage m = (CPushMessage) o;
		return this.hashCode() == m.hashCode();
	}
	
	public String printString(){
		String ret = "";
		ret+= (" msg body:"+body);
		ret+= (" msg stockId:"+stockID);
		ret+= (" msg type:"+type);
		ret+= (" msg opt_info_type:"+opt_info_type);
		ret+= (" msg opt_id:"+opt_id);
		ret+= (" msg msg_id:"+msg_id);
		ret+= (" msg badge:"+badge);
		ret+= (" msg sound:"+sound);
		return ret;
		
	}
	
	public CPushMessage(){
		
	}
	
	public static CPushMessage buildFromString(String str) {
		CPushMessage message = new CPushMessage();
		try {
			JSONObject js = new JSONObject(str);
			
			if (js.has("aps")) {
				JSONObject aps = js.getJSONObject("aps");
				if (aps.has("badge")) {
					message.badge = aps.getInt("badge");
				}
				if (aps.has("sound")) {
					message.sound = aps.getString("sound");
				}
				JSONObject alert = aps.getJSONObject("alert");
				message.body = alert.getString("body");
				JSONArray loc_args = alert.getJSONArray("loc-args");
				message.stockID = loc_args.optString(0);
				message.type = loc_args.optString(1);
				message.opt_info_type = loc_args.optInt(2);
				message.opt_id = loc_args.optString(3);
				message.msg_id = loc_args.optString(4);
			}else
				return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return message;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(body);
		out.writeString(stockID);
		out.writeString(type);
		out.writeInt(opt_info_type);
		out.writeString(opt_id);
		out.writeString(msg_id);
		out.writeInt(badge);
		out.writeString(sound);
	}
	private CPushMessage(Parcel in){
		body = in.readString();
		stockID = in.readString();
		type = in.readString();
		opt_info_type=in.readInt();
		opt_id=in.readString();
		msg_id=in.readString();
		badge=in.readInt();
		sound=in.readString();
	}
	public static final Parcelable.Creator<CPushMessage>CREATOR = new Parcelable.Creator<CPushMessage>() {      
	       public CPushMessage createFromParcel(Parcel in) {       
	           return new CPushMessage(in);  
	       }  
	   
	       public CPushMessage[] newArray(int size) {         
	           return new CPushMessage[size];  
	       }  
	    };  

}
