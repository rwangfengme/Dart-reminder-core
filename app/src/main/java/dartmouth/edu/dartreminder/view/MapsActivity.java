package dartmouth.edu.dartreminder.view;

import android.Manifest;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.data.CustomLocation;
import dartmouth.edu.dartreminder.utils.CustomLocationLoader;
import dartmouth.edu.dartreminder.utils.Globals;
import dartmouth.edu.dartreminder.utils.LocationGridViewAdapter;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
        LoaderManager.LoaderCallbacks<ArrayList<CustomLocation>>{

    private GoogleMap mMap;
    private ListView mLocationList;
    private ImageView mImageView;
    private EditText mSearchText;
    private Button mSaveButton;
    private Button mCancelButton;
    private RadioButton mArrive;
    private RadioButton mLeave;
    private TextView mRadiusText;

    private Marker mCenterMarker;
    private Marker mSelectMarker;
    private Polyline mPolyLine;
    private Circle mCircle;

    private final double DEFAULT_RADIUS = 50.0;

    private String locationName = Globals.DEFAULT_LOCATION_NAME;
    private double lat;
    private double lng;
    private double radius = DEFAULT_RADIUS;

    private LatLng currentLatlng;

    private List<CustomLocation> mCustomLocationList;
    public static LocationAdapter locationListAdapter;
    private LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mImageView = (ImageView) findViewById(R.id.map_search_button);
        mSearchText = (EditText) findViewById(R.id.map_search_text);
        mSaveButton = (Button) findViewById(R.id.map_save_button);
        mCancelButton = (Button) findViewById(R.id.map_cancel_button);
        mArrive = (RadioButton) findViewById(R.id.map_arrive_radio);
        mLeave = (RadioButton) findViewById(R.id.map_leave_radio);
        mRadiusText = (TextView) findViewById(R.id.map_radius_text);

        mArrive.setChecked(true);
        mRadiusText.setText("Radius: " + Math.round(radius) + " M");

        setUpMapIfNeeded();

        mLocationList = (ListView) findViewById(R.id.locationList);

        //add current location to service
        loaderManager = getLoaderManager();
        loaderManager.initLoader(1, null, this).forceLoad();

        mLocationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mMap.clear();
                CustomLocation locationEntry = mCustomLocationList.get(position);
                mSearchText.setText(locationEntry.getTitle());
                if (position == 0){
                    mLeave.setChecked(true);
                    mArrive.setEnabled(false);
                }else{
                    mArrive.setEnabled(true);
                    mArrive.setChecked(true);
                    locationName = locationEntry.getTitle();
                }
                updateMap(locationEntry.getLatitude(), locationEntry.getLongitude(), true);
            }
        });


        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = mSearchText.getText().toString();
                CustomLocation customLocation = locationListAdapter.containsLocation(location);
                if (customLocation != null) {
                    mMap.clear();
                    updateMap(customLocation.getLatitude(), customLocation.getLongitude(), true);
                    mSearchText.setText(customLocation.getTitle());
                    locationName = customLocation.getTitle();
                } else {
                    List<Address> addressList = null;
                    if (location != null && !location.isEmpty()) {
                        Geocoder geocoder = new Geocoder(getApplicationContext());
                        try {
                            addressList = geocoder.getFromLocationName(location, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (addressList != null) {
                        Address address = addressList.get(0);
                        mMap.clear();
                        updateMap(address.getLatitude(), address.getLongitude(), true);
                        mSearchText.setText(fromAddressToString(address));
                    } else {
                        Toast.makeText(getApplicationContext(), "No Location Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra(Globals.SAVE, true);
                i.putExtra(Globals.LOCATION_NAME, locationName);

                if (mArrive.isChecked()){
                    i.putExtra(Globals.ARRIVE, true);
                }else{
                    i.putExtra(Globals.ARRIVE, false);
                }

                i.putExtra(Globals.RADIUS, radius);
                i.putExtra(Globals.LAT, lat);
                i.putExtra(Globals.LNG, lng);
                setResult(RESULT_OK, i);
                finish();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra(Globals.SAVE, false);
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

    //<-- Location Related Area -->
    public String fromAddressToString(Address address){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < address.getMaxAddressLineIndex(); i++){
            String addressLine = address.getAddressLine(i);
            if (addressLine != null && !addressLine.isEmpty())
                sb.append(address.getAddressLine(i)).append(", ");
        }
        sb.append(address.getCountryName());
        return sb.toString();
    }

    public static LatLng fromLocationToLatLng(Location location){
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    private void getLocationAddress(Location location, CustomLocation customLocation) {
        String latLongString = "No location found";
        String addressString = "No address found";

        if (location != null) {
            // Update the map location.
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            customLocation.setLatitude(lat);
            customLocation.setLongtitude(lng);
            latLongString = "Lat:" + lat + "\nLong:" + lng;

            Geocoder gc = new Geocoder(this, Locale.getDefault());

            if (!Geocoder.isPresent())
                addressString = latLongString;
            else {
                try {
                    List<Address> addresses = gc.getFromLocation(lat, lng, 1);
                    StringBuilder sb = new StringBuilder();
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);

                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++){
                            String addressLine = address.getAddressLine(i);
                            if (addressLine != null && !addressLine.isEmpty())
                                sb.append(address.getAddressLine(i)).append(", ");
                        }
//
//                        sb.append(address.getLocality()).append(", ");
//                        sb.append(address.getPostalCode()).append(", ");
                        sb.append(address.getCountryName());
                    }
                    addressString = sb.toString();
                } catch (IOException e) {
                    Log.d("Location", "IO Exception", e);
                }
            }
            customLocation.setDetail(addressString);
        }
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

    private void updateMap(double lat, double lng, boolean isCenter){
        LatLng latlng = new LatLng(lat, lng);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,
                17));
        if (isCenter){
            mCenterMarker = mMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .draggable(false));
            radius = DEFAULT_RADIUS;
            mRadiusText.setText("Radius: " + Math.round(radius) + " M");
            drawCircle(radius);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (mCenterMarker == null){
            mMap.clear();
            radius = DEFAULT_RADIUS;
            mRadiusText.setText("Radius: " + Math.round(radius) + " M");
            mCenterMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .draggable(false));

            drawCircle(DEFAULT_RADIUS);

            lat = latLng.latitude;
            lng = latLng.longitude;

        }else{
            if(mSelectMarker != null){
                mSelectMarker.remove();
                mSelectMarker = null;
            }

            mSelectMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .draggable(true));

            if (mPolyLine != null) {
                mPolyLine.remove();
                mPolyLine = null;
            }

            mPolyLine = mMap.addPolyline(new PolylineOptions()
                    .add(mCenterMarker.getPosition(), mSelectMarker.getPosition())
                    .width(5)
                    .color(Color.BLACK));

//        radius = distance(mCenterMarker.getPosition().latitude, mCenterMarker.getPosition().longitude,
//                mSelectMarker.getPosition().latitude, mSelectMarker.getPosition().longitude);

            Location center = latLng2Location(mCenterMarker.getPosition());
            Location bound = latLng2Location(mSelectMarker.getPosition());
            radius = center.distanceTo(bound);

            mRadiusText.setText("Radius: " + Math.round(radius) + " M");
            mSelectMarker.setTitle(Math.round(radius) + " M");

            drawCircle(radius);
        }
    }

    public Location latLng2Location(LatLng latLng){
        Location location = new Location("");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        return location;
    }

    public void drawCircle(Double radius){
        if (mCircle != null){
            mCircle.remove();
            mCircle = null;
        }
        mCircle = mMap.addCircle(new CircleOptions()
                .center(mCenterMarker.getPosition())
                .radius(radius)
                .strokeColor(Color.RED)
                .strokeWidth(5));
    }

    @Override
    public Loader<ArrayList<CustomLocation>> onCreateLoader(int id, Bundle args) {
        return new CustomLocationLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<CustomLocation>> loader, ArrayList<CustomLocation> data) {
        mCustomLocationList = data;

        Location location;
        if (MainActivity.currentLatLng == null){
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
            currentLatlng = fromLocationToLatLng(location);
            lat = location.getLatitude();
            lng = location.getLongitude();
        }else{
            location = new Location("");
            currentLatlng = MainActivity.currentLatLng;
            lat = currentLatlng.latitude;
            lng = currentLatlng.longitude;
            location.setLongitude(currentLatlng.longitude);
            location.setLatitude(currentLatlng.latitude);
        }

        final CustomLocation currentLocation = new CustomLocation("Current Location", R.drawable.arraw_icon);
        getLocationAddress(location, currentLocation);

        if (mCustomLocationList == null){
            mCustomLocationList = new ArrayList<>();
        }
        mCustomLocationList.add(0, currentLocation);

        locationListAdapter = new LocationAdapter(this, mCustomLocationList);
        mLocationList.setAdapter(locationListAdapter);
        Log.d("TAGG", "Load Finished");
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<CustomLocation>> loader) {
        locationListAdapter = new LocationAdapter(this, mCustomLocationList);
        mLocationList.setAdapter(locationListAdapter);
    }


    //<-- Location Adapter Class -->
    // Subclass of ArrayAdapter to display interpreted database row values in
    // customized list view.
    private class LocationAdapter extends ArrayAdapter<CustomLocation> {
        List<CustomLocation> locations;
        public LocationAdapter(Context context, List<CustomLocation> locations) {
            // set layout to show two lines for each item
            super(context, R.layout.location_item_layout, locations);
            this.locations = locations;
        }

        public CustomLocation containsLocation(String location){
            CustomLocation result = null;
            String locationLowerCase = location.toLowerCase();
            for (CustomLocation customLocation: locations){
                String custom = customLocation.getTitle().toLowerCase();

                if (custom.equals(locationLowerCase)){
                    return customLocation;
                }else{
                    if (custom.contains(location)){
                        result = customLocation;
                    }
                }
            }
            return result;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View listItemView = convertView;
            if (null == convertView) {
                // we need to check if the convertView is null. If it's null,
                // then inflate it.
                listItemView = inflater.inflate(
                        R.layout.location_item_layout, parent, false);
            }

            // Setting up view's text1 is main title, text2 is detail.
            TextView titleView = (TextView) listItemView
                    .findViewById(R.id.location_line1);
            TextView detailView = (TextView) listItemView
                    .findViewById(R.id.location_line2);
            ImageView imageView = (ImageView) listItemView.
                    findViewById(R.id.map_icon);

            // get the corresponding Location
            CustomLocation entry = getItem(position);

            // put data into corresponding wedges
            titleView.setText(entry.getTitle());
            if (entry.hasDetail()){
                detailView.setText(entry.getDetail());
            }
            if (position == 0){
                imageView.setImageResource(R.drawable.ic_room_black_48dp);
            }else{
                imageView.setImageResource(R.drawable.ic_add_location_black_48dp);
            }

            return listItemView;
        }

    }
}
