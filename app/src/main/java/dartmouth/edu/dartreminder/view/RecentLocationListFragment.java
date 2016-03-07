package dartmouth.edu.dartreminder.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import dartmouth.edu.dartreminder.R;
import dartmouth.edu.dartreminder.data.DartReminderDBHelper;
import dartmouth.edu.dartreminder.data.Schedule;
import dartmouth.edu.dartreminder.utils.MarkerWindowAdapter;

/**
 * A fragment that launches other parts of the demo application.
 */
public class RecentLocationListFragment extends Fragment implements GoogleMap.OnMarkerClickListener{

    private DartReminderDBHelper mScheduleDBHelper;
    private Context mContext;
    private GetAllLocBasedTask task = null;
    private ArrayList LocationList;

    MapView mMapView;
    private GoogleMap googleMap;
    private TextView historyTitle;
    private TextView historyNote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_recent_location, container,
                false);
        mContext = getActivity();

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDeepRed)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NewScheduleActivity.class));
            }
        });

        historyTitle = (TextView) v.findViewById(R.id.TextView_title_from_recent_history);
        historyNote = (TextView) v.findViewById(R.id.TextView_notes_from_recent_history);

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        // latitude and longitude


        // Perform any camera updates here
        return v;
    }

    public void setUpLocation(ArrayList locArr){
        if (googleMap != null) {
            MarkerWindowAdapter markerWindowAdapter = new MarkerWindowAdapter(locArr, mContext);
            googleMap.setInfoWindowAdapter(markerWindowAdapter);
            googleMap.setOnMarkerClickListener(this);
            for(int i=locArr.size()-1; i >=0 ; i--){
                Schedule singleSchedule = (Schedule) locArr.get(i);
                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(singleSchedule.getLat(), singleSchedule.getLng())).title(i+"");


                // Changing marker icon
                marker.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED));

                // adding marker
                Marker addedMarker = googleMap.addMarker(marker);

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(singleSchedule.getLat(), singleSchedule.getLng())).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
            }

            // create marker

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mScheduleDBHelper = new DartReminderDBHelper(getActivity());
        task = new GetAllLocBasedTask();
        task.execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Schedule schedule = (Schedule)LocationList.get(Integer.parseInt(marker.getTitle()));
        historyTitle.setText("Title: " + schedule.getTitle());
        historyNote.setText("Note: " + schedule.getNotes());
        marker.showInfoWindow();
        return true;
    }

    class GetAllLocBasedTask extends AsyncTask<Void, String, ArrayList> {

        @Override
        protected ArrayList doInBackground(Void... unused) {
            ArrayList<Schedule> allSchedule = mScheduleDBHelper.fetchSchedulesByLocation();

            return allSchedule;
        }

        @Override
        protected void onProgressUpdate(String... name) {
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            LocationList = arrayList;
            setUpLocation(arrayList);
        }
    }
}