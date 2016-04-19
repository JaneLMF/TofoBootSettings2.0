package com.baustem.service.vodstream;

interface IVODStream {

	/*
	 * 功能描述：
	 * 		获得播放VOD节目的URL和播放实例ID
	 * 
	 * 参数说明：
	 * 		playURL 	- VOD影片中的playURL属性值
	 * 
	 * 返回结果：
	 * 		返回要播放的节目的实例句柄(INSTANCEID)和传输该节目内容的连接的URL(CONNECTION_URL).
	 * 		<Response code=”” message=””>
	 * 		   	<instanceid></ instanceid >
	 * 			<connection_url></connection_url>
	 * 		</Response>
	 * */
	String getPlayInfo(String playURL);

	/*
	 * 功能描述：
	 * 		播放VOD节目,可以指定倍速
	 * 
	 * 参数说明：
	 * 		instanceID 	- 播放句柄
	 * 		speed     	- 播放速度,0为正常播放，大于0为快进，小于0为快退
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		</Response>
	 * */
	String play(String instanceID, int speed);

	/*
	 * 功能描述：
	 * 		停止正在播放的VOD节目
	 * 
	 * 参数说明：
	 * 		instanceID 	- 播放句柄
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		</Response>
	 * */
	String stop(String instanceID);

	/*
	 * 功能描述：
	 * 		让正在播放的VOD节目跳到指定的时刻
	 * 
	 * 参数说明：
	 * 		instanceID 	- 播放句柄
	 * 		time    	- 格式：00:00:00
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		</Response>
	 * */
	String seek(String instanceID, String time);

	/*
	 * 功能描述：
	 * 		让正在播放的VOD节目暂停
	 * 
	 * 参数说明：
	 * 		instanceID 	- 播放句柄
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		</Response>
	 * */
	String pause(String instanceID);

	/*
	 * 功能描述：
	 * 		获取正在播放的VOD节目的当前状态
	 * 
	 * 参数说明：
	 * 		instanceID 	- 播放句柄
	 * 
	 * 返回结果：
	 *		<Response code=”” message=””>
	 *		   <status>0 播放; 1 停止; 2 暂停</status>
	 *		</Response>
	 * */
	String getPlayStatus(String instanceID);

	/*
	 * 功能描述：
	 * 		获取正在播放的VOD节目的播放进度
	 * 
	 * 参数说明：
	 * 		instanceID 	- 播放句柄
	 * 
	 * 返回结果：
	 * 		<Response code=”” message=””>
	 * 		   <progress>播放时间点，格式：00:00:00</ progress >
	 * 		</Response>
	 * */
	String getPlayProgress(String instanceID);

}
