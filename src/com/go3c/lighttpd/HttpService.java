package com.go3c.lighttpd;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.txbox.settings.utils.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

      

public class HttpService extends Service {
    private  int isStarted = 0;
    
    private static final Class[] mStartForegroundSignature = new Class[] {
        int.class, Notification.class };
    private static final Class[] mStopForegroundSignature = new Class[] { boolean.class };
    private NotificationManager mNM;
    private Method mStartForeground;
    private Method mStopForeground;
    private Object[] mStartForegroundArgs = new Object[2];
    private Object[] mStopForegroundArgs = new Object[1];

    
    IHttpService.Stub mBinder = new IHttpService.Stub(){

		@Override
		public void setConfigPath(String path) throws RemoteException {
			// TODO Auto-generated method stub
			setHttpConfigPath(path);
		}

		@Override
		public void startServer() throws RemoteException {
			// TODO Auto-generated method stub
			startHttpServer();
		}

		@Override
		public void stopServer() throws RemoteException {
			// TODO Auto-generated method stub
			stopHttpServer();
		}

		@Override
		public void setEnvValue(String name, String value)
				throws RemoteException {
			// TODO Auto-generated method stub
			setHttpEnvValue(name,value);
		}
    	
    };
    
    
    @Override
	public IBinder onBind(Intent intent) {
		Log.d("HttpService", "onBind");
		return mBinder;
	}
	
	public boolean onUnbind(Intent intent) {
		Log.d("HttpService", "onUnbind");
		return super.onUnbind(intent);
	}
	
	
	@Override
	public void onCreate()
	{
		mNM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            mStartForeground = HttpService.class.getMethod("startForeground",
                    mStartForegroundSignature);
            mStopForeground = HttpService.class.getMethod("stopForeground",
                    mStopForegroundSignature);
        } catch (NoSuchMethodException e) {
            mStartForeground = mStopForeground = null;
        }
        // 我们并不需要为 notification.flags 设置 FLAG_ONGOING_EVENT，因为
        // 前台服务的 notification.flags 总是默认包含了那个标志位
        Notification notification =new Notification();
        // 注意使用  startForeground ，id 为 0 将不会显示 notification
        startForegroundCompat(1, notification);
		if( Utils.fileIsExists(this, "liblighttpd_jni.so")){
			 String pathString = Utils.getLibPath(this, "liblighttpd_jni.so");
			 System.load(pathString);
		}else {
			 System.loadLibrary("lighttpd_jni");
	   }
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		String path = "";
		path = getApplicationContext().getFilesDir().getAbsolutePath();
		String config = path+"/config/lighttpd.conf";
		String libpath = path+"/www/txbox";
		setHttpConfigPath(config);
		setHttpEnvValue("LD_LIBRARY_PATH",libpath);
		startHttpServer();
	}	
	@Override
	public void onDestroy() {//重写的onDestroy
		Log.d("HttpService", "onDestroy");
		if(isStarted != 0){
			native_HttpServer_stop();
			isStarted = 0;
		}
		super.onDestroy();
	}
	
	 // 以兼容性方式开始前台服务
    private void startForegroundCompat(int id, Notification n) {
        if (mStartForeground != null) {
            mStartForegroundArgs[0] = id;
            mStartForegroundArgs[1] = n;
            try {
                mStartForeground.invoke(this, mStartForegroundArgs);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return;
        }
        mNM.notify(id, n);
    }

    // 以兼容性方式停止前台服务
    private void stopForegroundCompat(int id) {
        if (mStopForeground != null) {
            mStopForegroundArgs[0] = Boolean.TRUE;
            try {
                mStopForeground.invoke(this, mStopForegroundArgs);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return;
        }
        //   在 setForeground 之前调用 cancel，因为我们有可能在取消前台服务之后
        //   的那一瞬间被kill掉。这个时候 notification 便永远不会从通知一栏移除
        mNM.cancel(id);
    }


	public void setHttpConfigPath(String path){
		native_set_config_path(path);
	}
	
	public void startHttpServer()
	{
		if(isStarted == 0){
			native_HttpServer_start();
			isStarted = 1;
		}
	}
	
	public void stopHttpServer(){
		if(isStarted != 0){
			native_HttpServer_stop();
			isStarted = 0;
		}
	}
	
	public void setHttpEnvValue(String name,String value){
		System.out.println("set http env name=" + name + "value=" +value);
		native_add_env_value(name,value);
	}
	
	
	public native int native_set_config_path(String path);
	public native int native_HttpServer_start();	
	public native int native_HttpServer_stop ();
	public native int native_add_env_value(String name,String value);
    static {
       // System.loadLibrary("lighttpd_jni");
    } 
	
	
}
