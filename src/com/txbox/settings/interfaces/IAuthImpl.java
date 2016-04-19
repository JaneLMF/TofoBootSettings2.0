package com.txbox.settings.interfaces;

import com.txbox.settings.bean.GuidBean;
import com.txbox.settings.bean.TvsKeyBean;
/**
 * 
 * @类描述：鉴权票据相关回调接口
 * @项目名称：TXBootSettings
 * @包名： com.example.txbootsettings.interfaces
 * @类名称：IAuthImpl	
 * @创建人：Administrator
 * @创建时间：2014-9-4下午7:55:26	
 * @修改人：Administrator
 * @修改时间：2014-9-4下午7:55:26	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public interface IAuthImpl {
	public void onGuidFinished(GuidBean guidbean);
	public void onTvsKeyFinished(TvsKeyBean tvskeybean);
}
