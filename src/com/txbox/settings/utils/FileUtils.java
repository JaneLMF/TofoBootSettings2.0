package com.txbox.settings.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 
 * @类描述：文件读写工具类
 * @项目名称：go3cfactory
 * @包名： com.go3c.utils
 * @类名称：FileUtils
 * @创建人：gao
 * @创建时间：2014年6月23日下午12:23:51
 * @修改人：gao
 * @修改时间：2014年6月23日下午12:23:51
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright go3c
 * @mail 25550836@qq.com
 */
public class FileUtils {

	/**
	 * 
	 * @描述:创建目录
	 * @方法名: createDir
	 * @param path
	 * @返回类型 void
	 * @创建人 gao
	 * @创建时间 2014年9月12日下午2:42:24
	 * @修改人 gao
	 * @修改时间 2014年9月12日下午2:42:24
	 * @修改备注
	 * @since
	 * @throws
	 */
	public static void createDir(String path) {
		File file = new File(path);// 文件目录
		if (!file.exists() && !file.isDirectory()) {// 判断目录是否存在，不存在创建
			file.mkdir();// 创建目录
		}
	}

	/**
	 * 
	 * @描述:复制单个文件
	 * @方法名: copyFile
	 * @param oldPath
	 *            ：原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            ：复制后路径 如：f:/fqf.txt
	 * @返回类型 void
	 * @创建人 gao
	 * @创建时间 2014年9月13日下午2:55:28
	 * @修改人 gao
	 * @修改时间 2014年9月13日下午2:55:28
	 * @修改备注
	 * @since
	 * @throws
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @描述:复制整个文件夹内容
	 * @方法名: copyFolder
	 * @param oldPath
	 *            ：原文件路径
	 * @param newPath
	 *            ：复制后路径
	 * @返回类型 void
	 * @创建人 gao
	 * @创建时间 2014年9月4日下午9:05:39
	 * @修改人 gao
	 * @修改时间 2014年9月4日下午9:05:39
	 * @修改备注
	 * @since
	 * @throws
	 */
	public static void copyFolder(String oldPath, String newPath) {

		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();

		}

	}

	/**
	 * 
	 * @描述:删除文件夹
	 * @方法名: delFolder
	 * @param folderPath
	 *            ：文件夹路径
	 * @返回类型 void
	 * @创建人 gao
	 * @创建时间 2014年9月4日下午4:00:35
	 * @修改人 gao
	 * @修改时间 2014年9月4日下午4:00:35
	 * @修改备注
	 * @since
	 * @throws
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @描述:删除指定文件夹下所有文件
	 * @方法名: delAllFile
	 * @param path
	 *            ：文件夹路径
	 * @return
	 * @返回类型 boolean
	 * @创建人 gao
	 * @创建时间 2014年9月4日下午4:01:07
	 * @修改人 gao
	 * @修改时间 2014年9月4日下午4:01:07
	 * @修改备注
	 * @since
	 * @throws
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 
	 * @描述:移动指定文件夹内的全部文件
	 * @方法名: fileMove
	 * @param from
	 *            ：要移动的文件目录
	 * @param to
	 *            ：目标文件目录
	 * @throws Exception
	 * @返回类型 void
	 * @创建人 gao
	 * @创建时间 2014年8月28日上午9:36:42
	 * @修改人 gao
	 * @修改时间 2014年8月28日上午9:36:42
	 * @修改备注
	 * @since
	 * @throws
	 */
	public static void fileMove(String from, String to, boolean isrename) {
		try {
			File dir = new File(from);
			File[] files = dir.listFiles();// 文件一览
			if (files == null)
				return;
			File moveDir = new File(to);// 目标
			if (!moveDir.exists()) {
				moveDir.mkdirs();
			}
			// 文件移动
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					fileMove(files[i].getPath(), to + "/" + files[i].getName(), isrename);
					files[i].delete();// 成功，删除原文件
				}
				File moveFile = new File(moveDir.getPath() + "/" + files[i].getName());
				// 目标文件夹下存在的话，删除
				if (moveFile.exists()) {
					moveFile.delete();
				}

				if (isrename) {
					files[i].renameTo(moveFile);
				} else {
					android.os.FileUtils.copyFile(files[i], new File(moveDir.getPath() + "/" + files[i].getName()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @描述:获取本地图片
	 * @方法名: getLoacalBitmap
	 * @param url
	 * @return
	 * @返回类型 Bitmap
	 * @创建人 gao
	 * @创建时间 2014年6月23日下午12:24:04
	 * @修改人 gao
	 * @修改时间 2014年6月23日下午12:24:04
	 * @修改备注
	 * @since
	 * @throws
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @描述:读取文件并将结果转为json格式
	 * @方法名: convertStreamToString
	 * @param path
	 * @return
	 * @throws UnsupportedEncodingException
	 * @返回类型 StringBuilder
	 * @创建人 gao
	 * @创建时间 2014年6月23日下午12:24:20
	 * @修改人 gao
	 * @修改时间 2014年6月23日下午12:24:20
	 * @修改备注
	 * @since
	 * @throws
	 */
	public static StringBuilder convertStreamToString(String path) throws UnsupportedEncodingException {

		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		BufferedInputStream in = new BufferedInputStream(inputStream);
		in.mark(4);
		byte[] first3bytes = new byte[3];
		try {
			in.read(first3bytes);
			in.reset();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		BufferedReader br = null;

		try {
			br = new BufferedReader(new InputStreamReader(in, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String line = null;
		StringBuilder sb = new StringBuilder();

		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb;
	}

	/**
	 * 
	 * @描述:写入文件
	 * @方法名: writeFile
	 * @param path
	 * @param fileName
	 * @param content
	 * @throws IOException
	 * @返回类型 void
	 * @创建人 gao
	 * @创建时间 2014年6月23日下午12:24:56
	 * @修改人 gao
	 * @修改时间 2014年6月23日下午12:24:56
	 * @修改备注
	 * @since
	 * @throws
	 */
	public static void writeFile(String path, String fileName, String content) throws IOException {
		File file = new File(path);// 文件目录

		if (!file.exists()) {// 判断目录是否存在，不存在创建
			file.mkdir();// 创建目录
		}
		// 打开文件
		file = new File(path + fileName);

		// 判断文件是否存在,不存在则创建
		if (!file.exists()) {
			file.createNewFile();// 创建文件
		}

		// 写数据 注意这里，两个参数，第一个是写入的文件，第二个是指是覆盖还是追加，
		// 默认是覆盖的，就是不写第二个参数，这里设置为true就是说不覆盖，是在后面追加。
		FileOutputStream outputStream = new FileOutputStream(file, false);
		outputStream.write(content.getBytes());// 写入内容
		outputStream.close();// 关闭流
	}

	/**
	 * 
	 * @描述:写入文件
	 * @方法名: writeFile
	 * @param file
	 * @param content
	 * @throws IOException
	 * @返回类型 void
	 * @创建人 gao
	 * @创建时间 2014年6月23日下午12:25:09
	 * @修改人 gao
	 * @修改时间 2014年6月23日下午12:25:09
	 * @修改备注
	 * @since
	 * @throws
	 */
	public static void writeFile(File file, String content) throws IOException {
		if (file.exists()) {
			// 写数据 注意这里，两个参数，第一个是写入的文件，第二个是指是覆盖还是追加，
			// 默认是覆盖的，就是不写第二个参数，这里设置为true就是说不覆盖，是在后面追加。
			FileOutputStream outputStream = new FileOutputStream(file, false);
			outputStream.write(content.getBytes());// 写入内容
			outputStream.close();// 关闭流
		}
	}
}
