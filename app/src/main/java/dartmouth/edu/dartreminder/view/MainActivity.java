package dartmouth.edu.dartreminder.view;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.data.DartReminderDBHelper;
import dartmouth.edu.dartreminder.service.TimeReceiver;
import dartmouth.edu.dartreminder.service.TrackingService;
import dartmouth.edu.dartreminder.utils.Globals;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private final RecentListFragment mRecentListFragment = new RecentListFragment();
    private final UserProfileFragment mUserProfileFragment = new UserProfileFragment();
    private final LocationFragment mLocationFragment = new LocationFragment();

    private DartReminderDBHelper mDartReminderDBHelper;

    // Tracking Service based on time, location, activity
    private TrackingService mTrackingService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //------------------------------------------------------------------------------------------
        // Test for Alarm and Receiver
//        AlarmManager mgrAlarm = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
//        ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
//        long time = System.currentTimeMillis() + 30000;
//
//        for(int i = 0; i < 10; ++i) {
//            Intent intent = new Intent(getApplicationContext(), TimeReceiver.class);
//            intent.putExtra("id", i);
//            // Loop counter `i` is used as a `requestCode`
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), i, intent, 0);
//            // Single alarms in 1, 2, ..., 10 minutes (in `i` minutes)
//            mgrAlarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                    SystemClock.elapsedRealtime() + 30000 * i,
//                    pendingIntent);
//
//            intentArray.add(pendingIntent);
//        }


        // Test for Alarm and Receiver
        //------------------------------------------------------------------------------------------

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        RecentListFragment f1 = new RecentListFragment();
        //LocationFragment f1 = new LocationFragment();
        fragmentTransaction.replace(R.id.main_page, f1, "f1");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sync) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_recent_list) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_page, mRecentListFragment)
                    .commit();
        } else if (id == R.id.nav_user_profile) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_page, mUserProfileFragment)
                    .commit();
        } else if (id == R.id.nav_location_list) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_page, mLocationFragment)
                    .commit();
        } else if (id == R.id.nav_sync) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_sign_out) {
            clearSharedPreferences();
            mDartReminderDBHelper = new DartReminderDBHelper(this);
            mDartReminderDBHelper.deleteTable(Globals.TABLE_NAME_SCHEDULES);
            mDartReminderDBHelper.deleteTable(Globals.TABLE_NAME_CUSTOM_LOCATIONS);
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
}
