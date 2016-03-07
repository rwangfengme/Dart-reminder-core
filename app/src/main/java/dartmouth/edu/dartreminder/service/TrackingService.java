package dartmouth.edu.dartreminder.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.data.DartReminderDBHelper;
import dartmouth.edu.dartreminder.data.Schedule;
import dartmouth.edu.dartreminder.utils.Globals;
import dartmouth.edu.dartreminder.view.LocationDetailActivity;
import dartmouth.edu.dartreminder.view.MainActivity;
import dartmouth.edu.dartreminder.view.MapsActivity;

/**
 * Created by LeoZhu on 3/3/16.
 */

public class TrackingService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status> {

    public static final String EXTRA_MESSENGER = "EXTRA_MESSENGER";
    public static final String EXTRA_TRACKING = "EXTRA_TRACKING";
    public static final String MSG_ENTITY_UPDATE = "update";

    private int mInputType;

    // A request to connect to Location Services
    private LocationRequest mLocationRequest;

    // Stores the current instantiation of the location client in this object
    private GoogleApiClient mGoogleApiClient;

    // Set up binder for the TrackingService using IBinder
    private final IBinder binder = new TrackingServiceBinder();

    // Store all the uncompleted location based schedules
    private List<Schedule> mScheduleList;

    private NotificationManager mNotificationManager;

    // service started flag
    private boolean mIsStarted;

    private DartReminderDBHelper dartReminderDBHelper;
    private LatLng mCurrentLatLng;

    @Override
    public void onResult(Status status) {
        if (status.isSuccess()) {
            // Toggle the status of activity updates requested, and save in shared preferences.
        }
    }

    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(this, ActivityRecognitionService.class);

        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // requestActivityUpdates() and removeActivityUpdates().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // set up the MyRunsBinder
    public class TrackingServiceBinder extends Binder {
        public List<Schedule> getScheduleList() {
            return mScheduleList;
        }

        public TrackingService getService() {
            return TrackingService.this;
        }

        public LatLng getCurrentLatLng() {
            return mCurrentLatLng;
        }
    }

    @Override
    public void onCreate() {
        mIsStarted = false;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        dartReminderDBHelper = new DartReminderDBHelper(getApplicationContext());
        initScheduleList();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        start(intent);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        start(intent);
        return binder;
    }

    @Override
    public void onDestroy() {
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        mNotificationManager.cancelAll();
        mIsStarted = false;
        Log.d(Globals.TAG, "Service Destroyed");
        super.onDestroy();
    }

    // start tracking
    private void start(Intent intent) {
        if (mIsStarted) {
            return;
        }
        mIsStarted = true;

        // notification
        setupNotification();

        // Connect the client.
        mGoogleApiClient.connect();
    }

    // fetching uncompleted task from database
    private void initScheduleList() {
        mScheduleList = dartReminderDBHelper.fetchUncompletedLocationSchedules();
        if (mScheduleList == null){
            mScheduleList = new ArrayList<>();
        }
    }

    // send update to the activity
    private void sendUpdate(Schedule schedule, int i) {
        mScheduleList.remove(i);
        schedule.setCompleted(true);
        dartReminderDBHelper.updateSchedule(schedule);
        Intent intent = new Intent(
                MainActivity.ScheduleTriggeredReceiver.class.getName());
//        long id = -1;
//        if(schedule.getId() != null)
//            id = schedule.getId();
//        intent.putExtra(Globals.SCHEDULE_ID, (int)id);
        intent.putExtra(MSG_ENTITY_UPDATE, true);
        intent.putExtra(Globals.SCHEDULE_TITLE, schedule.getTitle());
        intent.putExtra(Globals.SCHEDULE_NOTE, schedule.getNotes());
        intent.putExtra(Globals.LOCATION_TITLE, schedule.getLocationName());
        intent.putExtra(Globals.LOCATION_LAT, schedule.getLat());
        intent.putExtra(Globals.LOCATION_LNG, schedule.getLng());
        this.sendBroadcast(intent);
    }

    private void sendLocationChange(Location location){
        Intent intent = new Intent(
                LocationDetailActivity.LocationChangedReceiver.class.getName());
        intent.putExtra(Globals.MSG_LOCATION_CHANGE, true);
        intent.putExtra(Globals.LOCATION_LAT, location.getLatitude());
        intent.putExtra(Globals.LOCATION_LNG, location.getLongitude());
        this.sendBroadcast(intent);
    }

    private void setupNotification() {
        // ----------------------Skeleton--------------------------
        // Setup the intent to fire MapDisplayActivity for the PendingIntent
        Intent i = new Intent(this, MainActivity.class);

        // ----------------------Skeleton--------------------------
        // Set flags to avoid re-invent activity.
        // http://developer.android.com/guide/topics/manifest/activity-element.html#lmode
        // IMPORTANT!. no re-create activity
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // ----------------------Skeleton--------------------------
        // Using pending intent to bring back the MapActivity from notification
        // center.
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

        // ----------------------Skeleton--------------------------

        // Use NotificationManager to build notification(icon, content, title,
        // flag and pIntent)

        Notification notification = new Notification.Builder(this)
                .setContentTitle(
                        getString(R.string.ui_maps_display_notification_title))
                .setContentText(
                        getString(R.string.ui_maps_display_notification_content))
                .setSmallIcon(R.drawable.arraw_icon).setContentIntent(pi).build();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification.flags = notification.flags
                | Notification.FLAG_ONGOING_EVENT;

        mNotificationManager.notify(0, notification);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(Globals.TAG, "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(Globals.TAG, "GoogleApiClient connection has failed");
    }

    // start location update when location service is connected
    public void onConnected(Bundle arg0) {
        Log.e("onConnected", "onConnected");
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(
                mGoogleApiClient,
                Globals.DETECTION_INTERVAL_IN_MILLISECONDS,
                getActivityDetectionPendingIntent()
        ).setResultCallback(this);
    }



    // handle location updates
    public void onLocationChanged(Location loc) {
        mCurrentLatLng = new LatLng(loc.getLatitude(), loc.getLongitude());
        sendLocationChange(loc);
        if (mScheduleList != null && mScheduleList.size() > 0) {
            // check whether condition has been fulfilled
            int length = mScheduleList.size();
            for (int i = 0; i < length; i++){
                Schedule schedule = mScheduleList.get(i);
                Location scheduleLocation = schedule.getLocation();
                double distance = loc.distanceTo(scheduleLocation);
                if (schedule.getArrive()){
                    if (distance < schedule.getRadius()){
                        Log.e("send update", "send update");
                        // send update to MapDisplayActivity
                        sendUpdate(schedule, i);
                        break;
                    }
                }else{
                    if (distance > schedule.getRadius()){
                        Log.e("send update", "send update");
                        // send update to MapDisplayActivity
                        sendUpdate(schedule, i);
                        break;
                    }
                }
            }
        }
    }
}
