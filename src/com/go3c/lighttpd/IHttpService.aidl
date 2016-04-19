
package com.go3c.lighttpd;


interface  IHttpService {
	void setConfigPath(String path);
	void startServer();
    void stopServer();
    void setEnvValue(String name,String value);
}
