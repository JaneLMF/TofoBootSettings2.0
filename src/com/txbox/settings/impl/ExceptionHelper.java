package com.txbox.settings.impl;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class ExceptionHelper {
	
	public static String PrintStack(Throwable t) 
	{
		 if(t == null)
		     return null;
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
		 try
		 {
			    try{
			    	t.printStackTrace();//输出到system.err
			        t.printStackTrace(new PrintStream(baos));
			    }finally{
			        baos.close();
			    }
		 }
		 catch(IOException e)
		 {
			 
		 }
		   
		 return baos.toString();
	}

}
