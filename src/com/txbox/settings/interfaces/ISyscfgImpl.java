package com.txbox.settings.interfaces;

import com.txbox.settings.bean.MusicBackPicItemBean;
import com.txbox.settings.bean.NetTestItemBean;
import com.txbox.settings.bean.NtpServerItemBean;
import com.txbox.settings.bean.SetCfgResultBean;
import com.txbox.settings.bean.SpeedTestItemBean;
import com.txbox.settings.bean.WeatherInterfaceItemBean;


/**
 * 
 * @类描述：配置系统接口回调接口
 * @项目名称：TXBootSettings
 * @包名： com.example.txbootsettings.interfaces
 * @类名称：ISyscfgImpl	
 * @创建人：Administrator
 * @创建时间：2014-9-9下午2:12:42	
 * @修改人：Administrator
 * @修改时间：2014-9-9下午2:12:42	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public interface ISyscfgImpl {
	/**
	 * 
	 * @描述:设置配置信息返回回调接口
	 * @方法名: onSetCfgFinished
	 * @param resultbean
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-9下午2:17:46	
	 * @修改人 Administrator
	 * @修改时间 2014-9-9下午2:17:46	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public void onSetCfgFinished(SetCfgResultBean resultbean);
	public void onGetSpeedTestFinished(SpeedTestItemBean speedtestbean);
	public void onGetMusicBackFinished(MusicBackPicItemBean muaicbackpicbean);
	public void onGetNetTestFinished(NetTestItemBean itembean);
	public void onGetNtpServerFinished(NtpServerItemBean itembean);
	public void onGetWeatherInterfaceFinished(WeatherInterfaceItemBean itembean);
}
