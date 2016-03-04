package dartmouth.edu.dartreminder.utils;

import dartmouth.edu.dartreminder.R;

/**
 * Created by gejing on 2/28/16.
 */
public abstract class Globals {
    public static final String TABLE_NAME_SCHEDULES = "SCHEDULES";
    public static final String TABLE_NAME_CUSTOM_LOCATIONS = "CUSTOM_LOCATIONS";
    public static final String TABLE_NAME_USER_ACCOUNTS = "USER_ACCOUNTS";

    public static final String TAG = "DartReminder";
    public static final String[] PRIORITIES = {"None", "Important", "Very Important"};
    public static final String[] REPEAT = {"Never", "Every Day", "Every Week", "Every Month", "Every Year"};
    public static final int[] LOCATION_LIST = {R.drawable.ic_add_location_black_48dp, R.drawable.arraw_icon, R.drawable.arraw_icon,
                                            R.drawable.arraw_icon, R.drawable.arraw_icon, R.drawable.arraw_icon};

    public static final String SAVE = "save";
    public static final String LOCATION_NAME = "location_name";
    public static final String ARRIVE = "arrive";
    public static final String RADIUS = "radius";
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String DEFAULT_LOCATION_NAME = "User Selected Location";

    public static final String ADD_LOCATION = "add_location";
    public static final String LOCATION_TITLE = "location_title";
    public static final String LOCATION_DETAIL = "location_detail";
    public static final String LOCATION_LAT = "location_lat";
    public static final String LOCATION_LNG = "location_lng";
}
