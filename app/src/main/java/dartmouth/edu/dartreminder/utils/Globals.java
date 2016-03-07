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
    public static final String[] ACTIVITIES = {"Standing", "Walking", "Running"};

    public static final String SAVE = "save";
    public static final String SCHEDULE_ID = "id";
    public static final String INTENT_TYPE = "type";
    public static final int TIME_INTENT = 0;
    public static final int LOCATION_INTENT = 1;
    public static final int ACTIVITY_INTENT = 2;


    public static final String LOCATION_NAME = "location_name";
    public static final String ARRIVE = "arrive";
    public static final String RADIUS = "radius";
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String DEFAULT_LOCATION_NAME = "User Selected Location";

    public static final String UI_TAB_ALL = "ALL";
    public static final String UI_TAB_MAP = "Location";
    public static final String UI_TAB_ACTIVITY = "Activity";

    public static final String ADD_LOCATION = "add_location";
    public static final String LOCATION_TITLE = "location_title";
    public static final String LOCATION_DETAIL = "location_detail";
    public static final String LOCATION_LAT = "location_lat";
    public static final String LOCATION_LNG = "location_lng";

    public static final String ACT_TITLE = "act_tilte";
    public static final String ACT_TYPE = "act_type";

    public static final String SCHEDULE_TITLE = "schedule_title";
    public static final String SCHEDULE_NOTE = "schedule_note";

    public static final String MSG_LOCATION_CHANGE = "location_changed";
    public static final String MSG_LOCATION_ALARM = "location_alarm";

    public static final String SERVER_ADDR = "https://4-dot-lexical-micron-124201.appspot.com";
//    public static final String SERVER_ADDR = "http://10.31.181.11:8080";

    public static final String GCM_AR = "Activity Recognition";
    public static final String AR_BROADCAST = "AR_BROADCAST";
    public static final int DETECTION_INTERVAL_IN_MILLISECONDS = 1000;
    public static final long[] mVibratePattern = new long[]{0, 500, 500};

    public static final int ACCELEROMETER_BUFFER_CAPACITY = 2048;
    public static final int ACCELEROMETER_BLOCK_CAPACITY = 64;


    public static final String CLASS_LABEL_KEY = "label";
    public static final String CLASS_LABEL_STANDING = "Standing";
    public static final String CLASS_LABEL_WALKING = "Walking";
    public static final String CLASS_LABEL_RUNNING = "Running";
    public static final String CLASS_LABEL_OTHER = "others";

    public static final String FEAT_FFT_COEF_LABEL = "fft_coef_";
    public static final String FEAT_MAX_LABEL = "max";
    public static final String FEAT_SET_NAME = "accelerometer_features";

    public static final String FEATURE_FILE_NAME = "features.arff";
    public static final String RAW_DATA_NAME = "raw_data.txt";
    public static final int FEATURE_SET_CAPACITY = 10000;

}
