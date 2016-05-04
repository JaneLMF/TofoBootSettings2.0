package com.tvxmpp.model;

import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;


public class XMPPDataPaserImpl implements IXMPPDataPaser {

	@Override
	public XMPPData parse(String inputStr) throws Exception {
		// TODO Auto-generated method stub
		XMPPData xmppdata = new XMPPData();
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(inputStr));
		int event = parser.getEventType(); 
		
		while (event != XmlPullParser.END_DOCUMENT) {  
			switch (event) {  
			    case XmlPullParser.START_DOCUMENT:  
			        break;  
			    case XmlPullParser.START_TAG:  
			    	String name = parser.getName();
			    	if (name.equalsIgnoreCase("type")) {
			    		xmppdata.setType(parser.nextText());// 如果后面是Text元素,即返回它的值
                    } else if (name.equalsIgnoreCase("subtype")) {
                    	xmppdata.setSubtype(parser.nextText());
                    } else if (name.equalsIgnoreCase("SessionId")) {
                    	xmppdata.setSessionId(parser.nextText());
                    } else if (name.equalsIgnoreCase("level")) {
                    	xmppdata.setLevel(parser.nextText());
                    } else if (name.equalsIgnoreCase("privateData")) {
                    	xmppdata.setPrivateData(parser.nextText());
                    }
			        break;  
			    case XmlPullParser.END_TAG:  
			        break;  
			    }  
			    event = parser.next();  
			}  
		return xmppdata;
	}

	@Override
	public String serialize(XMPPData xmppdata) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
