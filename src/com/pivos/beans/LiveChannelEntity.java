package com.pivos.beans;

/**
 * Created by baiqiliang on 16/1/13.
 */
public class LiveChannelEntity {
    private String channelID = "";
    private String channelNr = "";//逻辑频道号
    private String channelName = "";
    private String listImageURL = "";
    private String playURL = "";
    private String detailsImageURL = "";
    private int seqNumber = 0;
    private String aspectRatio = "";
    private String serviceid = "";//频道的 serviceid, 是 tsid,onid,serviceid 的组合” 

    public void setChannelID(String channelID){this.channelID = channelID;}
    public String getChannelID(){return channelID;}

    public void  setChannelName(String channelName){this.channelName = channelName;}
    public String getChannelName(){return channelName;}

    public void setListImageURL(String image){this.listImageURL = image;}
    public String getListImageURL(){return this.listImageURL;}

    public void setPlayURL(String playURL){this.playURL = playURL;}
    public String getPlayURL(){return this.playURL;}

    public void setChannelNr(String channelNr){this.channelNr = channelNr;}
    public String getChannelNr(){return this.channelNr;}
    
    public void setServiceid(String serviceid){ this.serviceid = serviceid.split(",")[2];}
    public String getServiceid(){ return this.serviceid;}
}
