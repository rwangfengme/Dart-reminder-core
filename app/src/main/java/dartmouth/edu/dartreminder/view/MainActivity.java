package dartmouth.edu.dartreminder.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import java.util.ArrayList;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.service.TrackingService;
import dartmouth.edu.dartreminder.utils.Globals;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;


    private ArrayList<Fragment> fragments;
    private ViewPageAdapter mViewPagerAdapter;
    private RecentActivityListFragment recentActivityListFragment;
    private RecentLocationListFragment recentLocationListFragment;
    private RecentTimeListFragment recentTimeListFragment;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private final RecentTimeListFragment mRecentTimeListFragment = new RecentTimeListFragment();
    private final UserProfileFragment mUserProfileFragment = new UserProfileFragment();
    private final LocationFragment mLocationFragment = new LocationFragment();
    // Tracking Service based on time, location, activity
    private TrackingService mTrackingService;


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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        RecentTimeListFragment recentTimeListFragment = new RecentTimeListFragment();
        //LocationFragment f1 = new LocationFragment();
        fragmentTransaction.replace(R.id.main_page, recentTimeListFragment, "f1");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/

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
        /*if (id == R.id.action_sync) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_recent_list) {
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
            SharedPreferences prefs = getSharedPreferences("userProfile", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("USERNAME");
            editor.remove("PASSWORD");
            editor.apply();
            finish();
            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(myIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
