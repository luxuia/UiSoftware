package com.example.uisoftware;

import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.List;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SQLiteManager {

	private static final String TAG = "SQLiteManager";
	
	
	private MySQLite mySQLite;
	private SQLiteDatabase db;
	
	public SQLiteManager(Context context) {
		mySQLite = new MySQLite(context);
		db = mySQLite.getWritableDatabase();
	}
	
	
	public void add(List<Project> projects) {
		db.beginTransaction();
		try {
			for (Project project:projects) {
				ContentValues cv = new ContentValues();
				
				java.util.Date udata = new java.util.Date();
				
				
				cv.put("time", new Timestamp(udata.getTime()).toString());
				
				
				for (int i = 0; i < 11; i++) {
					cv.put(Constants.keysString[i], project.values[i]);
					Log.e(TAG, "Adding " + Constants.keysString[i] + " : " + project.values[i]);
				}
				
				db.insert(Constants.TABLE_NAME, null, cv);
				
				Log.e(TAG, "Successed insert "+ cv.toString() + " into " + Constants.TABLE_NAME);
				//db.execSQL("INSERT INTO " + Constants.TABLE_NAME + "VALUES(")
			}
			db.setTransactionSuccessful();
		} finally {
			// TODO: handle exception
			db.endTransaction();
		}
	}
	
	
	public void delete(Timestamp date, String arg) {
		
		Log.e(TAG, "delete " + Constants.TABLE_NAME+ " time" + arg + " ?" + date.toString());
		
		db.delete(Constants.TABLE_NAME, "time " + arg + " ?", new String[]{date.toString()});
	}
	
	public List<Project> query(SqlCommond cmd) {
		ArrayList<Project> projects = new ArrayList<Project>();
		Cursor cursor = queryTheCursor(cmd.command);
		while (cursor.moveToNext()) {
			Project project = new Project();
			project._id = cursor.getInt(cursor.getColumnIndex("_id"));
			project.time = cursor.getString((cursor.getColumnIndex("time")));
			
			for (int i = 0; i < 11; i++) {
				project.values[i] = cursor.getInt(cursor.getColumnIndex(Constants.keysString[i]));
			}
			Log.e(TAG, "Successed load " + project.toString());
			
			projects.add(project);
		}
		return projects;
	}
	
	
	public Cursor queryTheCursor(String sql) {
		Cursor cursor = db.rawQuery(sql
				//"SELECT * FROM " + Constants.TABLE_NAME
				, null);
		return cursor;
	}
	
	
	public void closeDB() {
		db.close();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
