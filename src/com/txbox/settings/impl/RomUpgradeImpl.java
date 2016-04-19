package com.txbox.settings.impl;

import com.txbox.settings.bean.FirmwareMd5Bean;
import com.txbox.settings.bean.UpgradeBean;
import com.txbox.settings.bean.UserBean;
import com.txbox.settings.impl.SyscfgImpl.GetSpeedTestAsync;
import com.txbox.settings.impl.UserImpl.IVipInfoImpl;
import com.txbox.settings.report.GlobalInfo;
import com.txbox.settings.utils.ConfigManager;
import android.content.Context;
import android.os.AsyncTask;
import com.txbox.settings.utils.DeviceUtils;


/**
 * 
 * @类描述：获取rom更新信息接口实现类
 * @项目名称：TXBootSettings
 * @包名： com.example.txbootsettings.impl
 * @类名称：RomUpgradeImpl	
 * @创建人：Administrator
 * @创建时间：2014-9-9下午5:34:11	
 * @修改人：Administrator
 * @修改时间：2014-9-9下午5:34:11	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright pivos
 * @mail 939757170@qq.com
 */
public class RomUpgradeImpl extends GsonApi{
	private Context context;
	private IRomUpgradeImpl mlisterner;
	private IGetMd5Impl mGetMd5listerner;

	public RomUpgradeImpl(Context context) {
		this.context = context;
	}
	public interface IRomUpgradeImpl {
		/**
		 * 
		 * @描述:获取rom升级信息接口回调接口
		 * @方法名: onGetRomUpgradeFinished
		 * @param upgradetbean
		 * @返回类型 void
		 * @创建人 Administrator
		 * @创建时间 2014-9-9下午5:38:55	
		 * @修改人 Administrator
		 * @修改时间 2014-9-9下午5:38:55	
		 * @修改备注 
		 * @since
		 * @throws
		 */
		public void onGetRomUpgradeFinished(UpgradeBean upgradetbean);
	}
	public interface IGetMd5Impl {
		public void onGetMd5Finished(FirmwareMd5Bean md5bean);

	}
	public void SetGetmd5Listener(IGetMd5Impl listener){
		mGetMd5listerner = listener;
	}
	public void SetRomUpgradeImplListener(IRomUpgradeImpl listener){
		mlisterner = listener;
	}
	/**
	 * 
	 * @描述:获取rom更新信息接口
	 * @方法名: GetRomUpgradeInfo
	 * @param server
	 * @param version
	 * @param platform
	 * @param md5
	 * @param guid
	 * @param wifimac
	 * @返回类型 void
	 * @创建人 Administrator
	 * @创建时间 2014-9-10上午9:47:18	
	 * @修改人 Administrator
	 * @修改时间 2014-9-10上午9:47:18	
	 * @修改备注 
	 * @since
	 * @throws
	 */
	public void GetRomUpgradeInfo(String server,String version,String platform,String rom_version,String guid,String wifimac){
		String qua = GlobalInfo.getQua("UPGRADER");
		String hard_info = GlobalInfo.getHardInfo();
		String url = "/get_upgrade_info?version="+version+"&guid="+guid+"&mac="+wifimac+"&format=json"+"&hard_info="+hard_info+"&Q-UA="+qua;
		GetRomUpgradeInfoAsync romupgradeAsync = new GetRomUpgradeInfoAsync();
		romupgradeAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,server,url);
	}

	public void GetUpgradeInfo(String server,String rom_version,String guid,String rmac){
		String pt = DeviceUtils.getPT();
		String chid = DeviceUtils.getChid();
		String hwver = DeviceUtils.getHwVer();
		String url = "?m=Upgrade.getUpgradeInfo"+"&version="+rom_version+"&guid="+guid+"&mac="+rmac+"&PT="+pt+"&CHID="+chid+"&hw_ver="+hwver;
		GetRomUpgradeInfoAsync romupgradeAsync = new GetRomUpgradeInfoAsync();
		romupgradeAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,server,url);
	}
	public void GetFirmwareMd5(String server,String version){
		String url = "?m=tgetmd5&versioncode="+version;
		GetFirmwareMd5Async md5Async = new GetFirmwareMd5Async();
		md5Async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,server,url);
	}
	
	/**
	 * 
	 * @类描述：异步获取rom更新信息类
	 * @项目名称：TXBootSettings
	 * @包名： com.example.txbootsettings.impl
	 * @类名称：GetRomUpgradeInfoAsync	
	 * @创建人：Administrator
	 * @创建时间：2014-9-10上午9:47:48	
	 * @修改人：Administrator
	 * @修改时间：2014-9-10上午9:47:48	
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright pivos
	 * @mail 939757170@qq.com
	 */
	class GetRomUpgradeInfoAsync extends AsyncTask<String, Integer, UpgradeBean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected UpgradeBean doInBackground(String... params) {
			String server = params[0];
			String url = params[1];
			String json = getJsonByGet(server+url,null);
			if (json.equals(ERROR))
				return null;
			try{
				UpgradeBean commonReturn = getGson().fromJson(json, UpgradeBean.class);
				return commonReturn;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(UpgradeBean result) {
			if (mlisterner != null)
				mlisterner.onGetRomUpgradeFinished(result);
		}
	}
	
	class GetFirmwareMd5Async extends AsyncTask<String, Integer, FirmwareMd5Bean> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected FirmwareMd5Bean doInBackground(String... params) {
			String server = params[0];
			String url = params[1];
			String json = getJsonByGet(server+url,null);
			if (json.equals(ERROR))
				return null;
			try{
				FirmwareMd5Bean commonReturn = getGson().fromJson(json, FirmwareMd5Bean.class);
				return commonReturn;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(FirmwareMd5Bean result) {
			if (mGetMd5listerner != null)
				mGetMd5listerner.onGetMd5Finished(result);
		}
	}
}
