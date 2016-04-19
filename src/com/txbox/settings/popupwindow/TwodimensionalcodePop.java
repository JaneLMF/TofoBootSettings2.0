package com.txbox.settings.popupwindow;

import java.util.List;

import com.txbox.settings.bean.AlbumMembers;
import com.txbox.settings.bean.AlbumMembersBean;
import com.txbox.settings.bean.GuidBean;
import com.txbox.settings.bean.GuidDataBean;
import com.txbox.settings.bean.GuidInfoBean;
import com.txbox.settings.bean.QrcodeBean;
import com.txbox.settings.bean.ResultBean;
import com.txbox.settings.bean.TicketClientBean;
import com.txbox.settings.bean.TicketTvskeyBean;
import com.txbox.settings.bean.TvsKeyBean;
import com.txbox.settings.bean.TvsKeyDataBean;
import com.txbox.settings.impl.AlbumImpl;
import com.txbox.settings.impl.AuthImpl;
import com.txbox.settings.impl.TicketImpl;
import com.txbox.settings.interfaces.IAlbumImpl;
import com.txbox.settings.interfaces.IAuthImpl;
import com.txbox.settings.interfaces.ITicketImpl;
import com.txbox.settings.utils.ConfigManager;
import com.txbox.settings.utils.DensityUtil;
import com.txbox.settings.utils.DeviceUtils;
import com.txbox.settings.utils.ImageDownloader2;
import com.txbox.settings.utils.ServerManager;
import com.txbox.txsdk.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class TwodimensionalcodePop {
	
	private Context mContext;
	private PopupWindow TwoDimensionalCodePop;
	private View Container = null;
	private ImageView twodimenImg;
	private ImageDownloader2 down;
	private String qrcode;
	private String type;
	private TextView tip;
	public TwodimensionalcodePop(Context mContext,String qrcode,String type){
		this.mContext = mContext;
		down = new ImageDownloader2(mContext);
		this.qrcode = qrcode;
		this.type = type;
	}
	public void showListPop(){
		if(TwoDimensionalCodePop==null){
			TwoDimensionalCodePop = initTwoDimensionalCodePop();
		}
		if(TwoDimensionalCodePop!=null &&TwoDimensionalCodePop.isShowing()){
			TwoDimensionalCodePop.dismiss();
		}else{
			TwoDimensionalCodePop.showAtLocation(Container, Gravity.CENTER, 0, 30);
		}
	}
	
	private PopupWindow initTwoDimensionalCodePop(){
		PopupWindow Pop = null;
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		Container = layoutInflater.inflate(R.layout.two_dimension_code_pop, null);
		twodimenImg = (ImageView) Container.findViewById(R.id.twodimenImg);
		tip =  (TextView) Container.findViewById(R.id.tip);
		if(type.equals("photo")){
			tip.setText(mContext.getString(R.string.useWebChatLogin_text));
		}else if(type.equals("feedback")){
			tip.setText(mContext.getString(R.string.useWebChatFeedback_text));
		}
		down.download(qrcode, twodimenImg);
		Pop = new PopupWindow(Container, DensityUtil.dip2px(mContext, 400),DensityUtil.dip2px(mContext, 430), false);
		Pop.setBackgroundDrawable(new BitmapDrawable());//
		Pop.setOutsideTouchable(true);
		Pop.setFocusable(true); 
		return Pop;
	}
	public PopupWindow getPop(){
		if(TwoDimensionalCodePop==null){
			return TwoDimensionalCodePop;
		}
		return TwoDimensionalCodePop;
	}
	
}
