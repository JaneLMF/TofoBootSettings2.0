package com.txbox.settings.interfaces;

import com.txbox.settings.bean.CommonReturn;


/**
 * 
 * @类描述：反馈相关回调接口
 * @项目名称：go3cLauncher
 * @包名： com.go3c.interfaces
 * @类名称：IReportImpl	
 * @创建人：gao
 * @创建时间：2014年6月13日下午3:55:49	
 * @修改人：gao
 * @修改时间：2014年6月13日下午3:55:49	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright go3c
 * @mail 25550836@qq.com
 */
public interface IReportImpl {
	/**
	 * 
	 * @描述:通用返回回调
	 * @方法名: onCommonResultFinished
	 * @param commonReturn
	 * @返回类型 void
	 * @创建人 gao
	 * @创建时间 2014年6月13日下午3:55:57	
	 * @修改人 gao
	 * @修改时间 2014年6月13日下午3:55:57	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public void onCommonResultFinished(CommonReturn commonReturn);
}
