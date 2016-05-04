package com.tvxmpp.util;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.view.WindowManager;

import com.txbox.txsdk.R;

/**
 * UncaughtException澶勭悊绫�,褰撶▼搴忓彂鐢烾ncaught寮傚父鐨勬椂鍊�,鐢辫绫绘潵鎺ョ绋嬪簭,骞惰褰曞彂閫侀敊璇姤鍛�.
 * 
 * @author way
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
	private Thread.UncaughtExceptionHandler mDefaultHandler;// 绯荤粺榛樿鐨刄ncaughtException澶勭悊绫�
	private static CrashHandler INSTANCE;// CrashHandler瀹炰緥
	private Context mContext;// 绋嬪簭鐨凜ontext瀵硅薄

	/** 淇濊瘉鍙湁涓�涓狢rashHandler瀹炰緥 */
	private CrashHandler() {

	}

	/** 鑾峰彇CrashHandler瀹炰緥 ,鍗曚緥妯″紡 */
	public static CrashHandler getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CrashHandler();
		return INSTANCE;
	}

	/**
	 * 鍒濆鍖�
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;

		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();// 鑾峰彇绯荤粺榛樿鐨刄ncaughtException澶勭悊鍣�
		Thread.setDefaultUncaughtExceptionHandler(this);// 璁剧疆璇rashHandler涓虹▼搴忕殑榛樿澶勭悊鍣�
	}

	/**
	 * 褰揢ncaughtException鍙戠敓鏃朵細杞叆璇ラ噸鍐欑殑鏂规硶鏉ュ鐞�
	 */
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 濡傛灉鑷畾涔夌殑娌℃湁澶勭悊鍒欒绯荤粺榛樿鐨勫紓甯稿鐞嗗櫒鏉ュ鐞�
			mDefaultHandler.uncaughtException(thread, ex);
		}
	}

	/**
	 * 鑷畾涔夐敊璇鐞�,鏀堕泦閿欒淇℃伅 鍙戦�侀敊璇姤鍛婄瓑鎿嶄綔鍧囧湪姝ゅ畬鎴�.
	 * 
	 * @param ex
	 *            寮傚父淇℃伅
	 * @return true 濡傛灉澶勭悊浜嗚寮傚父淇℃伅;鍚﹀垯杩斿洖false.
	 */
	public boolean handleException(Throwable ex) {
		if (ex == null || mContext == null)
			return false;
		final String crashReport = getCrashReport(mContext, ex);
		new Thread() {
			public void run() {
				Looper.prepare();
				File file = save2File(crashReport);
				sendAppCrashReport(mContext, crashReport, file);
				Looper.loop();
			}

		}.start();
		return true;
	}

	private File save2File(String crashReport) {
		String fileName = "crash-" + System.currentTimeMillis() + ".txt";
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			try {
				File dir = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + File.separator + "crash");
				if (!dir.exists())
					dir.mkdir();
				File file = new File(dir, fileName);
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(crashReport.toString().getBytes());
				fos.close();
				return file;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private void sendAppCrashReport(final Context context,
			final String crashReport, final File file) {
		// TODO Auto-generated method stub
		AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("app_error");
		builder.setMessage("app_error_message");
		builder.setPositiveButton("submit_report",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						try {

							Intent intent = new Intent(Intent.ACTION_SEND);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							String[] tos = { "way.ping.li@gmail.com" };
							intent.putExtra(Intent.EXTRA_EMAIL, tos);

							intent.putExtra(Intent.EXTRA_SUBJECT,
									"XMPP Client - 閿欒鎶ュ憡");
							if (file != null) {
								intent.putExtra(Intent.EXTRA_STREAM,
										Uri.fromFile(file));
								intent.putExtra(Intent.EXTRA_TEXT,
										"璇峰皢姝ら敊璇姤鍛婂彂閫佺粰鎴戯紝浠ヤ究鎴戝敖蹇慨澶嶆闂锛岃阿璋㈠悎浣滐紒\n");
							} else {
								intent.putExtra(Intent.EXTRA_TEXT,
										"璇峰皢姝ら敊璇姤鍛婂彂閫佺粰鎴戯紝浠ヤ究鎴戝敖蹇慨澶嶆闂锛岃阿璋㈠悎浣滐紒\n"
												+ crashReport);
							}
							intent.setType("text/plain");
							intent.setType("message/rfc882");
							Intent.createChooser(intent, "Choose Email Client");
							context.startActivity(intent);
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							dialog.dismiss();
							// 閫�鍑�
							android.os.Process.killProcess(android.os.Process
									.myPid());
							System.exit(1);
						}
					}
				});
		builder.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 閫�鍑�
						android.os.Process.killProcess(android.os.Process
								.myPid());
						System.exit(1);
					}
				});
		dialog = builder.create();
		dialog.getWindow()
				.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
	}

	/**
	 * 鑾峰彇APP宕╂簝寮傚父鎶ュ憡
	 * 
	 * @param ex
	 * @return
	 */
	private String getCrashReport(Context context, Throwable ex) {
		PackageInfo pinfo = getPackageInfo(context);
		StringBuffer exceptionStr = new StringBuffer();
		exceptionStr.append("Version: " + pinfo.versionName + "("
				+ pinfo.versionCode + ")\n");
		exceptionStr.append("Android: " + android.os.Build.VERSION.RELEASE
				+ "(" + android.os.Build.MODEL + ")\n");
		exceptionStr.append("Exception: " + ex.getMessage() + "\n");
		StackTraceElement[] elements = ex.getStackTrace();
		for (int i = 0; i < elements.length; i++) {
			exceptionStr.append(elements[i].toString() + "\n");
		}
		return exceptionStr.toString();
	}

	/**
	 * 鑾峰彇App瀹夎鍖呬俊鎭�
	 * 
	 * @return
	 */
	private PackageInfo getPackageInfo(Context context) {
		PackageInfo info = null;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			 e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

}
