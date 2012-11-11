package com.example.uisoftware;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static android.provider.BaseColumns._ID;

public class MySQLite extends SQLiteOpenHelper{

	private static final String DATABASE_NAME = "UIData.db";
	private static final int DATABASE_VERSION = 1;
	
	public MySQLite(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	

	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//       _ID, LEU, NIT, UBG, PRO, PH, BLD, SG, KET, BIL, GLU, VC 
		String sqlString = 	"CREATE TABLE "+ Constants.TABLE_NAME
				+"("
				+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
				+Constants.TITLE+" TEXT,"
				+Constants.TIME	+" DATETIME NOT NULL,"
				
				+Constants.LEU	+" INTEGER,"
				+Constants.NIT	+" INTEGER,"
				+Constants.UBG	+" INTEGER,"
				
				+Constants.PRO	+" INTEGER,"
				+Constants.PH 	+" INTEGER,"
				+Constants.BLD	+" INTEGER,"
				
				+Constants.SG 	+" INTEGER,"
				+Constants.KET	+" INTEGER,"
				+Constants.BIL	+" INTEGER,"
				
				+Constants.GLU	+" INTEGER,"
				+Constants.VC 	+" INTEGER);";
		
		db.execSQL(sqlString);  		
				
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS" + Constants.TABLE_NAME);
		onCreate(db);
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	

}
