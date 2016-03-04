package dartmouth.edu.dartreminder.utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import dartmouth.edu.dartreminder.data.CustomLocation;
import dartmouth.edu.dartreminder.data.DartReminderDBHelper;

/**
 * Created by LeoZhu on 3/3/16.
 */
public class CustomLocationLoader extends AsyncTaskLoader<ArrayList<CustomLocation>> {
    public Context mContext;

    public CustomLocationLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<CustomLocation> loadInBackground() {
        Log.d("TAGG", "Started");
        DartReminderDBHelper dartReminderDBHelper = new DartReminderDBHelper(mContext);
        ArrayList<CustomLocation> locationList = dartReminderDBHelper
                .fetchAllLocations();
        Log.d("TAGG","Finished");

        return locationList;
    }
}
