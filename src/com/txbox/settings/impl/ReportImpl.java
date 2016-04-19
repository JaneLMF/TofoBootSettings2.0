package com.txbox.settings.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import com.txbox.settings.bean.CommonReturn;
import com.txbox.settings.common.NetworkManager;
import com.txbox.settings.interfaces.IReportImpl;
import com.txbox.settings.mbx.api.NetManager;
import com.txbox.settings.mbx.api.NetManager.NetworkDetailInfo;
import com.txbox.txsdk.R;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemProperties;


/**
 * 
 * @类描述：反馈相关实现类
 * @项目名称：go3cLauncher
 * @包名： com.go3c.impl
 * @类名称：ReportImpl
 * @创建人：gao
 * @创建时间：2014年6月13日下午3:56:16
 * @修改人：gao
 * @修改时间：2014年6月13日下午3:56:16
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright go3c
 * @mail 25550836@qq.com
 */
public class ReportImpl extends GsonApi {
	private Context context;
	private IReportImpl mlisterner;
	private CommonCrashReturnAsync commonReturnAsync = null ;
	private NetManager netMgr;

	public ReportImpl(Context context, IReportImpl listerner) {
		this.context = context;
		this.mlisterner = listerner;
		netMgr = new NetManager(context);
	}

	/**
	 * 
	 * @描述:添加crash到本地数据库
	 * @方法名: addCrash
	 * @param server
	 * @param userid
	 * @param crashDesc
	 * @返回类型 void
	 * @创建人 gao
	 * @创建时间 2014年6月30日下午5:18:40
	 * @修改人 gao
	 * @修改时间 2014年6月30日下午5:18:40
	 * @修改备注
	 * @since
	 * @throws
	 */
	public void addCrash(String server, String userid, String crashTime, int id, String crashDesc) {
		try {
			
			String os = "tencent.os";
			String model = "tencent.model";
			String version = "tencent.version";
			String hwconfig = "tencent.cpu";

			
			String rmac = SystemProperties.get("persist.sys.macaddr", "");
			NetworkDetailInfo netInfor = netMgr.getNetworkDetailInfo();
			
			if(netInfor.mac!=null){
				 rmac = netInfor.mac;
			}
			String cid = context.getString(R.string.app_name);
			String clientid = "S812";

			StringBuilder url = new StringBuilder(server + "/api.php");

			String[] params = { url.toString(), "crash", userid, "STB", "Android " + os, model, hwconfig, version, crashTime, String.valueOf(id), crashDesc, cid, rmac, clientid };
			if(commonReturnAsync == null){
				commonReturnAsync = new CommonCrashReturnAsync();
				commonReturnAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @类描述：通用处理类
	 * @项目名称：go3cLauncher
	 * @包名： com.go3c.impl
	 * @类名称：CommonReturnAsync
	 * @创建人：gao
	 * @创建时间：2014年7月3日上午11:49:03
	 * @修改人：gao
	 * @修改时间：2014年7月3日上午11:49:03
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright go3c
	 * @mail 25550836@qq.com
	 */
	class CommonReturnAsync extends AsyncTask<String, Integer, CommonReturn> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected CommonReturn doInBackground(String... params) {
			String url = params[0];
			url = url.replaceAll(" ", "%20");
			String json = getJsonByGet(url,null);
			if (json.equals(ERROR))
				return null;
			try {
				CommonReturn commonReturn = getGson().fromJson(json, CommonReturn.class);
				return commonReturn;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(CommonReturn result) {
			if (mlisterner != null)
				mlisterner.onCommonResultFinished(result);
		}
	}

	/**
	 * 
	 * @类描述：Crash处理类
	 * @项目名称：go3cLauncher
	 * @包名： com.go3c.impl
	 * @类名称：CommonCrashReturnAsync
	 * @创建人：gao
	 * @创建时间：2014年7月3日上午11:48:48
	 * @修改人：gao
	 * @修改时间：2014年7月3日上午11:48:48
	 * @修改备注：
	 * @version v1.0
	 * @see [nothing]
	 * @bug [nothing]
	 * @Copyright go3c
	 * @mail 25550836@qq.com
	 */
	class CommonCrashReturnAsync extends AsyncTask<String, Integer, CommonReturn> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected CommonReturn doInBackground(String... params) {
			String url = params[0];

			String[] objName = { "m", "userid", "term", "os", "hwmodel", "hwconfig", "version", "crashtime", "id", "description", "cid", "rmac", "clientid" };
			Object[] obj = Arrays.copyOfRange(params, 1, 14);
			if(obj!=null){
				String json = getJsonByPost(url, objName, obj,null);
				if (json.equals(ERROR))
					return null;
				try {
					CommonReturn commonReturn = getGson().fromJson(json, CommonReturn.class);
					return commonReturn;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(CommonReturn result) {
			if (mlisterner != null)
				mlisterner.onCommonResultFinished(result);
				commonReturnAsync = null;
		}
	}

	/**
	 * 
	 * @描述:将字符串转为时间戳
	 * @方法名: getTime
	 * @param user_time
	 * @return
	 * @返回类型 String
	 * @创建人 gao
	 * @创建时间 2014年7月3日上午11:49:47
	 * @修改人 gao
	 * @修改时间 2014年7月3日上午11:49:47
	 * @修改备注
	 * @since
	 * @throws
	 */
	private String getTime(String user_time) {
		long l = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d = sdf.parse(user_time);
			l = d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return String.valueOf(l);
	}
}
