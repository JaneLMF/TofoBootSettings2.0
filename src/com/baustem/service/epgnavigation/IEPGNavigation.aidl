package com.baustem.service.epgnavigation;

interface IEPGNavigation {

	/*
	 * 功能描述：
	 * 		浏览EPG目录下的内容
	 * 
	 * 参数说明：
	 * 		parentID 	- 父目录ID（""表示根目录）
	 * 		startIndex	- 获取项目的起始位置
	 * 		maxCount	- 每次最大获取项目数
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Amount  total=”” numberReturned=””/>
	 * 		     <Containers>
	 * 		          EPGContainer*
	 * 		     </Containers>
	 * 		     <Items>
	 * 		          EPGEvent*
	 * 		     < /Items>
	 * 		</Response>
	 * */
	String browseEPG(String parentID, int startIndex, int maxCount);

	/*
	 * 功能描述：
	 * 		获取EPG列表
	 * 
	 * 参数说明：
	 * 		channelNr 	- 逻辑频道号,如果为空则返回所有频道的EPG
	 * 		startTime	- 事件开始时间,如果为空则表示任何时间都可
	 * 		endTime  	- 事件结束时间,如果为空则表示任何时间都可
	 * 		startIndex	- 每次获取的起始位置
	 * 		maxCount	- 每次获取的最大数
	 * 
	 * 返回结果：
	 * 		返回EPGEvent的列表
	 * 		<Response code=”” message=””>
	 * 		     <Amount  total=”” numberReturned=””/>
	 * 		     <Items>
	 * 		          EPGEvent*
	 * 		     < /Items>
	 * 		</Response>
	 * */
	String getEPGList(String channelNr, String startTime, String endTime, int startIndex, int maxCount);
	
	/*
	 * 功能描述：
	 * 		浏览PF目录下的内容
	 * 
	 * 参数说明：
	 * 		parentID 	- 父目录ID（""表示根目录）
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Amount  total=”” numberReturned=””/>
	 * 		     <Containers>
	 * 		          EPGContainer*
	 * 		     </Containers>
	 * 		     <Items>
	 * 		          EPGEvent*
	 * 		     < /Items>
	 * 		</Response>
	 * */
	String browsePF(String parentID);
	
	/*
	 * 功能描述：
	 * 		获取指定频道的PF信息
	 * 
	 * 参数说明：
	 * 		channelNr 	- 逻辑频道号
	 * 
	 * 返回结果：
	 * 		返回两条EPGEvent,当前Event和下一个Event
	 *		<Response code=”” message=””>
	 * 		     <Amount  total=”” numberReturned=””/>
	 * 		     <Items>
	 * 		          EPGEvent*
	 * 		     < /Items>
	 * 		</Response>
	 * */
	String getPF(String channelNr);

	/*
	 * 功能描述：
	 * 		获取指定的Event信息
	 * 
	 * 参数说明：
	 * 		eventid 	- 
	 * 
	 * 返回结果：
	 * 		返回EPGEvent
	 *		<Response code=”” message=””>
	 * 		     <Items>
	 * 		          EPGEvent*
	 * 		     < /Items>
	 * 		</Response>
	 * */
	String getEvent(String eventid);

}
