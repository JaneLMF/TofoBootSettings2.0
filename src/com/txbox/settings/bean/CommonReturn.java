package com.txbox.settings.bean;

import org.apache.http.util.VersionInfo;

/**
 * 
 * @类描述：接口通用返回实体
 * @项目名称：go3cLauncher
 * @包名： com.go3c.bean
 * @类名称：CommonReturn
 * @创建人：gao
 * @创建时间：2014-4-28下午4:11:42
 * @修改人：gao
 * @修改时间：2014-4-28下午4:11:42
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @mail 25550836@qq.com
 */
public class CommonReturn {
	private String code;// 接口通用返回code
	private String description;// 接口通用返回des
	private String id;
	private String path;
	
	private VersionInfo data;

	
	public VersionInfo getData() {
		return data;
	}

	public void setData(VersionInfo data) {
		this.data = data;
	}


	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
