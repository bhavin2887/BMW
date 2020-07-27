package com.example.garbagecollection.bmw;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GarbageDataStorage {

	/** Database Opening count is Maintained */
	public static int dbOpenCount=0;

	/** Database Closing count is Maintained */
	public static int dbCloseCount=0;

	/** Object used for creating, insertion into Database object */
	private static SQLiteDatabase sqlitedb;
	private final Context context;					// Context for passing in current classes constructor
	private DatabaseHelper DBHelperObject;			// DatabaseHelper object for getting getting object

	/** Database Name Declaration */
	public static final String DATABASE_NAME = "Garbage.db";

	/** Database Version Number */
	private static final int DATABASE_VERSION = 2;

	/** Inner class object created for synchronizing when BMWDataStorage object is Created */
	private static DatabaseHelper iDbHelper;

	// Devices Table Fields

	public static final String EMPLOYEE= "Employee";
	public static final String VEHICLE= "Vehicle";

	public static final String DATABASE_ROUTE_VISIT= "Route_Visit";
	public static final String DATABASE_DAILY_VISIT= "Daily_Visit";
	public static final String DATABASE_BAGS_VISIT= "Bags_Visit";
	public static final String DATABASE_SUMMARY_VISIT= "Summary_Visit";
	public static final String IS_ON_SERVER= "IsOnServer";
	public static final String HANDSET= "handset";

	public static final String ROUTE= "Route";

	public static final String DailyData= "DailyData";

	public static final String DATATYPE= "Type";

	public static final String ROUTEID= "RouteID";
	public static final String DAILY= "Daily";
	public static final String BAGS= "Bags";
	public static final String SUMMARY= "Summary";


	/** Database Object for Singleton */
	private static GarbageDataStorage instance;
	/**
	 * Getting object of DatabaseHelper
	 * @param ctx
	 */
	public GarbageDataStorage(Context ctx) {
		Log.i("BMWDataStorage","Context");
		this.context = ctx;
		DBHelperObject = new DatabaseHelper(context);
		synchronized (DATABASE_NAME) {
			if (iDbHelper == null)
				iDbHelper = new DatabaseHelper(ctx);
			}
	}

	/**
	 * Opens the database
	 * @return BMWDataStorage object
	 * @throws SQLException
	 */
	public GarbageDataStorage open() {
		try {
			dbOpenCount = dbOpenCount + 1;
			sqlitedb = DBHelperObject.getWritableDatabase();
			System.out.println("BMWDataStorage.open()"+dbOpenCount);
		} catch (SQLException ex) {
			sqlitedb = DBHelperObject.getReadableDatabase();
			Log.d("log_tag", "Exception is Thrown open get Readable");
			ex.printStackTrace();
		}
		return this;
	}

	/**
	 * Closes the database
	 * @return BMWDataStorage object
	 */
	public void close() {
			DBHelperObject.close();
			System.out.println("BMWDataStorage.close()"+dbCloseCount);
	}


	private static final String TABLE_EMP = "CREATE TABLE "+EMPLOYEE+" (employee_id text, employee_name text, isActive text);";
	private static final String TABLE_VEH = "CREATE TABLE "+VEHICLE+" (vehicle_id text, vehicle_regno text, isActive text);";
	private static final String TABLE_ROU= "CREATE TABLE "+ROUTE+" (route_id text, route_name text, isActive text);";
	private static final String TABLE_HAN= "CREATE TABLE "+HANDSET+" (handset_id text, isActive text);";

	private static final String TABLE_ROUTE_HOS_VISIT = "CREATE TABLE "
			+ DATABASE_ROUTE_VISIT + " ("
			+ DailyData + " text,"
			+ DATATYPE + " text,"
			+ IS_ON_SERVER + " text);";

	private static final String TABLE_DAILY_HOS_VISIT = "CREATE TABLE "
			+ DATABASE_DAILY_VISIT + " ("
			+ DailyData + " text,"
			+ DATATYPE + " text,"
			+ IS_ON_SERVER + " text);";

	private static final String TABLE_BAGS_HOS_VISIT = "CREATE TABLE "
			+ DATABASE_BAGS_VISIT+ " ("
			+ DailyData + " text,"
			+ DATATYPE + " text,"
			+ IS_ON_SERVER + " text);";

	private static final String TABLE_SUMMARY_HOS_VISIT = "CREATE TABLE "
			+ DATABASE_SUMMARY_VISIT + " ("
			+ DailyData + " text,"
			+ DATATYPE + " text,"
			+ IS_ON_SERVER + " text);";

	/**
	singleton BamDataStorage.
	 *
	 * @param context
	 * @return BamDataStorage database instance.
	 */
	public static GarbageDataStorage GetFor(Context context) {
		if (instance == null)
			instance = new GarbageDataStorage(context);
		// if (!instance.isOpen())
		// instance.open();
		return instance;
	}

	/**
	 * Creation of Database Tables and Upgradation is done.
	 * @author Bhavin
	 *
	 */
	public static class DatabaseHelper extends SQLiteOpenHelper {
		/**
		 * Get records in a particular table according to sql query
		 * @param tablename
		 * @return a cursor object pointed to the record(s) selected by sql query.
		 */
		public synchronized static Cursor getRecordBySelection(String tablename) {
			return sqlitedb.query(tablename, null, null, null, null, null,null);
		}

		/**
		 * Constructor created for DatabaseHelper
		 * @param context
		 */
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		//	Database Tables are created
		@Override
		public void onCreate(SQLiteDatabase db) {
			//db.execSQL(DATABASE_TEST);
			db.execSQL(TABLE_EMP);
			db.execSQL(TABLE_ROU);
			db.execSQL(TABLE_HAN);
			db.execSQL(TABLE_VEH);
			db.execSQL(TABLE_ROUTE_HOS_VISIT);
			db.execSQL(TABLE_DAILY_HOS_VISIT);
			db.execSQL(TABLE_BAGS_HOS_VISIT);
			db.execSQL(TABLE_SUMMARY_HOS_VISIT);

		}

		// Database Upgradation is done
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("BAMDB", "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS "+EMPLOYEE);
			db.execSQL("DROP TABLE IF EXISTS "+VEHICLE);
			db.execSQL("DROP TABLE IF EXISTS "+ROUTE);
			db.execSQL("DROP TABLE IF EXISTS "+HANDSET);
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_ROUTE_VISIT);
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_DAILY_VISIT);
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_BAGS_VISIT);
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_SUMMARY_VISIT);

			onCreate(db);
		}

	}

	/**
	 * Insert a record into particular table
	 *
	 * @param tablename
	 * @param values
	 * @returnthe row ID of the newly inserted row, or -1 if an error occurred
	 */
	public synchronized long insert(String tablename, ContentValues values) {
		return sqlitedb.insert(tablename, null, values);
	}

	public void deleteAllData(String table){
		sqlitedb.delete(table,null,null);
	}
	/**
	 * Getting all the ProfileNames from the Profile Table
	 * @return cursor
	 */
	public Cursor getTableData(String type) {
		return sqlitedb.rawQuery("select * from "+type, null);
	}

	public Cursor getEmployeeData(String empName){
		String selectQuery = "SELECT employee_id FROM " +EMPLOYEE+ " where employee_name ='"+empName+"'";
		Cursor cursor = sqlitedb.rawQuery(selectQuery, null);
		return cursor;
	}

	public Cursor getVehicleData(String regName){
		String selectQuery = "SELECT vehicle_id FROM " +VEHICLE+ " where vehicle_regno ='"+regName+"'";
		Cursor cursor = sqlitedb.rawQuery(selectQuery, null);
		return cursor;
	}

	public Cursor getRouteData(String rouName){
		String selectQuery = "SELECT route_id FROM " +ROUTE+ " where route_name ='"+rouName+"'";
		Cursor cursor = sqlitedb.rawQuery(selectQuery, null);
		return cursor;
	}

	public Cursor getDataByDataType(String database_name, String type) {
		// Select All Query
		//SELECT * FROM transactions order by rowid limit 1
		String selectQuery = "SELECT * FROM " + database_name + " where "+ DATATYPE + " = '"+ type+"' and "+IS_ON_SERVER+" = 'False'";
		Cursor cursor = sqlitedb.rawQuery(selectQuery, null);
		return cursor;
	}


	public Cursor getDataByDataTypeOneId(String database_name, String type) {
		// Select All Query
		//SELECT * FROM transactions order by rowid limit 1
		String selectQuery = "SELECT * FROM " + database_name + " where "+ DATATYPE + " = '"+ type+"'";
		Cursor cursor = sqlitedb.rawQuery(selectQuery, null);
		return cursor;
	}

	public void UpdateStartID(String dbtable, String type){
		try {
			/*ContentValues cv = new ContentValues();
			cv.put(GarbageDataStorage.IS_ON_SERVER, "True");
			String where = "rowid=(SELECT MIN(rowid) from "+DATABASE_DAILY_VISIT+")";
			sqlitedb.update(DATABASE_DAILY_VISIT, cv, where, null);*/
			String query = "UPDATE "+dbtable+" SET  IsOnServer = 'True' WHERE Type = '"+type+"' and  rowid=( SELECT MIN(rowid) from "+dbtable+")";
			sqlitedb.execSQL(query);
		} catch (SQLException e) {
			Log.d("log_tag", "Exception is Thrown open get Readable");
			e.printStackTrace();
		}
	}

	public void UpdateOneID(String database, String type){
		try {
			String query = "DELETE FROM "+database+" WHERE Type = '"+type+"' and  rowid in (select MIN(rowid) FROM "+database+")";
			sqlitedb.execSQL(query);
		} catch (SQLException e) {
			Log.d("log_tag", "Exception is Thrown open get Readable");
			e.printStackTrace();
		}
	}


}
