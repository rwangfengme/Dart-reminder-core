package dartmouth.edu.dartreminder.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by gejing on 3/2/16.
 */
public class DartReminderDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "DartReminderDB";
    private static final int DATABASE_VERSION = 1;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SCHEDULES);
        db.execSQL(CREATE_TABLE_CUSTOM_LOCATIONS);
        db.execSQL(CREATE_TABLE_USER_ACCOUNTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
    }

    public DartReminderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // part I: Schedule
    private static final String TABLE_NAME_SCHEDULES = "SCHEDULES";

    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_NOTES = "notes";
    public static final String KEY_USE_TIME = "use_time";
    public static final String KEY_TIME = "time";
    public static final String KEY_LOCATION_NAME = "location_name";
    public static final String KEY_ARRIVE = "arrive";
    public static final String KEY_RADIUS = "radius";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LNG = "lng";
    public static final String KEY_PRIORITY = "priority";
    public static final String KEY_REPEAT = "repeat";
    public static final String KEY_COMPLETED = "completed";

    private static final String CREATE_TABLE_SCHEDULES = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME_SCHEDULES
            + " ("
            + KEY_ROWID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_TITLE
            + " TEXT, "
            + KEY_NOTES
            + " TEXT, "
            + KEY_USE_TIME
            + " INTEGER, "
            + KEY_TIME
            + " INTEGER NOT NULL, "
            + KEY_LOCATION_NAME
            + " TEXT, "
            + KEY_ARRIVE
            + " INTEGER, "
            + KEY_RADIUS
            + " FLOAT, "
            + KEY_LAT
            + " FLOAT,"
            + KEY_LNG
            + " FLOAT, "
            + KEY_PRIORITY
            + " INTEGER, "
            + KEY_REPEAT
            + " INTEGER, "
            + KEY_COMPLETED
            + " INTEGER );";

    private static final String[] mColumnList_Schedule = new String[] { KEY_ROWID,
            KEY_TITLE, KEY_NOTES, KEY_USE_TIME, KEY_TIME, KEY_LOCATION_NAME,
            KEY_ARRIVE, KEY_RADIUS, KEY_LAT, KEY_LNG, KEY_PRIORITY,
            KEY_REPEAT, KEY_COMPLETED};

    public long insertSchedule(Schedule schedule) {

        ContentValues value = new ContentValues();

        value.put(KEY_TITLE, schedule.getTitle());
        value.put(KEY_NOTES, schedule.getNotes());
        value.put(KEY_USE_TIME, schedule.getUseTime() ? 1 : 0);
        value.put(KEY_TIME, schedule.getTime());
        value.put(KEY_LOCATION_NAME, schedule.getLocationName());
        value.put(KEY_ARRIVE, schedule.getArrive() ? 1 : 0);
        value.put(KEY_RADIUS, schedule.getRadius());
        value.put(KEY_LAT, schedule.getLat());
        value.put(KEY_LNG, schedule.getLng());
        value.put(KEY_PRIORITY, schedule.getPriority());
        value.put(KEY_REPEAT, schedule.getRepeat());
        value.put(KEY_COMPLETED, schedule.getCompleted() ? 1 : 0);

        SQLiteDatabase dbObj = getWritableDatabase();
        long id = dbObj.insert(TABLE_NAME_SCHEDULES, null, value);
        dbObj.close();

        return id;
    }

    public void removeSchedule(long rowIndex) {
        SQLiteDatabase dbObj = getWritableDatabase();
        dbObj.delete(TABLE_NAME_SCHEDULES, KEY_ROWID + "=" + rowIndex, null);
        dbObj.close();
    }

    public Schedule fetchScheduleByIndex(long rowId) throws SQLException {
        SQLiteDatabase dbObj = getReadableDatabase();
        Schedule schedule = null;
        Cursor cursor = dbObj.query(true, TABLE_NAME_SCHEDULES, mColumnList_Schedule,
                KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            schedule = cursorToSchedule(cursor, true);
        }
        cursor.close();
        dbObj.close();
        return schedule;
    }

    public ArrayList<Schedule> fetchSchedules() {
        SQLiteDatabase dbObj = getReadableDatabase();
        ArrayList<Schedule> list = new ArrayList<Schedule>();
        Cursor cursor = dbObj.query(TABLE_NAME_SCHEDULES, mColumnList_Schedule, null,
                null, null, null, null);

        while (cursor.moveToNext()) {
            Schedule schedule = cursorToSchedule(cursor, false);
            list.add(schedule);
            Log.d("TAGG", "Got data");
        }

        cursor.close();
        dbObj.close();

        return list;
    }

    private Schedule cursorToSchedule(Cursor cursor, boolean b) {
        Schedule schedule = new Schedule();
        schedule.setId(cursor.getLong(cursor.getColumnIndex(KEY_ROWID)));
        schedule.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
        schedule.setNotes(cursor.getString(cursor.getColumnIndex(KEY_NOTES)));
        schedule.setUseTime(cursor.getInt(cursor.getColumnIndex(KEY_USE_TIME)) == 1);
        schedule.setTime(cursor.getLong(cursor.getColumnIndex(KEY_TIME)));
        schedule.setLocationName(cursor.getString(cursor.getColumnIndex(KEY_LOCATION_NAME)));
        schedule.setArrive(cursor.getInt(cursor.getColumnIndex(KEY_ARRIVE)) == 1);
        schedule.setRadius(cursor.getDouble(cursor.getColumnIndex(KEY_RADIUS)));
        schedule.setLat(cursor.getDouble(cursor.getColumnIndex(KEY_LAT)));
        schedule.setLng(cursor.getDouble(cursor.getColumnIndex(KEY_LNG)));
        schedule.setPriority(cursor.getInt(cursor.getColumnIndex(KEY_PRIORITY)));
        schedule.setRepeat(cursor.getInt(cursor.getColumnIndex(KEY_REPEAT)));
        schedule.setCompleted(cursor.getInt(cursor.getColumnIndex(KEY_COMPLETED)) == 1);
        return schedule;
    }

    // part II: Location
    private static final String TABLE_NAME_CUSTOM_LOCATIONS = "CUSTOM_LOCATIONS";
    public static final String KEY_LOCATION_ROWID = "_id";
    public static final String KEY_LOCATION_TITLE = "title";
    public static final String KEY_LOCATION_LAT = "latitude";
    public static final String KEY_LOCATION_LNG = "longitude";
    public static final String KEY_LOCATION_DETAIL = "detail";

    private static final String CREATE_TABLE_CUSTOM_LOCATIONS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME_CUSTOM_LOCATIONS
            + " ("
            + KEY_LOCATION_ROWID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_LOCATION_TITLE
            + " TEXT, "
            + KEY_LOCATION_LAT
            + " FLOAT,"
            + KEY_LOCATION_LNG
            + " FLOAT, "
            + KEY_LOCATION_DETAIL
            + " TEXT );";

    private static final String[] mColumnList_Location = new String[] { KEY_LOCATION_ROWID,
            KEY_LOCATION_TITLE, KEY_LOCATION_LAT, KEY_LOCATION_LNG, KEY_LOCATION_DETAIL};

    public long insertCustomLocation(CustomLocation cs) {

        ContentValues value = new ContentValues();

        value.put(KEY_LOCATION_TITLE, cs.getTitle());
        value.put(KEY_LOCATION_LAT, cs.getLatitude());
        value.put(KEY_LOCATION_LNG, cs.getLongitude());
        value.put(KEY_LOCATION_DETAIL, cs.getDetail());

        SQLiteDatabase dbObj = getWritableDatabase();
        long id = dbObj.insert(TABLE_NAME_CUSTOM_LOCATIONS, null, value);
        dbObj.close();

        return id;
    }

    public void removeCustomLocation(long rowIndex) {
        SQLiteDatabase dbObj = getWritableDatabase();
        dbObj.delete(TABLE_NAME_CUSTOM_LOCATIONS, KEY_LOCATION_ROWID + "=" + rowIndex, null);
        dbObj.close();
    }

    public CustomLocation fetchLocationByIndex(long rowId) throws SQLException {
        SQLiteDatabase dbObj = getReadableDatabase();
        CustomLocation cs = null;
        Cursor cursor = dbObj.query(true, TABLE_NAME_CUSTOM_LOCATIONS, mColumnList_Location,
                KEY_LOCATION_ROWID + "=" + rowId, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            cs = cursorToLocation(cursor, true);
        }
        cursor.close();
        dbObj.close();
        return cs;
    }

    public ArrayList<CustomLocation> fetchAllLocations() {
        SQLiteDatabase dbObj = getReadableDatabase();
        ArrayList<CustomLocation> list = new ArrayList<CustomLocation>();
        Cursor cursor = dbObj.query(TABLE_NAME_CUSTOM_LOCATIONS, mColumnList_Location, null,
                null, null, null, null);
        while (cursor.moveToNext()) {
            CustomLocation cs = cursorToLocation(cursor, false);
            list.add(cs);
            Log.d("TAGG", "Got data");
        }

        cursor.close();
        dbObj.close();

        return list;
    }

    private CustomLocation cursorToLocation(Cursor cursor, boolean b) {
        CustomLocation cs = new CustomLocation("");
        cs.setId(cursor.getLong(cursor.getColumnIndex(KEY_LOCATION_ROWID)));
        cs.setTitle(cursor.getString(cursor.getColumnIndex(KEY_LOCATION_TITLE)));
        cs.setLatitude(cursor.getDouble(cursor.getColumnIndex(KEY_LOCATION_LAT)));
        cs.setLongtitude(cursor.getDouble(cursor.getColumnIndex(KEY_LOCATION_LNG)));
        cs.setDetail(cursor.getString(cursor.getColumnIndex(KEY_LOCATION_DETAIL)));
        return cs;
    }

    // part III: user account
    private static final String TABLE_NAME_USER_ACCOUNTS = "USER_ACCOUNTS";

    public static final String KEY_USER_ROWID = "_id";
    public static final String KEY_USER_NAME = "email";
    public static final String KEY_USER_PASSWORD = "password";

    private static final String CREATE_TABLE_USER_ACCOUNTS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME_USER_ACCOUNTS
            + " ("
            + KEY_USER_ROWID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_USER_NAME
            + " TEXT, "
            + KEY_USER_PASSWORD
            + " TEXT );";

    private static final String[] mColumnList_User = new String[] { KEY_USER_ROWID,
            KEY_USER_NAME, KEY_USER_PASSWORD};

    public long insertUser(UserAccount user) {

        ContentValues value = new ContentValues();

        value.put(KEY_USER_NAME, user.getUsername());
        value.put(KEY_USER_PASSWORD, user.getPassword());

        SQLiteDatabase dbObj = getWritableDatabase();
        long id = dbObj.insert(TABLE_NAME_USER_ACCOUNTS, null, value);
        dbObj.close();

        return id;
    }

    public UserAccount fetchUserByName(String user_name) throws SQLException {
        String query = "Select _id, password from USER_ACCOUNTS where email ='"+user_name+"'";
        UserAccount user = new UserAccount(0, user_name, "");
        SQLiteDatabase dbObj = getReadableDatabase();
        Cursor cursor = dbObj.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                user.setUserId(cursor.getLong(cursor.getColumnIndex(KEY_ROWID)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(KEY_USER_PASSWORD)));
            } while (cursor.moveToNext());
        }
        return user;
    }
}