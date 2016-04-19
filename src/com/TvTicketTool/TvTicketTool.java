/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.TvTicketTool;
import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import android.os.Bundle;


public class TvTicketTool
{
	/**
	 * 
	 * @描述:生成ticket_client供获取临时登录态使用
	 * @方法名: GetTicketClient
	 * @param guid
	 * @param secret
	 * @return
	 * @返回类型 String
	 * @创建人 Administrator
	 * @创建时间 2014-9-5下午1:17:06	
	 * @修改人 Administrator
	 * @修改时间 2014-9-5下午1:17:06	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public String GetTicketClient(String guid,String secret){
		String BigTicket = "";
		String Random = "random code for ticket";
		BigTicket = getBigTicket(guid, secret, Random);
	    if("" == BigTicket)
	    {
	    	Log.v("bigTicket", "getBigTicket Failed.");
	        //check if the input string is too large.
	    }else
	    {
	        Log.v("bigTicket", BigTicket);
	    }
	    return BigTicket;
	}
	/**
	 * 
	 * @描述:解析含guid_tvskey(临时登录态)的加密串
	 * @方法名: DecGuidSkey
	 * @param guid
	 * @param ticket_server
	 * @return
	 * @返回类型 String
	 * @创建人 Administrator
	 * @创建时间 2014-9-5下午1:18:42	
	 * @修改人 Administrator
	 * @修改时间 2014-9-5下午1:18:42	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public String DecGuidSkey(String guid,String ticket_server){
		 String strTvSeky;
		 String Random = "random code for ticket";
	     strTvSeky = decGuidSkey(ticket_server, Random, guid);
	     if("" == strTvSeky)
	     {
	        Log.v("decTvSkey", "decTvSkey failed.");
	     }else
	     {
	        Log.v("decTvSkey", strTvSeky);
	     }
	     return strTvSeky;
	}
	/**
	 * 
	 * @描述: 生成token，在设置全局配置时，服务器端鉴权使用
	 * @方法名: GenToken
	 * @return
	 * @返回类型 int
	 * @创建人 Administrator
	 * @创建时间 2014-9-24上午11:31:42	
	 * @修改人 Administrator
	 * @修改时间 2014-9-24上午11:31:42	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public static int GenToken(String strTvSeky)
	{
		int iRet = -1;
		int dwGtoken ;
		int dwHash = 5381;
		int dwLen = strTvSeky.length();
		for(int i = 0; i < dwLen; i++)
		{
			dwHash += (dwHash << 5) + strTvSeky.charAt(i);
		}

		dwGtoken = dwHash & 0x7fffffff;


		return dwGtoken;
	}
  
    /*
     * 功能: 生成ticket_client供获取临时登录态使用
     *
     * 输入:
     * @strGuid: GUID,目前为定长32
     * @strSecret: 初始化时返回的密码
     * @strRandom: 随机字符串，用于加密ticket_client和解密登录态.长度小于128
     *
     * 输出:
     * @strTicket: 生成的BigTicket
     * */
    public native String  getBigTicket(String strGuid, String strSecret, String strRandom);
    
    /*
     * 功能: 解析含guid_tvskey(临时登录态)的加密串
     *
     * 输入:
     * @strTicketServer: 含临时登录态的加密串.调用获取临时登录态接口获得
     * @strRandom: 解密key。即生成BigTicket时的随机字符串
     * @strGuid: GUID，用于验证
     *
     * 输出
     * @strTvSkey: 临时登录态。目前长度<128
     * */
    public native String  decGuidSkey(String strTicketServer, String strRandom, String strGuid);
    
    static {
        System.loadLibrary("tv-ticket-tool");
    }
}
