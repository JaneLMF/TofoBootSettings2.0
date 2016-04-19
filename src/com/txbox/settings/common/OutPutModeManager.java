package com.txbox.settings.common;

//import android.app.SystemWriteManager;
//import android.app.MboxOutputModeManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;






public class OutPutModeManager {
    private static final String TAG = "OutPutModeManager";

	private Context mContext = null;

    private static String[] ALL_HDMI_MODE_VALUE_LIST = {"1080p","1080p50hz","1080p24hz","720p","720p50hz","4k2k24hz","4k2k25hz","4k2k30hz","4k2ksmpte","576p","480p","1080i","1080i50hz","576i","480i"};
    private static String[] ALL_HDMI_MODE_TITLE_LIST = {"1080p 60hz","1080p 50hz","1080p 24hz","720p 60hz","720p 50hz","4k2k 24hz","4k2k 25hz","4k2k 30hz","4k2k smpte","576p 50hz","480p 60hz","1080i 60hz","1080i 50hz","576i 50hz","480i 60hz" };
    private static final String[] CVBS_MODE_VALUE_LIST = {"480cvbs","576cvbs"}; 
    private static final String[] CVBS_MODE_TITLE_LIST = {"480 CVBS","576 CVBS"}; 
    private static final String HDMI_MODE_PROP = "ubootenv.var.hdmimode";
    private static final String COMMON_MODE_PROP = "ubootenv.var.outputmode";
    private static final String NO_FILTER_SET="no_filter_set";
   
	private final static String DISPLAY_MODE_SYSFS = "/sys/class/display/mode";
    private final static String HDMI_ONLY_MODE = "ro.platform.hdmionly";

    //=== hdmi + cvbs dual
    private final static String DISPLAY_MODE_SYSFS_DUAL = "/sys/class/display2/mode";
    private final static String HDMI_CVBS_DUAL = "ro.platform.has.cvbsmode"; 

    private ArrayList<String> mTitleList = null;
    private ArrayList<String> mValueList = null;
    private ArrayList<String> mSupportList = null;
    

	//private static SystemWriteManager sw;
    //private static MboxOutputModeManager mom;

    private static String mUiMode = "hdmi";

	private SharedPreferences sharepreference = null;
    private boolean hasRealOutput = false;
    private final static int UPDATE_OUTPUT_MODE_UI = 101;
    private static final int hdmi4KmodeNum =  4 ;

    public OutPutModeManager(Context context , String mode){
        mContext = context;
        mUiMode = mode;
		sharepreference = mContext.getSharedPreferences(MboxConst.PREFERENCE_BOX_SETTING,
				Context.MODE_PRIVATE);
		
		//sw = (SystemWriteManager) mContext.getSystemService("system_write");
        //mom = (MboxOutputModeManager) mContext.getSystemService(Context.MBOX_OUTPUTMODE_SERVICE);
        mTitleList = new ArrayList<String>();
        mValueList = new ArrayList<String>();
        initModeValues(mUiMode);  
    }
	
	public OutPutModeManager(Context context, ListView listview , String mode) {
		mContext = context;
		//sw = (SystemWriteManager) mContext.getSystemService("system_write");
        mUiMode = mode;
        initModeValues(mUiMode);
	}

	
	public ArrayList<String>getOutPutModeList()
	{
		return mTitleList;
	}

    public int getCurrentSelectItemID(){
         /*String currentHdmiMode = sw.readSysfs(DISPLAY_MODE_SYSFS);
         for(int i=0 ; i < mValueList.size();i++){
             if(currentHdmiMode.equals(mValueList.get(i))){
                return i ;
             }
         }*/
         
         if(mUiMode.equals("hdmi")){
            return 4;
         }else{
            return 0;
         }   
    }

    public boolean isModeSupported(String mode){
    	
        for(int i=0 ; i < mValueList.size();i++){
            if(mode.equals(mValueList.get(i))){
               return true ;
            }
        }
    	return false;
    }
    
    
    private void initModeValues(String mode){
        mTitleList = new ArrayList<String>();
        mValueList = new ArrayList<String>();
        mSupportList = new ArrayList<String>();
        filterOutputMode();
       /* 
        hasRealOutput = sw.getPropertyBoolean("ro.platform.has.realoutputmode", false);
        if(mode.equalsIgnoreCase("hdmi")){
            for(int i=0 ; i< ALL_HDMI_MODE_VALUE_LIST.length; i++){
                mTitleList.add(ALL_HDMI_MODE_TITLE_LIST[i]);
                mValueList.add(ALL_HDMI_MODE_VALUE_LIST[i]);}      
        }else if(mode.equalsIgnoreCase("cvbs")){          
            for(int i = 0 ; i< CVBS_MODE_VALUE_LIST.length; i++){
                mTitleList.add(CVBS_MODE_VALUE_LIST[i]);
            }          
            for(int i=0 ; i < CVBS_MODE_VALUE_LIST.length ; i++){
                mValueList.add(CVBS_MODE_VALUE_LIST[i]);
            }
        }*/ 
   
    }

	public static String getCurrentOutPutModeTitle( String ui) {
        Log.d(TAG,"==== getCurrentOutPutMode() " );
        /*String currentHdmiMode = sw.readSysfs(DISPLAY_MODE_SYSFS);
        
        int type = 0;
        if(ui.equalsIgnoreCase("hdmi"))
            type = 1;
        if(type==0){  // cvbs
           if(currentHdmiMode.contains("cvbs")){
                for(int i=0 ; i < CVBS_MODE_VALUE_LIST.length ; i++){
                    if(currentHdmiMode.equals(CVBS_MODE_VALUE_LIST[i])){
                        return CVBS_MODE_TITLE_LIST[i] ;
                    }
                }
            }
            return CVBS_MODE_TITLE_LIST[0];
        }else{      // hdmi
            for(int i=0 ; i< ALL_HDMI_MODE_VALUE_LIST.length ; i++){
                if(currentHdmiMode.equals(ALL_HDMI_MODE_VALUE_LIST[i])){
                    Log.d(TAG,"==== get the title is :" + ALL_HDMI_MODE_TITLE_LIST[i]);
                    return ALL_HDMI_MODE_TITLE_LIST[i] ;
                }
            }
            return ALL_HDMI_MODE_TITLE_LIST[4];
		}
	*/
		return null;
	}

	public void selectItem(int index) {
        /*final String oldMode = sw.readSysfs(DISPLAY_MODE_SYSFS);
        String mode = mValueList.get(index) ;
        if(mode.equals(oldMode)){
            Log.d(TAG,"===== the same mode with current !");
            return ;
        }
        change2NewMode(mode); 
	*/
	}

    boolean getAutoHDMIMode() {
        boolean isAutoHdmiMode = true;
        /*try {
            isAutoHdmiMode = ((0 == Settings.Global.getInt(mContext.getContentResolver(), Settings.Global.DISPLAY_OUTPUTMODE_AUTO))?false:true) ;
        } catch (Settings.SettingNotFoundException se) {
            Log.d(TAG, "Error: "+se);
        }*/
        return isAutoHdmiMode;
    }
    



	public void setAutoOutputMode(boolean enable){
        /*boolean isAutoHdmiMode = getAutoHDMIMode();
        if (enable == false) {
            Settings.Global.putInt(mContext.getContentResolver(), Settings.Global.DISPLAY_OUTPUTMODE_AUTO, 0);
        } else {
            Settings.Global.putInt(mContext.getContentResolver(), Settings.Global.DISPLAY_OUTPUTMODE_AUTO, 1);
            
            mom.setHdmiPlugged();
        }*/
	}

	
	public boolean isAutoOutputMode(){
        return getAutoHDMIMode();
	}
    public void change2NewMode(final String mode) {
        //SettingsMboxActivity.oldMode = Utils.readSysFile(sw, DISPLAY_MODE_SYSFS);
        
        //mom.setOutputMode(mode);

           
        return; 
	}
    


    public String getBestMatchResolution() {
        //return mom.getBestMatchResolution();
	return null;
    }
/*
    public void hdmiPlugged(){
        Log.d(TAG,"===== hdmiPlugged()");
        mom.setHdmiPlugged();
    }

    public void hdmiUnPlugged(){
        Log.d(TAG,"===== hdmiUnPlugged()");
        mom.setHdmiUnPlugged();
    }

    public boolean isHDMIPlugged() {
        return mom.isHDMIPlugged();
    }

	public boolean isHdmiCvbsDual() {
		return Utils.getPropertyBoolean(sw, "ro.platform.has.cvbsmode", false);
	}
    
    public boolean ifModeIsSetting() {
        return mom.ifModeIsSetting();
    }
*/
    public void  filterOutputMode() {
        /*String str_filter_mode = sw.getPropertyString("ro.platform.filter.modes", "");
        
        if(str_filter_mode == null || str_filter_mode.length() == 0){
            return;
        }
        
        String[] array_filter_mode = str_filter_mode.split(",");
        List<String> list_value = new ArrayList<String>();
        List<String> list_title = new ArrayList<String>();

        for (int i = 0; i < ALL_HDMI_MODE_VALUE_LIST.length; i++){
            list_value.add(ALL_HDMI_MODE_VALUE_LIST[i]);
            list_title.add(ALL_HDMI_MODE_TITLE_LIST[i]);
        }

        for (int i = 0; i < array_filter_mode.length; i++){
            for (int j = 0; j < list_value.size(); j++){
                if((list_value.get(j).toString()).equals(array_filter_mode[i])){
                    list_value.remove(j);
                    list_title.remove(j);
                }
            }
        }

        ALL_HDMI_MODE_VALUE_LIST = list_value.toArray(new String[list_value.size()]);
        ALL_HDMI_MODE_TITLE_LIST = list_title.toArray(new String[list_title.size()]);
	*/
    }
	private void setStatusBarProperty(String value) {
		System.out.println("Hide the StatusBar :" +value);
		//sw.setProperty("persist.sys.hideStatusBar", value);
	}

	private String getStatusBarProperty() {
		//return sw.getPropertyString("persist.sys.hideStatusBar", "true");
		return null;
	}
	
    public  boolean isStatusBarHide()
    { 
    	String val = getStatusBarProperty();  
    	if(val.equalsIgnoreCase("true"))
    		return true;
    	else return false;
    }
    
	public void hideStatusBar(boolean hide) 
	{
		Editor editor = sharepreference.edit();

		if (hide) {
			setStatusBarProperty("true");
			editor.putString("hide_status_bar", "true");
			editor.commit();
		} else {
			setStatusBarProperty("false");
			editor.putString("hide_status_bar", "false");
			editor.commit();
		}
	}


    
}
