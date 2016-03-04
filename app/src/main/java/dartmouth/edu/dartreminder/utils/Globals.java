package dartmouth.edu.dartreminder.utils;

import dartmouth.edu.dartreminder.R;

/**
 * Created by gejing on 2/28/16.
 */
public abstract class Globals {
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

    public static final String UI_TAB_TIME = "Time";
    public static final String UI_TAB_MAP = "Location";
    public static final String UI_TAB_ACTIVITY = "Activity";
}
