package dartmouth.edu.dartreminder.view;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.data.CustomLocation;
import dartmouth.edu.dartreminder.data.DartReminderDBHelper;
import dartmouth.edu.dartreminder.utils.Globals;

public class LocationDetailActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private TextView mTitleView;
    private TextView mAddressView;
    private EditText mTitleText;
    private EditText mAddressText;
    private EditText mSearchText;
    private ImageView mSearchIcon;
    private Button mSaveButton;
    private Button mCancelButton;
    private LinearLayout mSearchLayout;
    private GoogleMap mMap;

    private Marker mMarker;
    private LatLng mLatLng;
    private DartReminderDBHelper datasource;

    private boolean registered = false;
    private boolean isAdd = false;
    private boolean isAlarm = false;

    private AddCustomLocationTask addCustomLocationTask;
    private LocationChangedReceiver locationChangedReceiver;

    public class LocationChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context ctx, Intent intent) {
            Log.e("onReceive","onReceive");

            double lat = intent.getDoubleExtra(Globals.LOCATION_LAT, 0);
            double lng = intent.getDoubleExtra(Globals.LOCATION_LNG, 0);

            setMarker(new LatLng(lat, lng));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);

        mTitleView = (TextView) findViewById(R.id.location_title_view);
        mAddressView = (TextView) findViewById(R.id.location_address_view);
        mTitleText = (EditText) findViewById(R.id.location_title_text);
        mAddressText = (EditText) findViewById(R.id.location_address_text);
        mSearchText = (EditText) findViewById(R.id.location_search_text);
        mSearchIcon = (ImageView) findViewById(R.id.location_search_button);
        mSaveButton = (Button) findViewById(R.id.location_save_button);
        mCancelButton = (Button) findViewById(R.id.location_cancel_button);
        mSearchLayout = (LinearLayout) findViewById(R.id.location_search_frame);

        datasource = new DartReminderDBHelper(getApplicationContext());
        locationChangedReceiver = new LocationChangedReceiver();

        setUpMapIfNeeded();
        Intent data = getIntent();

        isAlarm = data.getBooleanExtra(Globals.MSG_LOCATION_ALARM, false);
        if (isAlarm){
            String title = data.getStringExtra(Globals.LOCATION_TITLE);
            String detail = data.getStringExtra(Globals.LOCATION_DETAIL);
            double lat = data.getDoubleExtra(Globals.LOCATION_LAT, 0);
            double lng = data.getDoubleExtra(Globals.LOCATION_LNG, 0);

            mTitleText.setText(title);
            mAddressText.setText(detail);
            mLatLng = new LatLng(lat, lng);
            setMarker(mLatLng);

            mTitleView.setText("Title: ");
            mTitleText.setText("");

            mSaveButton.setVisibility(View.GONE);
            //mCancelButton.setVisibility(View.GONE);
            mSearchLayout.setVisibility(View.GONE);
            mTitleText.setEnabled(false);
            mAddressText.setEnabled(false);
        }else{
            isAdd = data.getBooleanExtra(Globals.ADD_LOCATION, false);
            if (!isAdd){
                String title = data.getStringExtra(Globals.LOCATION_TITLE);
                String detail = data.getStringExtra(Globals.LOCATION_DETAIL);
                double lat = data.getDoubleExtra(Globals.LOCATION_LAT, 0);
                double lng = data.getDoubleExtra(Globals.LOCATION_LNG, 0);
                mTitleText.setText(title);
                mAddressText.setText(detail);
                mLatLng = new LatLng(lat, lng);
                setMarker(mLatLng);

                mSaveButton.setVisibility(View.GONE);
                mCancelButton.setVisibility(View.GONE);
                mSearchLayout.setVisibility(View.GONE);
                mTitleText.setEnabled(false);
                mAddressText.setEnabled(false);
            }else{
                IntentFilter intentFilter = new IntentFilter(
                        LocationChangedReceiver.class.getName());
                registerReceiver(locationChangedReceiver, intentFilter);
                registered = true;

                LocationManager locationManager;
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                final Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setSpeedRequired(false);
                criteria.setCostAllowed(true);
                String provider = locationManager.getBestProvider(criteria, true);

                Location location;
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
                location = locationManager.getLastKnownLocation(provider);
                mLatLng = location2Latlng(location);
                setMarker(mLatLng);
                mAddressText.setText(fromLatLng2Address(mLatLng));
            }
        }

        mSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (registered){
                    unregisterReceiver(locationChangedReceiver);
                    registered = false;
                }

                String location = mSearchText.getText().toString();
                List<Address> addressList = null;
                if (location != null && !location.isEmpty()) {
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (addressList != null && addressList.size() > 0) {
                    Address address = addressList.get(0);
                    mMap.clear();
                    mLatLng = new LatLng(address.getLatitude(), address.getLongitude());
                    setMarker(mLatLng);
                    mSearchText.setText("");
                    mAddressText.setText(fromAddressToString(address));
                } else {
                    Toast.makeText(getApplicationContext(), "No Location Found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitleText.getText().toString();
                String address = mAddressText.getText().toString();
                if (title == null){
                    Toast.makeText(getApplicationContext(), "No Location Found", Toast.LENGTH_SHORT).show();
                }else if (address == null){
                    Toast.makeText(getApplicationContext(), "No Location Found", Toast.LENGTH_SHORT).show();
                }else if (mLatLng == null){
                    Toast.makeText(getApplicationContext(), "No Location Found", Toast.LENGTH_SHORT).show();
                }else{
                    CustomLocation customLocation = new CustomLocation(title);
                    customLocation.setTitle(title);
                    customLocation.setDetail(address);
                    customLocation.setLatitude(mLatLng.latitude);
                    customLocation.setLongtitude(mLatLng.longitude);
                    customLocation.setIcon(R.drawable.ic_add_location_black_48dp);

                    addCustomLocationTask = new AddCustomLocationTask();
                    addCustomLocationTask.execute(customLocation);
                    finish();
                }
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isAlarm)
                    Toast.makeText(getApplicationContext(), "Location Entry Discarded.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onDestroy(){
        if (registered){
            unregisterReceiver(locationChangedReceiver);
        }
        super.onDestroy();
    }


    private String fromAddressToString(Address address){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < address.getMaxAddressLineIndex(); i++){
            String addressLine = address.getAddressLine(i);
            if (addressLine != null && !addressLine.isEmpty())
                sb.append(address.getAddressLine(i)).append(", ");
        }
        sb.append(address.getCountryName());
        return sb.toString();
    }

    private LatLng location2Latlng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    private void setMarker(LatLng latLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,
                17));

        if (mMarker != null){
            mMarker.remove();
            mMarker = null;
        }

        mMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .draggable(false));
    }

    private String fromLatLng2Address(LatLng latLng){
        String addressString = "No address found";
        Geocoder gc = new Geocoder(this, Locale.getDefault());

        double latitude = latLng.latitude;
        double longitude = latLng.longitude;

        if (!Geocoder.isPresent())
            addressString = "No geocoder available";
        else {
            try {
                List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
                StringBuilder sb = new StringBuilder();
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);

                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                        sb.append(address.getAddressLine(i)).append(", ");

                    sb.append(address.getCountryName());
                }
                addressString = sb.toString();
            } catch (IOException e) {
                Log.d("DartReminder", "IO Exception", e);
            }
        }
        return addressString;
    }

    //<-- Map Related Area -->
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (isAdd){
            mLatLng = latLng;
            setMarker(latLng);
            mAddressText.setText(fromLatLng2Address(latLng));
            if (registered){
                registered = false;
                unregisterReceiver(locationChangedReceiver);
            }
        }
    }


    //Asynctask used to add entry
    private class AddCustomLocationTask extends AsyncTask<CustomLocation, CustomLocation, Long> {
        //save the data to the database
        @Override
        public Long doInBackground(CustomLocation... customLocations) {
            Log.d(Globals.TAG, "save, do in background");
            long id = datasource.insertCustomLocation(customLocations[0]);
            customLocations[0].setId(id);
            publishProgress(customLocations[0]);
            return id;
        }

        //update the adapter of the listview in the history tab
        @Override
        public void onProgressUpdate(CustomLocation... customLocations) {
            LocationFragment.gridViewAdapter.add(customLocations[0]);
        }

        //toast the message notifying users that entry has been saved.
        @Override
        public void onPostExecute(Long id) {
            Toast.makeText(getApplicationContext(), "Entry #" + id + " saved.", Toast.LENGTH_SHORT).show();
            addCustomLocationTask = null;
        }
    }
}
