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

public class CustomLocationDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DartReminderDB";
    private static final String TABLE_NAME_CUSTOM_LOCATIONS = "CUSTOM_LOCATIONS";
    private static final int DATABASE_VERSION = 1;

    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_LAT = "latitude";
    public static final String KEY_LNG = "longitude";
    public static final String KEY_DETAIL = "detail";

    private static final String CREATE_TABLE_CUSTOM_LOCATIONS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME_CUSTOM_LOCATIONS
            + " ("
            + KEY_ROWID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_TITLE
            + " TEXT, "
            + KEY_LAT
            + " FLOAT,"
            + KEY_LNG
            + " FLOAT, "
            + KEY_DETAIL
            + " TEXT );";

    private static final String[] mColumnList = new String[] { KEY_ROWID,
            KEY_TITLE, KEY_LAT, KEY_LNG, KEY_DETAIL};

    public CustomLocationDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CUSTOM_LOCATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
    }

    public long insertCustomLocation(CustomLocation cs) {

        ContentValues value = new ContentValues();

        value.put(KEY_TITLE, cs.getTitle());
        value.put(KEY_LAT, cs.getLatitude());
        value.put(KEY_LNG, cs.getLongitude());
        value.put(KEY_DETAIL, cs.getDetail());

        SQLiteDatabase dbObj = getWritableDatabase();
        long id = dbObj.insert(TABLE_NAME_CUSTOM_LOCATIONS, null, value);
        dbObj.close();

        return id;
    }

    public void removeCustomLocation(long rowIndex) {
        SQLiteDatabase dbObj = getWritableDatabase();
        dbObj.delete(TABLE_NAME_CUSTOM_LOCATIONS, KEY_ROWID + "=" + rowIndex, null);
        dbObj.close();
    }

    public CustomLocation fetchLocationByIndex(long rowId) throws SQLException {
        SQLiteDatabase dbObj = getReadableDatabase();
        CustomLocation cs = null;
        Cursor cursor = dbObj.query(true, TABLE_NAME_CUSTOM_LOCATIONS, mColumnList,
                KEY_ROWID + "=" + rowId, null, null, null, null, null);
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
        Cursor cursor = dbObj.query(TABLE_NAME_CUSTOM_LOCATIONS, mColumnList, null,
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
        cs.setId(cursor.getLong(cursor.getColumnIndex(KEY_ROWID)));
        cs.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
        cs.setLatitude(cursor.getDouble(cursor.getColumnIndex(KEY_LAT)));
        cs.setLongtitude(cursor.getDouble(cursor.getColumnIndex(KEY_LNG)));
        cs.setDetail(cursor.getString(cursor.getColumnIndex(KEY_DETAIL)));
        return cs;
    }
}
