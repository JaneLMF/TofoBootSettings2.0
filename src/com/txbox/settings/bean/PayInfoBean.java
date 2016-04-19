package com.txbox.settings.bean;
/**
 * 
 * @类描述：获取单片付费情况实体类
 * @项目名称：TXBootSettings
 * @包名： com.txbox.settings.bean
 * @类名称：PayInfoBean	
 * @创建人：Administrator
 * @创建时间：2014-10-11下午5:36:46	
 * @修改人：Administrator
 * @修改时间：2014-10-11下午5:36:46	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class PayInfoBean {
	String cid;//影片专辑id。
	String openid;//QQ号对应的openId
	int pay; //用户是否购买影片。0为未购买；1为已经支付；2表示用户是会员，并且片子是会员免费的，用户可以直接观看;
	         //3 表示影片是用户通过好友赠送获得
	int ret; //接口返回值，成功返回为0，否则失败
	long start;//用户可免费观看影片的开始时间（UTC秒）
	long end;//用户可免费观看影片的结束时间（UTC秒）
	String sign;//签名验证信息
	String msg;//接口返回失败信息，成功返回“ok”
	int is_lost;
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public int getPay() {
		return pay;
	}
	public void setPay(int pay) {
		this.pay = pay;
	}
	public int getRet() {
		return ret;
	}
	public void setRet(int ret) {
		this.ret = ret;
	}
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public long getEnd() {
		return end;
	}
	public void setEnd(long end) {
		this.end = end;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getIs_lost() {
		return is_lost;
	}
	public void setIs_lost(int is_lost) {
		this.is_lost = is_lost;
	}
}
