package ca.ualberta.cs.completemytask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_TASKS = "tasks";
	public static final String TABLE_COMMENTS = "comments";
	public static final String TABLE_PHOTOS = "photos";

	// Common
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_PARENT_ID = "parentId";
	public static final String COLUMN_GLOBALID = "globalId";
	public static final String COLUMN_PARENT_GLOBALID = "parentGlobalId";
	public static final String COLUMN_USER = "user";
	public static final String COLUMN_EMAIL = "email";

	// Tasks
	public static final String COLUMN_TASK_NAME = "name";
	public static final String COLUMN_TASK_DESCRIPTION = "description";
	public static final String COLUMN_TASK_COMPLETE = "complete";
	public static final String COLUMN_TASK_NEEDS_COMMENT = "comment";
	public static final String COLUMN_TASK_NEEDS_PHOTO = "photo";
	public static final String COLUMN_TASK_NEEDS_AUDIO = "audio";

	// Comments
	public static final String COLUMN_COMMENT = "comment";
	
	// Photos
	public static final String COLUMN_PHOTO = "photo";

	private static final String DATABASE_NAME = "localSave.db";
	private static final int DATABASE_VERSION = 3;

	// Database creation sql statement
	private static final String DATABASE_CREATE_TASKS = "create table "
			+ TABLE_TASKS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_GLOBALID
			+ " text, " + COLUMN_TASK_NAME + " text, "
			+ COLUMN_TASK_DESCRIPTION + " text, " + COLUMN_TASK_COMPLETE
			+ " integer, " + COLUMN_TASK_NEEDS_COMMENT + " integer, "
			+ COLUMN_TASK_NEEDS_PHOTO + " integer, " + COLUMN_TASK_NEEDS_AUDIO
			+ " integer, " 
			+ COLUMN_USER + " text,"
			+ COLUMN_EMAIL + " text);";

	private static final String DATABASE_CREATE_COMMENTS = "create table "
			+ TABLE_COMMENTS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_PARENT_ID
			+ " integer, " + COLUMN_GLOBALID + " text, "
			+ COLUMN_PARENT_GLOBALID + " text, " + COLUMN_COMMENT + " text, " +
			COLUMN_USER + " text);";
	
	private static final String DATABASE_CREATE_PHOTOS = "create table "
			+ TABLE_PHOTOS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_PARENT_ID
			+ " integer, " + COLUMN_GLOBALID + " text, "
			+ COLUMN_PARENT_GLOBALID + " text, " + COLUMN_PHOTO + " text, " +
			COLUMN_USER + " text);";

	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE_TASKS);
		db.execSQL(DATABASE_CREATE_COMMENTS);
		db.execSQL(DATABASE_CREATE_PHOTOS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SQLiteHelper.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);

		onCreate(db);
	}

}
