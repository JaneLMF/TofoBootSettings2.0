package com.tvxmpp.model;


/**
 * 首先定义一些接口，需要实现一些什么样的功能，
 * 
 * 
 */
public interface IXMPPDataPaser {  
    /** 
     *  
     * 解析输入流 
     * @param inputStr 
     * @return 
     * @throws Exception 
     */  
    public XMPPData parse(String inputStr) throws Exception;  
    
    /** 
     *  
     * 序列化XMPPData对象集合，得到XML形式的字符串 
     * @param xmppdata 
     * @return 
     * @throws Exception 
     */  
    public String serialize(XMPPData xmppdata) throws Exception;  
  
  
}  