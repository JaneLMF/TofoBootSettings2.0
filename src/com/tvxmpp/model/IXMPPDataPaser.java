package com.tvxmpp.model;


/**
 * ���ȶ���һЩ�ӿڣ���Ҫʵ��һЩʲô���Ĺ��ܣ�
 * 
 * 
 */
public interface IXMPPDataPaser {  
    /** 
     *  
     * ���������� 
     * @param inputStr 
     * @return 
     * @throws Exception 
     */  
    public XMPPData parse(String inputStr) throws Exception;  
    
    /** 
     *  
     * ���л�XMPPData���󼯺ϣ��õ�XML��ʽ���ַ��� 
     * @param xmppdata 
     * @return 
     * @throws Exception 
     */  
    public String serialize(XMPPData xmppdata) throws Exception;  
  
  
}  