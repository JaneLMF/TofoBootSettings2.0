package com.txbox.settings.interfaces;

import com.txbox.settings.bean.AlbumMembersBean;
import com.txbox.settings.bean.GuidBean;
import com.txbox.settings.bean.QrcodeBean;

/**
 * 
 * @类描述：获取相册相关数据回调
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.interfaces
 * @类名称：IAlbumImpl	
 * @创建人：Administrator
 * @创建时间：2014-9-18下午8:37:25	
 * @修改人：Administrator
 * @修改时间：2014-9-18下午8:37:25	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public interface IAlbumImpl {
	public void onGetMembersFinished(AlbumMembersBean membersbean);
	public void onGetQrcodeFinished(QrcodeBean qrcodebean);
}
