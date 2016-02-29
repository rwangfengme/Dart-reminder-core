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
 * Created by gejing on 2/28/16.
 */
public class ScheduleDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SchedulesDB";
    private static final String TABLE_NAME_SCHEDULES = "SCHEDULES";
    private static final int DATABASE_VERSION = 1;

    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_NOTES = "notes";
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

    private static final String[] mColumnList = new String[] { KEY_ROWID,
            KEY_TITLE, KEY_NOTES, KEY_TIME, KEY_LOCATION_NAME,
            KEY_ARRIVE, KEY_RADIUS, KEY_LAT, KEY_LNG, KEY_PRIORITY,
            KEY_REPEAT, KEY_COMPLETED};

    public ScheduleDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SCHEDULES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
    }

    public long insertSchedule(Schedule schedule) {

        ContentValues value = new ContentValues();

        value.put(KEY_TITLE, schedule.getTitle());
        value.put(KEY_NOTES, schedule.getNotes());
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
        Cursor cursor = dbObj.query(true, TABLE_NAME_SCHEDULES, mColumnList,
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
        Cursor cursor = dbObj.query(TABLE_NAME_SCHEDULES, mColumnList, null,
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
}
