package com.txbox.settings.db;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.txbox.settings.bean.CrashBean;
import com.txbox.settings.utils.Params;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * @类描述：
 * @项目名称：go3cLauncher
 * @包名： com.go3c.db
 * @类名称：AppDao	
 * @创建人："liting yan"
 * @创建时间：2014-4-30上午10:32:30	
 * @修改人："liting yan"
 * @修改时间：2014-4-30上午10:32:30	
 * @修改备注：
 * @version v1.0
 * @see [nothing]
 * @bug [nothing]
 * @Copyright go3c
 * @mail 939757170@qq.com
 */
public class AppDao {
	private static SqliteHandler db = null;
	private static AppDao dao = null;
	
	public static final int RESULT_REDO = -2;
	public static final int RESULT_ERROR = -1;
	public static final int RESULT_SUCCESS = 0;

	private AppDao(Context context) {
	
		//CopyOldDB(context);
		
		if(context!=null){
			db = new SqliteHandler(context, Params.DB_NAME, null, Params.DB_VERSION);
		}else{
			//db = new SqliteHandler(context, Params.DB_NAME, null, Params.DB_VERSION);
		}
	}
	
	private void CopyOldDB(Context context){
		
		File file = context.getDatabasePath(Params.DB_NAME);
		if(file.exists()){
			File destfile = new File(Params.internalPath+"/database/");
			if(!destfile.exists()){
				destfile.mkdir();
			}
			File dbfile = new File(Params.internalPath+"/database/"+Params.DB_NAME);
			if(!dbfile.exists()){
				file.renameTo(dbfile);
				file.delete();
			}
			
			
		}
		
	}
	public static AppDao getInstance(Context context) {
		if (dao == null) {
			dao = new AppDao(context);
			return dao;
		} else {
			return dao;
		}
	}
	public static AppDao getDao(){
		if(dao!=null){
			return dao;
		}else{
			return null;
		}
	}

	/**
	 * 根据状态获取数据
	 * 
	 * @param status
	 * @param downloadstatus
	 * @return
	 */
	public List<Map<String, Object>> getAppByStatus(int status,
			int downloadstatus) {
		Cursor cursor = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String sql = "select * from app_list where stauts=? and downloadstatus=? group by id";
			cursor = db.getReadableDatabase().rawQuery(sql,
					new String[] { status + "", downloadstatus + "" });
			list = convertCursor2List(cursor);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}
		return list;
	}

	/**
	 * 根据状态获取数据
	 * 
	 * @param status
	 * @param downloadstatus
	 * @return
	 */
	public List<Map<String, Object>> getAppByStatus(int status) {
		Cursor cursor = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String sql = "select * from app_list where stauts=? group by id order by installtime desc";
			cursor = db.getReadableDatabase().rawQuery(sql,
					new String[] { status + "" });
			list = convertCursor2List(cursor);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}
		return list;
	}

	/**
	 * 根据安装状态获取分页数据
	 * 
	 * @param status
	 * @param downloadstatus
	 * @return
	 */
	public List<Map<String, Object>> getAppByStatus(int status, int start,
			int end) {
		Cursor cursor = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String sql = "select * from app_list where stauts=? group by id order by installtime desc limit ?,?";
			cursor = db.getReadableDatabase().rawQuery(sql,
					new String[] { status + "", start + "", end + "" });
			list = convertCursor2List(cursor);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}
		return list;
	}

	/**
	 * 根据安装状态获取数据数量
	 * 
	 * @param status
	 * @param downloadstatus
	 * @return
	 */
	public int getAppCountByStatus(int status) {
		Cursor cursor = null;
		int count = 0;
		try {
			String sql = "select id from app_list where stauts=?";
			cursor = db.getReadableDatabase().rawQuery(sql,
					new String[] { status + "" });
			count = cursor.getCount();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}
		return count;
	}

	/**
	 * 根据状态和忽略状态获取数据(分页)
	 * 
	 * @param status
	 * @param downloadstatus
	 * @return
	 */
	public List<Map<String, Object>> getAppByStatusAndIgnored(int status,
			int downloadstatus, int ignored, int start, int end) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Cursor cursor = null;
		String sql = "select * from app_list where stauts=? and downloadstatus=? and ignored=? group by id limit ?,?";
		try {
			cursor = db.getReadableDatabase().rawQuery(
					sql,
					new String[] { status + "", downloadstatus + "",
							ignored + "", start + "", end + "" });
			list = convertCursor2List(cursor);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

		return list;
	}

	/**
	 * 根据状态和忽略状态获取数据(分页)
	 * 
	 * @param status
	 * @param downloadstatus
	 * @return
	 */
	public int getAppCountByStatusAndIgnored(int status, int downloadstatus,
			int ignored) {
		Cursor cursor = null;
		int count = 0;
		String sql = "select * from app_list where stauts=? and downloadstatus=? and ignored=? group by id";
		try {
			cursor = db.getReadableDatabase().rawQuery(
					sql,
					new String[] { status + "", downloadstatus + "",
							ignored + "" });
			count = cursor.getCount();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

		return count;
	}

	/**
	 * 获取不包含downloadstatus的数据
	 * 
	 * @param status
	 * @param downloadstatus
	 * @return
	 */
	public List<Map<String, Object>> getAppByExcludeStatus(int status,
			int downloadstatus) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String sql = "select * from app_list where stauts=? and downloadstatus!=? group by id";
		Cursor cursor = null;
		try {
			cursor = db.getReadableDatabase().rawQuery(sql,
					new String[] { status + "", downloadstatus + "" });
			list = convertCursor2List(cursor);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}
		return list;
	}

	/**
	 * 获取所有app
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getAllApp() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Cursor cursor = null;
		try {
			//String sql = "select * from app_list";
			String sql = "select * from app_list group by id";
			cursor = db.getReadableDatabase().rawQuery(sql, null);
			list = convertCursor2List(cursor);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

		return list;
	}

	/**
	 * 插入数据到数据库
	 * 
	 * @param appId
	 * @param appTitle
	 * @param url
	 * @param image
	 * @param percent
	 * @param startPos
	 * @param status
	 * @param downloadStatus
	 * @param version
	 * @param ignored
	 * @param vernum
	 */
	public void insertApp(int appId, String appTitle, String url, String image,
			int percent, int startPos, int status, int downloadStatus,
			String version, int ignored, float size,String packageName) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String s = sdf.format(date);
		String sql = "insert into app_list values(" + appId + ",'" + appTitle
				+ "'," + percent + ",'" + url + "','" + image + "'," + startPos
				+ "," + status + "," + downloadStatus + "," + ignored + ",'"
				+ version + "','" + packageName +"',"+ size + ",'"+s+"')";
		db.getWritableDatabase().execSQL(sql);
	}

	public boolean containedApp(int appId) {
		boolean flag = false;
		String sql = "select id from app_list where id=?";
		Cursor cursor = null;
		try {
			cursor = db.getReadableDatabase().rawQuery(sql,
					new String[] { appId + "" });
			if (cursor.moveToFirst()) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

		return flag;
	}

	private List<Map<String, Object>> convertCursor2List(Cursor cursor) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while (cursor.moveToNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", cursor.getInt(0));
			map.put("title", cursor.getString(1));
			map.put("percent", cursor.getInt(2));
			map.put("url", cursor.getString(3));
			map.put("image", cursor.getString(4));
			map.put("startpos", cursor.getInt(5));
			map.put("status", cursor.getInt(6));
			map.put("downloadstatus", cursor.getInt(7));
			map.put("ignored", cursor.getInt(8));
			map.put("version", cursor.getString(9));
			map.put("package", cursor.getString(10));
			map.put("size", cursor.getFloat(11));
			list.add(map);
		}
		return list;

	}

	public void updateAppStatus(int status, int downloadstatus, int appId) {
		String sql = "update app_list set stauts=?,downloadstatus=? where id=?";
		db.getWritableDatabase().execSQL(sql,
				new Object[] { status, downloadstatus, appId });
	}

	public List<Map<String, Object>> getAppById(int id) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String sql = "select * from app_list where id=? group by id";
		Cursor cursor = null;
		try {
			cursor = db.getReadableDatabase().rawQuery(sql,
					new String[] { id + "" });
			list = convertCursor2List(cursor);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

		return list;
	}

	public void updateAppStatusAndPackage(int status, int downloadstatus,
			String packageName, int appId) {
		String sql = "update app_list set stauts=?,downloadstatus=?,package=? where id=?";
		db.getWritableDatabase().execSQL(sql,
				new Object[] { status, downloadstatus, packageName, appId });
	}

	public Integer getAppIdByPackage(String packageName) {
		Integer id = null;
		String sql = "select id from app_list where package=?";
		Cursor cursor = null;
		try {
			cursor = db.getReadableDatabase().rawQuery(sql,
					new String[] { packageName });
			if (cursor.moveToFirst()) {
				id = cursor.getInt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

		return id;
	}

	public void deleteApp(int id) {
		String sql = "delete from app_list where id=?";
		db.getWritableDatabase().execSQL(sql, new Object[] { id });
	}

	public void updateDownloadProgress(int appId, int rate, int hasRead) {
		String sql = "update app_list set startpos=?,percent=? where id=?";
		db.getWritableDatabase().execSQL(sql,
				new Object[] { hasRead, rate, appId });
	}

	/**
	 * 
	 * 更新忽略状态
	 * 
	 * @param appId
	 * @param ignore
	 */
	public void updateIgnoreStatus(int appId, int ignore) {
		String sql = "update app_list set ignored=? where id=?";
		db.getWritableDatabase().execSQL(sql, new Object[] { ignore, appId });

	}

	/**
	 * 批量更新忽略状态
	 * 
	 * @param appId
	 * @param ignore
	 */
	public void updateIgnoreStatus(int[] appIds, int ignore) {
		String sql = "update app_list set ignored=? where id in (";
		for (int i = 0; i < appIds.length; i++) {
			sql= sql+appIds[i];
			sql = sql+",";
		}
		sql = sql.substring(0,sql.length()-1);
		sql = sql+")";
		db.getWritableDatabase().execSQL(sql, new Object[] { ignore });
	}

	/**
	 * 更新状态和版本
	 * 
	 * @param status
	 * @param downloadstatus
	 * @param appId
	 * @param version
	 * @param vernum
	 */
	public void updateAppStatusAndVersion(int status, int downloadstatus,
			int appId, String version, float size) {
		String sql = "update app_list set stauts=?,downloadstatus=?,version=?,size=?,installtime=? where id=?";
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String s = sdf.format(date);
		System.out.println("-------------------------"+s);
		db.getWritableDatabase().execSQL(sql,
				new Object[] { status, downloadstatus, version, size, s,appId });

	}

	/**
	 * 获取不包含状态的数据
	 * 
	 * @param downloadstatus
	 * @return
	 */
	public List<Map<String, Object>> getAppExcludeDownloadstatus(
			int downloadstatus, int start, int end) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String sql = "select * from app_list where downloadstatus!=? group by id order by installtime desc limit ?,?";
		Cursor cursor = null;
		try {
			cursor = db.getReadableDatabase().rawQuery(sql,
					new String[] { downloadstatus + "", start + "", end + "" });
			list = convertCursor2List(cursor);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}

		return list;
	}

	/**
	 * 获取不包含状态的数据的总页数
	 * 
	 * @param downloadstatus
	 * @return
	 */
	public int getAppExcludeDownloadstatusCount(int downloadstatus) {
		int count = 0;
		String sql = "select id from app_list where downloadstatus!=?";
		Cursor cursor = null;
		try {
			cursor = db.getReadableDatabase().rawQuery(sql,
					new String[] { downloadstatus + "" });
			count = cursor.getCount();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		}
		return count;
	}

	public void closeAll() {
		if (db != null) {
			db.close();
		}
	}
//	/**
//	 * 
//	 * @描述:添加播放记录
//	 * @方法名: addPlayHistory
//	 * @param item
//	 * @return
//	 * @返回类型 int
//	 * @创建人 "liting yan"
//	 * @创建时间 2014-4-30上午10:23:21	
//	 * @修改人 "liting yan"
//	 * @修改时间 2014-4-30上午10:23:21	
//	 * @修改备注 
//	 * @since
//	 * @throws
//	 */
//	public int addPlayHistory(PlayHistory item){
//		int result = RESULT_ERROR;
//		SQLiteDatabase sdb=db.getReadableDatabase();
//		String sql  = "SELECT vId FROM history_list" + " WHERE vId ="+"\""+item.getVID()+"\"";
//		System.out.println("add play history sql=" + sql);
//		Cursor cur = sdb.rawQuery(sql, null);
//		if(cur==null) return RESULT_ERROR;
//		if(cur.getCount()>0){
//			result = UpdatePlayerHistory(sdb,item);
//		} else {
//			result = InsertPlayerHistory(sdb,item);
//		}
//		cur.close();
//		sdb.close();
//		return result;
//	}
//	/**
//	 * 
//	 * @描述:删除单条播放记录
//	 * @方法名: delPlayHistory
//	 * @param vid
//	 * @return
//	 * @返回类型 int
//	 * @创建人 "liting yan"
//	 * @创建时间 2014-4-30上午10:21:37	
//	 * @修改人 "liting yan"
//	 * @修改时间 2014-4-30上午10:21:37	
//	 * @修改备注 
//	 * @since
//	 * @throws
//	 */
//	public int delPlayHistory(String vid){
//		SQLiteDatabase sdb=db.getWritableDatabase();
//		String sql  = "delete from history_list" + " WHERE vId =" +"\"" + vid + "\"";
//		sdb.execSQL(sql);
//		return 0;
//	}
//
//	/**
//	 * 
//	 * @描述:清空播放记录
//	 * @方法名: clearPlayHistory
//	 * @return
//	 * @返回类型 int
//	 * @创建人 "liting yan"
//	 * @创建时间 2014-4-30上午10:21:00	
//	 * @修改人 "liting yan"
//	 * @修改时间 2014-4-30上午10:21:00	
//	 * @修改备注 
//	 * @since
//	 * @throws
//	 */
//	public int clearPlayHistory(){
//		SQLiteDatabase sdb=db.getWritableDatabase();
//		String sql  = "delete from history_list";
//		sdb.execSQL(sql);
//		return 0;
//	}
//	
//	/**
//	 * 
//	 * @描述:获取单条播放记录
//	 * @方法名: getPlayHistory
//	 * @param vid
//	 * @return
//	 * @返回类型 PlayHistory
//	 * @创建人 "liting yan"
//	 * @创建时间 2014-4-30上午10:20:31	
//	 * @修改人 "liting yan"
//	 * @修改时间 2014-4-30上午10:20:31	
//	 * @修改备注 
//	 * @since
//	 * @throws
//	 */
//	public PlayHistory getPlayHistory(String vid){
//		PlayHistory info = new PlayHistory();
//		SQLiteDatabase sdb= db.getReadableDatabase();
//		String sql = "select * from history_list" + " where vId =" +"\"" + vid + "\"";
//		System.out.println("get play history sql=" + sql);
//		Cursor cursor = null;
//		int status = 1;
//		try {
//			cursor = sdb.rawQuery(sql,null);
//			while (cursor.moveToNext()) {
//				info.setVID(cursor.getString(cursor.getColumnIndex("vId")));
//				info.setUrl(cursor.getString(cursor.getColumnIndex("vUrl")));
//				info.setName(cursor.getString(cursor.getColumnIndex("vName")));
//				info.setImage(cursor.getString(cursor.getColumnIndex("vImage")));
//				info.setEpisode(cursor.getInt(cursor.getColumnIndex("vEpisode")));
//				info.setPosition(cursor.getInt(cursor.getColumnIndex("vPos")));
//				info.setColumeId(cursor.getInt(cursor.getColumnIndex("vColumeId")));
//				return info;
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (cursor != null && !cursor.isClosed()) {
//				cursor.close();
//				sdb.close();
//				cursor = null;
//			}
//		}
//		return null;
//	}
//	
//	/**
//	 * 
//	 * @描述:获取播放记录列表
//	 * @方法名: getPlayHistoryList
//	 * @param columeId
//	 * @return
//	 * @返回类型 List<PlayHistory>
//	 * @创建人 "liting yan"
//	 * @创建时间 2014-4-30上午10:22:11	
//	 * @修改人 "liting yan"
//	 * @修改时间 2014-4-30上午10:22:11	
//	 * @修改备注 
//	 * @since
//	 * @throws
//	 */
//	public List<PlayHistory> getPlayHistoryList(int columeId){
//		List<PlayHistory> list = new ArrayList<PlayHistory>();
//		SQLiteDatabase sdb= db.getReadableDatabase();
//		String sql = "";
//		if(columeId>0){
//			sql = "select * from history_list" + " where vColumeId ="+columeId +" order by updatetime desc";
//		}else{
//			sql = "select * from history_list" + " where 1 order by updatetime desc";
//		}
//		System.out.println("get play history list sql=" + sql);
//		Cursor cursor = null;
//		try {
//			cursor = sdb.rawQuery(sql,null);
//			while (cursor.moveToNext()) {
//				PlayHistory info = new PlayHistory();
//				info.setVID(cursor.getString(cursor.getColumnIndex("vId")));
//				info.setUrl(cursor.getString(cursor.getColumnIndex("vUrl")));
//				info.setName(cursor.getString(cursor.getColumnIndex("vName")));
//				info.setImage(cursor.getString(cursor.getColumnIndex("vImage")));
//				info.setEpisode(cursor.getInt(cursor.getColumnIndex("vEpisode")));
//				info.setPosition(cursor.getInt(cursor.getColumnIndex("vPos")));
//				info.setColumeId(cursor.getInt(cursor.getColumnIndex("vColumeId")));
//				list.add(info);;
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (cursor != null && !cursor.isClosed()) {
//				cursor.close();
//				sdb.close();
//				cursor = null;
//			}
//		}
//		return list;
//	}
//	/**
//	 * 
//	 * @描述:更新播放记录
//	 * @方法名: UpdatePlayerHistory
//	 * @param db
//	 * @param item
//	 * @return
//	 * @返回类型 int
//	 * @创建人 "liting yan"
//	 * @创建时间 2014-4-30上午10:22:36	
//	 * @修改人 "liting yan"
//	 * @修改时间 2014-4-30上午10:22:36	
//	 * @修改备注 
//	 * @since
//	 * @throws
//	 */
//	private int UpdatePlayerHistory(SQLiteDatabase db,PlayHistory item){
//		int result = RESULT_SUCCESS;
//		String sql = "update history_list set vEpisode=?,vPos=?,updatetime=datetime('now','localtime') where vId=?";
//		System.out.println("update play history sql=" + sql);
//		db.execSQL(sql,
//				new Object[] { item.getEpisode(), item.getPosition(),item.getVID() });
//		return result;
//	}
//	/**
//	 * 
//	 * @描述:插入一条新的播放记录
//	 * @方法名: InsertPlayerHistory
//	 * @param db
//	 * @param item
//	 * @return
//	 * @返回类型 int
//	 * @创建人 "liting yan"
//	 * @创建时间 2014-4-30上午10:22:56	
//	 * @修改人 "liting yan"
//	 * @修改时间 2014-4-30上午10:22:56	
//	 * @修改备注 
//	 * @since
//	 * @throws
//	 */
//	private int InsertPlayerHistory(SQLiteDatabase db,PlayHistory item) {
//		int result = RESULT_ERROR;
//		ContentValues cv=new ContentValues();
//		cv.put("vId", item.getVID());
//		cv.put("vUrl", item.getUrl());
//		cv.put("vName", item.getName());
//		cv.put("vImage",item.getImage());
//		cv.put("vEpisode",item.getEpisode());
//		cv.put("vPos",item.getPosition());
//		cv.put("vColumeId",item.getColumeId());
//		System.out.println("insert play history " );
//		result=(int) db.insert("history_list", null, cv);
//		return result;
//	}
//
//	/**
//	 * 
//	 * @描述:插入收藏记录
//	 * @方法名: addBookmark
//	 * @param db
//	 * @param item
//	 * @return
//	 * @返回类型 int
//	 * @创建人 gao
//	 * @创建时间 2014-6-6下午3:14:27
//	 * @修改人 gao
//	 * @修改时间 2014-6-6下午3:14:27
//	 * @修改备注
//	 * @since
//	 * @throws
//	 */
//	public int addBookmark(Bookmark item) {
//		SQLiteDatabase sdb = db.getReadableDatabase();
//		int result = RESULT_ERROR;
//		ContentValues cv = new ContentValues();
//		cv.put("vId", item.getVideoID());
//		cv.put("type", item.getType());
//		cv.put("folder", item.getFolder());
//		cv.put("name", item.getVideoName());
//		cv.put("image", item.getImageUrl());
//		cv.put("columeId", item.getColumnID());
//		System.out.println("insert play bookmark ");
//		result = (int) sdb.insert("bookmark_list", null, cv);
//		return result;
//	}
//
//	/**
//	 * 
//	 * @描述:获取收藏列表
//	 * @方法名: getBookmarkList
//	 * @param columeId
//	 * @param type
//	 * @return
//	 * @返回类型 List<Bookmark>
//	 * @创建人 gao
//	 * @创建时间 2014-6-6下午3:14:46
//	 * @修改人 gao
//	 * @修改时间 2014-6-6下午3:14:46
//	 * @修改备注
//	 * @since
//	 * @throws
//	 */
//	public List<Bookmark> getBookmarkList(Integer columeId, String type) {
//		List<Bookmark> list = new ArrayList<Bookmark>();
//		SQLiteDatabase sdb = db.getReadableDatabase();
//		StringBuilder sql = new StringBuilder();
//		sql.append("select * from bookmark_list where 1 ");
//		if (columeId != null && columeId > 0) {
//			sql.append(" and columeId=" + columeId);
//		}
//		if (type != null && !"".equals(type)) {
//			sql.append(" and type='" + type + "'");
//		}
//		sql.append(" order by updatetime desc");
//		System.out.println("get play bookmark list sql=" + sql);
//		Cursor cursor = null;
//		try {
//			cursor = sdb.rawQuery(sql.toString(), null);
//			while (cursor.moveToNext()) {
//				Bookmark info = new Bookmark();
//				info.setVideoID(cursor.getString(cursor.getColumnIndex("vId")));
//				info.setType(cursor.getString(cursor.getColumnIndex("type")));
//				info.setFolder(cursor.getString(cursor.getColumnIndex("folder")));
//				info.setVideoName(cursor.getString(cursor.getColumnIndex("name")));
//				info.setImageUrl(cursor.getString(cursor.getColumnIndex("image")));
//				info.setColumnID(cursor.getInt(cursor.getColumnIndex("columeId")));
//				list.add(info);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (cursor != null && !cursor.isClosed()) {
//				cursor.close();
//				sdb.close();
//				cursor = null;
//			}
//		}
//		return list;
//	}
//
//	/**
//	 * 
//	 * @描述:删除收藏记录
//	 * @方法名: delBookmark
//	 * @param vid
//	 * @return
//	 * @返回类型 int
//	 * @创建人 gao
//	 * @创建时间 2014-6-6下午3:14:59
//	 * @修改人 gao
//	 * @修改时间 2014-6-6下午3:14:59
//	 * @修改备注
//	 * @since
//	 * @throws
//	 */
//	public int delBookmark(String vid) {
//		SQLiteDatabase sdb = db.getWritableDatabase();
//		String sql = "delete from bookmark_list WHERE vId ='" + vid + "'";
//		sdb.execSQL(sql);
//		return 0;
//	}
//
//	/**
//	 * 
//	 * @描述:校验收藏记录是否存在
//	 * @方法名: checkBookmark
//	 * @param vid
//	 * @return
//	 * @返回类型 int
//	 * @创建人 gao
//	 * @创建时间 2014-6-6下午3:15:13
//	 * @修改人 gao
//	 * @修改时间 2014-6-6下午3:15:13
//	 * @修改备注
//	 * @since
//	 * @throws
//	 */
//	public int checkBookmark(String vid) {
//		int result = RESULT_ERROR;
//		SQLiteDatabase sdb = db.getReadableDatabase();
//		String sql = "select * from bookmark_list where vId ='" + vid + "'";
//		System.out.println("get play bookmark sql=" + sql);
//		Cursor cursor = null;
//		try {
//			cursor = sdb.rawQuery(sql, null);
//			while (cursor.moveToNext()) {
//				result = RESULT_SUCCESS;
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (cursor != null && !cursor.isClosed()) {
//				cursor.close();
//				sdb.close();
//				cursor = null;
//			}
//		}
//		return result;
//	}

	/**
	 * 
	 * @描述:插入crash记录
	 * @方法名: insertCrashList
	 * @param item
	 * @return
	 * @返回类型 int
	 * @创建人 gao
	 * @创建时间 2014年6月30日下午6:28:56
	 * @修改人 gao
	 * @修改时间 2014年6月30日下午6:28:56
	 * @修改备注
	 * @since
	 * @throws
	 */
	public int insertCrashList(CrashBean item) {
		if(db==null) return -1;
		SQLiteDatabase sdb = db.getWritableDatabase();
		int result = RESULT_ERROR;
		ContentValues cv = new ContentValues();
		cv.put("descrption", item.getDescrption());
		System.out.println("insert crash_list");
		result = (int) sdb.insert("crash_list", null, cv);
		return result;
	}

	/**
	 * 
	 * @描述:删除crash记录
	 * @方法名: delCrashById
	 * @param id
	 * @return
	 * @返回类型 int
	 * @创建人 gao
	 * @创建时间 2014年6月30日下午6:29:10
	 * @修改人 gao
	 * @修改时间 2014年6月30日下午6:29:10
	 * @修改备注
	 * @since
	 * @throws
	 */
	public int delCrashById(String id) {
		SQLiteDatabase sdb = db.getWritableDatabase();
		String sql = "delete from crash_list WHERE id ='" + id + "'";
		sdb.execSQL(sql);
		return 0;
	}

	/**
	 * 
	 * @描述:获取crash列表
	 * @方法名: getCrashList
	 * @return
	 * @返回类型 List<CrashBean>
	 * @创建人 gao
	 * @创建时间 2014年6月30日下午6:29:23
	 * @修改人 gao
	 * @修改时间 2014年6月30日下午6:29:23
	 * @修改备注
	 * @since
	 * @throws
	 */
	public List<CrashBean> getCrashList() {
		List<CrashBean> list = new ArrayList<CrashBean>();
		SQLiteDatabase sdb = db.getReadableDatabase();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from crash_list order by time_added desc");
		System.out.println("get crash_list sql=" + sql);
		Cursor cursor = null;
		try {
			cursor = sdb.rawQuery(sql.toString(), null);
			while (cursor.moveToNext()) {
				CrashBean info = new CrashBean();
				info.setId(cursor.getInt(cursor.getColumnIndex("id")));
				info.setTime_added(cursor.getString(cursor.getColumnIndex("time_added")));
				info.setDescrption(cursor.getString(cursor.getColumnIndex("descrption")));
				list.add(info);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
				sdb.close();
				cursor = null;
			}
		}
		return list;
	}
	
//	private int UpdateUserBehavior(SQLiteDatabase db,BehaviorBean item,int behavior){
//		int result = RESULT_SUCCESS;
//		int playtotal = item.getPlayTotal();
//		int favoritetotal = item.getFavoriteTotal();
//		int pubtotal = item.getPubTotal();
//		if(behavior==0) playtotal+=1;
//		if(behavior==1) favoritetotal+=1;
//		if(behavior==2) pubtotal+=1;
//		String sql = "update userbehavior_list set play_total=?,favorite_total=?,pub_total=?,time_added=datetime('now','localtime') where cId=?";
//		System.out.println("update play userbehavior_list sql=" + sql);
//		db.execSQL(sql,
//				new Object[] { playtotal,favoritetotal,pubtotal,item.getID() });
//		return result;
//	}
//	
//	private int InsertUserBehavior(SQLiteDatabase db,BehaviorBean item,int behavior) {
//		int result = RESULT_ERROR;
//		ContentValues cv=new ContentValues();
//		int playtotal = item.getPlayTotal();
//		int favoritetotal = item.getFavoriteTotal();
//		int pubtotal = item.getPubTotal();
//		if(behavior==0) playtotal+=1;
//		if(behavior==1) favoritetotal+=1;
//		if(behavior==2) pubtotal+=1;
//		cv.put("cId", item.getID());
//		cv.put("play_total", playtotal);
//		cv.put("favorite_total", favoritetotal);
//		cv.put("pub_total",pubtotal);
//		result=(int) db.insert("userbehavior_list", null, cv);
//		return result;
//	}
//	
//	/**
//	 * 
//	 * @描述:添加用户行为统计
//	 * @方法名: addUserBehavior
//	 * @param cID
//	 * @param behavior 0：增加播放次数，1：增加收藏次数，2：增加公播次数
//	 * @return
//	 * @返回类型 int
//	 * @创建人 Administrator
//	 * @创建时间 2014-8-29下午4:47:34	
//	 * @修改人 Administrator
//	 * @修改时间 2014-8-29下午4:47:34	
//	 * @修改备注 
//	 * @since
//	 * @throws
//	 */
//	public int addUserBehavior(String cID,int behavior) {
//		int result = RESULT_ERROR;
//		SQLiteDatabase sdb=db.getReadableDatabase();
//		String sql  = "SELECT * FROM userbehavior_list" + " WHERE cID ="+"\""+cID+"\"";
//		System.out.println("add play total sql=" + sql);
//		Cursor cur = sdb.rawQuery(sql, null);
//		if(cur==null) return RESULT_ERROR;
//		int playtotal = 0;
//		int favoritetotal = 0;
//		int pubtotal = 0;
//		
//		BehaviorBean info = new BehaviorBean();
//		if(cur.getCount()>0){
//			try {
//				cur = sdb.rawQuery(sql,null);
//				while (cur.moveToNext()) {
//					info.setID(cur.getString(cur.getColumnIndex("cId")));
//					info.setPlayTotal(cur.getInt(cur.getColumnIndex("play_total")));
//					info.setFavoriteTotal(cur.getInt(cur.getColumnIndex("favorite_total")));
//					info.setPubTotal(cur.getInt(cur.getColumnIndex("pub_total")));
//				}
//				
//			} catch (Exception e) {
//				e.printStackTrace();
//			} 
//			result = UpdateUserBehavior(sdb,info,behavior);
//		} else {
//			info.setID(cID);
//			info.setPlayTotal(0);
//			info.setFavoriteTotal(0);
//			info.setPubTotal(0);
//			result = InsertUserBehavior(sdb,info,behavior);
//		}
//		cur.close();
//		sdb.close();
//		return result;
//	}
//	
//	public List<BehaviorBean> getUserBehaviorList() {
//		List<BehaviorBean> list = new ArrayList<BehaviorBean>();
//		SQLiteDatabase sdb = db.getReadableDatabase();
//		StringBuilder sql = new StringBuilder();
//		sql.append("select * from userbehavior_list where 1");
//		System.out.println("get userbehavior_list sql=" + sql);
//		Cursor cursor = null;
//		try {
//			cursor = sdb.rawQuery(sql.toString(), null);
//			while (cursor.moveToNext()) {
//				BehaviorBean info = new BehaviorBean();
//				info.setID(cursor.getString(cursor.getColumnIndex("cId")));
//				info.setPlayTotal(cursor.getInt(cursor.getColumnIndex("play_total")));
//				info.setFavoriteTotal(cursor.getInt(cursor.getColumnIndex("favorite_total")));
//				info.setPubTotal(cursor.getInt(cursor.getColumnIndex("pub_total")));
//				list.add(info);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (cursor != null && !cursor.isClosed()) {
//				cursor.close();
//				sdb.close();
//				cursor = null;
//			}
//		}
//		return list;
//	}
}
