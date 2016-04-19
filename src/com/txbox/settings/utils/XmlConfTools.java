package com.txbox.settings.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import com.txbox.settings.bean.ClipBean;

import android.util.Xml;

/**
 * 
 * @类描述：xml配置文件读写类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.utils
 * @类名称：XmlConfTools	
 * @创建人：Administrator
 * @创建时间：2014-9-19下午1:13:06	
 * @修改人：Administrator
 * @修改时间：2014-9-19下午1:13:06	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class XmlConfTools {
//	private static String XMLCONF_PATH = "/mnt/sdcard";
//	private static String XMLCONF_PATH = "/sdcard/Android/data/com.pivos.tofu/files/.tofu/userdata/addon_data/service.tencent.data.provider";
//	private static String XMLCONF_PATH = "/sdcard/Android";
	private static String XMLCONF_FILE = "settings.xml";
	
	/**
	 * 
	 * @描述: 获取配置文件字段
	 * @方法名: getValue
	 * @param name
	 * @return
	 * @throws Exception
	 * @返回类型 String
	 * @创建人 Administrator
	 * @创建时间 2014-9-19下午3:42:26	
	 * @修改人 Administrator
	 * @修改时间 2014-9-19下午3:42:26	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public static String getValue(String path, String name)throws Exception{        
		File dir = new File(path);
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		File localfile = new File(path,XMLCONF_FILE);
		if(!localfile.exists()) return null;
		FileInputStream inputxml =new FileInputStream(localfile);
        XmlPullParser pullParser = Xml.newPullParser();
        pullParser.setInput(inputxml, "UTF-8"); //为Pull解释器设置要解析的XML数据        
        int event = pullParser.getEventType();
        
        while (event != XmlPullParser.END_DOCUMENT){
            switch (event) {
            
            case XmlPullParser.START_DOCUMENT:             
                break;    
            case XmlPullParser.START_TAG: 
            	String tag = pullParser.getName();  
            	if(tag.equals("setting")){
            		String id = pullParser.getAttributeValue(0);
            		String value = pullParser.getAttributeValue(1);
            		//System.out.println("tag = " + tag + "id=" + id + "value=" + value);
            		if(id.equals(name)){
            			return value;
            		}
            		
            	}
                break;
                
            case XmlPullParser.END_TAG:
             
                break;
                
            }
            
            event = pullParser.next();
        }
        
        
        return null;
    }
    
 
	/**
	 * 
	 * @描述:保存字段到获取配置文件
	 * @方法名: setValue
	 * @param name
	 * @param value
	 * @throws Exception
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-19下午3:43:32	
	 * @修改人 Administrator
	 * @修改时间 2014-9-19下午3:43:32	
	 * @修改备注 
	 * @since
	 * @throws
	 */
    public static void setValue(String path, String name,String value) throws Exception {
    	
    	File dir = new File(path);
		if(!dir.exists()){
			dir.mkdirs();
		}
    	
    	File file = new File(path,XMLCONF_FILE);
    	
        
        List<Setting> settings = new ArrayList<Setting>();
        
        boolean isExist = false;
        if(file.exists()){
	        FileInputStream inputxml =new FileInputStream(file);
	        XmlPullParser pullParser = Xml.newPullParser();
	        pullParser.setInput(inputxml, "UTF-8"); //为Pull解释器设置要解析的XML数据        
	        int event = pullParser.getEventType();
	        while (event != XmlPullParser.END_DOCUMENT){
	            switch (event) {
	            
	            case XmlPullParser.START_DOCUMENT:             
	                break;    
	            case XmlPullParser.START_TAG: 
	            	String tag = pullParser.getName();  
	            	if(tag.equals("setting")){
	            		Setting setting = new Setting();
	            		String idName = pullParser.getAttributeValue(0);
	            		String idValue = pullParser.getAttributeValue(1);
	            		if(idName.equals(name)){
	            			isExist = true;
	            			idValue = value;
	            		}
	            		setting.setId(idName);
	            		setting.setValue(idValue);
	            		settings.add(setting);
	            	}
	                break;
	            case XmlPullParser.END_TAG:
	                break;
	            }
	            
	            event = pullParser.next();
	        }
        }
        if(isExist==false){
        	Setting setting = new Setting();
        	setting.setId(name);
    		setting.setValue(value);
    		settings.add(setting);
        }
        
        OutputStream out = new FileOutputStream(file);
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(out, "UTF-8");
        //serializer.startDocument("UTF-8", true);
        serializer.startTag(null, "settings");  
        
        for (Setting setting : settings) {
            serializer.startTag(null, "setting");            
            serializer.attribute(null, "id", setting.getId());
            serializer.attribute(null, "value", setting.getValue());     
            serializer.endTag(null, "setting");
        }        
        serializer.endTag(null, "settings");
        serializer.endDocument();
        out.flush();
        out.close();
        
    }
    
    public static void setPlaying10c(String path,String url,String title,String logo,List<ClipBean> clips) throws Exception {
    	File file = new File(path,"playing.10c");
        List<Setting> settings = new ArrayList<Setting>();
                  
        OutputStream out = new FileOutputStream(file);
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(out, "UTF-8");
        serializer.startTag(null, "playinfo");  
        serializer.startTag(null, "videourl");
        serializer.startTag(null, "title");
        serializer.text(title);
        serializer.endTag(null, "title");
        
        serializer.startTag(null, "logo");
        serializer.text(logo);
        serializer.endTag(null, "logo");
        serializer.startTag(null, "clipsinfo");
        
        int size = clips.size();
        for (int i=0;i<size;i++) {
        	ClipBean item = clips.get(i);
            serializer.startTag(null, "clipinfo");            
            serializer.attribute(null, "clipid", String.valueOf(i));
            serializer.startTag(null, "url");
            serializer.text(url+"&"+item.getClipparam());
            serializer.endTag(null, "url");
            serializer.startTag(null, "size");
            serializer.text(item.getClipsize());
            serializer.endTag(null, "size");
            serializer.startTag(null, "duration");
            serializer.text(String.valueOf(Float.valueOf(item.getDuration())/1000000));
            serializer.endTag(null, "duration");
            serializer.endTag(null, "clipinfo");
        } 
        serializer.endTag(null, "clipsinfo");
        serializer.endTag(null, "videourl");
        serializer.endTag(null, "playinfo");
        serializer.endDocument();
        out.flush();
        out.close();
        
    }
    
    static class Setting{
    	String id;
    	String value;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
    }
}
