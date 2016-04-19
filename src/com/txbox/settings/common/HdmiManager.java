package com.txbox.settings.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


import android.app.ActivityManager;
//import android.app.MboxOutputModeManager;
//import android.app.SystemWriteManager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

public class HdmiManager{
    private final String TAG = "HdmiManager";
    private Context mContext = null;
	//private static SystemWriteManager sw;
    //private static MboxOutputModeManager mom;


    private SharedPreferences sharedPrefrences = null;
    public HdmiManager(Context context){
    	
        mContext = context;
	//	sw = (SystemWriteManager) mContext.getSystemService("system_write");
        //mom = (MboxOutputModeManager) mContext.getSystemService(Context.MBOX_OUTPUTMODE_SERVICE);
    }
    
    public void hdmiPlugged(){
        Log.d(TAG,"===== hdmiPlugged()");
        //mom.setHdmiPlugged();
    }

    public void hdmiUnPlugged(){
        Log.d(TAG,"===== hdmiUnPlugged()");
        //mom.setHdmiUnPlugged();
    }

    public boolean isHDMIPlugged() {
        //return mom.isHDMIPlugged();
	return false;
    }

	public boolean isHdmiCvbsDual() {
		//return Utils.getPropertyBoolean(sw, "ro.platform.has.cvbsmode", false);
		return false;
	}
    
    public boolean ifModeIsSetting() {
        //return mom.ifModeIsSetting();
	return false;
    }
}
