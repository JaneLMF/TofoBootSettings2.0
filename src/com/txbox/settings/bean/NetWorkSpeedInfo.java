package com.txbox.settings.bean;

public class NetWorkSpeedInfo {
	public long avgspeed = 0;// 平均网速
	public long curspeed = 0;//当前网速
	public long hadFinishedBytes = 0;// 已下载
	public long totalBytes = 1024;// 文件总大小，默认为1024bytes
	public int networkType = 0;// 网络类型，3G、wifi等
	public int downloadPercent = 0;// 下载文件百分比，0～100
}
