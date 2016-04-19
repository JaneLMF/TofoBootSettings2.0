package com.pivos.beans;

public class VODProgramEntity {
	private boolean isSerial = false;// «∑ÒµÁ ”æÁ
	private String id = "";
	private String name = "";
	private String description = "";
	private String director = "";
	private String actor = "";
	private String duration = "";
	private String playUrls = "";
	private String posterURLs = "";
	
	public void setIsSerial(boolean isSerial){ this.isSerial = isSerial;}
	public boolean getIsSerial(){ return isSerial;}
	
	public void setId(String id){ this.id = id;}
    public String getId(){ return this.id;}
    
    public void setName(String name){ this.name = name;}
    public String getName(){ return this.name;}
    
    public void setDescription(String description){this.description = description;}
    public String getDesString(){ return this.description;}
    
    public void setDirector(String director){ this.director = director;}
    public String getDirector(){ return this.director;}
    
    public void setActor(String actor){ this.actor = actor;}
    public String getActor(){ return this.actor;}
    
    public void setDuration(String duration){ this.duration = duration;}
    public String getDuration(){ return this.duration;}
    
    public void setPlayUrls(String playUrls){ this.playUrls = playUrls;}
    public String getPlayUrls(){ return this.playUrls;}
    public void setPosterURLs(String posterURLs){ this.posterURLs = posterURLs;}
    public String getPosterURLs(){ return this.posterURLs;}
}
