package com.txbox.settings.launcher.systemsettings;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;





import com.txbox.txsdk.R;
import com.txbox.settings.bean.GuidBean;
import com.txbox.settings.bean.GuidDataBean;
import com.txbox.settings.bean.GuidInfoBean;
import com.txbox.settings.bean.LoginBean;
import com.txbox.settings.bean.NetTestItemBean;
import com.txbox.settings.bean.TvsKeyBean;
import com.txbox.settings.common.LocalInfor;
import com.txbox.settings.impl.AuthImpl;
import com.txbox.settings.impl.SyscfgImpl;
import com.txbox.settings.impl.SyscfgImpl.GetNetTestListener;
import com.txbox.settings.interfaces.IAuthImpl;
import com.txbox.settings.utils.ConfigManager;
import com.txbox.settings.utils.DecodeImgUtil;
import com.txbox.settings.utils.DeviceUtils;
import com.txbox.settings.utils.ServerManager;
import com.txbox.settings.utils.ValidUtils;
import com.txbox.settings.view.gifview.GifView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NetworkSetting_Detecting extends Activity{

	
	private ImageView local_network_icon,outside_network_icon,dns_network_icon,server_network_icon,tip_icon;
	private TextView server_network_text,dns_network_text,outside_network_text,local_network_text,tip_msg;
	private LinearLayout tip_ll;
	private static final int LOCALNETSUCCESS = 200;
	private static final int LOCALNETFAIL = 201;
	private static final int OUTSIDENETSUCCESS = 202;
	private static final int OUTSIDENETFAIL = 203;
	private static final int DNSSUCCESS = 204;
	private static final int DNSFAIL = 205;
	private static final int SEVERSUCCESS = 206;
	private static final int SEVERFAIL = 207;
	private static final int SHOWRESOUT = 208;
	private Context mContext;
	private String ip = "http://113.108.11.73/index.html";
	private String ip2 = "http://115.239.210.27";
	private String dns = "http://www.qq.com";
	private String message = "";
	
	private TextView localTime;
	 private LocalInfor mLocalInfor;
	 private static final int UPDATETIME = 1024;
	 private GifView left_gif;
	 private GifView right_gif;
	 private ImageView right_gif_finish,left_gif_finish;
	 private CheckThead myThead;
	 private Bitmap b;
	 private RelativeLayout main_rl;
	Handler h = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOCALNETSUCCESS:
				left_gif.setVisibility(View.GONE);
				left_gif_finish.setVisibility(View.VISIBLE);
				local_network_icon.setImageResource(R.drawable.lock_unlock);
				local_network_text.setText(mContext.getString(R.string.network_detect_local_network_success));
				CheckOutsideNet();
				break;
			case LOCALNETFAIL:
				left_gif.setVisibility(View.GONE);
				local_network_icon.setImageResource(R.drawable.red_wrong_icon);
				local_network_text.setText(mContext.getString(R.string.network_detect_local_network_fail));
				if(message.equals("")){
				 message = mContext.getString(R.string.network_detect_local_network_text);
				}
				CheckOutsideNet();
				break;
			case OUTSIDENETSUCCESS:
				outside_network_icon.setImageResource(R.drawable.lock_unlock);
				outside_network_text.setText(mContext.getString(R.string.network_detect_outside_network_success));
				CheckDNS();
				break;
			case OUTSIDENETFAIL:
				outside_network_icon.setImageResource(R.drawable.red_wrong_icon);
				outside_network_text.setText(mContext.getString(R.string.network_detect_outside_network_fail));
				if(message.equals("")){
					 message = mContext.getString(R.string.network_detect_outside_network_text);
					}
				CheckDNS();
				break;
			case DNSSUCCESS:
				dns_network_icon.setImageResource(R.drawable.lock_unlock);
				dns_network_text.setText(mContext.getString(R.string.network_detect_dns_network_success));
				CheckSever();
				break;
			case DNSFAIL:
				dns_network_icon.setImageResource(R.drawable.red_wrong_icon);
				dns_network_text.setText(mContext.getString(R.string.network_detect_dns_network_fail));
				if(message.equals("")){
					 message = mContext.getString(R.string.network_detect_dns_network_text);
					}
				CheckSever();
				break;
			case SEVERSUCCESS:
				server_network_icon.setImageResource(R.drawable.lock_unlock);
				server_network_text.setText(mContext.getString(R.string.network_detect_server_network_success));
				h.removeMessages(SHOWRESOUT);
				h.sendEmptyMessageDelayed(SHOWRESOUT,1000);
				break;
			case SEVERFAIL:
				server_network_icon.setImageResource(R.drawable.red_wrong_icon);
				server_network_text.setText(mContext.getString(R.string.network_detect_server_network_fail));
				if(message.equals("")){
					 message = mContext.getString(R.string.network_detect_server_network_text);
					}
				h.removeMessages(SHOWRESOUT);
				h.sendEmptyMessageDelayed(SHOWRESOUT,1000);
				break;
			case SHOWRESOUT:
				local_network_icon.setVisibility(View.GONE);
				local_network_text.setVisibility(View.GONE);
				outside_network_icon.setVisibility(View.GONE);
				outside_network_text.setVisibility(View.GONE);
				dns_network_icon.setVisibility(View.GONE);
				dns_network_text.setVisibility(View.GONE);
				server_network_icon.setVisibility(View.GONE);
				server_network_text.setVisibility(View.GONE);
				
				tip_ll.setVisibility(View.VISIBLE);
				//TODO:show two different results:success or fail
				if(message.equals("")){
				   right_gif.setVisibility(View.GONE);
				   right_gif_finish.setVisibility(View.VISIBLE);
					
					tip_icon.setImageResource(R.drawable.lock_unlock);
					tip_msg.setText(mContext.getString(R.string.network_detect_success));
				}else{
					right_gif.setVisibility(View.GONE);
					tip_icon.setImageResource(R.drawable.red_wrong_icon);
					tip_msg.setText(mContext.getString(R.string.network_detect_fail)+message);
				}
				break;
			case UPDATETIME:
				localTime.setText(mLocalInfor.getLocalTime());
				h.sendEmptyMessageDelayed(UPDATETIME, 5*1000);
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(arg0);
		setContentView(R.layout.network_detect_settings);
		main_rl = (RelativeLayout) findViewById(R.id.main_rl);
		b = new DecodeImgUtil(this, R.raw.settings_bg).getBitmap();
		BitmapDrawable bd= new BitmapDrawable(b);
		main_rl.setBackground(bd);
		mContext = this;
		mLocalInfor = new LocalInfor(mContext);
		initView();
		
		left_gif.setGifImage(R.drawable.detecting_icon);
		left_gif.setShowDimension(400, 40);	
		CheckStart();
	}
	
	private void CheckStart(){ //start checking
		message = "";
		if(checkLocalNet()){
			h.sendEmptyMessageDelayed(LOCALNETSUCCESS, 2000);
		}else{
			h.sendEmptyMessageDelayed(LOCALNETFAIL, 2000);
		}
	}
	
	class CheckThead extends Thread{
		private String type ;
		public CheckThead(String type){
			this.type = type;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(type.equals("outside")){
				if(isInternetConnected(ip)||isInternetConnected(ip2)){
					h.sendEmptyMessageDelayed(OUTSIDENETSUCCESS, 2000);
				}else{
					h.sendEmptyMessageDelayed(OUTSIDENETFAIL, 2000);
				}
			}else if(type.equals("dns")){
				if( isInternetConnected(dns)){
					h.sendEmptyMessageDelayed(DNSSUCCESS, 2000);
				}else{
					h.sendEmptyMessageDelayed(DNSFAIL, 2000);
				}
			}
			
			super.run();
		}
	}
	
	private void CheckOutsideNet(){ //check the Internet
		right_gif.setGifImage(R.drawable.detecting_icon);
		right_gif.setShowDimension(420, 40);
		myThead = new CheckThead("outside");
		myThead.start();

	}
	private void CheckDNS(){
		myThead = new CheckThead("dns");
		myThead.start();
	}
	private void CheckSever(){
		GetNetTest();
	}
	
	/**
	 * 
	 * @描述:判断当前是否连接网络（这里的网络指的是外网）
	 * @方法名: isInternetConnected
	 * @return
	 * @返回类型 boolean
	 * @创建人 gao
	 * @创建时间 2014年9月15日上午11:50:28
	 * @修改人 gao
	 * @修改时间 2014年9月15日上午11:50:28
	 * @修改备注
	 * @since
	 * @throws
	 */
	public static boolean isInternetConnected(String urlStr) {
		boolean result = false;
		try {
			// froyo之前的系统使用httpurlconnection存在bug
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
				System.setProperty("http.keepAlive", "false");
			}

			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);// 设置为允许输出
			conn.setConnectTimeout(3 * 1000);// 3S超时

			Map<String, List<String>> headerMap = conn.getHeaderFields();
			Iterator<String> iterator = headerMap.keySet().iterator();
			if(iterator==null){
				System.err.println("iterator==null");
				System.out.println("iterator==null");
			}
			while (iterator.hasNext()) {
				result = true;
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	private void initView(){
		local_network_icon = (ImageView) findViewById(R.id.local_network_icon);
		outside_network_icon = (ImageView) findViewById(R.id.outside_network_icon);
		dns_network_icon = (ImageView) findViewById(R.id.dns_network_icon);
		server_network_icon = (ImageView) findViewById(R.id.server_network_icon);
		tip_icon = (ImageView) findViewById(R.id.tip_icon);
		
		server_network_text = (TextView) findViewById(R.id.server_network_text);
		dns_network_text = (TextView) findViewById(R.id.dns_network_text);
		outside_network_text = (TextView) findViewById(R.id.outside_network_text);
		local_network_text = (TextView) findViewById(R.id.local_network_text);
		tip_msg = (TextView) findViewById(R.id.tip_msg);
		left_gif = (GifView) findViewById(R.id.left_gif);
		right_gif = (GifView) findViewById(R.id.right_gif);
		tip_ll = (LinearLayout) findViewById(R.id.tip_ll);
		localTime = (TextView) findViewById(R.id.localTime);
		left_gif_finish = (ImageView) findViewById(R.id.left_gif_finish);
		right_gif_finish =  (ImageView) findViewById(R.id.right_gif_finish);
		h.sendEmptyMessage(UPDATETIME);
	}

	private boolean checkLocalNet(){
		return ValidUtils.isNetworkExist(mContext);
	}
	/**
	 * 
	 * @描述:获取网络联通检查url
	 * @方法名: GetNetTest
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-17下午3:43:41	
	 * @修改人 Administrator
	 * @修改时间 2014-9-17下午3:43:41	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	private void GetNetTest(){
		final SyscfgImpl cfgImpl = new SyscfgImpl(mContext);
		cfgImpl.SetNetTestListener(new GetNetTestListener(){

			@Override
			public void onGetNetTestFinished(NetTestItemBean itembean) {
				// TODO Auto-generated method stub
				try {
					if(itembean!=null){
						System.out.println("dns = " + itembean.getDns());
						System.out.println("ip = " + itembean.getIp());
//						ip = itembean.getIp();
//						dns = itembean.getDns();
						h.sendEmptyMessageDelayed(SEVERSUCCESS, 2000);
					}else{
						h.sendEmptyMessageDelayed(SEVERFAIL, 2000);
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		final LoginBean logininfo = new LoginBean(); 
		AuthImpl authImpl = new AuthImpl(mContext,new IAuthImpl() {
			
			@Override
			public void onTvsKeyFinished(TvsKeyBean tvskeybean) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGuidFinished(GuidBean guidbean) {
				// TODO Auto-generated method stub
				if(guidbean!=null){
					GuidDataBean data = guidbean.getData();
					System.out.println("guid = " + data.getGuid() + "Guid_secret=" + data.getGuid_secret());
					GuidInfoBean info = new GuidInfoBean();
					info.setGuid(data.getGuid());
					info.setGuid_secret(data.getGuid_secret());
					info.setTvskey("");
					SaveGuidInfo(info);
					cfgImpl.GetNetTestInfo(ServerManager.SYSCFG_SERVER, ConfigManager.TV_TYPE, guidbean.getData().getGuid(), logininfo, "test_net","0");
				}
			}
		});
		GuidInfoBean guid = authImpl.GetGudiInfo(ConfigManager.GUID_INII_PATH);
		if(guid!=null && guid.getGuid().length()>0){
			cfgImpl.GetNetTestInfo(ServerManager.SYSCFG_SERVER, ConfigManager.TV_TYPE, guid.getGuid(), logininfo, "test_net","0");
		}
		
	}
	
	private void SaveGuidInfo(GuidInfoBean data){
		AuthImpl authImpl = new AuthImpl(mContext,null);
		authImpl.SaveGuidInfo(ConfigManager.GUID_INII_PATH, data);
	}
}
