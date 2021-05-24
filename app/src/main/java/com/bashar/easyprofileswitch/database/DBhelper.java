package com.bashar.easyprofileswitch.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {

	// TABLE INFORMATTION
	public static final String TABLE_PROFILE = "profile";
	public static final String PROFILE_ID = "_id";
	public static final String PROFILE_NAME = "name";
	public static final String PROFILE_RINGTONE = "ringtone";
	public static final String PROFILE_RING_VOL = "ring_vol";
	public static final String PROFILE_WIFI = "wifi";
	public static final String PROFILE_MOBILEDATA = "data";
	public static final String PROFILE_ALARM = "alarm";
	public static final String PROFILE_SOUND = "sound";
	public static final String PROFILE_IMAGE = "image";

	public static final String PROFILE_START1 = "start1";
	public static final String PROFILE_START2 = "start2";
	public static final String PROFILE_START3 = "start3";
	public static final String PROFILE_START4 = "start4";
	public static final String PROFILE_START5 = "start5";
	public static final String PROFILE_DELAY = "delay";

	public static final String PROFILE_START1_SEL = "start1sel";
	public static final String PROFILE_START2_SEL = "start2sel";
	public static final String PROFILE_START3_SEL = "start3sel";
	public static final String PROFILE_START4_SEL = "start4sel";
	public static final String PROFILE_START5_SEL = "start5sel";
	public static final String PROFILE_DELAY_SEL = "delaysel";

	public static final String PROFILE_AFTER_TIMER = "aftertimer";


		

		// DATABASE INFORMATION
		static final String DB_NAME = "PROFILE.DB";
		static final int DB_VERSION = 1;
		
	// TABLE CREATION STATEMENT
	private static final String CREATE_TABLE = "create table "
			+ TABLE_PROFILE + "(" + PROFILE_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ PROFILE_NAME + " TEXT NOT NULL, "
            + PROFILE_RINGTONE + " TEXT NOT NULL, "
			+ PROFILE_RING_VOL + " TEXT NOT NULL, "
            + PROFILE_WIFI + " TEXT NOT NULL, "
            + PROFILE_MOBILEDATA + " TEXT NOT NULL, "
			+ PROFILE_ALARM + " TEXT NOT NULL, "
            + PROFILE_START1 + " TEXT NOT NULL, "
			+ PROFILE_START2 + " TEXT NOT NULL, "
			+ PROFILE_START3 + " TEXT NOT NULL, "
			+ PROFILE_START4 + " TEXT NOT NULL, "
			+ PROFILE_START5 + " TEXT NOT NULL, "
			+ PROFILE_DELAY + " TEXT NOT NULL, "
			+ PROFILE_START1_SEL + " TEXT NOT NULL, "
			+ PROFILE_START2_SEL + " TEXT NOT NULL, "
			+ PROFILE_START3_SEL + " TEXT NOT NULL, "
			+ PROFILE_START4_SEL + " TEXT NOT NULL, "
			+ PROFILE_START5_SEL + " TEXT NOT NULL, "
			+ PROFILE_DELAY_SEL + " TEXT NOT NULL, "
			+ PROFILE_AFTER_TIMER + " TEXT, "
			+ PROFILE_IMAGE + " INTEGER,"
			+ PROFILE_SOUND + " TEXT);";

	public DBhelper(Context context) {
		super(context, DB_NAME, null,DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
		onCreate(db);
	}
}