package com.pivos.beans;

/**
 * Created by baiqiliang on 16/1/18.
 */
public class PlayInfoEntity {
    private String instanceid = "";//要播放的节目的实例句柄
    private String connection_url = "";//传输该节目内容的连接的URL

    public void setInstanceid(String instanceid){this.instanceid = instanceid;}
    public String getInstanceid(){return this.instanceid;}

    public void setConnection_url(String connection_url){this.connection_url = connection_url;}
    public String getConnection_url(){return this.connection_url;}
}
