package com.txbox.settings.interfaces;

import com.txbox.settings.bean.UpgradeBean;

/**
 * 
 * @类描述：获取rom升级信息接口回调接口类
 * @项目名称：TXBootSettings
 * @包名： com.example.txbootsettings.interfaces
 * @类名称：IRomUpgradeImpl	
 * @创建人：Administrator
 * @创建时间：2014-9-9下午5:38:11	
 * @修改人：Administrator
 * @修改时间：2014-9-9下午5:38:11	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public interface IRomUpgradeImpl {
	/**
	 * 
	 * @描述:获取rom升级信息接口回调接口
	 * @方法名: onGetRomUpgradeFinished
	 * @param upgradetbean
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-9下午5:38:55	
	 * @修改人 Administrator
	 * @修改时间 2014-9-9下午5:38:55	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public void onGetRomUpgradeFinished(UpgradeBean upgradetbean);
}
