package com.go3c.qrencodec;

import android.annotation.SuppressLint;
import android.content.Context;
 
public class Qrenc {
	/*
	 *  Create QRencode PNG file from String
	 *  Param:
	 *     url: the url to create the png stream
	 *     path: the path for saving the png file
	 *     size: specify module size in dots (pixels), default is 3.
	 *     errorCorrection:[LMQH], specify error correction level from L (lowest) to H (highest). 
	 *  Return Value:
	 *       0: failed!
	 *       1: succes!  
	 */
	public int String2Png(String url, String path, int size,String errorCorrection)
	{
		if(url == null || url.length()==0) return 0;
		if(path == null || path.length()==0) return 0;
		String strSize = String.format("%d", size == 0? 3:size);
		String strError = null;
		if(errorCorrection == null || errorCorrection.length() == 0)
			strError = "H";
		else strError = errorCorrection;
		return  native_qrencodec_encode(url,path,strSize,strError);
	}
	
	 
	public native int native_qrencodec_encode(String url,String path,String size,String errorCorrection);
	
    static {
    	System.loadLibrary("qrencodec");
    } 
}
