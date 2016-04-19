package com.txbox.settings.interfaces;

import com.txbox.settings.bean.NetWorkSpeedInfo;

/**
 * 
 * @类描述：网络测速回调接口
 * @项目名称：TXBootSettings
 * @包名： com.example.txbootsettings.interfaces
 * @类名称：INetworkSpeedImpl	
 * @创建人：Administrator
 * @创建时间：2014-9-10下午9:41:51	
 * @修改人：Administrator
 * @修改时间：2014-9-10下午9:41:51	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public interface INetworkSpeedImpl {
	public void onChange(NetWorkSpeedInfo speedinfo);
}
