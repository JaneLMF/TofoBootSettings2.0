package com.pivos.cqdvbserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import org.cybergarage.http.HTTPServerList;

import com.pivos.utils.WebServer;
import com.txbox.settings.common.TXbootApp;

/**
 * Created by baiqiliang on 16/1/12.
 */
public class CqDvbService extends Service {

    private WebServer webServer = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        TXbootApp app = (TXbootApp) getApplicationContext();
        webServer = new WebServer(app);
        webServer.start();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        HTTPServerList httpServerList = webServer.getHttpServerList();
        httpServerList.stop();
        httpServerList.close();
        httpServerList.clear();
        webServer.interrupt();
    }
}
