package com.baustem.service.dvbnavigation;

interface IDVBNavigation {

	/*
	 * 功能描述：
	 * 		按照给定的目录ID获取该目录下的子目录或频道
	 * 
	 * 参数说明：
	 * 		parentID 	- DVB目录ID,如果parentID==“”那么表示从根目录开始获取
	 * 
	 * 返回结果：
	 * 		<Response  code=””  message=””>
	 * 		     <Amount  total=”” numberReturned=””/>
	 * 		     <Containers>
	 * 		          DVBContainer*
	 * 		     < /Containers >
	 * 		     <Items>
	 * 		          Channel*
	 * 		     < /Items>
	 * 		</ Response >
	 * */
	String getChannelList(String parentID);

	/*
	 * 功能描述：
	 * 		按照给定的内容ID获取该内容信息
	 * 
	 * 参数说明：
	 * 		contentid 	- DVB内容ID
	 * 
	 * 返回结果：
	 * 		<Content code=”” message=””>
	 * 		     <Containers>
	 * 		          DVBContainer？
	 * 		     < /Containers >
	 * 		     <Items>
	 * 		          Channel？
	 * 		     < /Items>
	 * 		</Content>
	 * */
	String getChannel(String contentid);

}
