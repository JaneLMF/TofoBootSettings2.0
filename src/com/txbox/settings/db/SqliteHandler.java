package com.txbox.settings.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHandler extends SQLiteOpenHelper {
	private static final String TABLE_NAME_FAV="history_list";
	final String sql = "create table app_list(id integer,title text(50),"
			+ "percent Integer,url text(200),image text(200),startpos "
			+ "integer, stauts integer,downloadstatus integer,"
			+ "ignored integer,version text,package text(100),size real,installtime timestamp)";
	public SqliteHandler(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sql);
		
		// 创建收藏表
		String bookmarkSql = "CREATE TABLE bookmark_list (id int(11) primary key, vId varcher(64), name varcher(128), image varcher(256), columeId int(8),type varcher(12),updatetime updatetime TIMESTAMP default (datetime('now', 'localtime')))";
		db.execSQL(bookmarkSql);
		
		// 创建crash表
		String crashSql = "CREATE TABLE crash_list (id integer primary key autoincrement,time_added TIMESTAMP default(datetime('now', 'localtime')),descrption text)";
		db.execSQL(crashSql);

		String sql = "CREATE TABLE " + TABLE_NAME_FAV + " (id int(11) primary key, vId varcher(64),vUrl varcher(256), vName varcher(128), vImage varcher(256),vEpisode int(10),vPos int(10),vInfo text, vColumeId int(8),updatetime updatetime TIMESTAMP default (datetime('now', 'localtime')))";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("oldVersion:"+oldVersion+"-----newVersion:"+newVersion);
		if(oldVersion != newVersion){
			// 创建收藏表
			String bookmarkSql="drop table if exists bookmark_list";
			db.execSQL(bookmarkSql);
			bookmarkSql = "CREATE TABLE bookmark_list (id int(11) primary key, vId varcher(64), name varcher(128), image varcher(256), columeId int(8),type varcher(12),updatetime updatetime TIMESTAMP default (datetime('now', 'localtime')))";
			db.execSQL(bookmarkSql);
			
			// 创建crash表
			String crashSql = "drop table if exists crash_list";
			db.execSQL(crashSql);
			crashSql = "CREATE TABLE crash_list (id integer primary key autoincrement,time_added TIMESTAMP default(datetime('now', 'localtime')),descrption text)";
			db.execSQL(crashSql);

			String sql = "drop table if exists " + TABLE_NAME_FAV;
			db.execSQL(sql);
			sql = "CREATE TABLE " + TABLE_NAME_FAV + " (id int(11) primary key, vId varcher(64),vUrl varcher(256), vName varcher(128), vImage varcher(256),vEpisode int(10),vPos int(10),vInfo text, vColumeId int(8),updatetime TIMESTAMP default (datetime('now', 'localtime')))";
			db.execSQL(sql);
		}
	}
}
