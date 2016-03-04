//package dartmouth.edu.dartreminder.utils;
//
//import android.Manifest;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.os.Binder;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.support.v4.app.ActivityCompat;
//import android.util.Log;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//
//import java.util.List;
//
//import dartmouth.edu.dartreminder.data.Schedule;
//
///**
// * Created by LeoZhu on 3/3/16.
// */
//
//public class TrackingService extends Service implements
//        GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener, LocationListener {
//
//    public static final String EXTRA_MESSENGER = "EXTRA_MESSENGER";
//    public static final String EXTRA_TRACKING = "EXTRA_TRACKING";
//    public static final String MSG_ENTITY_UPDATE = "update";
//
//    private int mInputType;
//
//    // A request to connect to Location Services
//    private LocationRequest mLocationRequest;
//
//    // Stores the current instantiation of the location client in this object
//    private GoogleApiClient mGoogleApiClient;
//
//    // Set up binder for the TrackingService using IBinder
//    private final IBinder binder = new TrackingServiceBinder();
//
//    private List<Schedule> mScheduleList;
//
//    private NotificationManager mNotificationManager;
//
//    // service started flag
//    private boolean mIsStarted;
//
//    // set up the MyRunsBinder
//    public class TrackingServiceBinder extends Binder {
//        public List<Schedule> getExerciseEntry() {
//            return mScheduleList;
//        }
//
//        TrackingService getService() {
//            return TrackingService.this;
//        }
//
//    }
//
//    @Override
//    public void onCreate() {
//        mIsStarted = false;
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//
//        super.onCreate();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        start(intent);
//        return START_NOT_STICKY;
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        start(intent);
//        return binder;
//    }
//
//    @Override
//    public void onDestroy() {
//        // Disconnecting the client invalidates it.
//        mGoogleApiClient.disconnect();
//        mNotificationManager.cancelAll();
//        mIsStarted = false;
//        Log.d(Globals.TAG, "Service Destroyed");
//        super.onDestroy();
//    }
//
//    // start tracking
//    private void start(Intent intent) {
//        if (mIsStarted) {
//            return;
//        }
//        mIsStarted = true;
//
//        // notification
//        setupNotification();
//
//        // Connect the client.
//        mGoogleApiClient.connect();
//
//        // activity recognize
//        // If it's automatic mode, registering motion sensor for activity
//        // recognition.
//        initExerciseEntry(intent.getExtras().getInt(Globals.KEY_ACTIVITY_TYPE));
//        if (mEntry.getActivityType() == -1) {
//            mInputType = Globals.INPUT_TYPE_AUTOMATIC;
//        }
//    }
//
//    // init the data structure
//    private void initExerciseEntry(int activityType) {
//        mEntry = new ExerciseEntry();
//        mEntry.setActivityType(activityType);
//
//        if (activityType == -1) {
//            mEntry.setInputType(Globals.INPUT_TYPE_AUTOMATIC);
//        } else {
//            mEntry.setInputType(Globals.INPUT_TYPE_GPS);
//        }
//    }
//
//    // send update to the activity
//    private void sendUpdate() {
//        Intent intent = new Intent(
//                MapDisplayActivity.EntityUpdateReceiver.class.getName());
//        intent.putExtra(MSG_ENTITY_UPDATE, true);
//        this.sendBroadcast(intent);
//
//    }
//
//    private void setupNotification() {
//        // ----------------------Skeleton--------------------------
//        // Setup the intent to fire MapDisplayActivity for the PendingIntent
//        Intent i = new Intent(this, MapDisplayActivity.class);
//
//        // ----------------------Skeleton--------------------------
//        // Set flags to avoid re-invent activity.
//        // http://developer.android.com/guide/topics/manifest/activity-element.html#lmode
//        // IMPORTANT!. no re-create activity
//        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        // ----------------------Skeleton--------------------------
//        // Using pending intent to bring back the MapActivity from notification
//        // center.
//        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
//
//        // ----------------------Skeleton--------------------------
//
//        // Use NotificationManager to build notification(icon, content, title,
//        // flag and pIntent)
//
//        Notification notification = new Notification.Builder(this)
//                .setContentTitle(
//                        getString(R.string.ui_maps_display_notification_title))
//                .setContentText(
//                        getString(R.string.ui_maps_display_notification_content))
//                .setSmallIcon(R.drawable.icon).setContentIntent(pi).build();
//        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notification.flags = notification.flags
//                | Notification.FLAG_ONGOING_EVENT;
//
//        mNotificationManager.notify(0, notification);
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        Log.i(Globals.TAG, "GoogleApiClient connection has been suspend");
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        Log.i(Globals.TAG, "GoogleApiClient connection has failed");
//    }
//
//    // start location update when location service is connected
//    public void onConnected(Bundle arg0) {
//        Log.e("onConnected", "onConnected");
//        mLocationRequest = LocationRequest.create();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(1000); // Update location every second
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(
//                mGoogleApiClient, mLocationRequest, this);
//    }
//
//
//
//    // handle location updates
//    public void onLocationChanged(Location loc) {
//        if (mScheduleList != null) {
//            // insert into ExerciseEntry
//            mEntry.insertLocation(loc);
//            Log.e("send update", "send update");
//            // send update to MapDisplayActivity
//            sendUpdate();
//        }
//
//    }
//
//}
