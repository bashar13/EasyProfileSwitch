package com.bashar.easyprofileswitch.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SQLController {

	private DBhelper dbhelper;
	private Context ourcontext;
	private SQLiteDatabase database;

	public SQLController(Context c) {
		ourcontext = c;
	}

	public SQLController open() throws SQLException {
		dbhelper = new DBhelper(ourcontext);
		database = dbhelper.getWritableDatabase();
		return this;

	}

	public void close() {
		dbhelper.close();
	}

	public void insertData(String name, String ring, String ring_vol, String alarm, String sound, String wifi, String m_data, int image,
						   String start1, String start2, String start3, String start4, String start5,
						   String sel_s1, String sel_s2, String sel_s3, String sel_s4, String sel_s5,
						   String delay, String sel_delay, String after_timer) {
		ContentValues cv = new ContentValues();
		cv.put(DBhelper.PROFILE_NAME, name);
		cv.put(DBhelper.PROFILE_SOUND, sound);
		cv.put(DBhelper.PROFILE_IMAGE, image);
		cv.put(DBhelper.PROFILE_RINGTONE, ring);
		cv.put(DBhelper.PROFILE_RING_VOL, ring_vol);
		cv.put(DBhelper.PROFILE_WIFI, wifi);
		cv.put(DBhelper.PROFILE_MOBILEDATA, m_data);
		cv.put(DBhelper.PROFILE_ALARM, alarm);
		cv.put(DBhelper.PROFILE_START1, start1);
		cv.put(DBhelper.PROFILE_START2, start2);
		cv.put(DBhelper.PROFILE_START3, start3);
		cv.put(DBhelper.PROFILE_START4, start4);
		cv.put(DBhelper.PROFILE_START5, start5);
		cv.put(DBhelper.PROFILE_DELAY, delay);
		cv.put(DBhelper.PROFILE_START1_SEL, sel_s1);
		cv.put(DBhelper.PROFILE_START2_SEL, sel_s2);
		cv.put(DBhelper.PROFILE_START3_SEL, sel_s3);
		cv.put(DBhelper.PROFILE_START4_SEL, sel_s4);
		//cv.put(DBhelper.PROFILE_AFTER_TIMER, after_timer);
		cv.put(DBhelper.PROFILE_START5_SEL, sel_s5);
		cv.put(DBhelper.PROFILE_DELAY_SEL, sel_delay);
		database.insert(DBhelper.TABLE_PROFILE, null, cv);
	}

	public Cursor readData() {
		String[] allColumns = new String[] { DBhelper.PROFILE_ID, DBhelper.PROFILE_NAME,
				DBhelper.PROFILE_RINGTONE, DBhelper.PROFILE_RING_VOL, DBhelper.PROFILE_ALARM, DBhelper.PROFILE_SOUND,
				DBhelper.PROFILE_WIFI, DBhelper.PROFILE_MOBILEDATA, DBhelper.PROFILE_IMAGE,
				DBhelper.PROFILE_START1, DBhelper.PROFILE_START2, DBhelper.PROFILE_START3,
				DBhelper.PROFILE_START4, DBhelper.PROFILE_START5,
				DBhelper.PROFILE_START1_SEL, DBhelper.PROFILE_START2_SEL, DBhelper.PROFILE_START3_SEL,
				DBhelper.PROFILE_START4_SEL, DBhelper.PROFILE_START5_SEL,
				DBhelper.PROFILE_DELAY, DBhelper.PROFILE_DELAY_SEL};
		Cursor c = database.query(DBhelper.TABLE_PROFILE, allColumns, null,
				null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	public int updateData(long profileID, String name, String ring, String ring_vol, String alarm, String sound, String wifi, String m_data, int image) {
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(DBhelper.PROFILE_NAME, name);
		cvUpdate.put(DBhelper.PROFILE_SOUND, sound);
		cvUpdate.put(DBhelper.PROFILE_IMAGE, image);
		cvUpdate.put(DBhelper.PROFILE_RINGTONE, ring);
        cvUpdate.put(DBhelper.PROFILE_RING_VOL, ring_vol);
		cvUpdate.put(DBhelper.PROFILE_WIFI, wifi);
		cvUpdate.put(DBhelper.PROFILE_MOBILEDATA, m_data);
		cvUpdate.put(DBhelper.PROFILE_ALARM, alarm);
		int i = database.update(DBhelper.TABLE_PROFILE, cvUpdate,
				DBhelper.PROFILE_ID + " = " + profileID, null);
		return i;
	}

	public int updateProfileSchedule(long profileId, int childPosition, String select) {
		ContentValues cvUpdate =  new ContentValues();
		if(childPosition == 0) {
			cvUpdate.put(DBhelper.PROFILE_DELAY_SEL, select);
		}
		else if(childPosition == 2) {
			cvUpdate.put(DBhelper.PROFILE_START1_SEL, select);
		}
		else if(childPosition == 3) {
			cvUpdate.put(DBhelper.PROFILE_START2_SEL, select);
		}
		else if(childPosition == 4) {
			cvUpdate.put(DBhelper.PROFILE_START3_SEL, select);
		}
		else if(childPosition == 5) {
			cvUpdate.put(DBhelper.PROFILE_START4_SEL, select);
		}
		else if(childPosition == 6) {
			cvUpdate.put(DBhelper.PROFILE_START5_SEL, select);
		}

		int i = database.update(DBhelper.TABLE_PROFILE, cvUpdate,
				DBhelper.PROFILE_ID + " = " + profileId, null);
		return i;

	}

	public int updateProfileScheduleValue(long profileId, int childPosition, String value) {
		ContentValues cvUpdate =  new ContentValues();
		if(childPosition == 0) {
			cvUpdate.put(DBhelper.PROFILE_DELAY, value);
		}
		else if(childPosition == 1) {
			cvUpdate.put(DBhelper.PROFILE_AFTER_TIMER, value);
		}
		else if(childPosition == 2) {
			cvUpdate.put(DBhelper.PROFILE_START1, value);
		}
		else if(childPosition == 3) {
			cvUpdate.put(DBhelper.PROFILE_START2, value);
		}
		else if(childPosition == 4) {
			cvUpdate.put(DBhelper.PROFILE_START3, value);
		}
		else if(childPosition == 5) {
			cvUpdate.put(DBhelper.PROFILE_START4, value);
		}
		else if(childPosition == 6) {
			cvUpdate.put(DBhelper.PROFILE_START5, value);
		}

		int i = database.update(DBhelper.TABLE_PROFILE, cvUpdate,
				DBhelper.PROFILE_ID + " = " + profileId, null);
		return i;

	}

	public void deleteData(int profileID) {
		database.delete(DBhelper.TABLE_PROFILE, DBhelper.PROFILE_ID + "="
				+ profileID, null);
	}

	public void deleteTable()
	{
		database.delete(DBhelper.TABLE_PROFILE, null, null);
		database.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + DBhelper.TABLE_PROFILE + "'");
		//database.delete("SQLITE_SEQUENCE", "NAME = ?", new String[]{DBhelper.TABLE_PROFILE});
	}

	public Cursor readSpecificData(String tableName, int row_id) {
		String[] allColumns = new String[] { DBhelper.PROFILE_ID,
				DBhelper.PROFILE_NAME, DBhelper.PROFILE_RINGTONE, DBhelper.PROFILE_RING_VOL,
                DBhelper.PROFILE_ALARM, DBhelper.PROFILE_SOUND, DBhelper.PROFILE_WIFI,
				DBhelper.PROFILE_MOBILEDATA, DBhelper.PROFILE_IMAGE, DBhelper.PROFILE_DELAY_SEL, DBhelper.PROFILE_DELAY, DBhelper.PROFILE_AFTER_TIMER };
		try {
			Cursor c = database.query(tableName, allColumns, DBhelper.PROFILE_ID
					+ "=" + row_id, null, null, null, null);
			int count = c.getCount();
			if (c != null) {
				c.moveToFirst();
			}
			return c;
		} catch (Exception e) {
		}

		return null;
	}

	public Cursor readSpecificDataTime(String tableName, String profile_name) {
		String[] allColumns = new String[] { DBhelper.PROFILE_ID,
				DBhelper.PROFILE_NAME, DBhelper.PROFILE_DELAY, DBhelper.PROFILE_START1,
				DBhelper.PROFILE_START2, DBhelper.PROFILE_START3,
				DBhelper.PROFILE_START4, DBhelper.PROFILE_START5 };
		try {
			Cursor c = database.query(tableName, allColumns, DBhelper.PROFILE_NAME
					+ "=" + profile_name, null, null, null, null);
			int count = c.getCount();
			if (c != null) {
				c.moveToFirst();
			}
			return c;
		} catch (Exception e) {
		}

		return null;
	}

	public int getcount(String tableName) {
		String table = "select * from ";
		table = table + tableName;
		try {
			Cursor c = database.rawQuery(table, null);
			return c.getCount();
		} catch (Exception E) {
			// Toast.makeText(this, "message insert", 2000).show();
		}
		return 0;
	}

}// outer class end
