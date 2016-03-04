package dartmouth.edu.dartreminder.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.data.DartReminderDBHelper;
import dartmouth.edu.dartreminder.data.Schedule;
//import dartmouth.edu.dartreminder.service.TimeReceiver;
import dartmouth.edu.dartreminder.service.TrackingService;
import dartmouth.edu.dartreminder.utils.Globals;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;

    public static List<Schedule> unCompletedLocationScheduleList;
    public static LatLng currentLatLng;

    // Tracking Service based on location
    private TrackingService mTrackingService;
    private Intent mServiceIntent;
    private ScheduleTriggeredReceiver mScheduleTriggeredReceiver;
    // For keeping if the service bound already
    private boolean mIsBound;

    public static SlidingTabLayout slidingTabLayout;
    public static ViewPager viewPager;

    private ArrayList<Fragment> fragments;
    private ViewPageAdapter mViewPagerAdapter;
    private RecentActivityListFragment recentActivityListFragment;
    private RecentLocationListFragment recentLocationListFragment;
    private RecentTimeListFragment recentTimeListFragment;


    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

//    private final RecentListFragment mRecentListFragment = new RecentListFragment();
    private final RecentTimeListFragment mRecentTimeListFragment = new RecentTimeListFragment();
    private final UserProfileFragment mUserProfileFragment = new UserProfileFragment();
    private final LocationFragment mLocationFragment = new LocationFragment();

    private DartReminderDBHelper mDartReminderDBHelper;

    public class ScheduleTriggeredReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context ctx, Intent intent) {
            Log.e("onReceive","onReceive");
            // obtain the schedule information
            String title = intent.getStringExtra(Globals.SCHEDULE_TITLE);
            String note = intent.getStringExtra(Globals.SCHEDULE_NOTE);
            double lat = intent.getDoubleExtra(Globals.LOCATION_LAT, 0);
            double lng = intent.getDoubleExtra(Globals.LOCATION_LNG, 0);

            // put schedule information into corresponding fields
            scheduleTriggerUpdate(title, note, lat, lng);
        }
    }

    public void scheduleTriggerUpdate(String title, String note, double lat, double lng){
        Intent intent = new Intent(this, LocationDetailActivity.class);
        intent.putExtra(Globals.MSG_LOCATION_ALARM, true);
        intent.putExtra(Globals.LOCATION_TITLE, title);
        intent.putExtra(Globals.LOCATION_DETAIL, note);
        intent.putExtra(Globals.LOCATION_LAT, lat);
        intent.putExtra(Globals.LOCATION_LNG, lng);
        //startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        recentTimeListFragment = new RecentTimeListFragment();
        recentLocationListFragment = new RecentLocationListFragment();
        recentActivityListFragment = new RecentActivityListFragment();


        fragments = new ArrayList<Fragment>();
        fragments.add(recentTimeListFragment);
        fragments.add(recentLocationListFragment);
        fragments.add(recentActivityListFragment);

        mViewPagerAdapter =new ViewPageAdapter(getFragmentManager(),
                fragments);
        viewPager.setAdapter(mViewPagerAdapter);

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();*/

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();*/

        /*RecentListFragment f1 = new RecentListFragment();
        fragmentTransaction.replace(R.id.main_page, f1, "f1");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/


        //service section
        startTrackingService();

        //register schedule triggered receiver
        mScheduleTriggeredReceiver = new ScheduleTriggeredReceiver();
        IntentFilter intentFilter = new IntentFilter(
                ScheduleTriggeredReceiver.class.getName());
        registerReceiver(mScheduleTriggeredReceiver, intentFilter);
        //fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_sync) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_recent_list) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            //TODO:
            /*getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_page, mRecentTimeListFragment)
                    .commit();*/
        } else if (id == R.id.nav_user_profile) {
            slidingTabLayout.setVisibility(View.INVISIBLE);
            viewPager.setVisibility(View.INVISIBLE);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_page, mUserProfileFragment)
                    .commit();
        } else if (id == R.id.nav_location_list) {
            slidingTabLayout.setVisibility(View.INVISIBLE);
            viewPager.setVisibility(View.INVISIBLE);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_page, mLocationFragment)
                    .commit();
        } else if (id == R.id.nav_sync) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_sign_out) {
            slidingTabLayout.setVisibility(View.INVISIBLE);
            viewPager.setVisibility(View.INVISIBLE);
            mDartReminderDBHelper = new DartReminderDBHelper(this);
            clearSharedPreferences();
            clearAllDataTable();
            unregisterReceiver(mScheduleTriggeredReceiver);
            finish();
            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(myIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void clearSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("userProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("USER");
        editor.remove("USERPHONE");
        editor.remove("USERNAME");
        editor.remove("PASSWORD");
        editor.apply();
    }

    //<-- Service Section-->
    // service connection that handles service binding
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        // when tracking service is connected, save the instance of service and
        // exercise entry
        public void onServiceConnected(ComponentName name, IBinder service) {
            // get the binder
            TrackingService.TrackingServiceBinder binder = (TrackingService.TrackingServiceBinder) service;

            // save the service object
            mTrackingService = binder.getService();

            // obtain the reference to the uncompleted location based schedule list
            unCompletedLocationScheduleList = binder.getScheduleList();
            currentLatLng = binder.getCurrentLatLng();
        }

        public void onServiceDisconnected(ComponentName name) {
            // This ONLY gets called when crashed.
            Log.d(Globals.TAG, "Connection disconnected");
            // stopService(mServiceIntent);
            mTrackingService = null;
        }
    };


    private void startTrackingService() {
        mServiceIntent = new Intent(this, TrackingService.class);

        // start the service first
        startService(mServiceIntent);
        // Establish a connection with the service. We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        bindService(mServiceIntent, mServiceConnection,
                Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    private void stopTrackingService() {
        // Stop the service and the notification.
        // ----------------------Skeleton--------------------------
        // Need to check whether the mSensorService is null or not
        // before unbind and stop the service.
        if (mTrackingService != null) {
            doUnbindService();
            stopService(mServiceIntent);
        }
    }

    private void doUnbindService() {
        if (mIsBound) {
            // Double unbind behaves like double free. So check first.
            // Detach our existing connection.
            unbindService(mServiceConnection);
            mIsBound = false;
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();

        if (isFinishing()) {
            stopTrackingService();
            // do stuff
        } else {
            //It's an orientation change.
        }
    }

    private void clearAllDataTable() {
        mDartReminderDBHelper.deleteTable(Globals.TABLE_NAME_SCHEDULES);
        mDartReminderDBHelper.deleteTable(Globals.TABLE_NAME_CUSTOM_LOCATIONS);
    }
}