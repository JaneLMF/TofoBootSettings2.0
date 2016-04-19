package com.pivos.beans;

/**
 * Created by baiqiliang on 16/1/14.
 */
public class EpgEntity {
    private String channelID = "";
    private String epgID = "";
    private String type = "";
    private String title = "";
    private String description = "";
    private String startTimestamp = "";
    private String endTimestamp = "";
    private String serviceid = "";

    public void setChannelID(String channelID){this.channelID = channelID;}
    public String getChannelID(){return this.channelID;}

    public void setEpgID(String epgID){this.epgID = epgID;}
    public String getEpgID(){return this.epgID;}

    public void setType(String type){this.type = type;}
    public String getType(){return this.type;}

    public void setTitle(String title){this.title = title;}
    public String getTitle(){return this.title;}

    public void setDescription(String description){this.description = description;}
    public String getDescription(){return this.description;}

    public void setStartTimestamp(String startTimestamp){this.startTimestamp = startTimestamp;}
    public String getStartTimestamp(){return this.startTimestamp;}

    public void setEndTimestamp(String endTimestamp){this.endTimestamp = endTimestamp;}
    public String getEndTimestamp(){return this.endTimestamp;}
    
    public void setServiceid(String serviceid){ this.serviceid = serviceid.split(",")[2];}
    public String getServiceid(){ return this.serviceid;}
}
