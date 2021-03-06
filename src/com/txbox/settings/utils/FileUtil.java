package com.txbox.settings.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Comparator;

import com.txbox.txsdk.R;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.widget.ImageView;


public class FileUtil {

	public static final String APP_DIR = "/povisFace/";
	public static final String SCREENCAST_DIR = "screencast/";
	public static final String IMG_DIR = "img";
	public static final String APK_NAME = "ZDStream.apk";
	public static final String PUSH_DIR = "PushImg";
	public static final String CITY_XML_DIR = "cityList";
	
	private static final double MB = 1024 * 1024;
	private static final double mCacheSize = 40 * MB;
	private static final double mFreeSize = 10 * MB;
	private Context mCtx;
	private String PATH_LOGCAT;
	public String mAppPath;
	public String mImgPath;
	public String mAPKPath;
	public String mScreenCastPath;
	public String mPushPath;
	public String mCityList;

	private static FileUtil instance;

	public static FileUtil getInstance(Context ctx) {
		if (instance == null) {
			instance = new FileUtil(ctx);
		}
		return instance;
	}

	public static void releaseInstance() {
		instance = null;
	}

	private FileUtil(Context ctx) {
		super();
		this.mCtx = ctx;
		initPath();
	}

	private boolean hasSDCard() {
		boolean r = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		// Logs.PRINT("has sdcard = " + r);
		String path = Environment.getExternalStorageDirectory().getPath();
		File file = null;
		file = new File(path);
		if (file.canWrite()) {
			return true;
		} else {
			return false;
		}
		// return r;
	}

	private void initPath() {
		System.out.println("FileUtil init PATH !!!!!");
		if (hasSDCard()) {
			System.out.println("FileUtil init PATH  hasSDCard !!!!!");
			mAppPath = Environment.getExternalStorageDirectory().getPath()
					+ APP_DIR;
			mImgPath = mAppPath + IMG_DIR;
			mAPKPath = mAppPath + APK_NAME;
			mScreenCastPath = mAppPath + SCREENCAST_DIR;
			mPushPath = mAppPath + PUSH_DIR;
			mCityList = mAppPath + CITY_XML_DIR;
			File file = null;
			file = new File(mAppPath);

			if (!file.exists())
				file.mkdirs();
			else if (file.isFile()) {
				file.delete();
				file.mkdirs();
			}
			file = new File(mImgPath);
			if (!file.exists())
				file.mkdirs();
			else if (file.isFile()) {
				file.delete();
				file.mkdirs();
			}
			file = new File(mPushPath);
			if (!file.exists())
				file.mkdirs();
			else if (file.isFile()) {
				file.delete();
				file.mkdirs();
			}
			file = new File(mCityList);
			if (!file.exists())
				file.mkdirs();
			else if (file.isFile()) {
				file.delete();
				file.mkdirs();
			}
		} else {
			System.out.println("FileUtil init PATH  no SDCard !!!!!");
			mAppPath = null;
			mImgPath = null;
			mAPKPath = APK_NAME;
		}
	}


	private File getParentDir() {
		if (mAppPath != null)
			return new File(mAppPath);
		else
			return new File(mCtx.getFilesDir().getAbsolutePath());
	}

	private int getFreeSpace() {
		StatFs stat = new StatFs(getParentDir().getAbsolutePath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
				.getBlockSize());
		return (int) sdFreeMB;
	}

	private void updateLastModifiedTime(File file) {
		if (file == null)
			return;
		long newModifiedTime = System.currentTimeMillis();
		if (file.exists()) {
			file.setLastModified(newModifiedTime);
		}
	}

	private void removeCache() {
		File dir = getParentDir();
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			return;
		}
		int dirSize = 0;
		for (int i = 0; i < files.length; i++) {
			dirSize += files[i].length();
		}
		if (dirSize > mCacheSize || mFreeSize > getFreeSpace()) {
			int removeFactor = (int) ((0.4 * files.length) + 1);
			Arrays.sort(files, new FileLastModifSort());
			for (int i = 0; i < removeFactor; i++) {
				files[i].delete();
			}
		}
	}

	// --------------------------------------------------------------------------sdcard
	private void removeSDCard(String fileName) {
		if (mImgPath == null)
			return;
		if (fileName == null)
			return;
		File file = new File(mImgPath, fileName);
		if (!file.exists())
			return;
		file.delete();
	}
	private boolean saveUrlSDCard2(String u, String fileName) {
		if (mPushPath == null)
			return false;
		if (u == null || fileName == null)
			return false;
		removeCache();
		File file = new File(mPushPath, fileName);
		if (file.exists())
			return true;
		try {
			file.createNewFile();
			URL url = new URL(u);
			URLConnection con = url.openConnection();
			InputStream is = con.getInputStream();
			byte[] bs = new byte[1024];
			int len;
			int count = 0;
			int total = con.getContentLength();
			OutputStream os = new FileOutputStream(file);
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
				count += len;
//				if (pl != null) {
//					if (total > 0) {
//						pl.onProgress(count * 100 / total);
//					}
//				}
				// Logs.PRINT("saveUrlSDCard----------------");
			}
			os.close();
			is.close();
//			if (pl != null) {
//				pl.onProgress(-1);
//			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
//		if (pl != null) {
//			pl.onProgress(-2);
//		}
		return false;
	}
	private boolean saveUrlSDCard(String u, String fileName, ProgressListener pl) {
		if (mImgPath == null)
			return false;
		if (u == null || fileName == null)
			return false;
		removeCache();
		File file = new File(mImgPath, fileName);
		if (file.exists())
			return true;
		try {
			file.createNewFile();
			URL url = new URL(u);
			URLConnection con = url.openConnection();
			InputStream is = con.getInputStream();
			byte[] bs = new byte[1024];
			int len;
			int count = 0;
			int total = con.getContentLength();
			OutputStream os = new FileOutputStream(file);
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
				count += len;
				
				// Logs.PRINT("saveUrlSDCard----------------");
			}
			os.close();
			is.close();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	private void saveBmpSDCard(Bitmap bm, String fileName) {
		if (mImgPath == null)
			return;
		if (bm == null)
			return;
		removeCache();
		File file = new File(mImgPath, fileName);
		if (file.exists())
			return;
		try {
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private File getFileSDCard(String fileName) {
		if (mImgPath == null)
			return null;
		File file = new File(mImgPath, fileName);
		return file;
	}

	public Bitmap getBitmapByPath(String path, String fileName, int dstwidth,
			int dstheight) {
		if (path == null)
			return null;
		File file = new File(path, fileName);
		if (!file.exists())
			return null;
		float scale_tmp = 0;
		int scale = 0;
		try {

			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(file), null, o);
			int srcwidth = o.outWidth, srcheight = o.outHeight;

			if (dstwidth > 0 && dstheight > 0) {
				float scale_width = 0, scale_height = 0;
				if (srcwidth > dstwidth) {
					scale_width = srcwidth / dstwidth;
				}
				if (srcheight > dstheight) {
					scale_height = srcheight / dstheight;
				}
				scale_tmp = scale_width >= scale_height ? scale_width
						: scale_height;
				Log.i("decodeSDcard", "scale_tmp = " + scale_tmp
						+ "scale_width=" + scale_width + "scale_height="
						+ scale_height);
				if (scale_tmp == 0) {
					scale = 0;
				} else if (scale_tmp < 2) {
					scale = 2;
				} else if (scale_tmp < 4) {
					scale = 4;
				} else if (scale_tmp < 8) {
					scale = 8;
				} else {
					scale = 16;
				}

			}
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			Bitmap bm = BitmapFactory.decodeStream(new FileInputStream(file),
					null, o2);
			if (bm == null) {
				file.delete();
				return null;
			} else {
				updateLastModifiedTime(file);
				return bm;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Bitmap getBitmapSDCard(String fileName) {
		if (mImgPath == null)
			return null;
		File file = new File(mImgPath, fileName);
		if (!file.exists())
			return null;
		try {
			Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
			if (bm == null) {
				file.delete();
				return null;
			} else {
				updateLastModifiedTime(file);
				return bm;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Bitmap getBitmapSDCard(String fileName, int dstwidth, int dstheight) {
		if (mImgPath == null)
			return null;
		File file = new File(mImgPath, fileName);
		if (!file.exists())
			return null;
		float scale_tmp = 0;
		int scale = 0;
		try {
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(file), null, o);
			int srcwidth = o.outWidth, srcheight = o.outHeight;
			if (dstwidth > 0 && dstheight > 0) {
				float scale_width = 0, scale_height = 0;
				if (srcwidth > dstwidth) {
					scale_width = srcwidth / dstwidth;
				}
				if (srcheight > dstheight) {
					scale_height = srcheight / dstheight;
				}
				scale_tmp = scale_width >= scale_height ? scale_width
						: scale_height;

				if (scale_tmp == 0) {
					scale = 0;
				} else if (scale_tmp <= 2) {
					scale = 2;
				} else if (scale_tmp <= 4) {
					scale = 4;
				} else if (scale_tmp <= 8) {
					scale = 8;
				} else {
					scale = 16;
				}

				Log.d("decodeSDcard", "scale_tmp = " + scale_tmp
						+ "scale_width=" + scale_width + "scale_height="
						+ scale_height + "scale = " + scale);
			}
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			Bitmap bm = BitmapFactory.decodeStream(new FileInputStream(file),
					null, o2);
			if (bm == null) {
				file.delete();
				return null;
			} else {
				updateLastModifiedTime(file);
				return bm;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void deleteSDcardCacheAll() {
		if (mAppPath != null) {
			File img = new File(mImgPath);
			if (img.isDirectory()) {
				File[] imgs = img.listFiles();
				for (int i = 0; i < imgs.length; i++) {
					imgs[i].delete();
				}
			}
		}
	}

	// --------------------------------------------------------------------------memory
	private boolean saveUrlMemory(String u, String fileName, ProgressListener pl) {
		if (u == null || fileName == null)
			return false;
		try {
			File file = writecard(u, fileName);
			URL url = new URL(u);
			URLConnection con = url.openConnection();
			InputStream is = con.getInputStream();
			byte[] bs = new byte[1024];
			int len;
			int count = 0;
			int total = con.getContentLength();
			// OutputStream os = mCtx.openFileOutput(fileName,
			// Context.MODE_PRIVATE);
			OutputStream os = new FileOutputStream(file);
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
				count += len;
				if (pl != null) {
					if (total > 0) {
						pl.onProgress(count * 100 / total);
					}
				}
				// Logs.PRINT("saveUrlMemory----------------");
			}
			os.close();
			is.close();
			if (pl != null) {
				pl.onProgress(-1);
			}
			/*
			 * String command = "chmod 604 " + mCtx.getFilesDir() + "/" +
			 * Tab4ActivityDetail.mFullName;
			 * com.zhadui.streamdemo.utils.Constants.runCommand(command);
			 */
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (pl != null) {
			pl.onProgress(-2);
		}
		return false;
	}

	private File writecard(String urlpath, String filename) {
		long phoneMemory = getAvailableInternalMemorySize();
		if (phoneMemory > 52428800) // 50MB
		{
			PATH_LOGCAT = mCtx.getFilesDir().getAbsolutePath();
			File f = new File(PATH_LOGCAT);

			try {
				Runtime.getRuntime().exec("chmod 777 " + f.getAbsolutePath());
				// URL url = new URL(urlpath);
				// String surl = url.getFile().substring(
				// url.getFile().lastIndexOf("/"));
				File file = new File(f.toString() + "/" + filename);
				mImgPath = f.toString();
				return file;
			} catch (Exception e) {
				e.printStackTrace();

			}
		} else {
			// 内存容量不足
//			Toast.makeText(mCtx,
//					mCtx.getResources().getString(R.string.memory_notNogith),
//					Toast.LENGTH_LONG).show();

		}
		return null;
	}

	/**
	 * <获取手机可用的内存大小 单位为：B>
	 */
	public long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		if (null != path) {
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return availableBlocks * blockSize;
		}
		return 0;
	}

	private void removeMemory(String fileName) {
		if (fileName == null)
			return;
		try {
			mCtx.deleteFile(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveBmpMemory(Bitmap bm, String fileName) {
		if (bm == null)
			return;
		removeCache();
		FileOutputStream fos = null;
		try {
			fos = mCtx.openFileOutput(fileName, Context.MODE_PRIVATE);
			bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private File getFileMemory(String fileName) {
		if (fileName == null)
			return null;
		File file = mCtx.getFileStreamPath(fileName);
		return file;
	}

	private Bitmap getBitmapMemory(String fileName) {
		if (fileName == null)
			return null;
		try {

			FileInputStream fis = mCtx.openFileInput(fileName);
			Bitmap bm = BitmapFactory.decodeStream(fis);
			if (bm == null) {
				mCtx.deleteFile(fileName);
				return null;
			} else {
				return bm;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Bitmap getBitmapMemory(String fileName, int dstwidth, int dstheight) {
		if (fileName == null)
			return null;
		float scale_tmp = 0;
		int scale = 0;
		try {
			FileInputStream fis = mCtx.openFileInput(fileName);

			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(mCtx.openFileInput(fileName), null, o);
			int srcwidth = o.outWidth, srcheight = o.outHeight;

			if (dstwidth > 0 && dstheight > 0) {
				float scale_width = 0, scale_height = 0;
				if (srcwidth > dstwidth) {
					scale_width = srcwidth / dstwidth;
				}
				if (srcheight > dstheight) {
					scale_height = srcheight / dstheight;
				}
				scale_tmp = scale_width >= scale_height ? scale_width
						: scale_height;
				// Log.i("decodeMemory","scale_tmp = " + scale_tmp +
				// "scale_width=" + scale_width + "scale_height=" +
				// scale_height);
				if (scale_tmp == 0) {
					scale = 0;
				} else if (scale_tmp < 2) {
					scale = 2;
				} else if (scale_tmp < 4) {
					scale = 4;
				} else if (scale_tmp < 8) {
					scale = 8;
				} else {
					scale = 16;
				}

			}
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			Bitmap bm = BitmapFactory.decodeStream(fis, null, o2);
			if (bm == null) {
				mCtx.deleteFile(fileName);
				return null;
			} else {
				return bm;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void deleteMemoryCacheFile() {
		File f = mCtx.getFilesDir();
		if (!f.exists()) {
			return;
		}
		if (!f.isDirectory()) {
			f.delete();
		} else {
			File[] c = f.listFiles();
			for (int i = 0; i < c.length; i++) {
				// delAllFile(f);
				c[i].delete();
			}
			// f.delete();
		}
	}

	// -------------------------------------------------------------------------
	public void remove(String fileName) {
		if (fileName == null)
			return;
		if (mAppPath == null)
			removeMemory(fileName);
		else
			removeSDCard(fileName);
	}

	public boolean saveUrl(String u, String fileName, ProgressListener l) {
		if (u == null || fileName == null)
			return false;

		if (mAppPath == null)
			return saveUrlMemory(u, fileName, l);
		else
			return saveUrlSDCard(u, fileName, l);

	}

	public boolean saveUrl(String u, String fileName) {
		if (u == null || fileName == null)
			return false;
		if (mAppPath == null)
			return saveUrlMemory(u, fileName, null);
		else
			return saveUrlSDCard(u, fileName, null);

	}
	public boolean saveUrlPush(String u, String fileName) {
		if (u == null || fileName == null)
			return false;
		if (mAppPath == null)
			return saveUrlMemory(u, fileName, null);
		else
			return saveUrlSDCard2(u, fileName);

	}

	public void saveBmp(Bitmap bm, String fileName) {
		if (bm == null || fileName == null)
			return;
		if (mAppPath == null)
			saveBmpMemory(bm, fileName);
		else
			saveBmpSDCard(bm, fileName);
	}

	public String getFilePath() {
		return mImgPath;
	}
	public String getPushImgPath() {
		return mPushPath;
	}
	public File getFile(String fileName) {
		if (fileName == null)
			return null;
		if (mAppPath == null)
			return getFileMemory(fileName);
		else {
			File f = getFileSDCard(fileName);
			if (f == null)
				return getFileMemory(fileName);
			else
				return f;
		}
	}

	public Bitmap getBitmap(String fileName) {
		if (fileName == null)
			return null;
		if (mAppPath == null)
			return getBitmapMemory(fileName);
		else {
			Bitmap b = getBitmapSDCard(fileName);
			if (b == null)
				return getBitmapMemory(fileName);
			return b;
		}
	}

	public Bitmap getBitmap(String fileName, int width, int height) {
		if (fileName == null)
			return null;
		if (mAppPath == null)
			return getBitmapMemory(fileName, width, height);
		else {
			Bitmap b = getBitmapSDCard(fileName, width, height);
			if (b == null)
				return getBitmapMemory(fileName, width, height);
			return b;
		}
	}

	public String getAPKPath() {
		return getFile(APK_NAME).getAbsolutePath();
	}

	public String getScreenCastPath() {
		return mScreenCastPath;
	}
	class FileLastModifSort implements Comparator<File> {
		public int compare(File arg0, File arg1) {
			if (arg0.lastModified() > arg1.lastModified()) {
				return 1;
			} else if (arg0.lastModified() == arg1.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}
	}

	public static byte[] getByteFromImageView(ImageView iv) {
		byte[] imageData = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Bitmap bm = iv.getDrawingCache();
		if (bm != null) {
			bm.compress(Bitmap.CompressFormat.PNG, 50, baos);
			imageData = baos.toByteArray();
		}
		return imageData;
	}

	public void deleteAll() {
		deleteSDcardCacheAll();
		deleteMemoryCacheFile();
	}

	public interface ProgressListener {

		public void onProgress(int progress);
	}

	public void saveStringToFile(String fileName, String value) {
		File file = new File(fileName);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, false);
			fos.write(value.getBytes());
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 
	 * @描述:将drawable转为bitmap
	 * @方法名: drawableToBitmap
	 * @param drawable
	 * @return
	 * @返回类型 Bitmap
	 * @创建人 gao
	 * @创建时间 2014年7月22日上午11:38:58
	 * @修改人 gao
	 * @修改时间 2014年7月22日上午11:38:58
	 * @修改备注
	 * @since
	 * @throws
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 
	 * @描述:
	 * @方法名: copyIcon2Data
	 * @param bitmap
	 *            图片
	 * @param iconName
	 *            目标文件全路径
	 * @param path
	 *            目标目录
	 * @返回类型 void
	 * @创建人 gao
	 * @创建时间 2014年7月22日上午11:34:11
	 * @修改人 gao
	 * @修改时间 2014年7月22日上午11:34:11
	 * @修改备注
	 * @since
	 * @throws
	 */
	public static void copyIcon2Data(Bitmap bitmap, String path, String iconName) {
		try {
			// 如果文件夹不存在，创建一个（只能在应用包下面的目录，其他目录需要申请权限 OWL）
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			// 获得封装 文件的InputStream对象
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			InputStream is = new ByteArrayInputStream(baos.toByteArray());

			FileOutputStream fos = new FileOutputStream(iconName);

			byte[] buffer = new byte[8192];
			int count = 0;

			// 开始复制Logo图片文件
			while ((count = is.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
			fos.close();
			is.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean saveUrlcityList(String u, String fileName) {
		if (u == null || fileName == null)
			return false;

		if (mAppPath == null)
			return saveUrlMemory(u, fileName, null);
		else
			return saveUrlSDCard3(u, fileName, null);

	}
	private boolean saveUrlSDCard3(String u, String fileName, ProgressListener pl) {
		if (mCityList == null)
			return false;
		if (u == null || fileName == null)
			return false;
		removeCache();
		File file = new File(mCityList, fileName);
		if (file.exists())
			return true;
		try {
			file.createNewFile();
			URL url = new URL(u);
			URLConnection con = url.openConnection();
			InputStream is = con.getInputStream();
			byte[] bs = new byte[1024];
			int len;
			int count = 0;
			int total = con.getContentLength();
			OutputStream os = new FileOutputStream(file);
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
				count += len;
			}
			os.close();
			is.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
