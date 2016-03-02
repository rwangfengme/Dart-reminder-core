package dartmouth.edu.dartreminder.view;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.data.CustomLocation;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ListView mLocationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setUpMapIfNeeded();

        LocationManager locationManager;
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        String provider = locationManager.getBestProvider(criteria, true);

        final Location location = locationManager.getLastKnownLocation(provider);

        mLocationList = (ListView) findViewById(R.id.locationList);

        //add current location to service
        final List<CustomLocation> locationList = new ArrayList<>();
        CustomLocation currentLocaiton = new CustomLocation("Current Location");
        getLocationAddress(location, currentLocaiton);
        locationList.add(currentLocaiton);

         locationList.add(new CustomLocation("Location", 0, 0, "location 2"));
        LocationAdapter locationListAdapter = new LocationAdapter(this, locationList);
        mLocationList.setAdapter(locationListAdapter);

        mLocationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                CustomLocation locationEntry = locationList.get(position);
                updateMap(locationEntry.getLatitude(), locationEntry.getLongitude());
            }
        });
    }

    //<-- Location Related Area -->
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
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(android.os.Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
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

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private void updateMap(double lat, double lng){
        LatLng latlng = new LatLng(lat, lng);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,
                17));
        mMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_RED)));
    }



    //<-- Location Adapter Class -->
    // Subclass of ArrayAdapter to display interpreted database row values in
    // customized list view.
    private class LocationAdapter extends ArrayAdapter<CustomLocation> {
        public LocationAdapter(Context context, List<CustomLocation> locations) {
            // set layout to show two lines for each item
            super(context, R.layout.location_item_layout, locations);
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
                imageView.setImageResource(R.drawable.arraw_icon);
            }else{
                imageView.setImageResource(R.drawable.mark_icon);
            }

            return listItemView;
        }

    }
}
