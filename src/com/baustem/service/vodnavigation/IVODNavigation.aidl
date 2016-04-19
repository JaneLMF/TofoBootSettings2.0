package com.baustem.service.vodnavigation;

interface IVODNavigation {

	/*
	 * 功能描述：
	 * 		按条件搜索VOD节目，如按影片名、导演、主演
	 * 
	 * 参数说明：
	 * 		target 	- 要搜索的内容
	 * 		type   	- 0 ：影片名；1：导演；2：主演
	 * 		startIndex	- 获取项目的起始位置
	 * 		maxCount	- 每次最大获取项目数
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Amount  total=”” numberReturned=””/>
	 * 		     <Items>
	 * 		          VODProgram*
	 * 		          SerialConatiner*
	 * 		     < /Items>
	 * 		</Response>
	 * */
	String searchContent(String target, int type, int startIndex, int maxCount);

	/*
	 * 功能描述：
	 * 		获得VOD内容列表
	 * 
	 * 参数说明：
	 * 		parentID 	- 父目录ID
	 * 		contentType	- 0:视频点播；1:电视回看；2:书签；3:收藏；4:时移电视
	 * 		startIndex	- 获取项目的起始位置
	 * 		maxCount	- 每次最大获取项目数
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Amount  total=”” numberReturned=””/>
	 * 		     <Containers>
	 * 		          GeneralContainer*
	 * 		     </Containers>
	 * 		     <Items>
	 * 		          VODProgram*
	 * 		          SerialConatiner*
	 * 		          TimeShiftProgram*
	 * 		     < /Items>
	 * 		</Response>
	 * */
	String getContentList(String parentID, int contentType, int startIndex, int maxCount);

	/*
	 * 功能描述：
	 * 		获得指定逻辑频道号的时移频道
	 * 
	 * 参数说明：
	 * 		channelNr 	- 逻辑频道号
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Items>
	 * 				TimeShiftProgram?
	 * 		     < /Items>
	 * 		</Response>
	 * */
	String getTimeShift(String channelNr);

	/*
	 * 功能描述：
	 * 		获得指定的VOD内容
	 * 
	 * 参数说明：
	 * 		contentid 	- VOD ID
	 * 		contentType	- 0:视频点播；1:电视回看；2:书签；3:收藏；4:时移电视
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Containers>
	 * 		          GeneralContainer?
	 * 		     </Containers>
	 * 		     <Items>
	 * 		          VODProgram?|SerialConatiner?|TimeShiftProgram?
	 * 		     < /Items>
	 * 		</Response>
	 * */
	String getContent(String contentid, int contentType);

	/*
	 * 功能描述：
	 * 		添加内容
	 * 
	 * 参数说明：
	 * 		contentid	- 
	 * 		contentName	- 
	 * 		contentType	- 0：书签；1：收藏；2:其它
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		</Response>
	 * */
	String addContent(String contentid, String contentName, int contentType);

	/*
	 * 功能描述：
	 * 		删除内容
	 * 
	 * 参数说明：
	 * 		contentid	- 
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		</Response>
	 * */
	String delContent(String contentid);

	/*
	 * 功能描述：
	 * 		获得指定逻辑频道号的时移节目列表
	 * 
	 * 参数说明：
	 * 		channelNr 	- 逻辑频道号
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Items>
	 * 				EPGEvent*
	 * 		     < /Items>
	 * 		</Response>
	 * */
	String getTimeShiftPrograms(String channelNr);

	/*
	 * 功能描述：
	 * 		获得指定Service ID的时移频道
	 * 
	 * 参数说明：
	 * 		serviceId 	- 频道的serviceid,是serviceid,tsid,onid的组合
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Items>
	 * 				TimeShiftProgram?
	 * 		     < /Items>
	 * 		</Response>
	 * */
	String getTimeShiftByServiceId(String serviceId);

	/*
	 * 功能描述：
	 * 		获得指定Service ID的时移节目列表
	 * 
	 * 参数说明：
	 * 		serviceId 	- 频道的serviceid,是serviceid,tsid,onid的组合
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Items>
	 * 				EPGEvent*
	 * 		     < /Items>
	 * 		</Response>
	 * */
	String getTimeShiftProgramsByServiceId(String serviceId);

	/*
	 * 功能描述：
	 * 		获得推荐节目列表
	 * 
	 * 参数说明：
	 * 		id 	- VOD节目ID
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		     <Items>
	 * 				VODProgram*
	 * 				SerialConatiner*
	 * 				TimeShiftProgram*
	 * 		     < /Items>
	 * 		</Response>
	 * */
	String getRecommendList(String id);

}
