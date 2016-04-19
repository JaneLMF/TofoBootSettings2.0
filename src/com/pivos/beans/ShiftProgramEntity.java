package com.pivos.beans;

/**
 * Created by baiqiliang on 16/1/15.
 */

/*
 * 表示时移节目。包括回看、书签
id=”标识” parentid=”所属父目录 ID” name=”影片名”
description =”简介” longDescription =”详细介绍”
playURLs=“播放地址,可以使 HTTP 协议或 RTSP 协议,多个之间以逗号分隔”channelNr=” 所属频道的逻辑频道号”
channelName=”所属频道的名称”
logoURL=”台标”
startTime=”节目开始时间,格式 yyyy-MM-dd HH:mm:ss” endTime=”节目结束时间,格式 yyyy-MM-dd HH:mm:ss” serviceid =”
频道的 serviceid,是 tsid,onid,serviceid 的组合”
 */

public class ShiftProgramEntity {
    private String id = "";
    private String name = "";
    private String playUrls = "";
    private String channelName = "";
    private String startTime = "";
    private String endTime = "";
    private String serviceid = "";//频道的 serviceid, 是 tsid,onid,serviceid 的组合” 

    public void setId(String id){ this.id = id;}
    public String getId(){ return this.id;}

    public void setName(String name){ this.name = name;}
    public String getName(){ return this.name;}

    public void setPlayUrls(String playUrls){ this.playUrls = playUrls;}
    public String getPlayUrls(){ return this.playUrls;}

    public void setChannelName(String channelName){ this.channelName = channelName;}
    public String getChannelName(){ return this.channelName;}

    public void setStartTime(String startTime){this.startTime = startTime;}
    public String getStartTime(){ return this.startTime;}

    public void setEndTime(String endTime){ this.endTime = endTime;}
    public String getEndTime(){ return this.endTime;}
    
    public void setServiceid(String serviceid){ this.serviceid = serviceid.split(",")[2];}
    public String getServiceid(){ return this.serviceid;}
}
