package com.pivos.beans;

import java.util.ArrayList;

/**
 * Created by baiqiliang on 16/1/13.
 */
public class ChannelEntity  {
    private String categoryID = "";
    private String categoryName = "";
    private String iconImageURL = "";

    private ArrayList<LiveChannelEntity> liveChannels;

    public void setCategoryID(String categoryID){this.categoryID = categoryID;}
    public String getCategoryID(){return categoryID;}

    public void setCategoryName(String categoryName){this.categoryName = categoryName;}
    public String getCategoryName(){return this.categoryName;}

    public void setLiveChannels(ArrayList<LiveChannelEntity> list){this.liveChannels = list;}
}
