package com.txbox.settings.interfaces;

import com.txbox.settings.bean.TicketClientBean;
import com.txbox.settings.bean.TicketTvskeyBean;

/**
 * 
 * @类描述：通过本地http加密、解密票据数据测试回调接口
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.interfaces
 * @类名称：ITicketImpl	
 * @创建人：Administrator
 * @创建时间：2014-9-17下午10:48:22	
 * @修改人：Administrator
 * @修改时间：2014-9-17下午10:48:22	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public interface ITicketImpl {
	public void onTicketClientFinished(TicketClientBean clientbean);
	public void onTvSkeyFinished(TicketTvskeyBean tvskeybean);
}
