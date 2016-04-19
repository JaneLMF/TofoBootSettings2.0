package com.txbox.settings.bean;

import java.util.List;

/**
 * 
 * @类描述：获取家庭相册成员接口返回值data实体类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.bean
 * @类名称：AlbumMembersDataBean	
 * @创建人：Administrator
 * @创建时间：2014-9-18下午9:07:30	
 * @修改人：Administrator
 * @修改时间：2014-9-18下午9:07:30	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class AlbumMembersDataBean {
	List<AlbumMembers> members;

	public List<AlbumMembers> getMembers() {
		return members;
	}

	public void setMembers(List<AlbumMembers> members) {
		this.members = members;
	}

	
}
